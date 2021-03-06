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
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.Icons;

/**
 * Created by HELLO on 7/6/2016.
 */
public class LargeBox extends LargeChest {
    public LargeBox() {
        super(null, true);

        name = "large box";
        image = ItemSpriteSheet.LARGEBOX;
        material = Material.Wood;

        tabIcon = Icons.LARGEBOX;

        size = 36;
        priority = 8;
        price = 8;
        weight(Encumbrance.UNIT * 350);
    }

    @Override
    protected void onLockedChanged() {
        if (locked()) {
            image = ItemSpriteSheet.LARGEBOX_LOCKED;
        } else {
            image = ItemSpriteSheet.LARGEBOX;
        }
    }

    @Override
    public String info() {
        String retval = "This heavy box is used to store items for safe keeping.";

        if (locked()) {
            retval += "\nIt appears to be locked.";
        }

        return retval;
    }
}
