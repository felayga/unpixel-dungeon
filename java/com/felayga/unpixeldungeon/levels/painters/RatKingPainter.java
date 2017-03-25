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

import com.felayga.unpixeldungeon.actors.mobs.npcs.RatKing;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.LargeBox;
import com.felayga.unpixeldungeon.items.bags.LargeChest;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class RatKingPainter extends Painter {

    public static void paint(Level level, Room room) {

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.EMPTY_SP);

        Room.Door entrance = room.entrance();
        entrance.set(Room.Door.Type.SECRET);
        int door = entrance.x + entrance.y * Level.WIDTH;

        for (int i = room.left + 1; i < room.right; i++) {
            addChest(level, (room.top + 1) * Level.WIDTH + i, door);
            addChest(level, (room.bottom - 1) * Level.WIDTH + i, door);
        }

        for (int i = room.top + 2; i < room.bottom - 1; i++) {
            addChest(level, i * Level.WIDTH + room.left + 1, door);
            addChest(level, i * Level.WIDTH + room.right - 1, door);
        }

        RatKing king = new RatKing();
        king.pos(room.random(1));
        level.mobs.add(king);
    }

    private static void addChest(Level level, int pos, int door) {
        if (pos == door - 1 ||
                pos == door + 1 ||
                pos == door - Level.WIDTH ||
                pos == door + Level.WIDTH) {
            return;
        }

        Bag chest;
        Item item;

        switch (Random.Int(10)) {
            case 0:
                chest = new LargeBox();
                chest.collect(Generator.random(Generator.Category.WEAPON));
                item = chest;
                break;
            case 1:
                chest = new LargeBox();
                chest.collect(Generator.random(Generator.Category.ARMOR));
                item = chest;
                break;
            default:
                Gold gold = new Gold();
                gold.random();
                gold.quantity(Math.max(gold.quantity() / Random.IntRange(5, 10), 1));
                item = gold;
                break;
        }

        level.drop(item, pos);
    }
}
