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
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

/**
 * Created by HELLO on 6/5/2016.
 */
public class Held extends FlavourBuff implements ISpeedModifierBuff {
    public Held() {
        type = buffType.NEGATIVE;
        actPriority = Integer.MIN_VALUE + 1; //has to go before everything else, in case of load -> pending linkage to host
    }

    public long movementModifier() {
        return 0L;
    }

    public long attackModifier() {
        return GameTime.TICK;
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
        Char host = Char.Registry.get(ownerRegistryIndex());

        if (host != null && Dungeon.visible[host.pos()]) {
            return "The " + host.name + " is holding you in place.";
        }
        return "Something is holding you in place.";
    }
}

