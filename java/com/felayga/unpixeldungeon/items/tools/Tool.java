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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.noosa.audio.Sample;

/**
 * Created by hello on 12/21/15.
 */
public abstract class Tool extends Item implements ITool {
    private static Tool curTool;

    private boolean applyCell;
    private boolean applyItem;
    private WndBackpack.Mode applyItemMode;
    private Class<?> applyItemClass;

    public Tool(boolean applyCell, boolean applyItem) {
        this(applyCell, applyItem, WndBackpack.Mode.ALL, null);

        if (applyItem) {
            GLog.d("bad constructor for applyItem=true");
            GLog.d(""+1/0);
        }
    }

    public Tool(boolean applyCell, boolean applyItem, WndBackpack.Mode applyItemMode, Class<?> applyItemClass) {
        this.applyCell = applyCell;
        this.applyItem = applyItem;
        this.applyItemMode = applyItemMode;
        this.applyItemClass = applyItemClass;
    }

    public abstract String getToolClass();

    @Override
    public boolean execute(Hero hero, String action) {
        curUser = hero;
        curTool = this;

        if (action.equals(Constant.Action.APPLY)) {
            if (applyCell && applyItem) {
            } else if (applyCell) {
                GameScene.selectCell(cellApplier);
            } else if (applyItem) {
                GameScene.selectItem(itemApplier, applyItemMode, applyItemClass, "Apply " + this.getToolClass(), this);
            }

            return false;
        }
        else {
            return super.execute(hero, action);
        }
    }

    public abstract void apply(Hero hero, int target);

    public static CellSelector.Listener cellApplier = new CellSelector.Listener() {
        @Override
        public boolean onSelect(Integer target) {
            if (target != null) {
                curTool.apply(curUser, target);
            }
            return true;
        }

        @Override
        public String prompt() {
            return "Apply " + curTool.getToolClass();
        }
    };


    public abstract void apply(Hero hero, Item target);

    protected static WndBackpack.Listener itemApplier = new WndBackpack.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null) {
                curTool.apply(curUser, item);
            }
        }
    };

}
