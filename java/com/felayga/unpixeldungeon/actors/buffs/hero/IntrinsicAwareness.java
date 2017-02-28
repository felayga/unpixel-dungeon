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
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.IIntrinsicBuff;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

import java.util.List;

/**
 * Created by HELLO on 2/26/2017.
 */
public class IntrinsicAwareness extends FlavourBuff {

    @Override
    public String toString() {
        return "Intrinsic Awareness";
    }

    @Override
    public int icon() {
        return BuffIndicator.INTRINSIC;
    }

    @Override
    public String desc() {
        return "You have been granted knowledge of your current innate abilities and resistances.\n" +
                "\n" +
                descResistances() +
                "\n" +
                descAbilities();
    }

    protected String descResistances() {
        String retval = "Current resistances:\n";

        List<MagicType> resistances = MagicType.toList(target.immunityMagical);
        for (int n=0;n<resistances.size();n++) {
            retval += resistances.get(n).name + "\n";
        }

        return retval;
    }

    protected String descAbilities() {
        String retval = "Current abilities:\n";

        boolean found = false;
        for (Buff buff : target.buffs()) {
            if (buff instanceof IIntrinsicBuff) {
                retval += buff.toString() + "\n";
                found = true;
            }
        }

        if (!found) {
            retval += "None\n";
        }
        return retval;
    }
}
