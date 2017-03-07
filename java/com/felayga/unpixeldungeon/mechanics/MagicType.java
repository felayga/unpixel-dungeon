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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HELLO on 5/19/2016.
 */
public enum MagicType {
    None(0x0000, "None"),
    Acid(0x0001, "Acid"),
    Cold(0x0002, "Cold"),
    Fire(0x0004, "Fire"),
    Magic(0x0008, "Magic"),
    Poison(0x0010, "Poison"),
    Shock(0x0020, "Shock"),
    Sleep(0x0040, "Sleep"),
    Hallucination(0x0080, "Hallucination"),
    Drain(0x0100, "Drain"),
    Stoning(0x0200, "Stoning"),
    Sliming(0x0400, "Sliming"),
    Sickness(0x0800, "Sickness"),
    Mundane(0x1000, "Mundane");

    public final int value;
    public final String name;

    MagicType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static MagicType fromInt(int index) {
        switch (index) {
            case 0:
                return Acid;
            case 1:
                return Cold;
            case 2:
                return Fire;
            case 3:
                return Magic;
            case 4:
                return Poison;
            case 5:
                return Shock;
            case 6:
                return Sleep;
            case 7:
                return Hallucination;
            case 8:
                return Drain;
            case 9:
                return Stoning;
            case 10:
                return Sliming;
            case 11:
                return Sickness;
            case 12:
                return Mundane;
            default:
                return None;
        }
    }

    public static List<MagicType> toList(int flags) {
        List<MagicType> retval = new ArrayList<>();

        for (int n = 0; n < 13; n++) {
            MagicType type = fromInt(n);
            if ((flags & type.value) != 0) {
                retval.add(type);
            }
        }

        if (flags == None.value) {
            retval.add(None);
        }

        return retval;
    }

    public static int value(MagicType... magicTypes) {
        int retval = 0;

        for (MagicType magicType : magicTypes) {
            retval |= magicType.value;
        }

        return retval;
    }
}
