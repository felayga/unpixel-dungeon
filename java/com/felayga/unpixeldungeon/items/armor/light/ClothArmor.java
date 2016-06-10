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
package com.felayga.unpixeldungeon.items.armor.light;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class ClothArmor extends LightArmor {
	public ClothArmor()
	{
		super(1, 8, 0, 16);
		name = "cloth armor";
		image = ItemSpriteSheet.ARMOR_CLOTH;

		price = 5;
		textureIndex = 2;

		bones = false; //Finding them in bones would be semi-frequent and disappointing.

		weight(Encumbrance.UNIT * 90);
	}
	
	@Override
	public String desc() {
		return "This lightweight armor offers basic protection.";
	}
}
