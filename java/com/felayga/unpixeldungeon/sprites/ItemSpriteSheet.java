/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.sprites;

public class ItemSpriteSheet {

	private static final int ROWSIZE = 16;

	// Row definers
	private static final int ROW1 = 	0*ROWSIZE;
	private static final int ROW2 = 	1*ROWSIZE;
	private static final int ROW3 = 	2*ROWSIZE;
	private static final int ROW4 = 	3*ROWSIZE;
	private static final int ROW5 = 	4*ROWSIZE;
	private static final int ROW6 = 	5*ROWSIZE;
	private static final int ROW7 = 	6*ROWSIZE;
	private static final int ROW8 = 	7*ROWSIZE;
	private static final int ROW9 = 	8*ROWSIZE;
	private static final int ROW10 =	9*ROWSIZE;
	private static final int ROW11 =	10*ROWSIZE;
	private static final int ROW12 =	11*ROWSIZE;
	private static final int ROW13 =	12*ROWSIZE;
	private static final int ROW14 =	13*ROWSIZE;
	private static final int ROW15 =	14*ROWSIZE;
	private static final int ROW16 =	15*ROWSIZE;
	private static final int ROW17 =	16*ROWSIZE;
	private static final int ROW18 =	17*ROWSIZE;
	private static final int ROW19 =	18*ROWSIZE;
	private static final int ROW20 =	19*ROWSIZE;
	private static final int ROW21 =	20*ROWSIZE;
	private static final int ROW22 =	21*ROWSIZE;
	private static final int ROW23 =	22*ROWSIZE;
	private static final int ROW24 =	23*ROWSIZE;
	private static final int ROW25 =	24*ROWSIZE;
	private static final int ROW26 =	25*ROWSIZE;
	private static final int ROW27 =	26*ROWSIZE;
	private static final int ROW28 =	27*ROWSIZE;
	private static final int ROW29 =	28*ROWSIZE;
	private static final int ROW30 =	29*ROWSIZE;
	private static final int ROW31 =	30*ROWSIZE;
	private static final int ROW32 =	31*ROWSIZE;

	//Row One: Items which can't be obtained
	//null warning occupies space 0, should only show up if there's a bug.
	public static final int NULLWARN    = ROW1+0;
	public static final int DEWDROP	    = ROW1+1;
	public static final int PETAL	    = ROW1+2;
	public static final int SANDBAG     = ROW1+3;
	// Heaps (containers)
	public static final int BONES			= ROW1+4;
	public static final int REMAINS         = ROW1+5;
	public static final int TOMB			= ROW1+6;
	public static final int GRAVE			= ROW1+7;
	public static final int CHEST			= ROW1+8;
	public static final int LOCKED_CHEST	= ROW1+9;
	public static final int CRYSTAL_CHEST	= ROW1+10;
	// Placeholders
	public static final int WEAPON	= ROW2+0;
	public static final int ARMOR	= ROW2+1;
	public static final int RING	= ROW2+2;
	public static final int SMTH	= ROW2+3;
	public static final int SHIELD	= ROW2+4;
	public static final int TOOL	= ROW2+5;
	public static final int GLOVES	= ROW2+6;
	public static final int BOOTS	= ROW2+7;
	public static final int CLOAK	= ROW2+8;
	public static final int AMULETB	= ROW2+9;
	public static final int FACE	= ROW2+10;

	//Row Two: Miscellaneous single use items
	public static final int GOLD	    = ROW3+0;
	public static final int TORCH	    = ROW3+1;
	public static final int STYLUS	    = ROW3+2;
	public static final int ANKH	    = ROW3+3;
	// Keys
	public static final int IRON_KEY		= ROW3+4;
	public static final int GOLDEN_KEY		= ROW3+5;
	public static final int SKELETON_KEY	= ROW3+6;
	//Boss Rewards
	public static final int BEACON	= ROW3+7;
	public static final int MASTERY	= ROW3+8;
	public static final int KIT		= ROW3+9;
	public static final int AMULET	= ROW3+10;
	public static final int WEIGHT  = ROW3+11;
	public static final int BOMB    = ROW3+12;
	public static final int DBL_BOMB= ROW3+13;
	public static final int HONEYPOT= ROW3+14;
	public static final int SHATTPOT= ROW3+15;

