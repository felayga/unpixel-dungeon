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

import com.felayga.unpixeldungeon.Bones;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.consumable.scrolls.Scroll;
import com.felayga.unpixeldungeon.levels.Room.Type;
import com.felayga.unpixeldungeon.levels.branches.DungeonBranch;
import com.felayga.unpixeldungeon.levels.painters.Painter;
import com.felayga.unpixeldungeon.levels.traps.FireTrap;
import com.felayga.unpixeldungeon.levels.traps.Trap;
import com.felayga.unpixeldungeon.levels.traps.WornTrap;
import com.felayga.unpixeldungeon.mechanics.Roll;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class RegularLevel extends Level {

    protected HashSet<Room> rooms;

    protected Room roomEntrance;
    protected Room roomExit;

    protected ArrayList<Room.Type> specials;
    protected ArrayList<Room.Type> specialsExtra;

    public int secretDoors;

    public RegularLevel(int flags) {
        super(flags);
    }

    protected boolean determineEntranceExit() {
        do {
            roomEntrance = Random.element(rooms);
        } while (roomEntrance.width() < 4 || roomEntrance.height() < 4);

        do {
            roomExit = Random.element(rooms);
        } while (roomExit == roomEntrance || roomExit.width() < 4 || roomExit.height() < 4);

        return true;
    }

    protected void buildSpecials() {
        specials.addAll(Room.SPECIALS);

        if (Dungeon.depth() >= 3 && Dungeon.depth() >= DungeonBranch.Normal.levelMin && Dungeon.depth() <= DungeonBranch.Normal.levelMax) {
            if (3.0f / (float) (Dungeon.depthAdjusted - 1) >= Random.Float()) {
                specialsExtra.add(Room.Type.SHOP);
            }
        }

        if ((flags & FLAG_CHASM_NOT_DIGGABLE) != 0) {
            specials.remove(Room.Type.WEAK_FLOOR);
        }

        for (DungeonBranch branch : DungeonBranch.values()) {
            if (branch.branchLevel() == Dungeon.depth()) {
                GLog.d("branch=" + branch + " level=" + branch.branchLevel() + " depth=" + Dungeon.depth() + " (true)");
                if (branch.branchDown) {
                    specialsExtra.add(Type.EXIT_ALTERNATE);
                } else {
                    specialsExtra.add(Type.ENTRANCE_ALTERNATE);
                }
            } else {
                GLog.d("branch=" + branch + " level=" + branch.branchLevel() + " depth=" + Dungeon.depth() + " (false)");
            }
        }
    }

    @Override
    protected boolean build() {
        if (!initRooms()) {
            return false;
        }

        int distance = 0;
        int retry = 0;
        int minDistance = (int) Math.sqrt(rooms.size());
        do {
            if (!determineEntranceExit()) {
                continue;
            }

            Graph.buildDistanceMap(rooms, roomExit);
            distance = roomEntrance.distance();

            if (retry++ > 10) {
                return false;
            }
        } while (distance < minDistance);

        roomEntrance.type = Type.ENTRANCE;
        roomExit.type = Type.EXIT;

        HashSet<Room> connected = new HashSet<>();
        connected.add(roomEntrance);

        Graph.buildDistanceMap(rooms, roomExit);
        List<Room> path = Graph.buildPath(rooms, roomEntrance, roomExit);

        Room room = roomEntrance;
        for (Room next : path) {
            room.connect(next);
            room = next;
            connected.add(room);
        }

        Graph.setPrice(path, roomEntrance.distance);

        Graph.buildDistanceMap(rooms, roomExit);
        path = Graph.buildPath(rooms, roomEntrance, roomExit);

        room = roomEntrance;
        for (Room next : path) {
            room.connect(next);
            room = next;
            connected.add(room);
        }

        int nConnected = (int) (rooms.size() * Random.Float(0.5f, 0.7f));
        while (connected.size() < nConnected) {
            Room cr = Random.element(connected);
            Room or = Random.element(cr.neigbours);
            if (!connected.contains(or)) {
                cr.connect(or);
                connected.add(or);
            }
        }

        specials = new ArrayList<>();
        specialsExtra = new ArrayList<>();
        buildSpecials();

        /*
        if (Dungeon.isChallenged(Challenges.NO_ARMOR)) {
            //no sense in giving an armor reward room on a run with no armor.
            specials.remove(Room.Type.CRYPT);
        }
        if (Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
            //sorry warden, no lucky sungrass or blandfruit seeds for you!
            specials.remove(Room.Type.GARDEN);
        }
        */
        if (!assignRoomType()) {
            return false;
        }

        paint();
        paintWater();
        paintGrass();

        placeTraps();

        return specialsExtra.size() == 0;
    }

    protected void placeSign() {
        while (true) {
            int pos = roomEntrance.random();
            if (pos != entrance && traps.get(pos) == null && findMob(pos) == null) {
                map(pos, Terrain.SIGN);
                break;
            }
        }
    }

    protected boolean initRooms() {
        rooms = new HashSet<>();
        splitRooms();

        if (rooms.size() < 10) {
            return false;
        }

        Room[] ra = rooms.toArray(new Room[0]);
        for (int i = 0; i < ra.length - 1; i++) {
            for (int j = i + 1; j < ra.length; j++) {
                ra[i].addNeigbour(ra[j]);
            }
        }

        return true;
    }

    protected void splitRooms() {
        split(new Rect(0 + Level.EDGEBUFFER, 0 + Level.EDGEBUFFER, WIDTH - 1 - Level.EDGEBUFFER, HEIGHT - 1 - Level.EDGEBUFFER));
    }

    protected boolean assignRoomType() {
        int specialRooms = 0;
        boolean pitMade = false;

        while (specialsExtra.size() > 0) {
            Room.Type type = specialsExtra.get(0);

            Room r = null;
            for (Room subr : rooms) {
                GLog.d("test type="+type.toString());
                if (subr.width() > 3 && subr.height() > 3 && type.canUse(subr)) {
                    r = subr;
                    break;
                }
            }

            if (r != null) {
                r.type = type;
                Room.useType(r.type);
                GLog.d("placed specialextra room=" + r.type.name() + " at r=" + r.center());
            } else {
                GLog.d("failed to place specialextra room=" + type.name());
            }
            specialsExtra.remove(0);
        }

        for (Room r : rooms) {
            if (r.type == Type.NULL && r.connected.size() > 0) {
                if (specials.size() > 0 &&
                        r.width() > 3 && r.height() > 3 &&
                        Random.Int(specialRooms * specialRooms + 2) == 0) {

                    Room.Type type;

                    if (pitRoomNeeded && !pitMade) {
                        type = Type.PIT;
                        pitMade = true;

                        specials.remove(Type.ARMORY);
                        specials.remove(Type.CRYPT);
                        specials.remove(Type.LABORATORY);
                        //specials.remove(Type.LIBRARY);
                        //specials.remove(Type.STATUE);
                        specials.remove(Type.TREASURY);
                        specials.remove(Type.VAULT);
                        specials.remove(Type.WEAK_FLOOR);
                    } else if (specials.contains(Type.LABORATORY)) {
                        type = Type.LABORATORY;
                    } else if (specials.contains(Type.MAGIC_WELL)) {
                        type = Type.MAGIC_WELL;
                    } else {
                        int n = specials.size();
                        type = specials.get(Math.min(Random.Int(n), Random.Int(n)));
                        if (r.type == Type.WEAK_FLOOR) {
                            weakFloorCreated = true;
                        }
                    }

                    if (type.canUse(r)) {
                        r.type = type;
                        Room.useType(r.type);
                        specials.remove(r.type);
                        specialRooms++;
                    } else {
                        GLog.d("can't use " + type.toString());
                    }
                } else if (Random.Int(2) == 0) {
                    HashSet<Room> neigbours = new HashSet<Room>();
                    for (Room n : r.neigbours) {
                        if (!r.connected.containsKey(n) &&
                                !Room.SPECIALS.contains(n.type) &&
                                n.type != Type.PIT) {
                            neigbours.add(n);
                        }
                    }
                    if (neigbours.size() > 1) {
                        r.connect(Random.element(neigbours));
                    }
                }
            }
        }

        if (pitRoomNeeded && !pitMade) return false;

        int count = 0;
        for (Room r : rooms) {
            if (r.type == Type.NULL) {
                int connections = r.connected.size();
                if (connections == 0) {

                } else if (Random.Int(connections * connections + 40) == 0) {
                    r.type = Type.STANDARD;
                    count++;
                } else {
                    r.type = Type.TUNNEL;
                }
            }
        }

        while (count + specialRooms < minimumRooms() || count < 2) {
            Room r = randomRoom(Type.TUNNEL, 1);
            if (r != null) {
                r.type = Type.STANDARD;
                count++;
            }
        }

        for (Room r : rooms) {
            if (r.type == Type.NULL) {
                fill(r.left, r.top, r.width(), r.height(), Terrain.WALL);
            }
        }

        return true;
    }

    protected int minimumRooms() {
        return 6;
    }

    protected void paintWater() {
        boolean[] lake = water();
        for (int i = 0; i < LENGTH; i++) {
            if (map(i) == Terrain.EMPTY && lake[i]) {
                map(i, Terrain.PUDDLE);
            }
        }
    }

    protected void paintGrass() {
        boolean[] grass = grass();

        if (feeling == Feeling.GRASS) {

            for (Room room : rooms) {
                if (room.type != Type.NULL && room.type != Type.PASSAGE && room.type != Type.TUNNEL) {
                    grass[(room.left + 1) + (room.top + 1) * WIDTH] = true;
                    grass[(room.right - 1) + (room.top + 1) * WIDTH] = true;
                    grass[(room.left + 1) + (room.bottom - 1) * WIDTH] = true;
                    grass[(room.right - 1) + (room.bottom - 1) * WIDTH] = true;
                }
            }
        }

        for (int i = WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
            if (map(i) == Terrain.EMPTY && grass[i]) {
                int count = 1;
                for (int n : NEIGHBOURS8) {
                    if (grass[i + n]) {
                        count++;
                    }
                }
                map(i, (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS);
            }
        }
    }

    protected abstract boolean[] water();

    protected abstract boolean[] grass();

    protected void placeTraps() {
        int nTraps = nTraps();
        float[] trapChances = trapChances();
        Class<?>[] trapClasses = trapClasses();

        LinkedList<Integer> validCells = new LinkedList<Integer>();

        for (int i = 0; i < LENGTH; i++) {
            if (map(i) == Terrain.EMPTY) {

                if (Dungeon.depth() == 1) {
                    //extra check to prevent annoying inactive traps in hallways on floor 1
                    Room r = room(i);
                    if (r != null && r.type != Type.TUNNEL) {
                        validCells.add(i);
                    }
                } else
                    validCells.add(i);
            }
        }

        //no more than one trap every 5 valid tiles.
        nTraps = Math.min(nTraps, validCells.size() / 5);

        Collections.shuffle(validCells);

        for (int i = 0; i < nTraps; i++) {

            int trapPos = validCells.removeFirst();

            try {
                Trap trap = ((Trap) trapClasses[Random.chances(trapChances)].newInstance()).hide();
                setTrap(null, trap, trapPos);
                //some traps will not be hidden
                map(trapPos, trap.visible ? Terrain.TRAP : Terrain.SECRET_TRAP);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected int nTraps() {
        return Random.NormalIntRange(1, 4 + (Dungeon.depthAdjusted / 2));
    }

    protected Class<?>[] trapClasses() {
        return new Class<?>[]{WornTrap.class};
    }

    protected float[] trapChances() {
        return new float[]{1};
    }

    protected int minRoomSize = 7; //original=7
    protected int maxRoomSize = 13; //original=9 -> 13

    protected int splitMargin = 2;

    protected void split(Rect rect) {
        int w = rect.width();
        int h = rect.height();

        if (w > minRoomSize * 2 && h < minRoomSize) {
            int vw = Random.Int(rect.left + splitMargin, rect.right - splitMargin);
            split(new Rect(rect.left, rect.top, vw, rect.bottom));
            split(new Rect(vw, rect.top, rect.right, rect.bottom));
        } else if (h > minRoomSize * 2 && w < minRoomSize) {
            int vh = Random.Int(rect.top + splitMargin, rect.bottom - splitMargin);
            split(new Rect(rect.left, rect.top, rect.right, vh));
            split(new Rect(rect.left, vh, rect.right, rect.bottom));
        } else if ((Math.random() <= (minRoomSize * minRoomSize / rect.square()) && w <= maxRoomSize && h <= maxRoomSize) || w < minRoomSize || h < minRoomSize) {
            rooms.add((Room) new Room().set(rect));
        } else {
            if (Random.Float() < (float) (w - 2) / (w + h - 4)) {
                int vw = Random.Int(rect.left + splitMargin, rect.right - splitMargin);
                split(new Rect(rect.left, rect.top, vw, rect.bottom));
                split(new Rect(vw, rect.top, rect.right, rect.bottom));
            } else {
                int vh = Random.Int(rect.top + splitMargin, rect.bottom - splitMargin);
                split(new Rect(rect.left, rect.top, rect.right, vh));
                split(new Rect(rect.left, vh, rect.right, rect.bottom));
            }
        }
    }

    protected void paint() {
        for (Room r : rooms) {
            if (r.type != Type.NULL) {
                placeDoors(r);
                r.type.paint(this, r);
            } else {
                if (feeling == Feeling.CHASM && Random.Int(2) == 0) {
                    Painter.fill(this, r, Terrain.WALL);
                }
            }
        }

        for (Room r : rooms) {
            paintDoors(r);
        }
    }

    private void placeDoors(Room r) {
        for (Room n : r.connected.keySet()) {
            Room.Door door = r.connected.get(n);
            if (door == null) {

                Rect i = r.intersect(n);
                if (i.width() == 0) {
                    door = new Room.Door(
                            i.left,
                            Random.Int(i.top + 1, i.bottom));
                } else {
                    door = new Room.Door(
                            Random.Int(i.left + 1, i.right),
                            i.top);
                }

                r.connected.put(n, door);
                n.connected.put(r, door);
            }
        }
    }

    protected void paintDoors(Room r) {
        for (Room n : r.connected.keySet()) {
            if (joinRooms(r, n)) {
                continue;
            }

            Room.Door d = r.connected.get(n);
            int door = d.x + d.y * WIDTH;

            paintDoor(door, d.type);
        }
    }

    protected void paintDoor(int pos, Room.Door.Type doorType) {
        switch (doorType) {
            case EMPTY:
                map(pos, Terrain.EMPTY);
                break;
            case TUNNEL:
                map(pos, tunnelTile());
                break;
            case REGULAR:
                assignDoor(pos, Terrain.DOOR, Terrain.SECRET_DOOR, Terrain.OPEN_DOOR, Terrain.LOCKED_DOOR, Terrain.SECRET_LOCKED_DOOR, Terrain.WOOD_DEBRIS);
                break;
            case REGULAR_UNBROKEN:
                assignDoor(pos, Terrain.DOOR, Terrain.SECRET_DOOR, Terrain.OPEN_DOOR, Terrain.LOCKED_DOOR, Terrain.SECRET_LOCKED_DOOR, Terrain.DOOR);
                break;
            case UNLOCKED:
                map(pos, Terrain.DOOR);
                break;
            case BARRICADE:
                map(pos, Terrain.BARRICADE);
                break;
            case LOCKED:
                assignDoor(pos, Terrain.LOCKED_DOOR, Terrain.SECRET_LOCKED_DOOR, Terrain.LOCKED_DOOR, Terrain.DOOR, Terrain.SECRET_DOOR, Terrain.LOCKED_DOOR);
                break;
            case NICHE:
                assignDoor(pos, Terrain.WALL, Terrain.LOCKED_DOOR, Terrain.SECRET_LOCKED_DOOR, Terrain.LOCKED_DOOR, Terrain.DOOR, Terrain.SECRET_DOOR);
                break;
            case JAIL:
                map(pos, Terrain.IRON_BARS);
                break;
            case SECRET:
                assignDoor(pos, Terrain.SECRET_DOOR, Terrain.SECRET_LOCKED_DOOR, Terrain.SECRET_LOCKED_DOOR, Terrain.SECRET_DOOR, Terrain.SECRET_LOCKED_DOOR, Terrain.SECRET_LOCKED_DOOR);
                break;
        }
    }

    private void assignDoor(int pos, int regular, int regularHidden, int regularBroken, int other, int otherHidden, int otherBroken) {
        /* selection probabilities based on depthAdjusted
            depth<2 {0.44, 0.00, 0.22, 0.22, 0.00, 0.11}
            depth=2 {0.40, 0.07, 0.20, 0.20, 0.03, 0.10}
            depth=3 {0.40, 0.07, 0.20, 0.19, 0.04, 0.10}
            depth=4 {0.39, 0.08, 0.19, 0.19, 0.04, 0.10}
            depth=5 {0.38, 0.09, 0.19, 0.19, 0.05, 0.09}
            depth>5 {0.37, 0.11, 0.18, 0.18, 0.06, 0.09}
        */

        if (Random.Int(3) == 0) {
            regular = other;
            regularHidden = otherHidden;
            regularBroken = otherBroken;
        }

        if (Dungeon.depthAdjusted > 1) {
            boolean secret = (Dungeon.depthAdjusted < 6 ? Random.Int(12 - Dungeon.depthAdjusted) : Random.Int(6)) == 0;

            if (secret) {
                map(pos, regularHidden);
                secretDoors++;
                return;
            }
        }

        if (Random.Int(3) == 0) {
            map(pos, regularBroken);
        } else {
            map(pos, regular);
        }
    }

    protected boolean joinRooms(Room r, Room n) {

        if (r.type != Room.Type.STANDARD || n.type != Room.Type.STANDARD) {
            return false;
        }

        Rect w = r.intersect(n);
        if (w.left == w.right) {

            if (w.bottom - w.top < 3) {
                return false;
            }

            if (w.height() == Math.max(r.height(), n.height())) {
                return false;
            }

            if (r.width() + n.width() > maxRoomSize) {
                return false;
            }

            w.top += 1;
            w.bottom -= 0;

            w.right++;

            Painter.fill(this, w.left, w.top, 1, w.height(), Terrain.EMPTY);

        } else {

            if (w.right - w.left < 3) {
                return false;
            }

            if (w.width() == Math.max(r.width(), n.width())) {
                return false;
            }

            if (r.height() + n.height() > maxRoomSize) {
                return false;
            }

            w.left += 1;
            w.right -= 0;

            w.bottom++;

            Painter.fill(this, w.left, w.top, w.width(), 1, Terrain.EMPTY);
        }

        return true;
    }

    @Override
    public int nMobs() {
        switch (Dungeon.depthAdjusted) {
            case 1:
                //mobs are not randomly spawned on floor 1.
                return 0;
            default:
                return 4 + Dungeon.depthAdjusted % 3 + Random.Int(12);
        }
    }

    @Override
    protected void createMobs() {
        //on floor 1, 10 rats are created so the player can get level 2.
        int mobsToSpawn = Dungeon.depthAdjusted == 1 ? 10 : nMobs();

        HashSet<Room> stdRooms = new HashSet<>();
        for (Room room : rooms) {
            if (room.type == Type.STANDARD) stdRooms.add(room);
        }
        Iterator<Room> stdRoomIter = stdRooms.iterator();

        while (mobsToSpawn > 0) {
            if (!stdRoomIter.hasNext()) {
                stdRoomIter = stdRooms.iterator();
            }

            final Room roomToSpawn = stdRoomIter.next();

            Bestiary.spawn(new Bestiary.SpawnParams(Dungeon.depthAdjusted, Dungeon.hero.level, true) {
                @Override
                public Level level() {
                    return RegularLevel.this;
                }

                @Override
                public int position() {
                    return roomToSpawn.random();
                }

                @Override
                public void initialize(Mob mob) {
                    mobs.add(mob);
                }
            });

            mobsToSpawn--;

            /*
            Mob mob = Bestiary.mob(Dungeon.depth, Dungeon.hero.level);
            mob.pos = roomToSpawn.random();

            if (findMob(mob.pos) == null && Level.passable[mob.pos]) {
                mobsToSpawn--;
                mobs.add(mob);

                //TODO: perhaps externalize this logic into a method. Do I want to make mobs more likely to clump deeper down?
                if (mobsToSpawn > 0 && Random.Int(4) == 0) {
                    mob = Bestiary.mob(Dungeon.depth, Dungeon.hero.level);
                    mob.pos = roomToSpawn.random();

                    if (findMob(mob.pos) == null && Level.passable[mob.pos]) {
                        mobsToSpawn--;
                        mobs.add(mob);
                    }
                }
            }
            */
        }
    }

    @Override
    public int randomRespawnCell() {
        int count = 0;
        int cell = -1;

        while (true) {

            if (++count > 30) {
                return -1;
            }

            Room room = randomRoom(Room.Type.STANDARD, 10);
            if (room == null) {
                continue;
            }

            cell = room.random();
            if (!Dungeon.visible[cell] && Actor.findChar(cell) == null && Level.passable[cell]) {
                return cell;
            }

        }
    }

    @Override
    public int randomDestination() {

        int cell = -1;

        while (true) {

            Room room = Random.element(rooms);
            if (room == null) {
                continue;
            }

            cell = room.random();
            if (Level.passable[cell]) {
                return cell;
            }

        }
    }

    @Override
    protected void createItems() {

        int nItems = 5;

        float dropBonusChance = Roll.DropBonusChance(Dungeon.hero);
        while (Random.Float() < dropBonusChance) {
            nItems++;
        }

        for (int i = 0; i < nItems; i++) {
            int cell = randomDropCell();
            Heap.Type type = null;

            switch (Random.Int(20)) {
                case 1:
                    if (Dungeon.depthAdjusted > 1) {
                        //todo: mimic spawning
                        break;
                    }
                case 2:
                case 3:
                case 4:
                case 5:
                    Item chest = Generator.random(Generator.Category.CONTAINER);
                    drop(chest, cell);
                    break;
                default:
                    type = Heap.Type.HEAP;
                    break;
            }

            if (type != null) {
                drop(Generator.random(), cell).type = type;
            }

        }

        for (Item item : itemsToSpawn) {
            int cell = randomDropCell();
            if (item instanceof Scroll) {
                int terrain = map(cell);
                while ((terrain == Terrain.TRAP || terrain == Terrain.SECRET_TRAP)
                        && traps.get(cell) instanceof FireTrap) {
                    cell = randomDropCell();
                }
            }
            drop(item, cell).type = Heap.Type.HEAP;
        }

        //todo: placement of bones after overhaul
        /*
        Item item = Bones.get();
        if (item != null) {
            drop(item, randomDropCell()).type = Heap.Type.REMAINS;
        }
        */
    }

    protected Room randomRoom(Room.Type type, int tries) {
        for (int i = 0; i < tries; i++) {
            Room room = Random.element(rooms);
            if (room.type == type) {
                return room;
            }
        }
        return null;
    }

    public Room room(int pos) {
        for (Room room : rooms) {
            if (room.type != Type.NULL && room.inside(pos)) {
                return room;
            }
        }

        return null;
    }

    protected int randomDropCell() {
        while (true) {
            Room room = randomRoom(Room.Type.STANDARD, 1);
            if (room != null) {
                int pos = room.random();
                if (passable[pos]) {
                    return pos;
                }
            }
        }
    }

    @Override
    public int pitCell() {
        for (Room room : rooms) {
            if (room.type == Type.PIT) {
                return room.random();
            }
        }

        return super.pitCell();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        if (rooms != null) {
            bundle.put("rooms", rooms);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        if (bundle.contains("rooms")) {
            rooms = new HashSet<>((Collection<Room>) ((Collection<?>) bundle.getCollection("rooms")));
            for (Room r : rooms) {
                if (r.type == Type.WEAK_FLOOR) {
                    weakFloorCreated = true;
                    break;
                }
                if (r.type == Type.ENTRANCE) {
                    roomEntrance = r;
                } else if (r.type == Type.EXIT || r.type == Type.BOSS_EXIT) {
                    roomExit = r;
                }
            }
        }
    }

}
