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
package com.felayga.unpixeldungeon.items.armor.medium;

import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class MailArmor extends MediumArmor {
	public MailArmor()
	{
		super(5, 2, 96);
		name = "chainmail armor";
		image = ItemSpriteSheet.ARMOR_MAIL;

		price = 150;
		textureIndex = 4;
	}

	@Override
	public String desc() {
		return
			"Interlocking metal links make for a tough but flexible suit of armor.";
	}
}