	//Row Three: Melee weapons
	public static final int KNUCKLEDUSTER	= ROW4+0;
	public static final int DAGGER			= ROW4+1;
	public static final int SHORT_SWORD		= ROW4+2;
	public static final int MAGES_STAFF     = ROW4+3;
	public static final int QUARTERSTAFF	= ROW4+4;
	public static final int SPEAR			= ROW4+5;
	public static final int MACE			= ROW4+6;
	public static final int SWORD			= ROW4+7;
	public static final int BATTLE_AXE		= ROW4+8;
	public static final int LONG_SWORD		= ROW4+9;
	public static final int WAR_HAMMER		= ROW4+10;
	public static final int GLAIVE			= ROW4+11;

	//Row Four: Missile weapons
	public static final int DART			= ROW5+0;
	public static final int BOOMERANG		= ROW5+1;
	public static final int INCENDIARY_DART	= ROW5+2;
	public static final int SHURIKEN		= ROW5+3;
	public static final int CURARE_DART		= ROW5+4;
	public static final int JAVELIN			= ROW5+5;
	public static final int TOMAHAWK		= ROW5+6;
	public static final int ROCK			= ROW5+7;

	//Row Five: Armors
	public static final int ARMOR_CLOTH				= ROW6+0;
	public static final int ARMOR_LEATHER			= ROW6+1;
	public static final int ARMOR_MAIL				= ROW6+2;
	public static final int ARMOR_SCALE				= ROW6+3;
	public static final int ARMOR_HALFPLATE 		= ROW6+4;
	public static final int ARMOR_FULLPLATE			= ROW6+5;
	public static final int ARMOR_STUDDEDLEATHER	= ROW6+6;
	public static final int ARMOR_HIDE				= ROW6+7;
	public static final int ARMOR_BANDED			= ROW6+8;


	public static final int ARMOR_WARRIOR			= ROW6+5;
	public static final int ARMOR_MAGE				= ROW6+13;
	public static final int ARMOR_ROGUE				= ROW6+14;
	public static final int ARMOR_HUNTRESS			= ROW6+15;

	//Row Six: Wands
	public static final int WAND_MAGIC_MISSILE	= ROW7+0;
	public static final int WAND_FIREBOLT		= ROW7+1;
	public static final int WAND_FROST			= ROW7+2;
	public static final int WAND_LIGHTNING		= ROW7+3;
	public static final int WAND_DISINTEGRATION	= ROW7+4;
	public static final int WAND_PRISMATIC_LIGHT= ROW7+5;
	public static final int WAND_VENOM			= ROW7+6;
	public static final int WAND_LIVING_EARTH	= ROW7+7;
	public static final int WAND_BLAST_WAVE		= ROW7+8;
	public static final int WAND_CORRUPTION		= ROW7+9;
	public static final int WAND_WARDING      	= ROW7+10;
	public static final int WAND_REGROWTH		= ROW7+11;
	public static final int WAND_TRANSFUSION	= ROW7+12;

	//Row Seven: Rings
	public static final int RING_GARNET		= ROW8+0;
	public static final int RING_RUBY		= ROW8+1;
	public static final int RING_TOPAZ		= ROW8+2;
	public static final int RING_EMERALD	= ROW8+3;
	public static final int RING_ONYX		= ROW8+4;
	public static final int RING_OPAL		= ROW8+5;
	public static final int RING_TOURMALINE	= ROW8+6;
	public static final int RING_SAPPHIRE	= ROW8+7;
	public static final int RING_AMETHYST	= ROW8+8;
	public static final int RING_QUARTZ		= ROW8+9;
	public static final int RING_AGATE		= ROW8+10;
	public static final int RING_DIAMOND	= ROW8+11;

	//Row Eight: Artifacts with Static Images
	public static final int ARTIFACT_CLOAK      = ROW9+0;
	public static final int ARTIFACT_ARMBAND    = ROW9+1;
	public static final int ARTIFACT_CAPE       = ROW9+2;
	public static final int ARTIFACT_TALISMAN   = ROW9+3;
	public static final int ARTIFACT_HOURGLASS  = ROW9+4;
	public static final int ARTIFACT_TOOLKIT    = ROW9+5;
	public static final int ARTIFACT_SPELLBOOK  = ROW9+6;
	public static final int ARTIFACT_BEACON     = ROW9+7;
	public static final int ARTIFACT_CHAINS     = ROW9+8;

