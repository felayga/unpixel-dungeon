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

package com.felayga.unpixeldungeon.plants;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.items.Dewdrop;
import com.felayga.unpixeldungeon.levels.Level;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 3/3/2017.
 */
public class Dewcatcher extends Plant {

    public Dewcatcher() {
        super("Dewcatcher", 12);
    }

    @Override
    public void activate() {

        int nDrops = Random.NormalIntRange(2, 8);

        ArrayList<Integer> candidates = new ArrayList<Integer>();
        for (int i : Level.NEIGHBOURS8) {
            if (Level.passable[pos + i]) {
                candidates.add(pos + i);
            }
        }

        for (int i = 0; i < nDrops && !candidates.isEmpty(); i++) {
            Integer c = Random.element(candidates);
            Dungeon.level.drop(new Dewdrop(), c).sprite.drop(pos);
            candidates.remove(c);
        }

    }

    @Override
    public String desc() {
        return "Dewcatchers camouflage as grass to avoid attention, " +
                "but their bulges of collected dew give them away.";
    }

    //seed is never dropped, only care about plant class
    public static class Seed extends Plant.Seed {
        {
            plantClass = Dewcatcher.class;
        }
    }
}

