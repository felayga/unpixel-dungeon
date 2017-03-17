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

package com.felayga.unpixeldungeon.actors.buffs.positive;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.IIntrinsicBuff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HELLO on 2/11/2017.
 */
public class Haste extends FlavourBuff implements ISpeedModifierBuff {
    @Override
    public long movementModifier() {
        return GameTime.TICK * 3 / 5;
    }

    @Override
    public long attackModifier() {
        return GameTime.TICK / 2;
    }

    public Haste() {
        type = buffType.POSITIVE;
        improved(false);
    }

    public static void detach(Char target, boolean intrinsic) {
        List<Buff> pendingRemoval = new ArrayList<>();
        for (Buff buff : target.buffs()) {
            if (buff instanceof Haste) {
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
                if (buff instanceof Slow) {
                    pendingRemoval.add(buff);
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
            return "You feel yourself speed up.";
        }

        return super.attachedMessage(isHero);
    }


    private int icon;

    @Override
    public int icon() {
        return icon;
    }



    private boolean improved;

    public boolean improved() {
        return improved;
    }

    protected void improved(boolean value) {
        this.improved = value;

        if (value) {
            name = "Improved Haste";
            icon = BuffIndicator.HASTE_IMPROVED;
        } else {
            name = "Haste";
            icon = BuffIndicator.HASTE;
        }
    }

    private String name;

    @Override
    public String toString() {
        return name;
    }


    @Override
    public String desc() {
        return "Haste magic affects the target's relative time flow, decreasing the time required for both movement and attacks.\n" +
                //descExtra() +
                "\n" +
                descDuration();
    }

    protected String descExtra() { return ""; }

    protected String descDuration() {
        return "The haste will last for " + dispTurns() + ".";
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
            return "The haste will last indefinitely.";
        }

    }

    public static class Intrinsic extends Indefinite implements IIntrinsicBuff {
        @Override
        public long movementModifier() {
            return GameTime.TICK * 3 / 4;
        }

        public Intrinsic() {
        }

        @Override
        protected String descExtra() {
            return "This magic also neutralizes any slowing magic in effect on the target.\n";
        }

        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }
    }
}