	//Row Nine: Artifacts with Dynamic Images
	public static final int ARTIFACT_HORN1      = ROW10+0;
	public static final int ARTIFACT_HORN2      = ROW10+1;
	public static final int ARTIFACT_HORN3      = ROW10+2;
	public static final int ARTIFACT_HORN4      = ROW10+3;
	public static final int ARTIFACT_CHALICE1   = ROW10+4;
	public static final int ARTIFACT_CHALICE2   = ROW10+5;
	public static final int ARTIFACT_CHALICE3   = ROW10+6;
	public static final int ARTIFACT_SANDALS    = ROW10+7;
	public static final int ARTIFACT_SHOES      = ROW10+8;
	public static final int ARTIFACT_BOOTS      = ROW10+9;
	public static final int ARTIFACT_GREAVES    = ROW10+10;
	public static final int ARTIFACT_ROSE1      = ROW10+11;
	public static final int ARTIFACT_ROSE2      = ROW10+12;
	public static final int ARTIFACT_ROSE3      = ROW10+13;

	//Row Ten: Scrolls
	public static final int SCROLL_01			= ROW11+0;
	public static final int SCROLL_02			= ROW11+1;
	public static final int SCROLL_03			= ROW11+2;
	public static final int SCROLL_04			= ROW11+3;
	public static final int SCROLL_05			= ROW11+4;
	public static final int SCROLL_06			= ROW11+5;
	public static final int SCROLL_07			= ROW11+6;
	public static final int SCROLL_08			= ROW11+7;
	public static final int SCROLL_09			= ROW11+8;
	public static final int SCROLL_10			= ROW11+9;
	public static final int SCROLL_11			= ROW11+10;
	public static final int SCROLL_12			= ROW11+11;
	public static final int SCROLL_13			= ROW11+12;
	public static final int SCROLL_14			= ROW11+13;
	public static final int SCROLL_15			= ROW11+14;
	public static final int SCROLL_16			= ROW11+15;
	public static final int SCROLL_17			= ROW12+0;
	public static final int SCROLL_18			= ROW12+1;
	public static final int SCROLL_19			= ROW12+2;
	public static final int SCROLL_20			= ROW12+3;
	public static final int SCROLL_21			= ROW12+4;
	public static final int SCROLL_22			= ROW12+5;
	public static final int SCROLL_23			= ROW12+6;
	public static final int SCROLL_24			= ROW12+7;
	public static final int SCROLL_25			= ROW12+8;
	public static final int SCROLL_BLANK		= ROW12+9;

	//Row Eleven: Potions
	public static final int POTION_RUBY			= ROW13+0;
	public static final int POTION_PINK			= ROW13+1;
	public static final int POTION_ORANGE		= ROW13+2;
	public static final int POTION_YELLOW		= ROW13+3;
	public static final int POTION_EMERALD		= ROW13+4;
	public static final int POTION_DARKGREEN	= ROW13+5;
	public static final int POTION_CYAN			= ROW13+6;
	public static final int POTION_SKYBLUE		= ROW13+7;
	public static final int POTION_BRILLIANTBLUE= ROW13+8;
	public static final int POTION_MAGENTA		= ROW13+9;
	public static final int POTION_PURPLERED	= ROW13+10;
	public static final int POTION_PUCE			= ROW13+11;
	public static final int POTION_MILKY		= ROW13+12;
	public static final int POTION_SWIRLY		= ROW13+13;
	public static final int POTION_BUBBLY		= ROW13+14;
	public static final int POTION_SMOKY		= ROW13+15;
	public static final int POTION_CLOUDY		= ROW14+0;
	public static final int POTION_EFFERVESCENT	= ROW14+1;
	public static final int POTION_BLACK		= ROW14+2;
	public static final int POTION_GOLDEN		= ROW14+3;
	public static final int POTION_BROWN		= ROW14+4;
	public static final int POTION_FIZZY		= ROW14+5;
	public static final int POTION_DARK			= ROW14+6;
	public static final int POTION_WHITE		= ROW14+7;
	public static final int POTION_MURKY		= ROW14+8;
	public static final int POTION_CLEAR		= ROW14+9;

	//Row Twelve: Seeds
	public static final int SEED_ROTBERRY	= ROW15+0;
	public static final int SEED_FIREBLOOM	= ROW15+1;
	public static final int SEED_STARFLOWER	= ROW15+2;
	public static final int SEED_BLINDWEED	= ROW15+3;
	public static final int SEED_SUNGRASS	= ROW15+4;
	public static final int SEED_ICECAP		= ROW15+5;
	public static final int SEED_STORMVINE	= ROW15+6;
	public static final int SEED_SORROWMOSS	= ROW15+7;
	public static final int SEED_DREAMFOIL	= ROW15+8;
	public static final int SEED_EARTHROOT	= ROW15+9;
	public static final int SEED_FADELEAF	= ROW15+10;
	public static final int SEED_BLANDFRUIT	= ROW15+11;

