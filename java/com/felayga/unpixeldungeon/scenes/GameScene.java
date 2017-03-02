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
package com.felayga.unpixeldungeon.scenes;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.FogOfWar;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.WarningSpriteHandler;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.BannerSprites;
import com.felayga.unpixeldungeon.effects.BlobEmitter;
import com.felayga.unpixeldungeon.effects.EmoIcon;
import com.felayga.unpixeldungeon.effects.Flare;
import com.felayga.unpixeldungeon.effects.FloatingText;
import com.felayga.unpixeldungeon.effects.Ripple;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.RegularLevel;
import com.felayga.unpixeldungeon.levels.branches.DungeonBranch;
import com.felayga.unpixeldungeon.levels.features.Chasm;
import com.felayga.unpixeldungeon.levels.traps.Trap;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.DiscardedItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteBase;
import com.felayga.unpixeldungeon.sprites.PlantSprite;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.ui.Banner;
import com.felayga.unpixeldungeon.ui.BusyIndicator;
import com.felayga.unpixeldungeon.ui.CustomTileVisual;
import com.felayga.unpixeldungeon.ui.GameLog;
import com.felayga.unpixeldungeon.ui.HallucinationOverlay;
import com.felayga.unpixeldungeon.ui.HealthIndicator;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.felayga.unpixeldungeon.ui.StatusPane;
import com.felayga.unpixeldungeon.ui.Toast;
import com.felayga.unpixeldungeon.ui.Toolbar;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.ui.tag.ActionIndicator;
import com.felayga.unpixeldungeon.ui.tag.AttackIndicator;
import com.felayga.unpixeldungeon.ui.tag.LootIndicator;
import com.felayga.unpixeldungeon.ui.tag.ResumeIndicator;
import com.felayga.unpixeldungeon.unPixelDungeon;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndGame;
import com.felayga.unpixeldungeon.windows.WndInfoCell;
import com.felayga.unpixeldungeon.windows.WndInfoItem;
import com.felayga.unpixeldungeon.windows.WndInfoMob;
import com.felayga.unpixeldungeon.windows.WndInfoPlant;
import com.felayga.unpixeldungeon.windows.WndInfoTrap;
import com.felayga.unpixeldungeon.windows.WndMessage;
import com.felayga.unpixeldungeon.windows.WndStory;
import com.felayga.unpixeldungeon.windows.WndTradeItem;
import com.felayga.unpixeldungeon.windows.hero.WndHero;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;

//import com.felayga.unpixeldungeon.items.Honeypot;

public class GameScene extends PixelScene {

    private static final String TXT_WELCOME = "Welcome to level %s of Pixel Dungeon!";
    private static final String TXT_WELCOME_BACK = "Welcome back to level %s of Pixel Dungeon!";

    private static final String TXT_CHASM = "Your steps echo across the dungeon.";
    private static final String TXT_WATER = "You hear water splashing around you.";
    private static final String TXT_GRASS = "The smell of vegetation is thick in the air.";
    private static final String TXT_DARK = "You can hear enemies moving in the darkness...";
    private static final String TXT_SECRETS = "The atmosphere hints that this floor hides many secrets.";

    static GameScene scene;

    private SkinnedBlock water;
    private SkinnedBlock waterUnder;
    private DungeonTilemap tiles;
    private FogOfWar fog;
    private HallucinationOverlay hallucinationOverlay;
    private HeroSprite hero;

    private GameLog log;

    private BusyIndicator busy;

    private static CellSelector cellSelector;

    private Group terrain;
    private Group customTiles;
    private Group levelVisuals;
    private Group ripples;
    private Group plants;
    private Group traps;
    private Group heaps;
    private Group mobs;
    private Group emitters;
    private Group effects;
    private Group gases;
    private Group spells;
    private Group statuses;
    private Group emoicons;

    private WarningSpriteHandler overlay;

    private Toolbar toolbar;
    private Toast prompt;

    private AttackIndicator attack;
    private LootIndicator loot;
    private ResumeIndicator resume;
    private ActionIndicator action;

