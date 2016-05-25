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
package com.felayga.unpixeldungeon.actors.hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.tools.ITool;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.backpack.ConsumablesBackpack;
import com.felayga.unpixeldungeon.items.bags.backpack.Spellbook;
import com.felayga.unpixeldungeon.items.bags.backpack.UncategorizedBackpack;
import com.felayga.unpixeldungeon.items.bags.backpack.EquipmentBackpack;
import com.felayga.unpixeldungeon.items.keys.IronOldKey;
import com.felayga.unpixeldungeon.items.keys.OldKey;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.felayga.unpixeldungeon.items.weapon.missiles.Boomerang;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Belongings implements Iterable<Item>, IDecayable, IBag {
	public static final int BACKPACK_SIZE	= 36; //original=19
	
	private Char owner;
	
	public ConsumablesBackpack backpack1;
	public UncategorizedBackpack backpack2;
	public EquipmentBackpack backpack3;
	public Spellbook backpack4;

	public void onWeightChanged(int change) {
		weight += change;
	}

	public boolean collect(Item item) {
		boolean retval = false;
		if (backpack3.grab(item)) {
			GLog.d("->bag3");
			retval = backpack3.collect(item);
		}
		else if (backpack1.grab(item)) {
			GLog.d("->bag1");
			retval = backpack1.collect(item);
		}
		else if (backpack4.grab(item)) {
			GLog.d("->bag4");
			retval = backpack4.collect(item);
		}
		else {
			GLog.d("->bag2");
			retval = backpack2.collect(item);
		}

		GLog.d("collect "+item.getDisplayName()+" "+ retval);
		GLog.d("weight2="+weight+" "+backpack1.weight()+" "+backpack2.weight()+" "+backpack3.weight()+" "+backpack4.weight());

		return retval;
	}

	public Item remove(Item item)
	{
		Dungeon.quickslot.clearItem(item);
		item.updateQuickslot();
		Item retval = null;

		if (backpack3.contains(item))
		{
			retval = remove(backpack3, item);
		}
		else if (backpack1.contains(item))
		{
			retval = remove(backpack1, item);
		}
		else if (backpack4.contains(item))
		{
			retval = remove(backpack4, item);
		}
		else if (backpack2.contains(item))
		{
			retval = remove(backpack2, item);
		}

		GLog.d("weight3="+weight+" "+backpack1.weight()+" "+backpack2.weight()+" "+backpack3.weight()+" "+backpack4.weight());

		return retval;
	}

	public Item remove(Item item, int quantity) {
		if (item.quantity() > quantity) {
			Item detached = item.parent.remove(item, quantity);
			item.updateQuickslot();

			return detached;
		} else if (item.quantity() == quantity) {
			if (item.stackable || item instanceof Boomerang) {
				Dungeon.quickslot.convertToPlaceholder(item);
			}

			return remove(item);
		}

		return null;
	}

	private Item remove(Bag bag, Item detachItem)
	{
		Iterator<Item> iterator = bag.iterator(false);
		while (iterator.hasNext()){
			Item item = iterator.next();
			if (item == detachItem) {
				iterator.remove();
				item.onDetach();

				GLog.d("weight5=" + weight + " " + backpack1.weight() + " " + backpack2.weight() + " " + backpack3.weight() + " " + backpack4.weight());
				return item;
			}
		}

		return null;
	}

	public boolean tryMergeStack(Item test) {
		return false;
	}

	public boolean contains(Item item) {
		return backpack3.contains(item)
				|| backpack1.contains(item)
				|| backpack4.contains(item)
				|| backpack2.contains(item);
	}

	private boolean tryReplaceSimple(EquippableItem.Slot slot, EquippableItem item) {
		switch (slot) {
			case Weapon:
				if (weapon == null || unequip(weapon, true)) {
					weapon = item;
					return true;
				}
				break;
			case Offhand:
				if (offhand == null || unequip(offhand, true)) {
					offhand = item;
					return true;
				}
				break;
			case Armor:
				if (armor == null || unequip(armor, true)) {
					armor = item;
					return true;
				}
				break;
			case Gloves:
				if (gloves == null || unequip(gloves, true)) {
					gloves = item;
					return true;
				}
				break;
			case Boots:
				if (boots == null || unequip(boots, true)) {
					boots = item;
					return true;
				}
				break;
			case Ring1:
				if (ring1 == null || unequip(ring1, true)) {
					ring1 = item;
					return true;
				}
				break;
			case Ring2:
				if (ring2 == null || unequip(ring2, true)) {
					ring2 = item;
					return true;
				}
				break;
			case Amulet:
				break;
			case Cloak:
				break;
			case Face:
				break;
            case Helmet:
                if (helmet == null || unequip(helmet, true)) {
                    helmet = item;
                    return true;
                }
                break;
            case Pants:
                if (pants == null || unequip(pants, true)) {
                    pants = item;
                    return true;
                }
                break;
		}

		return false;
	}

	private boolean tryReplaceOneHanded(final EquippableItem.Slot[] slots, final EquippableItem item, final int quickslot) {
		final EquippableItem[] items = new EquippableItem[slots.length];

		for (int n = 0; n < slots.length; n++) {
			switch (slots[n]) {
				case Weapon:
					if (weapon == null) {
						weapon = item;
						return true;
					} else {
						if (weapon.twoHanded) {
							return tryReplaceSimple(slots[n], item);
						}
						else {
							items[n] = weapon;
						}
					}
					break;
				case Offhand:
					if (offhand == null) {
						offhand = item;
						return true;
					} else {
						items[n] = offhand;
					}
					break;
				case Armor:
					if (armor == null) {
						armor = item;
						return true;
					} else {
						items[n] = armor;
					}
					break;
				case Gloves:
					if (gloves == null) {
						gloves = item;
						return true;
					} else {
						items[n] = gloves;
					}
					break;
				case Boots:
					if (boots == null) {
						boots = item;
						return true;
					} else {
						items[n] = boots;
					}
					break;
				case Ring1:
					if (ring1 == null) {
						ring1 = item;
						return true;
					} else {
						items[n] = ring1;
					}
					break;
				case Ring2:
					if (ring2 == null) {
						ring2 = item;
						return true;
					} else {
						items[n] = ring2;
					}
					break;
				case Amulet:
					if (amulet == null) {
						amulet = item;
						return true;
					} else {
						items[n] = amulet;
					}
					break;
				case Cloak:
					if (cloak == null) {
						cloak = item;
						return true;
					} else {
						items[n] = cloak;
					}
					break;
				case Face:
					if (face == null) {
						face = item;
						return true;
					} else {
						items[n] = face;
					}
					break;
                case Helmet:
                    if (helmet == null) {
                        helmet = item;
                        return true;
                    }
                    else {
                        items[n] = helmet;
                    }
                    break;
                case Pants:
                    if (pants == null) {
                        pants = item;
                        return true;
                    }
                    else {
                        items[n] = pants;
                    }
                    break;
			}
		}

		final List<String> options = new ArrayList<String>();

		for (int n = 0; n < items.length; n++) {
			options.add(items[n].getDisplayName());
		}

		options.add(Constant.TXT_CANCEL);

		ShatteredPixelDungeon.scene().add(
				new WndOptions("Unequip One Item", "No empty equipment slots for this item. Choose an item to swap out.", options) {

					@Override
					protected void onSelect(int index) {
						if (index >= 0 && index < items.length) {
							if (tryReplaceSimple(slots[index], item)) {
								onItemEquipped(owner, item, quickslot, true);
							}
						}
					}
				});

		return false;
	}

	private boolean tryReplaceTwoHanded(final EquippableItem.Slot[] slots, final EquippableItem item, final int quickslot) {
		final EquippableItem[] items = new EquippableItem[slots.length];
		int[] quickslots = new int[slots.length];

		for (int n = 0; n < slots.length; n++) {
            switch (slots[n]) {
                case Weapon:
                    items[n] = weapon;
                    break;
                case Offhand:
                    items[n] = offhand;
                    break;
                case Armor:
                    items[n] = armor;
                    break;
                case Gloves:
                    items[n] = gloves;
                    break;
                case Boots:
                    items[n] = boots;
                    break;
                case Ring1:
                    items[n] = ring1;
                    break;
                case Ring2:
                    items[n] = ring2;
                    break;
                case Amulet:
                    items[n] = amulet;
                    break;
                case Cloak:
                    items[n] = cloak;
                    break;
                case Face:
                    items[n] = face;
                    break;
                case Helmet:
                    items[n] = helmet;
                    break;
                case Pants:
                    items[n] = pants;
                    break;
            }
        }

		for (int n=0;n<slots.length;n++) {
			for (int subn=n+1;subn<slots.length;subn++) {
				if (items[subn] != null && items[subn]==items[n]) {
					items[subn] = null;
				}
			}
		}

		if (owner instanceof Hero) {
			for (int n=0;n<slots.length;n++) {
				quickslots[n] = Dungeon.quickslot.getSlot(items[n]);
			}
		}
		else {
			for (int n=0;n<slots.length;n++) {
				quickslots[n] = -1;
			}
		}

		for (int n=0;n<slots.length;n++) {
			if (!unequip(items[n], true)) {
				for (int subn=n-1;subn>=0;subn--) {
					if (tryReplaceSimple(slots[subn], items[subn])) {
						onItemEquipped(owner, items[subn], quickslots[subn], true);
					}
				}

				return false;
			}
		}

		for (int n=0;n<slots.length;n++) {
			switch (slots[n]) {
				case Weapon:
					weapon = item;
					break;
				case Offhand:
					offhand = item;
					break;
				case Armor:
					armor = item;
					break;
				case Gloves:
					gloves = item;
					break;
				case Boots:
					boots = item;
					break;
				case Ring1:
					ring1 = item;
					break;
				case Ring2:
					ring2 = item;
					break;
				case Amulet:
					amulet = item;
					break;
				case Cloak:
					cloak = item;
					break;
				case Face:
					face = item;
					break;
                case Helmet:
                    helmet = item;
                    break;
                case Pants:
                    pants = item;
                    break;
			}
		}

		return true;
	}

    public boolean collectEquip(EquippableItem item) {
        if (equip(item)) {
            return true;
        }

        return collect(item);
    }

	public boolean equip(EquippableItem item) {
		//In addition to equipping itself, item reassigns itself to the quickslot
		//This is a special case as the item is being removed from inventory, but is staying with the hero.
		int slot = -1;
		if (owner instanceof Hero) {
			slot = Dungeon.quickslot.getSlot(item);
		}

		EquippableItem.Slot[] slots = item.getSlots();

		boolean good = false;
		if (slots.length == 1 && tryReplaceSimple(slots[0], item)) {
			good = true;
		}
		else if (!item.twoHanded && tryReplaceOneHanded(slots, item, slot)) {
			good = true;
		}
		else if (item.twoHanded && tryReplaceTwoHanded(slots, item, slot))
		{
			good = true;
		}

		if (good)
		{
            GLog.d("equip "+item.getDisplayName());

			remove(item);

			onItemEquipped(owner, item, slot, true);
		}

		return good;
	}

	private void onItemEquipped(Char owner, EquippableItem item, int quickslot, boolean cursednotify) {
		boolean cursed = false;

		if (item.bucStatus() == BUCStatus.Cursed) {
			cursed = true;
			item.bucStatus(true);

            if (Level.fieldOfView[owner.pos] && owner.sprite != null) {
                owner.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                Sample.INSTANCE.play(Assets.SND_CURSED);
            }
		}

		item.onEquip(owner, cursed & cursednotify);
        onWeightChanged(item.weight() * item.quantity());

		GLog.d("weight0="+weight+" "+backpack1.weight()+" "+backpack2.weight()+" "+backpack3.weight()+" "+backpack4.weight());

		if (quickslot != -1) {
			Dungeon.quickslot.setSlot(quickslot, item);
			item.updateQuickslot();
		}

		owner.spend(item.equipTime, false);
	}

	private void onItemUnequipped(Char owner, EquippableItem item, boolean single) {
		item.onUnequip(owner);
		weight -= item.weight() * item.quantity();

		owner.spend(item.equipTime, single);
		GLog.d("weight1="+weight+" "+backpack1.weight()+" "+backpack2.weight()+" "+backpack3.weight()+" "+backpack4.weight());
	}

	private static final String TXT_UNEQUIP_CANT	= "You can't remove the %s!";

	public boolean unequip(EquippableItem item, boolean collect) {
		return unequip(item, collect, true);
	}

	public boolean unequip(EquippableItem item, boolean collect, boolean single) {
		if (item.bucStatus() == BUCStatus.Cursed) {
			if (owner instanceof Hero) {
				GLog.w(TXT_UNEQUIP_CANT, item.getDisplayName());
				item.bucStatus(true);
			}

			return false;
		}

		unequip(item);
		onItemUnequipped(owner, item, single);

		if (collect && !collect(item)) {
			if (owner instanceof Hero) {
				Dungeon.quickslot.clearItem(item);
			}
			item.updateQuickslot();
			Dungeon.level.drop( item, owner.pos );
		}

		return true;
	}

	private void unequip(EquippableItem item) {
		EquippableItem.Slot[] slots = item.getSlots();

		for (int n=0;n<slots.length;n++) {
			switch(slots[n]) {
				case Weapon:
					if (weapon == item) {
						weapon = null;
					}
					break;
				case Offhand:
					if (offhand == item) {
						offhand = null;
					}
					break;

                case Face:
                    if (face == item) {
                        face = null;
                    }
                    break;
                case Amulet:
                    if (amulet == item) {
                        amulet = null;
                    }
                    break;
                case Ring1:
                    if (ring1 == item) {
                        ring1 = null;
                    }
                    break;
                case Ring2:
                    if (ring2 == item) {
                        ring2 = null;
                    }
                    break;

                case Cloak:
                    if (cloak == item) {
                        cloak = null;
                    }
                    break;
				case Armor:
					if (armor == item) {
						armor = null;
					}
					break;
                case Helmet:
                    if (helmet == item) {
                        helmet = null;
                    }
                    break;
				case Gloves:
					if (gloves == item) {
						gloves = null;
					}
					break;
                case Pants:
                    if (pants == item) {
                        pants = null;
                    }
                    break;
				case Boots:
					if (boots == item) {
						boots = null;
					}
					break;
			}
		}
	}

	public boolean isEquipped(Item item) {
		Iterator<Item> iterator = iterator(true, false);

		while (iterator.hasNext()) {
			Item subitem = iterator.next();

			if (subitem == item) {
				return true;
			}
		}

		return false;
	}

	private EquippableItem weapon = null;
	private EquippableItem offhand = null;

    private EquippableItem face = null;
    private EquippableItem amulet = null;
	private EquippableItem ring1 = null;
	private EquippableItem ring2 = null;

	private EquippableItem cloak = null;
	private EquippableItem helmet = null;
	private EquippableItem armor = null;
    private EquippableItem gloves = null;
	private EquippableItem pants = null;
    private EquippableItem boots = null;

    public EquippableItem weapon() {
        return weapon;
    }

    public EquippableItem offhand() {
        return offhand;
    }

    public EquippableItem face() {
        return face;
    }

    public EquippableItem amulet() {
        return amulet;
    }

    public EquippableItem ring1() {
        return ring1;
    }

    public EquippableItem ring2() {
        return ring2;
    }

    public EquippableItem cloak() {
        return cloak;
    }

    public EquippableItem armor() {
        return armor;
    }

    public EquippableItem helmet() {
        return helmet;
    }

    public EquippableItem gloves() {
        return gloves;
    }

    public EquippableItem pants() {
        return pants;
    }

    public EquippableItem boots() {
        return boots;
    }


	public int weight = 0;
	
	public Belongings( Char owner ) {
		this.owner = owner;
		
		backpack1 = new ConsumablesBackpack(owner) {{
			name = "consumables";
			size = BACKPACK_SIZE + 12;
		}};
		backpack1.parent = this;

		backpack2 = new UncategorizedBackpack(owner) {{
			name = "valuables";
			size = BACKPACK_SIZE + 12;
		}};
		backpack2.parent = this;

		backpack3 = new EquipmentBackpack(owner) {{
			name = "equipment";
			size = BACKPACK_SIZE;
		}};
		backpack3.parent = this;

		backpack4 = new Spellbook(owner) {{
			name = "spellbook";
			size = BACKPACK_SIZE + 12;
		}};
		backpack4.parent = this;
	}

	private static final String BACKPACK1	= "backpack1";
	private static final String BACKPACK2	= "backpack2";
	private static final String BACKPACK3	= "backpack3";
	private static final String BACKPACK4	= "backpack4";

	private static final String WEAPON		= "weapon";
    private static final String OFFHAND 	= "offhand";

	private static final String RING1   	= "ring1";
	private static final String RING2   	= "ring2";
	private static final String AMULET		= "amulet";
	private static final String FACE    	= "face";

	private static final String CLOAK   	= "cloak";
	private static final String ARMOR		= "armor";

	private static final String HELMET		= "helmet";
    private static final String GLOVES  	= "gloves";
	private static final String PANTS		= "pants";
    private static final String BOOTS   	= "boots";

	public void storeInBundle( Bundle bundle ) {
		backpack1.storeInBundle(bundle, BACKPACK1);
		backpack2.storeInBundle(bundle, BACKPACK2);
		backpack3.storeInBundle(bundle, BACKPACK3);
		backpack4.storeInBundle(bundle, BACKPACK4);
		/*
		bundle.put( BACKPACK1, backpack1.items );
		bundle.put( BACKPACK2, backpack2.items );
		bundle.put( BACKPACK3, backpack3.items );
		bundle.put( BACKPACK4, backpack4.items );
		*/
		/*
		backpack1.storeInBundle(bundle);
		backpack2.storeInBundle(bundle);
		backpack3.storeInBundle(bundle);
		*/

		bundle.put(WEAPON, weapon);
        bundle.put(OFFHAND, offhand);

		bundle.put(RING1, ring1);
		bundle.put(RING2, ring2);
		bundle.put(AMULET, amulet);
		bundle.put(FACE, face);

        bundle.put(CLOAK, cloak);
		bundle.put(ARMOR, armor);
        bundle.put(HELMET, helmet);
        bundle.put(GLOVES, gloves);
		bundle.put(PANTS, pants);
        bundle.put(BOOTS, boots);

	}
	
	public void restoreFromBundle( Bundle bundle ) {
		backpack1.clear();
		backpack2.clear();
		backpack3.clear();
		backpack4.clear();

		for (Bundlable item : bundle.getCollection(BACKPACK1)) {
			if (item != null) backpack1.collect((Item) item);
		}

		for (Bundlable item : bundle.getCollection(BACKPACK2)) {
			if (item != null) backpack2.collect((Item) item);
		}

		for (Bundlable item : bundle.getCollection(BACKPACK3)) {
			if (item != null) backpack3.collect((Item) item);
		}

		for (Bundlable item : bundle.getCollection(BACKPACK4)) {
			if (item != null) backpack4.collect((Item) item);
		}

		/*
		backpack1.restoreFromBundle(bundle);
		backpack2.restoreFromBundle(bundle);
		backpack3.restoreFromBundle(bundle);
		*/

		weapon = (EquippableItem) bundle.get(WEAPON);
		offhand = (EquippableItem) bundle.get(OFFHAND);

		ring1 = (EquippableItem) bundle.get(RING1);
		ring2 = (EquippableItem) bundle.get(RING2);
		amulet = (EquippableItem) bundle.get(AMULET);
		face = (EquippableItem) bundle.get(FACE);

		cloak = (EquippableItem) bundle.get(CLOAK);
		armor = (EquippableItem) bundle.get(ARMOR);
		helmet = (EquippableItem) bundle.get(HELMET);
		gloves = (EquippableItem) bundle.get(GLOVES);
		pants = (EquippableItem) bundle.get(PANTS);
		boots = (EquippableItem) bundle.get(BOOTS);


		Iterator<Item> iterator = iterator(true, false);

		while (iterator.hasNext()) {
			Item item = iterator.next();

			if (item instanceof EquippableItem) {
				onItemEquipped(owner, (EquippableItem)item, -1, false);
			}
		}

        /*
		if (bundle.get( WEAPON ) instanceof Wand){
			//handles the case of an equipped wand from pre-0.3.0
			Wand item = (Wand) bundle.get(WEAPON);
			//try to add the wand to inventory
			if (!item.collect(backpack)){
				//if it's too full, shove it in anyway
				backpack.items.add(item);
			}
		} else {
			weapon = (KindOfWeapon) bundle.get(WEAPON);
			if (weapon != null) {
				weapon.activate(owner);
			}
		}
		
		armor = (Armor)bundle.get( ARMOR );
		
		misc1 = (KindofMisc)bundle.get(MISC1);
		if (misc1 != null) {
			misc1.activate( owner );
		}
		
		misc2 = (KindofMisc)bundle.get(MISC2);
		if (misc2 != null) {
			misc2.activate( owner );
		}

        misc3 = (KindofMisc)bundle.get(MISC3);
        if (misc3 != null)
        {
            misc3.activate(owner);
        }

        misc4 = (KindofMisc)bundle.get(MISC4);
        if (misc4 != null) {
            misc4.activate(owner);
        }
        */
	}

	public Gold getGold()
	{
		Iterator<Item> items = iterator(false, true);

		while (items.hasNext()) {
			Item item = items.next();

			if (item instanceof Gold) {
				return (Gold) item;
			}
		}

		return null;
	}

	public int removeGold(int quantity)
	{
		Gold gold = getGold();

		if (gold != null)
		{
			int newQuantity = gold.quantity() - quantity;
			if (newQuantity > 0)
			{
				gold.quantity(newQuantity);
				return 0;
			}
			else {
				remove(gold);

				return -newQuantity;
			}
		}
		else {
			return 0;
		}
	}

	public int goldQuantity()
	{
		Gold gold = getGold();

		if (gold != null) {
			return gold.quantity();
		}

		return 0;
	}

	public int getArmor(int currentBonus) {
		Iterator<Item> iterator = iterator(true, false);
		int armorAmount = 0;

		while (iterator.hasNext()) {
			Item item = iterator.next();

			if (item instanceof Armor) {
				Armor armor = (Armor)item;

				armorAmount += armor.armor;

				if (currentBonus > armor.armorBonusMaximum) {
					currentBonus = armor.armorBonusMaximum;
				}
			}
		}

		return armorAmount + currentBonus;
	}

	public int getResistance() {
		Iterator<Item> iterator = iterator(true, false);
		int resistanceAmount = 0;

		while (iterator.hasNext()) {
			Item item = iterator.next();

			if (item instanceof Armor) {
				Armor armor = (Armor)item;

				if (resistanceAmount < armor.armorMagic) {
					resistanceAmount = armor.armorMagic;
				}
			}
		}

		return resistanceAmount;
	}

	public int getSpellFailure()
	{
		int spellFailure = 0;

		Iterator<Item> iterator = iterator(true, false);

		while (iterator.hasNext()) {
			Item item = iterator.next();

			if (item instanceof Armor) {
				Armor armor = (Armor)item;
				spellFailure += armor.spellFailure;
			}
		}

		return spellFailure;
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends OldKey> T getKey( Class<T> kind, int depth ) {
		Iterator<Item> iterator = iterator(false, true);

		while (iterator.hasNext())
		{
			Item item = iterator.next();

			if (item.getClass() == kind && ((OldKey)item).depth == depth) {
				return (T)item;
			}
		}
		
		return null;
	}

	public void countIronKeys() {

		IronOldKey.curDepthQuantity = 0;

		Iterator<Item> iterator = iterator(false, true);

		while (iterator.hasNext())
		{
			Item item = iterator.next();
			if (item instanceof IronOldKey && ((IronOldKey)item).depth == Dungeon.depth) {
				IronOldKey.curDepthQuantity += item.quantity();
			}
		}
	}
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe() {
		if (weapon != null) {
			weapon.identify();
			Badges.validateItemLevelAquired( weapon );
		}
        if (offhand != null)
        {
            offhand.identify();
            Badges.validateItemLevelAquired(offhand);
        }

		if (ring1 != null) {
			ring1.identify();
			Badges.validateItemLevelAquired(ring1);
		}
		if (ring2 != null) {
			ring2.identify();
			Badges.validateItemLevelAquired(ring2);
		}
		if (amulet != null) {
			amulet.identify();
			Badges.validateItemLevelAquired(amulet);
		}
		if (face != null) {
			face.identify();
			Badges.validateItemLevelAquired(face);
		}

		if (cloak != null){
			cloak.identify();
			Badges.validateItemLevelAquired(cloak);
		}
		if (armor != null) {
			armor.identify();
			Badges.validateItemLevelAquired( armor );
		}
		if (helmet != null) {
			helmet.identify();
			Badges.validateItemLevelAquired(helmet);
		}
		if (gloves != null) {
			gloves.identify();
			Badges.validateItemLevelAquired(gloves);
		}
		if (pants != null) {
			pants.identify();
			Badges.validateItemLevelAquired(pants);
		}
        if (boots != null){
            boots.identify();
            Badges.validateItemLevelAquired(boots);
        }

		/*
		for (Item item : backpack) {
			//todo: should this be done here?
			item.bucStatusKnown = true;
		}
		*/
	}
	
	public int bucChange(boolean changeStatus, BUCStatus status, boolean changeStatusKnown, boolean statusKnown, boolean equippedItems, boolean backpackItems) {
		Iterator<Item> items = iterator(equippedItems, backpackItems);
		int itemsModified = 0;

		while (items.hasNext()) {
			Item item = items.next();

			if (changeStatus) {
				if (item.bucStatus() != status)
				{
					item.bucStatus(status);
					itemsModified++;
				}
			}

			if (changeStatusKnown) {
				item.bucStatus(statusKnown);
			}
		}

		return itemsModified;
	}

	public int bucUncurse(boolean changeStatusKnown, boolean statusKnown, boolean equippedItems, boolean backpackItems) {
		Iterator<Item> items = iterator(equippedItems, backpackItems);
		int itemsModified = 0;

		while (items.hasNext()) {
			Item item = items.next();

			switch(item.bucStatus())
			{
				case Cursed:
					item.bucStatus(BUCStatus.Uncursed);
					itemsModified++;
					break;
			}

			if (changeStatusKnown) {
				item.bucStatus(statusKnown);
			}
		}

		return itemsModified;
	}

	public int identifyAll(boolean equippedItems, boolean backpackItems) {
		Iterator<Item> items = iterator(equippedItems, backpackItems);
		int itemsModified = 0;

		while (items.hasNext()) {
			Item item = items.next();

			//GLog.d("do item=" + item.getDisplayName());

			if (!item.isIdentified()) {
				//GLog.d("  not identified");
				item.identify();
				itemsModified++;
			}
			else {
				//GLog.d("  identified");
			}
		}

		return itemsModified;
	}

    public Item randomEquipped() {
        ItemIterator iterator = new ItemIterator(true, false);

        int count = 0;

        for (int n = 0; n < iterator.equippedLength; n++) {
            if (iterator.equipped[n] != null) {
                count++;
            }
        }

        if (count > 0) {
            int index = Random.Int(count);

            for (int n = 0; n < iterator.equippedLength; n++) {
                if (iterator.equipped[n] != null) {
                    if (index == 0) {
                        return iterator.equipped[n];
                    }
                    index--;
                }
            }
        }

        return null;
    }

	public Item randomUnequipped() {
		Item retval = null;
		int bagstested = 0;

		while (retval == null) {
			if (bagstested == 7) {
				return null;
			}
			Bag backpack;
			switch(Random.Int(3)) {
				case 0:
					backpack = backpack1;
					bagstested |= 1;
					break;
				case 1:
					backpack = backpack2;
					bagstested |= 2;
					break;
				default:
					backpack = backpack3;
					bagstested |= 4;
					break;
			}

			retval = backpack.randomItem();
		}

		return retval;
	}
	
	public void resurrect( int depth ) {
		/*
		for (Item item : backpack.items.toArray( new Item[0])) {
			if (item instanceof OldKey) {
				if (((OldKey)item).depth == depth) {
					item.detachAll( backpack );
				}
			} else if (item.unique) {
				item.detachAll(backpack);
				//you keep the bag itself, not its contents.
				if (item instanceof Bag){
					((Bag)item).clear();
				}
				item.collect();
			} else if (!item.isEquipped( owner )) {
				item.detachAll( backpack );
			}
		}
		*/

		/*
		if (weapon != null) {
			weapon.cursed = false;
			weapon.activate( owner );
		}
        if (offhand != null){
            offhand.cursed = false;
            offhand.activate(owner);
        }
        if (tool1 != null){
            tool1.cursed = false;
        }
        if (tool2 != null) {
            tool2.cursed=false;
        }
		
		if (armor != null) {
			armor.cursed = false;
		}
        if (gloves!=null){
            gloves.cursed=false;
        }
        if (boots != null) {
            boots.cursed = false;
        }
        if (cloak != null) {
            cloak.cursed = false;
        }

		if (ring1 != null) {
			ring1.cursed = false;
			ring1.activate( owner );
		}
		if (ring2 != null) {
			ring2.cursed = false;
			ring2.activate( owner );
		}
        if (amulet != null) {
            amulet.cursed=false;
            amulet.activate(owner);
        }
        if (face != null) {
            face.cursed = false;
            face.activate(owner);
        }
        */
	}
	
	public int charge( boolean full) {
		
		int count = 0;
		
		for (Item item : this) {
			if (item instanceof Wand) {
				Wand wand = (Wand)item;
				if (wand.curCharges < wand.maxCharges) {
					wand.curCharges = full ? wand.maxCharges : wand.curCharges + 1;
					count++;
					
					wand.updateQuickslot();
				}
			}
		}
		
		return count;
	}
	
	public int discharge() {
		
		int count = 0;
		
		for (Item item : this) {
			if (item instanceof Wand) {
				Wand wand = (Wand)item;
				if (wand.curCharges > 0) {
					wand.curCharges--;
					count++;
					
					wand.updateQuickslot();
				}
			}
		}
		
		return count;
	}

	public void dropAll(int pos) {
		ArrayList<Integer> passable = new ArrayList<Integer>();
		for (Integer ofs : Level.NEIGHBOURS8) {
			if (ofs==0) {
				continue;
			}

			int cell = pos + ofs;

			if ((Level.passable[cell] || Level.avoid[cell]) && Dungeon.level.heaps.get( cell ) == null) {
				passable.add( cell );
			}
		}

		boolean passableOrigin = Level.passable[pos];

		Iterator<Item> items = iterator();

		while (items.hasNext()) {
			Collections.shuffle(passable);

			if (passableOrigin && items.hasNext())
			{
				Item item = items.next();
				if (item.droppable) {
					Dungeon.level.drop(item, pos).sprite.drop(pos);
				}
			}

			for (Integer cell : passable) {
				if (!items.hasNext()) {
					break;
				}

				Item item = items.next();
				if (item.droppable) {
					Dungeon.level.drop(item, cell).sprite.drop(pos);
				}
			}
		}
	}


	public long decay() {
		return 0;
	}
	public void decay(long amount, boolean updateTime, boolean fixTime) {
		Iterator<Item> iterator = iterator();

		while (iterator.hasNext()) {
			Item next = iterator.next();

			if (next instanceof IDecayable) {
				IDecayable decayable = (IDecayable)next;

				decayable.decay(amount, updateTime, fixTime);
				if (decayable.decayed()) {
					iterator.remove();
				}
			}
		}
	}
	public boolean decayed() {
		return false;
	}


	public ITool[] getToolTypes(boolean equipped, boolean unequipped, String... types) {
		ITool[] retval = new ITool[types.length];
		Iterator<Item> iterator = iterator(equipped, unequipped);

		while (iterator.hasNext()) {
			Item item = iterator.next();

			if (item instanceof ITool) {
				ITool tool = (ITool)item;

				int found = 0;

				String toolClass = tool.getToolClass();
				for (int n=0;n<types.length;n++) {
					if (retval[n]==null) {
						if (types[n]==toolClass) {
							retval[n] = tool;
							found++;
						}
					}
					else {
						found++;
					}
				}

				if (found == retval.length) {
					break;
				}
			}
		}

		return retval;
	}


	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}

	public Iterator<Item> iterator(boolean equippedItems, boolean backpackItems) {
		return new ItemIterator(equippedItems, backpackItems);
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;



		private Iterator<Item> backpack1Iterator = backpack1.iterator();
		private Iterator<Item> backpack2Iterator = backpack2.iterator();
		private Iterator<Item> backpack3Iterator = backpack3.iterator();
		private Iterator<Item> backpack4Iterator = backpack4.iterator();
		private int backpackIndex = -1;

		public Item[] equipped = {weapon, offhand, face, amulet, ring1, ring2, cloak, armor, helmet, gloves, pants, boots };
		public int equippedLength = equipped.length;

		private boolean backpackItems;

		public ItemIterator()
		{
			this(true, true);
		}

		public ItemIterator(boolean equippedItems, boolean backpackItems)
		{
			super();

			if (!equippedItems)
			{
				index = equippedLength;
			}

			this.backpackItems = backpackItems;
		}



		@Override
		public boolean hasNext() {
			for (int i = index; i < equippedLength; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}

			if (!backpackItems) {
				return false;
			}

			return backpack1Iterator.hasNext() || backpack2Iterator.hasNext() || backpack3Iterator.hasNext() || backpack4Iterator.hasNext();
		}

		@Override
		public Item next() {
			while (index < equippedLength) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}

			if (backpackItems) {
				if (backpack1Iterator.hasNext()) {
					backpackIndex = 0;
					return backpack1Iterator.next();
				}
				if (backpack2Iterator.hasNext()) {
					backpackIndex = 1;
					return backpack2Iterator.next();
				}
				if (backpack3Iterator.hasNext()) {
					backpackIndex = 2;
					return backpack3Iterator.next();
				}
				if (backpack4Iterator.hasNext()) {
					backpackIndex = 3;
					return backpack4Iterator.next();
				}
			}

			return null;
		}

		@Override
		public void remove() {
			switch(backpackIndex)
			{
				case -1:
					removeEquipped(index - 1);
					break;
				case 0:
					backpack1Iterator.remove();
					break;
				case 1:
					backpack2Iterator.remove();
					break;
				case 2:
					backpack3Iterator.remove();
					break;
				case 3:
					backpack4Iterator.remove();
					break;
            }
		}

		private void removeEquipped(int index)
		{
			switch (index) {
				case 0:
					equipped[0] = null;
					unequip(weapon);
					break;
				case 1:
					equipped[1] = null;
					unequip(offhand);
					break;

                case 2:
                    equipped[2] = null;
                    unequip(face);
                    break;
                case 3:
                    equipped[3] = null;
                    unequip(amulet);
                    break;
				case 4:
					equipped[4] = null;
					unequip(ring1);
					break;
				case 5:
					equipped[5] = null;
					unequip(ring2);
					break;

				case 6:
					equipped[6] = null;
					unequip(cloak);
					break;
				case 7:
					equipped[7] = null;
					unequip(armor);
					break;
				case 8:
					equipped[8] = null;
					unequip(helmet);
					break;
				case 9:
					equipped[9] = null;
					unequip(gloves);
					break;
				case 10:
					equipped[10] = null;
					unequip(pants);
					break;
				case 11:
					equipped[11] = null;
					unequip(boots);
					break;
			}
		}
	}
}
