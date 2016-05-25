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
package com.felayga.unpixeldungeon.items.armor.heavy;

import com.felayga.unpixeldungeon.actors.buffs.Encumbrance;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class FullPlateArmor extends HeavyArmor {
    public FullPlateArmor()
    {
        super(8, 1, 2, 112);
        name = "full plate armor";
        image = ItemSpriteSheet.ARMOR_FULLPLATE;

        price = 1500;
        textureIndex = 7;

        weight(Encumbrance.UNIT * 450);
    }

    @Override
    public String desc() {
        return
                "Enormous plates of metal are joined together into a suit that provides " +
                        "unmatched protection to any adventurer strong enough to bear its staggering weight.";
    }
}
