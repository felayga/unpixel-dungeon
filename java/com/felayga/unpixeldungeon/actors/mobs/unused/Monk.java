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

import com.felayga.unpixeldungeon.actors.buffs.negative.Amok;
import com.felayga.unpixeldungeon.actors.buffs.negative.Terror;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Imp;
import com.felayga.unpixeldungeon.items.consumable.food.Food;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.DisarmChance;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.unused.MonkSprite;
import com.watabou.utils.Bundle;

public class Monk extends Mob {

	DisarmChance specialWeapon;

	{
		name = "dwarf monk";
		spriteClass = MonkSprite.class;

		canOpenDoors = true;

		DEXCHA = 30;

		HP = HT = 70;
		defenseSkill = 30;
		
		EXP = 11;
		maxLvl = 21;
		
		loot = new Food();
		lootChance = 0.083f;

		specialWeapon = new DisarmChance(GameTime.TICK / 2, 12, 16);
		belongings.weapon = specialWeapon;
	}
	
	@Override
	public String defenseVerb() {
		return "parried";
	}
	
	@Override
	public void die( Object cause ) {
		Imp.Quest.process( this );
		
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"These monks are fanatics, who devoted themselves to protecting their city's secrets from all aliens. " +
			"They don't use any armor or weapons, relying solely on the art of hand-to-hand combat.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

	private static String DISARMHITS = "hitsToDisarm";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DISARMHITS, specialWeapon.hitsToDisarm);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		specialWeapon.hitsToDisarm = bundle.getInt(DISARMHITS);
	}
}
*/