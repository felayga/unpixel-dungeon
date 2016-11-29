/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */
package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Hallucination;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.plants.Blindweed;
import com.felayga.unpixeldungeon.plants.Swampweed;

public class PotionOfHallucination extends Potion {

    public PotionOfHallucination()
    {
        name = "Potion of Hallucination";
        initials = "Ha";

        bones = true;
        isHelpful = false;

        price = 30;

        alchemyPrimary = Swampweed.Seed.class;
        alchemySecondary = Blindweed.Seed.class;
    }

    @Override
    public void apply( Char hero ) {
        setKnown();
        Buff.affect(Dungeon.hero, Hallucination.class, 200 * GameTime.TICK);
    }

    @Override
    public String desc() {
        return
                "An elixir that is probably illegal in most civilized places.";
    }
}
