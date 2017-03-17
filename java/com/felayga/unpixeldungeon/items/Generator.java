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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.armor.heavy.HalfPlateArmor;
import com.felayga.unpixeldungeon.items.armor.light.ClothArmor;
import com.felayga.unpixeldungeon.items.armor.light.LeatherArmor;
import com.felayga.unpixeldungeon.items.armor.medium.MailArmor;
import com.felayga.unpixeldungeon.items.armor.medium.ScaleArmor;
import com.felayga.unpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.felayga.unpixeldungeon.items.artifacts.Artifact_old;
import com.felayga.unpixeldungeon.items.artifacts.CapeOfThorns;
import com.felayga.unpixeldungeon.items.artifacts.CloakOfShadows;
import com.felayga.unpixeldungeon.items.artifacts.EtherealChains;
import com.felayga.unpixeldungeon.items.artifacts.HornOfPlenty;
import com.felayga.unpixeldungeon.items.artifacts.MasterThievesArmband;
import com.felayga.unpixeldungeon.items.artifacts.TalismanOfForesight;
import com.felayga.unpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.felayga.unpixeldungeon.items.artifacts.UnstableSpellbook;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.IceBox;
import com.felayga.unpixeldungeon.items.bags.LargeBox;
import com.felayga.unpixeldungeon.items.bags.LargeChest;
import com.felayga.unpixeldungeon.items.food.Blandfruit;
import com.felayga.unpixeldungeon.items.food.CannedFood;
import com.felayga.unpixeldungeon.items.food.CramRation;
import com.felayga.unpixeldungeon.items.food.LembasWafer;
import com.felayga.unpixeldungeon.items.food.Ration;
import com.felayga.unpixeldungeon.items.food.TripeRation;
import com.felayga.unpixeldungeon.items.food.fruit.Apple;
import com.felayga.unpixeldungeon.items.food.fruit.Banana;
import com.felayga.unpixeldungeon.items.food.fruit.Grapes;
import com.felayga.unpixeldungeon.items.food.fruit.Orange;
import com.felayga.unpixeldungeon.items.food.fruit.Pear;
import com.felayga.unpixeldungeon.items.food.vegetable.BellPepper;
import com.felayga.unpixeldungeon.items.food.vegetable.Carrot;
import com.felayga.unpixeldungeon.items.food.vegetable.Garlic;
import com.felayga.unpixeldungeon.items.food.vegetable.GingerRoot;
import com.felayga.unpixeldungeon.items.food.vegetable.Potato;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.potions.PotionOfAcid;
import com.felayga.unpixeldungeon.items.potions.PotionOfAwareness;
import com.felayga.unpixeldungeon.items.potions.PotionOfBlindness;
import com.felayga.unpixeldungeon.items.potions.PotionOfBooze;
import com.felayga.unpixeldungeon.items.potions.PotionOfConfusion;
import com.felayga.unpixeldungeon.items.potions.PotionOfEnergy;
import com.felayga.unpixeldungeon.items.potions.PotionOfFrost;
import com.felayga.unpixeldungeon.items.potions.PotionOfFruitJuice;
import com.felayga.unpixeldungeon.items.potions.PotionOfFullEnergy;
import com.felayga.unpixeldungeon.items.potions.PotionOfFullHealing;
import com.felayga.unpixeldungeon.items.potions.PotionOfFullRestoration;
import com.felayga.unpixeldungeon.items.potions.PotionOfGainAbility;
import com.felayga.unpixeldungeon.items.potions.PotionOfGainLevel;
import com.felayga.unpixeldungeon.items.potions.PotionOfHallucination;
import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.items.potions.PotionOfHeroism;
import com.felayga.unpixeldungeon.items.potions.PotionOfInvisibility;
import com.felayga.unpixeldungeon.items.potions.PotionOfIpecac;
import com.felayga.unpixeldungeon.items.potions.PotionOfLevitation;
import com.felayga.unpixeldungeon.items.potions.PotionOfMonsterDetection;
import com.felayga.unpixeldungeon.items.potions.PotionOfObjectDetection;
import com.felayga.unpixeldungeon.items.potions.PotionOfOil;
import com.felayga.unpixeldungeon.items.potions.PotionOfParalyticGas;
import com.felayga.unpixeldungeon.items.potions.PotionOfPolymorph;
import com.felayga.unpixeldungeon.items.potions.PotionOfPurity;
import com.felayga.unpixeldungeon.items.potions.PotionOfRestoration;
import com.felayga.unpixeldungeon.items.potions.PotionOfRestoreAbility;
import com.felayga.unpixeldungeon.items.potions.PotionOfSeeInvisible;
import com.felayga.unpixeldungeon.items.potions.PotionOfShielding;
import com.felayga.unpixeldungeon.items.potions.PotionOfSickness;
import com.felayga.unpixeldungeon.items.potions.PotionOfSleeping;
import com.felayga.unpixeldungeon.items.potions.PotionOfSpeed;
import com.felayga.unpixeldungeon.items.potions.PotionOfToxicGas;
import com.felayga.unpixeldungeon.items.potions.PotionOfWater;
import com.felayga.unpixeldungeon.items.rings.Ring;
import com.felayga.unpixeldungeon.items.rings.RingOfAccuracy;
import com.felayga.unpixeldungeon.items.rings.RingOfElements;
import com.felayga.unpixeldungeon.items.rings.RingOfEvasion;
import com.felayga.unpixeldungeon.items.rings.RingOfForce;
import com.felayga.unpixeldungeon.items.rings.RingOfFuror;
import com.felayga.unpixeldungeon.items.rings.RingOfHaste;
import com.felayga.unpixeldungeon.items.rings.RingOfMagic;
import com.felayga.unpixeldungeon.items.rings.RingOfMight;
import com.felayga.unpixeldungeon.items.rings.RingOfSharpshooting;
import com.felayga.unpixeldungeon.items.rings.RingOfTenacity;
import com.felayga.unpixeldungeon.items.rings.RingOfWealth;
import com.felayga.unpixeldungeon.items.scrolls.BlankScroll;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll.ScrollOfIdentify;
import com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll.ScrollOfLullaby;
import com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.items.scrolls.inventoryscroll.ScrollOfMagicalInfusion;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRage;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.felayga.unpixeldungeon.items.scrolls.positionscroll.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll.ScrollOfTerror;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll.ScrollOfPsionicBlast;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.felayga.unpixeldungeon.items.wands.WandOfDisintegration;
import com.felayga.unpixeldungeon.items.wands.WandOfFireblast;
import com.felayga.unpixeldungeon.items.wands.WandOfFrost;
import com.felayga.unpixeldungeon.items.wands.WandOfLightning;
import com.felayga.unpixeldungeon.items.wands.WandOfMagicMissile;
import com.felayga.unpixeldungeon.items.wands.WandOfRegrowth;
import com.felayga.unpixeldungeon.items.wands.WandOfVenom;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.BattleAxe;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.Glaive;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.Longsword;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.Sword;
import com.felayga.unpixeldungeon.items.weapon.melee.martial.WarHammer;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.dagger.Dagger;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Knuckles;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Mace;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Quarterstaff;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Spear;
import com.felayga.unpixeldungeon.items.weapon.missiles.martial.Boomerang;
import com.felayga.unpixeldungeon.items.weapon.missiles.martial.Javelin;
import com.felayga.unpixeldungeon.items.weapon.missiles.martial.Shuriken;
import com.felayga.unpixeldungeon.items.weapon.missiles.martial.Tomahawk;
import com.felayga.unpixeldungeon.items.weapon.missiles.simple.CurareDart;
import com.felayga.unpixeldungeon.items.weapon.missiles.simple.Dart;
import com.felayga.unpixeldungeon.items.weapon.missiles.simple.IncendiaryDart;
import com.felayga.unpixeldungeon.plants.BlandfruitBush;
import com.felayga.unpixeldungeon.plants.Blindweed;
import com.felayga.unpixeldungeon.plants.Bloodleaf;
import com.felayga.unpixeldungeon.plants.Deathroot;
import com.felayga.unpixeldungeon.plants.Dreamfoil;
import com.felayga.unpixeldungeon.plants.Earthroot;
import com.felayga.unpixeldungeon.plants.Fadeleaf;
import com.felayga.unpixeldungeon.plants.Firebloom;
import com.felayga.unpixeldungeon.plants.Icecap;
import com.felayga.unpixeldungeon.plants.Moongrass;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.plants.Sorrowmoss;
import com.felayga.unpixeldungeon.plants.Starflower;
import com.felayga.unpixeldungeon.plants.Stoneberry;
import com.felayga.unpixeldungeon.plants.Stormvine;
import com.felayga.unpixeldungeon.plants.Sungrass;
import com.felayga.unpixeldungeon.plants.Swampweed;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//import com.felayga.unpixeldungeon.actors.mobs.npcs.Ghost;

