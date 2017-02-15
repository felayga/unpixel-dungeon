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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.ItemVision;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;

/**
 * Created by HELLO on 2/3/2017.
 */
public class PotionOfObjectDetection extends Potion {

    public PotionOfObjectDetection()
    {
        name = "Potion of Item Awareness";
        initials = "IA";

        isHelpful = true;

        price = 35;
    }

    @Override
    public void apply( Char hero ) {
        setKnown();
        //todo: item vision duration
        Buff.affect(hero, hero, ItemVision.class, GameTime.TICK * 4);
        Dungeon.observe();

        if (Dungeon.level.heaps.size() > 0) {
            GLog.i("You are somehow aware of items in the dungeon!");
        } else {
            GLog.i( "You can somehow tell that there are no items on this level." );
        }
    }

    @Override
    public String desc() {
        return
                "After drinking this, your mind will become attuned to the magical signature " +
                        "of distant items, enabling you to sense their presence through walls. " +
                        "Also this potion will permit you to see through nearby walls and doors.";
    }

}
