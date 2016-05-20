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
package com.felayga.unpixeldungeon.items.food;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.Hunger;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.keys.OldKey;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.felayga.unpixeldungeon.items.tools.unlocking.UnlockingTool;
import com.felayga.unpixeldungeon.levels.features.Door;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Food extends Item {

	private static final int AMOUNT_EATEN_PER_ROUND	= 125;
	
	public static final String AC_EAT		= "EAT";
	public static final String AC_EAT_START	= "EAT_START";
	
	public int energy = 800;
	public boolean partiallyEaten = false;

	public static final String TXT_EATING_RESUMED = "You resume eating the %s.";
	public static final String TXT_EATING_DONE = "You finish eating the %s.";
	public static final String TXT_OVEREATING_DONE = "You're finally finished.";
	public static final String TXT_OVEREATING_DEAD = "You choke on your %s.";

	public int hornValue = 3;

	public Food()
	{
		stackable = true;
		name = "ration of food";
		image = ItemSpriteSheet.RATION;

		bones = true;

		weight = Encumbrance.UNIT * 20;
	}


	private static final String PARTIALLYEATEN	= "partiallyEaten";
	private static final String ENERGY		= "energy";
	private static final String STACKABLE = "stackable";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);

		bundle.put(PARTIALLYEATEN, partiallyEaten);
		bundle.put(ENERGY, energy);
		bundle.put(STACKABLE, stackable);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);

		partiallyEaten = bundle.getBoolean(PARTIALLYEATEN);
		energy = bundle.getInt(ENERGY);
		stackable = bundle.getBoolean(STACKABLE);
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}

	@Override
	public boolean execute( final Hero hero, String action ) {
		if (action.equals(AC_EAT_START))
		{
			if (!partiallyEaten) {
				GLog.i(message());
			}
			else {
				GLog.i(TXT_EATING_RESUMED, name);
			}

			hero.sprite.operate(hero.pos);
			SpellSprite.show(hero, SpellSprite.FOOD);
			Sample.INSTANCE.play(Assets.SND_EAT);

			action = AC_EAT;
		}

		if (action.equals( AC_EAT )) {
			long time;
			int subenergy;
			Hunger hunger = hero.buff(Hunger.class);

			if (energy > AMOUNT_EATEN_PER_ROUND) {
				subenergy = AMOUNT_EATEN_PER_ROUND;
				time = GameTime.TICK;
			} else {
				subenergy = energy;
				time = Math.max(GameTime.TICK * subenergy / AMOUNT_EATEN_PER_ROUND, 1);
			}

			if (!partiallyEaten) {
				startedEating(hero);
			}
			else {
				continuedEating(hero);
			}

			energy -= subenergy;
			hunger.satisfy_new(subenergy);

			if (hunger.isStuffed() && hunger.choke(hero))
			{
				GLog.n(TXT_OVEREATING_DEAD, name);
				hero.damage(hero.HT, this);
				hero.sprite.die();
				return false;
			}

			//GLog.d("foodtick energy="+energy+" subenergy="+subenergy+"time="+time);
			hero.spend(time, false);

			if (energy <= 0) {
				doneEating(hero, hunger.isStuffed());

				return false;
			}

			return true;
			/*
			int subenergy = Math.min(AMOUNT_EATEN_PER_ROUND, energy);
			int newenergy = energy - subenergy;

			if (newenergy <= 0) {
				detach(hero.belongings.backpack);
				GLog.i(TXT_EATING_DONE, name);

				Statistics.foodEaten++;
				Badges.validateFoodEaten();
			}
			else if (!partiallyEaten) {
				partiallyEaten = true;
				detach(hero.belongings.backpack);

				stackable = false;
				name = "partially eaten " + name;

				collect(hero.belongings.backpack);
			}

			energy = newenergy;

			((Hunger)hero.buff( Hunger.class)).satisfy_new(subenergy);

			float time = (float)subenergy / (float)AMOUNT_EATEN_PER_ROUND;
			*/

			/*
			switch (hero.heroClass) {
			case WARRIOR:
				if (hero.HP < hero.HT) {
					hero.HP = Math.min( hero.HP + 5, hero.HT );
					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				}
				break;
			case MAGE:
				//1 charge
				Buff.affect( hero, ScrollOfRecharging.Recharging.class, 4f );
				ScrollOfRecharging.charge( hero );
				break;
			case ROGUE:
			case HUNTRESS:
				break;
			}
			*/
		} else {
			return super.execute( hero, action );
		}
	}

	protected void startedEating(Hero hero)
	{
		//GLog.d("startedEating");
		partiallyEaten = true;
	}

	protected void continuedEating(Hero hero)
	{
		//GLog.d("continuedEating");
	}

	protected void doneEating(Hero hero, boolean stuffed) {
		//GLog.d("doneEating");
		if (stuffed) {
			GLog.i(TXT_OVEREATING_DONE);
		}
		else {
			GLog.i(TXT_EATING_DONE, name);
		}

		Statistics.foodEaten++;
		Badges.validateFoodEaten();
	}

	public void stoppedEating(Hero hero)
	{
		//GLog.d("stoppedEating");
		GLog.p("You stop eating.");
	}

	@Override
	public String getName() {
		if (partiallyEaten) {
			return "partially eaten " + super.getName();
		} else {
			return super.getName();
		}
	}

	@Override
	public String info() {
		return
			"Nothing fancy here: dried meat, " +
			"some biscuits - things like that." + energy;
	}

	public String message()
	{
		return "This food tastes delicious!";
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}
}
