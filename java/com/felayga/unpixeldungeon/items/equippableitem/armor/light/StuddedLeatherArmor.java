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
package com.felayga.unpixeldungeon.items.equippableitem.armor.light;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;

public class StuddedLeatherArmor extends LightArmor {
    public StuddedLeatherArmor() {
        super(3, 5, 1, 48);

        name = "studded armor";
        image = ItemSpriteSheet.ARMOR_STUDDEDLEATHER;
        material = Material.Leather;

        price = 25;
        spriteTextureIndex = HeroSprite.ArmorIndex.StuddedLeather;

        weight(Encumbrance.UNIT * 180);
    }

    @Override
    public String desc() {
        return "Armor made from tanned monster hide. Not as light as cloth armor but provides better protection.";
    }
}
