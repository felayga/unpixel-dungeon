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
import com.felayga.unpixeldungeon.items.potions.PotionOfLevitation;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.levels.traps.BlazingTrap;
import com.felayga.unpixeldungeon.levels.traps.ConfusionTrap;
import com.felayga.unpixeldungeon.levels.traps.DisintegrationTrap;
import com.felayga.unpixeldungeon.levels.traps.ExplosiveTrap;
import com.felayga.unpixeldungeon.levels.traps.FlockTrap;
import com.felayga.unpixeldungeon.levels.traps.GrimTrap;
import com.felayga.unpixeldungeon.levels.traps.ParalyticTrap;
import com.felayga.unpixeldungeon.levels.traps.SpearTrap;
import com.felayga.unpixeldungeon.levels.traps.SummoningTrap;
import com.felayga.unpixeldungeon.levels.traps.TeleportationTrap;
import com.felayga.unpixeldungeon.levels.traps.ToxicTrap;
import com.felayga.unpixeldungeon.levels.traps.Trap;
import com.felayga.unpixeldungeon.levels.traps.VenomTrap;
import com.felayga.unpixeldungeon.levels.traps.WarpingTrap;
import com.watabou.utils.Random;

public class TrapsPainter extends Painter {

    public static void paint(Level level, Room room) {

        fill(level, room, Terrain.WALL);

        Class<? extends Trap> trapClass;
        switch (Random.Int(5)) {
            case 0:
            default:
                trapClass = SpearTrap.class;
                break;
            case 1:
                trapClass = SummoningTrap.class;
                break;
            case 2:
            case 3:
            case 4:
                trapClass = Random.oneOf(levelTraps[Dungeon.depthAdjusted / 5]);
                break;
        }

        //todo: secondary door ended up in a stupid place (one block inside the room)

        if (trapClass == null) {
            fill(level, room, 1, Terrain.CHASM);
        } else {
            fill(level, room, 1, Terrain.TRAP);
        }

        for (Room.Door door : room.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }

        int lastRow = level.map[room.left + 1 + (room.top + 1) * Level.WIDTH] == Terrain.CHASM ? Terrain.CHASM : Terrain.EMPTY;

        /*
        int x = -1;
        int y = -1;
        if (entrance.x == room.left) {
            x = room.right - 1;
            y = room.top + room.height() / 2;
            fill(level, x, room.top + 1, 1, room.height() - 1, lastRow);
        } else if (entrance.x == room.right) {
            x = room.left + 1;
            y = room.top + room.height() / 2;
            fill(level, x, room.top + 1, 1, room.height() - 1, lastRow);
        } else if (entrance.y == room.top) {
            x = room.left + room.width() / 2;
            y = room.bottom - 1;
            fill(level, room.left + 1, y, room.width() - 1, 1, lastRow);
        } else if (entrance.y == room.bottom) {
            x = room.left + room.width() / 2;
            y = room.top + 1;
            fill(level, room.left + 1, y, room.width() - 1, 1, lastRow);
        }
        */

        for (int cell : room.getCells()) {
            if (level.map[cell] == Terrain.TRAP) {
                try {
                    level.setTrap(null, trapClass.newInstance().reveal(), cell);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        int x = (room.left + room.right) / 2;
        int y = (room.top + room.bottom) / 2;

        int pos = x + y * Level.WIDTH;

        level.set(pos, lastRow, true);

        if (Random.Int(3) == 0) {
            if (lastRow == Terrain.CHASM) {
                set(level, pos, Terrain.EMPTY);
            }

            level.drop(Generator.random(Generator.Category.CONTAINER), pos);
        } else {
            set(level, pos, Terrain.PEDESTAL);
            level.drop(prize(level), pos);
        }

        level.addItemToSpawn(new PotionOfLevitation());
    }

    private static Item prize(Level level) {

        Item prize;

        if (Random.Int(4) != 0) {
            prize = level.findPrizeItem();
            if (prize != null)
                return prize;
        }

        prize = Generator.random(Random.oneOf(
                Generator.Category.WEAPON,
                Generator.Category.ARMOR
        ));

        for (int i = 0; i < 3; i++) {
            Item another = Generator.random(Random.oneOf(
                    Generator.Category.WEAPON,
                    Generator.Category.ARMOR
            ));
            if (another.level() > prize.level()) {
                prize = another;
            }
        }

        return prize;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Trap>[][] levelTraps = new Class[][]{
            //sewers
            {ToxicTrap.class, TeleportationTrap.class, FlockTrap.class},
            //prison
            {ConfusionTrap.class, ExplosiveTrap.class, ParalyticTrap.class},
            //caves
            {BlazingTrap.class, VenomTrap.class, ExplosiveTrap.class},
            //city
            {WarpingTrap.class, VenomTrap.class, DisintegrationTrap.class},
            //halls, muahahahaha
            {GrimTrap.class}
    };
}
