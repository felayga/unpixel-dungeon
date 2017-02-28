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
package com.felayga.unpixeldungeon.items.armor.glyphs;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Roots;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.EarthParticle;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.armor.Armor.Glyph;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Entanglement extends Glyph {

	private static final String TXT_ENTANGLEMENT	= "%s of entanglement";
	
	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x448822 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage ) {

		int level = Math.max( 0, armor.level() );
		
		if (Random.Int( 4 ) == 0) {
			
			Buff.prolong( defender, attacker, Roots.class, 5 - level / 5 );
			Buff.affect( defender, attacker, Roots.class );
			CellEmitter.bottom( defender.pos() ).start( EarthParticle.FACTORY, 0.05f, 8 );
			Camera.main.shake( 1, 0.4f );
			
		}

		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return String.format( TXT_ENTANGLEMENT, weaponName );
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}
		
}
