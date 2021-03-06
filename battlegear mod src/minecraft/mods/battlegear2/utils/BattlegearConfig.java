package mods.battlegear2.utils;

import mods.battlegear2.Battlegear;
import mods.battlegear2.inventory.CreativeTabMB_B_2;
import mods.battlegear2.items.*;
import mods.battlegear2.recipies.DyeRecipie;
import mods.battlegear2.recipies.QuiverRecipie2;
import mods.battlegear2.recipies.ShieldRemoveArrowRecipie;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

import java.util.Arrays;

public class BattlegearConfig {
	public static final CreativeTabs customTab=new CreativeTabMB_B_2("Battlegear2");
	public static boolean forceBackSheath = false;
	public static final String[] itemNames = new String[] {"heraldric","chain","quiver", "dagger","waraxe","mace","spear","shield","knight.armour", "mb.arrow"};
	public static final String[] toolTypes = new String[] {"wood", "stone", "iron", "diamond", "gold"};
    public static final String[] shieldTypes = new String[] {"wood", "hide", "iron", "diamond", "gold"};
	public static final String[] armourTypes = new String[] {"helmet", "plate", "legs", "boots"};
	public static final int firstDefaultItemIndex = 26201;
	public static int[] itemOffests = new int[]{0, 1, 2, 5, 10, 15, 20, 25, 30, 35};
	
	public static ItemWeapon[] dagger=new ItemWeapon[5],warAxe=new ItemWeapon[5],mace=new ItemWeapon[5],spear=new ItemWeapon[5];
    public static ItemShield[] shield=new ItemShield[5];
	public static Item chain,quiver,heradricItem, MbArrows;
	public static Block banner;
	public static ItemBlock bannerItem;
	public static ItemArmor[] knightArmor=new ItemArmor[4];

    public static String[] disabledRecipies = new String[0];


