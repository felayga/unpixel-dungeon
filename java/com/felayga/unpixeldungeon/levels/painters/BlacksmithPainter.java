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

import com.felayga.unpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.levels.traps.FireTrap;
import com.watabou.utils.Random;

public class BlacksmithPainter extends Painter {

    public static void paint(Level level, Room room) {

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.TRAP);
        fill(level, room, 2, Terrain.EMPTY_SP);

        for (int i = 0; i < 2; i++) {
            int pos;
            do {
                pos = room.random();
            } while (level.map(pos) != Terrain.EMPTY_SP);
            level.drop(
                    Generator.random(Random.oneOf(
                            Generator.Category.ARMOR,
                            Generator.Category.WEAPON
                    )), pos);
        }

        for (Room.Door door : room.connected.values()) {
            door.set(Room.Door.Type.UNLOCKED);
            drawInside(level, room, door, 1, Terrain.EMPTY);
        }

        Blacksmith npc = new Blacksmith();
        do {
            npc.pos(room.random(1));
        } while (level.heaps.get(npc.pos()) != null);
        level.mobs.add(npc);

        for (int cell : room.getCells()) {
            if (level.map(cell) == Terrain.TRAP) {
                level.setTrap(npc, new FireTrap().reveal(), cell);
            }
        }
    }
}
