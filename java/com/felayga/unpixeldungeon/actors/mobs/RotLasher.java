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
package com.felayga.unpixeldungeon.actors.mobs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.blobs.ToxicGas;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.CrippleChance;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.RotLasherSprite;
import com.felayga.unpixeldungeon.utils.Utils;

import java.util.HashSet;

public class RotLasher extends Mob {

	{
		name = "rot lasher";
		spriteClass = RotLasherSprite.class;

		DEXCHA = 15;

		HP = HT = 40;
		defenseSkill = 0;

		EXP = 1;

		loot = Generator.Category.SEED;
		lootChance = 1f;

		state = WANDERING = new Waiting();

		belongings.weapon = new CrippleChance(GameTime.TICK, 4, 12, 1, GameTime.TICK * 2);
	}

	@Override
	protected boolean act() {
		if (Dungeon.level.map[pos] != Terrain.GRASS && Dungeon.level.map[pos] != Terrain.HIGH_GRASS){
			destroy();
			sprite.die();
			return true;
		} else {
			if (enemy == null || !Level.adjacent(pos, enemy.pos)) {
				HP = Math.min(HT, HP + 3);
			}
			return super.act();
		}
	}


	@Override
	protected boolean getCloser(int target) {
		return true;
	}

	@Override
	protected boolean getFurther(int target) {
		return true;
	}

	@Override
	public String description() {
		return
			"The rot lasher is a part of a mature rotberry plant's root structure, and also their primary means of defence. " +
			"Lashers are stuck into the ground, but will violently assault anything that gets near to them. " +
			"When there is no nearby prey, they stand motionless, attempting to blend in with surrounding vegetation.";
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
	static {
		IMMUNITIES.add( ToxicGas.class );
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

	private class Waiting extends Mob.Wandering{
		@Override
		public String status() {
			return Utils.format("This %s is idle", name);
		}
	}
}
