/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */
package com.felayga.unpixeldungeon.actors.buffs.hero;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Fainting;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Hunger extends Buff implements Hero.Doom {
	private static final String TXT_INCREASETO_OVERSATIATED	= "You're eating too much!";
	private static final String TXT_INCREASETO_SATIATED		= "You're having a hard time getting it all down.";
	private static final String TXT_INCREASETO_NOTHUNGRY	= "You are no longer hungry.";
	private static final String TXT_INCREASETO_HUNGRY		= "You only feel hungry now.";
	private static final String TXT_INCREASETO_WEAK			= "You only feel weak now.";

	private static final String TXT_DECREASETO_SATIATED		= "You feel satiated.";
	private static final String TXT_DECREASETO_HUNGRY		= "You feel hungry.";
	private static final String TXT_DECREASETO_WEAK			= "You are beginning to feel weak.";
	private static final String TXT_DECREASETO_FAINTING		= "You are fainting from hunger!";
	private static final String TXT_DECREASETO_DEAD			= "You die from starvation.";

	private int level;
	private int lifeRegenTick;
	private int manaRegenTick;
	private int trinketHungerTick;

	private int hungerTickAmount;

	//todo: figure out what this is (viscosity glyph?)
	private float partialDamage;

	public boolean regenActive = true;

	private static final String LEVEL				= "level";
	private static final String PARTIALDAMAGE 		= "partialDamage";
	private static final String LIFEREGENTICK		= "lifeRegenTick";
	private static final String MANAREGENTICK		= "manaRegenTick";
	private static final String TRINKETHUNGERTICK	= "trinketHungerTick";
	private static final String HUNGERTICKAMOUNT	= "hungerTickAmount";
	private static final String REGENACTIVE			= "regenActive";

	public Hunger()
	{
		super();

		level = 900;
		lifeRegenTick = 0;
		manaRegenTick = 0;
		trinketHungerTick = 0;
		hungerTickAmount = -1;
		regenActive = true;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(LEVEL, level);
		bundle.put( PARTIALDAMAGE, partialDamage );
		bundle.put(LIFEREGENTICK, lifeRegenTick);
		bundle.put(MANAREGENTICK, manaRegenTick);
		bundle.put(TRINKETHUNGERTICK, trinketHungerTick);
		bundle.put(HUNGERTICKAMOUNT, hungerTickAmount);
		bundle.put(REGENACTIVE, regenActive);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		level = bundle.getInt( LEVEL );
		partialDamage = bundle.getFloat(PARTIALDAMAGE);
		lifeRegenTick = bundle.getInt(LIFEREGENTICK);
		manaRegenTick = bundle.getInt(MANAREGENTICK);
		trinketHungerTick = bundle.getInt(TRINKETHUNGERTICK);
		hungerTickAmount = bundle.getInt(HUNGERTICKAMOUNT);
		regenActive = bundle.getBoolean(REGENACTIVE);
	}

	@Override
	public boolean act() {

		/*
		//todo: no idea what this is
		if (Dungeon.level.locked){
			spend(STEP, false);
			return true;
		}
		*/

		if (target.isAlive()) {
			Hero hero = (Hero)target;

			int test;


			if (regenActive) {
				lifeRegenTick++;

				if (hero.level >= 10) {
					test = 3;

					if (lifeRegenTick >= test) {
						lifeRegenTick = 0;

						hero.HP += Random.Int(0, hero.STRCON) + 1;
					}
				} else {
					test = 42 / (hero.level + 2) + 1;

					if (lifeRegenTick >= test) {
						lifeRegenTick = 0;

						hero.HP++;
					}
				}

				if (hero.HP > hero.HT) {
					hero.HP = hero.HT;
				}


				test = 19 - hero.level / 2;
				manaRegenTick++;

				if (manaRegenTick >= test) {
					manaRegenTick = 0;

					test = 1 + hero.INTWIS / 7;
					if (test > 1) {
						hero.MP += Random.Int(0, test) + 1;
					} else {
						hero.MP++;
					}
				}

				if (hero.MP > hero.MT) {
					hero.MP = hero.MT;
				}
			}

			trinketHungerTick++;
			if (trinketHungerTick >= 20) {
				trinketHungerTick -= 20;

				if (hero.belongings.ring1() != null) {
					hungerTickAmount++;
				}
				if (hero.belongings.ring2() != null) {
					hungerTickAmount++;
				}
				if (hero.belongings.amulet() != null) {
					hungerTickAmount++;
				}
			}

			if (satisfyDirect(hungerTickAmount)) {
				hero.interrupt();

				if (HungerLevel.fromInt(level) == HungerLevel.DEAD) {
					target.damage(target.HP, MagicType.Mundane, null);
					//todo: why doesn't this happen on its own like it should?
					target.sprite.die();
				}
				else {
					if (Random.Int(8) == 0) {
						Buff.prolong( target, Fainting.class, (Random.Int(4) + 1) * GameTime.TICK );
					}
				}
			}

			hungerTickAmount = -1;

			long STEP = GameTime.TICK;
			long step = ((Hero)target).heroClass == HeroClass.ROGUE ? STEP * 12 / 10 : STEP;
			spend( target.buff( Shadows.class ) == null ? step : step * 3 / 2, false );
		} else {
			deactivate();
		}

		return true;
	}

	public void satisfy_new( int amount ) {
		if (amount > 0)
		{
			satisfyDirect(amount);
		}
		else {
			hungerTickAmount += amount;
		}
	}

	private boolean satisfyDirect( int energy ) {
		boolean interrupt = false;

		int oldlevel = level;
		HungerLevel oldHunger = HungerLevel.fromInt(oldlevel);

		level += energy;
		HungerLevel newHunger = HungerLevel.fromInt(level);

		if (oldHunger != newHunger) {
			if (oldlevel < level) {
				switch (newHunger) {
					case OVERSATIATED:
						GLog.w(TXT_INCREASETO_OVERSATIATED);
						break;
					case SATIATED:
						GLog.w(TXT_INCREASETO_SATIATED);
						break;
					case NOTHUNGRY:
						GLog.p(TXT_INCREASETO_NOTHUNGRY);
						break;
					case HUNGRY:
						GLog.p(TXT_INCREASETO_HUNGRY);
						break;
					case WEAK:
						GLog.p(TXT_INCREASETO_WEAK);
						break;
				}
			} else {
				switch (newHunger) {
					case SATIATED:
						GLog.p(TXT_DECREASETO_SATIATED);
						break;
					case HUNGRY:
						GLog.w(TXT_DECREASETO_HUNGRY);
						break;
					case WEAK:
						GLog.w(TXT_DECREASETO_WEAK);
						interrupt = true;
						break;
					case FAINTING:
						GLog.n(TXT_DECREASETO_FAINTING);
						interrupt = true;
						break;
					case DEAD:
						interrupt = true;
						break;
				}
			}

			BuffIndicator.refreshHero();
		}
		else if (newHunger == HungerLevel.FAINTING)
		{
			interrupt = true;
		}

		return interrupt;
	}

	public boolean isStarving()
	{
		return level < 0;
	}
	public boolean isStuffed() { return level >= 1500; }

	public boolean choke(Char ch)
	{
		return Random.PassFail((level-1750) * 2 / ch.STRCON);
	}

	public enum HungerLevel {
		OVERSATIATED,
		SATIATED,
		NOTHUNGRY,
		HUNGRY,
		WEAK,
		FAINTING,
		DEAD;

		public static HungerLevel fromInt(int hunger) {
			if (hunger >= 2000) {
				return HungerLevel.OVERSATIATED;
			} else if (hunger >= 1000) {
				return HungerLevel.SATIATED;
			} else if (hunger >= 150) {
				return HungerLevel.NOTHUNGRY;
			} else if (hunger >= 50) {
				return HungerLevel.HUNGRY;
			} else if (hunger >= 0) {
				return HungerLevel.WEAK;
			} else if (hunger >= -200) {
				return HungerLevel.FAINTING;
			} else {
				return HungerLevel.DEAD;
			}
		}
	}

	@Override
	public int icon() {
		HungerLevel hunger = HungerLevel.fromInt(level);

		switch(hunger)
		{
			case OVERSATIATED:
				return BuffIndicator.HUNGER_OVERSATIATED;
			case SATIATED:
				return BuffIndicator.HUNGER_SATIATED;
			case HUNGRY:
				return BuffIndicator.HUNGER_HUNGRY;
			case WEAK:
				return BuffIndicator.HUNGER_WEAK;
			case FAINTING:
				return BuffIndicator.HUNGER_FAINTING;
		}

		return BuffIndicator.NONE;
	}

	@Override
	public String toString() {
		HungerLevel hunger = HungerLevel.fromInt(level);

		switch(hunger)
		{
			case OVERSATIATED:
				return "Oversatiated";
			case SATIATED:
				return "Satiated";
			case HUNGRY:
				return "Hungry";
			case WEAK:
				return "Weak";
			case FAINTING:
				return "Fainting";
		}

		return "Not hungry";
	}

	@Override
	public String desc() {
        HungerLevel hunger = HungerLevel.fromInt(level);

        switch (hunger) {
            case OVERSATIATED:
                return "You couldn't possibly eat another bite.  Not even a wafer-thin mint.";
            case SATIATED:
                return "You're full of food and happy about it.";
            case HUNGRY:
                return "A snack wouldn't be a bad idea.";
            case WEAK:
                return "You're getting pretty weak from hunger.  Better find some food.";
            case FAINTING:
                return "You're so weak from hunger, you're fainting.  " + Dungeon.hero.className() + " needs food, badly!";
        }

        return "You're not hungry.";
    }

	@Override
	public void onDeath() {

		Badges.validateDeathFromHunger();

		Dungeon.fail( ResultDescriptions.HUNGER );
		GLog.n( TXT_DECREASETO_DEAD );
	}
}
