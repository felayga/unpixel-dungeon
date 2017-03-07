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

package com.felayga.unpixeldungeon.items.weapon.melee.martial.shortsword;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.SimpleMeleeWeapon;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by HELLO on 3/6/2017.
 */
public class ShortSword extends SimpleMeleeWeapon {
    public ShortSword() {
        this(GameTime.TICK, 1, 6);
    }

    public ShortSword(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);

        name = "short sword";
        image = ItemSpriteSheet.MELEE_SHORT_SWORD;
        material = Material.Iron;

        weight(30 * Encumbrance.UNIT);
        price = 4;
    }

    @Override
    public String desc() {
        return "Being just a few inches longer than a dagger, the iron blade of the sword is indeed quite short.";
    }
}
