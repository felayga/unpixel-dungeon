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
package com.felayga.unpixeldungeon;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Amok;
import com.felayga.unpixeldungeon.actors.buffs.positive.Light;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.felayga.unpixeldungeon.items.Ankh;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.food.CannedFood;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.rings.Ring;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.levels.DeadEndLevel;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.MineTownLevel;
import com.felayga.unpixeldungeon.levels.MinesLevel;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.SewerLevel;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.levels.branches.DungeonBranch;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.felayga.unpixeldungeon.utils.BArray;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.hero.WndInitHero;
import com.felayga.unpixeldungeon.windows.hero.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

//import com.felayga.unpixeldungeon.actors.mobs.npcs.Ghost;
//import com.felayga.unpixeldungeon.actors.mobs.npcs.Imp;

public class Dungeon {
    public static int challenges;

    public static Hero hero;
    public static Level level;

    public static QuickSlot quickslot = new QuickSlot();

    private static int __depth;
    public static int depthAdjusted;

    public static int depth() {
        return __depth;
    }

    public static void depth(int newDepth) {
        if (__depth == newDepth) {
            return;
        }

        __depth = newDepth;
        depthAdjusted = DungeonBranch.getAdjustedDepth(__depth);
    }

    // Reason of death
    public static String resultDescription;

    public static HashSet<Integer> chapters;

    // Hero's field of view
    public static boolean[] visible = new boolean[Level.LENGTH];

    public static SparseArray<ArrayList<Item>> droppedItems;

    public static int version;

    public static void init() {
        version = Game.versionCode;
        challenges = ShatteredPixelDungeon.challenges();

        Actor.clear();
        Actor.resetNextID();

        PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);

        Scroll.initLabels();
        Potion.initColors();
        Ring.initGems();
        Wand.initLabels();

        Statistics.reset();
        Journal.reset();

        quickslot.reset();
        QuickSlotButton.reset();

        __depth = 0;
        depthAdjusted = 0;
        //gold = 0;

        droppedItems = new SparseArray<ArrayList<Item>>();

        chapters = new HashSet<Integer>();

        //Ghost.Quest.reset();
        Wandmaker.Quest.reset();
        Blacksmith.Quest.reset();
        //Imp.Quest.reset();

        Room.shuffleTypes();

        Generator.initArtifacts();
        hero = new Hero();
        hero.live();

        Badges.reset();

        HeroClass.toHeroClass(WndInitHero.heroClassSelected).initHero(hero);

