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
package com.felayga.unpixeldungeon.levels.painters;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.ui.CustomTileVisual;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class WeakFloorPainter extends Painter {

	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.CHASM );
		
		Room.Door entrance = room.entrance();
        for (Room.Door door : room.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }
		
		if (entrance.x == room.left) {
			for (int i=room.top + 1; i < room.bottom; i++) {
				drawInside( level, room, new Point( room.left, i ), Random.IntRange( 1, room.width() - 2 ), Terrain.EMPTY_SP );
			}
		} else if (entrance.x == room.right) {
			for (int i=room.top + 1; i < room.bottom; i++) {
				drawInside( level, room, new Point( room.right, i ), Random.IntRange( 1, room.width() - 2 ), Terrain.EMPTY_SP );
			}
		} else if (entrance.y == room.top) {
			for (int i=room.left + 1; i < room.right; i++) {
				drawInside( level, room, new Point( i, room.top ), Random.IntRange( 1, room.height() - 2 ), Terrain.EMPTY_SP );
			}
		} else if (entrance.y == room.bottom) {
			for (int i=room.left + 1; i < room.right; i++) {
				drawInside( level, room, new Point( i, room.bottom ), Random.IntRange( 1, room.height() - 2 ), Terrain.EMPTY_SP );
			}
		}

		Point well = null;
		if (entrance.x == room.left) {
			well = new Point( room.right-1, Random.Int( 2 ) == 0 ? room.top + 2 : room.bottom - 1 );
		} else if (entrance.x == room.right) {
			well = new Point( room.left+1, Random.Int( 2 ) == 0 ? room.top + 2 : room.bottom - 1 );
		} else if (entrance.y == room.top) {
			well = new Point( Random.Int( 2 ) == 0 ? room.left + 1 : room.right - 1, room.bottom-1 );
		} else if (entrance.y == room.bottom) {
			well = new Point( Random.Int( 2 ) == 0 ? room.left + 1 : room.right - 1, room.top+2 );
		}
		set(level, well, Terrain.CHASM);
		CustomTileVisual vis = new HiddenWell(-1);
		vis.pos(well.x, well.y);
		level.customTiles.add(vis);
	}

	public static class HiddenWell extends CustomTileVisual{

		public HiddenWell(int pos)
		{
			super(pos);

			name = "Distant well";

			tx = Assets.WEAK_FLOOR;
			txX = Dungeon.depthAdjusted/5;
			txY = 0;
		}

		@Override
		public String desc() {
			return "You can just make out a well in the depths below, perhaps there is something down there?";
		}
	}
}
