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
package com.felayga.unpixeldungeon.actors.mobs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.ToxicGas;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.RotHeartSprite;

import java.util.HashSet;

public class RotHeart extends Mob {

	{
		name = "rot heart";
		spriteClass = RotHeartSprite.class;

		DEXCHA = 0;

		HP = HT = 80;
		defenseSkill = 0;

		EXP = 4;

		state = PASSIVE;

		belongings.weapon = new MeleeMobAttack(GameTime.TICK, 0, 0);
	}

	@Override
	protected boolean act() {
		if (Dungeon.level.map[pos] != Terrain.GRASS && Dungeon.level.map[pos] != Terrain.HIGH_GRASS){
			destroy();
			sprite.die();
			return true;
		} else {
			return super.act();
		}
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		GameScene.add(Blob.seed(pos, 20, ToxicGas.class));

		return super.defenseProc(enemy, damage);
	}

	@Override
	protected boolean getCloser(int target) {
		return false;
	}

	@Override
	public void destroy() {
		super.destroy();
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])){
			if (mob instanceof RotLasher){
				mob.die(null);
			}
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		Dungeon.level.drop( new Wandmaker.Rotberry.Seed(), pos ).sprite.drop();
	}

	@Override
	public String description() {
		return
			"A Rotberry's fruit is very unique. Instead of rotting away and providing nutrients, the fruit grows, " +
			"hardens, and encompasses the seed. It provides protection for the internal organs which grow " +
			"inside the fruit. This giant orb is referred to as the heart of an adult rotberry plant.";
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
	static {
		IMMUNITIES.add( ToxicGas.class );
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

}
