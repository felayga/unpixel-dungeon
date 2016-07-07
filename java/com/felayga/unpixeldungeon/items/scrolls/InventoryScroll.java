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
package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public abstract class InventoryScroll extends Scroll {
	protected static String inventoryTitle = "Select an item";
	protected WndBackpack.Mode mode = WndBackpack.Mode.ALL;

	private static final String TXT_WARNING =
			"Do you really want to cancel this scroll usage? " +
					"It will be consumed anyway.";
	private static final String TXT_YES = "Yes, I'm positive";
	private static final String TXT_NO = "No, I changed my mind";

	@Override
	protected void doRead() {
		if (!isKnown()) {
			setKnown();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}

		GameScene.selectItem(itemSelector, mode, inventoryTitle);
	}

	private void confirmCancelation() {
		GameScene.show(new WndOptions(getDisplayName(), TXT_WARNING, TXT_YES, TXT_NO) {
			@Override
			protected void onSelect(int index) {
				switch (index) {
					case 0:
						curUser.spend_new(TIME_TO_READ, true);
						identifiedByUse = false;
						break;
					case 1:
						GameScene.selectItem(itemSelector, mode, inventoryTitle);
						break;
				}
			}

			public void onBackPressed() {
			}

			;
		});
	}

	protected void onItemSelected(Item item) {
		curUser.spend_new(TIME_TO_READ, true);

		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();
	}

	protected static boolean identifiedByUse = false;
	protected static WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				((InventoryScroll)curItem).onItemSelected(item);
			} else if (identifiedByUse && !((Scroll) curItem).ownedByBook) {
				((InventoryScroll) curItem).confirmCancelation();
			} else if (!((Scroll) curItem).ownedByBook) {
				curUser.belongings.collect(curItem);
			}
		}
	};
}
