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

package com.felayga.unpixeldungeon.levels.branches;

import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 6/17/2016.
 */
public enum DungeonBranch {
    Elemental   ("E", -4, 0, -5, -5, false),
    Normal      ("", 1, 30, -5, -5, true),
    Mines       ("M", 32, 40, 2, 4, true),
    Puzzle      ("P", 42, 45, 5, 9, false);

    public final String designator;
    public final int levelMin;
    public final int levelMax;
    public final boolean branchDown;

    private int branchLevel = -1048576;
    public int branchLevel() {
        GLog.d("branchLevel="+branchLevel+" for this="+this.name());
        return branchLevel;
    }

    DungeonBranch(String designator, int levelMin, int levelMax, int entranceMin, int entranceMax, boolean branchDown) {
        this.designator = designator;
        this.levelMin = levelMin;
        this.levelMax = levelMax;

        if (entranceMin > 0 && entranceMax > 0) {
            this.branchLevel = Random.IntRange(entranceMin, entranceMax);
        }
        else {
            this.branchLevel = 0;
        }
        this.branchDown = branchDown;
    }

    public static final int MINLEVEL = -4;
    public static final int MAXLEVEL = 256 + MINLEVEL;

    public static String getDepthText(int depth) {
        for (DungeonBranch branch : DungeonBranch.values()) {
            if (depth >= branch.levelMin && depth <= branch.levelMax) {
                return branch.designator + (depth - branch.levelMin + 1);
            }
        }

        return "?" + depth;
    }

    public static int getAdjustedDepth(int depth) {
        DungeonBranch branch = currentBranch(depth);

        if (branch != null) {
            return branch.branchLevel + (depth - branch.levelMin + 1);
        }

        return depth;
    }

    public static DungeonBranch currentBranch(int depth) {
        for (DungeonBranch branch : DungeonBranch.values()) {
            if (depth >= branch.levelMin && depth <= branch.levelMax) {
                return branch;
            }
        }

        return null;
    }

    public static DungeonBranch returnBranch(int curDepth, int offset) {
        int index = curDepth + offset;
        for (DungeonBranch branch : DungeonBranch.values()) {
            int test;
            if (branch.branchDown) {
                test = branch.levelMin - 1;
            }
            else {
                test = branch.levelMax + 1;
            }

            if (test == index) {
                return branch;
            }
        }

        return null;
    }

    public static DungeonBranch destinationBranch(int curLevel) {
        for (DungeonBranch branch : DungeonBranch.values()) {
            if (branch.branchLevel == curLevel) {
                return branch;
            }
        }

        return null;
    }

    private static final String MINES =     "minesBranch";
    private static final String PUZZLE =    "puzzleBranch";

    public static void save(Bundle bundle) {
        bundle.put(MINES, Mines.branchLevel);
        bundle.put(PUZZLE, Puzzle.branchLevel);
    }

    public static void restore(Bundle bundle) {
        Mines.branchLevel = bundle.getInt(MINES);
        Puzzle.branchLevel = bundle.getInt(PUZZLE);
    }
}