        //todo: fart
        //StartScene.curClass.initHero( hero );
    }

    public static boolean isChallenged(int mask) {
        return (challenges & mask) != 0;
    }

    public static Level newLevel(int index) {

        Dungeon.level = null;
        Actor.clear();

        depth(index);

		/*
		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;
			
			if (Statistics.qualifiedForNoKilling) {
				Statistics.completedWithNoKilling = true;
			} else {
				Statistics.completedWithNoKilling = false;
			}
		}
		*/

        Arrays.fill(visible, false);

        Level level;
        if (__depth >= DungeonBranch.Normal.levelMin && __depth <= DungeonBranch.Normal.levelMax) {
            level = new MineTownLevel();
        } else if (__depth >= DungeonBranch.Mines.levelMin && __depth <= DungeonBranch.Mines.levelMax) {
            int offset = __depth - DungeonBranch.Mines.levelMin;

            switch(offset) {
                case 2:
                    level = new MineTownLevel();
                    break;
                default:
                    level = new MinesLevel();
            }
        } else {
            level = new DeadEndLevel();
        }

        /*
        switch (depth) {
            case 1:
            case 2:
            case 3:
            case 4:
                level = new SewerLevel();
                break;
            case 5:
                level = new SewerBossLevel();
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                level = new PrisonLevel();
                break;
            case 10:
                //level = new PrisonBossLevel();
                //break;
            case 11:
            case 12:
            case 13:
            case 14:
                level = new CavesLevel();
                break;
            case 15:
                level = new CavesBossLevel();
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                level = new CityLevel();
                break;
            case 20:
                level = new CityBossLevel();
                break;
            case 21:
                level = new LastShopLevel();
                break;
            case 22:
            case 23:
            case 24:
                level = new HallsLevel();
                break;
            case 25:
                //level = new HallsBossLevel();
                //break;
            case 26:
                level = new LastLevel();
                break;
            default:
                level = new DeadEndLevel();
			//Statistics.deepestFloor--;
        }
        */
        level.create();

        return level;
    }

    public static void resetLevel() {

        Actor.clear();

        Arrays.fill(visible, false);

        level.reset();
        switchLevel(level, level.entrance);
    }

    /*
    public static boolean shopOnLevel() {
        return depth == 6 || depth == 11 || depth == 16;
    }

    public static boolean bossLevel() {
        return bossLevel(depth);
    }

    public static boolean bossLevel(int depth) {
        return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25;
    }
    */

    @SuppressWarnings("deprecation")
    public static void switchLevel(final Level level, int pos) {

        Dungeon.level = level;
        Actor.init();

        Actor respawner = level.respawner();
        if (respawner != null) {
            Actor.add(level.respawner());
        }

        if (pos == Constant.Position.EXIT) {
            hero.pos = level.exit;
        } else if (pos == Constant.Position.ENTRANCE) {
            hero.pos = level.entrance;
        } else if (pos == Constant.Position.RANDOM) {
            while (pos < 0 || (Terrain.flags[level.map[pos]] & Terrain.FLAG_PASSABLE) == 0) {
                pos = Random.Int(level.WIDTH - 1) + 1 + (Random.Int(level.HEIGHT - 1) + 1) * level.WIDTH;
            }

            hero.pos = pos;
        } else if (pos == Constant.Position.EXIT) {
            hero.pos = level.exitAlternate;
        } else if (pos == Constant.Position.ENTRANCE) {
            hero.pos = level.entranceAlternate;
        } else {
            hero.pos = pos;
        }

        Light light = hero.buff(Light.class);
        hero.viewDistance = light == null ? level.viewDistance : Math.max(Light.DISTANCE, level.viewDistance);

        //logic for pre 0.3.0 saves, need to give mages a staff.
        if (Dungeon.version <= 38 && Dungeon.hero.heroClass == HeroClass.MAGE) {
            MagesStaff staff = new MagesStaff();
            staff.identify();
            if (!Dungeon.hero.belongings.collect(staff)) {
                Dungeon.level.drop(staff, Dungeon.hero.pos);
            }
        }

        observe();
        try {
            saveAll();
        } catch (IOException e) {
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
        }
    }

    public static void dropToChasm(Item item) {
        int depth = Dungeon.__depth + 1;
        ArrayList<Item> dropped = (ArrayList<Item>) Dungeon.droppedItems.get(depth);
        if (dropped == null) {
            Dungeon.droppedItems.put(depth, dropped = new ArrayList<Item>());
        }
        dropped.add(item);
    }

    private static final String RG_GAME_FILE = "game.dat";
    private static final String RG_DEPTH_FILE = "depth%d.dat";

    private static final String WR_GAME_FILE = "warrior.dat";
    private static final String WR_DEPTH_FILE = "warrior%d.dat";

    private static final String MG_GAME_FILE = "mage.dat";
    private static final String MG_DEPTH_FILE = "mage%d.dat";

    private static final String RN_GAME_FILE = "ranger.dat";
    private static final String RN_DEPTH_FILE = "ranger%d.dat";

    private static final String VERSION = "version";
    private static final String CHALLENGES = "challenges";
    private static final String GOLD = "gold";
    private static final String DROPPED = "dropped%d";
    private static final String LEVEL = "level";
    private static final String LIMDROPS = "limiteddrops";
    private static final String DV = "dewVial";
    private static final String WT = "transmutation";
    private static final String CHAPTERS = "chapters";
    private static final String QUESTS = "quests";
    private static final String BADGES = "badges";

    public static final String HERO = "hero";
    public static final String DEPTH = "depth";

    //TODO: to support pre-0.2.3 saves, remove when needed
    private static final String POS = "potionsOfStrength";
    private static final String SOU = "scrollsOfEnhancement";
    private static final String AS = "arcaneStyli";

    public static String gameFile(int index) {
        return "SAVE" + index + ".DAT";
    }

    private static String depthFile(int index) {
        return "SAVE" + index + "L%d.DAT";
    }

    public static void saveGame(String fileName) throws IOException {
        try {
            Bundle bundle = new Bundle();

            version = Game.versionCode;
            bundle.put(VERSION, version);
            bundle.put(CHALLENGES, challenges);
            bundle.put(HERO, hero);
            //bundle.put( GOLD, gold );
            bundle.put(DEPTH, __depth);

            GamesInProgress.Info info = GamesInProgress.check(WndInitHero.savedGameIndex);
            if (info == null) {
                info = new GamesInProgress.Info();
                info.fromWndHeroInit();
            }

            info.storeInBundle(bundle);

            for (int d : droppedItems.keyArray()) {
                bundle.put(String.format(DROPPED, d), droppedItems.get(d));
            }

            quickslot.storePlaceholders(bundle);

            int count = 0;
            int ids[] = new int[chapters.size()];
            for (Integer id : chapters) {
                ids[count++] = id;
            }
            bundle.put(CHAPTERS, ids);

            Bundle quests = new Bundle();
            //Ghost		.Quest.storeInBundle( quests );
            Wandmaker.Quest.storeInBundle(quests);
            Blacksmith.Quest.storeInBundle(quests);
            //Imp			.Quest.storeInBundle( quests );
            bundle.put(QUESTS, quests);

            Room.storeRoomsInBundle(bundle);

            Statistics.storeInBundle(bundle);
            Journal.storeInBundle(bundle);
            Generator.storeInBundle(bundle);

            Scroll.save(bundle);
            Potion.save(bundle);
            Ring.save(bundle);
            Wand.save(bundle);
            CannedFood.save(bundle);
            DungeonBranch.save(bundle);

            Actor.storeNextID(bundle);

            Bundle badges = new Bundle();
            Badges.saveLocal(badges);
            bundle.put(BADGES, badges);

            OutputStream output = Game.instance.openFileOutput(fileName, Game.MODE_PRIVATE);
            Bundle.write(bundle, output);
            output.close();

        } catch (IOException e) {

            GamesInProgress.setUnknown(WndInitHero.savedGameIndex);
        }
    }

    public static void saveLevel() throws IOException {
        Bundle bundle = new Bundle();
        bundle.put(LEVEL, level);

        OutputStream output = Game.instance.openFileOutput(
                Utils.format(depthFile(WndInitHero.savedGameIndex), __depth), Game.MODE_PRIVATE);
        Bundle.write(bundle, output);
        output.close();
    }

    public static void saveAll() throws IOException {
        if (hero.isAlive()) {

            GameTime.fix();
            saveGame(gameFile(WndInitHero.savedGameIndex));
            saveLevel();

            GamesInProgress.set(WndInitHero.savedGameIndex, hero.heroClass, hero.level, __depth);

        } else if (WndResurrect.instance != null) {

            WndResurrect.instance.hide();
            Hero.reallyDie(WndResurrect.causeOfDeath);

        }
    }

    public static void loadGame(int index) throws IOException {
        loadGame(gameFile(index), true);
    }

    public static void loadGame(String fileName) throws IOException {
        loadGame(fileName, false);
    }

    public static void loadGame(String fileName, boolean fullLoad) throws IOException {

        Bundle bundle = gameBundle(fileName);

        version = bundle.getInt(VERSION);

        Generator.reset();

        Actor.restoreNextID(bundle);

        quickslot.reset();
        QuickSlotButton.reset();

        Dungeon.challenges = bundle.getInt(CHALLENGES);

        Dungeon.level = null;
        Dungeon.__depth = -1;
        Dungeon.depthAdjusted = -1;

        if (fullLoad) {
            PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);
        }

        Scroll.restore(bundle);
        Potion.restore(bundle);
        Ring.restore(bundle);
        Wand.restore(bundle);
        CannedFood.restore(bundle);
        DungeonBranch.restore(bundle);

        quickslot.restorePlaceholders(bundle);

        if (fullLoad) {
            chapters = new HashSet<Integer>();
            int ids[] = bundle.getIntArray(CHAPTERS);
            if (ids != null) {
                for (int id : ids) {
                    chapters.add(id);
                }
            }

            Bundle quests = bundle.getBundle(QUESTS);
            if (!quests.isNull()) {
                //Ghost.Quest.restoreFromBundle( quests );
                Wandmaker.Quest.restoreFromBundle(quests);
                Blacksmith.Quest.restoreFromBundle(quests);
                //Imp.Quest.restoreFromBundle( quests );
            } else {
                //Ghost.Quest.reset();
                Wandmaker.Quest.reset();
                Blacksmith.Quest.reset();
                //Imp.Quest.reset();
            }

            Room.restoreRoomsFromBundle(bundle);
        }

        Bundle badges = bundle.getBundle(BADGES);
        if (!badges.isNull()) {
            Badges.loadLocal(badges);
        } else {
            Badges.reset();
        }

        hero = null;
        hero = (Hero) bundle.get(HERO);

        //gold = bundle.getInt( GOLD );
        depth(bundle.getInt(DEPTH));

        Statistics.restoreFromBundle(bundle);
        Journal.restoreFromBundle(bundle);
        Generator.restoreFromBundle(bundle);

        droppedItems = new SparseArray<ArrayList<Item>>();
        for (int i = 2; i < DungeonBranch.MAXLEVEL; i++) {
            ArrayList<Item> dropped = new ArrayList<Item>();
            for (Bundlable b : bundle.getCollection(String.format(DROPPED, i))) {
                dropped.add((Item) b);
            }
            if (!dropped.isEmpty()) {
                droppedItems.put(i, dropped);
            }
        }

		/*
		//logic for pre 0.2.4 bags, remove when no longer supporting those saves.
		if (version <= 32){
			int deepest = Statistics.deepestFloor;
			if (deepest > 15) limitedDrops.wandBag.count = 1;
			if (deepest > 10) limitedDrops.scrollBag.count = 1;
			if (deepest > 5)  limitedDrops.seedBag.count = 1;
		}
		*/
    }

    public static Level loadLevel(int index) throws IOException {
        Dungeon.level = null;
        Actor.clear();

        InputStream input = Game.instance.openFileInput(Utils.format(depthFile(index), __depth));
        Bundle bundle = Bundle.read(input);
        input.close();

        return (Level) bundle.get("level");
    }

    public static void deleteGame(int index, boolean dead, boolean deleteLevels) {
        GamesInProgress.delete(index);

        try {
            saveGame(gameFile(index));
        } catch (IOException whocares) {
            Game.instance.deleteFile(gameFile(index));
        }

        if (deleteLevels) {
            int depth = 1;
            while (Game.instance.deleteFile(Utils.format(depthFile(index), depth))) {
                depth++;
            }
        }

    }

    public static Bundle gameBundle(String fileName) throws IOException {

        InputStream input = Game.instance.openFileInput(fileName);
        Bundle bundle = Bundle.read(input);
        input.close();

        return bundle;
    }

    public static void fail(String desc) {
        resultDescription = desc;
        if (hero.belongings.getItem(Ankh.class) == null) {
            Rankings.INSTANCE.submit(false);
        }
    }

    public static void win(String desc) {

        hero.belongings.identify();

        if (challenges != 0) {
            Badges.validateChampion();
        }

        resultDescription = desc;
        Rankings.INSTANCE.submit(true);
    }

    public static void observe() {

        if (level == null) {
            return;
        }

        level.updateFieldOfView(hero);
        System.arraycopy(Level.fieldOfView, 0, visible, 0, visible.length);

        BArray.or(level.visited, visible, level.visited);

        GameScene.afterObserve();
    }

    private static boolean[] passable = new boolean[Level.LENGTH];

    public static int findPath(Char ch, int from, int to, boolean pass[], boolean[] diagonal, boolean[] visible) {
        if (Level.canStep(from, to, diagonal)) {
            return Actor.findChar(to) == null && (pass[to] || Level.avoid[to]) ? to : -1;
        }

        if (ch.flying || ch.buff(Amok.class) != null) {
            BArray.or(pass, Level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Level.LENGTH);
        }

        for (Char c : Actor.chars()) {
            if (visible[c.pos]) {
                passable[c.pos] = false;
            }
        }

        return PathFinder.getStep(from, to, passable, diagonal);

    }

    public static int flee(Char ch, int cur, int from, boolean pass[], boolean[] diagonal, boolean[] visible) {

        if (ch.flying) {
            BArray.or(pass, Level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Level.LENGTH);
        }

        for (Char c : Actor.chars()) {
            if (visible[c.pos]) {
                passable[c.pos] = false;
            }
        }

        passable[cur] = true;

        return PathFinder.getStepBack(cur, from, passable, diagonal);

    }

}
