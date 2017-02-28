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
import com.felayga.unpixeldungeon.Challenges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.WarningHandler;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Alchemy;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.LockedFloor;
import com.felayga.unpixeldungeon.actors.buffs.hero.Shadows;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.positive.ItemVision;
import com.felayga.unpixeldungeon.actors.buffs.positive.Light;
import com.felayga.unpixeldungeon.actors.buffs.positive.MindVision;
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.felayga.unpixeldungeon.effects.particles.FlowParticle;
import com.felayga.unpixeldungeon.effects.particles.WindParticle;
import com.felayga.unpixeldungeon.items.Dewdrop;
import com.felayga.unpixeldungeon.items.Gemstone;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.felayga.unpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.felayga.unpixeldungeon.items.bags.ScrollHolder;
import com.felayga.unpixeldungeon.items.bags.SeedPouch;
import com.felayga.unpixeldungeon.items.food.Blandfruit;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.items.rings.RingOfWealth;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.levels.features.Chasm;
import com.felayga.unpixeldungeon.levels.features.HighGrass;
import com.felayga.unpixeldungeon.levels.painters.Painter;
import com.felayga.unpixeldungeon.levels.traps.Trap;
import com.felayga.unpixeldungeon.levels.traps.WornTrap;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.mechanics.ShadowCaster;
import com.felayga.unpixeldungeon.plants.BlandfruitBush;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.ui.CustomTileVisual;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//import com.felayga.unpixeldungeon.items.artifacts.DriedRose;

public abstract class Level implements Bundlable, IDecayable {

    public static enum Feeling {
        NONE,
        CHASM,
        WATER,
        GRASS,
        DARK
    }

    public static final int WIDTH       = 97; //79
    public static final int HEIGHT      = 31; //21
    public static final int LENGTH      = WIDTH * HEIGHT;
    public static final int EDGEBUFFER  = 7;

    public static final int[] NEIGHBOURS4 = {-WIDTH, +1, +WIDTH, -1};
    public static final int[] NEIGHBOURS4DIAGONAL = {+1 - WIDTH, +1 + WIDTH, -1 + WIDTH, -1 - WIDTH};
    public static final int[] NEIGHBOURS5 = {0, -WIDTH, +1, +WIDTH, -1};
    public static final int[] NEIGHBOURS8 = {-WIDTH, +1 - WIDTH, +1, +1 + WIDTH, +WIDTH, -1 + WIDTH, -1, -1 - WIDTH};
    public static final int[] NEIGHBOURS9 = {0, -WIDTH, +1 - WIDTH, +1, +1 + WIDTH, +WIDTH, -1 + WIDTH, -1, -1 - WIDTH};

    //make sure to check insideMap() when using these, as there's a risk something may be outside the map
    public static final int[] NEIGHBOURS8DIST2 = {+2 + 2 * WIDTH, +1 + 2 * WIDTH, 2 * WIDTH, -1 + 2 * WIDTH, -2 + 2 * WIDTH,
            +2 + WIDTH, +1 + WIDTH, +WIDTH, -1 + WIDTH, -2 + WIDTH,
            +2, +1, -1, -2,
            +2 - WIDTH, +1 - WIDTH, -WIDTH, -1 - WIDTH, -2 - WIDTH,
            +2 - 2 * WIDTH, +1 - 2 * WIDTH, -2 * WIDTH, -1 - 2 * WIDTH, -2 - 2 * WIDTH};
    public static final int[] NEIGHBOURS9DIST2 = {+2 + 2 * WIDTH, +1 + 2 * WIDTH, 2 * WIDTH, -1 + 2 * WIDTH, -2 + 2 * WIDTH,
            +2 + WIDTH, +1 + WIDTH, +WIDTH, -1 + WIDTH, -2 + WIDTH,
            +2, +1, 0, -1, -2,
            +2 - WIDTH, +1 - WIDTH, -WIDTH, -1 - WIDTH, -2 - WIDTH,
            +2 - 2 * WIDTH, +1 - 2 * WIDTH, -2 * WIDTH, -1 - 2 * WIDTH, -2 - 2 * WIDTH};


    protected static final long TIME_TO_RESPAWN = GameTime.TICK * 25;

    private static final String TXT_HIDDEN_PLATE_CLICKS = "A hidden pressure plate clicks!";

    public static boolean resizingNeeded;
    public static int loadedMapSize;


    public int version;
    public int[] map;
    public boolean[] visited;
    public boolean[] mapped;
    public long time;

    public WarningHandler warnings = new WarningHandler();

    public int viewDistance = 8;

    public static boolean[] fieldOfView = new boolean[LENGTH];
    public static boolean[] fieldOfTouch = new boolean[LENGTH];
    public static boolean[] fieldOfSound = new boolean[LENGTH];

    public static boolean[] passable = new boolean[LENGTH];
    public static boolean[] pathable = new boolean[LENGTH];
    public static boolean[] diagonal = new boolean[LENGTH];
    public static boolean[] losBlocking = new boolean[LENGTH];
    public static boolean[] secret = new boolean[LENGTH];
    public static boolean[] solid = new boolean[LENGTH];
    public static boolean[] avoid = new boolean[LENGTH];
    public static boolean[] puddle = new boolean[LENGTH];
    public static boolean[] pit = new boolean[LENGTH];
    public static boolean[] burnable = new boolean[LENGTH];
    public static boolean[] stone = new boolean[LENGTH];
    public static boolean[] losDark = new boolean[LENGTH];
    public int[] lightMap = new int[LENGTH];

    public static boolean[] discoverable = new boolean[LENGTH];

    public static int flags = 0;
    private int flagsLocal = 0;

    public static final int FLAG_UNDIGGABLEWALLS    = 0x0001;
    public static final int FLAG_UNDIGGABLEFLOOR    = 0x0002;
    public static final int FLAG_UNDIGGABLEBOULDERS = 0x0004;
    public static final int FLAG_NOTELEPORTATION    = 0x0008;


    //lightMap: 0x01 dark 0x02 magiclit 0x04 magicdark
    //lightMap: 0x00 lit 0x01 dark 0x02 0x03 magiclit 0x04 0x05 magicdark 0x06 0x07 invalid (0x02 and 0x04 are exclusive)
    //lightMap: 0x00FFFFF8 handled by Light.Registry, 0x7F000000 reserved for mobile darkness if I feel like implementing it
    //dark: 0x01 0x04 0x05
    public static final int LIGHTMAP_NATURALMASK    = 0x00FFFFFE;
    public static final int LIGHTMAP_NATURALLIT     = 0x00000000;
    public static final int LIGHTMAP_NATURALDARK    = 0x00000001;

    public static final int LIGHTMAP_ARTIFICIALMASK = 0x00FFFFF9;
    public static final int LIGHTMAP_ARTIFICIALLIT  = 0x00000002;
    public static final int LIGHTMAP_ARTIFICIALDARK = 0x00000004;

    public static final int LIGHTMAP_MOBILEMASK     = 0x00000007;
    public static final int LIGHTMAP_MOBILELIT      = 0x00FFFFF8;

    public static final int LIGHTMAP_FULLMASK       = 0x00FFFFFF;


