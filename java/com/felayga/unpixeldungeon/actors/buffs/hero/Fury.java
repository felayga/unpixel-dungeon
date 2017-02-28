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

import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Fury extends Buff {
	
	public static float LEVEL	= 0.5f;

    public Fury()
	{
		type = buffType.POSITIVE;
	}
	
	@Override
	public boolean act() {
		if (target.HP > target.HT * LEVEL) {
			detach();
		}

        spend_new(GameTime.TICK, false );
		
		return true;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FURY;
	}
	
	@Override
	public String toString() {
		return "Furious";
	}

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You become furious!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
	public String desc() {
		return "You are angry, enemies won't like you when you're angry.\n" +
				"\n" +
				"A great rage burns within you, increasing the damage you deal with physical attacks by 50%. \n" +
				"\n" +
				"This rage will last as long as you are injured below 50% health.\n";
	}
}
