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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.spells.inventory.IdentifySpell;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ScrollOfIdentify extends InventoryScroll {
	protected int usesLeft;

	private static final String USESLEFT = "usesLeft";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(USESLEFT, usesLeft);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		usesLeft = bundle.getInt(USESLEFT);
	}


	public ScrollOfIdentify()
	{
		name = "Scroll of Identify";
		initials = "Id";

		inventoryTitle = "Select an item to identify";
		mode = WndBackpack.Mode.UNIDENTIFED;

		bones = true;

        price = 30;
	}

	@Override
	protected void prepareRead(Hero hero) {
		int roll;

		switch(bucStatus) {
			case Blessed:
				roll = Random.Int(5);
				if (roll == 0) {
					usesLeft = -1;
				}
				else {
					usesLeft = roll;
				}
				break;
			case Uncursed:
				roll = Random.Int(25);
				if (roll == 0) {
					usesLeft = -1;
				}
				else if (roll < 4) {
					usesLeft = roll + 1;
				}
				else {
					usesLeft = 1;
				}
				break;
			default:
				usesLeft = 1;
				break;
		}

		super.prepareRead(hero);
	}

	@Override
	protected void doRead()
	{
		if (usesLeft > 0) {
			super.doRead();
		}
		else {
			IdentifySpell.spellEffect(curUser, null, true);
			Dungeon.hero.belongings.identifyAll(true, true);
			GLog.p("You know everything about your possessions.");
		}
		//GLog.d("usesLeft="+usesLeft);
	}
	
	@Override
	protected void onItemSelected( Item item ) {
		//GLog.d("usesLeft=" + usesLeft);

		IdentifySpell.spellEffect(curUser, item, false);

		usesLeft--;
		//GLog.d("usesLeft="+usesLeft);


		if (usesLeft > 0) {
			super.doRead();
		}
		else {
			IdentifySpell.spellEffect(curUser, null, true);
			super.onItemSelected(item);
		}
	}
	
	@Override
	public String desc() {
		return
			"Permanently reveals all of the secrets of a single item.";
	}

}
