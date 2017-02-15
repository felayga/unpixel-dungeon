/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * unPixel Dungeon
 * Copyright (C) 2015-2016 Randall Foudray
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */
package com.felayga.unpixeldungeon.sprites;

public class ItemSpriteSheet {

	private static final int ROWSIZE = 16;

	// Row definers
	private static final int ROW_HEAPS          = 0 * ROWSIZE;
	private static final int ROW_PLACEHOLDERS   = 1 * ROWSIZE;
	private static final int ROW_MISCELLANEOUS  = 2 * ROWSIZE;
	private static final int ROW_WEAPONMELEE    = 3 * ROWSIZE;
    private static final int ROW_WEAPONRANGED   = 4 * ROWSIZE;
	private static final int ROW_WEAPONAMMO     = 5 * ROWSIZE;
	private static final int ROW_WAND           = 6 * ROWSIZE;
    private static final int ROW_ARMOR          = 8 * ROWSIZE;
	private static final int ROW_RING           = 9 * ROWSIZE;
	private static final int ROW_ARTIFACT       = 11 * ROWSIZE;
	private static final int ROW_SCROLL         = 13 * ROWSIZE;
	private static final int ROW_POTION         = 15 * ROWSIZE;
	private static final int ROW_SEED           = 18 * ROWSIZE;
    private static final int ROW_ALCHEMY        = 19 * ROWSIZE;
	private static final int ROW_FOOD           = 20 * ROWSIZE;
	private static final int ROW_QUEST          = 21 * ROWSIZE;
	private static final int ROW_TOOLS          = 22 * ROWSIZE;
	private static final int ROW_GEMSTONE       = 23 * ROWSIZE;
	private static final int ROW_BOOK           = 24 * ROWSIZE;
	private static final int ROW_SPELL          = 27 * ROWSIZE;
	private static final int ROW_BOOTS          = 28 * ROWSIZE;
	private static final int ROW_GLOVES         = 29 * ROWSIZE;
	private static final int ROW_HELMET         = 30 * ROWSIZE;
    private static final int ROW_CLOAK          = 31 * ROWSIZE;

	//Row One: Items which can't be obtained
	//null warning occupies space 0, should only show up if there's a bug.
	public static final int NULLWARN        = ROW_HEAPS + 0;

	// Heaps (containers)
    public static final int POUCH	        = ROW_HEAPS + 1;
    public static final int HOLDER	        = ROW_HEAPS + 2;
    public static final int BANDOLIER       = ROW_HEAPS + 3;
    public static final int HOLSTER	        = ROW_HEAPS + 4;
	public static final int BONES			= ROW_HEAPS + 5;
	public static final int REMAINS         = ROW_HEAPS + 6;
	public static final int TOMB			= ROW_HEAPS + 7;
	public static final int GRAVE			= ROW_HEAPS + 8;
	public static final int CHEST			= ROW_HEAPS + 9;
	public static final int CHEST_LOCKED	= ROW_HEAPS + 10;
    public static final int LARGEBOX        = ROW_HEAPS + 11;
    public static final int LARGEBOX_LOCKED = ROW_HEAPS + 12;
    public static final int ICEBOX          = ROW_HEAPS + 13;
    public static final int SACK_BURLAP     = ROW_HEAPS + 14;
    public static final int SACK_OILSKIN    = ROW_HEAPS + 15;

