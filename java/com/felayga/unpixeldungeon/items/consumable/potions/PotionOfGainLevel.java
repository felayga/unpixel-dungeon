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

package com.felayga.unpixeldungeon.items.consumable.potions;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;

/**
 * Created by HELLO on 2/4/2017.
 */
public class PotionOfGainLevel extends Potion {

    public PotionOfGainLevel()
    {
        name = "Potion of Gain Level";
        initials = "GL";

        bones = true;
        isHelpful = true;

        price = 80;
    }

    @Override
    public void apply( Char c ) {
        setKnown();

        if (c == Dungeon.hero) {
            Hero hero = (Hero) c;
            hero.earnExp(hero.maxExp());
        }
    }

    @Override
    public String desc() {
        return
                "The storied experiences of multitudes of battles reduced to liquid form, " +
                        "this draught will instantly raise your experience level.";
    }

}