    @Override
    public void create() {
        Music.INSTANCE.play(Assets.TUNE, true);
        Music.INSTANCE.volume(unPixelDungeon.musicVol() / 10f);

        unPixelDungeon.lastClass(Dungeon.hero.heroClass.ordinal());

        super.create();
        Camera.main.zoom(GameMath.gate(minZoom, defaultZoom + unPixelDungeon.zoom(), maxZoom));

        scene = this;

        terrain = new Group(-1);
        add(terrain);


        waterUnder = new SkinnedBlock(
                Level.WIDTH * DungeonTilemap.SIZE,
                Level.HEIGHT * DungeonTilemap.SIZE,
                Dungeon.level.waterUnderTex());
        terrain.add(waterUnder);


        water = new SkinnedBlock(
                Level.WIDTH * DungeonTilemap.SIZE,
                Level.HEIGHT * DungeonTilemap.SIZE,
                Dungeon.level.waterTex());
        terrain.add(water);

        ripples = new Group(-1);
        terrain.add(ripples);

        tiles = new DungeonTilemap();
        terrain.add(tiles);

        customTiles = new Group(-1);
        terrain.add(customTiles);

        for (CustomTileVisual visual : Dungeon.level.customTiles) {
            addCustomTile(visual.create());
        }

        levelVisuals = Dungeon.level.addVisuals();
        add(levelVisuals);

        traps = new Group(-1);
        add(traps);

        int size = Dungeon.level.traps.size();
        for (int i = 0; i < size; i++) {
            addTrapSprite(Dungeon.level.traps.valueAt(i));
        }

        plants = new Group(-1);
        add(plants);

        size = Dungeon.level.plants.size();
        for (int i = 0; i < size; i++) {
            addPlantSprite(Dungeon.level.plants.valueAt(i));
        }

        heaps = new Group(-1);
        add(heaps);

        size = Dungeon.level.heaps.size();
        for (int i = 0; i < size; i++) {
            addHeapSprite(Dungeon.level.heaps.valueAt(i));
        }

        emitters = new Group(-1);
        effects = new Group(-1);
        emoicons = new Group(-1);

        mobs = new Group(-1);
        add(mobs);

        for (Mob mob : Dungeon.level.mobs) {
            addMobSprite(mob);
            if (Statistics.amuletObtained) {
                mob.beckon(Dungeon.hero.pos());
            }
        }

        add(emitters);
        add(effects);

        gases = new Group(-1);
        add(gases);

        for (Blob blob : Dungeon.level.blobs.values()) {
            blob.emitter = null;
            addBlobSprite(blob);
        }

        hallucinationOverlay = new HallucinationOverlay(Level.WIDTH, Level.HEIGHT);
        add(hallucinationOverlay);

        fog = new FogOfWar(Level.WIDTH, Level.HEIGHT);
        fog.updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
        add(fog);

        if (overlay == null) {
            overlay = new WarningSpriteHandler();
        }
        add(overlay);

        brightness(unPixelDungeon.brightness());

        spells = new Group(-1);
        add(spells);

        statuses = new Group(-1);
        add(statuses);

        add(emoicons);

        hero = new HeroSprite(Dungeon.hero);
        hero.place(Dungeon.hero.pos());
        mobs.add(hero);

        add(new HealthIndicator());

        add(cellSelector = new CellSelector(tiles));

        StatusPane sb = new StatusPane();
        sb.camera = uiCamera;
        sb.setSize(uiCamera.width, 0);
        add(sb);

        toolbar = new Toolbar();
        toolbar.camera = uiCamera;
        toolbar.setRect(0, uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height());
        add(toolbar);

        attack = new AttackIndicator();
        attack.camera = uiCamera;
        add(attack);

        loot = new LootIndicator();
        loot.camera = uiCamera;
        add(loot);

        action = new ActionIndicator();
        action.camera = uiCamera;
        add(action);

        resume = new ResumeIndicator();
        resume.camera = uiCamera;
        add(resume);

        log = new GameLog();
        log.camera = uiCamera;
        add(log);

        layoutTags();

        if (Statistics.floorsVisited[Dungeon.depth()]) {
            GLog.i(TXT_WELCOME_BACK, DungeonBranch.getDepthText(Dungeon.depth()));
        } else {
            GLog.i(TXT_WELCOME, DungeonBranch.getDepthText(Dungeon.depth()));
            if (InterlevelScene.mode == InterlevelScene.Mode.DESCEND)
                Sample.INSTANCE.play(Assets.SND_DESCEND);
        }

        Dungeon.hero.updateEncumbrance();

        Statistics.floorsVisited[Dungeon.depth()] = true;

        switch (Dungeon.level.feeling) {
            case CHASM:
                GLog.w(TXT_CHASM);
                break;
            case WATER:
                GLog.w(TXT_WATER);
                break;
            case GRASS:
                GLog.w(TXT_GRASS);
                break;
            case DARK:
                GLog.w(TXT_DARK);
                break;
            default:
        }
        if (Dungeon.level instanceof RegularLevel &&
                ((RegularLevel) Dungeon.level).secretDoors > Random.IntRange(3, 4)) {
            GLog.w(TXT_SECRETS);
        }

        busy = new BusyIndicator();
        busy.camera = uiCamera;
        busy.x = 1;
        busy.y = sb.bottom() + 1;
        add(busy);

        switch (InterlevelScene.mode) {
            case RESURRECT:
                ScrollOfTeleportation.appear(Dungeon.hero, Dungeon.level.entrance);
                new Flare(8, 32).color(0xFFFF66, true).show(hero, 2f);
                break;
            case RETURN:
                ScrollOfTeleportation.appear(Dungeon.hero, Dungeon.hero.pos());
                break;
            case FALL:
                Chasm.heroLand();
                break;
            case DESCEND:
                switch (Dungeon.depth()) {
                    case 1:
                        WndStory.showChapter(WndStory.ID_SEWERS);
                        break;
                    case 6:
                        WndStory.showChapter(WndStory.ID_PRISON);
                        break;
                    case 11:
                        WndStory.showChapter(WndStory.ID_CAVES);
                        break;
                    case 16:
                        WndStory.showChapter(WndStory.ID_METROPOLIS);
                        break;
                    case 22:
                        WndStory.showChapter(WndStory.ID_HALLS);
                        break;
                }
                if (Dungeon.hero.isAlive() && Dungeon.depth() != 22) {
                    Badges.validateNoKilling();
                }
                break;
            default:
                break;
        }
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;

        ArrayList<Item> dropped = Dungeon.droppedItems.get(Dungeon.depth());
        if (dropped != null) {
            for (Item item : dropped) {
                int pos = Dungeon.level.randomRespawnCell();
                //todo: dropped dungeon items, owned or not?  not as if affected targets on the lower level know where it came from
                if (item instanceof Potion) {
                    ((Potion) item).shatter(null, pos);
                } else if (item instanceof Plant.Seed) {
                    Dungeon.level.plant(null, (Plant.Seed) item, pos);
                    //} else if (item instanceof Honeypot) {
                    //	Dungeon.level.drop(((Honeypot) item).shatter(null, pos), pos);
                } else {
                    Dungeon.level.drop(item, pos);
                }
            }
            Dungeon.droppedItems.remove(Dungeon.depth());
        }

        Dungeon.hero.next();

        Camera.main.target = hero;
        fadeIn();
    }

