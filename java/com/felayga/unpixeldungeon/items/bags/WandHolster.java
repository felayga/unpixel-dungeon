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

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class WandHolster extends Bag {

	public WandHolster()
	{
		super(null, false, 30 * Encumbrance.UNIT);

		name = "wand holster";
		image = ItemSpriteSheet.HOLSTER;
		
		size = 12;
		priority = 8;
        price = 50;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Wand;
	}

	/*
	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			if (owner != null) {
				for (Item item : items) {
					((Wand)item).charge( owner );
				}
			}
			return true;
		} else {
			return false;
		}
	}
	*/

	/*
	@Override
	public void onDetach( ) {
		super.onDetach();
		for (Item item : items) {
			((Wand)item).stopCharging();
		}
	}
	*/

	
	@Override
	public String info() {
		return
			"This slim holster is made from some exotic animal, and is designed to compactly carry up to " + size + " wands.\n\n" +
			"The size seems a bit excessive, who would ever have that many wands?";
	}
}
