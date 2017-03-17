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
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.IIntrinsicBuff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.actors.buffs.positive.Haste;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

import java.util.ArrayList;
import java.util.List;

public class Slow extends FlavourBuff implements ISpeedModifierBuff {
    @Override
    public long movementModifier() {
        return GameTime.TICK * 5 / 3;
    }

    @Override
    public long attackModifier() {
        return GameTime.TICK * 2;
    }

    public Slow() {
        type = buffType.NEGATIVE;
    }

    public static void detach(Char target, boolean intrinsic) {
        List<Buff> pendingRemoval = new ArrayList<>();
        for (Buff buff : target.buffs()) {
            if (buff instanceof Slow) {
                if (buff instanceof IIntrinsicBuff) {
                    if (intrinsic) {
                        pendingRemoval.add(buff);
                    }
                } else {
                    if (!intrinsic) {
                        pendingRemoval.add(buff);
                    }
                }
            }
        }

        for (Buff buff : pendingRemoval) {
            buff.detach();
        }
    }

    /*
    @Override
    public boolean attachTo(Char target, Char source) {
        if (super.attachTo(target, source)) {
            List<Buff> pendingRemoval = new ArrayList<>();
            for (Buff buff : target.buffs()) {
                if (buff instanceof Haste) {
                    Haste haste = (Haste)buff;
                    if (!haste.improved()) {
                        pendingRemoval.add(haste);
                    }
                }
            }

            for (Buff buff : pendingRemoval) {
                buff.detach();
            }
            return true;
        } else {
            return false;
        }
    }
    */

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You feel yourself slow down.";
        }

        return super.attachedMessage(isHero);
    }

    @Override
    public int icon() {
        return BuffIndicator.SLOW;
    }


    @Override
    public String toString() {
        return "Slow";
    }


    @Override
    public String desc() {
        return "Slowing magic affects the target's relative time flow, requiring more time for movement and attack actions.\n" +
                //"This magic also neutralizes any haste magic in effect on the target.\n" +
                "\n" +
                descDuration();
    }


    protected String descDuration() {
        return "The slowness will last for " + dispTurns() + ".";
    }

    public static class Indefinite extends Haste {
        @Override
        public long movementModifier() {
            return GameTime.TICK * 3 / 5;
        }

        public Indefinite() {
        }

        public static Indefinite prolong(Char target, Char source, Class<? extends Indefinite> haste) {
            return Buff.prolong(target, source, haste, GameTime.TICK * 1024);
        }

        @Override
        public boolean act() {
            spend_new(GameTime.TICK * 1024, false);
            return true;
        }

        @Override
        protected String descDuration() {
            return "The slowness will last indefinitely.";
        }

    }

    public static class Intrinsic extends Indefinite implements IIntrinsicBuff {
        public Intrinsic() {
        }

        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }
    }
}
