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

package com.felayga.unpixeldungeon.spellcasting;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.watabou.utils.SparseArray;

import java.util.HashSet;
import java.util.List;

/**
 * Created by HELLO on 3/11/2017.
 */

public class AreaSleepSpellcaster extends SleepSpellcaster {
    public AreaSleepSpellcaster() {
        super("Sleep", 3);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicRay, Ballistica.Mode.StopSelf);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        HashSet<Integer> spots = Level.getRadius(targetPos, 6 + levelBoost);
        spots.remove(source.pos());

        SparseArray<Char> chars = Actor.chars();
        for (int n = 0; n < chars.size(); n++) {
            Char target = chars.valueAt(n);
            int pos = target.pos();

            if (spots.contains(pos) && Level.fieldOfSound[pos]) {
                super.onZap(source, null, pos);
            }
        }
    }
}
