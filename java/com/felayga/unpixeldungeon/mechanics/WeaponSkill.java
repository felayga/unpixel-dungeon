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
public enum WeaponSkill {
    None                (0),
    Simple              (1),
    Martial             (2),
    Exotic              (3);

    public final int value;

    WeaponSkill(int value) {
        this.value = value;
    }

    public static WeaponSkill fromInt(int value) {
        switch(value) {
            case 1:
                return Simple;
            case 2:
                return Martial;
            case 3:
                return Exotic;
            default:
                return None;
        }
    }
}
