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

package com.felayga.unpixeldungeon.items.consumable.scrolls.inventoryscroll;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.consumable.scrolls.Scroll;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndOptions;

public abstract class InventoryScroll extends Scroll {
	protected static String inventoryTitle = "Select an item";
	protected WndBackpack.Mode mode = WndBackpack.Mode.ALL;

	private static final String TXT_WARNING =
			"Do you really want to cancel this scroll usage? It will be consumed anyway.";
	private static final String TXT_YES = "Yes, I'm positive";
	private static final String TXT_NO = "No, I changed my mind";

	@Override
	protected void doRead() {
        super.doRead();

		if (!isKnown()) {
			setKnown();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}

		GameScene.selectItem(itemSelector, mode, inventoryTitle, null);
	}

	private void confirmCancelation() {
		GameScene.show(new WndOptions(getName(), TXT_WARNING, TXT_YES, TXT_NO) {
			@Override
			protected void onSelect(int index) {
				switch (index) {
					case 0:
						curUser.spend_new(TIME_TO_READ, true);
						identifiedByUse = false;
						break;
					case 1:
						GameScene.selectItem(itemSelector, mode, inventoryTitle, null);
						break;
				}
			}

			public void onBackPressed() {
			}
		});
	}

	protected abstract void onItemSelected(Item item);

	protected static boolean identifiedByUse = false;
	protected static WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				((InventoryScroll)curItem).onItemSelected(item);
			} else {
				((InventoryScroll) curItem).confirmCancelation();
			}
		}
	};
}
