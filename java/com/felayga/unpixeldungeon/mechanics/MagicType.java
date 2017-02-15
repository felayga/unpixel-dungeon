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
 * Created by HELLO on 5/19/2016.
 */
public enum MagicType {
    None                (0x0000),
    Acid                (0x0001),
    Cold                (0x0002),
    Disintegration      (0x0004),
    Fire                (0x0008),
    Magic               (0x0010),
    Poison              (0x0020),
    Shock               (0x0040),
    Sleep               (0x0080),
    Hallucination       (0x0100),
    Drain               (0x0200),
    Stoning             (0x0400),
    Sliming             (0x0800),
    Sickness            (0x1000),
    Mundane             (0x2000);

    public final int value;

    MagicType(int value) {
        this.value = value;
    }
}
