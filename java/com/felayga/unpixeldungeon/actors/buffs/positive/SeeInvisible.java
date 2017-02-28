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

/**
 * Created by HELLO on 11/13/2016.
 */

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.IIntrinsicBuff;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class SeeInvisible extends FlavourBuff {

    public static final long DURATION	= GameTime.TICK * 20;

    public SeeInvisible()
    {
        type = buffType.POSITIVE;
    }

    @Override
    public boolean attachTo(Char target, Char source) {
        if (super.attachTo(target, source)) {
            if (target == Dungeon.hero) {
                for (Mob mob : Dungeon.level.mobs) {
                    mob.updateSpriteState();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        super.detach();

        if (target == Dungeon.hero) {
            for (Mob mob : Dungeon.level.mobs) {
                mob.updateSpriteState();
            }
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.SEEINVISIBLE;
    }


    @Override
    public String toString() {
        return "See Invisible";
    }

    @Override
    public String desc() {
        return "Your eyes are magically enhanced, allowing you to just barely make out invisible creatures."+
                "\n" +
                descDuration();
    }


    protected String descDuration() {
        return "The enhancement will last for " + dispTurns() + ".";
    }


    public static class Indefinite extends SeeInvisible {
        public Indefinite() {
        }

        public static Indefinite prolong(Char target, Char source, Class<? extends Indefinite> seeinvisible) {
            return Buff.prolong(target, source, seeinvisible, GameTime.TICK * 1024);
        }

        @Override
        public boolean act() {
            spend_new(GameTime.TICK * 1024, false);
            return true;
        }

        @Override
        protected String descDuration() {
            return "The enhancement will last indefinitely.";
        }

    }

    public static class Intrinsic extends Indefinite implements IIntrinsicBuff {
        public Intrinsic() {
        }

        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }
    }}
