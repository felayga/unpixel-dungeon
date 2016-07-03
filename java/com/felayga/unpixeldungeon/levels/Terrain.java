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

	public static final int CHASM				    = 0;
	public static final int EMPTY				    = 1;
	public static final int GRASS				    = 2;
	public static final int EMPTY_WELL			    = 3;
	public static final int WALL				    = 4;
	public static final int DOOR				    = 5;
	public static final int OPEN_DOOR			    = 6;
	public static final int STAIRS_UP			    = 7;
	public static final int STAIRS_DOWN			    = 8;
	public static final int EMBERS				    = 9;
	public static final int LOCKED_DOOR			    = 10;
	public static final int PEDESTAL			    = 11;
	public static final int WALL_DECO			    = 12;
	public static final int BARRICADE			    = 13;
	public static final int EMPTY_SP			    = 14;
	public static final int HIGH_GRASS			    = 15;

	public static final int SECRET_DOOR	    	    = 16;
    public static final int SECRET_LOCKED_DOOR 	    = 17;
	public static final int SECRET_TRAP     	    = 18;
	public static final int TRAP            	    = 19;
	public static final int INACTIVE_TRAP   	    = 20;

	public static final int EMPTY_DECO			    = 21;
	public static final int LOCKED_EXIT			    = 22;
	public static final int UNLOCKED_EXIT		    = 23;
	public static final int SIGN				    = 24;
	public static final int WELL				    = 25;
	public static final int STATUE				    = 26;
	public static final int STATUE_SP			    = 27;
	public static final int ALCHEMY				    = 28;

	public static final int CHASM_FLOOR			    = 29;
	public static final int CHASM_FLOOR_SP		    = 30;
	public static final int CHASM_WALL			    = 31;
	public static final int CHASM_WATER			    = 32;
	public static final int DENATURED_DEBRIS	    = 33;
	public static final int WOOD_DEBRIS			    = 34;
	public static final int WALL_STONE			    = 35;
    public static final int WELL_MAGIC              = 36;

    public static final int STAIRS_UP_ALTERNATE     = 37;
    public static final int STAIRS_DOWN_ALTERNATE   = 38;


    public static final int FACED_TILE_MIN          = 64; //needs to be aligned to blocksize
    public static final int FACED_TILE_MAX          = 127;
    public static final int FACED_TILE_BLOCKSIZE    = 16;

	public static final int WATER_TILES	    	    = 64;
	public static final int WATER		    	    = WATER_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int WATER_DEEP_TILES	    = 80;
    public static final int WATER_DEEP			    = WATER_DEEP_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int DIRT_TILES			    = 96;
	public static final int DIRT				    = DIRT_TILES + FACED_TILE_BLOCKSIZE - 1;

	public static final int DIRT_DEEP_TILES		    = 112;
	public static final int DIRT_DEEP			    = DIRT_DEEP_TILES + FACED_TILE_BLOCKSIZE - 1;

    public static final int TILE_MAX                = 127;

	public static final int FLAG_PASSABLE		    = 0x0001;
	public static final int FLAG_LOSBLOCKING	    = 0x0002;
	public static final int FLAG_WOOD			    = 0x0004;
	public static final int FLAG_SECRET			    = 0x0008;
	public static final int FLAG_SOLID			    = 0x0010;
	public static final int FLAG_AVOID			    = 0x0020;
	public static final int FLAG_LIQUID			    = 0x0040;
	public static final int FLAG_PIT			    = 0x0080;
	
	public static final int FLAG_UNSTITCHABLE	    = 0x0100;
	public static final int FLAG_PATHABLE		    = 0x0200;
	public static final int FLAG_UNDISCOVERABLE	    = 0x0400;
	public static final int FLAG_STONE			    = 0x0800;

    public static final int FLAG_DIAGONALPASSAGE    = 0x8000;


	public static final int[] flags = new int[256];
	static {
		flags[CHASM] = FLAG_AVOID | FLAG_PIT | FLAG_UNSTITCHABLE | FLAG_DIAGONALPASSAGE;
		flags[EMPTY] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
		flags[GRASS] = FLAG_PASSABLE | FLAG_WOOD | FLAG_DIAGONALPASSAGE;
		flags[EMPTY_WELL] = FLAG_STONE | FLAG_DIAGONALPASSAGE;
		flags[WATER] = FLAG_PASSABLE | FLAG_LIQUID | FLAG_UNSTITCHABLE | FLAG_DIAGONALPASSAGE;
		flags[WALL] = FLAG_LOSBLOCKING | FLAG_SOLID | FLAG_STONE | FLAG_UNSTITCHABLE | FLAG_UNDISCOVERABLE;
		flags[DOOR] = FLAG_PATHABLE | FLAG_LOSBLOCKING | FLAG_WOOD | FLAG_SOLID | FLAG_UNSTITCHABLE;
		flags[OPEN_DOOR] = FLAG_PASSABLE | FLAG_WOOD | FLAG_UNSTITCHABLE;
		flags[STAIRS_UP] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
		flags[STAIRS_DOWN] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
		flags[EMBERS] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
		flags[LOCKED_DOOR] = FLAG_LOSBLOCKING | FLAG_SOLID | FLAG_WOOD | FLAG_UNSTITCHABLE;
		flags[PEDESTAL] = FLAG_PASSABLE | FLAG_UNSTITCHABLE | FLAG_DIAGONALPASSAGE;
		flags[WALL_DECO] = FLAG_LOSBLOCKING | FLAG_SOLID | FLAG_STONE | FLAG_UNSTITCHABLE | FLAG_UNDISCOVERABLE;
		flags[BARRICADE] = FLAG_WOOD | FLAG_SOLID | FLAG_LOSBLOCKING;
		flags[EMPTY_SP] = FLAG_PASSABLE | FLAG_UNSTITCHABLE | FLAG_DIAGONALPASSAGE;
		flags[HIGH_GRASS] = FLAG_PASSABLE | FLAG_LOSBLOCKING | FLAG_WOOD | FLAG_DIAGONALPASSAGE;

		flags[SECRET_DOOR] = FLAG_LOSBLOCKING | FLAG_SOLID | FLAG_SECRET | FLAG_WOOD | FLAG_UNSTITCHABLE;
        flags[SECRET_LOCKED_DOOR] = flags[SECRET_DOOR];
		flags[SECRET_TRAP] = FLAG_PASSABLE | FLAG_SECRET;
		flags[TRAP] = FLAG_PASSABLE | FLAG_AVOID | FLAG_DIAGONALPASSAGE;
		flags[INACTIVE_TRAP] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;

		flags[EMPTY_DECO] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;
		flags[LOCKED_EXIT] = FLAG_SOLID;
		flags[UNLOCKED_EXIT] = FLAG_PASSABLE;
		flags[SIGN] = FLAG_PASSABLE | FLAG_WOOD | FLAG_DIAGONALPASSAGE;
		flags[WELL] = flags[EMPTY_WELL];
		flags[STATUE] = FLAG_SOLID | FLAG_STONE;
		flags[STATUE_SP] = FLAG_SOLID | FLAG_STONE | FLAG_UNSTITCHABLE;
		flags[ALCHEMY] = FLAG_PASSABLE | FLAG_DIAGONALPASSAGE;

		flags[CHASM_WALL] = flags[CHASM];
		flags[CHASM_FLOOR] = flags[CHASM];
		flags[CHASM_FLOOR_SP] = flags[CHASM];
		flags[CHASM_WATER] = flags[CHASM];
		flags[DENATURED_DEBRIS] = flags[EMBERS];
		flags[WOOD_DEBRIS] = flags[OPEN_DOOR] | FLAG_DIAGONALPASSAGE;
		flags[WALL_STONE] = flags[WALL];
        flags[WELL_MAGIC] = flags[EMPTY_WELL];
        flags[STAIRS_UP_ALTERNATE] = flags[STAIRS_UP];
        flags[STAIRS_DOWN_ALTERNATE] = flags[STAIRS_DOWN];

		for (int n = WATER_TILES; n <= WATER; n++) {
			flags[n] = flags[WATER];
		}

		for (int n = DIRT_TILES; n <= DIRT; n++) {
			flags[n] = flags[EMPTY];
		}

		for (int n = WATER_DEEP_TILES; n <= WATER_DEEP; n++) {

		}

		for (int n = DIRT_DEEP_TILES; n <= DIRT_DEEP; n++) {
            flags[n] = FLAG_AVOID | FLAG_PIT | FLAG_DIAGONALPASSAGE;
		}
	};

	public static int discover( int terr ) {
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

	//converts terrain values from pre versioncode 44 (0.3.0c) saves
	//TODO: remove when no longer supporting saves from 0.3.0b and under
	public static int[] convertTrapsFrom43( int[] map, SparseArray<Trap> traps){
		for (int i = 0; i < map.length; i++){

			int c = map[i];

			//non-trap tiles getting their values shifted around
			if (c >= 24 && c <= 26) {
				c -= 4; //24-26 becomes 20-22
			} else if (c == 29) {
				c = 23; //29 becomes 23
			} else if ( c >= 34 && c <= 36) {
				c -= 10; //34-36 becomes 24-26
			} else if ( c >= 41 && c <= 46) {
				c -= 14; //41-46 becomes 27-32
			}

			//trap tiles, must be converted to general trap tiles and specific traps instantiated
			else if (c >= 17 && c <= 40){
				//this is going to be messy...
				Trap trap = null;
				switch(c){
					case 17: trap = new ToxicTrap().reveal(); break;
					case 18: trap = new ToxicTrap().hide(); break;

					case 19: trap = new FireTrap().reveal(); break;
					case 20: trap = new FireTrap().hide(); break;

					case 21: trap = new ParalyticTrap().reveal(); break;
					case 22: trap = new ParalyticTrap().hide(); break;

					case 23:
						c = INACTIVE_TRAP;
						trap = null;
						break;

					case 27: trap = new PoisonTrap().reveal(); break;
					case 28: trap = new PoisonTrap().hide(); break;

					case 30: trap = new AlarmTrap().reveal(); break;
					case 31: trap = new AlarmTrap().hide(); break;

					case 32: trap = new LightningTrap().reveal(); break;
					case 33: trap = new LightningTrap().hide(); break;

					case 37: trap = new GrippingTrap().reveal(); break;
					case 38: trap = new GrippingTrap().hide(); break;

					case 39: trap = new SummoningTrap().reveal(); break;
					case 40: trap = new SummoningTrap().hide(); break;
				}
				if (trap != null){
					trap.set( i );
					traps.put( trap.pos, trap );
					if (trap.visible)
						c = TRAP;
					else
						c = SECRET_TRAP;
 				}
			}

			map[i] = c;
		}
		return map;
	}


}
