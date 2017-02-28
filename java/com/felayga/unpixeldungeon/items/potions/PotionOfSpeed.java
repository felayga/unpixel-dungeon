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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.positive.Haste;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/4/2017.
 */
public class PotionOfSpeed extends Potion {

    public PotionOfSpeed() {
        name = "Potion of Speed";
        initials = "Sp";

        bones = true;

        price = 200;
    }

    @Override
    public void apply(Char hero) {
        setKnown();

        Buff.detach(hero, Cripple.class);

        long duration = Random.Int(20);
        switch (bucStatus()) {
            case Blessed:
                duration += 150;
                break;
            case Cursed:
                duration += 30;
                break;
            default:
                duration += 90;
                break;
        }
        Buff.affect(hero, hero, Haste.class, duration * GameTime.TICK);
    }

    @Override
    public String desc() {
        return
                "nope";
    }

}