	// Placeholders
	public static final int PLACEHOLDER_WEAPON	= ROW_PLACEHOLDERS + 0;
	public static final int PLACEHOLDER_ARMOR	= ROW_PLACEHOLDERS + 1;
	public static final int PLACEHOLDER_RING	= ROW_PLACEHOLDERS + 2;
	public static final int PLACEHOLDER_SMTH	= ROW_PLACEHOLDERS + 3;
	public static final int PLACEHOLDER_SHIELD	= ROW_PLACEHOLDERS + 4;
	public static final int PLACEHOLDER_TOOL	= ROW_PLACEHOLDERS + 5;
	public static final int PLACEHOLDER_GLOVES	= ROW_PLACEHOLDERS + 6;
	public static final int PLACEHOLDER_BOOTS	= ROW_PLACEHOLDERS + 7;
	public static final int PLACEHOLDER_CLOAK	= ROW_PLACEHOLDERS + 8;
	public static final int PLACEHOLDER_AMULET	= ROW_PLACEHOLDERS + 9;
	public static final int PLACEHOLDER_FACE	= ROW_PLACEHOLDERS + 10;
	public static final int PLACEHOLDER_HELMET	= ROW_PLACEHOLDERS + 11;
	public static final int PLACEHOLDER_PANTS	= ROW_PLACEHOLDERS + 12;

	//Row Two: Miscellaneous single use items
	public static final int GOLD	        = ROW_MISCELLANEOUS + 0;
	public static final int TORCH	        = ROW_MISCELLANEOUS + 1;
	public static final int STYLUS	        = ROW_MISCELLANEOUS + 2;
	public static final int ANKH	        = ROW_MISCELLANEOUS + 3;
	// Keys
	public static final int GOLDEN_KEY		= ROW_MISCELLANEOUS + 5;
	public static final int SKELETON_KEY	= ROW_MISCELLANEOUS + 6;
	//Boss Rewards
	public static final int BEACON	        = ROW_MISCELLANEOUS + 7;
	public static final int MASTERY	        = ROW_MISCELLANEOUS + 8;
	public static final int KIT		        = ROW_MISCELLANEOUS + 9;
	public static final int AMULET	        = ROW_MISCELLANEOUS + 10;
	public static final int WEIGHT          = ROW_MISCELLANEOUS + 11;
	public static final int BOMB            = ROW_MISCELLANEOUS + 12;
	public static final int DBL_BOMB        = ROW_MISCELLANEOUS + 13;
	public static final int HONEYPOT        = ROW_MISCELLANEOUS + 14;
	public static final int SHATTPOT        = ROW_MISCELLANEOUS + 15;

	//Row Three: Melee weapons
	public static final int MELEE_KNUCKLEDUSTER	= ROW_WEAPONMELEE + 0;
	public static final int MELEE_DAGGER		= ROW_WEAPONMELEE + 1;
	public static final int MELEE_SHORT_SWORD	= ROW_WEAPONMELEE + 2;
	public static final int MELEE_MAGES_STAFF   = ROW_WEAPONMELEE + 3;
	public static final int MELEE_QUARTERSTAFF	= ROW_WEAPONMELEE + 4;
	public static final int MELEE_SPEAR			= ROW_WEAPONMELEE + 5;
	public static final int MELEE_MACE			= ROW_WEAPONMELEE + 6;
	public static final int MELEE_SWORD			= ROW_WEAPONMELEE + 7;
	public static final int MELEE_BATTLE_AXE	= ROW_WEAPONMELEE + 8;
	public static final int MELEE_LONG_SWORD	= ROW_WEAPONMELEE + 9;
	public static final int MELEE_WAR_HAMMER	= ROW_WEAPONMELEE + 10;
	public static final int MELEE_GLAIVE		= ROW_WEAPONMELEE + 11;
    public static final int MELEE_DAGGER_CRUDE  = ROW_WEAPONMELEE + 12;
    public static final int MELEE_DAGGER_RUNED  = ROW_WEAPONMELEE + 13;
    public static final int MELEE_CRICKETBAT    = ROW_WEAPONMELEE + 14;

    //Row Derp: Ranged weapons
    public static final int RANGED_SLING           = ROW_WEAPONRANGED + 0;

