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

import java.util.Iterator;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.items.KindofMisc;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.keys.IronKey;
import com.felayga.unpixeldungeon.items.keys.Key;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Belongings implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 43; //original=19
	
	private Hero owner;
	
	public Bag backpack;

	public KindOfWeapon weapon = null;
	public KindOfWeapon offhand = null;
	public Tool tool1 = null;
	public Tool tool2 = null;

	public Armor armor = null;
    public Armor gloves = null;
    public Armor boots = null;
    public Armor cloak = null;

	public KindofMisc ring1 = null;
	public KindofMisc ring2 = null;
	public KindofMisc amulet = null;
	public KindofMisc face = null;
	
	public Belongings( Hero owner ) {
		this.owner = owner;
		
		backpack = new Bag() {{
			name = "backpack";
			size = BACKPACK_SIZE;
		}};
		backpack.owner = owner;
	}
	
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
		
		backpack.storeInBundle( bundle );
		
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
		
		backpack.clear();
		backpack.restoreFromBundle(bundle);

        weapon = (KindOfWeapon)bundle.get(WEAPON);
        offhand = (KindOfWeapon)bundle.get(OFFHAND);
        tool1 = (Tool)bundle.get(TOOL1);
        tool2 = (Tool)bundle.get(TOOL2);

        armor = (Armor)bundle.get(ARMOR);
        gloves = (Armor)bundle.get(GLOVES);
        boots = (Armor)bundle.get(BOOTS);
        cloak = (Armor)bundle.get(CLOAK);

        ring1 = (KindofMisc)bundle.get(RING1);
        ring2 = (KindofMisc)bundle.get(RING2);
        amulet = (KindofMisc)bundle.get(AMULET);
        face = (KindofMisc)bundle.get(FACE);

        if (weapon != null) {
            weapon.activate(owner);
        }
        if (offhand != null) {
            offhand.activate(owner);
        }

        if (ring1 != null) {
            ring1.activate(owner);
        }
        if (ring2 != null) {
            ring2.activate(owner);
        }
        if (amulet != null) {
            amulet.activate(owner);
        }
        if (face != null) {
            face.activate(owner);
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
	public <T extends Key> T getKey( Class<T> kind, int depth ) {
		
		for (Item item : backpack) {
			if (item.getClass() == kind && ((Key)item).depth == depth) {
				return (T)item;
			}
		}
		
		return null;
	}

	public void countIronKeys() {

		IronKey.curDepthQuantity = 0;

		for (Item item : backpack) {
			if (item instanceof IronKey && ((IronKey)item).depth == Dungeon.depth) {
				IronKey.curDepthQuantity += item.quantity();
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
		for (Item item : backpack) {
			item.cursedKnown = true;
		}
	}
	
	public void uncurseEquipped() {
		ScrollOfRemoveCurse.uncurse( owner, weapon, offhand, tool1, tool2, armor, gloves, boots, cloak, ring1, ring2, amulet, face);
	}
	
	public Item randomUnequipped() {
		return Random.element( backpack.items );
	}
	
	public void resurrect( int depth ) {

		for (Item item : backpack.items.toArray( new Item[0])) {
			if (item instanceof Key) {
				if (((Key)item).depth == depth) {
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

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		private Item[] equipped = {weapon, offhand, tool1, tool2, armor, gloves, boots, cloak, ring1, ring2, amulet, face};
		private int backpackIndex = equipped.length;
		
		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {
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
                default:
                    backpackIterator.remove();
            }
		}
	}
}
