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
package com.felayga.unpixeldungeon.actors.hero;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Challenges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.armor.boots.LeatherBoots;
import com.felayga.unpixeldungeon.items.armor.gloves.LeatherGloves;
import com.felayga.unpixeldungeon.items.armor.heavy.BandedArmor;
import com.felayga.unpixeldungeon.items.armor.heavy.FullPlateArmor;
import com.felayga.unpixeldungeon.items.armor.light.ClothArmor;
import com.felayga.unpixeldungeon.items.armor.heavy.HalfPlateArmor;
import com.felayga.unpixeldungeon.items.armor.light.LeatherArmor;
import com.felayga.unpixeldungeon.items.armor.light.StuddedLeatherArmor;
import com.felayga.unpixeldungeon.items.armor.medium.HideArmor;
import com.felayga.unpixeldungeon.items.armor.medium.MailArmor;
import com.felayga.unpixeldungeon.items.armor.medium.ScaleArmor;
import com.felayga.unpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.felayga.unpixeldungeon.items.artifacts.CloakOfShadows;
import com.felayga.unpixeldungeon.items.artifacts.UnstableSpellbook;
import com.felayga.unpixeldungeon.items.bags.SeedPouch;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.gemstones.randomized.GlassGemstone;
import com.felayga.unpixeldungeon.items.gemstones.white.DilithiumGemstone;
import com.felayga.unpixeldungeon.items.potions.PotionOfAcid;
import com.felayga.unpixeldungeon.items.potions.PotionOfBooze;
import com.felayga.unpixeldungeon.items.potions.PotionOfExperience;
import com.felayga.unpixeldungeon.items.potions.PotionOfExtraHealing;
import com.felayga.unpixeldungeon.items.potions.PotionOfFrost;
import com.felayga.unpixeldungeon.items.potions.PotionOfFullHealing;
import com.felayga.unpixeldungeon.items.potions.PotionOfHallucination;
import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.items.potions.PotionOfInvisibility;
import com.felayga.unpixeldungeon.items.potions.PotionOfLevitation;
import com.felayga.unpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.felayga.unpixeldungeon.items.potions.PotionOfMight;
import com.felayga.unpixeldungeon.items.potions.PotionOfMindVision;
import com.felayga.unpixeldungeon.items.potions.PotionOfParalyticGas;
import com.felayga.unpixeldungeon.items.potions.PotionOfPurity;
import com.felayga.unpixeldungeon.items.potions.PotionOfStrength;
import com.felayga.unpixeldungeon.items.potions.PotionOfToxicGas;
import com.felayga.unpixeldungeon.items.potions.PotionOfWater;
import com.felayga.unpixeldungeon.items.rings.Ring;
import com.felayga.unpixeldungeon.items.rings.RingOfWealth;
import com.felayga.unpixeldungeon.items.scrolls.BlankScroll;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.items.spells.inventory.IdentifySpell;
import com.felayga.unpixeldungeon.items.tools.digging.Pickaxe;
import com.felayga.unpixeldungeon.items.tools.unlocking.LockPick;
import com.felayga.unpixeldungeon.items.tools.unlocking.SkeletonKey;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.BattleAxe;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.items.wands.WandOfMagicMissile;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Dagger;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.ShortSword;
import com.felayga.unpixeldungeon.items.weapon.missiles.Dart;
import com.felayga.unpixeldungeon.items.weapon.missiles.Boomerang;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.plants.Earthroot;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public enum HeroClass {

	WARRIOR( "warrior" ), MAGE( "mage" ), ROGUE( "rogue" ), HUNTRESS( "huntress" ), DEBUG( "debug" ), NONE( null );

	public static int toInt(HeroClass c) {
		switch(c)
		{
			case WARRIOR:
				return 0;
			case ROGUE:
				return 1;
			case MAGE:
				return 2;
			case HUNTRESS:
				return 3;
			case DEBUG:
				return 4;
		}

		return -1;
	}

	public static HeroClass toHeroClass(int index) {
		switch(index) {
			case 0:
				return WARRIOR;
			case 1:
				return ROGUE;
			case 2:
				return MAGE;
			case 3:
				return HUNTRESS;
			case 4:
				return DEBUG;
			default:
				return NONE;
		}
	}
	
	private String title;
	
	private HeroClass( String title ) {
		this.title = title;
	}
	
	public static final String[] WAR_PERKS = {
		"The Warrior starts with 11 points of Strength.",
		"The Warrior starts with a unique short sword. This sword can be later \"reforged\" to upgrade another melee weapon.",
		"The Warrior is less proficient with missile weapons.",
		"Any piece of food restores some health when eaten.",
		"Potions of Strength are identified from the beginning.",
	};
	
	public static final String[] MAG_PERKS = {
		"The Mage starts with a unique Staff, which can be imbued with the properties of a wand.",
		"The Mage's staff can be used as a melee weapon or a more powerful wand.",
		"The Mage partially identifies wands after using them.",
		"When eaten, any piece of food restores 1 charge for all wands in the inventory.",
		"Scrolls of Upgrade are identified from the beginning."
	};
	
	public static final String[] ROG_PERKS = {
		"The Rogue starts with a unique Cloak of Shadows.",
		"The Rogue identifies a type of a ring on equipping it.",
		"The Rogue is proficient with light armor, dodging better with excess strength.",
		"The Rogue is more proficient in detecting hidden doors and traps.",
		"The Rogue can go without food longer.",
		"Scrolls of Magic Mapping are identified from the beginning."
	};
	
	public static final String[] HUN_PERKS = {
		"The Huntress starts with a unique upgradeable boomerang.",
		"The Huntress is proficient with missile weapons, getting bonus damage from excess strength.",
		"The Huntress is able to recover a single used missile weapon from each enemy.",
		"The Huntress senses neighbouring monsters even if they are hidden behind obstacles.",
		"Potions of Mind Vision are identified from the beginning."
	};

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case DEBUG:
				initDebug(hero);
				break;
		}

		hero.updateAwareness();
	}

	private static void initCommon( Hero hero ) {
		if (!Dungeon.isChallenged(Challenges.NO_ARMOR)) {
			(hero.belongings.armor = new ClothArmor()).identify();
		}

		if (!Dungeon.isChallenged(Challenges.NO_FOOD)) {
			hero.belongings.collect(new Food().quantity(Random.Int(4, 8)).identify());
		}
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initDebug( Hero hero ){
		(hero.belongings.weapon = new ShortSword()).identify();
		Dart darts = new Dart( 8 );
		hero.belongings.collect(darts.identify());

		Dungeon.quickslot.setSlot(0, darts);

		hero.belongings.armor = (Armor)new HalfPlateArmor().upgrade(darts, 20);
		hero.belongings.weapon = (Weapon)new BattleAxe().upgrade(darts, 20);
		hero.belongings.ring1 = (Ring)new RingOfWealth().upgrade(darts, 20);

		hero.belongings.collect(new PotionOfAcid().quantity(20).identify());
		hero.belongings.collect(new PotionOfBooze().quantity(20).identify());
		hero.belongings.collect(new PotionOfExperience().quantity(20).identify());
		hero.belongings.collect(new PotionOfExtraHealing().quantity(20).identify());
		hero.belongings.collect(new PotionOfFullHealing().quantity(20).identify());
		hero.belongings.collect(new PotionOfFrost().quantity(20).identify());
		hero.belongings.collect(new PotionOfHealing().quantity(20).identify());
		hero.belongings.collect(new PotionOfInvisibility().quantity(20).identify());
		hero.belongings.collect(new PotionOfLevitation().quantity(20).identify());
		hero.belongings.collect(new PotionOfLiquidFlame().quantity(20).identify());
		hero.belongings.collect(new PotionOfMight().quantity(20).identify());
		hero.belongings.collect(new PotionOfMindVision().quantity(20).identify());
		hero.belongings.collect(new PotionOfParalyticGas().quantity(20).identify());
		hero.belongings.collect(new PotionOfPurity().quantity(20).identify());
		hero.belongings.collect(new PotionOfStrength().quantity(20).identify());
		hero.belongings.collect(new PotionOfToxicGas().quantity(20).identify());
		hero.belongings.collect(new PotionOfWater().quantity(20).identify());
		hero.belongings.collect(new PotionOfHallucination().quantity(20).identify());
		hero.belongings.collect(new ScrollOfMagicMapping().quantity(30).bucStatus(BUCStatus.Cursed, false));
		hero.belongings.collect(new ScrollOfMagicMapping().quantity(30).bucStatus(BUCStatus.Uncursed, false));
		hero.belongings.collect(new ScrollOfMagicMapping().quantity(30).bucStatus(BUCStatus.Blessed, false));
		hero.belongings.collect(new ScrollOfTeleportation().quantity(15).bucStatus(BUCStatus.Cursed, false));
		hero.belongings.collect(new ScrollOfIdentify().quantity(10).bucStatus(BUCStatus.Cursed, false));
		hero.belongings.collect(new ScrollOfIdentify().quantity(10).bucStatus(BUCStatus.Uncursed, false));
		hero.belongings.collect(new ScrollOfIdentify().quantity(10).bucStatus(BUCStatus.Blessed, false));
		hero.belongings.collect(new LockPick());
		hero.belongings.collect(new SkeletonKey());
		hero.belongings.collect(new Pickaxe());
		hero.belongings.collect(new UnstableSpellbook());
		hero.belongings.collect(new ChaliceOfBlood());
		hero.belongings.collect(new GlassGemstone());
		hero.belongings.collect(new GlassGemstone());
		hero.belongings.collect(new GlassGemstone());
		hero.belongings.collect(new GlassGemstone());
		hero.belongings.collect(new DilithiumGemstone());
		hero.belongings.collect(new BlankScroll());
		hero.belongings.collect(new IdentifySpell());
		hero.belongings.collect(new SeedPouch());
		hero.belongings.collect(new Earthroot.Seed());
		hero.belongings.collect(new LeatherBoots());
		hero.belongings.collect(new LeatherGloves());
		hero.belongings.collect(new ClothArmor());
		hero.belongings.collect(new LeatherArmor());
		hero.belongings.collect(new StuddedLeatherArmor());
		hero.belongings.collect(new MailArmor());
		hero.belongings.collect(new HideArmor());
		hero.belongings.collect(new ScaleArmor());
		hero.belongings.collect(new HalfPlateArmor());
		hero.belongings.collect(new FullPlateArmor());
		hero.belongings.collect(new BandedArmor());
	}

	private static void initWarrior( Hero hero ) {
		//hero.STR = hero.STR + 1;

		(hero.belongings.armor = new ClothArmor()).identify();
		(hero.belongings.weapon = new ShortSword()).identify();
		Dart darts = new Dart( 8 );
		hero.belongings.collect(darts.identify());

		Dungeon.quickslot.setSlot(0, darts);

		new PotionOfStrength().setKnown();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff = new MagesStaff(new WandOfMagicMissile());
		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().setKnown();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.ring1 = cloak).identify();
		hero.belongings.ring1.activate(hero);

		Dart darts = new Dart( 8 );
		hero.belongings.collect(darts.identify());

		Dungeon.quickslot.setSlot(0, cloak);
		if (ShatteredPixelDungeon.quickSlots() > 1)
			Dungeon.quickslot.setSlot(1, darts);

		new ScrollOfMagicMapping().setKnown();
	}

	private static void initHuntress( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		hero.belongings.collect(boomerang.identify());

		Dungeon.quickslot.setSlot(0, boomerang);

		new PotionOfMindVision().setKnown();
	}
	
	public String title() {
		return title;
	}
	
	public String[] perks() {
		
		switch (this) {
		case WARRIOR:
			return WAR_PERKS;
		case MAGE:
			return MAG_PERKS;
		case ROGUE:
			return ROG_PERKS;
		case HUNTRESS:
			return HUN_PERKS;
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
