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

package com.felayga.unpixeldungeon.items.weapon.ammunition.martial;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by HELLO on 3/6/2017.
 */

public class ArrowRuned extends Arrow {
    public ArrowRuned() {
        super(1, 7);

        name = "runed arrow";
        image = ItemSpriteSheet.MISSILE_ARROW_RUNED;
        pickupSound = Assets.SND_ITEM_BLADE;
        material = Material.Wood;

        price = 2;
    }

    @Override
    public String desc() {
        return "This is a shafted wooden projectile designed to be launched at a target using a bow.";
    }
}
