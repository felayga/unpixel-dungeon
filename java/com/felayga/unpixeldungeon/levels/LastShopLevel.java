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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Bones;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Room.Type;
import com.watabou.noosa.Group;
import com.watabou.utils.Graph;
import com.watabou.utils.Random;

import java.util.List;

//import com.felayga.unpixeldungeon.actors.mobs.npcs.Imp;

public class LastShopLevel extends RegularLevel {

    public LastShopLevel() {
        super(0);

        color1 = 0x4b6636;
        color2 = 0xf2f2f2;
    }

    @Override
    public String tilesTex() {
        return Assets.TILES_CITY;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_CITY;
    }

    @Override
    protected boolean build() {

        feeling = Feeling.CHASM;
        viewDistance = 4;

        initRooms();

        int distance;
        int retry = 0;
        int minDistance = (int) Math.sqrt(rooms.size());
        do {
            int innerRetry = 0;
            do {
                if (innerRetry++ > 10) {
                    return false;
                }
                roomEntrance = Random.element(rooms);
            } while (roomEntrance.width() < 4 || roomEntrance.height() < 4);

            innerRetry = 0;
            do {
                if (innerRetry++ > 10) {
                    return false;
                }
                roomExit = Random.element(rooms);
            }
            while (roomExit == roomEntrance || roomExit.width() < 6 || roomExit.height() < 6 || roomExit.top == 0);

            Graph.buildDistanceMap(rooms, roomExit);
            distance = Graph.buildPath(rooms, roomEntrance, roomExit).size();

            if (retry++ > 10) {
                return false;
            }

        } while (distance < minDistance);

        roomEntrance.type = Type.ENTRANCE;
        roomExit.type = Type.EXIT;

        Graph.buildDistanceMap(rooms, roomExit);
        List<Room> path = Graph.buildPath(rooms, roomEntrance, roomExit);

        Graph.setPrice(path, roomEntrance.distance);

        Graph.buildDistanceMap(rooms, roomExit);
        path = Graph.buildPath(rooms, roomEntrance, roomExit);

        Room room = roomEntrance;
        for (Room next : path) {
            room.connect(next);
            room = next;
        }

        Room roomShop = null;
        int shopSquare = 0;
        for (Room r : rooms) {
            if (r.type == Type.NULL && r.connected.size() > 0) {
                r.type = Type.PASSAGE;
                if (r.square() > shopSquare) {
                    roomShop = r;
                    shopSquare = r.square();
                }
            }
        }

        if (roomShop == null || shopSquare < 54) {
            return false;
        } else {
            roomShop.type = /*Imp.Quest.isCompleted() ? Room.Type.SHOP :*/ Room.Type.STANDARD;
        }

        paint();

        paintWater();
        paintGrass();

        return true;
    }

    @Override
    protected void decorate() {

        for (int i = 0; i < LENGTH; i++) {
            int terrain = map(i);
            if (terrain == Terrain.EMPTY && Random.Int(10) == 0) {
                map(i, Terrain.EMPTY_DECO);
            } else if (terrain == Terrain.WALL && Random.Int(8) == 0) {
                map(i, Terrain.WALL_DECO);
            } else if (terrain == Terrain.SECRET_DOOR) {
                map(i, Terrain.DOOR);
            } else if (terrain == Terrain.SECRET_LOCKED_DOOR) {
                map(i, Terrain.LOCKED_DOOR);
            }
        }

        //if (Imp.Quest.isCompleted()) {
        placeSign();
        //}
    }

    @Override
    protected void createMobs() {
    }

    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {
        //todo: bones in last shop level
        /*
        Item item = Bones.get();
        if (item != null) {
            int pos;
            do {
                pos = roomEntrance.random();
            } while (pos == entrance || map(pos) == Terrain.SIGN);
            drop(item, pos).type = Heap.Type.REMAINS;
        }
        */
    }

    @Override
    public int randomRespawnCell() {
        return roomEntrance.random();
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.PUDDLE:
                return "Suspiciously colored puddle";
            case Terrain.HIGH_GRASS:
                return "High blooming flowers";
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.STAIRS_UP:
            case Terrain.STAIRS_UP_ALTERNATE:
                return "A ramp leads up to the upper depth.";
            case Terrain.STAIRS_DOWN:
            case Terrain.STAIRS_DOWN_ALTERNATE:
                return "A ramp leads down to the Inferno.";
            case Terrain.WALL_DECO:
            case Terrain.EMPTY_DECO:
                return "Several tiles are missing here.";
            case Terrain.EMPTY_SP:
                return "Thick carpet covers the floor.";
            default:
                return super.tileDesc(tile);
        }
    }

    @Override
    protected boolean[] water() {
        return Patch.generate(0.35f, 4);
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate(0.30f, 3);
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        CityLevel.addCityVisuals(this, visuals);
        return visuals;
    }
}
