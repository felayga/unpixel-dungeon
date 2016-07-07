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

package com.felayga.unpixeldungeon.items.tools.unlocking;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.tools.ITool;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.utils.GLog;

import java.util.ArrayList;

/**
 * Created by hello on 3/13/16.
 */
public class UnlockingTool extends Tool implements ITool {
    public static final String NAME = "unlocking tool";

    public UnlockingTool()
    {
        super();

        defaultAction = Constant.Action.APPLY;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Constant.Action.APPLY);
        return actions;
    }

    public boolean unlockDoor(Char user) {
        return false;
    }

    public boolean unlockChest(Char user) {
        return false;
    }

    @Override
    public final String getToolClass() {
        return NAME;
    }

    @Override
    public void apply(Hero hero, int target) {
        int cell = Dungeon.level.map[target];

        if (cell == Terrain.LOCKED_DOOR) {
            hero.curAction = new HeroAction.HandleDoor.UnlockDoor(target, this);
            hero.motivate(true);
            return;
        }

        Heap heap = Dungeon.level.heaps.get(target);
        if (heap != null) {
            Item heapItem = heap.peek();

            if (heapItem != null && heapItem instanceof Bag) {
                Bag bag = (Bag)heapItem;

                hero.curAction = new HeroAction.UseItem.UnlockBag(this, bag, cell);
                hero.motivate(true);
                return;
            }
        }

        GLog.n("Your " + NAME + " can't be applied there.");
    }

}
