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
import com.felayga.unpixeldungeon.actors.blobs.Alchemy;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.consumable.potions.Potion;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LaboratoryPainter extends Painter {

    public static void paint(Level level, Room room) {

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.EMPTY_SP);

        Room.Door entrance = room.entrance();

        Point pot = null;
        if (entrance.x == room.left) {
            pot = new Point(room.right - 1, Random.Int(2) == 0 ? room.top + 1 : room.bottom - 1);
        } else if (entrance.x == room.right) {
            pot = new Point(room.left + 1, Random.Int(2) == 0 ? room.top + 1 : room.bottom - 1);
        } else if (entrance.y == room.top) {
            pot = new Point(Random.Int(2) == 0 ? room.left + 1 : room.right - 1, room.bottom - 1);
        } else if (entrance.y == room.bottom) {
            pot = new Point(Random.Int(2) == 0 ? room.left + 1 : room.right - 1, room.top + 1);
        }
        set(level, pot, Terrain.ALCHEMY);

        Alchemy alchemy = new Alchemy();
        alchemy.seed(pot.x + Level.WIDTH * pot.y, 1);
        level.blobs.put(Alchemy.class, alchemy);

        int n = Random.IntRange(3 + Dungeon.hero.luck() / 5, 6 + Dungeon.hero.luck() / 4);
        while (n > 0) {
            int pos;
            do {
                pos = room.random();
            } while (level.map(pos) != Terrain.EMPTY_SP);
            level.drop(prize(level), pos);
            n--;
        }

        for (Room.Door door : room.connected.values()) {
            door.set(Room.Door.Type.LOCKED);
        }
        //level.addItemToSpawn( new IronOldKey( Dungeon.depth ) );
    }

    private static Item prize(Level level) {

        Item prize = level.findPrizeItem(Potion.class);
        if (prize == null)
            prize = Generator.random(Generator.Category.POTION);

        return prize;
    }
}
