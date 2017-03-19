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
package com.felayga.unpixeldungeon.actors.hero;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Challenges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Spinach;
import com.felayga.unpixeldungeon.items.Bomb;
import com.felayga.unpixeldungeon.items.armor.cloak.LeatherCloak;
import com.felayga.unpixeldungeon.items.armor.cloak.randomized.CloakOfDisplacement;
import com.felayga.unpixeldungeon.items.armor.cloak.randomized.CloakOfMagicResistance;
import com.felayga.unpixeldungeon.items.armor.cloak.randomized.CloakOfProtection;
import com.felayga.unpixeldungeon.items.books.spellbook.IdentifyBook;
import com.felayga.unpixeldungeon.items.potions.PotionOfToxicGas;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.felayga.unpixeldungeon.items.tools.Torch;
import com.felayga.unpixeldungeon.items.armor.amulet.mask.Blindfold;
import com.felayga.unpixeldungeon.items.armor.boots.LeatherBoots;
import com.felayga.unpixeldungeon.items.armor.cloak.randomized.CloakOfInvisibility;
import com.felayga.unpixeldungeon.items.armor.gloves.LeatherGloves;
import com.felayga.unpixeldungeon.items.armor.heavy.HalfPlateArmor;
import com.felayga.unpixeldungeon.items.armor.medium.ScaleArmor;
import com.felayga.unpixeldungeon.items.artifacts.CloakOfShadows;
import com.felayga.unpixeldungeon.items.artifacts.MasterThievesArmband;
import com.felayga.unpixeldungeon.items.bags.ScrollHolder;
import com.felayga.unpixeldungeon.items.bags.SeedPouch;
import com.felayga.unpixeldungeon.items.food.CannedFood;
import com.felayga.unpixeldungeon.items.food.Corpse;
import com.felayga.unpixeldungeon.items.food.Ration;
import com.felayga.unpixeldungeon.items.rings.RingOfWealth;
import com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll.ScrollOfIdentify;
import com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.items.scrolls.positionscroll.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.items.tools.CanOpener;
import com.felayga.unpixeldungeon.items.tools.CanningKit;
import com.felayga.unpixeldungeon.items.tools.Towel;
import com.felayga.unpixeldungeon.items.tools.Tricorder;
import com.felayga.unpixeldungeon.items.tools.digging.Pickaxe;
import com.felayga.unpixeldungeon.items.wands.WandOfCreateMonster;
import com.felayga.unpixeldungeon.items.wands.WandOfHaste;
import com.felayga.unpixeldungeon.items.wands.WandOfLight;
import com.felayga.unpixeldungeon.items.wands.WandOfLightning;
import com.felayga.unpixeldungeon.items.wands.WandOfMagicMissile;
import com.felayga.unpixeldungeon.items.wands.WandOfRegrowth;
import com.felayga.unpixeldungeon.items.wands.WandOfTunneling;
import com.felayga.unpixeldungeon.items.weapon.ammunition.martial.Arrow;
import com.felayga.unpixeldungeon.items.weapon.ammunition.simple.Rock;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.BattleAxe;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.Longsword;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.items.weapon.missiles.martial.Boomerang;
import com.felayga.unpixeldungeon.items.weapon.missiles.simple.Dart;
import com.felayga.unpixeldungeon.items.weapon.ranged.simple.Sling;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.plants.Starflower;
import com.felayga.unpixeldungeon.unPixelDungeon;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public enum HeroClass {

    WARRIOR(0, "warrior"),
    MAGE(1, "mage"),
    ROGUE(2, "rogue"),
    HUNTRESS(3, "huntress"),
    DEBUG(4, "debug"),
    NONE(-1, null);

    public static HeroClass fromInt(int index) {
        switch (index) {
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

    public final int value;
    public final String name;

    HeroClass(int value, String name) {
        this.value = value;
        this.name = name;
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

    public void initHero(Hero hero) {

        hero.heroClass = this;

        initCommon(hero);

        switch (this) {
            case WARRIOR:
                initWarrior(hero);
                break;

            case MAGE:
                initMage(hero);
                break;

            case ROGUE:
                initRogue(hero);
                break;

            case HUNTRESS:
                initHuntress(hero);
                break;

            case DEBUG:
                initDebug(hero);
                break;
        }
    }

    private static void initCommon(Hero hero) {
        if (!Dungeon.isChallenged(Challenges.NO_ARMOR)) {
            //(hero.belongings.armor = new ClothArmor()).identify();
        }

        if (!Dungeon.isChallenged(Challenges.NO_FOOD)) {
            hero.belongings.collect(new Ration().quantity(Random.IntRange(4, 8)).identify());
        }

        hero.belongings.collectEquip(new LeatherBoots());
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

    private static int[] generateAttributes(int strcon, int dexcha, int intwis, int remainder, int strconChance, int dexchaChance, int intwisChance) {
        int sum = strconChance + dexchaChance + intwisChance;

        while (remainder > 0) {
            int test = Random.Int(sum);

            if (test < strconChance) {
                strcon++;
            } else if (test < strconChance + dexchaChance) {
                dexcha++;
            } else {
                intwis++;
            }

            remainder--;
        }

        return new int[]{strcon, dexcha, intwis};
    }

    private static void initDebug(Hero hero) {
        hero.initAttributes(generateAttributes(10, 10, 10, 8, 33, 33, 33));
        hero.hpPerLevel = 10;
        hero.mpPerLevel = 10;
        hero.MP = hero.MT = 100;
        hero.spellSkill = 3;

        Dart darts = new Dart();
        darts.quantity(8).bucStatus(BUCStatus.Blessed, true).identify();
        hero.belongings.collectEquip(darts);

        hero.weaponSkill = WeaponSkill.Simple;

        hero.belongings.collectEquip(new HalfPlateArmor());
        hero.belongings.armor().upgrade(darts, 20);

        hero.belongings.collectEquip(new BattleAxe());
        hero.belongings.weapon().upgrade(darts, 20);

        hero.belongings.collect(new Arrow().quantity(20));
        hero.belongings.collect(new Sling());
        hero.belongings.collect(new Rock().quantity(20));

        hero.belongings.collectEquip(new RingOfWealth());
        hero.belongings.ring1().upgrade(darts, 20);

        hero.belongings.collect(new Pickaxe().upgrade(darts, 20));

        hero.belongings.collect(new SeedPouch());
        hero.belongings.collect(new ScrollHolder());

        hero.belongings.collectEquip(new MasterThievesArmband());
        hero.belongings.collect(new Bomb());
        hero.belongings.collect(new Bomb());
        hero.belongings.collect(new Bomb());

        hero.belongings.collect(new PotionOfToxicGas().quantity(5));

        /*
        hero.belongings.collect(new PotionOfWater().bucStatus(BUCStatus.Blessed, false));
        hero.belongings.collect(new PotionOfWater().bucStatus(BUCStatus.Uncursed, false));
        hero.belongings.collect(new PotionOfWater().bucStatus(BUCStatus.Cursed, false));
        hero.belongings.collect(new PotionOfWater().bucStatus(BUCStatus.Blessed, true));
        hero.belongings.collect(new PotionOfWater().bucStatus(BUCStatus.Uncursed, true));
        hero.belongings.collect(new PotionOfWater().bucStatus(BUCStatus.Cursed, true));

        hero.belongings.collect(new ScrollOfIdentify().bucStatus(BUCStatus.Blessed, true));
        */

        hero.belongings.collect(new ScrollOfMagicMapping().bucStatus(BUCStatus.Blessed, true).quantity(5).identify());
        hero.belongings.collect(new ScrollOfTeleportation().bucStatus(BUCStatus.Blessed, true).quantity(5).identify());
        hero.belongings.collect(new ScrollOfMirrorImage().bucStatus(BUCStatus.Blessed, true).quantity(5).identify());

        //hero.belongings.collect(new Sling());
        //hero.belongings.collect(new Rock(5));

        //hero.belongings.collect(new IceBox().random());
        hero.belongings.collect(new Starflower.Seed().quantity(10));
        hero.belongings.collect(new CanningKit().random().identify());

        hero.belongings.collect(new IdentifyBook());

        /*
        hero.belongings.collect(new PotionOfOil().bucStatus(BUCStatus.Uncursed, true).quantity(5));
        hero.belongings.collect(new PotionOfWater().bucStatus(BUCStatus.Uncursed, true).quantity(5));
        hero.belongings.collect(new PotionOfBlindness().identify().quantity(2));
        */
        //hero.belongings.collect(new PotionOfMonsterDetection().bucStatus(BUCStatus.Blessed, true).identify().quantity(10));
        hero.belongings.collect(new WandOfRegrowth().bucStatus(BUCStatus.Blessed, true).identify());
        hero.belongings.collect(new WandOfLightning().bucStatus(BUCStatus.Uncursed, true).identify());
        hero.belongings.collect(new WandOfCreateMonster().random().bucStatus(BUCStatus.Blessed, true).identify());
        hero.belongings.collect(new WandOfHaste().random().bucStatus(BUCStatus.Blessed, true).identify());
        hero.belongings.collect(new WandOfLight().random().bucStatus(BUCStatus.Blessed, true).identify());
        hero.belongings.collect(new WandOfTunneling().random().bucStatus(BUCStatus.Blessed, true).identify());

        hero.belongings.collect(new Blindfold());
        hero.belongings.collect(new Towel());
        hero.belongings.collect(new Torch().random());
        hero.belongings.collect(new Tricorder());

        hero.belongings.collect(new ScrollOfIdentify().bucStatus(BUCStatus.Uncursed, true).quantity(5).identify());

        hero.belongings.collect(new CannedFood(new Corpse(new Spinach()), false).bucStatus(BUCStatus.Blessed, false));
        hero.belongings.collect(new CannedFood(new Corpse(new Spinach()), false).bucStatus(BUCStatus.Uncursed, false));
        hero.belongings.collect(new CannedFood(new Corpse(new Spinach()), false).bucStatus(BUCStatus.Cursed, false));
        hero.belongings.collect(new CanOpener());
        //hero.belongings.collect(new Corpse(new KoboldZombie()));

        hero.belongings.collect(new LeatherCloak());
        hero.belongings.collect(new CloakOfDisplacement());
        hero.belongings.collect(new CloakOfInvisibility());
        hero.belongings.collect(new CloakOfMagicResistance());
        hero.belongings.collect(new CloakOfProtection());

    }

    private static void initWarrior(Hero hero) {
        hero.initAttributes(generateAttributes(12, 13, 11, 2, 50, 20, 30));
        hero.hpPerLevel = 10;
        hero.mpPerLevel = 2;

        hero.belongings.equip(new ScaleArmor());
        hero.belongings.armor().identify();

        hero.belongings.equip(new Longsword());
        hero.belongings.weapon().identify();

        hero.belongings.equip(new LeatherGloves());
        hero.belongings.gloves().identify();

        hero.belongings.equip(new Dart());
        hero.belongings.ammo().quantity(8).identify();

        hero.weaponSkill = WeaponSkill.Martial;
    }

    private static void initMage(Hero hero) {
        hero.initAttributes(generateAttributes(7, 7, 9, 15, 30, 30, 40));
        hero.hpPerLevel = 4;
        hero.mpPerLevel = 8;

        MagesStaff staff = new MagesStaff(new WandOfMagicMissile());
        //(hero.belongings.weapon = staff).identify();
        //((IActivateable)hero.belongings.weapon).activate(hero);

        Dungeon.quickslot.setSlot(0, staff);

        new ScrollOfUpgrade().setKnown();
    }

    private static void initRogue(Hero hero) {
        hero.initAttributes(generateAttributes(7, 8, 7, 16, 40, 40, 20));
        hero.hpPerLevel = 6;
        hero.mpPerLevel = 6;

        //(hero.belongings.weapon = new Dagger()).identify();

        CloakOfShadows cloak = new CloakOfShadows();
        //(hero.belongings.ring1 = cloak).identify();
        //((IActivateable)hero.belongings.ring1).activate(hero);

        Dart darts = new Dart();
        hero.belongings.collect(darts.quantity(8).identify());

        Dungeon.quickslot.setSlot(0, cloak);
        if (unPixelDungeon.quickSlots() > 1)
            Dungeon.quickslot.setSlot(1, darts);

        new ScrollOfMagicMapping().setKnown();
    }

    private static void initHuntress(Hero hero) {
        hero.initAttributes(generateAttributes(13, 8, 13, 4, 50, 30, 20));
        hero.hpPerLevel = 8;
        hero.mpPerLevel = 4;

        //(hero.belongings.weapon = new Dagger()).identify();
        Boomerang boomerang = new Boomerang();
        hero.belongings.collect(boomerang.identify());

        Dungeon.quickslot.setSlot(0, boomerang);

        //new PotionOfMindVision().setKnown();
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

    private static final String CLASS = "class";

    public void storeInBundle(Bundle bundle) {
        bundle.put(CLASS, toString());
    }

    public static HeroClass restoreInBundle(Bundle bundle) {
        String value = bundle.getString(CLASS);
        return value.length() > 0 ? valueOf(value) : ROGUE;
    }
}
