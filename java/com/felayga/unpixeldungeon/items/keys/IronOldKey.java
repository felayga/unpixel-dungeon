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
package com.felayga.unpixeldungeon.items.keys;

import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.Utils;

public class IronOldKey extends OldKey {

	private static final String TXT_FROM_DEPTH = "iron key from depth %d";

	public static int curDepthQuantity = 0;
	
	{
		name = "iron key";
		image = ItemSpriteSheet.TOOL_IRON_KEY;
	}
	
	public IronOldKey() {
		this( 0 );
	}
	
	public IronOldKey(int depth) {
		super();
		this.depth = depth;
	}
	
	@Override
	public String toString() {
		return Utils.format( TXT_FROM_DEPTH, depth );
	}
	
	@Override
	public String info() {
		return
			"The notches on this ancient iron key are well worn; its leather lanyard " +
			"is battered by age. What door might it open?";
	}
}
