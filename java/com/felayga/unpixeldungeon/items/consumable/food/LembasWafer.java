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

package com.felayga.unpixeldungeon.items.consumable.food;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by HELLO on 2/16/2017.
 */
public class LembasWafer extends Food {
    public LembasWafer() {
        super(800, 800 / 2, Encumbrance.UNIT * 5);

        name = "lembas wafer";
        image = ItemSpriteSheet.FOOD_LEMBAS;
        material = Material.Vegetable;

        stackable = true;
        bones = true;

        price = 20;
    }

    @Override
    protected boolean determineRotten() {
        return false;
    }

    @Override
    public String info() {
        return "Very thin cakes, baked brown and wrapped in leaves.  Highly nutritious." + super.info();
    }

    public String message() {
        return "This food tastes delicious!";
    }
}
