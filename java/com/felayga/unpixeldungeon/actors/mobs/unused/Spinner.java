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
package com.felayga.unpixeldungeon.actors.mobs.unused;
/*
import java.util.HashSet;

import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Web;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.actors.buffs.negative.Roots;
import com.felayga.unpixeldungeon.actors.buffs.negative.Terror;
import com.felayga.unpixeldungeon.items.food.unused.MysteryMeat;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.PoisonChance;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.mobs.unused.SpinnerSprite;
import com.watabou.utils.Random;

public class Spinner extends Mob {

	{
		name = "cave spinner";
		spriteClass = SpinnerSprite.class;

		DEXCHA = 20;

		HP = HT = 50;
		defenseSkill = 14;

		EXP = 9;
		maxLvl = 16;

		loot = new MysteryMeat();
		lootChance = 0.125f;

		FLEEING = new Fleeing();

		belongings.weapon = new PoisonChance(GameTime.TICK, 12, 16);
	}

	@Override
	protected boolean act() {
		boolean result = super.act();

		if (state == FLEEING && buff( Terror.class ) == null &&
				enemy != null && enemySeen && enemy.buff( Poison.class ) == null) {
				state = HUNTING;
		}
		return result;
	}

	public void flee() {
		state = FLEEING;
	}

	@Override
	public void move(int step) {
		if (state == FLEEING) {
			GameScene.add(Blob.seed(pos, Random.Int(5, 7), Web.class));
		}
		super.move(step);
	}

	@Override
	public String description() {
		return
				"These greenish furry cave spiders try to avoid direct combat, preferring to wait in the distance " +
						"while their victim, entangled in the spinner's excreted cobweb, slowly dies from their poisonous bite.";
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Poison.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Roots.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff(Terror.class) == null) {
				state = HUNTING;
			} else {
				super.nowhereToRun();
			}
		}
	}
}
*/