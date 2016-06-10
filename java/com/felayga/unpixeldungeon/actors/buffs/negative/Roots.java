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
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Roots extends FlavourBuff implements ISpeedModifierBuff {
    public Roots()
	{
		type = buffType.NEGATIVE;
	}

    public long movementModifier() {
        return 0L;
    }

    public long attackModifier() {
        return GameTime.TICK;
    }
	
	@Override
	public int icon() {
		return BuffIndicator.ROOTS;
	}

    @Override
    public boolean attachTo(Char target) {
        if (!target.flying) {
            return super.attachTo(target);
        }

        return false;
    }

    @Override
	public String toString() {
		return "Rooted";
	}

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You can't move!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
	public String desc() {
		return "Roots(magical or natural) grab at the feet, forcing them down to the ground.\n" +
				"\n" +
				"Roots lock a target in place, making it impossible for them to move, but other actions are not affected.\n" +
				"\n" +
				"The roots will last for " + dispTurns() + ".";
	}
}
