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

package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.utils.GLog;

/**
 * Created by hello on 3/16/16.
 */
public class BlankScroll extends Scroll {
    public BlankScroll()
    {
        name = "Blank Scroll";
        initials = "--";
    }

    @Override
    public boolean execute(Hero hero, String action) {
        if (action.equals(AC_READ)) {
            GLog.n("This scroll is blank.");
            identify();
            return false;
        } else {
            return super.execute(hero, action);
        }
    }

    @Override
    public void doRead() {
    }
}