public class Generator {

	public static enum Category {
		WEAPON(150, Weapon.class),
		ARMOR(100, Armor.class),
		POTION(500, Potion.class),
		SCROLL(400, Scroll.class),
		WAND(40, Wand.class),
		RING(15, Ring.class),
		ARTIFACT(15, Artifact_old.class),
		SEED(50, Plant.Seed.class),
		FOOD(0, Ration.class),
		GOLD(500, Gold.class),
        GEMSTONE(400, Gemstone.class),
        CONTAINER(150, Bag.class);

		public Class<?>[] classes;
		public float[] probs;

		public float prob;
		public Class<? extends Item> superClass;

		private Category(float prob, Class<? extends Item> superClass) {
			this.prob = prob;
			this.superClass = superClass;
		}

		public static int order(Item item) {
			for (int i = 0; i < values().length; i++) {
				if (values()[i].superClass.isInstance(item)) {
					return i;
				}
			}

			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
	}

	;

	private static HashMap<Category, Float> categoryProbs = new HashMap<Generator.Category, Float>();

	private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1};

	static {

        Category.GOLD.classes = new Class<?>[]{
                Gold.class};
        Category.GOLD.probs = new float[]{1};

        Category.SCROLL.classes = new Class<?>[]{
                ScrollOfIdentify.class,
                ScrollOfTeleportation.class,
                ScrollOfRemoveCurse.class,
                ScrollOfUpgrade.class,
                ScrollOfRecharging.class,
                ScrollOfMagicMapping.class,
                ScrollOfRage.class,
                ScrollOfTerror.class,
                ScrollOfLullaby.class,
                ScrollOfMagicalInfusion.class,
                ScrollOfPsionicBlast.class,
                ScrollOfMirrorImage.class,
                BlankScroll.class
        };
        Category.SCROLL.probs = new float[]{30, 10, 15, 0, 15, 15, 12, 8, 8, 0, 4, 10};

        Category.POTION.classes = new Class<?>[]{
                PotionOfAcid.class,
                PotionOfBlindness.class,
                PotionOfBooze.class,
                PotionOfConfusion.class,
                PotionOfEnergy.class,
                PotionOfFrost.class,
                PotionOfFruitJuice.class,
                PotionOfFullEnergy.class,
                PotionOfFullHealing.class,
                PotionOfFullRestoration.class,
                PotionOfGainAbility.class,
                PotionOfGainLevel.class,
                PotionOfHallucination.class,
                PotionOfHealing.class,
                PotionOfInvisibility.class,
                PotionOfIpecac.class,
                PotionOfLevitation.class,
                PotionOfMonsterDetection.class,
                PotionOfObjectDetection.class,
                PotionOfOil.class,
                PotionOfParalyticGas.class,
                PotionOfPolymorph.class,
                PotionOfPurity.class,
                PotionOfRestoration.class,
                PotionOfRestoreAbility.class,
                PotionOfSeeInvisible.class,
                PotionOfSickness.class,
                PotionOfSleeping.class,
                PotionOfSpeed.class,
                PotionOfToxicGas.class,
                PotionOfShielding.class,
                PotionOfHeroism.class,
                PotionOfAwareness.class,
                PotionOfWater.class
        };
        Category.POTION.probs = new float[]{5, 20, 15, 20, 20, 10, 25, 10, 10, 10, 20, 10, 20, 30, 20, 20, 25, 20, 20, 15, 20, 5, 10, 25, 20, 25, 5, 20, 30, 20, 20, 20, 10, 100};

        //TODO: add last ones when implemented
        Category.WAND.classes = new Class<?>[]{
                WandOfMagicMissile.class,
                WandOfLightning.class,
                WandOfDisintegration.class,
                WandOfFireblast.class,
                WandOfVenom.class,
                //WandOfBlastWave.class,
                //WandOfLivingEarth.class,
                WandOfFrost.class,
                //WandOfPrismaticLight.class,
                //WandOfWarding.class,
                //WandOfTransfusion.class,
                //WandOfCorruption.class,
                WandOfRegrowth.class};
        Category.WAND.probs = new float[]{4, 4, 4, 4, 4, 3, 3, /*3, 3, 3, 3, 3, 3*/};

        Category.WEAPON.classes = new Class<?>[]{
                Dagger.class,
                Knuckles.class,
                Quarterstaff.class,
                Spear.class,
                Mace.class,
                Sword.class,
                Longsword.class,
                BattleAxe.class,
                WarHammer.class,
                Glaive.class,
                Dart.class,
                Javelin.class,
                IncendiaryDart.class,
                CurareDart.class,
                Shuriken.class,
                Boomerang.class,
                Tomahawk.class};
        Category.WEAPON.probs = new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1};

