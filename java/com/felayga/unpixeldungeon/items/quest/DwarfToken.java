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
package com.felayga.unpixeldungeon.items.quest;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class DwarfToken extends Item {

    public DwarfToken()
	{
		name = "dwarf token";
		image = ItemSpriteSheet.TOKEN;
		
		stackable = true;
		unique = true;

        hasLevels(false);

        price = 100;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		return
			"Many dwarves and some of their larger creations carry these small pieces of metal of unknown purpose. " +
			"Maybe they are jewelry or maybe some kind of ID. Dwarves are strange folk.";
	}

}
