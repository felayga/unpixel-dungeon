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

package com.felayga.unpixeldungeon.items.consumable.food.vegetable;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by HELLO on 2/23/2017.
 */
public class Carrot extends Vegetable {
    public Carrot() {
        super(60, Encumbrance.UNIT * 2);

        stackable = true;
        name = "carrot";
        image = ItemSpriteSheet.FOOD_CARROT;

        bones = true;

        price = 7;
    }

    @Override
    public String info() {
        return "Historically valued for the leaves and seeds, the carrot root is rich in vitamin A." + super.info();
    }
}

