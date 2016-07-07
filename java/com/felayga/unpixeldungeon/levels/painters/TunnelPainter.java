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
package com.felayga.unpixeldungeon.levels.painters;

import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class TunnelPainter extends Painter {
    public static void _set( Level level, int x, int y, int value ) {
        //level.setRepairing(x + y * Level.WIDTH, value, false, false);
        set(level, x, y, value);
    }

	public static void paint( Level level, Room room ) {
		int floor = level.tunnelTile();
		
		Point c = room.center();
		
		if (room.width() > room.height() || (room.width() == room.height() && Random.Int( 2 ) == 0)) {
			int from = room.right - 1;
			int to = room.left + 1;
			
			for (Room.Door door : room.connected.values()) {
				
				int step = door.y < c.y ? +1 : -1;
				
				if (door.x == room.left) {
					
					from = room.left + 1;
					for (int i=door.y; i != c.y; i += step) {
                        _set(level, from, i, floor);
					}
					
				} else if (door.x == room.right) {
					
					to = room.right - 1;
					for (int i=door.y; i != c.y; i += step) {
                        _set(level, to, i, floor);
					}
					
				} else {
					if (door.x < from) {
						from = door.x;
					}
					if (door.x > to) {
						to = door.x;
					}
					
					for (int i=door.y+step; i != c.y; i += step) {
                        _set(level, door.x, i, floor);
					}
				}
			}
			
			for (int i=from; i <= to; i++) {
                _set(level, i, c.y, floor);
			}
			
		} else {
			
			int from = room.bottom - 1;
			int to = room.top + 1;
			
			for (Room.Door door : room.connected.values()) {
				
				int step = door.x < c.x ? +1 : -1;
				
				if (door.y == room.top) {
					
					from = room.top + 1;
					for (int i=door.x; i != c.x; i += step) {
                        _set(level, i, from, floor);
					}
					
				} else if (door.y == room.bottom) {
					
					to = room.bottom - 1;
					for (int i=door.x; i != c.x; i += step) {
                        _set(level, i, to, floor);
					}
					
				} else {
					if (door.y < from) {
						from = door.y;
					}
					if (door.y > to) {
						to = door.y;
					}
					
					for (int i=door.x+step; i != c.x; i += step) {
                        _set(level, i, door.y, floor);
					}
				}
			}
			
			for (int i=from; i <= to; i++) {
                _set( level, c.x, i, floor);
            }
		}
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.TUNNEL );
		}
	}
}
