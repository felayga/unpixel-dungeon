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
package com.felayga.unpixeldungeon.items.equippableitem.armor.glyphs;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Charm;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.equippableitem.armor.Armor;
import com.felayga.unpixeldungeon.items.equippableitem.armor.Armor.Glyph;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class Affection extends Glyph {

	private static final String TXT_AFFECTION	= "%s of affection";
	
	private static ItemSprite.Glowing PINK = new ItemSprite.Glowing( 0xFF4488 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = (int)GameMath.gate( 0, armor.level(), 6 );
		
		if (Level.canReach( attacker.pos(), defender.pos() ) && Random.Int( level / 2 + 5 ) >= 4) {
			
			long duration = Random.LongRange(GameTime.TICK * 3, GameTime.TICK * 7);

			Buff.affect( attacker, defender, Charm.class, Charm.durationFactor( attacker ) * duration / GameTime.TICK );
			attacker.sprite.centerEmitter(-1).start( Speck.factory( Speck.HEART ), 0.2f, 5 );

			duration *= Random.Long(GameTime.TICK/2, GameTime.TICK) / GameTime.TICK;

			Buff.affect( defender, attacker, Charm.class, Charm.durationFactor( defender ) * duration / GameTime.TICK );
			defender.sprite.centerEmitter(-1).start( Speck.factory( Speck.HEART ), 0.2f, 5 );
		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return String.format( TXT_AFFECTION, weaponName );
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return PINK;
	}
}