    public static double[] skeletonArrowSpawnRate = new double[ItemMBArrow.names.length];
	
	
	public static void getConfig(Configuration config) {
		//System.out.println("Config");
		config.load();

        heradricItem = new HeraldryCrest(config.get(config.CATEGORY_ITEM, itemNames[0], firstDefaultItemIndex).getInt());

        chain = new Item(config.get(config.CATEGORY_ITEM, itemNames[1], firstDefaultItemIndex+itemOffests[1]).getInt());
    	chain.setUnlocalizedName("battlegear2:"+itemNames[1]).func_111206_d("battlegear2:"+itemNames[1]).setCreativeTab(customTab);
        	
        forceBackSheath=config.get(config.CATEGORY_GENERAL, "Force Back Sheath", false).getBoolean(false);

        if(Battlegear.debug){
            quiver = new ItemQuiver2(config.get(config.CATEGORY_ITEM, itemNames[2], firstDefaultItemIndex+2).getInt());
            quiver.setUnlocalizedName("battlegear2:"+itemNames[2]).func_111206_d("battlegear2:quiver/"+itemNames[2]).setCreativeTab(customTab);

            MbArrows = new ItemMBArrow(config.get(config.CATEGORY_ITEM, itemNames[9], firstDefaultItemIndex+itemOffests[9]).getInt());
            MbArrows.setUnlocalizedName("battlegear2:" + itemNames[9]).func_111206_d("battlegear2:" + itemNames[9]).setCreativeTab(customTab);

            String customArrowSpawn = "Skeleton CustomArrow Spawn Rate";
            config.addCustomCategoryComment(customArrowSpawn, "The spawn rate (between 0 & 1) that Skeletons will spawn with Arrows provided from this mod");

            //default 10% for everything but ender (which is 0%)
            for(int i = 0; i < ItemMBArrow.names.length; i++){
                skeletonArrowSpawnRate[i] = config.get(customArrowSpawn, ItemMBArrow.names[i], i!=1?0.1F:0).getDouble(i!=1?0.1F:0);
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("This will disable the crafting recipie for the provided item/blocks.\n");
        sb.append("It should be noted that this WILL NOT remove the item from the game, it will only disable the recipe.\n");
        sb.append("In this way the items may still be obtained through creative mode and cheats, but playes will be unable to craft them.\n");
        sb.append("These should all placed on separate lines between the provide \'<\' and \'>\'. The valid values are: \n");

        int count = 0;
        for(int i = 1; i < itemNames.length; i++){

            if(i != 9){
                sb.append(itemNames[i]);
                sb.append(", ");
                count++;
                if(count % 5 == 0){
                    sb.append("\n");
                }
            }
        }

        for(int i = 0; i < ItemMBArrow.names.length; i++){
            sb.append(itemNames[9]);
            sb.append('.');
            sb.append(ItemMBArrow.names[i]);
            sb.append(", ");
            count++;
            if(count % 5 == 0){
                sb.append("\n");
            }
        }

        int last_comma = sb.lastIndexOf(",");
        if(last_comma > 0){
            sb.deleteCharAt(last_comma);
        }


        disabledRecipies = config.get(config.CATEGORY_GENERAL, "Disabled Recipies", new String[0], sb.toString()).getStringList();
        Arrays.sort(disabledRecipies);


        
        for(int i = 0; i < 5; i++){
        	EnumToolMaterial material = EnumToolMaterial.values()[i];

            warAxe[i]=new ItemWaraxe(
                    config.get(config.CATEGORY_ITEM, itemNames[4]+toolTypes[i], firstDefaultItemIndex+itemOffests[4]+i).getInt(),
                    material, itemNames[4], i==4?2:1);

        	dagger[i]=new ItemDagger(
        			config.get(config.CATEGORY_ITEM, itemNames[3]+"_"+toolTypes[i], firstDefaultItemIndex+itemOffests[3]+i).getInt(),
        			material, itemNames[3]);

    		mace[i]=new ItemMace(
    				config.get(config.CATEGORY_ITEM, itemNames[5]+toolTypes[i], firstDefaultItemIndex+itemOffests[5]+i).getInt(),
    				material, itemNames[5], 0.05F + 0.05F*i);
    		spear[i]=new ItemSpear(
    				config.get(config.CATEGORY_ITEM, itemNames[6]+toolTypes[i], firstDefaultItemIndex+itemOffests[6]+i).getInt(),
    				material, itemNames[6]);


            shield[i] = new ItemShield(
                    config.get(config.CATEGORY_ITEM, itemNames[7]+shieldTypes[i], firstDefaultItemIndex+itemOffests[7]+i).getInt(),
                    EnumShield.values()[i]
            );


        }
        //validWeaponsID=config.get(config.CATEGORY_GENERAL, "Valid Weapon IDs",new int[]{11,12,16,20,27}).getIntList();
        if (config.hasChanged())
        {        
      	  config.save();     	
        }
	}

	public static void registerRecipes() {
		
		//2 Iron ingots = 3 chain. This is because the chain armour has increased in damage resistance

        if(Arrays.binarySearch(disabledRecipies, itemNames[1])  < 0)
            GameRegistry.addShapedRecipe(new ItemStack(chain, 3), new Object[]{
                "I", "I", Character.valueOf('I'), Item.ingotIron
            });

        if(Battlegear.debug){
            //Quiver recipes :
            if(Arrays.binarySearch(disabledRecipies, itemNames[2])  < 0)
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(quiver), new Object[]
                    {"X X", "X X","XXX",Character.valueOf('X'), Item.leather}));

            GameRegistry.addRecipe(new QuiverRecipie2());

        }

        GameRegistry.addRecipe(new DyeRecipie());

        if(Arrays.binarySearch(disabledRecipies, "chain.armour")  < 0){
            //Chain armor recipes
            GameRegistry.addRecipe(new ItemStack(Item.helmetChain),  new Object[]
                    {"LLL","L L",Character.valueOf('L'),chain});
            GameRegistry.addRecipe(new ItemStack(Item.plateChain),  new Object[]
                    {"L L","LLL","LLL",Character.valueOf('L'),chain});
            GameRegistry.addRecipe(new ItemStack(Item.legsChain),  new Object[]
                    {"LLL","L L","L L",Character.valueOf('L'),chain});
            GameRegistry.addRecipe(new ItemStack(Item.bootsChain),  new Object[]
                    {"L L","L L",Character.valueOf('L'),chain});
        }
		//Weapon recipes
		ItemStack woodStack = new ItemStack(Block.planks.blockID,1,OreDictionary.WILDCARD_VALUE);
		for(int i = 0; i < 5; i++){
			Item craftingMaterial = Item.itemsList[EnumToolMaterial.values()[i].getToolCraftingMaterial()];
            if(Arrays.binarySearch(disabledRecipies, itemNames[4])  < 0){
                GameRegistry.addRecipe(
                        new ItemStack(warAxe[i]),
                        new Object[] {"L L","LSL"," S ",
                            Character.valueOf('S'), Item.stick,
                            Character.valueOf('L'),
                            i!=0?craftingMaterial:woodStack
                    });
            }
            if(Arrays.binarySearch(disabledRecipies, itemNames[5])  < 0) {
                GameRegistry.addRecipe(
                        new ItemStack(mace[i]),
                        new Object[] {" LL"," LL","S  ",
                                Character.valueOf('S'), Item.stick,
                                Character.valueOf('L'),
                                i!=0?craftingMaterial:woodStack
                        });
            }
            if(Arrays.binarySearch(disabledRecipies, itemNames[3])  < 0){
                GameRegistry.addRecipe(
                        new ItemStack(dagger[i]),
                        new Object[] {"L","S",
                                Character.valueOf('S'), Item.stick,
                                Character.valueOf('L'),
                                i!=0?craftingMaterial:woodStack
                        });
            }

            if(Arrays.binarySearch(disabledRecipies, itemNames[6])  < 0){
                if(i == 0){
                    GameRegistry.addRecipe(
                            new ItemStack(spear[i]),
                            new Object[] {"  S"," S ","S  ",
                                    Character.valueOf('S'), Item.stick
                            });
                }else{
                    GameRegistry.addRecipe(
                            new ItemStack(spear[i]),
                            new Object[] {" L","S ",
                                    Character.valueOf('S'), spear[0],
                                    Character.valueOf('L'), craftingMaterial
                            });
                }
            }
		}

        if(Arrays.binarySearch(disabledRecipies, itemNames[7])  < 0){
            //Wood Shield
            GameRegistry.addRecipe(new ItemStack(shield[0]),
                    new Object[] {" W ","WWW", " W ",
                            Character.valueOf('W'), woodStack
                    });

            //Hide Shield
            GameRegistry.addRecipe(new ItemStack(shield[1]),
                    new Object[] {" H ","HWH", " H ",
                            Character.valueOf('W'), woodStack,
                            Character.valueOf('H'), Item.leather
                    });
            //Iron Shield
            GameRegistry.addRecipe(new ItemStack(shield[2]),
                    new Object[] {"I I","IWI", " I ",
                            Character.valueOf('W'), woodStack,
                            Character.valueOf('I'), Item.ingotIron
                    });
            //Diamond Shield
            GameRegistry.addRecipe(new ItemStack(shield[3]),
                    new Object[] {"I I","IWI", " I ",
                            Character.valueOf('W'), woodStack,
                            Character.valueOf('I'), Item.diamond
                    });
            //Gold Shield
            GameRegistry.addRecipe(new ItemStack(shield[4]),
                    new Object[] {"I I","IWI", " I ",
                            Character.valueOf('W'), woodStack,
                            Character.valueOf('I'), Item.ingotGold
                    });
        }


        GameRegistry.addRecipe(new ShieldRemoveArrowRecipie());

        if(Battlegear.debug){
            //Exploding Arrows
            if(Arrays.binarySearch(disabledRecipies, itemNames[9]+"."+ItemMBArrow.names[0]) < 0){
                GameRegistry.addRecipe(new ItemStack(MbArrows, 1, 0),
                        new Object[] {"G","A",
                                Character.valueOf('G'), Item.gunpowder,
                                Character.valueOf('A'), Item.arrow
                        });
            }

            //Ender Arrows
            if(Arrays.binarySearch(disabledRecipies, itemNames[9]+"."+ItemMBArrow.names[1]) < 0){
                GameRegistry.addRecipe(new ItemStack(MbArrows, 1, 0),
                        new Object[] {"E","A",
                                Character.valueOf('E'), Item.enderPearl,
                                Character.valueOf('A'), Item.arrow
                        });
            }
        }




		
		for(int i = 0; i < 4; i++){
			//GameRegistry.addRecipe(new KnightArmourRecipie(i));
		}

        /*
		for(int x = 0; x < 16; x++){
			for(int y = 0; y < 16; y++){
				ItemStack bannerStack = new ItemStack(bannerItem);
				((IHeraldyItem)bannerStack.getItem()).setHeraldryCode(bannerStack,
						SigilHelper.packSigil(HeraldyPattern.HORIZONTAL_BLOCK, (byte) 0, (byte) 0,
                                new Color(ItemDye.dyeColors[15 - x]), new Color(ItemDye.dyeColors[15 - y]),
                                HeraldryIcon.Blank, HeraldryPositions.SINGLE, Color.WHITE, Color.WHITE)
						);
				GameRegistry.addRecipe(bannerStack,  new Object[]
						{" a "," b ", " S ",
						Character.valueOf('a'), new ItemStack(Block.cloth,0, x),
						Character.valueOf('b'), new ItemStack(Block.cloth,0, y),
						Character.valueOf('S'), Item.stick
						}
				);
			}
		}
		
		GameRegistry.addRecipe(new ItemStack(bannerItem),  new Object[]
				{" W "," W ", " S ",Character.valueOf('W'),Block.cloth, Character.valueOf('S'), Item.stick});
		*/
		

	}

	
}