	//Row Four: Missile weapons
	public static final int MISSILE_DART			= ROW_WEAPONAMMO + 0;
	public static final int MISSILE_BOOMERANG		= ROW_WEAPONAMMO + 1;
	public static final int MISSILE_INCENDIARY_DART	= ROW_WEAPONAMMO + 2;
	public static final int MISSILE_SHURIKEN		= ROW_WEAPONAMMO + 3;
	public static final int MISSILE_CURARE_DART		= ROW_WEAPONAMMO + 4;
	public static final int MISSILE_JAVELIN			= ROW_WEAPONAMMO + 5;
	public static final int MISSILE_TOMAHAWK		= ROW_WEAPONAMMO + 6;
	public static final int MISSILE_ROCK			= ROW_WEAPONAMMO + 7;

	//Row Five: Armors
	public static final int ARMOR_CLOTH				= ROW_ARMOR + 0;
	public static final int ARMOR_LEATHER			= ROW_ARMOR + 1;
	public static final int ARMOR_MAIL				= ROW_ARMOR + 2;
	public static final int ARMOR_SCALE				= ROW_ARMOR + 3;
	public static final int ARMOR_HALFPLATE 		= ROW_ARMOR + 4;
	public static final int ARMOR_FULLPLATE			= ROW_ARMOR + 5;
	public static final int ARMOR_STUDDEDLEATHER	= ROW_ARMOR + 6;
	public static final int ARMOR_HIDE				= ROW_ARMOR + 7;
	public static final int ARMOR_BANDED			= ROW_ARMOR + 8;


	public static final int ARMOR_WARRIOR			= ROW_ARMOR + 5;
	public static final int ARMOR_MAGE				= ROW_ARMOR + 13;
	public static final int ARMOR_ROGUE				= ROW_ARMOR + 14;
	public static final int ARMOR_HUNTRESS			= ROW_ARMOR + 15;

	//Row Six: Wands
    public static final int WAND_01     = ROW_WAND + 0;
    public static final int WAND_02     = ROW_WAND + 1;
    public static final int WAND_03     = ROW_WAND + 2;
    public static final int WAND_04     = ROW_WAND + 3;
    public static final int WAND_05     = ROW_WAND + 4;
    public static final int WAND_06     = ROW_WAND + 5;
    public static final int WAND_07     = ROW_WAND + 6;
    public static final int WAND_08     = ROW_WAND + 7;
    public static final int WAND_09     = ROW_WAND + 8;
    public static final int WAND_10     = ROW_WAND + 9;
    public static final int WAND_11     = ROW_WAND + 10;
    public static final int WAND_12     = ROW_WAND + 11;
    public static final int WAND_13     = ROW_WAND + 12;
    public static final int WAND_14     = ROW_WAND + 13;
    public static final int WAND_15     = ROW_WAND + 14;
    public static final int WAND_16     = ROW_WAND + 15;
    public static final int WAND_17     = ROW_WAND + 16;
    public static final int WAND_18     = ROW_WAND + 17;
    public static final int WAND_19     = ROW_WAND + 18;
    public static final int WAND_20     = ROW_WAND + 19;
    /*
	public static final int WAND_MAGIC_MISSILE	= ROW_WAND + 0;
	public static final int WAND_FIREBOLT		= ROW_WAND + 1;
	public static final int WAND_FROST			= ROW_WAND + 2;
	public static final int WAND_LIGHTNING		= ROW_WAND + 3;
	public static final int WAND_DISINTEGRATION	= ROW_WAND + 4;
	public static final int WAND_PRISMATIC_LIGHT= ROW_WAND + 5;
	public static final int WAND_VENOM			= ROW_WAND + 6;
	public static final int WAND_LIVING_EARTH	= ROW_WAND + 7;
	public static final int WAND_BLAST_WAVE		= ROW_WAND + 8;
	public static final int WAND_CORRUPTION		= ROW_WAND + 9;
	public static final int WAND_WARDING      	= ROW_WAND + 10;
	public static final int WAND_REGROWTH		= ROW_WAND + 11;
	public static final int WAND_TRANSFUSION	= ROW_WAND + 12;
	*/

