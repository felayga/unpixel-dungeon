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

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Cripple extends FlavourBuff {

	public static final long DURATION	= GameTime.TICK * 10;

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo(target)) {
			target.crippled.put(Constant.DEBUFF_CRIPPLE, GameTime.TICK / 2);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		target.crippled.remove(Constant.DEBUFF_CRIPPLE);
		super.detach();
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
