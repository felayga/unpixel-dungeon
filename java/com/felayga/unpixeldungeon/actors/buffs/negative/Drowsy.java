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
import com.felayga.unpixeldungeon.actors.buffs.positive.MagicalSleep;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Drowsy extends Buff {

    {
        type = buffType.NEUTRAL;
    }

    @Override
    public int icon() {
        return BuffIndicator.DROWSY;
    }

    public boolean attachTo(Char target) {
        //todo: Drowsy immunity/resistance
        if (/*!target.immunities().contains(Sleep.class) && */super.attachTo(target, Char.Registry.get(ownerRegistryIndex()))) {
            if (cooldown() == 0) {
                spend_new(Random.Long(GameTime.TICK * 3, GameTime.TICK * 6), false);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean act() {
        Buff.affect(target, Char.Registry.get(ownerRegistryIndex()), MagicalSleep.class);

        detach();
        return true;
    }

    @Override
    public String toString() {
        return "Drowsy";
    }

    @Override
    public String desc() {
        return "A magical force is making it difficult to stay awake.\n" +
                "\n" +
                "The hero can resist drowsiness by taking damage or by being at full health.\n" +
                "\n" +
                "After " + dispTurns(cooldown() + 1) + ", the target will fall into a deep magical sleep.";
    }
}
