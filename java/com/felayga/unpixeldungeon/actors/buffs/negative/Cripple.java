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

import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Cripple extends FlavourBuff implements ISpeedModifierBuff {

	public static final long DURATION	= GameTime.TICK * 10;

    public Cripple()
	{
		type = buffType.NEGATIVE;
	}

    public long movementModifier() {
        return GameTime.TICK * 2;
    }

    public long attackModifier() {
        return GameTime.TICK;
    }

	@Override
	public int icon() {
		return BuffIndicator.CRIPPLE;
	}
	
	@Override
	public String toString() {
		return "Crippled";
	}

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You are crippled!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
	public String desc() {
		return "You're pretty sure legs aren't meant to bend that way.\n" +
				"\n" +
				"Crippled halves movement speed, making moving a tile usually take two turns instead of one.\n" +
				"\n" +
				"This cripple will last for " + dispTurns() + ".";
	}
}
