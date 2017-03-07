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

package com.felayga.unpixeldungeon.items.tools;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.food.CannedFood;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.windows.WndBackpack;

import java.util.ArrayList;

/**
 * Created by HELLO on 2/23/2017.
 */

public class CanOpener extends Tool {
    public CanOpener() {
        super(false, true, WndBackpack.Mode.INSTANCEOF, CannedFood.class);

        name = "can opener";
        image = ItemSpriteSheet.TOOL_CANOPENER;
        material = Material.Iron;

        hasLevels(false);
        hasBuc(false);

        defaultAction = Constant.Action.APPLY;

        price = 30;
        weight(4 * Encumbrance.UNIT);
    }

    public String getToolClass() {
        return "can opener";
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Constant.Action.APPLY);
        return actions;
    }

    @Override
    public void apply(Hero hero, int pos) {
        //nothing
    }

    @Override
    public void apply(Hero hero, Item item){
        if (!(item instanceof CannedFood)) {
            return;
        }

        CannedFood can = (CannedFood)item;

        can.execute(hero, Constant.Action.OPEN);
    }

    @Override
    public void apply(Hero hero) {
        //nothing
    }

    @Override
    public String desc() {
        return "This tool is designed for efficiently opening canned foods.\nIt's not useful for much else.";
    }

}
