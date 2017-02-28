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
package com.felayga.unpixeldungeon.actors.buffs.negative;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Terror extends FlavourBuff {

	public static final long DURATION = GameTime.TICK * 10;

    public Terror()
	{
		type = buffType.NEGATIVE;
	}

	@Override
	public int icon() {
		return BuffIndicator.TERROR;
	}

	@Override
	public String toString() {
		return "Terrified";
	}

	@Override
	public String desc() {
		return "Terror is manipulative magic which forces its target into an uncontrollable panic.\n" +
				"\n" +
				"Terrified characters are forced to run away from their opponent, trying to put as many doors and " +
				"walls between them as  possible. The shock of pain is enough to break this effect, however.\n" +
				"\n" +
				"This terror will last for " + dispTurns() + ", or until the target takes damage.";
	}

	public static void recover( Char target ) {
		Terror terror = target.buff( Terror.class );
		if (terror != null && terror.cooldown() < DURATION) {
			target.remove( terror );
		}
	}
}