	//Row Seven: Rings
	public static final int RING_GARNET		= ROW_RING + 0;
	public static final int RING_RUBY		= ROW_RING + 1;
	public static final int RING_TOPAZ		= ROW_RING + 2;
	public static final int RING_EMERALD	= ROW_RING + 3;
	public static final int RING_ONYX		= ROW_RING + 4;
	public static final int RING_OPAL		= ROW_RING + 5;
	public static final int RING_TOURMALINE	= ROW_RING + 6;
	public static final int RING_SAPPHIRE	= ROW_RING + 7;
	public static final int RING_AMETHYST	= ROW_RING + 8;
	public static final int RING_QUARTZ		= ROW_RING + 9;
	public static final int RING_AGATE		= ROW_RING + 10;
	public static final int RING_DIAMOND	= ROW_RING + 11;
	public static final int RING_COPPER		= ROW_RING + 12;
	public static final int RING_BRASS		= ROW_RING + 13;
	public static final int RING_BRONZE		= ROW_RING + 14;
	public static final int RING_IRON		= ROW_RING + 15;
	public static final int RING_STEEL		= ROW_RING + 16;
	public static final int RING_SILVER		= ROW_RING + 17;
	public static final int RING_GOLD		= ROW_RING + 18;
	public static final int RING_CORAL		= ROW_RING + 19;
	public static final int RING_TIGEREYE	= ROW_RING + 20;
	public static final int RING_WOODEN		= ROW_RING + 21;
	public static final int RING_IVORY		= ROW_RING + 22;
	public static final int RING_TWISTED	= ROW_RING + 23;
	public static final int RING_MOONSTONE	= ROW_RING + 24;
	public static final int RING_JADE		= ROW_RING + 25;
	public static final int RING_PEARL		= ROW_RING + 26;
	public static final int RING_GRANITE	= ROW_RING + 27;
	public static final int RING_VINE		= ROW_RING + 28;
	public static final int RING_AMMOLITE	= ROW_RING + 29;
	public static final int RING_SPINEL		= ROW_RING + 30;
	public static final int RING_MEAT		= ROW_RING + 31;


	//Row Eight: Artifacts with Static Images
	public static final int ARTIFACT_CLOAK      = ROW_ARTIFACT + 0;
	public static final int ARTIFACT_ARMBAND    = ROW_ARTIFACT + 1;
	public static final int ARTIFACT_CAPE       = ROW_ARTIFACT + 2;
	public static final int ARTIFACT_TALISMAN   = ROW_ARTIFACT + 3;
	public static final int ARTIFACT_HOURGLASS  = ROW_ARTIFACT + 4;
	public static final int ARTIFACT_TOOLKIT    = ROW_ARTIFACT + 5;
	public static final int ARTIFACT_SPELLBOOK  = ROW_ARTIFACT + 6;
	public static final int ARTIFACT_BEACON     = ROW_ARTIFACT + 7;
	public static final int ARTIFACT_CHAINS     = ROW_ARTIFACT + 8;
    public static final int VIAL	            = ROW_ARTIFACT + 9;
    public static final int DEWDROP	            = ROW_ARTIFACT + 10;
    public static final int PETAL	            = ROW_ARTIFACT + 11;
    public static final int SANDBAG             = ROW_ARTIFACT + 12;

	//Row Nine: Artifacts with Dynamic Images
	public static final int ARTIFACT_HORN1      = ROW_ARTIFACT + 16;
	public static final int ARTIFACT_HORN2      = ROW_ARTIFACT + 17;
	public static final int ARTIFACT_HORN3      = ROW_ARTIFACT + 18;
	public static final int ARTIFACT_HORN4      = ROW_ARTIFACT + 19;
	public static final int ARTIFACT_CHALICE1   = ROW_ARTIFACT + 20;
	public static final int ARTIFACT_CHALICE2   = ROW_ARTIFACT + 21;
	public static final int ARTIFACT_CHALICE3   = ROW_ARTIFACT + 22;
	public static final int ARTIFACT_SANDALS    = ROW_ARTIFACT + 23;
	public static final int ARTIFACT_SHOES      = ROW_ARTIFACT + 24;
	public static final int ARTIFACT_BOOTS      = ROW_ARTIFACT + 25;
	public static final int ARTIFACT_GREAVES    = ROW_ARTIFACT + 26;
	public static final int ARTIFACT_ROSE1      = ROW_ARTIFACT + 27;
	public static final int ARTIFACT_ROSE2      = ROW_ARTIFACT + 28;
	public static final int ARTIFACT_ROSE3      = ROW_ARTIFACT + 29;

