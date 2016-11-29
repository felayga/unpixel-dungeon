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
package com.felayga.unpixeldungeon.actors.buffs.negative;

import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Vertigo extends FlavourBuff {

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public int icon() {
		return BuffIndicator.VERTIGO;
	}

	@Override
	public String toString() {
		return "Vertigo";
	}

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "Everything is spinning around you!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
	public String desc() {
		return "Walking in a straight line can be difficult when the whole world is spinning.\n" +
				"\n" +
				"While under the effects of vertigo, characters who attempt to move may go in a random direction, " +
				"instead of the one they intended to go in. \n" +
				"\n" +
				"This Vertigo effect with last for " + dispTurns() + ".";
	}

    /*
	public static long duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION / GameTime.TICK : DURATION;
	}
	*/
}
