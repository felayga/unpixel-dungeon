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
package com.felayga.unpixeldungeon.actors.buffs.hero;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class SnipersMark extends FlavourBuff {

    public SnipersMark() {

    }

	@Override
	public int icon() {
		return BuffIndicator.MARK;
	}
	
	@Override
	public String toString() {
		return "Sniper's mark";
	}

	@Override
	public String desc() {
        Char owner = Char.Registry.get(ownerRegistryIndex());
		return "The sniper is honed in " + ((owner == null) ? "on a lost target" : "on the nearby " + owner.name ) + ", " +
				"gaining increased attack speed and armor penetration while attacking it.\n" +
				"\n" +
				"The sniper will remain honed in until she switches targets, stops attacking, or the target dies.";
	}
}