	//Row Ten: Scrolls
	public static final int SCROLL_01			= ROW_SCROLL + 0;
	public static final int SCROLL_02			= ROW_SCROLL + 1;
	public static final int SCROLL_03			= ROW_SCROLL + 2;
	public static final int SCROLL_04			= ROW_SCROLL + 3;
	public static final int SCROLL_05			= ROW_SCROLL + 4;
	public static final int SCROLL_06			= ROW_SCROLL + 5;
	public static final int SCROLL_07			= ROW_SCROLL + 6;
	public static final int SCROLL_08			= ROW_SCROLL + 7;
	public static final int SCROLL_09			= ROW_SCROLL + 8;
	public static final int SCROLL_10			= ROW_SCROLL + 9;
	public static final int SCROLL_11			= ROW_SCROLL + 10;
	public static final int SCROLL_12			= ROW_SCROLL + 11;
	public static final int SCROLL_13			= ROW_SCROLL + 12;
	public static final int SCROLL_14			= ROW_SCROLL + 13;
	public static final int SCROLL_15			= ROW_SCROLL + 14;
	public static final int SCROLL_16			= ROW_SCROLL + 15;
	public static final int SCROLL_17			= ROW_SCROLL + 16;
	public static final int SCROLL_18			= ROW_SCROLL + 17;
	public static final int SCROLL_19			= ROW_SCROLL + 18;
	public static final int SCROLL_20			= ROW_SCROLL + 19;
	public static final int SCROLL_21			= ROW_SCROLL + 20;
	public static final int SCROLL_22			= ROW_SCROLL + 21;
	public static final int SCROLL_23			= ROW_SCROLL + 22;
	public static final int SCROLL_24			= ROW_SCROLL + 23;
	public static final int SCROLL_25			= ROW_SCROLL + 24;
    public static final int SCROLL_26			= ROW_SCROLL + 25;
	public static final int SCROLL_BLANK		= ROW_SCROLL + 26;

	//Row Eleven: Potions
	public static final int POTION_RUBY			= ROW_POTION + 0;
	public static final int POTION_PINK			= ROW_POTION + 1;
	public static final int POTION_ORANGE		= ROW_POTION + 2;
	public static final int POTION_YELLOW		= ROW_POTION + 3;
	public static final int POTION_EMERALD		= ROW_POTION + 4;
	public static final int POTION_DARKGREEN	= ROW_POTION + 5;
	public static final int POTION_CYAN			= ROW_POTION + 6;
	public static final int POTION_SKYBLUE		= ROW_POTION + 7;
	public static final int POTION_BRILLIANTBLUE= ROW_POTION + 8;
	public static final int POTION_MAGENTA		= ROW_POTION + 9;
	public static final int POTION_PURPLERED	= ROW_POTION + 10;
	public static final int POTION_PUCE			= ROW_POTION + 11;
	public static final int POTION_MILKY		= ROW_POTION + 12;
	public static final int POTION_SWIRLY		= ROW_POTION + 13;
	public static final int POTION_BUBBLY		= ROW_POTION + 14;
	public static final int POTION_SMOKY		= ROW_POTION + 15;
	public static final int POTION_CLOUDY		= ROW_POTION + 16;
	public static final int POTION_EFFERVESCENT	= ROW_POTION + 17;
	public static final int POTION_BLACK		= ROW_POTION + 18;
	public static final int POTION_GOLDEN		= ROW_POTION + 19;
	public static final int POTION_BROWN		= ROW_POTION + 20;
	public static final int POTION_FIZZY		= ROW_POTION + 21;
	public static final int POTION_DARK			= ROW_POTION + 22;
	public static final int POTION_WHITE		= ROW_POTION + 23;
	public static final int POTION_MURKY		= ROW_POTION + 24;
    public static final int POTION_DINGY        = ROW_POTION + 25;
    public static final int POTION_INDIGO       = ROW_POTION + 26;
    public static final int POTION_CREAMY       = ROW_POTION + 27;
    public static final int POTION_GLOWING      = ROW_POTION + 28;
    public static final int POTION_SILVERY      = ROW_POTION + 29;
    public static final int POTION_PUTRESCENT   = ROW_POTION + 30;
    public static final int POTION_SEAGREEN     = ROW_POTION + 31;
    public static final int POTION_RAINBOW      = ROW_POTION + 32;
    public static final int POTION_CGA          = ROW_POTION + 33;
    public static final int POTION_ORANGERED    = ROW_POTION + 34;
    public static final int POTION_OLIVE        = ROW_POTION + 35;
    public static final int POTION_MAUVE        = ROW_POTION + 36;
    public static final int POTION_CHARTREUSE   = ROW_POTION + 37;
    public static final int POTION_FLESHCOLORED = ROW_POTION + 38;
    public static final int POTION_CLEAR		= ROW_POTION + 39;
    public static final int POTION_BURNING      = ROW_POTION + 40;

