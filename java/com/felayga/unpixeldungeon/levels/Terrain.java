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
package com.felayga.unpixeldungeon.levels;

import com.felayga.unpixeldungeon.levels.traps.AlarmTrap;
import com.felayga.unpixeldungeon.levels.traps.FireTrap;
import com.felayga.unpixeldungeon.levels.traps.GrippingTrap;
import com.felayga.unpixeldungeon.levels.traps.LightningTrap;
import com.felayga.unpixeldungeon.levels.traps.ParalyticTrap;
import com.felayga.unpixeldungeon.levels.traps.PoisonTrap;
import com.felayga.unpixeldungeon.levels.traps.SummoningTrap;
import com.felayga.unpixeldungeon.levels.traps.ToxicTrap;
import com.felayga.unpixeldungeon.levels.traps.Trap;
import com.watabou.utils.SparseArray;

public class Terrain {
    public static final int CHASM                   = 0;
    public static final int EMPTY                   = 1;
    public static final int GRASS                   = 2;
    public static final int EMPTY_WELL              = 3;
    public static final int WALL                    = 4;
    public static final int DOOR                    = 5;
    public static final int OPEN_DOOR               = 6;
    public static final int STAIRS_UP               = 7;
    public static final int STAIRS_DOWN             = 8;
    public static final int CHARCOAL                = 9;
    public static final int LOCKED_DOOR             = 10;
    public static final int PEDESTAL                = 11;
    public static final int WALL_DECO               = 12;
    public static final int BARRICADE               = 13;
    public static final int EMPTY_SP                = 14;
    public static final int HIGH_GRASS              = 15;

    public static final int SECRET_DOOR             = 16;
    public static final int SECRET_LOCKED_DOOR      = 17;
    public static final int SECRET_TRAP             = 18;
    public static final int TRAP                    = 19;
    public static final int INACTIVE_TRAP           = 20;

    public static final int EMPTY_DECO              = 21;
    public static final int LOCKED_EXIT             = 22;
    public static final int UNLOCKED_EXIT           = 23;
    public static final int SIGN                    = 24;
    public static final int WELL                    = 25;
    public static final int STATUE                  = 26;
    public static final int STATUE_SP               = 27;
    public static final int ALCHEMY                 = 28;

    public static final int CHASM_FLOOR             = 29;
    public static final int CHASM_FLOOR_SP          = 30;
    public static final int CHASM_WALL              = 31;
    public static final int CHASM_WATER             = 32;
    public static final int WOOD_DEBRIS             = 34;
    public static final int WALL_STONE              = 35;
    public static final int WELL_MAGIC              = 36;

    public static final int STAIRS_UP_ALTERNATE     = 37;
    public static final int STAIRS_DOWN_ALTERNATE   = 38;

    public static final int ALCHEMY_EMPTY           = 39;
    public static final int ALTAR                   = 40;

    public static final int DIRT_HIGH_GRASS         = 41;
    public static final int DIRT_GRASS              = 42;
    public static final int DIRT_CHARCOAL           = 43;

    public static final int IRON_BARS               = 44;
    public static final int TOMBSTONE_GRAY          = 45;
    public static final int TOMBSTONE_WHITE         = 46;


    public static final int FACED_TILE_MIN          = 128; //needs to be aligned to FACED_TILE_BLOCKSIZE
    public static final int FACED_TILE_MAX          = 255;
    public static final int FACED_TILE_BLOCKSIZE    = 16;

    public static final int PUDDLE_TILES            = FACED_TILE_MIN;
    public static final int PUDDLE                  = PUDDLE_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int WATER_DEEP_TILES        = PUDDLE_TILES + FACED_TILE_BLOCKSIZE;
    public static final int WATER_DEEP              = WATER_DEEP_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int FACED_TILE_OVERLAY_MIN  = 192;

