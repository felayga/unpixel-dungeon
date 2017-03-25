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
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.HashSet;

/**
 * Created by HELLO on 7/3/2016.
 */
public class MineTownLevel extends RegularLevel {
    private Room bigroom;
    private HashSet<Room> topstores;
    private HashSet<Room> midleftstores;
    private HashSet<Room> midrightstores;
    private HashSet<Room> bottomstores;

    public MineTownLevel() {
        super(0);

        color1 = 0x534f3e;
        color2 = 0xb9d661;

        viewDistance = 7;
    }

    @Override
    protected boolean determineEntranceExit() {
        if (!super.determineEntranceExit() || (roomEntrance.right < WIDTH / 2 && roomExit.right < WIDTH / 2) || (roomEntrance.left > WIDTH / 2 && roomExit.left > WIDTH / 2)) {
            return false;
        }

        return true;
    }

    @Override
    protected int fillBlock() {
        return Terrain.WALL_STONE;
    }

    public int tunnelTile() { return Terrain.UNDERLAY_DIRT; }

    @Override
    protected Feeling determineFeeling() {
        return Feeling.NONE;
    }

    @Override
    protected void buildSpecials() {
        //nothing, manually controlled here
    }

    @Override
    protected void splitRooms() {
        int _minroomsize = minRoomSize;
        int _maxroomsize = maxRoomSize;

        minRoomSize = minRoomSize * 2 / 3 + 1; // 7 -> 5
        maxRoomSize = maxRoomSize * 2 / 3 + 1; // 13 -> 9

        int bigroomheight = HEIGHT - EDGEBUFFER * 2 - 4;

        Rect bigroom = new Rect(
                (WIDTH - EDGEBUFFER * 2) * 3 / 10 + EDGEBUFFER,
                EDGEBUFFER,
                (WIDTH - EDGEBUFFER * 2) * 7 / 10 + EDGEBUFFER,
                EDGEBUFFER + bigroomheight + 3
        );

        Rect topstores = new Rect(
                bigroom.left + 2,
                bigroom.top - 1,
                bigroom.right - 2,
                bigroom.top + bigroom.height() * 3 / 14
        );
        split(topstores);

        this.topstores = rooms;
        rooms = new HashSet<>();

        Rect bottomstores = new Rect(
                bigroom.left + 2,
                bigroom.bottom - bigroom.height() * 3 / 14,
                bigroom.right - 2,
                bigroom.bottom + 1
        );
        split(bottomstores);

        this.bottomstores = rooms;
        rooms = new HashSet<>();

        Rect midleftstores = new Rect(
                bigroom.left + 3,
                topstores.bottom + 3,
                bigroom.left + bigroom.width() / 3,
                bottomstores.top - 3
        );
        split(midleftstores);

        this.midleftstores = rooms;
        rooms = new HashSet<>();

        Rect midrightstores = new Rect(
                bigroom.right - bigroom.width() / 3,
                topstores.bottom + 3,
                bigroom.right - 3,
                bottomstores.top - 3
        );
        split(midrightstores);

        this.midrightstores = rooms;
        rooms = new HashSet<>();

        minRoomSize = _minroomsize;
        maxRoomSize = _maxroomsize;

        this.bigroom = new Room();
        this.bigroom.set(bigroom);

        split(new Rect(EDGEBUFFER, EDGEBUFFER, bigroom.left, HEIGHT - 1 - EDGEBUFFER));
        //split(new Rect(bigroom.left - 1, EDGEBUFFER, bigroom.right + 1, bigroom.top - 1));
        rooms.add(this.bigroom);
        //split(new Rect(bigroom.left - 1, bigroom.bottom + 1, bigroom.right + 1, HEIGHT - 1 - EDGEBUFFER));
        split(new Rect(bigroom.right, EDGEBUFFER, WIDTH - 1 - EDGEBUFFER, HEIGHT - 1 - EDGEBUFFER));
    }

    @Override
    protected boolean assignRoomType() {
        bigroom.type = Room.Type.PLAIN;

        determineShopSet(topstores);
        determineShopSet(midleftstores);
        determineShopSet(midrightstores);
        determineShopSet(bottomstores);

        Room leftmost = null;
        for (Room room : midrightstores) {
            if (leftmost == null || leftmost.left > room.left) {
                leftmost = room;
            }
        }

        leftmost.type = Room.Type.ALTAR;

        return super.assignRoomType();
    }

    private void determineShopSet(HashSet<Room> rooms) {
        for (Room room : rooms) {
            if (room.width() > 3) {
                if (Random.Int(4) == 0) {
                    room.type = Room.Type.STANDARD;
                } else {
                    room.type = Room.Type.SHOP;
                }
            }
            else {
                room.type = null;
            }
        }
    }

    @Override
    protected void paint() {
        super.paint();

        paintRoomSet(topstores, false, true);
        paintRoomSet(midleftstores, true, true);
        paintRoomSet(midrightstores, true, true);
        paintRoomSet(bottomstores, true, false);
    }

    private void paintRoomSet(HashSet<Room> rooms, boolean doorTop, boolean doorBottom) {
        HashSet<Room> newRooms = new HashSet<Room>();

        for (Room room : rooms) {
            if (room.type != null) {
                HashSet<Integer> doorcandidates = new HashSet<Integer>();

                if (doorTop) {
                    int y = room.top;
                    int pos;
                    for (int x = room.left + 1; x <= room.right - 1; x++) {
                        pos = x + y * WIDTH;

                        doorcandidates.add(pos);
                    }
                }

                if (doorBottom) {
                    int y = room.bottom;
                    int pos;
                    for (int x = room.left + 1; x <= room.right - 1; x++) {
                        pos = x + y * WIDTH;

                        doorcandidates.add(pos);
                    }
                }

                if (doorcandidates.size() > 0) {
                    ShopRoom shoproom = new ShopRoom(room);
                    int door = Random.element(doorcandidates);

                    shoproom.entrance = new Room.Door(door % WIDTH, door / WIDTH);

                    shoproom.type.paint(this, shoproom);
                    paintDoor(door, Room.Door.Type.REGULAR);
                    newRooms.add(shoproom);
                } else {
                    room.type = null;
                    newRooms.add(room);
                }
            }
        }

        rooms.clear();
        rooms.addAll(newRooms);
    }

    protected int minimumRooms() {
        return 2;
    }


        @Override
    public String tilesTex() {
        return Assets.TILES_PRISON;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_PRISON;
    }

    protected boolean[] water() {
        return Patch.generate(feeling == Feeling.WATER ? 0.60f : 0.45f, 6);
    }

    protected boolean[] grass() {
        return Patch.generate(feeling == Feeling.GRASS ? 0.55f : 0.35f, 3);
    }

    @Override
    protected void decorate() {

    }

    private static class ShopRoom extends Room {
        public ShopRoom(Room source) {
            super();

            set(source);
            type = source.type;
            connected = null;
        }

        private Door entrance;

        @Override
        public Door entrance() {
            return entrance;
        }

        public void entrance(Door door) {
            entrance = door;
        }
    }
}
