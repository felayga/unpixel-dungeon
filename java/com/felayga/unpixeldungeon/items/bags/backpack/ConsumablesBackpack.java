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

package com.felayga.unpixeldungeon.items.bags.backpack;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Bomb;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.ui.Icons;

/**
 * Created by hello on 3/17/16.
 */
public class ConsumablesBackpack extends Backpack {

    public ConsumablesBackpack(Char owner)
    {
        super(owner);

        tabIcon = Icons.BACKPACK_CONSUMABLES;
        priority = 15;
    }

    @Override
    public boolean grab( Item item ) {
        return item instanceof Potion
                || item instanceof Scroll
                || item instanceof Food
                || item instanceof Bomb
                || item instanceof Wand
                || item instanceof Plant.Seed;
    }

}
