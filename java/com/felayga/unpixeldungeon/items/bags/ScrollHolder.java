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
package com.felayga.unpixeldungeon.items.bags;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class ScrollHolder extends Bag {

	public ScrollHolder()
	{
		super(null, false);

		name = "scroll holder";
		image = ItemSpriteSheet.HOLDER;
		
		size = 12;
		priority = 8;
        price = 50;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Scroll;
	}
	
	@Override
	public String info() {
		return
			"This tubular container looks like it would hold an astronomer's charts, but your scrolls will fit just as well.\n\n" +
			"The holder doesn't look very flammable, so your scrolls should be safe from fire inside it.";
	}
}
