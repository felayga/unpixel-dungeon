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
package com.felayga.unpixeldungeon.items.tools;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.EquipableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.KindofMisc;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.WndOptions;

import java.util.ArrayList;

/**
 * Created by hello on 12/21/15.
 */
public abstract class Tool extends Item {
    private static Tool curTool;

    public Tool() {

    }

    public abstract String getToolClass();

    @Override
    public boolean execute(Hero hero, String action) {
        curUser = hero;
        curTool = this;

        if (action.equals(AC_APPLY)) {
            GameScene.selectCell(applier);

            return false;
        }
        else {
            return super.execute(hero, action);
        }
    }

    public abstract void apply(Hero hero, int target);

    protected static CellSelector.Listener applier = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                curTool.apply(curUser, target);
            }
        }

        @Override
        public String prompt() {
            return "Apply " + curTool.getToolClass();
        }
    };

}
