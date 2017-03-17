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
import com.felayga.unpixeldungeon.Journal;
import com.felayga.unpixeldungeon.actors.blobs.ToxicGas;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.items.weapon.Weapon.Enchantment;
import com.felayga.unpixeldungeon.items.weapon.enchantments.Death;
import com.felayga.unpixeldungeon.items.weapon.enchantments.Leech;
import com.felayga.unpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.felayga.unpixeldungeon.sprites.mobs.unused.StatueSprite;
import com.watabou.utils.Bundle;

public class Statue extends Mob {

	public Weapon specialWeapon;

	{
		name = "animated statue";
		spriteClass = StatueSprite.class;

		canOpenDoors = true;

		DEXCHA = 9 + Dungeon.depth;

		EXP = 0;
		state = PASSIVE;
	}
	
	public Statue() {
		super();
		
		do {
			specialWeapon = (Weapon)Generator.random( Generator.Category.WEAPON );
		} while (!(specialWeapon instanceof MeleeWeapon) || specialWeapon.level < 0);

		belongings.weapon = specialWeapon;

		specialWeapon.identify();
		specialWeapon.enchant( Enchantment.random() );
		
		HP = HT = 15 + Dungeon.depth * 5;
		defenseSkill = 4 + Dungeon.depth;
	}
	
	private static final String WEAPON	= "weapon";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( WEAPON, specialWeapon );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );

		specialWeapon = (Weapon)bundle.get( WEAPON );
		belongings.weapon = specialWeapon;
	}
	
	@Override
	protected boolean act() {
		if (Dungeon.visible[pos]) {
			Journal.add( Journal.Feature.STATUE );
		}
		return super.act();
	}
	
	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}
		
		super.damage( dmg, src );
	}
	
	@Override
	public void beckon( int cell ) {
		// Do nothing
	}
	
	@Override
	public void destroy() {
		Journal.remove( Journal.Feature.STATUE );
		super.destroy();
	}
	
	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		return
			"You would think that it's just another one of this dungeon's ugly statues, but its red glowing eyes give it away." +
			"\n\nWhile the statue itself is made of stone, the _" + belongings.weapon.getDisplayName() + "_, it's wielding, looks real.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Poison.class );
		RESISTANCES.add( Death.class );
		IMMUNITIES.add( Leech.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
*/