	//Row Twelve: Seeds
    public static final int SEED_BLOODLEAF  = ROW_SEED + 0;
	public static final int SEED_FIREBLOOM	= ROW_SEED + 1;
	public static final int SEED_STARFLOWER	= ROW_SEED + 2;
	public static final int SEED_BLINDWEED	= ROW_SEED + 3;
	public static final int SEED_SUNGRASS	= ROW_SEED + 4;
	public static final int SEED_ICECAP		= ROW_SEED + 5;
	public static final int SEED_STORMVINE	= ROW_SEED + 6;
	public static final int SEED_SORROWMOSS	= ROW_SEED + 7;
	public static final int SEED_DREAMFOIL	= ROW_SEED + 8;
	public static final int SEED_EARTHROOT	= ROW_SEED + 9;
	public static final int SEED_FADELEAF	= ROW_SEED + 10;
	public static final int SEED_BLANDFRUIT	= ROW_SEED + 11;
    public static final int SEED_DEATHROOT  = ROW_SEED + 12;
    public static final int SEED_MOONGRASS  = ROW_SEED + 13;
    public static final int SEED_STONEBERRY = ROW_SEED + 14;
    public static final int SEED_SWAMPWEED  = ROW_SEED + 15;

    //Row Twelve: Seeds
    public static final int ALCHEMY_BLOODLEAF   = ROW_ALCHEMY + 0;
    public static final int ALCHEMY_FIREBLOOM   = ROW_ALCHEMY + 1;
    public static final int ALCHEMY_STARFLOWER  = ROW_ALCHEMY + 2;
    public static final int ALCHEMY_BLINDWEED   = ROW_ALCHEMY + 3;
    public static final int ALCHEMY_SUNGRASS    = ROW_ALCHEMY + 4;
    public static final int ALCHEMY_ICECAP	    = ROW_ALCHEMY + 5;
    public static final int ALCHEMY_STORMVINE   = ROW_ALCHEMY + 6;
    public static final int ALCHEMY_SORROWMOSS  = ROW_ALCHEMY + 7;
    public static final int ALCHEMY_DREAMFOIL   = ROW_ALCHEMY + 8;
    public static final int ALCHEMY_EARTHROOT   = ROW_ALCHEMY + 9;
    public static final int ALCHEMY_FADELEAF    = ROW_ALCHEMY + 10;
    public static final int ALCHEMY_BLANDFRUIT  = ROW_ALCHEMY + 11;
    public static final int ALCHEMY_DEATHROOT   = ROW_ALCHEMY + 12;
    public static final int ALCHEMY_MOONGRASS   = ROW_ALCHEMY + 13;
    public static final int ALCHEMY_STONEBERRY  = ROW_ALCHEMY + 14;
    public static final int ALCHEMY_SWAMPWEED   = ROW_ALCHEMY + 15;

