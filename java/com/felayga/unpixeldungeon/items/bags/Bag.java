/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.items.bags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.weapon.missiles.Boomerang;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.ui.Icons;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndBag;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Bag extends Item implements Iterable<Item> {
	private static final String TXT_PACK_FULL = "Your pack is too full for the %s.";

	public static final String AC_OPEN = "OPEN";

	public Bag(Char owner) {
		this.owner = owner;

		image = 11;

		defaultAction = AC_OPEN;

		unique = true;

		priority = 16;
	}

	public Char owner;

	private ArrayList<Item> items = new ArrayList<>();

	public Icons tabIcon = null;
	public boolean locked = false;

	public int size = 1;

	//lower priority, chosen first
	public int priority;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_OPEN );
		return actions;
	}

	@Override
	public boolean execute(Hero hero, String action) {
		if (action.equals(AC_OPEN)) {
			//GameScene.show(new WndBackpack(this, null, WndBackpack.Mode.ALL, null));
			GameScene.show(new WndBag(this, null, WndBackpack.Mode.ALL, null, Constant.POS_NONE));

			return false;
		} else {
			return super.execute(hero, action);
		}
	}

	public boolean collect(Item collectItem) {
		if (this.items.contains(collectItem)) {
			return true;
		}

		/*
		for (Item item : this.items) {
			if (item instanceof Bag) {
				Bag bag = (Bag)item;

				if (bag.grab(item))
				{
					return bag.collect(item);
				}
			}
		}
		*/

		if (collectItem.stackable) {
			for (Item item : items) {
				if (collectItem.isSimilar(item)) {
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
			if (collectItem.stackable || collectItem instanceof Boomerang)
				Dungeon.quickslot.replaceSimilar(collectItem);
			collectItem.updateQuickslot();
			Collections.sort(items, collectItem.itemComparator);
			return true;

		} else {
			if (this.owner instanceof Hero) {
				GLog.n(TXT_PACK_FULL, collectItem.getDisplayName());
			}
			return false;

		}
	}

	public boolean tryMergeStack(Item test) {
		int similarities = 0;
		int index = -1;

		for (int n = 0; n < items.size(); n++) {
			Item item = items.get(n);

			if (item == test) {
				index = n;
			} else if (item.isSimilar(test)) {
				similarities++;
			}
		}

		if (similarities > 0) {
			items.remove(test);
			this.collect(test);
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
		return item;
	}

	/*
	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			
			owner = container.owner;
			
			for (Item item : container.items.toArray( new Item[container.items.size()] )) {
				if (grab( item )) {
					item.detachAll( container );
					item.collect( this );
				}
			}
			
			Badges.validateAllBagsBought( this );
			
			return true;
		} else {
			return false;
		}
	}
	*/

	protected void onItemAdded(Item item) {
		item.parent = this;
	}

	protected void onItemRemoved(Item item) {
		if (item.parent == this) {
			item.parent = null;
		}
	}

	@Override
	public void onDetach() {
		this.owner = null;
		for (Item item : items) {
			Dungeon.quickslot.clearItem(item);
		}
		updateQuickslot();
	}

	@Override
	public boolean isUpgradable() {
		return false;
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

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ITEMS, items);
		bundle.put(LOCKED, locked);
	}

	public void storeInBundle(Bundle bundle, String name) {
		bundle.put(name, items);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		locked = bundle.getBoolean(LOCKED);

		for (Bundlable item : bundle.getCollection(ITEMS)) {
			if (item != null) this.collect((Item) item);
		}
	}

	public boolean contains(Item item) {
		for (Item i : items) {
			if (i == item) {
				return true;
			} else if (i instanceof Bag && ((Bag) i).contains(item)) {
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
		return new ItemIterator(allowNested);
	}

	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		private Iterator<Item> nested = null;
		private boolean allowNested;

		public ItemIterator(boolean allowNested) {
			this.allowNested = allowNested;
		}

		@Override
		public boolean hasNext() {
			if (nested != null) {
				return nested.hasNext() || index < items.size();
			} else {
				return index < items.size();
			}
		}

		@Override
		public Item next() {
			if (nested != null && nested.hasNext()) {

				return nested.next();

			} else {

				nested = null;

				Item item = items.get(index++);

				if (allowNested && item instanceof Bag) {
					nested = ((Bag) item).iterator();
				}

				return item;
			}
		}

		@Override
		public void remove() {
			if (nested != null) {
				nested.remove();
			} else {
				index--;
				onItemRemoved(items.get(index));
				items.remove(index);
			}
		}
	}
}