    public void setLight(int pos, int radius, boolean light) {
        if (radius > 0) {
            int centerx = pos % WIDTH;
            int centery = pos / WIDTH;

            int y1 = centery + radius;

            for (int x1 = centerx; x1 >= centerx - radius; x1--) {
                while (distance(x1, y1, centerx, centery) > radius) {
                    y1--;
                }

                GLog.d("x1="+x1+" y1="+y1+" distance="+distance(x1, y1, centerx, centery));

                int x2 = centerx + centerx - x1;
                int y2 = centery + centery - y1;

                if (x1 >= 0 && x1 < WIDTH) {
                    if (y1 >= 0 && y1 < HEIGHT) {
                        setLight(x1 + y1 * WIDTH, 0, light);
                    }

                    if (y2 != y1) {
                        for (int suby2 = y2;suby2 < y1;suby2++) {
                            if (suby2 >= 0 && suby2 < HEIGHT) {
                                setLight(x1 + suby2 * WIDTH, 0, light);
                            }
                        }
                    }
                }

                if (x2 != x1 && x1 >= 0 && x2 < WIDTH) {
                    if (y1 >= 0 && y1 < HEIGHT) {
                        setLight(x2 + y1 * WIDTH, 0, light);
                    }

                    if (y2 != y1) {
                        for (int suby2 = y2;suby2 < y1;suby2++) {
                            if (suby2 >= 0 && suby2 < HEIGHT) {
                                setLight(x2 + suby2 * WIDTH, 0, light);
                            }
                        }
                    }
                }
            }
            GameScene.updateMap();
            Dungeon.observe();
        } else {
            lightMap[pos] = (lightMap[pos] & LIGHTMAP_ARTIFICIALMASK) | (light ? LIGHTMAP_ARTIFICIALLIT : LIGHTMAP_ARTIFICIALDARK);
            updateLightMap(pos);
        }
    }


    public Feeling feeling = Feeling.NONE;

    public int entrance;
    public int exit;
    public int entranceAlternate;
    public int exitAlternate;

    //when a boss level has become locked.
    public boolean locked = false;

    public HashSet<Mob> mobs;
    public SparseArray<Heap> heaps;
    public HashMap<Class<? extends Blob>, Blob> blobs;
    public SparseArray<Plant> plants;
    public SparseArray<Trap> traps;
    public HashSet<CustomTileVisual> customTiles;

    protected ArrayList<Item> itemsToSpawn = new ArrayList<>();

    protected Group visuals;

    public int color1 = 0x004400;
    public int color2 = 0x88CC44;

    protected static boolean pitRoomNeeded = false;
    protected static boolean weakFloorCreated = false;

    private static final String VERSION = "version";
    private static final String MAP = "map";
    private static final String VISITED = "visited";
    private static final String MAPPED = "mapped";
    private static final String ENTRANCE = "entrance";
    private static final String EXIT = "exit";
    private static final String ENTRANCEALTERNATE = "entranceAlternate";
    private static final String EXITALTERNATE = "exitAlternate";
    private static final String LOCKED = "locked";
    private static final String LIGHTMAP = "lightMap";
    private static final String HEAPS = "heaps";
    private static final String PLANTS = "plants";
    private static final String TRAPS = "traps";
    private static final String CUSTOM_TILES = "customTiles";
    private static final String MOBS = "mobs";
    private static final String BLOBS = "blobs";
    private static final String FEELING = "feeling";
    private static final String TIME = "time";
    private static final String FLAGSLOCAL = "flagsLocal";

    public Level(int flags) {
        flagsLocal = flags;
    }

    public void create() {
        resizingNeeded = false;

        map = new int[LENGTH];
        visited = new boolean[LENGTH];
        Arrays.fill(visited, false);
        mapped = new boolean[LENGTH];
        Arrays.fill(mapped, false);


        addItemToSpawn(Generator.random(Generator.Category.FOOD));

        int bonus = 0;
        for (Buff buff : Dungeon.hero.buffs(RingOfWealth.Wealth.class)) {
            bonus += ((RingOfWealth.Wealth) buff).level;
        }

        //todo: bonus item generation
        /*
        if (Random.Float() > Math.pow(0.95, bonus)) {
            if (Random.Int(2) == 0)
                addItemToSpawn(new ScrollOfMagicalInfusion());
            else
                addItemToSpawn(new PotionOfMight());
        }
        */

        /*
        DriedRose rose = Dungeon.hero.belongings.getItem(DriedRose.class);
        if (rose != null && rose.bucStatus() != BUCStatus.Cursed) {
            //this way if a rose is dropped later in the game, player still has a chance to max it out.
            int petalsNeeded = (int) Math.ceil((float) ((Dungeon.depth / 2) - rose.droppedPetals) / 3);

            for (int i = 1; i <= petalsNeeded; i++) {
                //the player may miss a single petal and still max their rose.
                if (rose.droppedPetals < 11) {
                    addItemToSpawn(new DriedRose.Petal());
                    rose.droppedPetals++;
                }
            }
        }
        */

        feeling = determineFeeling();
        applyFeeling(feeling);

        boolean pitNeeded = Dungeon.depthAdjusted > 1 && weakFloorCreated;

        do {
            initializeMap();

            pitRoomNeeded = pitNeeded;
            weakFloorCreated = false;

            mobs = new HashSet<>();
            heaps = new SparseArray<>();
            blobs = new HashMap<>();
            plants = new SparseArray<>();
            traps = new SparseArray<>();
            customTiles = new HashSet<>();

        } while (!build());
        decorate();

        buildFlagMaps();
        updateLightMap();
        cleanWalls();

        createMobs();
        createItems();

        stoneInside();
    }

    protected Feeling determineFeeling() {
        Feeling retval = Feeling.NONE;

        if (Dungeon.depthAdjusted > 1) {
            switch (Random.Int(10)) {
                case 0:
                    retval = Feeling.WATER;
                    break;
                case 1:
                    retval = Feeling.GRASS;
                    break;
                case 2:
                    retval = Feeling.DARK;
                    break;
                case 3:
                    if ((flagsLocal & FLAG_UNDIGGABLEFLOOR) == 0) {
                        retval = Feeling.CHASM;
                    }
                    break;
            }
        }

        return retval;
    }

    private final void applyFeeling(Feeling feeling) {
        boolean dark = Dungeon.isChallenged(Challenges.DARKNESS);

        switch(feeling) {
            case DARK:
                dark = true;
                //addItemToSpawn(new Torch());
                //viewDistance = (int) Math.ceil(viewDistance / 3f);
                break;
        }

        if (dark) {
            Arrays.fill(lightMap, LIGHTMAP_ARTIFICIALDARK);
        }
    }

    protected void initializeMap() {
        Arrays.fill(map, feeling == Feeling.CHASM ? fillBlockChasm() : fillBlockNormal());
    }

    protected int fillBlockNormal() {
        return Terrain.WALL;
    }

    protected int fillBlockChasm() {
        return Terrain.CHASM;
    }

    public void reset() {
        for (Mob mob : mobs.toArray(new Mob[0])) {
            if (!mob.reset()) {
                mobs.remove(mob);
            }
        }
        createMobs();
    }


    @Override
    public long decay() {
        return 0;
    }

    @Override
    public boolean decay(long currentTime, boolean updateTime, boolean fixTime) {
        warnings.decay(currentTime, updateTime, fixTime);

        if (updateTime && !fixTime) {
            if (time < currentTime) {
                time = currentTime;
            } else {
                return false;
            }
        }

        boolean updated = false;

        for (int n = 0; n < heaps.size(); n++) {
            Heap heap = heaps.valueAt(n);

            Iterator<Item> iterator = heap.iterator(false);
            boolean subupdated = false;
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item instanceof IDecayable) {
                    IDecayable decayable = (IDecayable) item;
                    if (decayable.decay(currentTime, updateTime, fixTime)) {
                        iterator.remove();
                        subupdated = true;
                    }
                }
            }