	//Row Thirteen: Food
	public static final int FOOD_MEAT		    = ROW_FOOD + 0;
    public static final int FOOD_MEAT_ROTTEN    = ROW_FOOD + 1;
    public static final int FOOD_LETTUCE        = ROW_FOOD + 2;
    public static final int FOOD_LETTUCE_ROTTEN = ROW_FOOD + 3;
    public static final int FOOD_OVERPRICED	    = ROW_FOOD + 4;
    public static final int FOOD_RATION		    = ROW_FOOD + 5;
    public static final int FOOD_PASTY		    = ROW_FOOD + 6;
	public static final int FOOD_BLANDFRUIT	    = ROW_FOOD + 7;
    public static final int FOOD_CAN_UNOPENED   = ROW_FOOD + 8;
    public static final int FOOD_CAN_MEAT       = ROW_FOOD + 9;
    public static final int FOOD_CAN_VEGETABLE  = ROW_FOOD + 10;
    public static final int FOOD_MRE            = ROW_FOOD + 11;
    public static final int FOOD_CRATION        = ROW_FOOD + 12;
    public static final int FOOD_LEMBAS         = ROW_FOOD + 13;

	//Row Fourteen: Quest Items
	public static final int SKULL		    = ROW_QUEST + 0;
	public static final int DUST		    = ROW_QUEST + 1;
	public static final int CANDLE		    = ROW_QUEST + 2;
	public static final int EMBER		    = ROW_QUEST + 3;
	public static final int ORE			    = ROW_QUEST + 4;
	public static final int TOKEN		    = ROW_QUEST + 5;
    public static final int SEED_ROTBERRY	= ROW_QUEST + 6;

	//Row Fifteen: Tools
    public static final int TOOL_PICKAXE    = ROW_TOOLS + 0;
    public static final int TOOL_IRON_KEY   = ROW_TOOLS + 1;
    public static final int TOOL_LOCKPICK   = ROW_TOOLS + 2;
    public static final int TOOL_CANNINGKIT = ROW_TOOLS + 3;
    public static final int TOOL_BLINDFOLD  = ROW_TOOLS + 4;
    public static final int TOOL_TOWEL      = ROW_TOOLS + 5;

	//Row Sixteen: Unused
	public static final int GEMSTONE_WHITE	= ROW_GEMSTONE + 0;
	public static final int GEMSTONE_RED	= ROW_GEMSTONE + 1;
	public static final int GEMSTONE_ORANGE	= ROW_GEMSTONE + 2;
	public static final int GEMSTONE_BROWN	= ROW_GEMSTONE + 3;
	public static final int GEMSTONE_YELLOW	= ROW_GEMSTONE + 4;
	public static final int GEMSTONE_GREEN	= ROW_GEMSTONE + 5;
	public static final int GEMSTONE_BLUE	= ROW_GEMSTONE + 6;
	public static final int GEMSTONE_VIOLET	= ROW_GEMSTONE + 7;
	public static final int GEMSTONE_BLACK	= ROW_GEMSTONE + 8;
	public static final int GEMSTONE_GRAY	= ROW_GEMSTONE + 9;

