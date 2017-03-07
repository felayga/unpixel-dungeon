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
 * Created by HELLO on 3/16/2016.
 */
public class Constant {
    public static class Action {
        public static final String DROP         = "DROP";
        public static final String THROW        = "THROW";
        public static final String APPLY        = "APPLY";
        public static final String BREW         = "BREW";
        public static final String KICK         = "KICK";
        public static final String FORCE        = "FORCE";
        public static final String PLANT        = "PLANT";
        public static final String SHOOT        = "SHOOT";
        public static final String TAKE         = "TAKE";
        public static final String EQUIP        = "EQUIP";
        public static final String UNEQUIP      = "UNEQUIP";
        public static final String DRINK        = "DRINK";
        public static final String DIP          = "DIP";
        public static final String EAT          = "EAT";
        public static final String EAT_START    = "EAT_START"; // necessary for proper handling of "you started eating" messages
        public static final String OPEN         = "OPEN";
        public static final String SLOW_ACTION  = "SLOW_ACTION"; // necessary for allowing the interruption of the use of items with actions that take several rounds to perform
        public static final String CAST         = "CAST";
        public static final String CAST_COMPLETE= "CAST_COMPLETE";
        public static final String FORGET       = "FORGET";

        public static final String YES      = "YES";
        public static final String CANCEL   = "CANCEL";
    }

    public static class Attribute {
        public static final int MAXIMUM = 24;
    }

    public static class Position {
        public static final int ENTRANCE            = -2;
        public static final int EXIT                = -1;
        public static final int RANDOM              = -3;
        public static final int ENTRANCE_ALTERNATE  = -4;
        public static final int EXIT_ALTERNATE      = -5;
        public static final int NONE                = -1048576;
    }

    public static class Text {
        public static final String HERO_READIED = "You ready your %s.";
        public static final String YES          = "YES";
        public static final String NO           = "NO";
    }

    public static class Time {
        public static final long ITEM_THROW     = GameTime.TICK;
        public static final long ITEM_PICKUP    = GameTime.TICK;
        public static final long ITEM_DROP      = GameTime.TICK / 2;

        public static final long HERO_REST		= GameTime.TICK;
        public static final long HERO_SEARCH	= GameTime.TICK * 2;
    }

    public static class Chance {
        public static final int ITEM_DESTROYED              = 3;
        public static final int CHAR_MISS_INVISIBLE_ENEMY   = 5;
        public static final int CORPSE_DROP                 = 2;
        public static final int HUNGER_STARVING_FAINT       = 8;
        public static final int DIG_EXCAVATE_BOULDER        = 12; // min 3
        public static final int KICK_SPACE_CRIPPLE          = 5;
        public static final int KICK_SOLID_CRIPPLE          = 3;
        public static final int TORCH_IGNITE_TILE           = 6;
    }

}
