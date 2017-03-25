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
package com.felayga.unpixeldungeon;

import com.felayga.unpixeldungeon.levels.branches.DungeonBranch;
import com.watabou.utils.Bundle;

public class Statistics {

    public static int goldCollected;
    public static boolean floorsVisited[];
    public static int enemiesSlain;
    public static int foodEaten;
    public static int potionsCooked;
    public static int piranhasKilled;
    public static int nightHunt;
    public static int ankhsUsed;

    public static float duration;

    public static boolean qualifiedForNoKilling = false;
    public static boolean completedWithNoKilling = false;

    public static boolean amuletObtained = false;

    public static void reset() {

        goldCollected = 0;
        floorsVisited = new boolean[DungeonBranch.MAXLEVEL - DungeonBranch.MINLEVEL];
        for (int n = DungeonBranch.MINLEVEL; n < DungeonBranch.MAXLEVEL; n++) {
            floorsVisited[n - DungeonBranch.MINLEVEL] = false;
        }

        enemiesSlain = 0;
        foodEaten = 0;
        potionsCooked = 0;
        piranhasKilled = 0;
        nightHunt = 0;
        ankhsUsed = 0;

        duration = 0;

        qualifiedForNoKilling = false;

        amuletObtained = false;

    }

    private static final String GOLD = "score";
    private static final String FLOORSVISITED = "floorsVisited";
    private static final String SLAIN = "enemiesSlain";
    private static final String FOOD = "foodEaten";
    private static final String ALCHEMY = "potionsCooked";
    private static final String PIRANHAS = "priranhas";
    private static final String NIGHT = "nightHunt";
    private static final String ANKHS = "ankhsUsed";
    private static final String DURATION = "duration";
    private static final String AMULET = "amuletObtained";

    public static void storeInBundle(Bundle bundle) {
        bundle.put(GOLD, goldCollected);
        bundle.put(FLOORSVISITED, floorsVisited);
        bundle.put(SLAIN, enemiesSlain);
        bundle.put(FOOD, foodEaten);
        bundle.put(ALCHEMY, potionsCooked);
        bundle.put(PIRANHAS, piranhasKilled);
        bundle.put(NIGHT, nightHunt);
        bundle.put(ANKHS, ankhsUsed);
        bundle.put(DURATION, duration);
        bundle.put(AMULET, amuletObtained);
    }

    public static void restoreFromBundle(Bundle bundle) {
        goldCollected = bundle.getInt(GOLD);
        floorsVisited = bundle.getBooleanArray(FLOORSVISITED);
        enemiesSlain = bundle.getInt(SLAIN);
        foodEaten = bundle.getInt(FOOD);
        potionsCooked = bundle.getInt(ALCHEMY);
        piranhasKilled = bundle.getInt(PIRANHAS);
        nightHunt = bundle.getInt(NIGHT);
        ankhsUsed = bundle.getInt(ANKHS);
        duration = bundle.getFloat(DURATION);
        amuletObtained = bundle.getBoolean(AMULET);
    }

}
