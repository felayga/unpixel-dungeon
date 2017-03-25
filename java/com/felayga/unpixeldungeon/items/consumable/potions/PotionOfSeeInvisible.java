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
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.SeeInvisible;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/4/2017.
 */
public class PotionOfSeeInvisible extends Potion {

    public PotionOfSeeInvisible()
    {
        name = "Potion of See Invisible";
        initials = "SIn";

        bones = true;

        price = 30;
    }

    @Override
    public void apply( Char hero ) {
        if (hero == Dungeon.hero) {
            switch (bucStatus()) {
                case Cursed:
                    GLog.w("This tastes like rotten fruit juice.");
                    break;
                default:
                    GLog.i("This tastes like fruit juice.");
                    break;
            }
        }

        switch (bucStatus()) {
            case Blessed:
                Buff.affect(hero, hero, SeeInvisible.Intrinsic.class);
                return;
            default:
                Buff.prolong(hero, hero, SeeInvisible.class, GameTime.TICK * Random.IntRange(750, 850));
                break;
        }
    }

    @Override
    public String desc() {
        return
                "nope";
    }

}