    public static final int OVERLAY_TILES           = FACED_TILE_OVERLAY_MIN;
    public static final int OVERLAY                 = OVERLAY_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int UNDERLAY_DIRT_TILES     = OVERLAY_TILES + FACED_TILE_BLOCKSIZE;
    public static final int UNDERLAY_DIRT           = UNDERLAY_DIRT_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int UNDERLAY_PIT_TILES      = UNDERLAY_DIRT_TILES + FACED_TILE_BLOCKSIZE;
    public static final int UNDERLAY_PIT            = UNDERLAY_PIT_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int UNDERLAY_CHASM_TILES    = UNDERLAY_PIT_TILES + FACED_TILE_BLOCKSIZE;
    public static final int UNDERLAY_CHASM          = UNDERLAY_CHASM_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int TILE_MAX                = 255;

    public static final int FLAG_PASSABLE           = 0x000001;
    public static final int FLAG_LOSBLOCKING        = 0x000002;
    public static final int FLAG_BURNABLE           = 0x000004;
    public static final int FLAG_SECRET             = 0x000008;
    public static final int FLAG_SOLID              = 0x000010;
    public static final int FLAG_AVOID              = 0x000020;
    public static final int FLAG_LIQUID             = 0x000040;
    public static final int FLAG_CHASM              = 0x000080;

    public static final int FLAG_UNSTITCHABLE       = 0x000100;
    public static final int FLAG_PATHABLE           = 0x000200;
    public static final int FLAG_UNDISCOVERABLE     = 0x000400;
    public static final int FLAG_STONE              = 0x000800;
    public static final int FLAG_UNDIGGABLE         = 0x001000;

    public static final int FLAG_LOSDARK            = 0x002000;

    public static final int FLAG_PIT                = 0x004000;

    public static final int FLAG_DIAGONALPASSAGE    = 0x008000;


    public static final int[] flags = new int[256];