    public void destroy() {
        freezeEmitters = false;

        scene = null;
        Badges.saveGlobal();

        super.destroy();
    }

    @Override
    public synchronized void pause() {
        try {
            Dungeon.saveAll();
            Badges.saveGlobal();
        } catch (IOException e) {
            //
        }
    }

    public static synchronized void updateLootIndicator() {
        scene.loot.update();
    }

    @Override
    public synchronized void update() {
        if (Dungeon.hero == null) {
            return;
        }

        super.update();

        if (!freezeEmitters) water.offset(0, -5 * Game.elapsed);

        Actor.process();

        if (Dungeon.hero.ready && Dungeon.hero.paralysed == 0) {
            log.newLine();
        }

        if (tagAttack != attack.active || tagLoot != loot.visible || tagResume != resume.visible || tagAction != action.visible) {
            //todo: figure out if tab positioning behavior (gaps underneath in some circumstances) is desirable
            boolean atkAppearing = attack.active && !tagAttack;
            boolean lootAppearing = loot.visible && !tagLoot;
            boolean resAppearing = resume.visible && !tagResume;
            boolean actionAppearing = action.visible && !tagAction;

            tagAttack = attack.active;
            tagLoot = loot.visible;
            tagResume = resume.visible;
            tagAction = action.visible;

            if (atkAppearing || lootAppearing || resAppearing || actionAppearing) {
                layoutTags();
            }
        }

        overlay.synchronize(Dungeon.level.warnings);

        cellSelector.enable(Dungeon.hero.ready);
    }

