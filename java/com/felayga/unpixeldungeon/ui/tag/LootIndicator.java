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
package com.felayga.unpixeldungeon.ui.tag;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.ui.ItemSlot;

public class LootIndicator extends Tag {
	private ItemSlot slot;
	
	private Item lastItem = null;
	private int lastQuantity = 0;
    private int lastHeapSize = 0;
	
	public LootIndicator() {
		super(0x1F75CC);
        slot.topRight().hardlight(ItemSlot.UPGRADED);
        slot.topRight().alpha(1.0f);

        setSize(24, 24);
		
		visible = false;
	}
	
	@Override
	protected void createChildren() {
        super.createChildren();

        slot = new ItemSlot() {
            protected void onClick() {
                if (Dungeon.hero.handle(Dungeon.hero.pos(), new HeroAction.HandleHeap(Dungeon.hero.pos()))) {
                    Dungeon.hero.next();
                }
                /*
                Dungeon.hero.curAction = new HeroAction.HandleHeap(Dungeon.hero.pos());
                Dungeon.hero.motivate(true);
                */
            }
        };
        slot.showParams(true, true, false);
        add(slot);
    }
	
	@Override
	protected void layout() {
		super.layout();
		
		slot.setRect( x + 2, y + 3, width - 2, height - 6 );
	}
	
	@Override
	public void update() {
		if (Dungeon.hero.ready) {
			Heap heap = Dungeon.level.heaps.get( Dungeon.hero.pos());
			if (heap != null && heap.size() > 0) {
				Item item = heap.peek();
                int heapSize = heap.size();

				if (item != lastItem || item.quantity() != lastQuantity || heapSize != lastHeapSize) {
					lastItem = item;
					lastQuantity = item.quantity();
                    lastHeapSize = heapSize;
					
					slot.item( item );
					flash();
				}

                if (heapSize > 1) {
                    slot.topRight().text("" + heapSize);
                    slot.topRight().measure();
                    slot.topRight().visible = true;
                }
                else {
                    slot.topRight().visible = false;
                }

                slot.performLayout();

				visible = true;
			} else {
				lastItem = null;
				visible = false;
			}
		}
		
		slot.enable( visible && Dungeon.hero.ready );
		
		super.update();
	}
}
