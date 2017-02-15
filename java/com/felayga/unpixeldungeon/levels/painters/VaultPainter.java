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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Roll;
import com.watabou.utils.Random;

public class VaultPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );
		
		int cx = (room.left + room.right) / 2;
		int cy = (room.top + room.bottom) / 2;
		int c = cx + cy * Level.WIDTH;
		
		switch (Random.Int( 3 )) {
		
		case 0:
            spawnLockedChest(level, c, prize(level));
			break;
			
		case 1:
			Item i1, i2;
			do {
				i1 = prize( level );
				i2 = prize( level );
			} while (i1.getClass() == i2.getClass());
            spawnLockedChest(level, c, i1);
            spawnLockedChest(level, c + Level.NEIGHBOURS8[Random.Int(8)], i2);
			break;
			
		case 2:
			level.drop( prize( level ), c );
			set( level, c, Terrain.PEDESTAL );
			break;
		}

        for (Room.Door door : room.connected.values()) {
            door.set(Room.Door.Type.LOCKED);
        }
	}

    private static void spawnLockedChest(Level level, int pos, Item prize) {
        float dropBonusChance = Roll.DropBonusChance(Dungeon.hero) / 2.0f;

        Bag chest = (Bag)Generator.random(Generator.Category.CONTAINER);
        chest.collect(prize);
        level.drop(chest, pos);
    }
	
	private static Item prize( Level level ) {
		return Generator.random( Random.oneOf(
			Generator.Category.WAND,
			Generator.Category.RING,
			Generator.Category.ARTIFACT
		) );
	}
}