    @Override
    protected void surfaceChanged() {
        hallucinationOverlay.surfaceChanged();
    }

    private boolean tagAttack = false;
    private boolean tagLoot = false;
    private boolean tagResume = false;
    private boolean tagAction = false;

    public static void layoutTags() {
        if (scene == null) return;

        float tagLeft = unPixelDungeon.flipTags() ? 0 : uiCamera.width - scene.attack.width();

        if (unPixelDungeon.flipTags()) {
            scene.log.setRect(scene.attack.width(), scene.toolbar.top(), uiCamera.width - scene.attack.width(), 0);
        } else {
            scene.log.setRect(0, scene.toolbar.top(), uiCamera.width - scene.attack.width(), 0);
        }

        float pos = scene.toolbar.actualTop();

        if (scene.tagAttack) {
            scene.attack.setPos(tagLeft, pos - scene.attack.height());
            scene.attack.flip(tagLeft == 0);
            pos = scene.attack.top();
        }

        if (scene.tagLoot) {
            scene.loot.setPos(tagLeft, pos - scene.loot.height());
            scene.loot.flip(tagLeft == 0);
            pos = scene.loot.top();
        }

        if (scene.tagAction) {
            scene.action.setPos(tagLeft, pos - scene.action.height());
            scene.action.flip(tagLeft == 0);
            pos = scene.action.top();
        }

        if (scene.tagResume) {
            scene.resume.setPos(tagLeft, pos - scene.resume.height());
            scene.resume.flip(tagLeft == 0);
        }
    }

    @Override
    protected void onBackPressed() {
        if (!cancel()) {
            add(new WndGame());
        }
    }

    @Override
    protected void onMenuPressed() {
        if (Dungeon.hero.ready) {
            selectItem(null, WndBackpack.Mode.ALL, null, null);
        }
    }

    public void brightness(int value) {
        value += 4;
        float shift;
        if (value >= 0)
            shift = value / 2f;
        else
            shift = value / 3f;

        fog.am = 1f + shift;
        fog.aa = 0f - shift;

        hallucinationOverlay.am = 1f + shift;
        hallucinationOverlay.aa = 0f - shift;
    }

    public void addCustomTile(CustomTileVisual visual) {
        customTiles.add(visual.create());
    }

    private void addHeapSprite(Heap heap) {
        ItemSprite sprite = heap.sprite = (ItemSprite) heaps.recycle(ItemSprite.class);
        sprite.revive();
        sprite.link(heap);
        heaps.add(sprite);
    }

    private void addDiscardedSprite(Heap heap) {
        heap.sprite = (DiscardedItemSprite) heaps.recycle(DiscardedItemSprite.class);
        heap.sprite.revive();
        heap.sprite.link(heap);
        heaps.add(heap.sprite);
    }

    private void addPlantSprite(Plant plant) {
        (plant.sprite = (PlantSprite) plants.recycle(PlantSprite.class)).reset(plant);
    }

    private void addTrapSprite(Trap trap) {
        (trap.sprite = (TrapSprite) traps.recycle(TrapSprite.class)).reset(trap);
        trap.sprite.visible = trap.visible;
    }

    private void addBlobSprite(final Blob gas) {
        if (gas.emitter == null) {
            gases.add(new BlobEmitter(gas));
        }
    }

