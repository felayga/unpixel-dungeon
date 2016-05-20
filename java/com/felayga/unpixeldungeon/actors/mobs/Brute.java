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

import java.util.HashSet;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Terror;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.BruteSprite;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Brute extends Mob {

	private static final String TXT_ENRAGED = "%s becomes enraged!";
	
	{
		name = "gnoll brute";
		spriteClass = BruteSprite.class;

		canOpenDoors = true;

		DEXCHA = 20;

		HP = HT = 40;
		defenseSkill = 15;
		
		EXP = 8;
		maxLvl = 15;
		
		loot = Gold.class;
		lootChance = 0.5f;

		belongings.weapon = new MeleeMobAttack(GameTime.TICK, 4, 18);
	}
	
	private boolean enraged = false;
	private boolean enragedCheck()
	{
		if (enraged || HP >= HT / 4) {
			return false;
		}

		enraged = true;
		STRCON += 32;

		return true;
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		enragedCheck();
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		super.damage( dmg, src );
		
		if (isAlive() && enragedCheck()) {
			spend(GameTime.TICK, false );
			if (Dungeon.visible[pos]) {
				GLog.w( TXT_ENRAGED, name );
				sprite.showStatus( CharSprite.NEGATIVE, "enraged" );
			}
		}
	}
	
	@Override
	public String description() {
		return
			"Brutes are the largest, strongest and toughest of all gnolls. When severely wounded, " +
			"they go berserk, inflicting even more damage to their enemies.";
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
