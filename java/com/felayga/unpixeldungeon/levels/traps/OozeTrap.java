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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.AcidBurning;
import com.felayga.unpixeldungeon.effects.Splash;
import com.felayga.unpixeldungeon.sprites.TrapSprite;

public class OozeTrap extends Trap {

	{
		name = "Ooze trap";
		color = TrapSprite.GREEN;
		shape = TrapSprite.DOTS;
	}

	@Override
	public void activate() {
		Char ch = Actor.findChar( pos );

        if (ch != null){
			Buff.affect(ch, Char.Registry.get(ownerRegistryIndex()), AcidBurning.class).resplash(ch);
			Splash.at(sprite.center(), 0x000000, 5);
		}
	}

	@Override
	public String desc() {
		return "This trap will splash out caustic ooze when activated, which will burn until it is washed away.";
	}
}
