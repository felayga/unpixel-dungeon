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

package com.felayga.unpixeldungeon.mechanics;

import com.watabou.noosa.ColorBlock;

/**
 * Created by hello on 3/13/16.
 */
public enum BUCStatus {
    Unknown,
    Cursed,
    Uncursed,
    Blessed;

    public static BUCStatus FromInt(int value)
    {
        switch(value)
        {
            case -1:
                return BUCStatus.Cursed;
            case 0:
                return BUCStatus.Uncursed;
            case 1:
                return BUCStatus.Blessed;
            default:
                return BUCStatus.Unknown;
        }
    }

    public static int ToInt(BUCStatus status)
    {
        switch(status)
        {
            case Cursed:
                return -1;
            case Uncursed:
                return 0;
            case Blessed:
                return 1;
            default:
                return -2;
        }
    }

    public static void colorizeBackground(ColorBlock background, BUCStatus status)
    {
        switch(status)
        {
            case Cursed:
                background.ra += 0.2f;
                background.ga -= 0.1f;
                background.ba -= 0.1f;
                break;
            case Blessed:
                background.ra -= 0.1f;
                background.ga += 0.2f;
                background.ba -= 0.1f;
                break;
            case Unknown:
                background.ra += 0.15f;
                background.ga += 0.15f;
        }
    }

    public static String getName(BUCStatus status)
    {
        switch(status) {
            case Cursed:
                return "cursed";
            case Uncursed:
                return "uncursed";
            case Blessed:
                return "blessed";
        }

        return "";
    }
}
