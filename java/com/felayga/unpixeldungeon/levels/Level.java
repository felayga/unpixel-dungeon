/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */
package com.felayga.unpixeldungeon.levels;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Challenges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Alchemy;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.WellWater;
import com.felayga.unpixeldungeon.actors.buffs.Awareness;
import com.felayga.unpixeldungeon.actors.buffs.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.LockedFloor;
import com.felayga.unpixeldungeon.actors.buffs.MindVision;
import com.felayga.unpixeldungeon.actors.buffs.Shadows;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.particles.FlowParticle;
import com.felayga.unpixeldungeon.effects.particles.WindParticle;
import com.felayga.unpixeldungeon.items.Dewdrop;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.Stylus;
import com.felayga.unpixeldungeon.items.Torch;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.artifacts.AlchemistsToolkit;
//import com.felayga.unpixeldungeon.items.artifacts.DriedRose;
import com.felayga.unpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.felayga.unpixeldungeon.items.bags.ScrollHolder;
import com.felayga.unpixeldungeon.items.bags.SeedPouch;
import com.felayga.unpixeldungeon.items.food.Blandfruit;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.items.potions.PotionOfMight;
import com.felayga.unpixeldungeon.items.potions.PotionOfStrength;
import com.felayga.unpixeldungeon.items.rings.RingOfWealth;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.felayga.unpixeldungeon.levels.features.Chasm;
import com.felayga.unpixeldungeon.levels.features.Door;
import com.felayga.unpixeldungeon.levels.features.HighGrass;
import com.felayga.unpixeldungeon.levels.painters.Painter;
import com.felayga.unpixeldungeon.levels.traps.*;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.mechanics.ShadowCaster;
import com.felayga.unpixeldungeon.plants.BlandfruitBush;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.ui.CustomTileVisual;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class Level implements Bundlable, IDecayable {

	public static enum Feeling {
		NONE,
		CHASM,
		WATER,
		GRASS,
		DARK
	}

	//what the flying fuck?  pathing algorithm for thrown objects fails with certain sizes?
	public static final int WIDTH = 79; //79
	public static final int HEIGHT = 21; //21
	public static final int LENGTH = WIDTH * HEIGHT;

	public static final int[] NEIGHBOURS4 = {-WIDTH, +1, +WIDTH, -1};
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

	public int viewDistance = Dungeon.isChallenged(Challenges.DARKNESS) ? 3 : 8;

	public static boolean[] fieldOfView = new boolean[LENGTH];

	public static boolean[] passable = new boolean[LENGTH];
	public static boolean[] pathable = new boolean[LENGTH];
	public static boolean[] losBlocking = new boolean[LENGTH];
	public static boolean[] secret = new boolean[LENGTH];
	public static boolean[] solid = new boolean[LENGTH];
	public static boolean[] avoid = new boolean[LENGTH];
	public static boolean[] water = new boolean[LENGTH];
	public static boolean[] pit = new boolean[LENGTH];
	public static boolean[] wood = new boolean[LENGTH];
	public static boolean[] stone = new boolean[LENGTH];

	public static boolean[] discoverable = new boolean[LENGTH];

	protected static boolean areaDiggable;

	public Feeling feeling = Feeling.NONE;

	public int entrance;
	public int exit;

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
	private static final String LOCKED = "locked";
	private static final String HEAPS = "heaps";
	private static final String PLANTS = "plants";
	private static final String TRAPS = "traps";
	private static final String CUSTOM_TILES = "customTiles";
	private static final String MOBS = "mobs";
	private static final String BLOBS = "blobs";
	private static final String FEELING = "feeling";
	private static final String AREADIGGABLE = "areaDiggable";
	private static final String TIME = "time";

	public Level() {
		areaDiggable = true;
	}

	public void create() {

		resizingNeeded = false;

		map = new int[LENGTH];
		visited = new boolean[LENGTH];
		Arrays.fill(visited, false);
		mapped = new boolean[LENGTH];
		Arrays.fill(mapped, false);

		if (!Dungeon.bossLevel()) {
			addItemToSpawn(Generator.random(Generator.Category.FOOD));
			if (Dungeon.posNeeded()) {
				addItemToSpawn(new PotionOfStrength());
				Dungeon.limitedDrops.strengthPotions.count++;
			}
			if (Dungeon.souNeeded()) {
				addItemToSpawn(new ScrollOfUpgrade());
				Dungeon.limitedDrops.upgradeScrolls.count++;
			}
			if (Dungeon.asNeeded()) {
				addItemToSpawn(new Stylus());
				Dungeon.limitedDrops.arcaneStyli.count++;
			}

			int bonus = 0;
			for (Buff buff : Dungeon.hero.buffs(RingOfWealth.Wealth.class)) {
				bonus += ((RingOfWealth.Wealth) buff).level;
			}
			if (Random.Float() > Math.pow(0.95, bonus)) {
				if (Random.Int(2) == 0)
					addItemToSpawn(new ScrollOfMagicalInfusion());
				else
					addItemToSpawn(new PotionOfMight());
			}

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

			if (Dungeon.depth > 1) {
				switch (Random.Int(10)) {
					case 0:
						if (!Dungeon.bossLevel(Dungeon.depth + 1)) {
							feeling = Feeling.CHASM;
						}
						break;
					case 1:
						feeling = Feeling.WATER;
						break;
					case 2:
						feeling = Feeling.GRASS;
						break;
					case 3:
						feeling = Feeling.DARK;
						addItemToSpawn(new Torch());
						viewDistance = (int) Math.ceil(viewDistance / 3f);
						break;
				}
			}
		}

		boolean pitNeeded = Dungeon.depth > 1 && weakFloorCreated;

		do {
			Arrays.fill(map, feeling == Feeling.CHASM ? Terrain.CHASM : Terrain.WALL);

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
		cleanWalls();

		createMobs();
		createItems();

		stoneInside();
	}

	public void reset() {

		for (Mob mob : mobs.toArray(new Mob[0])) {
			if (!mob.reset()) {
				mobs.remove(mob);
			}
		}
		createMobs();
	}


	public long decay() {
		return 0;
	}
	public void decay(long amount, boolean updateTime, boolean fixTime) {
		if (updateTime && !fixTime) {
			if (time < amount) {
				time = amount;
			}
			else {
				return;
			}
		}

		for (int n=0;n<heaps.size();n++)
		{
			int key = heaps.keyAt(n);
			Heap heap = heaps.get(key);

			Iterator<Item> iterator = heap.items.iterator();
			boolean updated = false;
			while (iterator.hasNext())
			{
				Item item = iterator.next();
				if (item instanceof IDecayable) {
					IDecayable decayable = (IDecayable)item;
					decayable.decay(amount, updateTime, fixTime);
					if (decayable.decayed()) {
						iterator.remove();
						updated = true;
					}
				}
			}

			if (updated) {
				heap.updateImage();
				n--;
			}
		}
	}
	public boolean decayed(){
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

		visited = bundle.getBooleanArray(VISITED);
		mapped = bundle.getBooleanArray(MAPPED);

		entrance = bundle.getInt(ENTRANCE);
		exit = bundle.getInt(EXIT);

		locked = bundle.getBoolean(LOCKED);

		areaDiggable = bundle.getBoolean(AREADIGGABLE);
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
			if (!heap.isEmpty())
				heaps.put(heap.pos, heap);
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
					mob.pos = adjustPos(mob.pos);
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
		if (feeling == Feeling.DARK)
			viewDistance = (int) Math.ceil(viewDistance / 3f);

		buildFlagMaps();
		cleanWalls();
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(VERSION, Game.versionCode);
		bundle.put(MAP, map);
		bundle.put(VISITED, visited);
		bundle.put(MAPPED, mapped);
		bundle.put(ENTRANCE, entrance);
		bundle.put(EXIT, exit);
		bundle.put(LOCKED, locked);
		bundle.put(HEAPS, heaps.values());
		bundle.put(PLANTS, plants.values());
		bundle.put(TRAPS, traps.values());
		bundle.put(CUSTOM_TILES, customTiles);
		bundle.put(MOBS, mobs);
		bundle.put(BLOBS, blobs.values());
		bundle.put(FEELING, feeling);
		bundle.put(AREADIGGABLE, areaDiggable);
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
			Buff.affect(Dungeon.hero, LockedFloor.class);
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
				if (i >= WIDTH && water[i - WIDTH]) {
					visuals.add(new FlowParticle.Flow(i - WIDTH));
				}
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
			if (mob.pos == pos) {
				return mob;
			}
		}
		return null;
	}

	public Actor respawner() {
		return new Actor() {

			{
				actPriority = 1; //as if it were a buff.
			}

			@Override
			protected boolean act() {
				if (mobs.size() < nMobs()) {

					Mob mob = Bestiary.mutable(Dungeon.depth, Dungeon.hero.level);
					mob.state = mob.WANDERING;
					mob.pos = randomRespawnCell();
					if (Dungeon.hero.isAlive() && mob.pos != -1 && distance(Dungeon.hero.pos, mob.pos) >= 4) {
						GameScene.add(mob);
						if (Statistics.amuletObtained) {
							mob.beckon(Dungeon.hero.pos);
						}
					}
				}
				spend(Dungeon.level.feeling == Feeling.DARK || Statistics.amuletObtained ? TIME_TO_RESPAWN / 2 : TIME_TO_RESPAWN, false);
				return true;
			}
		};
	}

	public int randomRespawnCell() {
		int cell;
		do {
			cell = Random.Int(LENGTH);
		} while (!passable[cell] || Dungeon.visible[cell] || Actor.findChar(cell) != null);
		return cell;
	}

	public int randomDestination() {
		int cell;
		do {
			cell = Random.Int(LENGTH);
		} while (!passable[cell]);
		return cell;
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

	protected void buildFlagMaps() {

		for (int i = 0; i < LENGTH; i++) {
			int flags = Terrain.flags[map[i]];
			passable[i] = (flags & Terrain.FLAG_PASSABLE) != 0;
			pathable[i] = (flags & (Terrain.FLAG_PASSABLE | Terrain.FLAG_PATHABLE)) != 0;
			losBlocking[i] = (flags & Terrain.FLAG_LOSBLOCKING) != 0;
			wood[i] = (flags & Terrain.FLAG_WOOD) != 0;
			secret[i] = (flags & Terrain.FLAG_SECRET) != 0;
			solid[i] = (flags & Terrain.FLAG_SOLID) != 0;
			avoid[i] = (flags & Terrain.FLAG_AVOID) != 0;
			water[i] = (flags & Terrain.FLAG_LIQUID) != 0;
			pit[i] = (flags & Terrain.FLAG_PIT) != 0;
			stone[i] = (flags & Terrain.FLAG_STONE) != 0;
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
			if (water[i]) {
				int t = Terrain.WATER_TILES;
				for (int j = 0; j < NEIGHBOURS4.length; j++) {
					if ((Terrain.flags[map[i + NEIGHBOURS4[j]]] & Terrain.FLAG_UNSTITCHABLE) != 0) {
						t += 1 << j;
					}
				}
				map[i] = t;
			}

			if (pit[i]) {
				if (!pit[i - WIDTH]) {
					int c = map[i - WIDTH];
					if (c == Terrain.EMPTY_SP || c == Terrain.STATUE_SP) {
						map[i] = Terrain.CHASM_FLOOR_SP;
					} else if (water[i - WIDTH]) {
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
				}

				index++;
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


	public static void set(int cell, int terrain) {
		Painter.set(Dungeon.level, cell, terrain);

		if (terrain != Terrain.TRAP && terrain != Terrain.SECRET_TRAP && terrain != Terrain.INACTIVE_TRAP) {
			Dungeon.level.traps.remove(cell);
		}

		int flags = Terrain.flags[terrain];
		passable[cell] = (flags & Terrain.FLAG_PASSABLE) != 0;
		pathable[cell] = (flags & (Terrain.FLAG_PASSABLE | Terrain.FLAG_PATHABLE)) != 0;
		losBlocking[cell] = (flags & Terrain.FLAG_LOSBLOCKING) != 0;
		wood[cell] = (flags & Terrain.FLAG_WOOD) != 0;
		secret[cell] = (flags & Terrain.FLAG_SECRET) != 0;
		solid[cell] = (flags & Terrain.FLAG_SOLID) != 0;
		avoid[cell] = (flags & Terrain.FLAG_AVOID) != 0;
		pit[cell] = (flags & Terrain.FLAG_PIT) != 0;
		water[cell] = terrain == Terrain.WATER || terrain >= Terrain.WATER_TILES;
		stone[cell] = (flags & Terrain.FLAG_STONE) != 0;
	}

	public static void setDirt(int cell) {
		setSpecialRecursive.clear();
		setSpecialRecursive(cell, Terrain.DIRT, Terrain.WALL_STONE);

		/*
		if (setSpecialRecursive(cell, Terrain.DIRT, Terrain.FLAG_STONE, Terrain.FLAG_LIQUID)) {
			fillWater(cell);
		}
		*/
	}

	private static HashSet<Integer> setSpecialRecursive = new HashSet<Integer>();

	private static void setSpecialRecursive(int cell, int terrain, int cooperativeCell) {
		setSpecialRecursive.add(cell);

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
		else if (test == cooperativeCell) {
			good = true;
		}
		if (good) {
			placedTerrain++;
		}

		good = false;
		index += WIDTH + 1;
		test = Dungeon.level.map[index];
		if (test >= terrainType && test <= terrain) {
			good = true;
			updates.add(index);
		}
		else if (test == cooperativeCell) {
			good = true;
		}
		if (good) {
			placedTerrain += 2;
		}

		good = false;
		index += WIDTH - 1;
		test = Dungeon.level.map[index];
		if (test >= terrainType && test <= terrain) {
			good = true;
			updates.add(index);
		}
		else if (test == cooperativeCell) {
			good = true;
		}
		if (good) {
			placedTerrain += 4;
		}

		good = false;
		index -= WIDTH + 1;
		test = Dungeon.level.map[index];
		if (test >= terrainType && test <= terrain) {
			good = true;
			updates.add(index);
		}
		else if (test == cooperativeCell) {
			good = true;
		}
		if (good) {
			placedTerrain += 8;
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

			setSpecialRecursive(newCell, terrain, cooperativeCell);
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

		} else if (heap.type == Heap.Type.LOCKED_CHEST || heap.type == Heap.Type.CRYSTAL_CHEST) {

			int n;
			do {
				n = cell + Level.NEIGHBOURS8[Random.Int(8)];
			} while (!Level.passable[n] && !Level.avoid[n]);
			return drop(item, n);

		}
		heap.drop(item);

		if (Dungeon.level != null) {
			press(cell, null);
		}

		return heap;
	}

	public Plant plant(Plant.Seed seed, int pos) {

		Plant plant = plants.get(pos);
		if (plant != null) {
			plant.wither();
		}

		if (map[pos] == Terrain.HIGH_GRASS ||
				map[pos] == Terrain.EMPTY ||
				map[pos] == Terrain.EMBERS ||
				map[pos] == Terrain.EMPTY_DECO) {
			map[pos] = Terrain.GRASS;
			wood[pos] = true;
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

	public Trap setTrap(Trap trap, int pos) {
		Trap existingTrap = traps.get(pos);
		if (existingTrap != null) {
			traps.remove(pos);
			if (existingTrap.sprite != null) existingTrap.sprite.kill();
		}
		trap.set(pos);
		traps.put(pos, trap);
		GameScene.add(trap);
		return trap;
	}

	public void disarmTrap(int pos) {
		set(pos, Terrain.INACTIVE_TRAP);
		GameScene.updateMap(pos);
	}

	public void discover(int cell) {
		set(cell, Terrain.discover(map[cell]));
		Trap trap = traps.get(cell);
		if (trap != null) {
			trap.reveal();
		}
		GameScene.updateMap(cell);
	}

	public int pitCell() {
		return randomRespawnCell();
	}

	public void press(int cell, Char ch) {

		if (ch != null && pit[cell] && !ch.flying) {
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

			case Terrain.WELL:
				WellWater.affectCell(cell);
				break;

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

		int cell = mob.pos;

		if (pit[cell] && !mob.flying) {
			Chasm.mobFall(mob);
			return;
		}

		Trap trap = null;
		switch (map[cell]) {

			case Terrain.TRAP:
				trap = traps.get(cell);
				break;
			case Terrain.DOOR:
				if (mob.canOpenDoors) {
					Door.open(cell);

					if (Random.Int(3) == 0) {
						GLog.i("You feel an unexpected draft.");
					}
				}
				break;
		}

		if (trap != null) {
			trap.trigger();
		}

		Plant plant = plants.get(cell);
		if (plant != null) {
			plant.trigger();
		}
	}

	public boolean[] updateFieldOfView(Char c) {

		int cx = c.pos % WIDTH;
		int cy = c.pos / WIDTH;

		boolean sighted = c.buff(Blindness.class) == null && c.buff(Shadows.class) == null
				&& c.buff(TimekeepersHourglass.timeStasis.class) == null && c.isAlive();
		if (sighted) {
			ShadowCaster.castShadow(cx, cy, fieldOfView, c.viewDistance);
		} else {
			Arrays.fill(fieldOfView, false);
		}

		int sense = 1;
		if (c.isAlive()) {
			for (Buff b : c.buffs(MindVision.class)) {
				sense = Math.max(((MindVision) b).distance, sense);
			}
		}

		if ((sighted && sense > 1) || !sighted) {

			int ax = Math.max(0, cx - sense);
			int bx = Math.min(cx + sense, WIDTH - 1);
			int ay = Math.max(0, cy - sense);
			int by = Math.min(cy + sense, HEIGHT - 1);

			int len = bx - ax + 1;
			int pos = ax + ay * WIDTH;
			for (int y = ay; y <= by; y++, pos += WIDTH) {
				Arrays.fill(fieldOfView, pos, pos + len, true);
			}

			for (int i = 0; i < LENGTH; i++) {
				fieldOfView[i] &= discoverable[i];
			}
		}

		if (c.isAlive()) {
			if (c.buff(MindVision.class) != null) {
				for (Mob mob : mobs) {
					int p = mob.pos;
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
			} else if (c == Dungeon.hero && ((Hero) c).heroClass == HeroClass.HUNTRESS) {
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
			if (c.buff(Awareness.class) != null) {
				for (Heap heap : heaps.values()) {
					int p = heap.pos;
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

		for (Heap heap : heaps.values())
			if (!heap.seen && fieldOfView[heap.pos] && c == Dungeon.hero)
				heap.seen = true;

		return fieldOfView;
	}

	public static int distance(int a, int b) {
		int ax = a % WIDTH;
		int ay = a / WIDTH;
		int bx = b % WIDTH;
		int by = b / WIDTH;
		return Math.max(Math.abs(ax - bx), Math.abs(ay - by));
	}

	public static boolean adjacent(int a, int b) {
		int diff = Math.abs(a - b);
		return diff == 1 || diff == WIDTH || diff == WIDTH + 1 || diff == WIDTH - 1;
	}

	//returns true if the input is a valid tile within the level
	public static boolean insideMap(int tile) {
		//outside map array
		return !((tile <= -1 || tile >= LENGTH) ||
				//top and bottom row
				(tile <= 31 || tile >= LENGTH - WIDTH) ||
				//left and right column
				(tile % WIDTH == 0 || tile % WIDTH == 31));
	}

	public String tileName(int tile) {

		if (tile >= Terrain.WATER_TILES && tile <= Terrain.WATER) {
			return tileName(Terrain.WATER);
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
			case Terrain.WATER:
				return "Water";
			case Terrain.WALL:
			case Terrain.WALL_DECO:
			case Terrain.SECRET_DOOR:
				return "Wall";
			case Terrain.DOOR:
				return "Closed door";
			case Terrain.OPEN_DOOR:
				return "Open door";
			case Terrain.ENTRANCE:
				return "Depth entrance";
			case Terrain.EXIT:
				return "Depth exit";
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
			case Terrain.BOOKSHELF:
				return "Bookshelf";
			case Terrain.ALCHEMY:
				return "Alchemy pot";
			case Terrain.DENATURED_DEBRIS:
				return "Denatured Debris";
			case Terrain.WOOD_DEBRIS:
				return "Wooden Debris";
			case Terrain.WALL_STONE:
				return "Stone Wall";
			default:
				return "???";
		}
	}

	public String tileDesc(int tile) {

		switch (tile) {
			case Terrain.CHASM:
				return "You can't see the bottom.";
			case Terrain.WATER:
				return "In case of burning step into the water to extinguish the fire.";
			case Terrain.ENTRANCE:
				return "Stairs lead up to the upper depth.";
			case Terrain.EXIT:
			case Terrain.UNLOCKED_EXIT:
				return "Stairs lead down to the lower depth.";
			case Terrain.EMBERS:
				return "Embers cover the floor.";
			case Terrain.HIGH_GRASS:
				return "Dense vegetation blocks the view.";
			case Terrain.LOCKED_DOOR:
				return "This door is locked, you need a matching key to unlock it.";
			case Terrain.LOCKED_EXIT:
				return "Heavy bars block the stairs leading down.";
			case Terrain.BARRICADE:
				return "The wooden barricade is firmly set but has dried over the years. Might it burn?";
			case Terrain.SIGN:
				return "You can't read the text from here.";
			case Terrain.INACTIVE_TRAP:
				return "The trap has been triggered before and it's not dangerous anymore.";
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return "Someone wanted to adorn this place, but failed, obviously.";
			case Terrain.ALCHEMY:
				return "Drop some seeds here to cook a potion.";
			case Terrain.EMPTY_WELL:
				return "The well has run dry.";
			case Terrain.DENATURED_DEBRIS:
				return "Unrecognizable debris covers the floor.";
			case Terrain.WOOD_DEBRIS:
				return "Wooden debris covers the floor.";
			default:
				if (tile >= Terrain.WATER_TILES) {
					return tileDesc(Terrain.WATER);
				}
				if ((Terrain.flags[tile] & Terrain.FLAG_PIT) != 0) {
					return tileDesc(Terrain.CHASM);
				}
				return "";
		}
	}
}
