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
package com.felayga.unpixeldungeon.items.equippableitem.armor.medium;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;

public class MailArmor extends MediumArmor {
	public MailArmor() {
		super(5, 2, 1, 96);

		name = "chainmail armor";
		image = ItemSpriteSheet.ARMOR_CHAINMAIL;
        material = Material.Iron;

		price = 150;
        spriteTextureIndex = HeroSprite.ArmorIndex.Chainmail;

		weight(Encumbrance.UNIT * 360);
	}

	@Override
	public String desc() {
		return
			"Interlocking metal links make for a tough but flexible suit of armor.";
	}
}
