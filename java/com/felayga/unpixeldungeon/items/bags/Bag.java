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
package com.felayga.unpixeldungeon.items.bags;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.consumable.potions.Potion;
import com.felayga.unpixeldungeon.items.consumable.wands.Wand;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.martial.Boomerang;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.ui.Icons;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndBag;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Bag extends Item implements Iterable<Item>, IBag {
	private static final String TXT_PACK_FULL = "Your pack is too full for the %s.";

	public static final String AC_OPEN = "OPEN";

    protected int baseWeight;
    public int baseWeight() {
        return baseWeight;
    }

	public Bag(Char owner, boolean lockable, int weight) {
		this.owner = owner;

        pickupSound = Assets.SND_ITEM_BAG;

        image = 11;

		defaultAction = AC_OPEN;

		this.locked = false;
        this.lockable = lockable;
        hasLevels(false);

		priority = 16;

        this.baseWeight = weight;
		weight(weight);
	}

    public void contentsImpact(boolean verbose) {
        Iterator<Item> iterator = iterator(false);

        List<Item> pendingremoval = new ArrayList<>();

        int shatter = 0;
        int crack = 0;

        while (iterator.hasNext()) {
            Item item = iterator.next();

            if (item instanceof IBag) {
                IBag bag = (IBag) item;
                bag.contentsImpact(verbose);
            } else if (item instanceof Potion) {
                int count = item.quantity();

                while (count > 0) {
                    if (Random.Int(Constant.Chance.ITEM_DESTROYED) != 0) {
                        pendingremoval.add(item);
                        shatter++;
                    }
                    count--;
                }
            } else if (item instanceof Wand) {
                if (Random.Int(Constant.Chance.ITEM_DESTROYED) != 0) {
                    pendingremoval.add(item);
                    crack++;
                }
            }
        }

        if (pendingremoval.size() > 0) {
            if (verbose) {
                if (shatter > 0 && crack > 0) {
                    GLog.n("You hear muffled breaking noises.");
                }
                else if (shatter > 1) {
                    GLog.n("You hear muffled shattering noises.");
                } else if (shatter > 0) {
                    GLog.n("You hear a muffled shatter.");
                } else if (crack > 1) {
                    GLog.n("You hear muffled cracking noises.");
                }
                else if (crack > 0){
                    GLog.n("You hear a muffled crack.");
                }
            }

            for (Item item : pendingremoval) {
                this.remove(item, 1);
            }
        }
    }

    public int pos() { return Constant.Position.NONE; }
    public String action() { return null; }
    public Char owner() { return owner; }

	public Char owner;

	private ArrayList<Item> items = new ArrayList<>();

	public Icons tabIcon = null;

    public Icons tabIcon() {
        return tabIcon;
    }

	private boolean locked;
    private boolean lockable;

    public boolean locked() { return locked; }
    public void locked(boolean value) {
        if (locked == value || (value && !lockable)) {
            return;
        }

        locked = value;
        onLockedChanged();
        updateQuickslot();
    }

    protected void onLockedChanged() {
    }

	public int size = 1;

    public int size() {
        return size;
    }

	//lower priority, chosen first
	public int priority;

	public void onWeightChanged(int change) {
		weight(weight() + change);
	}

    public long decay() {
        return 0;
    }

    @Override
    public boolean decay(long currentTime, boolean updateTime, boolean fixTime) {
        boolean updated = false;
        Iterator<Item> iterator = iterator(false);

        while (iterator.hasNext()) {
            Item item = iterator.next();

            if (item instanceof IDecayable) {
                IDecayable decayable = (IDecayable) item;
                if (decayable.decay(currentTime, updateTime, fixTime)) {
                    iterator.remove();
                    updated = true;
                }
            }
        }

        return updated;
    }

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_OPEN);
		return actions;
	}

    @Override
    public boolean execute(Hero hero) {
        //defaultAction usage override to facilitate bags not closing to whole inventory from QuickSlotButton usage

        if (defaultAction.equals(AC_OPEN)) {
            executeOpenBag(false);

            return false;
        } else {
            return execute(hero, defaultAction);
        }
    }

	@Override
	public boolean execute(Hero hero, String action) {
		if (action.equals(AC_OPEN)) {
            executeOpenBag(true);

			return false;
		} else {
			return super.execute(hero, action);
		}
	}

    private void executeOpenBag(boolean closeToBackpack) {
        if (locked) {

        } else {
            if (parent() instanceof Heap) {
                Heap heap = (Heap) parent();
                GameScene.show(new WndBag(this, null, WndBackpack.Mode.ALL, null, null, heap.pos(), closeToBackpack));
            } else {
                GameScene.show(new WndBag(this, null, WndBackpack.Mode.ALL, null, null, Constant.Position.NONE, closeToBackpack));
            }
        }
    }

	public boolean collect(Item collectItem) {
		if (this.items.contains(collectItem)) {
			return true;
		}

		if (collectItem.stackable) {
			for (Item item : items) {
				if (collectItem.isStackableWith(item)) {
					item.quantity(item.quantity() + collectItem.quantity());
                    item.updateQuickslot();

					return true;
				}
			}
		}

		if (items.size() < this.size) {
			if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
				Badges.validateItemLevelAquired(collectItem);
			}

			items.add(collectItem);
			onItemAdded(collectItem);

            if (this.owner == Dungeon.hero) {
                if (collectItem.stackable || collectItem instanceof Boomerang) {
                    Dungeon.quickslot.replaceSimilar(collectItem);
                }
                collectItem.updateQuickslot();
            }
			Collections.sort(items, collectItem.itemComparator);
			return true;
		} else {
			if (this.owner == Dungeon.hero) {
				GLog.n(TXT_PACK_FULL, collectItem.getDisplayName());
			}
			return false;
		}
	}

	public boolean tryMergeExistingStack(Item test) {
		int similarities = 0;
		int index = -1;

		for (int n = 0; n < items.size(); n++) {
			Item item = items.get(n);

			if (item == test) {
				index = n;
			} else if (item.isStackableWith(test)) {
				similarities++;
			}
		}

		if (similarities > 0) {
			remove(test);
			collect(test);
			return true;
		}

		return false;
	}

	public Item remove(Item item) {
		if (!this.items.contains(item)) {
			return null;
		}

        onItemRemoved(item);
        this.items.remove(item);
        item.updateQuickslot();
		return item;
	}

	public Item remove(Item item, int quantity) {
		if (item.quantity() > quantity) {
            return Item.ghettoSplitStack(item, quantity, owner);
		}
		else if (item.quantity() == quantity) {
			return remove(item);
		}

		return null;
	}

	protected void onItemAdded(Item item) {
		item.parent(this);
		onWeightChanged(item.weight() * item.quantity());
	}

	protected void onItemRemoved(Item item) {
		onWeightChanged(-item.weight() * item.quantity());

        this.parent().onNestedItemRemoved(item);
	}

    public void onNestedItemRemoved(Item item) {
        this.parent().onNestedItemRemoved(item);
    }

	@Override
	public void onDetach() {
        Char oldOwner = this.owner;
		this.owner = null;

		for (Item item : items) {
			Dungeon.quickslot.clearItem(item);
		}

        if (oldOwner instanceof Hero) {
            updateQuickslot();
        }
	}

    @Override
    public boolean canPerformActionExternally(Hero hero, String action) {
        if (action.equals(AC_OPEN)) {
            return true;
        }

        return super.canPerformActionExternally(hero, action);
    }

	@Override
	public boolean isIdentified() {
		return true;
	}

	public void clear() {
		for (int n = 0; n < items.size(); n++) {
			onItemRemoved(items.get(n));
		}
		items.clear();
	}

	private static final String ITEMS = "inventory";
	private static final String LOCKED = "locked";
    private static final String BASEWEIGHT = "baseWeight";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ITEMS, items);
		bundle.put(LOCKED, locked);
        bundle.put(BASEWEIGHT, baseWeight);
	}

	public void storeInBundle(Bundle bundle, String name) {
		bundle.put(name, items);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		locked = bundle.getBoolean(LOCKED);
        onLockedChanged();
        baseWeight = bundle.getInt(BASEWEIGHT);

        weight(baseWeight);

		for (Bundlable item : bundle.getCollection(ITEMS)) {
			if (item != null) this.collect((Item) item);
		}
	}

	public boolean contains(Item item) {
		for (Item i : items) {
			if (i == item) {
				return true;
			} else if (i instanceof IBag && ((IBag) i).contains(item)) {
				return true;
			}
		}
		return false;
	}

    public boolean contains(Class<?> type, boolean allowNested) {
        for (Item i: items) {
            if (type.isAssignableFrom(i.getClass())) {
                return true;
            } else if (allowNested && i instanceof IBag && ((IBag)i).contains(type, allowNested)) {
                return true;
            }
        }
        return false;
    }

	public boolean grab(Item item) {
		return false;
	}

	public Item randomItem() {
		return Random.element(items);
	}

	@Override
	public Iterator<Item> iterator() {
		return iterator(true);
	}

	public Iterator<Item> iterator(boolean allowNested) {
		return new ItemIterator(this, items, allowNested);
	}

}