    private void addMobSprite(Mob mob) {
        CharSprite sprite = mob.sprite();
        sprite.visible = mob.visibilityOverride(Dungeon.visible[mob.pos()]);
        mobs.add(sprite);
        sprite.link(mob);
    }

    private void prompt(String text) {
        if (prompt != null) {
            prompt.killAndErase();
            prompt = null;
        }

        if (text != null) {
            prompt = new Toast(text) {
                @Override
                protected void onClose() {
                    cancel();
                }
            };
            prompt.camera = uiCamera;
            prompt.setPos((uiCamera.width - prompt.width()) / 2, uiCamera.height - 60);
            add(prompt);
        }
    }

    private void showBanner(Banner banner) {
        banner.camera = uiCamera;
        banner.x = (uiCamera.width - banner.width) / 2;
        banner.y = (uiCamera.height - banner.height) / 3;
        add(banner);
    }

    // -------------------------------------------------------

    public static void add(Plant plant) {
        if (scene != null) {
            scene.addPlantSprite(plant);
        }
    }

    public static void add(Trap trap) {
        if (scene != null) {
            scene.addTrapSprite(trap);
        }
    }

    public static void add(Blob gas) {
        Actor.add(gas);
        if (scene != null) {
            scene.addBlobSprite(gas);
        }
    }

    public static void add(Heap heap) {
        if (scene != null) {
            scene.addHeapSprite(heap);
        }
    }

    public static void discard(Heap heap) {
        if (scene != null) {
            scene.addDiscardedSprite(heap);
        }
    }

    public static void add(Mob mob) {
        Dungeon.level.mobs.add(mob);
        Actor.add(mob);
        scene.addMobSprite(mob);
    }

    public static void add(Mob mob, long delay) {
        Dungeon.level.mobs.add(mob);
        Actor.addDelayed(mob, delay);
        scene.addMobSprite(mob);
    }

    public static void add(EmoIcon icon) {
        scene.emoicons.add(icon);
    }

    public static void effect(Visual effect) {
        if (scene != null) {
            scene.effects.add(effect);
        }
    }

    public static Ripple ripple(int pos) {
        Ripple ripple = (Ripple) scene.ripples.recycle(Ripple.class);
        ripple.reset(pos);
        return ripple;
    }

    public static SpellSprite spellSprite() {
        return (SpellSprite) scene.spells.recycle(SpellSprite.class);
    }

    public static Emitter emitter() {
        if (scene != null) {
            Emitter emitter = (Emitter) scene.emitters.recycle(Emitter.class);
            emitter.revive();
            return emitter;
        } else {
            return null;
        }
    }

    public static FloatingText status() {
        return scene != null ? (FloatingText) scene.statuses.recycle(FloatingText.class) : null;
    }

    public static void pickUp(Item item) {
        scene.toolbar.pickup(item);
    }

    public static void resetMap() {
        if (scene != null) {
            scene.tiles.map(Dungeon.level.map, Level.WIDTH);
        }
    }

    public static void updateMap() {
        if (scene != null) {
            scene.tiles.updated.set(0, 0, Level.WIDTH, Level.HEIGHT);
        }
    }

    public static void updateMap(int cell) {
        if (scene != null) {
            scene.tiles.updated.union(cell % Level.WIDTH, cell / Level.WIDTH);
        }
    }

    public static void discoverTile(int pos, int oldValue) {
        if (scene != null) {
            scene.tiles.discover(pos, oldValue);
        }
    }

    public static void show(Window wnd) {
        cancelCellSelector();
        scene.add(wnd);
    }

