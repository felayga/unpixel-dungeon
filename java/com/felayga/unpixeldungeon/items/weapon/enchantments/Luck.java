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
package com.felayga.unpixeldungeon.items.weapon.enchantments;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSprite.Glowing;

public class Luck extends Weapon.Enchantment {

	private static final String TXT_LUCKY	= "Lucky %s";
	
	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x00FF00 );
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		int level = Math.max( 0, weapon.level() );
		
		int dmg = damage;
		for (int i=1; i <= level+1; i++) {
			dmg = Math.max( dmg, weapon.damageRoll() - i );
		}
		
		if (dmg > damage) {
			defender.damage( dmg - damage, MagicType.Mundane, null );
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String name( String weaponName) {
		return String.format( TXT_LUCKY, weaponName );
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}
}
