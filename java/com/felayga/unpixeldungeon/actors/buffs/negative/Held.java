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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

/**
 * Created by HELLO on 6/5/2016.
 */
public class Held extends FlavourBuff {
    public Held() {
        type = buffType.NEGATIVE;
        hostPos = Constant.POS_NONE;
        actPriority = Integer.MIN_VALUE + 1; //has to go before everything else, in case of load -> pending linkage to host
    }

    @Override
    public boolean attachTo(Char target) {
        if (!target.flying && super.attachTo(target)) {
            target.crippled.put(Constant.DEBUFF_HELD, 0L);
            return true;
        } else {
            return false;
        }
    }

    public int hostPos;
    public Char host;

    private static String HOST = "heldHost";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        if (host != null) {
            bundle.put(HOST, host.pos);
        }
        else {
            bundle.put(HOST, hostPos);
        }
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        hostPos = bundle.getInt(HOST);
    }

    @Override
    public boolean act() {
        boolean retval = super.act();

        if (hostPos != Constant.POS_NONE && host == null) {
            host = Dungeon.level.findMob(hostPos);

            if (host == null) {
                if (hostPos == Dungeon.hero.pos) {
                    host = Dungeon.hero;
                }
            }

            if (host == null) {
                int x = hostPos % Level.WIDTH;
                int y = hostPos / Level.WIDTH;

                int tx = target.pos % Level.WIDTH;
                int ty = target.pos / Level.WIDTH;

                GLog.d("couldn't find host at pos=" + x+","+y+" with target pos="+tx+","+ty);
            }

            hostPos = Constant.POS_NONE;
        }

        return retval;
    }

    @Override
    public void detach() {
        target.crippled.remove(Constant.DEBUFF_HELD);
        super.detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.HELD;
    }

    @Override
    public String toString() {
        return "Held";
    }

    @Override
    public String desc() {
        if (host != null) {
            return "The " + host.name + " is holding you in place.";
        }
        return "Something is holding you in place.";
    }
}

