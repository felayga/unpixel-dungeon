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

/**
 * Created by HELLO on 2/13/2017.
 */

public class Deafness extends FlavourBuff {

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
        return BuffIndicator.DEAFENED;
    }

    @Override
    public String toString() {
        return "Deafened";
    }

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You are deafened!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
    public String desc() {
        return "Deafening makes the surrounding world completely silent.\n" +
                "\n" +
                "While deafened, a character can't hear anything, making spellcasting difficult." +
                "\n" +
                descDuration();
    }

    protected String descDuration() {
        return "The deafness will last for " + dispTurns() + ".";
    }

    public static class Indefinite extends Deafness {
        public Indefinite() {

        }

        public static Indefinite prolong(Char target, Char source, Class<? extends Indefinite> deafness) {
            return Buff.prolong(target, source, deafness, GameTime.TICK * 1024);
        }

        @Override
        public boolean act() {
            spend_new(GameTime.TICK * 1024, false);
            return true;
        }

        @Override
        protected String descDuration() {
            return "The deafness will last indefinitely.";
        }

    }
}

