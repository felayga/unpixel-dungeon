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

package com.felayga.unpixeldungeon.items.bags.backpack;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.tools.ITool;
import com.felayga.unpixeldungeon.ui.Icons;

/**
 * Created by hello on 3/17/16.
 */
public class EquipmentBackpack extends Backpack {

    public EquipmentBackpack(Char owner)
    {
        super(owner);

        name = "equipment";
        size = Backpack.SIZE;

        tabIcon = Icons.BACKPACK_EQUIPMENT;
        priority = 15;
    }

    @Override
    public boolean grab( Item item ) {
        return item instanceof EquippableItem
                || item instanceof ITool;
    }

}