    public static void afterObserve() {
        if (scene != null) {
            scene.fog.updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);

            for (Mob mob : Dungeon.level.mobs) {
                mob.sprite.visible = mob.visibilityOverride(Dungeon.visible[mob.pos()]);
            }
        }
    }

    public static void flash(int color) {
        scene.fadeIn(0xFF000000 | color, true);
    }

    public static void gameOver() {
        Banner gameOver = new Banner(BannerSprites.get(BannerSprites.Type.GAME_OVER));
        gameOver.show(0x000000, 1f);
        scene.showBanner(gameOver);

        Sample.INSTANCE.play(Assets.SND_DEATH);
    }

    public static void bossSlain() {
        if (Dungeon.hero.isAlive()) {
            Banner bossSlain = new Banner(BannerSprites.get(BannerSprites.Type.BOSS_SLAIN));
            bossSlain.show(0xFFFFFF, 0.3f, 5f);
            scene.showBanner(bossSlain);

            Sample.INSTANCE.play(Assets.SND_BOSS);
        }
    }

    public static void handleCell(int cell) {
        GLog.d("handleCell");
        cellSelector.select(cell);
    }

    public static void selectCell(CellSelector.Listener listener) {
        cellSelector.listener = listener;
        scene.prompt(listener.prompt());
    }

    private static boolean cancelCellSelector() {
        if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
            cellSelector.cancel();
            return true;
        } else {
            return false;
        }
    }

    public static WndBackpack selectItem(WndBackpack.Listener listener, Class<?> classMatch, String title, Class<?> classUnmatch, Item... excluded) {
        return selectItem(listener, WndBackpack.Mode.INSTANCEOF, classMatch, title, classUnmatch, excluded);
    }

    public static WndBackpack selectItem(WndBackpack.Listener listener, WndBackpack.Mode mode, String title, Class<?> classUnmatch, Item... excluded) {
        return selectItem(listener, mode, null, title, classUnmatch, excluded);
    }

    public static WndBackpack selectItem(WndBackpack.Listener listener, WndBackpack.Mode mode, Class<?> classMatch, String title, Class<?> classUnmatch, Item... excluded) {
        return selectItem(listener, mode, classMatch, title, false, classUnmatch, excluded);
    }

    public static WndBackpack selectItem(WndBackpack.Listener listener, WndBackpack.Mode mode, Class<?> classMatch, String title, boolean groundNearby, Class<?> classUnmatch, Item... excluded) {
        cancelCellSelector();

        WndBackpack wnd = WndBackpack.lastBag(listener, mode, classMatch, title, groundNearby, classUnmatch, excluded);

        scene.add(wnd);

        return wnd;
    }

    static boolean cancel() {
        if (Dungeon.hero.curAction != null || Dungeon.hero.resting) {
            Dungeon.hero.curAction = null;
            Dungeon.hero.resting = false;
            return true;
        } else {
            return cancelCellSelector();
        }
    }

    public static void ready() {
        selectCell(defaultCellListener);
        QuickSlotButton.cancel();
    }

    public static void examineCell(Integer cell) {
        if (cell == null) {
            return;
        }

        if (cell < 0 || cell > Level.LENGTH || (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
            GameScene.show(new WndMessage("You don't know what is there."));
            return;
        }

        if (cell == Dungeon.hero.pos()) {
            GameScene.show(new WndHero());
            return;
        }

        if (Dungeon.visible[cell]) {
            Mob mob = (Mob) Actor.findChar(cell);
            if (mob != null) {
                GameScene.show(new WndInfoMob(mob));
                return;
            }
        }

        Heap heap = Dungeon.level.heaps.get(cell);
        if (heap != null && heap.seen) {
            if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
                GameScene.show(new WndTradeItem(heap, false));
            } else {
                GameScene.show(new WndInfoItem(heap));
            }
            return;
        }

        Plant plant = Dungeon.level.plants.get(cell);
        if (plant != null) {
            GameScene.show(new WndInfoPlant(plant));
            return;
        }

        Trap trap = Dungeon.level.traps.get(cell);
        if (trap != null && trap.visible) {
            GameScene.show(new WndInfoTrap(trap));
            return;
        }

        GameScene.show(new WndInfoCell(cell));
    }

    public static void startHallucinating() {
        scene.hallucinationOverlay.startHallucinating();
    }

    public static void stopHallucinating() {
        scene.hallucinationOverlay.stopHallucinating();
    }


    private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
        @Override
        public boolean onSelect(Integer cell) {
            if (Dungeon.hero.handle(cell, true)) {
                Dungeon.hero.next();
            }
            return true;
        }

        @Override
        public String prompt() {
            return null;
        }
    };
}
