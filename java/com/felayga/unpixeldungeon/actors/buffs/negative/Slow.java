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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.items.rings.RingOfElements.Resistance;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Slow extends FlavourBuff implements ISpeedModifierBuff {

	{
		type = buffType.NEGATIVE;
	}

	private static final long DURATION = GameTime.TICK * 10;

	@Override
	public int icon() {
		return BuffIndicator.SLOW;
	}
	
	@Override
	public String toString() {
		return "Slowed";
	}

	@Override
	public String desc() {
		return "Slowing magic affects the target's rate of time, to them everything is moving super-fast.\n" +
				"\n" +
				"A slowed character performs all actions in twice the amount of time they would normally take.\n" +
				"\n" +
				"This slow will last for " + dispTurns() + ".";
	}

	public static long duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

    @Override
    public long movementModifier() {
        return GameTime.TICK * 2;
    }

    @Override
    public long attackModifier() {
        return movementModifier();
    }
}
