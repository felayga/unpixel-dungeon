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
package com.felayga.unpixeldungeon.actors.mobs.npcs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.ElmoParticle;
import com.felayga.unpixeldungeon.items.Gemstone;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.potions.PotionOfFullHealing;
import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.items.tools.unlocking.SkeletonKey;
import com.felayga.unpixeldungeon.items.wands.WandOfBlastWave;
import com.felayga.unpixeldungeon.items.wands.WandOfMagicMissile;
import com.felayga.unpixeldungeon.items.weapon.ammunition.simple.Rock;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.shopkeeper.ShopkeeperSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndTradeItem;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Shopkeeper extends NPC {
    public static class Registry {
        private static final SparseArray<Shopkeeper> fromIndex = new SparseArray<>();

        private static final String SHOPKEEPERREGISTRYINDEX = "shopkeeperRegistryIndex";

        private static int index = 0;

        public static void save(Bundle bundle) {
            bundle.put(SHOPKEEPERREGISTRYINDEX, index);
        }

        public static void restore(Bundle bundle) {
            index = bundle.getInt(SHOPKEEPERREGISTRYINDEX);
        }

        public static void add(Shopkeeper shopkeeper) {
            if (shopkeeper.shopkeeperRegistryIndex != -1) {
                GLog.d("tried to register shopkeeper, already has index="+shopkeeper.shopkeeperRegistryIndex);
                GLog.d(""+1/0);
            }

            shopkeeper.shopkeeperRegistryIndex = index;
            index++;

            fromIndex.put(shopkeeper.shopkeeperRegistryIndex, shopkeeper);
        }

        public static Shopkeeper get(int registryIndex) {
            return fromIndex.get(registryIndex);
        }

        public static void register(Level level) {
            for (Mob mob: level.mobs) {
                if (mob instanceof Shopkeeper) {
                    Shopkeeper shopkeeper = (Shopkeeper)mob;

                    if (shopkeeper.shopkeeperRegistryIndex != -1) {
                        fromIndex.put(shopkeeper.shopkeeperRegistryIndex, shopkeeper);
                    } else {
                        GLog.d("TRIED TO REGISTER UNKNOWN SHOPKEEPER FOUND AT POS="+shopkeeper.pos());
                    }
                }
            }
        }

        public static void unregister(Level level) {
            for (Mob mob : level.mobs) {
                if (mob instanceof Shopkeeper) {
                    Shopkeeper shopkeeper = (Shopkeeper)mob;

                    if (shopkeeper.shopkeeperRegistryIndex != -1) {
                        fromIndex.remove(shopkeeper.shopkeeperRegistryIndex);
                    } else {
                        GLog.d("TRIED TO UNREGISTER UNKNOWN SHOPKEEPER FOUND AT POS="+shopkeeper.pos());
                    }
                }
            }
        }
    }

    @Override
    protected String storeAIState() {
        if (state == STOREMANAGER) {
            return StoreManager.TAG;
        }

        return super.storeAIState();
    }

    public AiState restoreAIState(String state) {
        if (state.equals(StoreManager.TAG)) {
            return STOREMANAGER;
        }

        return super.restoreAIState(state);
    }


    public static final String TXT_THIEF = "Thief, Thief!";

    private int shopkeeperRegistryIndex = -1;
    public int shopkeeperRegistryIndex() { return shopkeeperRegistryIndex; }

	public Shopkeeper()
	{
		super(12);

		name = "shopkeeper";
		spriteClass = ShopkeeperSprite.class;

        movementSpeed(GameTime.TICK * 2 / 3);
        attackSpeed(GameTime.TICK);
        defenseMundane = 20;
        defenseMagical = 3;
        weight = Encumbrance.UNIT * 1450;
        nutrition = 400;
        resistanceMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.None.value;
        characteristics = Characteristic.value(Characteristic.Humanoid, Characteristic.Omnivore, Characteristic.WarmBlooded);

        belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 4, 16));
        belongings.collect(new Gold(1000 + 30 * Random.IntRange(1,100)));
        belongings.collect(new SkeletonKey());

        int extras = Random.IntRange(1,4);

        if (extras > 0) {
            belongings.collect(new WandOfBlastWave().random());
            extras--;
        }
        if (extras > 0) {
            belongings.collect(new PotionOfHealing().random());
            extras--;
        }
        if (extras > 0) {
            belongings.collect(new PotionOfFullHealing().random());
            extras--;
        }
        if (extras > 0) {
            belongings.collect(new WandOfMagicMissile().random());
            extras--;
        }

        state = STOREMANAGER;
	}

    private static final String SHOPKEEPERREGISTRYINDEX = "shopkeeperRegistryIndex";
    private static final String DOORWAYPOS = "doorwayPos";
    private static final String DOORPOS = "doorPos";
    private static final String WALLPOS = "wallPos";
    private static final String FIDGETPOS = "fidgetPos";
    private static final String MOVEFREQUENCY = "moveFrequency";
    private static final String AUTOREPAIR = "autoRepair";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        int[] _wallPos = new int[wallPos.size()];
        int n = 0;
        for (Integer pos : wallPos) {
            _wallPos[n] = pos;
            n++;
        }

        int[] _fidgetPos = new int[fidgetPos.size()];
        n = 0;
        for (Integer pos : fidgetPos) {
            _fidgetPos[n] = pos;
            n++;
        }

        int[] autoRepairKey = new int[autoRepair.size()];
        long[] autoRepairValue = new long[autoRepair.size()];

        for (n=0;n<autoRepair.size();n++) {
            autoRepairKey[n] = autoRepair.keyAt(n);
            autoRepairValue[n] = autoRepair.valueAt(n).value;
            n++;
        }

        bundle.put(SHOPKEEPERREGISTRYINDEX, shopkeeperRegistryIndex);
        bundle.put(DOORWAYPOS, doorwayPos);
        bundle.put(DOORPOS, doorPos);
        bundle.put(WALLPOS, _wallPos);
        bundle.put(FIDGETPOS, _fidgetPos);
        bundle.put(AUTOREPAIR + "Key", autoRepairKey);
        bundle.put(AUTOREPAIR + "Value", autoRepairValue);
        bundle.put(MOVEFREQUENCY, moveFrequency);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        shopkeeperRegistryIndex = bundle.getInt(SHOPKEEPERREGISTRYINDEX);
        doorwayPos = bundle.getInt(DOORWAYPOS);
        doorPos = bundle.getInt(DOORPOS);
        int[] _wallPos = bundle.getIntArray(WALLPOS);
        int[] _fidgetPos = bundle.getIntArray(FIDGETPOS);
        moveFrequency = bundle.getInt(MOVEFREQUENCY);
        int[] autoRepairKey = bundle.getIntArray(AUTOREPAIR + "Key");
        long[] autoRepairValue = bundle.getLongArray(AUTOREPAIR + "Value");

        wallPos = new HashSet<>();
        for (int n = 0; n < _wallPos.length; n++) {
            wallPos.add(_wallPos[n]);
        }

        fidgetPos = new HashSet<>();
        for (int n = 0; n < _fidgetPos.length; n++) {
            fidgetPos.add(_fidgetPos[n]);
        }

        autoRepair = new SparseArray<>();
        for (int n = 0; n < autoRepairKey.length; n++) {
            autoRepair.put(autoRepairKey[n], new LongWrapper(autoRepairValue[n]));
        }
    }

    public static int appraise(Item item, boolean playerBuying) {
        int value = item.price();

        if (playerBuying) {
            if (value < 5) {
                value = 5;
            }

            if (item instanceof Gemstone) {
                if (item instanceof Gemstone.Glass && !item.isIdentified()) {
                    Gemstone.Glass glass = (Gemstone.Glass)item;

                    value = glass.shopkeeperPriceJacking();
                }
            } else {
                if (item.shopkeeperPriceJacked) {
                    value = (value * 4) / 3;
                }
            }

            int dexcha = Dungeon.hero.DEXCHA();
            if (dexcha >= 19) {
                value /= 2;
            } else if (dexcha >= 18) {
                value = (value * 2) / 3;
            } else if (dexcha >= 16) {
                value = (value * 3) / 4;
            } else if (dexcha >= 11) {
                //nothing
            } else if (dexcha >= 8) {
                value = (value * 4) / 3;
            } else if (dexcha >= 6) {
                value = (value * 3) / 2;
            } else {
                value *= 2;
            }

            //todo: angry shopkeeper pricejacking

        } else {
            if (item instanceof Gemstone) {
                if (!item.isIdentified()) {
                    if (item instanceof Gemstone.Glass) {
                        Gemstone.Glass glass = (Gemstone.Glass)item;

                        value = glass.shopkeeperPriceJacking();
                    }

                    value = (value % 7) + 3;
                } else {
                    value /= 2;
                }
            } else {
                if (Random.Int(4) == 0) {
                    value = (value * 3) / 4;
                }

                value /= 2;
            }
        }

        value *= item.quantity();

        return value;
    }

    private int doorwayPos;
    private int doorPos;
    private HashSet<Integer> wallPos;
    private HashSet<Integer> fidgetPos;
    private SparseArray<LongWrapper> autoRepair;
    private int moveFrequency = 0;

    private static class LongWrapper {
        public long value;

        public LongWrapper(long value) {
            this.value = value;
        }
    }

    public void setRoom(int doorwayPos, int doorPos, List<Integer> wallPos, List<Integer> fidgetPos) {
        this.doorwayPos = doorwayPos;
        this.doorPos = doorPos;

        this.wallPos = new HashSet<>();
        for (Integer pos : wallPos) {
            this.wallPos.add(pos);
        }

        this.fidgetPos = new HashSet<>();
        for(Integer pos : fidgetPos) {
            this.fidgetPos.add(pos);
        }

        this.autoRepair = new SparseArray<>();
    }

    @Override
	protected boolean act() {
		//throwItem();
        //removed flee-if-not-in-original-position crap

        long oldTime = getTime();
        boolean retval = super.act();

        tryRepairShop(getTime() - oldTime);

        return retval;
	}

    private void tryRepairShop(long timeChange) {
        List<Integer> pendingRemoval = new ArrayList<>();

        for (int n=0;n<autoRepair.size();n++) {
            int key = autoRepair.keyAt(n);
            LongWrapper value = autoRepair.valueAt(n);

            value.value -= timeChange;
            if (value.value <= 0) {
                value.value = 0;

                Mob mob = Dungeon.level.findMob(key);

                if (mob != null || key == Dungeon.hero.pos()) {
                    if (mob instanceof Boulder) {
                        mob.die(null);
                    } else {
                        continue;
                    }
                }

                pendingRemoval.add(key);

                Heap test = Dungeon.level.heaps.get(key);

                if (test != null) {
                    Iterator<Item> subIterator = test.iterator(false);

                    while (subIterator.hasNext()) {
                        Item item = subIterator.next();

                        if (item instanceof Rock) {
                            subIterator.remove();
                        }
                    }
                }

                if (key == doorPos) {
                    Dungeon.level.set(key, Terrain.DOOR, true);
                } else {
                    Dungeon.level.set(key, Terrain.WALL, true);
                }
                GameScene.updateMap(key);

                if (test != null) {
                    test.contentsScatter();
                }
            }
        }

        for (int n = 0; n < pendingRemoval.size(); n++) {
            autoRepair.remove(pendingRemoval.get(n));
        }

        for (Integer pos : wallPos) {
            if (Dungeon.level.map(pos) != Terrain.WALL) {
                if (autoRepair.get(pos) == null) {
                    autoRepair.put(pos, new LongWrapper(5 * GameTime.TICK));
                }
            }
        }

        switch (Dungeon.level.map(doorPos)) {
            case Terrain.DOOR:
            case Terrain.SECRET_DOOR:
            case Terrain.LOCKED_DOOR:
            case Terrain.SECRET_LOCKED_DOOR:
            case Terrain.OPEN_DOOR:
                //nothing
                break;
            default:
                if (autoRepair.get(doorPos) == null) {
                    autoRepair.put(doorPos, new LongWrapper(5 * GameTime.TICK));
                }
                break;
        }
    }

	//todo: shopkeeper ain't no pansy
	@Override
	public int damage( int dmg, MagicType type, Char source, Item sourceItem ) {
		flee();
        return 0;
	}
	
	@Override
	public void add( Buff buff ) {
		flee();
	}
	
	public void flee() {
		for (Heap heap: Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.FOR_SALE && heap.owner() == this) {
				CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
				heap.destroy();
			}
		}
		
		destroy(null);
		
		sprite.killAndErase();
		CellEmitter.get( pos() ).burst( ElmoParticle.FACTORY, 6 );
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public String description() {
		return
			"This stout guy looks more appropriate for a trade district in some large city " +
			"than for a dungeon. His prices explain why he prefers to do business here.";
	}

    public static Shopkeeper currentShopkeeper;
	
	public static WndBackpack sell(Shopkeeper shopkeeper) {
        currentShopkeeper = shopkeeper;

		return GameScene.selectItem( itemSelector, WndBackpack.Mode.FOR_SALE, "Select an item to sell", null );
	}
	
	private static WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBackpack parentWnd = sell(currentShopkeeper);
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public void interact() {
		sell(this);
	}


    public AiState STOREMANAGER = new StoreManager();

    protected class StoreManager implements AiState {
        public static final String TAG	= "STOREMANAGER";

        @Override
        public boolean act( boolean enemyVisible, boolean enemyAudible, boolean enemyTouchable, boolean justAlerted ) {
            if (Level.fieldOfView[Dungeon.hero.pos()]) {
                boolean forceMove = false;

                if (fidgetPos.contains(Dungeon.hero.pos())) {
                    Iterator<Item> iterator = Dungeon.hero.belongings.iterator(true, true, true);
                    while (iterator.hasNext()) {
                        Item test = iterator.next();

                        if (test.shopkeeperRegistryIndex() == shopkeeperRegistryIndex) {
                            if (pos() != doorwayPos) {
                                sprite.move(pos(), doorwayPos);
                                move(doorwayPos);
                                spend_new(movementSpeed(), false);
                                return true;
                            } else {
                                moveFrequency = 5;
                            }
                        }
                    }

                    if (moveFrequency != 5 && pos() == doorwayPos) {
                        forceMove = true;
                    }
                }

                if (forceMove || (moveFrequency <= 0 && Random.Int(4) == 0)) {
                    moveFrequency = 4;
                    List<Integer> movePos = new ArrayList<>();

                    for (int offset : Level.NEIGHBOURS8) {
                        int subPos = pos() + offset;

                        if (fidgetPos.contains(subPos) && Dungeon.hero.pos() != subPos && Dungeon.level.findMob(subPos) == null) {
                            movePos.add(subPos);
                        }
                    }

                    if (movePos.size() > 0) {
                        int index = Random.Int(movePos.size());

                        int destination = movePos.get(index);

                        sprite.move(pos(), destination);
                        move(destination);
                        spend_new(movementSpeed(), false);
                    }

                    return true;
                } else {
                    moveFrequency--;
                }
            } else {
                moveFrequency = 4;
            }

            sprite.idle();
            sprite.turnTo(pos(), Dungeon.hero.pos());
            spend_new(movementSpeed(), false);

            return true;
        }

        @Override
        public String status() {
            return Utils.format("This %s is managing a store", name);
        }
    }
}
