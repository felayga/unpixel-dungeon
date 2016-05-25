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
package com.felayga.unpixeldungeon.actors.mobs;

import android.util.Log;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.mobs.bug.GridBug;
import com.felayga.unpixeldungeon.actors.mobs.jackal.Fox;
import com.felayga.unpixeldungeon.actors.mobs.jackal.Jackal;
import com.felayga.unpixeldungeon.actors.mobs.kobold.Kobold;
import com.felayga.unpixeldungeon.actors.mobs.kobold.KoboldZombie;
import com.felayga.unpixeldungeon.actors.mobs.lichen.Lichen;
import com.felayga.unpixeldungeon.actors.mobs.newt.Newt;
//import com.felayga.unpixeldungeon.actors.mobs.npcs.Ghost;
import com.felayga.unpixeldungeon.actors.mobs.orc.Goblin;
import com.felayga.unpixeldungeon.actors.mobs.rat.SewerRat;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Bestiary {

    public static Mob mob(int depth, int heroLevel) {
        @SuppressWarnings("unchecked")
        Class<? extends Mob> cl = (Class<? extends Mob>) mobClass(depth, heroLevel);
        try {
            return cl.newInstance();
        } catch (Exception e) {
            GLog.d("it's bad, mmmkay");
            GLog.d(e);
            return null;
        }
    }

    public static Mob mutable(int depth, int heroLevel) {
        @SuppressWarnings("unchecked")
        Class<? extends Mob> cl = (Class<? extends Mob>) mobClass(depth, heroLevel);

		/*
		if (Random.Int( 30 ) == 0) {
			if (cl == MarsupialRat.class) {
				cl = MarsupialRatAlbino.class;
			} else if (cl == Thief.class) {
				cl = Bandit.class;
			} else if (cl == Brute.class) {
				cl = Shielded.class;
			} else if (cl == Monk.class) {
				cl = Senior.class;
			} else if (cl == Scorpio.class) {
				cl = Acidic.class;
			}
		}
		*/

        try {
            return cl.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private static Class<?> mobClass(int depth, int heroLevel) {
        float[] chances;
        Class<?>[] classes;

        int min = depth / 7;
        int max = (depth + heroLevel) / 2;

        if (min > max) {
            int swap = min;
            min = max;
            max = swap;
        }

        int test = Random.Int(min, max + 1);

        switch (test) {
            case 0:
                chances = new float[]{3, 3, 1, 5, 1, 1, 4, 2, 1};
                classes = new Class<?>[]{GridBug.class, Jackal.class, KoboldZombie.class, Newt.class, SewerRat.class, Fox.class, Lichen.class, Goblin.class, Kobold.class};
                break;

            default:
                chances = new float[]{1};
                classes = new Class<?>[]{GridBug.class};
        }

        return classes[Random.chances(chances)];
    }

    public static boolean isUnique(Char mob) {
		/*
		return mob instanceof Goo || mob instanceof Tengu || mob instanceof DM300 || mob instanceof King
				|| mob instanceof Yog.BurningFist || mob instanceof Yog.RottingFist
			|| mob instanceof Ghost.MarisupialRatFetid || mob instanceof Ghost.GnollTrickster || mob instanceof Ghost.GreatCrab;
		*/

        return false;
    }
}