        Category.ARMOR.classes = new Class<?>[]{
                ClothArmor.class,
                LeatherArmor.class,
                MailArmor.class,
                ScaleArmor.class,
                HalfPlateArmor.class};
        Category.ARMOR.probs = new float[]{1, 1, 1, 1, 1};

        Category.FOOD.classes = new Class<?>[]{
                TripeRation.class,
                Apple.class,
                Banana.class,
                Grapes.class,
                Orange.class,
                Pear.class,
                Blandfruit.class,
                BellPepper.class,
                Carrot.class,
                Garlic.class,
                GingerRoot.class,
                Potato.class,
                CramRation.class,
                Ration.class,
                LembasWafer.class,
                CannedFood.class
                };
        Category.FOOD.probs = new float[]{140, 20, 20, 20, 20, 20, 80, 20, 20, 20, 20, 20, 140, 380, 20, 375};

        Category.RING.classes = new Class<?>[]{
                RingOfAccuracy.class,
                RingOfEvasion.class,
                RingOfElements.class,
                RingOfForce.class,
                RingOfFuror.class,
                RingOfHaste.class,
                RingOfMagic.class, //currently removed from drop tables, pending rework
                RingOfMight.class,
                RingOfSharpshooting.class,
                RingOfTenacity.class,
                RingOfWealth.class};
        Category.RING.probs = new float[]{1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1};

