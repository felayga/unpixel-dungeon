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
package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Acid;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.AcidBurning;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Splash;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemRandomizationHandler;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

public class Potion extends Item {
	private static final String TXT_HARMFUL = "Harmful potion!";
	private static final String TXT_BENEFICIAL = "Beneficial potion";
	private static final String TXT_YES = "Yes, I know what I'm doing";
	private static final String TXT_NO = "No, I changed my mind";
	private static final String TXT_R_U_SURE_DRINK =
			"Are you sure you want to drink it? In most cases you should throw such potions at your enemies.";
	private static final String TXT_R_U_SURE_THROW =
			"Are you sure you want to throw it? In most cases it makes sense to drink it.";

	private static final long TIME_TO_DRINK = GameTime.TICK;

	protected String initials;
	protected boolean isHelpful = false;
	protected boolean isHarmful = false;

    //todo: diluted state

	private static final int NONRANDOMPOTIONSATENDOFLISTS = 1;
    /*
//finished
booze
fruit juice
healing
extra healing
full healing
energy
full energy
sickness
//todo: potions
see invisible
confusion
hallucination
restore ability
sleeping
blindness
invisibility
monster detection
object detection
enlightenment
levitation
polymorph
speed
acid
oil
gain ability
gain level
paralysis
ipecac

frost
purity
toxic gas

restoration
full restoration
    */

	protected static final Class<?>[] potions = {
			//RANDOMIZED
            PotionOfAcid.class,
            PotionOfBlindness.class,
            PotionOfConfusion.class,
            PotionOfEnergy.class,
            PotionOfFrost.class,
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
            PotionOfSleeping.class,
            PotionOfSpeed.class,
            PotionOfToxicGas.class,
            PotionOfShielding.class,
            PotionOfHeroism.class,
            PotionOfBooze.class,

            PotionOfFruitJuice.class,
            PotionOfSickness.class,
            PotionOfAwareness.class,

			//NOT RANDOMIZED
			PotionOfWater.class
	};

	private static final String[] potionColors = {
			//RANDOMIZED
			"ruby", "pink", "orange", "yellow",
            "emerald", "dark green", "cyan", "sky blue",
            "brilliant blue", "magenta", "purple-red", "puce",
            "milky", "swirly", "bubbly", "smoky",
            "cloudy", "effervescent", "black", "golden",
			"brown", "fizzy", "dark", "white",
            "murky", "dingy", "indigo", "creamy",
            "glowing", "silvery", "putrescent", "sea green",
            "rainbow", "aged", "coquelicot", "olive",
            "mauve", "chartreuse", "flesh-colored",
			//NOT RANDOMIZED
			"clear"
	};
	private static final Integer[] potionImages = {
			//RANDOMIZED
			ItemSpriteSheet.POTION_RUBY, ItemSpriteSheet.POTION_PINK, ItemSpriteSheet.POTION_ORANGE, ItemSpriteSheet.POTION_YELLOW,
            ItemSpriteSheet.POTION_EMERALD, ItemSpriteSheet.POTION_DARKGREEN, ItemSpriteSheet.POTION_CYAN, ItemSpriteSheet.POTION_SKYBLUE,
			ItemSpriteSheet.POTION_BRILLIANTBLUE, ItemSpriteSheet.POTION_MAGENTA, ItemSpriteSheet.POTION_PURPLERED, ItemSpriteSheet.POTION_PUCE,
			ItemSpriteSheet.POTION_MILKY, ItemSpriteSheet.POTION_SWIRLY, ItemSpriteSheet.POTION_BUBBLY, ItemSpriteSheet.POTION_SMOKY,
			ItemSpriteSheet.POTION_CLOUDY, ItemSpriteSheet.POTION_EFFERVESCENT, ItemSpriteSheet.POTION_BLACK, ItemSpriteSheet.POTION_GOLDEN,
			ItemSpriteSheet.POTION_BROWN, ItemSpriteSheet.POTION_FIZZY, ItemSpriteSheet.POTION_DARK, ItemSpriteSheet.POTION_WHITE,
            ItemSpriteSheet.POTION_MURKY, ItemSpriteSheet.POTION_DINGY, ItemSpriteSheet.POTION_INDIGO, ItemSpriteSheet.POTION_CREAMY,
            ItemSpriteSheet.POTION_GLOWING, ItemSpriteSheet.POTION_SILVERY, ItemSpriteSheet.POTION_PUTRESCENT, ItemSpriteSheet.POTION_SEAGREEN,
            ItemSpriteSheet.POTION_RAINBOW, ItemSpriteSheet.POTION_CGA, ItemSpriteSheet.POTION_ORANGERED, ItemSpriteSheet.POTION_OLIVE,
            ItemSpriteSheet.POTION_MAUVE, ItemSpriteSheet.POTION_CHARTREUSE, ItemSpriteSheet.POTION_FLESHCOLORED,

			//NOT RANDOMIZED
			ItemSpriteSheet.POTION_CLEAR
	};
    private static final Material[] potionMaterials = {
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass, Material.Glass,
            Material.Glass, Material.Glass, Material.Glass,
            //non-randomized
            Material.Glass
    };

