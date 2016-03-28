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
package com.felayga.unpixeldungeon.actors.mobs.npcs;

import java.util.HashSet;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.ToxicGas;
import com.felayga.unpixeldungeon.actors.buffs.Burning;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.SuicideAttack;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.MirrorSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MirrorImage extends NPC {
	
	{
		name = "mirror image";
		spriteClass = MirrorSprite.class;
		
		state = HUNTING;

		belongings.weapon = new SuicideAttack(GameTime.TICK, 1, 4);
	}
	
	private static final String TIER			= "tier";
	private static final String STRCON_store	= "STRCON";
	private static final String DEXCHA_store	= "DEXCHA";
	private static final String INTWIS_store	= "INTWIS";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( STRCON_store, STRCON );
		bundle.put( DEXCHA_store, DEXCHA );
		bundle.put( INTWIS_store, INTWIS );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		STRCON = bundle.getInt( STRCON_store );
		DEXCHA = bundle.getInt( DEXCHA_store );
		INTWIS = bundle.getInt( INTWIS_store );
	}
	
	public void duplicate( Hero hero ) {
		STRCON = hero.STRCON;
		DEXCHA = hero.DEXCHA;
		INTWIS = hero.INTWIS;
	}
	
	protected Char chooseEnemy() {
		
		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob:Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos]) {
					enemies.add( mob );
				}
			}
			
			enemy = enemies.size() > 0 ? Random.element( enemies ) : null;
		}
		
		return enemy;
	}
	
	@Override
	public String description() {
		return
			"This illusion bears a close resemblance to you, " +
			"but it's paler and twitches a little.";
	}
	
	@Override
	public CharSprite sprite() {
		CharSprite s = super.sprite();
		//((MirrorSprite)s).updateArmor( tier );
		return s;
	}

	@Override
	public void interact() {
		
		int curPos = pos;
		
		moveSprite( pos, Dungeon.hero.pos );
		move( Dungeon.hero.pos );
		
		Dungeon.hero.sprite.move( Dungeon.hero.pos, curPos );
		Dungeon.hero.move( curPos );
		
		Dungeon.hero.spend( GameTime.TICK * GameTime.TICK / Dungeon.hero.speed(), false );
		Dungeon.hero.busy();
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( Burning.class );
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