        Category.ARTIFACT.classes = new Class<?>[]{
                CapeOfThorns.class,
                //ChaliceOfBlood.class,
                CloakOfShadows.class,
                HornOfPlenty.class,
                MasterThievesArmband.class,
                //SandalsOfNature.class,
                TalismanOfForesight.class,
                TimekeepersHourglass.class,
                UnstableSpellbook.class,
                AlchemistsToolkit.class, //currently removed from drop tables, pending rework.
                //DriedRose.class, //starts with no chance of spawning, chance is set directly after beating ghost quest.
                //LloydsBeacon.class,
                EtherealChains.class
        };
        Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();

        Category.SEED.classes = new Class<?>[]{
                BlandfruitBush.Seed.class,
                Blindweed.Seed.class,
                Bloodleaf.Seed.class,
                Deathroot.Seed.class,
                Dreamfoil.Seed.class,
                Earthroot.Seed.class,
                Fadeleaf.Seed.class,
                Firebloom.Seed.class,
                Icecap.Seed.class,
                Moongrass.Seed.class,
                Sorrowmoss.Seed.class,
                Starflower.Seed.class,
                Stoneberry.Seed.class,
                Stormvine.Seed.class,
                Sungrass.Seed.class,
                Swampweed.Seed.class};
        Category.SEED.probs = new float[]{4, 4, 2, 3, 4, 3, 4, 3, 4, 3, 4, 1, 2, 4, 4, 1};

