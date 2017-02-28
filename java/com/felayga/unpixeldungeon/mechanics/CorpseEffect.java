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

package com.felayga.unpixeldungeon.mechanics;

/**
 * Created by HELLO on 5/23/2016.
 */
public enum CorpseEffect {
    None                (0x0000),
    Rotten              (0x0001),
    Undecayable         (0x0002),
    Unrottable          (0x0004),
    Poisonous           (0x0008),
    ManaBoost           (0x0010),
    Stunning            (0x0020),
    Vegetable           (0x0040),
    Unstoning           (0x0080),
    Acidic              (0x0100),
    Hallucinogenic      (0x0200),
    Sickening           (0x0400),
    Canned              (0x0800),
    PlayerCanned        (0x1000),
    Spinach             (0x2000);

    public final int value;

    CorpseEffect(int value) {
        this.value = value;
    }

    public static int value(CorpseEffect... effects) {
        int retval = 0;

        for (CorpseEffect effect : effects) {
            retval |= effect.value;
        }

        return retval;
    }
}