	//herpty derp all these comments are out of date
	public static final int SPELLBOOK_01	= ROW_BOOK + 0;
	public static final int SPELLBOOK_02	= ROW_BOOK + 1;
	public static final int SPELLBOOK_03	= ROW_BOOK + 2;
	public static final int SPELLBOOK_04	= ROW_BOOK + 3;
	public static final int SPELLBOOK_05	= ROW_BOOK + 4;
	public static final int SPELLBOOK_06	= ROW_BOOK + 5;
	public static final int SPELLBOOK_07	= ROW_BOOK + 6;
	public static final int SPELLBOOK_08	= ROW_BOOK + 7;
	public static final int SPELLBOOK_09	= ROW_BOOK + 8;
	public static final int SPELLBOOK_10	= ROW_BOOK + 9;
	public static final int SPELLBOOK_11	= ROW_BOOK + 10;
	public static final int SPELLBOOK_12	= ROW_BOOK + 11;
	public static final int SPELLBOOK_13	= ROW_BOOK + 12;
	public static final int SPELLBOOK_14	= ROW_BOOK + 13;
	public static final int SPELLBOOK_15	= ROW_BOOK + 14;
	public static final int SPELLBOOK_16	= ROW_BOOK + 15;
	public static final int SPELLBOOK_17	= ROW_BOOK + 16;
	public static final int SPELLBOOK_18	= ROW_BOOK + 17;
	public static final int SPELLBOOK_19	= ROW_BOOK + 18;
	public static final int SPELLBOOK_20	= ROW_BOOK + 19;
	public static final int SPELLBOOK_21	= ROW_BOOK + 20;
	public static final int SPELLBOOK_22	= ROW_BOOK + 21;
	public static final int SPELLBOOK_23	= ROW_BOOK + 22;
	public static final int SPELLBOOK_24	= ROW_BOOK + 23;
	public static final int SPELLBOOK_25	= ROW_BOOK + 24;
	public static final int SPELLBOOK_26	= ROW_BOOK + 25;
	public static final int SPELLBOOK_27	= ROW_BOOK + 26;
	public static final int SPELLBOOK_28	= ROW_BOOK + 27;
	public static final int SPELLBOOK_29	= ROW_BOOK + 28;
	public static final int SPELLBOOK_30	= ROW_BOOK + 29;
	public static final int SPELLBOOK_31	= ROW_BOOK + 30;
	public static final int SPELLBOOK_32	= ROW_BOOK + 31;
	public static final int SPELLBOOK_33	= ROW_BOOK + 32;
	public static final int SPELLBOOK_34	= ROW_BOOK + 33;
	public static final int SPELLBOOK_35	= ROW_BOOK + 34;
	public static final int SPELLBOOK_36	= ROW_BOOK + 35;
	public static final int SPELLBOOK_37	= ROW_BOOK + 36;
	public static final int SPELLBOOK_38	= ROW_BOOK + 37;
	public static final int SPELLBOOK_39	= ROW_BOOK + 38;
	public static final int SPELLBOOK_40	= ROW_BOOK + 39;

	//fart
	public static final int SPELL_IDENTIFY	= ROW_SPELL + 0;

	//another fart
	public static final int BOOTS_LEATHER	= ROW_BOOTS + 0;

	//third fart
	public static final int GLOVES_LEATHER	= ROW_GLOVES+0;

    //blah blah
    public static final int HELMET_LEATHER  = ROW_HELMET + 0;
    public static final int HELMET_CRUDE    = ROW_HELMET + 1;

    //et cetera
    public static final int CLOAK_LEATHER   = ROW_CLOAK + 0;
    public static final int CLOAK_DWARVEN   = ROW_CLOAK + 1;
    public static final int CLOAK_ORCISH    = ROW_CLOAK + 2;
    public static final int CLOAK_ELVISH    = ROW_CLOAK + 3;
    public static final int CLOAK_APRON     = ROW_CLOAK + 4;
    public static final int CLOAK_OILSKIN   = ROW_CLOAK + 5;
    public static final int CLOAK_MUMMY     = ROW_CLOAK + 6;
    public static final int CLOAK_ROBE      = ROW_CLOAK + 7;
    public static final int CLOAK_PLAIN     = ROW_CLOAK + 8;
    public static final int CLOAK_OPERA     = ROW_CLOAK + 9;
    public static final int CLOAK_FANCY     = ROW_CLOAK + 10;
    public static final int CLOAK_TATTERED  = ROW_CLOAK + 11;

    public static final int ITEM_EMPTY      = 63 * ROWSIZE + ROWSIZE - 1;
}
