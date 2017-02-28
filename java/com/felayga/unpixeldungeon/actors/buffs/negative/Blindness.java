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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Blindness extends FlavourBuff {

    public Blindness()
	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}
	
	@Override
	public int icon() {
		return BuffIndicator.BLINDNESS;
	}
	
	@Override
	public String toString() {
		return "Blinded";
	}

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You are blinded!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
	public String desc() {
		return "Blinding turns the surrounding world into a dark haze.\n" +
				"\n" +
				"While blinded, a character can't see anything at all, making any " +
				"attacks difficult and making it very easy to lose track of distant enemies. Additionally, a blinded " +
				"hero is unable to read scrolls or books.\n" +
				"\n" +
				descDuration();
	}

    protected String descDuration() {
        return "The blindness will last for " + dispTurns() + ".";
    }

    public static class Indefinite extends Blindness {
        public Indefinite() {

        }

        public static Indefinite prolong(Char target, Char owner, Class<? extends Indefinite> blindness) {
            return Buff.prolong(target, owner, blindness, GameTime.TICK * 1024);
        }

        @Override
        public boolean act() {
            spend_new(GameTime.TICK * 1024, false);
            return true;
        }

        @Override
        protected String descDuration() {
            return "The blindness will last indefinitely.";
        }

    }
}