            if (subupdated) {
                heap.updateImage();
                n--;
                updated = true;
            }
        }

        return updated;
    }

    public boolean decayed() {
        return false;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        version = bundle.getInt(VERSION);

        mobs = new HashSet<>();
        heaps = new SparseArray<>();
        blobs = new HashMap<>();
        plants = new SparseArray<>();
        traps = new SparseArray<>();
        customTiles = new HashSet<>();

        map = bundle.getIntArray(MAP);
        warnings.restoreFromBundle(bundle);

        visited = bundle.getBooleanArray(VISITED);
        mapped = bundle.getBooleanArray(MAPPED);

        entrance = bundle.getInt(ENTRANCE);
        exit = bundle.getInt(EXIT);

        entranceAlternate = bundle.getInt(ENTRANCEALTERNATE);
        exitAlternate = bundle.getInt(EXITALTERNATE);

        locked = bundle.getBoolean(LOCKED);
        lightMap = bundle.getIntArray(LIGHTMAP);

        flagsLocal = bundle.getInt(FLAGSLOCAL);
        time = bundle.getLong(TIME);

        weakFloorCreated = false;

        adjustMapSize();

        //for pre-0.3.0c saves
        if (version < 44) {
            map = Terrain.convertTrapsFrom43(map, traps);
        }

        Collection<Bundlable> collection = bundle.getCollection(HEAPS);
        for (Bundlable h : collection) {
            Heap heap = (Heap) h;
            if (resizingNeeded) {
                heap.pos = adjustPos(heap.pos);
            }
            if (heap.size() > 0) {
                heaps.put(heap.pos, heap);
            }
        }

        collection = bundle.getCollection(PLANTS);
        for (Bundlable p : collection) {
            Plant plant = (Plant) p;
            if (resizingNeeded) {
                plant.pos = adjustPos(plant.pos);
            }
            plants.put(plant.pos, plant);
        }

        collection = bundle.getCollection(TRAPS);
        for (Bundlable p : collection) {
            Trap trap = (Trap) p;
            if (resizingNeeded) {
                trap.pos = adjustPos(trap.pos);
            }
            traps.put(trap.pos, trap);
        }

        collection = bundle.getCollection(CUSTOM_TILES);
        for (Bundlable p : collection) {
            CustomTileVisual vis = (CustomTileVisual) p;
            if (resizingNeeded) {
                //TODO: add proper resizing logic here
            }
            customTiles.add(vis);
        }

        //for pre-0.3.1 saves
        if (version < 52) {
            for (int i = 0; i < map.length; i++) {
                if (map[i] == Terrain.INACTIVE_TRAP) {
                    Trap t = new WornTrap().reveal();
                    t.active = false;
                    t.pos = i;
                    traps.put(i, t);
                }
            }
        }

        collection = bundle.getCollection(MOBS);
        for (Bundlable m : collection) {
            Mob mob = (Mob) m;
            if (mob != null) {
                if (resizingNeeded) {
                    mob.pos(adjustPos(mob.pos()));
                }
                mobs.add(mob);
            }
        }

        collection = bundle.getCollection(BLOBS);
        for (Bundlable b : collection) {
            Blob blob = (Blob) b;
            blobs.put(blob.getClass(), blob);
        }

        feeling = bundle.getEnum(FEELING, Feeling.class);
        /*
        if (feeling == Feeling.DARK) {
            viewDistance = (int) Math.ceil(viewDistance / 3f);
        }
        */

        buildFlagMaps();
        updateLightMap();
        cleanWalls();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(VERSION, Game.versionCode);
        bundle.put(MAP, map);
        warnings.storeInBundle(bundle);
        bundle.put(VISITED, visited);
        bundle.put(MAPPED, mapped);
        bundle.put(ENTRANCE, entrance);
        bundle.put(EXIT, exit);
        bundle.put(ENTRANCEALTERNATE, entranceAlternate);
        bundle.put(EXITALTERNATE, exitAlternate);
        bundle.put(LOCKED, locked);
        bundle.put(LIGHTMAP, lightMap);
        bundle.put(HEAPS, heaps.values());
        bundle.put(PLANTS, plants.values());
        bundle.put(TRAPS, traps.values());
        bundle.put(CUSTOM_TILES, customTiles);
        bundle.put(MOBS, mobs);
        bundle.put(BLOBS, blobs.values());
        bundle.put(FEELING, feeling);
        bundle.put(FLAGSLOCAL, flagsLocal);
        bundle.put(TIME, time);
    }

    public int tunnelTile() {
        return feeling == Feeling.CHASM ? Terrain.EMPTY_SP : Terrain.EMPTY;
    }

    private void adjustMapSize() {
        // For levels saved before 1.6.3
        // Seeing as shattered started on 1.7.1 this is never used, but the code may be resused in future.
        if (map.length < LENGTH) {

            resizingNeeded = true;
            loadedMapSize = (int) Math.sqrt(map.length);

            int[] map = new int[LENGTH];
            Arrays.fill(map, Terrain.WALL);

            boolean[] visited = new boolean[LENGTH];
            Arrays.fill(visited, false);

            boolean[] mapped = new boolean[LENGTH];
            Arrays.fill(mapped, false);

            for (int i = 0; i < loadedMapSize; i++) {
                System.arraycopy(this.map, i * loadedMapSize, map, i * WIDTH, loadedMapSize);
                System.arraycopy(this.visited, i * loadedMapSize, visited, i * WIDTH, loadedMapSize);
                System.arraycopy(this.mapped, i * loadedMapSize, mapped, i * WIDTH, loadedMapSize);
            }

            this.map = map;
            this.visited = visited;
            this.mapped = mapped;

            entrance = adjustPos(entrance);
            exit = adjustPos(exit);
        } else {
            resizingNeeded = false;
        }
    }

    public int adjustPos(int pos) {
        return (pos / loadedMapSize) * WIDTH + (pos % loadedMapSize);
    }

    public String tilesTex() {
        return null;
    }

    public String waterTex() {
        return null;
    }

    public String waterUnderTex() {
        return null;
    }

    abstract protected boolean build();

    abstract protected void decorate();

    abstract protected void createMobs();

    abstract protected void createItems();

    public void seal() {
        if (!locked) {
            locked = true;
            Buff.affect(Dungeon.hero, null, LockedFloor.class);
        }
    }

    public void unseal() {
        if (locked) {
            locked = false;
        }
    }

    public Group addVisuals() {
        if (visuals == null || visuals.parent == null) {
            visuals = new Group(-1);
        } else {
            visuals.clear();
        }
        for (int i = 0; i < LENGTH; i++) {
            if (pit[i]) {
                visuals.add(new WindParticle.Wind(i));
                if (i >= WIDTH && puddle[i - WIDTH]) {
                    visuals.add(new FlowParticle.Flow(i - WIDTH));
                }
            }
            if (map[i] == Terrain.WELL || map[i] == Terrain.WELL_MAGIC) {
                visuals.add( new SewerLevel.Sink( i, -2.5f, -6.0f ) );
            }
        }
        return visuals;
    }

    public void removeVisuals(int pos) {
        visuals.removeAt(pos);
    }

    public int nMobs() {
        return 0;
    }

    public Mob findMob(int pos) {
        for (Mob mob : mobs) {
            if (mob.pos() == pos) {
                return mob;
            }
        }
        return null;
    }

    public void spawnMob(final int pos, final Class<? extends Mob> type, final int quantity) {
        Bestiary.spawn(Dungeon.depthAdjusted, Dungeon.hero.level, true, new Bestiary.SpawnParams() {
            @Override
            public Level level() {
                return Level.this;
            }

            @Override
            public int position() {
                return pos;
            }

            @Override
            public Class<?> type(Class<?> _type) {
                return type;
            }

            @Override
            public int quantity(int _quantity) {
                return quantity;
            }

            @Override
            public void initialize(Mob mob) {
                mob.state = mob.WANDERING;
                GameScene.add(mob);
                if (Statistics.amuletObtained) {
                    mob.beckon(Dungeon.hero.pos());
                }
            }
        });
    }

    public void spawnMob() {
        Bestiary.spawn(Dungeon.depthAdjusted, Dungeon.hero.level, true, new Bestiary.SpawnParams() {
            @Override
            public Level level() {
                return Level.this;
            }

            @Override
            public int position() {
                return randomRespawnCell();
            }

            @Override
            public Class<?> type(Class<?> type) {
                return type;
            }

            @Override
            public int quantity(int quantity) {
                return quantity;
            }

            @Override
            public void initialize(Mob mob) {
                mob.state = mob.WANDERING;
                GameScene.add(mob);
                if (Statistics.amuletObtained) {
                    mob.beckon(Dungeon.hero.pos());
                }
            }
        });
    }

    public Actor respawner() {
        return new Actor() {

            {
                actPriority = 1; //as if it were a buff.
            }

            @Override
            protected boolean act() {
                if (mobs.size() < nMobs()) {
                    spawnMob();
                }
                spend_new(Dungeon.level.feeling == Feeling.DARK || Statistics.amuletObtained ? TIME_TO_RESPAWN / 2 : TIME_TO_RESPAWN, false);
                return true;
            }
        };
    }

    public int randomRespawnCell() {
        int cell;
        do {
            cell = Random.Int(LENGTH);
        } while (!passable[cell] || avoid[cell] || Dungeon.visible[cell] || Actor.findChar(cell) != null);
        return cell;
    }

    public int randomDestination() {
        int cell;
        do {
            cell = Random.Int(LENGTH);
        } while (!passable[cell]);
        return cell;
    }

    public int randomStep(int pos) {
        int cell;
        do {
            cell = pos + NEIGHBOURS9[Random.Int(NEIGHBOURS9.length)];
        } while (!passable[cell]);
        return cell;
    }

    public ArrayList<Integer> randomPositionsNear(int pos, int quantity, RandomPositionValidator validator) {
        //todo: this rarely returns a null ArrayList for some reason, fixme
        ArrayList<Integer> positionOffsets = new ArrayList<>();

        for (Integer ofs : Level.NEIGHBOURS9) {
            positionOffsets.add(ofs);
        }

        SparseArray<Object> retval = new SparseArray<>();
        Object alreadyTestedFlag = new Object();

        int repeatoffset = -1;
        int iterationlimit = 16;

        while (iterationlimit > 0) {
            if (repeatoffset >= 0) {
                if (retval.size() <= 0) {
                    return null;
                }

                int failsafe = retval.size() * retval.size();

                Object test = alreadyTestedFlag;
                while (test == alreadyTestedFlag) {
                    int index = Random.Int(retval.size());

                    pos = retval.keyAt(index);
                    test = retval.valueAt(index);

                    failsafe--;
                    if (failsafe <= 0) {
                        return null;
                    }
                }

                retval.put(pos, alreadyTestedFlag);
            }

            Collections.shuffle(positionOffsets);

            for (Integer ofs : positionOffsets) {
                int cell = pos + ofs;

                if (!Level.insideMap(cell)) {
                    continue;
                }

                boolean passable = validator.isValidPosition(cell);

                if (passable) {
                    retval.put(cell, null);

                    if (retval.size() >= quantity) {
                        iterationlimit = 0;
                        break;
                    }
                }
            }

            repeatoffset++;
            iterationlimit--;
        }

        ArrayList<Integer> list = new ArrayList<>();
        for (int n = 0; n < retval.size(); n++) {
            list.add(retval.keyAt(n));
        }
        Collections.shuffle(list);
        return list;
    }

    public interface RandomPositionValidator {
        boolean isValidPosition(int pos);
    }


    public void addItemToSpawn(Item item) {
        if (item != null) {
            itemsToSpawn.add(item);
        }
    }

    public Item findPrizeItem() {
        return findPrizeItem(null);
    }

    public Item findPrizeItem(Class<? extends Item> match) {
        if (itemsToSpawn.size() == 0)
            return null;

        if (match == null) {
            Item item = Random.element(itemsToSpawn);
            itemsToSpawn.remove(item);
            return item;
        }

        for (Item item : itemsToSpawn) {
            if (match.isInstance(item)) {
                itemsToSpawn.remove(item);
                return item;
            }
        }

        return null;
    }

    protected void updateFlagMap(int pos) {
        int flags = Terrain.flags[map[pos]];
        passable[pos] = (flags & Terrain.FLAG_PASSABLE) != 0;
        pathable[pos] = (flags & (Terrain.FLAG_PASSABLE | Terrain.FLAG_PATHABLE)) != 0;
        diagonal[pos] = (flags & Terrain.FLAG_DIAGONALPASSAGE) != 0;
        losBlocking[pos] = (flags & Terrain.FLAG_LOSBLOCKING) != 0;
        burnable[pos] = (flags & Terrain.FLAG_BURNABLE) != 0;
        secret[pos] = (flags & Terrain.FLAG_SECRET) != 0;
        solid[pos] = (flags & Terrain.FLAG_SOLID) != 0;
        avoid[pos] = (flags & Terrain.FLAG_AVOID) != 0;
        puddle[pos] = (flags & Terrain.FLAG_LIQUID) != 0;
        pit[pos] = (flags & Terrain.FLAG_PIT) != 0;
        stone[pos] = (flags & Terrain.FLAG_STONE) != 0;
        lightMap[pos] = (lightMap[pos] & LIGHTMAP_NATURALMASK) | (((flags & Terrain.FLAG_LOSDARK) != 0) ? LIGHTMAP_NATURALDARK : LIGHTMAP_NATURALLIT);
    }

    protected void updateLightMap(int pos) {
        boolean lit = (lightMap[pos] & LIGHTMAP_MOBILEMASK) == LIGHTMAP_NATURALLIT;
        lit |= (lightMap[pos] & LIGHTMAP_ARTIFICIALLIT) != 0;
        lit |= (lightMap[pos] & LIGHTMAP_MOBILELIT) != 0;
        losDark[pos] = !lit;
    }

    public void updateLightMap() {
        GLog.d("updatelightmap");
        for (int n = 0; n < LENGTH; n++) {
            updateLightMap(n);
        }
    }

    protected void buildFlagMaps() {
        flags = flagsLocal;

        for (int i = 0; i < LENGTH; i++) {
            updateFlagMap(i);
        }

        int lastRow = LENGTH - WIDTH;
        for (int i = 0; i < WIDTH; i++) {
            passable[i] = avoid[i] = false;
            passable[lastRow + i] = avoid[lastRow + i] = false;
        }
        for (int i = WIDTH; i < lastRow; i += WIDTH) {
            passable[i] = avoid[i] = false;
            passable[i + WIDTH - 1] = avoid[i + WIDTH - 1] = false;
        }

        for (int i = WIDTH; i < LENGTH - WIDTH; i++) {
            /*
            if (water[i]) {
                int t = Terrain.WATER_TILES;
                for (int j = 0; j < NEIGHBOURS4.length; j++) {
                    if ((Terrain.flags[map[i + NEIGHBOURS4[j]]] & Terrain.FLAG_UNSTITCHABLE) != 0) {
                        t += 1 << j;
                    }
                }
                map[i] = t;
            }
            */
            if (map[i] >= Terrain.FACED_TILE_MIN && map[i] <= Terrain.FACED_TILE_MAX) {
                repair(i, false, true);
            }

            if (pit[i] && map[i] < Terrain.FACED_TILE_MIN) {
                if (!pit[i - WIDTH]) {
                    int c = map[i - WIDTH];
                    if (c == Terrain.EMPTY_SP || c == Terrain.STATUE_SP) {
                        map[i] = Terrain.CHASM_FLOOR_SP;
                    } else if (puddle[i - WIDTH]) {
                        map[i] = Terrain.CHASM_WATER;
                    } else if ((Terrain.flags[c] & Terrain.FLAG_UNSTITCHABLE) != 0) {
                        map[i] = Terrain.CHASM_WALL;
                    } else {
                        map[i] = Terrain.CHASM_FLOOR;
                    }
                }
            }
        }
    }

    protected void stoneInside() {
        int index = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int walls = 0;

                int i = index - WIDTH - 1;

                if (y <= 0) {
                    walls += 3;
                    i += 2;
                } else {
                    if (x <= 0 || map[i] == Terrain.WALL || map[i] == Terrain.WALL_STONE) walls++;
                    i++;
                    if (map[i] == Terrain.WALL || map[i] == Terrain.WALL_STONE) walls++;
                    i++;
                    if (x >= WIDTH - 1 || map[i] == Terrain.WALL || map[i] == Terrain.WALL_STONE)
                        walls++;
                }

                i += WIDTH - 2;

                if (x <= 0 || map[i] == Terrain.WALL || map[i] == Terrain.WALL_STONE) walls++;

                i++;
                //if (map[index] == Terrain.WALL) walls++;

                i++;
                if (x >= WIDTH - 1 || map[i] == Terrain.WALL || map[i] == Terrain.WALL_STONE)
                    walls++;


                i += WIDTH - 2;
                if (y >= HEIGHT - 1) {
                    walls += 3;
                } else {
                    if (x <= 0 || map[i] == Terrain.WALL || map[i] == Terrain.WALL_STONE) walls++;
                    i++;
                    if (map[i] == Terrain.WALL || map[i] == Terrain.WALL_STONE) walls++;
                    i++;
                    if (x >= WIDTH - 1 || map[i] == Terrain.WALL || map[i] == Terrain.WALL_STONE)
                        walls++;
                }

                if (walls == 8) {
                    map[index] = Terrain.WALL_STONE;
                    discoverable[index] = false;

                    spawnGemstones(index);
                }

                index++;
            }
        }
    }

    public void spawnGemstones(int pos) {
        while (Random.Float() < 0.04f) {
            Gemstone gemstone = Generator.randomGemstone();
            if (gemstone != null) {
                drop(gemstone, pos);
            }
        }
    }

    protected void cleanWalls() {
        for (int i = 0; i < LENGTH; i++) {

            boolean d = false;

            for (int j = 0; j < NEIGHBOURS9.length; j++) {
                int n = i + NEIGHBOURS9[j];
                if (n >= 0 && n < LENGTH && (Terrain.flags[map[n]] & Terrain.FLAG_UNDISCOVERABLE) == 0) {
                    d = true;
                    break;
                }
            }

            if (d) {
                d = false;

                for (int j = 0; j < NEIGHBOURS9.length; j++) {
                    int n = i + NEIGHBOURS9[j];
                    if (n >= 0 && n < LENGTH && !pit[n]) {
                        d = true;
                        break;
                    }
                }
            }

            discoverable[i] = d;
        }
    }


    public void set(int cell, int terrain, boolean flagUpdates) {
        Painter.set(this, cell, terrain);

        if (terrain != Terrain.TRAP && terrain != Terrain.SECRET_TRAP && terrain != Terrain.INACTIVE_TRAP) {
            traps.remove(cell);
        }

        if (flagUpdates) {
            updateFlagMap(cell);
            updateLightMap(cell);
        }
    }

    public void repair(int cell, boolean mapUpdates, boolean flagUpdates) {
        int terrain = map[cell];
        int cooperativeTerrain = cooperativeCell(terrain);

        int terrainType = (terrain / Terrain.FACED_TILE_BLOCKSIZE) * Terrain.FACED_TILE_BLOCKSIZE;
        terrain = terrainType + Terrain.FACED_TILE_BLOCKSIZE - 1;

        int index;
        int test;
        int placedFlag = 1;
        boolean good;

        int placedTerrain = terrainType;
        List<Integer> updates = new ArrayList<Integer>();

        for (int n = 0; n < NEIGHBOURS4.length; n++) {
            good = false;
            index = cell + NEIGHBOURS4[n];
            test = map[index];
            if (test >= terrainType && test <= terrain) {
                good = true;
                updates.add(index);
            } else if (test == cooperativeTerrain) {
                good = true;
            }
            if (good) {
                placedTerrain += placedFlag;
            }
            placedFlag <<= 1;
        }

        test = map[cell];
        if (test == placedTerrain) {
            return;
        }

        set(cell, placedTerrain, flagUpdates);
        if (mapUpdates) {
            GameScene.updateMap(cell);
        }
    }

    private void setRepairing(int cell, int terrain, boolean mapUpdates, boolean flagUpdates) {
        int test = map[cell];

        if (test == terrain) {
            return;
        }

        set(cell, terrain, flagUpdates);
        if (mapUpdates) {
            GameScene.updateMap(cell);
        }

        determineSpecialFixes(cell, terrain, terrain);
    }

    private static int cooperativeCell(int cellType) {
        if (cellType >= Terrain.DIRT_TILES && cellType <= Terrain.DIRT)
        {
            return Terrain.WALL_STONE;
        }
        else if (cellType >= Terrain.PUDDLE_TILES && cellType <= Terrain.PUDDLE)
        {
            return Terrain.WALL;
        }

        return 0;
    }

    public void setEmpty(int cell, boolean mapUpdates, boolean flagUpdates) {
        setSetSpecialRecursiveInitialize();
        setRepairing(cell, Terrain.EMPTY, mapUpdates, flagUpdates);
        setSpecialFixes(mapUpdates, flagUpdates);
    }

    public void setWoodDebris(int cell, boolean mapUpdates, boolean flagUpdates) {
        setSetSpecialRecursiveInitialize();
        setRepairing(cell, Terrain.WOOD_DEBRIS, mapUpdates, flagUpdates);
        setSpecialFixes(mapUpdates, flagUpdates);
    }

    public void setDirt(int cell, boolean mapUpdates, boolean flagUpdates) {
        setSetSpecialRecursiveInitialize();
        setSpecialRecursive(cell, Terrain.DIRT, cooperativeCell(Terrain.DIRT), mapUpdates, flagUpdates);
        setSpecialFixes(mapUpdates, flagUpdates);

		/*
		//nope, too complicated / annoying / wasteful, who cares
		if (setSpecialRecursive(cell, Terrain.DIRT, Terrain.FLAG_STONE, Terrain.FLAG_LIQUID)) {
			fillWater(cell);
		}
		*/
    }

    public void setDirtDeep(int cell, boolean mapUpdates, boolean flagUpdates) {
        setSetSpecialRecursiveInitialize();
        setSpecialRecursive(cell, Terrain.DIRT_DEEP, cooperativeCell(Terrain.DIRT_DEEP), mapUpdates, flagUpdates);
        setSpecialFixes(mapUpdates, flagUpdates);
    }

    private static HashSet<Integer> setSpecialRecursive = new HashSet<>();
    private static HashSet<Integer> setSpecialFixes = new HashSet<>();

    private static void setSetSpecialRecursiveInitialize() {
        setSpecialRecursive.clear();
        setSpecialFixes.clear();
    }

    private void determineSpecialFixes(int cell, int terrainMin, int terrainMax) {
        int test;
        for (int n = 0; n < NEIGHBOURS4.length; n++) {
            int subcell = cell + NEIGHBOURS4[n];

            test = map[subcell];
            if (test >= Terrain.FACED_TILE_MIN && test <= Terrain.FACED_TILE_MAX) {
                //GLog.d(cell, subcell);
                if (test >= terrainMin && test <= terrainMax) {
                    //GLog.d("ignored");
                } else {
                    setSpecialFixes.add(subcell);
                }
            }
        }
    }

    private void setSpecialFixes(boolean mapUpdates, boolean flagUpdates) {
        for (Integer cell : setSpecialFixes.toArray(new Integer[]{})) {
            int test = map[cell];
            //GLog.d("try fix cell="+cell);
            setSpecialRecursive(cell, test, cooperativeCell(test), mapUpdates, flagUpdates);
        }
    }

    private void setSpecialRecursive(int cell, int terrain, int cooperativeTerrain, boolean mapUpdates, boolean flagUpdates) {
        setSpecialRecursive.add(cell);

        int terrainType = (terrain / Terrain.FACED_TILE_BLOCKSIZE) * Terrain.FACED_TILE_BLOCKSIZE;
        terrain = terrainType + Terrain.FACED_TILE_BLOCKSIZE - 1;

        int index;
        int test;
        int placedFlag = 1;
        boolean good;

        int placedTerrain = terrainType;
        List<Integer> updates = new ArrayList<Integer>();

        for (int n = 0; n < NEIGHBOURS4.length; n++) {
            good = false;
            index = cell + NEIGHBOURS4[n];
            test = map[index];
            if (test >= terrainType && test <= terrain) {
                good = true;
                updates.add(index);
            } else if (test == cooperativeTerrain) {
                good = true;
            }
            if (good) {
                placedTerrain += placedFlag;
            }
            placedFlag <<= 1;
        }

        test = map[cell];
        if (test == placedTerrain) {
            return;
        }

        set(cell, placedTerrain, flagUpdates);
        if (mapUpdates) {
            GameScene.updateMap(cell);
        }

        determineSpecialFixes(cell, terrainType, terrain);

        for (int n = 0; n < updates.size(); n++) {
            int newCell = updates.get(n);
            if (setSpecialRecursive.contains(newCell)) {
                continue;
            }

            setSpecialRecursive(newCell, terrain, cooperativeTerrain, mapUpdates, flagUpdates);
        }

    }

	/*

	//nope, too complicated / annoying / wasteful

	private static boolean setSpecialRecursive(int cell, int terrain, int cooperativeFlags, int notificationFlags) {
		setSpecialRecursive.add(cell);
		boolean retval = false;

		int terrainType = terrain - 15;

		int index;
		int test;
		boolean good;

		int placedTerrain = terrainType;
		List<Integer> updates = new ArrayList<Integer>();

		good = false;
		index = cell - WIDTH;
		test = Dungeon.level.map[index];
		if (test >= terrainType && test <= terrain) {
			good = true;
			updates.add(index);
		}
		else if ((Terrain.flags[test] & cooperativeFlags) != 0) {
			good = true;
		}
		if (good) {
			placedTerrain++;
		}
		if ((Terrain.flags[test] & notificationFlags) != 0) {
			retval = true;
		}

		good = false;
		index += WIDTH + 1;
		test = Dungeon.level.map[index];
		if (test >= terrainType && test <= terrain) {
			good = true;
			updates.add(index);
		}
		else if ((Terrain.flags[test] & cooperativeFlags) != 0) {
			good = true;
		}
		if (good) {
			placedTerrain += 2;
		}
		if ((Terrain.flags[test] & notificationFlags) != 0) {
			retval = true;
		}

		good = false;
		index += WIDTH - 1;
		test = Dungeon.level.map[index];
		if (test >= terrainType && test <= terrain) {
			good = true;
			updates.add(index);
		}
		else if ((Terrain.flags[test] & cooperativeFlags) != 0) {
			good = true;
		}
		if (good) {
			placedTerrain += 4;
		}
		if ((Terrain.flags[test] & notificationFlags) != 0) {
			retval = true;
		}

		good = false;
		index -= WIDTH + 1;
		test = Dungeon.level.map[index];
		if (test >= terrainType && test <= terrain) {
			good = true;
			updates.add(index);
		}
		else if ((Terrain.flags[test] & cooperativeFlags) != 0) {
			good = true;
		}
		if (good) {
			placedTerrain += 8;
		}
		if ((Terrain.flags[test] & notificationFlags) != 0) {
			retval = true;
		}

		test = Dungeon.level.map[cell];
		if (test == placedTerrain) {
			return;
		}

		set(cell, placedTerrain);

		for (int n=0;n<updates.size();n++) {
			int newCell = updates.get(n);
			if (setSpecialRecursive.contains(newCell)) {
				continue;
			}

			setSpecialRecursive(newCell, terrain, cooperativeFlags);

			retval |= setSpecialRecursive(newCell, terrain, cooperativeFlags, notificationFlags);
		}

		return retval;
	}

	private static void fillWater(int cell) {
		setSpecialRecursive.clear();

		fillWaterRecursive(cell);
		setSpecialRecursive(cell, Terrain.WATER, Terrain.FLAG_STONE, 0);
	}

	private static void fillWaterRecursive(int cell) {
		setSpecialRecursive.add(cell);
		int test = Dungeon.level.map[cell];

		if (test >= Terrain.DIRT_TILES && test <= Terrain.DIRT) {
			set(cell, test - Terrain.DIRT_TILES + Terrain.WATER_TILES);
		}
		else {
			return;
		}

		int index = cell - WIDTH;

		if (!setSpecialRecursive.contains(index)) {
			fillWaterRecursive(index);
		}

		index += WIDTH + 1;

		if (!setSpecialRecursive.contains(index)) {
			fillWaterRecursive(index);
		}

		index += WIDTH - 1;

		if (!setSpecialRecursive.contains(index)) {
			fillWaterRecursive(index);
		}

		index -= WIDTH + 1;

		if (!setSpecialRecursive.contains(index)) {
			fillWaterRecursive(index);
		}

	}
	*/

    public Heap drop(Item item, int cell) {

        //This messy if statement deals will items which should not drop in challenges primarily.
        if ((Dungeon.isChallenged(Challenges.NO_FOOD) && (item instanceof Food || item instanceof BlandfruitBush.Seed)) ||
                (Dungeon.isChallenged(Challenges.NO_ARMOR) && item instanceof Armor) ||
                (Dungeon.isChallenged(Challenges.NO_HEALING) && item instanceof PotionOfHealing) ||
                (Dungeon.isChallenged(Challenges.NO_HERBALISM) && (item instanceof Plant.Seed || item instanceof Dewdrop || item instanceof SeedPouch)) ||
                (Dungeon.isChallenged(Challenges.NO_SCROLLS) && ((item instanceof Scroll && !(item instanceof ScrollOfUpgrade)) || item instanceof ScrollHolder)) ||
                item == null) {

            //create a dummy heap, give it a dummy sprite, don't add it to the game, and return it.
            //effectively nullifies whatever the logic calling this wants to do, including dropping items.
            Heap heap = new Heap();
            ItemSprite sprite = heap.sprite = new ItemSprite();
            sprite.link(heap);
            return heap;
        }

        if ((map[cell] == Terrain.ALCHEMY) && (
                !(item instanceof Plant.Seed || item instanceof Blandfruit) ||
                        item instanceof BlandfruitBush.Seed ||
                        (item instanceof Blandfruit && (((Blandfruit) item).potionAttrib != null || heaps.get(cell) != null)) ||
                        Dungeon.hero.buff(AlchemistsToolkit.alchemy.class) != null && Dungeon.hero.buff(AlchemistsToolkit.alchemy.class).isCursed())) {
            int n;
            do {
                n = cell + NEIGHBOURS8[Random.Int(8)];
            } while (map[n] != Terrain.EMPTY_SP);
            cell = n;
        }

        Heap heap = heaps.get(cell);
        if (heap == null) {
            heap = new Heap();
            heap.seen = Dungeon.visible[cell];
            heap.pos = cell;
            if (map[cell] == Terrain.CHASM || (Dungeon.level != null && pit[cell])) {
                Dungeon.dropToChasm(item);
                GameScene.discard(heap);
            } else {
                heaps.put(cell, heap);
                GameScene.add(heap);
            }

        }

        heap.collect(item);

        if (Dungeon.level != null) {
            press(cell, null);
        }

        return heap;
    }

    public Plant plant(Char thrower, Plant.Seed seed, int pos) {

        Plant plant = plants.get(pos);
        if (plant != null) {
            plant.wither();
        }

        if (map[pos] == Terrain.HIGH_GRASS ||
                map[pos] == Terrain.EMPTY ||
                map[pos] == Terrain.EMBERS ||
                map[pos] == Terrain.EMPTY_DECO) {
            map[pos] = Terrain.GRASS;
            burnable[pos] = true;
            GameScene.updateMap(pos);
        }

        plant = seed.couch(pos);
        plants.put(pos, plant);

        GameScene.add(plant);

        return plant;
    }

    public void uproot(int pos) {
        plants.remove(pos);
    }

    public Trap setTrap(Char setter, Trap trap, int pos) {
        Trap existingTrap = traps.get(pos);
        if (existingTrap != null) {
            traps.remove(pos);
            if (existingTrap.sprite != null) existingTrap.sprite.kill();
        }
        trap.set(setter, pos);
        traps.put(pos, trap);
        GameScene.add(trap);
        return trap;
    }

    public void disarmTrap(int pos) {
        set(pos, Terrain.INACTIVE_TRAP, true);
        GameScene.updateMap(pos);
    }

    public void discover(int cell) {
        discover(cell, true, true);
    }

    public void discover(int cell, boolean terrain, boolean traps) {
        if (terrain) {
            set(cell, Terrain.discover(map[cell]), true);
        }
        if (traps) {
            Trap trap = this.traps.get(cell);
            if (trap != null) {
                trap.reveal();
            }
        }
        GameScene.updateMap(cell);
    }

    public int pitCell() {
        return randomRespawnCell();
    }

    public void press(int cell, Char ch) {
        if (ch != null && pit[cell] && !ch.flying()) {
            if (ch == Dungeon.hero) {
                Chasm.heroFall(cell);
            } else if (ch instanceof Mob) {
                Chasm.mobFall((Mob) ch);
            }
            return;
        }

        TimekeepersHourglass.timeFreeze timeFreeze = null;

        if (ch != null) {
            timeFreeze = ch.buff(TimekeepersHourglass.timeFreeze.class);
        }

        boolean frozen = timeFreeze != null;

        Trap trap = null;

        switch (map[cell]) {
            case Terrain.SECRET_TRAP:
                GLog.i(TXT_HIDDEN_PLATE_CLICKS);
            case Terrain.TRAP:
                trap = traps.get(cell);
                break;
            case Terrain.HIGH_GRASS:
                HighGrass.trample(this, cell, ch);
                break;
            /*
            case Terrain.WELL:
                WellWater.affectCell(cell);
                break;
            */
            case Terrain.ALCHEMY:
                if (ch == null) {
                    Alchemy.transmute(cell);
                }
                break;
			/*
			case Terrain.DOOR:
				Door.enter( cell );
				break;
			*/
        }

        if (trap != null && !frozen) {

            if (ch == Dungeon.hero) {
                Dungeon.hero.interrupt();
            }

            trap.trigger();

        } else if (trap != null && frozen) {

            Sample.INSTANCE.play(Assets.SND_TRAP);

            discover(cell);

            timeFreeze.setDelayedPress(cell);

        }

        Plant plant = plants.get(cell);
        if (plant != null) {
            plant.trigger();
        }
    }

    public void mobPress(Mob mob) {

        int cell = mob.pos();

        if (pit[cell] && !mob.flying()) {
            Chasm.mobFall(mob);
            return;
        }

        Trap trap = null;
        switch (map[cell]) {

            case Terrain.TRAP:
                trap = traps.get(cell);
                break;
            /*
            //needs to be in AI
            case Terrain.DOOR:
                if (mob.canOpenDoors) {
                    Door.open(cell);

                    if (Random.Int(3) == 0) {
                        GLog.i("You feel an unexpected draft.");
                    }
                }
                break;
            */
        }

        if (!mob.flying()) {
            if (trap != null) {
                trap.trigger();
            }

            Plant plant = plants.get(cell);
            if (plant != null) {
                plant.trigger();
            }
        }
    }

    public void updateFieldOfSenses(Char c) {
        int cx = c.pos() % WIDTH;
        int cy = c.pos() / WIDTH;

        boolean hasVision = c.viewDistance > 0 && c.buff(Blindness.class) == null && c.buff(Shadows.class) == null
                && c.buff(TimekeepersHourglass.timeStasis.class) == null && c.isAlive();
        boolean hasHearing = c.hearDistance > 0;
        boolean hasFeeling = c.touchDistance > 0;

        if (c.viewDistance == c.hearDistance) {
            if (hasVision) {
                ShadowCaster.castShadow(cx, cy, fieldOfView, c.viewDistance, c.touchDistance);

                if (hasHearing) {
                    System.arraycopy(fieldOfView, 0, fieldOfSound, 0, fieldOfView.length);
                } else {
                    Arrays.fill(fieldOfSound, false);
                }
            } else if (hasHearing) {
                Arrays.fill(fieldOfView, false);
                ShadowCaster.castShadow(cx, cy, fieldOfSound, c.viewDistance, c.touchDistance);
            }
        } else {
            if (hasVision) {
                ShadowCaster.castShadow(cx, cy, fieldOfView, c.viewDistance, c.touchDistance);
            } else {
                Arrays.fill(fieldOfView, false);
            }
            if (hasHearing) {
                ShadowCaster.castShadow(cx, cy, fieldOfSound, c.hearDistance, c.touchDistance);
            } else {
                Arrays.fill(fieldOfSound, false);
            }
        }

        if (hasFeeling) {
            switch(c.touchDistance) {
                case 1:
                    Arrays.fill(fieldOfTouch, false);
                    for (int offset : NEIGHBOURS9) {
                        fieldOfTouch[c.pos() + offset] = true;
                    }
                    break;
                case 0:
                    Arrays.fill(fieldOfTouch, false);
                    fieldOfTouch[c.pos()] = true;
                    break;
                default:
                    ShadowCaster.castShadow(cx, cy, fieldOfTouch, c.touchDistance, c.touchDistance);
                    break;
            }
        } else {
            Arrays.fill(fieldOfTouch, false);
        }

        /*
        int sense = 1;
        if (c.isAlive()) {
            for (Buff b : c.buffs(MindVision.class)) {
                sense = Math.max(((MindVision) b).distance, sense);
            }
        }

        //todo: keep through-wall sensing?
        if ((hasVision && sense > 1)) {
            int ax = Math.max(0, cx - sense);
            int bx = Math.min(cx + sense, WIDTH - 1);
            int ay = Math.max(0, cy - sense);
            int by = Math.min(cy + sense, HEIGHT - 1);

            int len = bx - ax + 1;
            int pos = ax + ay * WIDTH;
            for (int y = ay; y <= by; y++, pos += WIDTH) {
                Arrays.fill(fieldOfTouch, pos, pos + len, true);
            }

            for (int i = 0; i < LENGTH; i++) {
                fieldOfTouch[i] &= discoverable[i];
            }
        }
        */

        if (c.isAlive()) {
            boolean creatureVision = c.buff(MindVision.class) != null;
            boolean itemVision = c.buff(ItemVision.class) != null;

            if (creatureVision) {
                for (Mob mob : mobs) {
                    if ((mob.characteristics & Characteristic.Brainless.value) == 0) {
                        int p = mob.pos();
                        fieldOfView[p] = true;
                        fieldOfTouch[p] = true;
                        fieldOfTouch[p + 1] = true;
                        fieldOfTouch[p - 1] = true;
                        fieldOfTouch[p + WIDTH + 1] = true;
                        fieldOfTouch[p + WIDTH - 1] = true;
                        fieldOfTouch[p - WIDTH + 1] = true;
                        fieldOfTouch[p - WIDTH - 1] = true;
                        fieldOfTouch[p + WIDTH] = true;
                        fieldOfTouch[p - WIDTH] = true;
                        /*
                        fieldOfView[p] = true;
                        fieldOfView[p + 1] = true;
                        fieldOfView[p - 1] = true;
                        fieldOfView[p + WIDTH + 1] = true;
                        fieldOfView[p + WIDTH - 1] = true;
                        fieldOfView[p - WIDTH + 1] = true;
                        fieldOfView[p - WIDTH - 1] = true;
                        fieldOfView[p + WIDTH] = true;
                        fieldOfView[p - WIDTH] = true;
                        */
                    }
                }
            }
            /*
            else if (c == Dungeon.hero && ((Hero) c).heroClass == HeroClass.HUNTRESS) {
                for (Mob mob : mobs) {
                    int p = mob.pos;
                    if (distance(c.pos, p) == 2) {
                        fieldOfView[p] = true;
                        fieldOfView[p + 1] = true;
                        fieldOfView[p - 1] = true;
                        fieldOfView[p + WIDTH + 1] = true;
                        fieldOfView[p + WIDTH - 1] = true;
                        fieldOfView[p - WIDTH + 1] = true;
                        fieldOfView[p - WIDTH - 1] = true;
                        fieldOfView[p + WIDTH] = true;
                        fieldOfView[p - WIDTH] = true;
                    }
                }
            }
            */

            if (itemVision) {
                for (Heap heap : heaps.values()) {
                    int p = heap.pos;
                    fieldOfView[p] = true;
                    fieldOfTouch[p] = true;
                    fieldOfTouch[p + 1] = true;
                    fieldOfTouch[p - 1] = true;
                    fieldOfTouch[p + WIDTH + 1] = true;
                    fieldOfTouch[p + WIDTH - 1] = true;
                    fieldOfTouch[p - WIDTH + 1] = true;
                    fieldOfTouch[p - WIDTH - 1] = true;
                    fieldOfTouch[p + WIDTH] = true;
                    fieldOfTouch[p - WIDTH] = true;
                    /*
                    fieldOfView[p] = true;
                    fieldOfView[p + 1] = true;
                    fieldOfView[p - 1] = true;
                    fieldOfView[p + WIDTH + 1] = true;
                    fieldOfView[p + WIDTH - 1] = true;
                    fieldOfView[p - WIDTH + 1] = true;
                    fieldOfView[p - WIDTH - 1] = true;
                    fieldOfView[p + WIDTH] = true;
                    fieldOfView[p - WIDTH] = true;
                    */
                }
            }
        }

        if (c == Dungeon.hero) {
            for (Heap heap : heaps.values()) {
                if (!heap.seen) {
                    if (fieldOfView[heap.pos]) {
                        heap.seen = true;
                    }
                }
            }

            Heap test = heaps.get(c.pos());
            if (test != null) {
                if (!test.seen) {
                    test.seen = true;
                    GLog.i("You feel something on the ground here.");
                }
            }
        }
    }

    public static int distance(int a, int b) {
        int ax = a % WIDTH;
        int ay = a / WIDTH;
        int bx = b % WIDTH;
        int by = b / WIDTH;
        return Math.max(Math.abs(ax - bx), Math.abs(ay - by));
    }

    public static int distance(int x1, int y1, int x2, int y2) {
        x1 -= x2;
        y1 -= y2;

        return (int)Math.sqrt(x1*x1+y1*y1);
    }

    public static boolean canReach(int a, int b) {
        int diff = Math.abs(a - b);
        return diff == 1 || diff == WIDTH || diff == WIDTH + 1 || diff == WIDTH - 1;
    }

    public static boolean canStep(int a, int b, boolean[] diagonal) {
        boolean aDiagonal = diagonal[a];
        boolean bDiagonal = diagonal[b];

        int diff = Math.abs(a - b);
        return diff == 1 || diff == WIDTH || (aDiagonal && bDiagonal && (diff == WIDTH + 1 || diff == WIDTH - 1));
    }

    //returns true if the input is a valid tile within the level
    public static boolean insideMap(int tile) {
        //outside map array
        //WHY THE FLYING FUCK WAS WIDTH-1 HARDCODED?
        return !((tile <= -1 || tile >= LENGTH) ||
                //top and bottom row
                (tile <= WIDTH - 1 || tile >= LENGTH - WIDTH) ||
                //left and right column
                (tile % WIDTH == 0 || tile % WIDTH == WIDTH - 1));
    }

    public String tileName(int tile) {

        if (tile >= Terrain.PUDDLE_TILES && tile <= Terrain.PUDDLE) {
            return tileName(Terrain.PUDDLE);
        }

        if (tile >= Terrain.DIRT_TILES && tile <= Terrain.DIRT) {
            return "Dirt";
        }

        if (tile != Terrain.CHASM && (Terrain.flags[tile] & Terrain.FLAG_PIT) != 0) {
            return tileName(Terrain.CHASM);
        }

        switch (tile) {
            case Terrain.CHASM:
                return "Chasm";
            case Terrain.EMPTY:
            case Terrain.EMPTY_SP:
            case Terrain.EMPTY_DECO:
            case Terrain.SECRET_TRAP:
                return "Floor";
            case Terrain.GRASS:
                return "Grass";
            case Terrain.PUDDLE:
                return "Water Puddle";
            case Terrain.WALL:
            case Terrain.WALL_DECO:
            case Terrain.SECRET_DOOR:
            case Terrain.SECRET_LOCKED_DOOR:
                return "Wall";
            case Terrain.DOOR:
                return "Closed door";
            case Terrain.OPEN_DOOR:
                return "Open door";
            case Terrain.STAIRS_UP:
            case Terrain.STAIRS_UP_ALTERNATE:
                return "Upward staircase";
            case Terrain.STAIRS_DOWN:
            case Terrain.STAIRS_DOWN_ALTERNATE:
                return "Downward staircase";
            case Terrain.EMBERS:
                return "Embers";
            case Terrain.LOCKED_DOOR:
                return "Locked door";
            case Terrain.PEDESTAL:
                return "Pedestal";
            case Terrain.BARRICADE:
                return "Barricade";
            case Terrain.HIGH_GRASS:
                return "High grass";
            case Terrain.LOCKED_EXIT:
                return "Locked depth exit";
            case Terrain.UNLOCKED_EXIT:
                return "Unlocked depth exit";
            case Terrain.SIGN:
                return "Sign";
            case Terrain.WELL:
                return "Well";
            case Terrain.EMPTY_WELL:
                return "Empty well";
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return "Statue";
            case Terrain.INACTIVE_TRAP:
                return "Triggered trap";
            case Terrain.ALCHEMY:
                return "Brewing Station";
            case Terrain.DENATURED_DEBRIS:
                return "Denatured Debris";
            case Terrain.WOOD_DEBRIS:
                return "Wooden Debris";
            case Terrain.WALL_STONE:
                return "Stone Wall";
            case Terrain.ALCHEMY_EMPTY:
                return "Empty Brewing Station";
            case Terrain.ALTAR:
                return "Altar";
            default:
                return "???";
        }
    }

    public String tileDesc(int tile) {

        switch (tile) {
            case Terrain.CHASM:
                return "You can't see the bottom.";
            case Terrain.PUDDLE:
                return "In case of being on fire, you can bring yourself to a non-on-fire state instantly here.";
            case Terrain.STAIRS_UP:
            case Terrain.STAIRS_UP_ALTERNATE:
                return "Stairs lead up to the upper depth.";
            case Terrain.STAIRS_DOWN:
            case Terrain.UNLOCKED_EXIT:
            case Terrain.STAIRS_DOWN_ALTERNATE:
                return "Stairs lead down to the lower depth.";
            case Terrain.EMBERS:
                return "Embers cover the floor.";
            case Terrain.HIGH_GRASS:
                return "Dense vegetation blocks the view.";
            case Terrain.LOCKED_DOOR:
                return "This door is locked.  Try kicking it down?";
            case Terrain.LOCKED_EXIT:
                return "Heavy bars block the stairs leading down.";
            case Terrain.BARRICADE:
                return "The wooden barricade is firmly set but has dried over the years.  Might it burn?";
            case Terrain.SIGN:
                return "You can't read the text from here.";
            case Terrain.INACTIVE_TRAP:
                return "The trap has been triggered before and it's not dangerous anymore.";
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return "Someone wanted to adorn this place, but failed, obviously.";
            case Terrain.ALCHEMY:
                return "Drop some seeds here to brew a potion.";
            case Terrain.ALCHEMY_EMPTY:
                return "The brewing equipment here has been used up.";
            case Terrain.EMPTY_WELL:
                return "The well has run dry.";
            case Terrain.DENATURED_DEBRIS:
                return "Unrecognizable debris covers the floor.";
            case Terrain.WOOD_DEBRIS:
                return "Wooden debris covers the floor.";
            case Terrain.ALTAR:
                return "Drop items here to determine their blessed or cursed status.";
            default:
                if (tile >= Terrain.PUDDLE_TILES && tile <= Terrain.PUDDLE) {
                    return tileDesc(Terrain.PUDDLE);
                }
                if ((Terrain.flags[tile] & Terrain.FLAG_PIT) != 0) {
                    return tileDesc(Terrain.CHASM);
                }
                return "";
        }
    }
}
