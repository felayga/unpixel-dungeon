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

package com.felayga.unpixeldungeon.items.potions;

/**
 * Created by HELLO on 11/10/2016.
 */

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.utils.GLog;

public class PotionOfFruitJuice extends Potion {

    public PotionOfFruitJuice()
    {
        name = "Potion of Fruit Juice";
        initials = "FJ";

        bones = true;

        price = 30;
    }

    @Override
    public void apply( Char hero ) {
        setKnown();

        GLog.p("This tastes like fruit juice.");

        switch(bucStatus) {
            case Cursed:
                hero.buff(Hunger.class).satisfy_new(250);
                break;
            case Blessed:
                hero.buff(Hunger.class).satisfy_new(750);
                //no more effects
                return;
            default:
                hero.buff(Hunger.class).satisfy_new(500);
                break;
        }
    }

    @Override
    public String desc() {
        return "A nutritious blend of various fruits.";
    }

}
