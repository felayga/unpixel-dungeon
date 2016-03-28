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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.Roots;
import com.felayga.unpixeldungeon.effects.Wound;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.watabou.utils.Random;

public class GrippingTrap extends Trap {

	{
		name = "Gripping trap";
		color = TrapSprite.GREY;
		shape = TrapSprite.CROSSHAIR;
	}

	@Override
	public void activate() {

		Char c = Actor.findChar( pos );

		if (c != null) {
			int damage = Math.max( 0,  (Dungeon.depth) );
			Buff.affect( c, Bleeding.class ).set( damage );
			Buff.prolong( c, Cripple.class, GameTime.TICK * 15);
			Buff.prolong( c, Roots.class, GameTime.TICK * 5);
			Wound.hit( c );
		} else {
			Wound.hit( pos );
		}

	}

	@Override
	public String desc() {
		return "Triggering this trap will send barbed claws along the ground, " +
				"damaging the victims feet and rooting them in place.";
	}
}
