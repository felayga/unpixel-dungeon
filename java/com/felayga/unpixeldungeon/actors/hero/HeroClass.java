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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Challenges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.armor.ClothArmor;
import com.felayga.unpixeldungeon.items.armor.PlateArmor;
import com.felayga.unpixeldungeon.items.artifacts.CloakOfShadows;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.PotionBandolier;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.potions.PotionOfAcid;
import com.felayga.unpixeldungeon.items.potions.PotionOfBooze;
import com.felayga.unpixeldungeon.items.potions.PotionOfExperience;
import com.felayga.unpixeldungeon.items.potions.PotionOfExtraHealing;
import com.felayga.unpixeldungeon.items.potions.PotionOfFrost;
import com.felayga.unpixeldungeon.items.potions.PotionOfFullHealing;
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
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.items.weapon.melee.BattleAxe;
import com.felayga.unpixeldungeon.items.weapon.melee.MagesStaff;
import com.felayga.unpixeldungeon.items.wands.WandOfMagicMissile;
import com.felayga.unpixeldungeon.items.weapon.melee.Dagger;
import com.felayga.unpixeldungeon.items.weapon.melee.ShortSword;
import com.felayga.unpixeldungeon.items.weapon.missiles.Dart;
import com.felayga.unpixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR( "warrior" ), MAGE( "mage" ), ROGUE( "rogue" ), HUNTRESS( "huntress" );
	
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
		}

		hero.updateAwareness();
	}

	private static void initCommon( Hero hero ) {
		if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
			(hero.belongings.armor = new ClothArmor()).identify();

		if (!Dungeon.isChallenged(Challenges.NO_FOOD))
			new Food().identify().collect();
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

	private static void initWarrior( Hero hero ) {
		//hero.STR = hero.STR + 1;

		(hero.belongings.weapon = new ShortSword()).identify();
		Dart darts = new Dart( 8 );
		darts.identify().collect();

		Dungeon.quickslot.setSlot(0, darts);

		hero.belongings.armor = (Armor)new PlateArmor().upgrade(20);
		hero.belongings.weapon = (Weapon)new BattleAxe().upgrade(20);
		hero.belongings.ring1 = (Ring)new RingOfWealth().upgrade(20);

		new PotionBandolier().identify().collect();

		new PotionOfAcid().quantity(20).identify().collect();
		new PotionOfBooze().quantity(20).identify().collect();
		new PotionOfExperience().quantity(20).identify().collect();
		new PotionOfExtraHealing().quantity(20).identify().collect();
		new PotionOfFullHealing().quantity(20).identify().collect();
		new PotionOfFrost().quantity(20).identify().collect();
		new PotionOfHealing().quantity(20).identify().collect();
		new PotionOfInvisibility().quantity(20).identify().collect();
		new PotionOfLevitation().quantity(20).identify().collect();
		new PotionOfLiquidFlame().quantity(20).identify().collect();
		new PotionOfMight().quantity(20).identify().collect();
		new PotionOfMindVision().quantity(20).identify().collect();
		new PotionOfParalyticGas().quantity(20).identify().collect();
		new PotionOfPurity().quantity(20).identify().collect();
		new PotionOfStrength().quantity(20).identify().collect();
		new PotionOfToxicGas().quantity(20).identify().collect();
		new PotionOfWater().quantity(20).identify().collect();



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
		hero.belongings.ring1.activate( hero );

		Dart darts = new Dart( 8 );
		darts.identify().collect();

		Dungeon.quickslot.setSlot(0, cloak);
		if (ShatteredPixelDungeon.quickSlots() > 1)
			Dungeon.quickslot.setSlot(1, darts);

		new ScrollOfMagicMapping().setKnown();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();

		Dungeon.quickslot.setSlot(0, boomerang);

		new PotionOfMindVision().setKnown();
	}
	
	public String title() {
		return title;
	}
	
	public String spritesheet() {
		
		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		}
		
		return null;
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
