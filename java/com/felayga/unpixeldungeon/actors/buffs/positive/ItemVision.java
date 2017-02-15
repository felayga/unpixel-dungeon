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

package com.felayga.unpixeldungeon.actors.buffs.positive;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

/**
 * Created by HELLO on 2/14/2017.
 */
public class ItemVision extends FlavourBuff {

    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.ITEM_VISION;
    }

    @Override
    public String toString() {
        return "Item vision";
    }

    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
    }

    @Override
    public String desc() {
        return "Somehow you are able to see all items on this floor through your mind. It's a weird feeling.\n" +
                "\n" +
                "All loot on this floor is visible to you as long as you have item vision. " +
                "Seeing loot through mind vision counts as it being seen or nearby for " +
                "the purposes of many magical effects.\n" +
                "\n" +
                "The item vision will last for " + dispTurns() + ".";
    }
}

