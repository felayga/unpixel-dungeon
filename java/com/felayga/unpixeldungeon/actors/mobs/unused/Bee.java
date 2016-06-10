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
package com.felayga.unpixeldungeon.actors.mobs.unused;

/*
import java.util.HashSet;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Amok;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.TauntChance;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.BeeSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Bee extends Mob {
	
	{
		name = "golden bee";
		spriteClass = BeeSprite.class;
		
		viewDistance = 4;
		
		flying = true;
		state = WANDERING;

		belongings.weapon = new TauntChance(GameTime.TICK, 1, 4);
	}

	private int level;

	//-1 refers to a pot that has gone missing.
	private int potPos;
	//-1 for no owner
	private int potHolder;
	
	private static final String LEVEL	    = "level";
	private static final String POTPOS	    = "potpos";
	private static final String POTHOLDER	= "potholder";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
		bundle.put( POTPOS, potPos );
		bundle.put( POTHOLDER, potHolder );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		spawn( bundle.getInt( LEVEL ) );
		potPos = bundle.getInt( POTPOS );
		potHolder = bundle.getInt( POTHOLDER );
	}
	
	public void spawn( int level ) {
		this.level = level;
		
		HT = (2 + level) * 4;
		defenseSkill = 9 + level;
		DEXCHA = 9 + level;
	}

	public void setPotInfo(int potPos, Char potHolder){
		this.potPos = potPos;
		if (potHolder == null)
			this.potHolder = -1;
		else
			this.potHolder = potHolder.id();
	}

	@Override
	protected Char chooseEnemy() {
		//if the pot is no longer present, target the hero
		if (potHolder == -1 && potPos == -1)
			return Dungeon.hero;

		//if something is holding the pot, target that
		else if (Actor.findById(potHolder) != null)
			return (Char)Actor.findById(potHolder);

		//if the pot is on the ground
		else {

			//if already targeting something, and that thing is still alive and near the pot, keeping targeting it.
			if (enemy != null && enemy.isAlive()
					&& Level.fieldOfView[enemy.pos] && enemy.invisible == 0
					&& Level.distance(enemy.pos, potPos) <= 3)
				return enemy;

			//find all mobs near the pot
			HashSet<Char> enemies = new HashSet<Char>();
			for (Mob mob : Dungeon.level.mobs)
				if (!(mob instanceof Bee) && Level.distance(mob.pos, potPos) <= 3 && (mob.hostile || mob.ally))
					enemies.add(mob);

			//pick one, if there are none, check if the hero is near the pot, go for them, otherwise go for nothing.
			if (enemies.size() > 0) return Random.element(enemies);
			else return (Level.distance(Dungeon.hero.pos, potPos) <= 3) ? Dungeon.hero : null ;
		}
	}

	@Override
	protected boolean getCloser(int target) {
		if (enemy != null && Actor.findById(potHolder) == enemy) {
			target = enemy.pos;
		} else if (potPos != -1 && (state == WANDERING || Level.distance(target, potPos) > 3))
			this.target = target = potPos;
		return super.getCloser( target );
	}

	@Override
	public String description() {
		return
			"Despite their small size, golden bees tend " +
			"to protect their home fiercely. This one is very mad, better keep your distance.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Poison.class );
		IMMUNITIES.add( Amok.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
*/