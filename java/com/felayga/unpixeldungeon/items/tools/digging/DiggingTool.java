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

package com.felayga.unpixeldungeon.items.tools.digging;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.tools.ITool;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.utils.GLog;

/**
 * Created by HELLO on 3/14/2016.
 */
public class DiggingTool extends Tool implements ITool {
    public static final String NAME = "digging tool";

    public DiggingTool() {
        super();

        defaultAction = AC_APPLY;
    }

    @Override
    public final String getToolClass() {
        return NAME;
    }

    @Override
    public void apply(Hero hero, int target) {
        int cell = Dungeon.level.map[target];

        {
            GLog.n("Your " + NAME + " can't be applied there.");
        }
    }
}
