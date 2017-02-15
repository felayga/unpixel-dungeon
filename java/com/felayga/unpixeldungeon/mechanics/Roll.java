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

package com.felayga.unpixeldungeon.mechanics;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 5/19/2016.
 */
public class Roll {
    public static int MobHP(int level) {
        if (level > 0) {
            return HP(level, 8);
        } else {
            return HP(1, 4);
        }
    }

    public static int HP(int quantity, int size) {
        int retval = 0;

        while (quantity > 0) {
            retval += Random.Int(size) + 1;
            quantity--;
        }

        return retval;
    }

    public static float DropBonusChance(Hero hero) {
        return 0.75f + (hero.luck() + 2.0f) * 0.015f;
    }
}