	protected static ItemRandomizationHandler<Potion> handler;

	protected String color;

	public boolean ownedByFruit = false;

	@SuppressWarnings("unchecked")
	public static void initColors() {
		handler = new ItemRandomizationHandler<>((Class<? extends Potion>[]) potions, potionColors, potionImages, potionMaterials, NONRANDOMPOTIONSATENDOFLISTS);
	}

	public static void save(Bundle bundle) {
		handler.save(bundle);
	}

	@SuppressWarnings("unchecked")
	public static void restore(Bundle bundle) {
		handler = new ItemRandomizationHandler<>((Class<? extends Potion>[]) potions, potionColors, potionImages, potionMaterials, bundle);
	}

	public Potion() {
		super();
        syncRandomizedProperties();

        pickupSound = Assets.SND_ITEM_POTION;

		stackable = true;
		defaultAction = Constant.Action.DRINK;
        hasLevels(false);
		weight(Encumbrance.UNIT * 20);
        price = 20;
	}

	@Override
	public void syncRandomizedProperties() {
		image = handler.image(this);
		color = handler.label(this);
        material = handler.material(this);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
        actions.add(Constant.Action.APPLY);
		actions.add(Constant.Action.DRINK);
		return actions;
	}

	@Override
	public boolean execute(final Hero hero, String action) {
		if (action.equals(Constant.Action.DRINK)) {
			if (isKnown() && isHarmful) {
				GameScene.show(
						new WndOptions(TXT_HARMFUL, TXT_R_U_SURE_DRINK, TXT_YES, TXT_NO) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									drink(hero);
								}
							}
						}
				);
			} else {
				drink(hero);
			}

			return false;
		} else {
			return super.execute(hero, action);
		}
	}

	@Override
	public void doThrow(final Hero hero) {

		if (isKnown() && isHelpful) {

			GameScene.show(
					new WndOptions(TXT_BENEFICIAL, TXT_R_U_SURE_THROW, TXT_YES, TXT_NO) {
						@Override
						protected void onSelect(int index) {
							if (index == 0) {
								Potion.super.doThrow(hero);
							}
						}
					}
			);

		} else {
			super.doThrow(hero);
		}
	}

	protected void drink(Char hero) {
		hero.belongings.remove(this, 1);

		hero.spend_new(TIME_TO_DRINK, false);
		hero.busy();
		apply(hero);

		Sample.INSTANCE.play(Assets.SND_DRINK);

		hero.sprite.operate(hero.pos());
	}

	@Override
	protected void onThrow(Char thrower, int cell) {
		if (Dungeon.level.map(cell) == Terrain.WELL || Level.chasm[cell]) {
			super.onThrow(thrower, cell);
		} else {
			shatter(thrower, cell);
		}
	}

	public void apply(Char hero) {
		shatter(hero, hero.pos());
	}

	public void shatter(Char thrower, int cell) {
		if (Dungeon.visible[cell]) {
			GLog.i("The flask shatters and " + color() + " liquid splashes harmlessly");
            splash(cell);
		}

        if (Dungeon.audible[cell]) {
            Sample.INSTANCE.play(Assets.SND_SHATTER);
        }
	}

	@Override
	public void cast(final Hero user, int dst) {
		super.cast(user, dst);
	}

	public boolean isKnown() {
		return handler.isKnown(this);
	}

	public boolean setKnown() {
		if (!ownedByFruit) {
			if (!isKnown()) {
				handler.know(this);
				Badges.validateAllPotionsIdentified();

				return true;
			}
		}

		return false;
	}

	@Override
	public Item identify(boolean updateQuickslot) {
		if (setKnown()) {
			updateQuickslot = true;
		}

		return super.identify(updateQuickslot);
	}

	protected String color() {
		return color;
	}

	@Override
	public String getName() {
		return isKnown() ? super.getName() : color + " potion";
	}

	@Override
	public String info() {
		if (isKnown()) {
			return desc();
		}
		char c = color.charAt(0);
		String a_an;
		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
			a_an = "an";
		} else {
			a_an = "a";
		}
		return "This flask contains " + a_an + " " + color + " liquid. " +
				"Who knows what it will do when drunk or thrown?";
	}

	public String initials() {
		return isKnown() ? initials : null;
	}

	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown();
	}

	public static HashSet<Class<? extends Potion>> getKnown() {
		return handler.known();
	}

	public static HashSet<Class<? extends Potion>> getUnknown() {
		return handler.unknown();
	}

	public static boolean allKnown() {
		return handler.known().size() == potions.length;
	}

	protected void splash(int cell) {
		final int color = ItemSprite.pick(image, 8, 10);
		Splash.at(cell, color, 5);

		Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
		if (fire != null) {
            fire.clear(cell);
        }

		Acid acid = (Acid) Dungeon.level.blobs.get(Acid.class);
		if (acid != null) {
            acid.clear(cell);
        }

		Char ch = Actor.findChar(cell);
		if (ch != null) {
			Buff.detach(ch, Burning.class);
			Buff.detach(ch, AcidBurning.class);
		}
	}

}
