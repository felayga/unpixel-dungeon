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

package com.felayga.unpixeldungeon.items.food;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by HELLO on 6/7/2016.
 */
public class Ration extends Food {
    public Ration()
    {
        super(800, Encumbrance.UNIT * 20);

        stackable = true;
        name = "ration of food";
        image = ItemSpriteSheet.FOOD_RATION;

        bones = true;

        price = 45;
    }

    @Override
    public String info() {
        return "Nothing fancy here: dried meat, some biscuits - things like that." + super.info();
    }

    public String message()
    {
        return "This food tastes delicious!";
    }

}
