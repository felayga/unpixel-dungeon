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

package com.felayga.unpixeldungeon.items.tools;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.positive.Light;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.bags.backpack.Backpack;
import com.felayga.unpixeldungeon.items.bags.backpack.Belongings;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Torch extends Tool implements IDecayable {
    public static class Registry {
        private static final int BITOFFSET = 4;

        private static Torch[] occupado = new Torch[12 + 8];

        public static Torch get(int registryFlag) {
            int index = lazylog2(registryFlag) - BITOFFSET;

            if (index < 0 || index > occupado.length) {
                return null;
            }

            return occupado[index];
        }

        public static void register(Torch what) {
            if (what.registryFlag != 0) {
                int index = lazylog2(what.registryFlag) - BITOFFSET;
                GLog.d("tried to register already-registered torch=" + index);
                if (occupado[index] == null) {
                    GLog.d("not occupied, let it have it");
                    occupado[index] = what;
                }

                return;
            }

            int index = -1;
            for (int n = 0; n < occupado.length; n++) {
                if (occupado[n] == null) {
                    index = n;
                    break;
                }
            }

            if (index < 0) {
                GLog.d("tried to register too many torches, failing");
                GLog.d("" + 1 / 0);
            } else {
                GLog.d("register torch with index=" + index);
            }

            occupado[index] = what;

            what.registryFlag = 1 << (index + BITOFFSET);
        }

        public static void unregister(Torch what) {
            if (what.registryFlag == 0) {
                GLog.d("tried to unregister not-registered torch");
                return;
            }

            int index = lazylog2(what.registryFlag) - BITOFFSET;

            if (occupado[index] != what) {
                GLog.d("tried to unregister improperly registered torch (expected torch with index=" + index + ", " + "found index=" + (occupado[index] != null ? (lazylog2(occupado[index].registryFlag) - BITOFFSET) + "" : "<null>"));
                return;
            }
            occupado[index] = null;

            GLog.d("unregister torch with index=" + index);

            what.registryFlag = 0;
        }

        private static int lazylog2(int value) {
            return (31 - Integer.numberOfLeadingZeros(value));
        }

        public static void register(Level level) {
            for (Mob mob : level.mobs) {
                register(level, mob.belongings, mob);
            }

            for (int n=0;n<level.heaps.size();n++) {
                Heap heap = level.heaps.valueAt(n);
                register(level, heap, null);
            }

            register(level, Dungeon.hero.belongings, Dungeon.hero);
        }

        private static void register(Level level, IBag container, Char owner) {
            Iterator<Item> iterator = container.iterator(false);
            while (iterator.hasNext()) {
                Item item = iterator.next();

                if (item instanceof Torch) {
                    Torch torch = (Torch)item;

                    if (torch.ignited) {
                        Registry.register(torch);
                        torch.attachBuff(level, torch.parent(), owner);
                    }
                }
            }
        }

        public static void unregister(Level level) {
            for (Mob mob : level.mobs) {
                unregister(level, mob.belongings, mob);
            }

            for (int n=0;n<level.heaps.size();n++) {
                Heap heap = level.heaps.valueAt(n);
                unregister(level, heap, null);
            }

            unregister(level, Dungeon.hero.belongings, Dungeon.hero);
        }

        private static void unregister(Level level, IBag container, Char owner) {
            Iterator<Item> iterator = container.iterator(false);
            while (iterator.hasNext()) {
                Item item = iterator.next();

                if (item instanceof Torch) {
                    Torch torch = (Torch)item;

                    if (torch.ignited) {
                        torch.detachBuff(level, torch.parent(), owner);
                        Registry.unregister(torch);
                    }
                }
            }
        }

    }

    List<Integer> lightUndoList = new ArrayList<>();

    protected long maxDecay;
    protected long decay;
    protected long decayTime;
    protected long tileIgnition;
    protected boolean ignited;
    protected int registryFlag;
    protected int distance;

    protected boolean ignitesTiles;

    public long decay() {
        return decay;
    }

    public boolean decay(long currentTime, boolean updateTime, boolean fixTime) {
        if (!ignited) {
            updateTime = false;
            fixTime = false;
        }

        if (fixTime || updateTime) {
            long newAmount = currentTime - decayTime;
            if (fixTime) {
                decayTime = 0;
            } else {
                decayTime = currentTime;
            }
            currentTime = newAmount;
        } else {
            decayTime = currentTime;
            currentTime = 0;
        }

        decay -= currentTime;

        if (ignitesTiles) {
            tileIgnition += currentTime;

            if (tileIgnition >= GameTime.TICK) {
                IBag parent = parent();
                boolean burnt = false;

                while (tileIgnition >= GameTime.TICK) {
                    if (!burnt && ignited && parent instanceof Heap && Random.Int(Constant.Chance.TORCH_IGNITE_TILE) == 0) {
                        int pos = parent.pos();

                        if (Level.burnable[pos]) {
                            burnt = true;
                            GameScene.add(Blob.seed(null, pos, 2, Fire.class));
                        }
                    }

                    tileIgnition -= GameTime.TICK;
                }
            }
        }

        if (decay <= 0) {
            if (ignited) {
                return burnout();
            }
        }

        return false;
    }

    protected boolean burnout() {
        IBag parent = parent();
        Char owner = parent.owner();

        extinguish(parent, owner);

        return true;
    }

	public static final long TIME_TO_LIGHT = GameTime.TICK;

    public Torch() {
        this(800 * GameTime.TICK);
        material = Material.Wood;

        hasBuc(false);

        price = 8;
        weight(20 * Encumbrance.UNIT);
    }

    public Torch(long maxLife)
	{
        super(false, false);
        maxDecay = maxLife;

		name = "torch";
		image = ItemSpriteSheet.TORCH;
		
		stackable = false;
        hasLevels(false);

        distance = 4;
        ignited = false;
        ignitesTiles = true;

        price = 10;

        defaultAction = Constant.Action.APPLY;
    }

    @Override
    public Item random() {
        super.random();

        decay = Random.LongRange(maxDecay * 3 / 4, maxDecay);

        return this;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Constant.Action.APPLY);
        return actions;
    }

    @Override
    public String getToolClass() {
        return "light source";
    }

    public Emitter emitter() {
        if (ignited) {
            Emitter emitter = new Emitter();

            emitter.pour(new Emitter.Factory() {
                @Override
                public void emit(Emitter emitter, int index, float x, float y) {
                    Visual target = emitter.target();
                    PointF scale = target.scale();

                    FlameParticle p = ((FlameParticle) emitter.recycle(FlameParticle.class));
                    float xrand = target.width * 2.0f / 16.0f * scale.x;
                    float yrand = target.height * 2.0f / 16.0f * scale.y;
                    x += target.width * 4.0f / 16.0f * scale.x;
                    y += target.height * 5.0f / 16.0f * scale.y;

                    if (Random.Int(2) == 0) {
                        x += Random.Float(xrand);
                    } else {
                        x -= Random.Float(xrand);
                    }

                    if (Random.Int(2) == 0) {
                        y += Random.Float(yrand);
                    } else {
                        y -= Random.Float(yrand);
                    }

                    p.reset(x, y);
                }
            }, 0.1f);
            emitter.fillTarget = false;

            return emitter;
        }

        return null;
    }


    @Override
    public void parent(IBag parent) {
        if (ignited) {
            boolean updateLightMap = false;

            IBag oldParent = this.parent();
            Char oldOwner = null;

            if (oldParent != null) {
                oldOwner = oldParent.owner();
            }

            super.parent(parent);

            Char owner = null;
            if (parent != null) {
                owner = parent.owner();
            }

            boolean keeplit = true;
            if (parent != null) {
                if (parent instanceof Backpack || parent instanceof Belongings || parent instanceof Heap) {
                    //nothing
                } else {
                    //nested in bag, extinguish
                    keeplit = false;
                }
            }

            if (oldOwner != owner) {
                updateLightMap |= detachBuff(Dungeon.level, oldParent, oldOwner);
                if (keeplit) {
                    updateLightMap |= attachBuff(Dungeon.level, parent, owner);
                }
            }

            if (!keeplit) {
                updateLightMap |= extinguish(parent, owner);
            }

            if (updateLightMap) {
                Dungeon.level.updateLightMap();
                Dungeon.observe();
            }

        } else {
            super.parent(parent);
        }
    }

    private static final String DECAY = "decay";
    private static final String DECAYTIME = "decayTime";
    private static final String IGNITED = "ignited";
    private static final String REGISTRYFLAG = "registryFlag";
    private static final String DISTANCE = "distance";
    private static final String LIGHTUNDOLIST = "lightUndoList";
    private static final String TILEIGNITION = "tileIgnition";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(DECAY, decay);
        bundle.put(DECAYTIME, decayTime);
        bundle.put(IGNITED, ignited);
        bundle.put(REGISTRYFLAG, registryFlag);
        bundle.put(DISTANCE, distance);

        int[] undo = new int[lightUndoList.size()];
        for (int n=0;n<lightUndoList.size();n++) {
            undo[n] = lightUndoList.get(n);
        }
        bundle.put(LIGHTUNDOLIST, undo);

        bundle.put(TILEIGNITION, tileIgnition);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        decay = bundle.getLong(DECAY);
        decayTime = bundle.getLong(DECAYTIME);
        ignited = bundle.getBoolean(IGNITED);
        registryFlag = bundle.getInt(REGISTRYFLAG);
        distance = bundle.getInt(DISTANCE);

        int[] undo = bundle.getIntArray(LIGHTUNDOLIST);
        lightUndoList.clear();
        for (int n=0;n<undo.length;n++) {
            lightUndoList.add(undo[n]);
        }

        tileIgnition = bundle.getLong(TILEIGNITION);
    }

    public void apply(Hero hero, int target) {
        //nothing
    }

    public void apply(Hero hero, Item target) {
        //nothing
    }

    @Override
    public void apply(Hero hero) {
        hero.spend_new(TIME_TO_LIGHT, false);
        hero.busy();

        hero.sprite.operate(hero.pos());

        if (ignited) {
            extinguish(parent(), hero);
        } else {
            light(parent(), hero);
        }
    }

    private void light(IBag parent, Char owner) {
        ignited = true;

        Registry.register(this);
        attachBuff(Dungeon.level, parent, owner);

        Emitter emitter = owner.sprite.centerEmitter(-1);
        emitter.start(FlameParticle.FACTORY, 0.2f, 3);

        updateQuickslot();
    }

    private boolean attachBuff(Level level, IBag parent, Char owner) {
        if (owner != null) {
            Light light = null;
            for (Buff buff : owner.buffs()) {
                if (buff instanceof Light) {
                    Light subLight = (Light) buff;
                    if (subLight.registryFlag == registryFlag) {
                        light = subLight;
                        break;
                    }
                }
            }

            if (light == null) {
                light = Buff.append(owner, owner, Light.class);
            }
            light.ignite(level, distance, registryFlag);

            return false;
        }
        else if (parent != null) {
            Light.handleArea(parent.pos(), distance, registryFlag, lightUndoList, level, true, true);

            return true;
        }

        return false;
    }

    private boolean extinguish(IBag parent, Char owner) {
        ignited = false;

        boolean retval = detachBuff(Dungeon.level, parent, owner);

        Registry.unregister(this);

        updateQuickslot();

        return retval;
    }

    private boolean detachBuff(Level level, IBag parent, Char owner) {
        if (owner != null) {
            for (Buff buff : owner.buffs()) {
                if (buff instanceof Light) {
                    Light light = (Light) buff;
                    if (light.registryFlag == registryFlag) {
                        light.detach(level);
                        return false;
                    }
                }
            }

            return true;
        } else if (parent != null) {
            Light.handleArea(parent.pos(), distance, registryFlag, lightUndoList, level, false, true);

            return true;
        }

        return false;
    }
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
        String retval = "An adventuring staple, when a dungeon goes dark, a torch can help lead the way.";

        float amount = (float) decay / (float) maxDecay;

        if (amount >= 0.8) {
            retval += "\n\nLooks like it's hardly been used.";
        } else if (amount >= 0.6) {
            retval += "\n\nIt's been used, but it should last a while.";
        } else if (amount >= 0.4) {
            retval += "\n\nAround half of the flammable material has been used up.";
        } else if (amount >= 0.2) {
            retval += "\n\nMost of the flammable material has been used up.";
        } else {
            retval += "\n\nThere's not much left to burn.  It'll extinguish itself soon.";
        }

        if (ignited) {
            retval += "\n\nThis torch is burning.";
        }

		return retval;
	}
}
