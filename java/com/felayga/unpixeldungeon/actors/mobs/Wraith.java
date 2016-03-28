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

import java.util.HashSet;

import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.buffs.Terror;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.weapon.enchantments.Death;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.WraithSprite;
import com.watabou.utils.Bundle;

public class Wraith extends Mob {

	private static final long SPAWN_DELAY	= GameTime.TICK * 2;
	
	private int level;
	
	{
		name = "wraith";
		spriteClass = WraithSprite.class;

		//todo: why should a ghost have to open doors, anyway?
		canOpenDoors = true;
		isEthereal = true;

		HP = HT = 1;
		EXP = 0;
		
		flying = true;

		belongings.weapon = new MeleeMobAttack(GameTime.TICK, 1, 3+level);
	}
	
	private static final String LEVEL = "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(LEVEL, level);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getInt( LEVEL );
		adjustStats(level);
	}

	
	public void adjustStats( int level ) {
		this.level = level;
		DEXCHA = 10 + level;
		defenseSkill = (10 + level) * 5;
		enemySeen = true;
		belongings.weapon = new MeleeMobAttack(GameTime.TICK, 1, 3+level);
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

	@Override
	public String description() {
		return
			"A wraith is a vengeful spirit of a sinner, whose grave or tomb was disturbed. " +
			"Being an ethereal entity, it is very hard to hit with a regular weapon.";
	}
	
	public static void spawnAround( int pos ) {
		for (int n : Level.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar( cell ) == null) {
				spawnAt( cell );
			}
		}
	}
	
	public static Wraith spawnAt( int pos ) {
		if (Level.passable[pos] && Actor.findChar( pos ) == null) {
			
			Wraith w = new Wraith();
			w.adjustStats( Dungeon.depth );
			w.pos = pos;
			w.state = w.HUNTING;
			GameScene.add( w, SPAWN_DELAY );
			
			w.sprite.alpha( 0 );
			w.sprite.parent.add( new AlphaTweener( w.sprite, 1, 0.5f ) );
			
			w.sprite.emitter().burst( ShadowParticle.CURSE, 5 );
			
			return w;
		} else {
			return null;
		}
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Death.class );
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