    static {
        flags[CHASM] = FLAG_AVOID | FLAG_CHASM | FLAG_UNSTITCHABLE | FLAG_DIAGONALPASSAGE | FLAG_PASSABLE;
        flags[EMPTY] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
        flags[GRASS] = FLAG_PASSABLE | FLAG_BURNABLE | FLAG_DIAGONALPASSAGE;
        flags[EMPTY_WELL] = FLAG_STONE | FLAG_DIAGONALPASSAGE;
        flags[PUDDLE] = FLAG_PASSABLE | FLAG_LIQUID | FLAG_UNSTITCHABLE | FLAG_DIAGONALPASSAGE;
        flags[WALL] = FLAG_LOSBLOCKING | FLAG_SOLID | FLAG_STONE | FLAG_UNSTITCHABLE | FLAG_UNDISCOVERABLE;
        flags[DOOR] = FLAG_PATHABLE | FLAG_LOSBLOCKING | FLAG_BURNABLE | FLAG_SOLID | FLAG_UNSTITCHABLE;
        flags[OPEN_DOOR] = FLAG_PASSABLE | FLAG_BURNABLE | FLAG_UNSTITCHABLE;
        flags[STAIRS_UP] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE | FLAG_UNDIGGABLE;
        flags[STAIRS_DOWN] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE | FLAG_UNDIGGABLE;
        flags[CHARCOAL] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
        flags[LOCKED_DOOR] = FLAG_PATHABLE | FLAG_LOSBLOCKING | FLAG_SOLID | FLAG_BURNABLE | FLAG_UNSTITCHABLE;
        flags[PEDESTAL] = FLAG_PASSABLE | FLAG_UNSTITCHABLE | FLAG_DIAGONALPASSAGE;
        flags[WALL_DECO] = FLAG_LOSBLOCKING | FLAG_SOLID | FLAG_STONE | FLAG_UNSTITCHABLE | FLAG_UNDISCOVERABLE;
        flags[BARRICADE] = FLAG_BURNABLE | FLAG_SOLID | FLAG_LOSBLOCKING;
        flags[EMPTY_SP] = FLAG_PASSABLE | FLAG_UNSTITCHABLE | FLAG_DIAGONALPASSAGE;
        flags[HIGH_GRASS] = FLAG_PASSABLE | FLAG_LOSBLOCKING | FLAG_BURNABLE | FLAG_DIAGONALPASSAGE;

        flags[SECRET_DOOR] = FLAG_LOSBLOCKING | FLAG_SOLID | FLAG_SECRET | FLAG_BURNABLE | FLAG_UNSTITCHABLE;
        flags[SECRET_LOCKED_DOOR] = flags[SECRET_DOOR];
        flags[SECRET_TRAP] = FLAG_PASSABLE | FLAG_SECRET;
        flags[TRAP] = FLAG_PASSABLE | FLAG_AVOID | FLAG_DIAGONALPASSAGE;
        flags[INACTIVE_TRAP] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;

        flags[EMPTY_DECO] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
        flags[LOCKED_EXIT] = FLAG_SOLID;
        flags[UNLOCKED_EXIT] = FLAG_PASSABLE;
        flags[SIGN] = FLAG_PASSABLE | FLAG_BURNABLE | FLAG_DIAGONALPASSAGE;
        flags[WELL] = flags[EMPTY_WELL];
        flags[STATUE] = FLAG_SOLID | FLAG_STONE;
        flags[STATUE_SP] = FLAG_SOLID | FLAG_STONE | FLAG_UNSTITCHABLE;
        flags[ALCHEMY] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
        flags[IRON_BARS] = FLAG_SOLID | FLAG_UNSTITCHABLE;

        flags[CHASM_WALL] = flags[CHASM];
        flags[CHASM_FLOOR] = flags[CHASM];
        flags[CHASM_FLOOR_SP] = flags[CHASM];
        flags[CHASM_WATER] = flags[CHASM];
        flags[WOOD_DEBRIS] = flags[OPEN_DOOR] | FLAG_DIAGONALPASSAGE;
        flags[WALL_STONE] = flags[WALL] | FLAG_LOSDARK;
        flags[WELL_MAGIC] = flags[EMPTY_WELL];
        flags[STAIRS_UP_ALTERNATE] = flags[STAIRS_UP];
        flags[STAIRS_DOWN_ALTERNATE] = flags[STAIRS_DOWN];
        flags[ALCHEMY_EMPTY] = flags[ALCHEMY];
        flags[ALTAR] = flags[TRAP];

        flags[DIRT_HIGH_GRASS] = flags[HIGH_GRASS];
        flags[DIRT_GRASS] = flags[GRASS];
        flags[DIRT_CHARCOAL] = flags[CHARCOAL];

        for (int n = PUDDLE_TILES; n <= PUDDLE; n++) {
            flags[n] = flags[PUDDLE];
        }

        for (int n = WATER_DEEP_TILES; n <= WATER_DEEP; n++) {
            flags[n] = flags[PUDDLE] | FLAG_AVOID | FLAG_PIT;
        }

        for (int n = UNDERLAY_DIRT_TILES; n <= UNDERLAY_DIRT; n++) {
            flags[n] = flags[EMPTY] | FLAG_LOSDARK;
        }

        for (int n = UNDERLAY_PIT_TILES; n <= UNDERLAY_PIT; n++) {
            flags[n] = flags[EMPTY] | FLAG_LOSDARK | FLAG_PIT;
        }

        for (int n = UNDERLAY_CHASM_TILES; n <= UNDERLAY_CHASM; n++) {
            flags[n] = FLAG_CHASM | FLAG_AVOID | FLAG_DIAGONALPASSAGE | FLAG_PASSABLE;
        }
    }

    public static int discover(int terr) {
        switch (terr) {
            case SECRET_DOOR:
                return DOOR;
            case SECRET_LOCKED_DOOR:
                return LOCKED_DOOR;
            case SECRET_TRAP:
                return TRAP;
            default:
                return terr;
        }
    }


}
