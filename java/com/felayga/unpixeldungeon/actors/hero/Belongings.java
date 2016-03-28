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
package com.felayga.unpixeldungeon.actors.hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.Amulet;
import com.felayga.unpixeldungeon.items.EquipableItem;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.armor.boots.Boots;
import com.felayga.unpixeldungeon.items.armor.gloves.Gloves;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.items.KindofMisc;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.backpack.ConsumablesBackpack;
import com.felayga.unpixeldungeon.items.bags.backpack.Spellbook;
import com.felayga.unpixeldungeon.items.bags.backpack.UncategorizedBackpack;
import com.felayga.unpixeldungeon.items.bags.backpack.EquipmentBackpack;
import com.felayga.unpixeldungeon.items.keys.IronOldKey;
import com.felayga.unpixeldungeon.items.keys.OldKey;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.felayga.unpixeldungeon.items.weapon.missiles.Boomerang;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Belongings implements Iterable<Item>, IDecayable {
	public static final int BACKPACK_SIZE	= 36; //original=19
	
	private Char owner;
	
	public ConsumablesBackpack backpack1;
	public UncategorizedBackpack backpack2;
	public EquipmentBackpack backpack3;
	public Spellbook backpack4;

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

		return retval;
	}

	public final Item detach(Item detachItem) {
		if (detachItem.quantity() <= 0) {
			GLog.d("quantity<=0, return null");
			return null;
		} else if (detachItem.quantity() == 1) {
			if (detachItem.stackable || detachItem instanceof Boomerang) {
				Dungeon.quickslot.convertToPlaceholder(detachItem);
			}

			GLog.d("quantity==1, detachAll" );
			return detachAll(detachItem);
		} else {
			detachItem.quantity(detachItem.quantity()-1);
			detachItem.updateQuickslot();

			try {
				//todo: WHAT THE FLYING FUCK.
				//pssh, who needs copy constructors?
				Item detached = detachItem.getClass().newInstance();
				Bundle copy = new Bundle();
				detachItem.storeInBundle(copy);
				GLog.d(copy.toString());
				detached.restoreFromBundle(copy);
				detached.quantity(1);
				detached.onDetach();
				return detached;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public final Item detachAll(Item detachItem) {
		Dungeon.quickslot.clearItem(detachItem);
		detachItem.updateQuickslot();

		if (backpack3.contains(detachItem))
		{
			return detachAll(backpack3, detachItem);
		}
		else if (backpack1.contains(detachItem))
		{
			return detachAll(backpack1, detachItem);
		}
		else if (backpack4.contains(detachItem))
		{
			return detachAll(backpack4, detachItem);
		}
		else if (backpack2.contains(detachItem))
		{
			return detachAll(backpack2, detachItem);
		}

		GLog.d("return null because not found");
		return null;
	}

	private Item detachAll(Bag bag, Item detachItem)
	{
		Iterator<Item> iterator = bag.iterator(false);
		while (iterator.hasNext()){
			Item item = iterator.next();
			if (item == detachItem) {
				iterator.remove();
				item.onDetach();
				return item;
			}
		}

		return null;
	}

	public boolean contains(Item item) {
		return backpack3.contains(item)
				|| backpack1.contains(item)
				|| backpack4.contains(item)
				|| backpack2.contains(item);
	}

	private boolean tryReplace(EquipableItem.Slot slot, EquipableItem item) {
		switch (slot) {
			case Weapon:
				if (item instanceof KindOfWeapon) {
					if (weapon == null || unequip(weapon, true)) {
						weapon = (KindOfWeapon)item;
						return true;
					}
				}
				break;
			case Offhand:
				if (item instanceof KindOfWeapon) {
					if (offhand == null || unequip(offhand, true)) {
						offhand = (KindOfWeapon)item;
						return true;
					}
				}
				break;
			case Armor:
				if (item instanceof Armor) {
					if (armor == null || unequip(armor, true)) {
						armor = (Armor)item;
						return true;
					}
				}
				break;
			case Gloves:
				if (item instanceof Gloves) {
					if (gloves == null || unequip(gloves, true)) {
						gloves = (Gloves)item;
						return true;
					}
				}
				break;
			case Boots:
				if (item instanceof Boots) {
					if (boots == null || unequip(boots, true)) {
						boots = (Boots)item;
						return true;
					}
				}
				break;
			case Ring:
				//empty
				break;
			case Amulet:
				break;
			case Cloak:
				break;
			case Face:
				break;
		}

		return false;
	}

	public boolean equip(EquipableItem item) {
		//In addition to equipping itself, item reassigns itself to the quickslot
		//This is a special case as the item is being removed from inventory, but is staying with the hero.
		int slot = -1;
		if (owner instanceof Hero) {
			slot = Dungeon.quickslot.getSlot(item);
		}

		EquipableItem.Slot[] slots = item.getSlots();

		boolean good = false;
		if (slots.length == 1 && tryReplace(slots[0], item)) {
			good = true;
		}

		if (good)
		{
			boolean cursed = false;
			if (item.bucStatus() == BUCStatus.Cursed) {
				item.bucStatus(true);

				cursed = true;
				owner.sprite.emitter().burst(ShadowParticle.CURSE, 6);
				Sample.INSTANCE.play(Assets.SND_CURSED);
			}

			if (slot != -1) {
				Dungeon.quickslot.setSlot(slot, item);
				item.updateQuickslot();
			}

			owner.spend(item.equipTime, false);
		}

		return good;
	}

	private static final String TXT_UNEQUIP_CANT	= "You can't remove the %s!";

	public boolean unequip(EquipableItem item, boolean collect) {
		return unequip(item, collect, true);
	}

	public boolean unequip(EquipableItem item, boolean collect, boolean single) {
		if (item.bucStatus() == BUCStatus.Cursed) {
			if (owner instanceof Hero) {
				GLog.w(TXT_UNEQUIP_CANT, item.getDisplayName());
			}

			item.bucStatus(true);

			return false;
		}

		owner.spend(item.equipTime, single);

		if (collect && !collect(item)) {
			item.onDetach();
			item.onUnequip(owner);
			if (owner instanceof Hero) {
				Dungeon.quickslot.clearItem(item);
			}
			item.updateQuickslot();
			Dungeon.level.drop( item, owner.pos );
		}

		return true;
	}

	public KindOfWeapon weapon = null;
	public KindOfWeapon offhand = null;
	public Tool tool1 = null;
	public Tool tool2 = null;

	public Armor armor = null;
    public Gloves gloves = null;
    public Boots boots = null;
    public Armor cloak = null;

	public KindofMisc ring1 = null;
	public KindofMisc ring2 = null;
	public KindofMisc amulet = null;
	public KindofMisc face = null;
	
	public Belongings( Char owner ) {
		this.owner = owner;
		
		backpack1 = new ConsumablesBackpack(owner) {{
			name = "consumables";
			size = BACKPACK_SIZE;
		}};
		backpack2 = new UncategorizedBackpack(owner) {{
			name = "valuables";
			size = BACKPACK_SIZE;
		}};
		backpack3 = new EquipmentBackpack(owner) {{
			name = "equipment";
			size = BACKPACK_SIZE;
		}};
		backpack4 = new Spellbook(owner) {{
			name = "spellbook";
			size = BACKPACK_SIZE;
		}};
	}

	private static final String BACKPACK1	= "backpack1";
	private static final String BACKPACK2	= "backpack2";
	private static final String BACKPACK3	= "backpack3";
	private static final String BACKPACK4	= "backpack4";

	private static final String WEAPON		= "weapon";
    private static final String OFFHAND     = "offhand";
    private static final String TOOL1       = "tool1";
    private static final String TOOL2       = "tool2";
	private static final String ARMOR		= "armor";
    private static final String GLOVES      = "gloves";
    private static final String BOOTS        = "boots";
    private static final String CLOAK       = "cloak";
	private static final String RING1       = "ring1";
	private static final String RING2       = "ring2";
	private static final String AMULET		= "amulet";
    private static final String FACE        = "face";

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
        bundle.put(TOOL1, tool1);
        bundle.put(TOOL2, tool2);

		bundle.put(ARMOR, armor);
        bundle.put(GLOVES, gloves);
        bundle.put(BOOTS, boots);
        bundle.put(CLOAK, cloak);

		bundle.put(RING1, ring1);
		bundle.put(RING2, ring2);
        bundle.put(AMULET, amulet);
        bundle.put(FACE, face);
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

		weapon = (KindOfWeapon) bundle.get(WEAPON);
		offhand = (KindOfWeapon) bundle.get(OFFHAND);
		tool1 = (Tool) bundle.get(TOOL1);
		tool2 = (Tool) bundle.get(TOOL2);

		armor = (Armor) bundle.get(ARMOR);
		gloves = (Gloves) bundle.get(GLOVES);
		boots = (Boots) bundle.get(BOOTS);
		cloak = (Armor) bundle.get(CLOAK);

		ring1 = (KindofMisc) bundle.get(RING1);
		ring2 = (KindofMisc) bundle.get(RING2);
		amulet = (KindofMisc) bundle.get(AMULET);
		face = (KindofMisc) bundle.get(FACE);

		if (weapon != null) {
			weapon.activate(this.owner);
		}
		if (offhand != null) {
			offhand.activate(this.owner);
		}

		if (ring1 != null) {
			ring1.activate(this.owner);
		}
		if (ring2 != null) {
			ring2.activate(this.owner);
		}
		if (amulet != null) {
			amulet.activate(this.owner);
		}
		if (face != null) {
			face.activate(this.owner);
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
				detachAll(gold);

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
        if (tool1 != null)
        {
            tool1.identify();
            Badges.validateItemLevelAquired(tool1);
        }
        if (tool2 != null){
            tool2.identify();
            Badges.validateItemLevelAquired(tool2);
        }
		if (armor != null) {
			armor.identify();
			Badges.validateItemLevelAquired( armor );
		}
        if (gloves != null) {
            gloves.identify();
            Badges.validateItemLevelAquired(gloves);
        }
        if (boots != null){
            boots.identify();
            Badges.validateItemLevelAquired(boots);
        }
        if (cloak != null){
            cloak.identify();
            Badges.validateItemLevelAquired(cloak);
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


	public Tool getEquippedTool(String type) {
		if (tool1 != null && tool1.getToolClass() == type) {
			return tool1;
		}
		else if (tool2 != null && tool2.getToolClass() == type) {
			return tool2;
		}

		return null;
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

		private Item[] equipped = {weapon, offhand, tool1, tool2, armor, gloves, boots, cloak, ring1, ring2, amulet, face};
		private int equippedLength = equipped.length;

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
					equipped[0] = weapon = null;
					break;
				case 1:
					equipped[1] = offhand = null;
					break;
				case 2:
					equipped[2] = tool1 = null;
					break;
				case 3:
					equipped[3] = tool2 = null;
					break;
				case 4:
					equipped[4] = armor = null;
					break;
				case 5:
					equipped[5] = gloves = null;
					break;
				case 6:
					equipped[6] = boots = null;
					break;
				case 7:
					equipped[7] = cloak = null;
					break;
				case 8:
					equipped[8] = ring1 = null;
					break;
				case 9:
					equipped[9] = ring2 = null;
					break;
				case 10:
					equipped[10] = amulet = null;
					break;
				case 11:
					equipped[11] = face = null;
					break;
			}
		}
	}
}
