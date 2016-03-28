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
package com.felayga.unpixeldungeon.items.food;

import com.felayga.unpixeldungeon.actors.buffs.Hunger;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class ChargrilledMeat extends Food {

	{
		name = "chargrilled meat";
		image = ItemSpriteSheet.STEAK;
		energy = 125;
		hornValue = 1;
	}
	
	@Override
	public String info() {
		return "It looks like a decent steak.";
	}
	
	@Override
	public int price() {
		return 5 * quantity;
	}
	
	public static Food cook( MysteryMeat ingredient ) {
		ChargrilledMeat result = new ChargrilledMeat();
		result.quantity = ingredient.quantity();
		return result;
	}
}
