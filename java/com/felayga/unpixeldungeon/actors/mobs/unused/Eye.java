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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.positive.Light;
import com.felayga.unpixeldungeon.actors.buffs.negative.Terror;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.PurpleParticle;
import com.felayga.unpixeldungeon.items.unused.Dewdrop;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfDisintegration;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Death;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Leech;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.mobs.unused.EyeSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class Eye extends Mob {
	
	private static final String TXT_DEATHGAZE_KILLED = "%s's deathgaze killed you...";
	
	{
		name = "evil eye";
		spriteClass = EyeSprite.class;

		DEXCHA = 30;

		HP = HT = 100;
		defenseSkill = 20;
		viewDistance = Light.DISTANCE;
		
		EXP = 13;
		maxLvl = 25;
		
		flying = true;
		
		loot = new Dewdrop();
		lootChance = 0.5f;

		belongings.weapon = new MeleeMobAttack(GameTime.TICK * 16 / 10, 14, 20);
	}
	
	private Ballistica beam;
	
	@Override
	protected boolean canAttack( Char enemy ) {
		
		beam = new Ballistica( pos, enemy.pos, Ballistica.STOP_TERRAIN);

		return beam.subPath(1, beam.dist).contains(enemy.pos);
	}
	
	@Override
	protected boolean doAttack( boolean thrown, Char enemy ) {

		spend( attackDelay(((KindOfWeapon)belongings.weapon).delay_new), false );

		boolean rayVisible = false;
		
		for (int i : beam.subPath(0, beam.dist)) {
			if (Dungeon.visible[i]) {
				rayVisible = true;
			}
		}
		
		if (rayVisible) {
			sprite.attack( beam.collisionPos );
			return false;
		} else {
			attack( (KindOfWeapon)belongings.weapon, thrown, enemy );
			return true;
		}
	}
	
	@Override
	public boolean attack( KindOfWeapon weapon, boolean thrown, Char enemy ) {
		for (int pos : beam.subPath(1, beam.dist)) {

			Char ch = Actor.findChar( pos );
			if (ch == null) {
				continue;
			}
			
			if (hit( this, weapon, true, ch, true )) {
				ch.damage( weapon.damageRoll(), this );
				
				if (Dungeon.visible[pos]) {
					ch.sprite.flash();
					CellEmitter.center( pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
				}
				
				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail( Utils.format( ResultDescriptions.MOB, Utils.indefinite( name ) ) );
					GLog.n( TXT_DEATHGAZE_KILLED, name );
				}
			} else {
				ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
			}
		}
		
		return true;
	}
	
	@Override
	public String description() {
		return
			"One of this demon's other names is \"orb of hatred\", because when it sees an enemy, " +
			"it uses its deathgaze recklessly, often ignoring its allies and wounding them.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( WandOfDisintegration.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( Leech.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
*/