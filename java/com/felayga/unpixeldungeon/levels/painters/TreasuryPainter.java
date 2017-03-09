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

import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.LargeBox;
import com.felayga.unpixeldungeon.items.bags.LargeChest;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class TreasuryPainter extends Painter {

    public static void paint(Level level, Room room) {

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.EMPTY);

        set(level, room.center(), Terrain.STATUE);

        int n = Random.IntRange(2, 3);

        int chestPos;
        do {
            chestPos = room.random();
        } while (level.map(chestPos) != Terrain.EMPTY || level.heaps.get(chestPos) != null);

        Bag chest;
        if (Random.Int(15) > 7) {
            chest = new LargeChest();
        } else {
            chest = new LargeBox();
        }

        chest.random();

        while (n > 0) {
            chest.collect(new Gold().random());
            //level.drop( new Gold().random(), pos ).type = (Random.Int(20) == 0 && heapType == Heap.Type.CHEST ? Heap.Type.MIMIC : heapType);
            n--;
        }

        level.drop(chest, chestPos);

        n = Random.IntRange(6, 12);

        while (n > 0) {
            int pos;
            do {
                pos = room.random();
            } while (level.map(pos) != Terrain.EMPTY || pos == chestPos);
            level.drop(new Gold(Random.IntRange(5, 12)), pos);
            n--;
        }

        for (Room.Door door : room.connected.values()) {
            door.set(Room.Door.Type.LOCKED);
        }
    }
}
