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
package com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Terror;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.Weapon;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Horror extends Weapon.Enchantment {

	private static final String TXT_ELDRITCH	= "Eldritch %s";
	
	private static ItemSprite.Glowing GREY = new ItemSprite.Glowing( 0x222222 );
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// lvl 0 - 20%
		// lvl 1 - 33%
		// lvl 2 - 43%
		int level = Math.max( 0, weapon.level() );
		
		if (Random.Int( level + 5 ) >= 4) {

			if (defender == Dungeon.hero) {
				Buff.affect( defender, attacker, Vertigo.class, 20 * GameTime.TICK );
			} else {
				Buff.affect( defender, attacker, Terror.class, Terror.DURATION );
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return GREY;
	}
	
	@Override
	public String name( String weaponName) {
		return String.format( TXT_ELDRITCH, weaponName );
	}

}