	//Row Thirteen: Food
	public static final int MEAT		= ROW16+0;
	public static final int STEAK		= ROW16+1;
	public static final int OVERPRICED	= ROW16+2;
	public static final int CARPACCIO	= ROW16+3;
	public static final int BLANDFRUIT	= ROW16+4;
	public static final int RATION		= ROW16+5;
	public static final int PASTY		= ROW16+6;

	//Row Fourteen: Quest Items
	public static final int SKULL		= ROW17+0;
	public static final int DUST		= ROW17+1;
	public static final int CANDLE		= ROW17+2;
	public static final int EMBER		= ROW17+3;
	public static final int PICKAXE		= ROW17+4;
	public static final int ORE			= ROW17+5;
	public static final int TOKEN		= ROW17+6;
	public static final int LOCKPICK	= ROW17+7;

	//Row Fifteen: Containers/Bags
	public static final int VIAL	    = ROW18+0;
	public static final int POUCH	    = ROW18+1;
	public static final int HOLDER	    = ROW18+2;
	public static final int BANDOLIER   = ROW18+3;
	public static final int HOLSTER	    = ROW18+4;

	//Row Sixteen: Unused
	public static final int GEMSTONE_WHITE	= ROW19+0;
	public static final int GEMSTONE_RED	= ROW19+1;
	public static final int GEMSTONE_ORANGE	= ROW19+2;
	public static final int GEMSTONE_BROWN	= ROW19+3;
	public static final int GEMSTONE_YELLOW	= ROW19+4;
	public static final int GEMSTONE_GREEN	= ROW19+5;
	public static final int GEMSTONE_BLUE	= ROW19+6;
	public static final int GEMSTONE_VIOLET	= ROW19+7;
	public static final int GEMSTONE_BLACK	= ROW19+8;
	public static final int GEMSTONE_GRAY	= ROW19+9;

	//herpty derp all these comments are out of date
	public static final int SPELLBOOK_01	= ROW20+0;
	public static final int SPELLBOOK_02	= ROW20+1;
	public static final int SPELLBOOK_03	= ROW20+2;
	public static final int SPELLBOOK_04	= ROW20+3;
	public static final int SPELLBOOK_05	= ROW20+4;
	public static final int SPELLBOOK_06	= ROW20+5;
	public static final int SPELLBOOK_07	= ROW20+6;
	public static final int SPELLBOOK_08	= ROW20+7;
	public static final int SPELLBOOK_09	= ROW20+8;
	public static final int SPELLBOOK_10	= ROW20+9;
	public static final int SPELLBOOK_11	= ROW20+10;
	public static final int SPELLBOOK_12	= ROW20+11;
	public static final int SPELLBOOK_13	= ROW20+12;
	public static final int SPELLBOOK_14	= ROW20+13;
	public static final int SPELLBOOK_15	= ROW20+14;
	public static final int SPELLBOOK_16	= ROW20+15;
	public static final int SPELLBOOK_17	= ROW21+0;
	public static final int SPELLBOOK_18	= ROW21+1;
	public static final int SPELLBOOK_19	= ROW21+2;
	public static final int SPELLBOOK_20	= ROW21+3;
	public static final int SPELLBOOK_21	= ROW21+4;
	public static final int SPELLBOOK_22	= ROW21+5;
	public static final int SPELLBOOK_23	= ROW21+6;
	public static final int SPELLBOOK_24	= ROW21+7;
	public static final int SPELLBOOK_25	= ROW21+8;
	public static final int SPELLBOOK_26	= ROW21+9;
	public static final int SPELLBOOK_27	= ROW21+10;
	public static final int SPELLBOOK_28	= ROW21+11;
	public static final int SPELLBOOK_29	= ROW21+12;
	public static final int SPELLBOOK_30	= ROW21+13;
	public static final int SPELLBOOK_31	= ROW21+14;
	public static final int SPELLBOOK_32	= ROW21+15;
	public static final int SPELLBOOK_33	= ROW22+0;
	public static final int SPELLBOOK_34	= ROW22+1;
	public static final int SPELLBOOK_35	= ROW22+2;
	public static final int SPELLBOOK_36	= ROW22+3;
	public static final int SPELLBOOK_37	= ROW22+4;
	public static final int SPELLBOOK_38	= ROW22+5;
	public static final int SPELLBOOK_39	= ROW22+6;
	public static final int SPELLBOOK_40	= ROW22+7;

	//fart
	public static final int SPELL_IDENTIFY	= ROW23+0;

	//another fart
	public static final int BOOTS_LEATHER	= ROW24+0;

	//third fart
	public static final int GLOVES_LEATHER	= ROW25+0;
}