        Category.GEMSTONE.classes = new Class<?>[]{
                Gemstone.BlackOpal.class,
                Gemstone.JetStone.class,
                Gemstone.Obsidian.class,
                Gemstone.Garnet.class,
                Gemstone.Jasper.class,
                Gemstone.Ruby.class,
                Gemstone.Jacinth.class,
                Gemstone.Sunstone.class,
                Gemstone.Chrysoberyl.class,
                Gemstone.Citrine.class,
                Gemstone.Amber.class,
                Gemstone.TigersEye.class,
                Gemstone.Topaz.class,
                Gemstone.Emerald.class,
                Gemstone.Jade.class,
                Gemstone.LapisLazuli.class,
                Gemstone.Sapphire.class,
                Gemstone.Amethyst.class,
                Gemstone.Diamond.class,
                Gemstone.Dilithium.class,
                Gemstone.Opal.class,
                Gemstone.Agate.class,
                Gemstone.Aquamarine.class,
                Gemstone.Fluorite.class,
                Gemstone.Glass.class,
                Gemstone.Onyx.class,
                Gemstone.Tanzanite.class,
                Gemstone.Turquoise.class,
                Gemstone.Zircon.class};
        Category.GEMSTONE.probs = new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 2, 4, 2000, 2, 2, 2, 5};

        Category.CONTAINER.classes = new Class<?>[]{
                LargeChest.class,
                LargeBox.class,
                IceBox.class};
        Category.CONTAINER.probs = new float[]{7, 8, 1};
    }

	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put(cat, cat.prob);
		}
	}

	public static Item random() {
        Category category = Random.chances(categoryProbs);
        GLog.d("try category="+category);
		return random(category);
	}

	public static Item random(Category cat) {
		try {
			categoryProbs.put(cat, categoryProbs.get(cat) / 2);

			switch (cat) {
				case ARMOR:
					return randomArmor();
				case WEAPON:
					return randomWeapon();
				case ARTIFACT:
					Item item = randomArtifact();
					//if we're out of artifacts, return a ring instead.
					return item != null ? item : random(Category.RING);
                case GEMSTONE:
                    return randomGemstone();
				default:
					return ((Item) cat.classes[Random.chances(cat.probs)].newInstance()).random();
			}

		} catch (Exception e) {
            GLog.d("random item failed");
            GLog.d(e);
			return null;
		}
	}

	public static Item random(Class<? extends Item> cl) {
		try {
			return cl.newInstance().random();
		} catch (Exception e) {

			return null;

		}
	}

	public static Armor randomArmor() {
		try {
			Armor retval = (Armor) Category.ARMOR.classes[Random.chances(Category.ARMOR.probs)].newInstance();
			retval.random();

			return retval;
		} catch (Exception whocares) {
			return null;
		}
	}

	public static Weapon randomWeapon() {
		try {
			Category cat = Category.WEAPON;

            Weapon weapon = (Weapon)cat.classes[Random.chances(cat.probs)].newInstance();

			weapon.random();

            return weapon;
		} catch (Exception e) {
			return null;
		}
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact_old randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances(cat.probs);

			//if no artifacts are left, return null
			if (i == -1) {
				return null;
			}

			Artifact_old artifactOld = (Artifact_old) cat.classes[i].newInstance();

			//remove the chance of spawning this artifactOld.
			cat.probs[i] = 0;
			spawnedArtifacts.add(cat.classes[i].getSimpleName());

			artifactOld.random();

			return artifactOld;

		} catch (Exception e) {
			return null;
		}
	}

	public static boolean removeArtifact(Artifact_old artifactOld) {
		if (spawnedArtifacts.contains(artifactOld.getClass().getSimpleName()))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifactOld.getClass())) {
				if (cat.probs[i] == 1) {
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifactOld.getClass().getSimpleName());
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();

		//checks for dried rose quest completion, adds the rose in accordingly.
		//if (Ghost.Quest.completed()) Category.ARTIFACT.probs[10] = 1;

		spawnedArtifacts = new ArrayList<String>();
	}

    public static Gemstone randomGemstone() {
        int index = Random.chances(Category.GEMSTONE.probs);
        try {
            Class c = Category.GEMSTONE.classes[index];
            return (Gemstone)c.newInstance();
        } catch (Exception whocares) {
            GLog.d("gemstone failed index="+index);
            GLog.d(whocares);
            return null;
        }
    }



    private static ArrayList<String> spawnedArtifacts = new ArrayList<String>();

	private static final String ARTIFACTS = "artifacts";

	//used to store information on which artifacts have been spawned.
	public static void storeInBundle(Bundle bundle) {
		bundle.put(ARTIFACTS, spawnedArtifacts.toArray(new String[spawnedArtifacts.size()]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		initArtifacts();

		if (bundle.contains(ARTIFACTS)) {
			Collections.addAll(spawnedArtifacts, bundle.getStringArray(ARTIFACTS));
			Category cat = Category.ARTIFACT;

			for (String artifact : spawnedArtifacts)
				for (int i = 0; i < cat.classes.length; i++)
					if (cat.classes[i].getSimpleName().equals(artifact))
						cat.probs[i] = 0;
		}
	}
}