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

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.food.CannedFood;
import com.felayga.unpixeldungeon.items.food.Corpse;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 7/9/2016.
 */
public class CanningKit extends Tool {
    public CanningKit() {
        super(false, true, WndBackpack.Mode.CORPSE_FRESH, null);

        name = "canning kit";
        image = ItemSpriteSheet.TOOL_CANNINGKIT;

        hasLevels(false);

        defaultAction = Constant.Action.APPLY;
    }

    private static final String CUR_CHARGES = "curCharges";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CUR_CHARGES, curCharges);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        curCharges = bundle.getInt(CUR_CHARGES);
    }

    @Override
    public String getToolClass() {
        return "canning kit";
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (curCharges > 0) {
            actions.add(Constant.Action.APPLY);
        }
        return actions;
    }

    private int curCharges = 0;

    @Override
    public Item random() {
        super.random();

        curCharges = Random.IntRange(30, 100);

        return this;
    }

    @Override
    public String status() {
        return curCharges + "";
    }

    @Override
    protected void doApply(Hero hero) {
        if (curCharges > 0) {
            super.doApply(hero);
        } else {
            GLog.w("This canning kit is out of materials.");
        }
    }

    @Override
    public void apply(Hero hero, int target) {
        //nothing
    }

    @Override
    public void apply(Hero hero, Item item) {
        if (!(item instanceof Corpse)) {
            return;
        }

        Corpse test = (Corpse) item;

        if (!test.partiallyEaten) {
            Corpse corpse = (Corpse) item.parent().remove(item, 1);
            hero.belongings.collect(new CannedFood(corpse, true).bucStatus(this));
            curCharges--;
            updateQuickslot();
        } else {
            GLog.n("Eh, it's been partially eaten.");
        }
    }

    @Override
    public void apply(Hero hero) {
        //nothing
    }

    @Override
    public String desc() {
        return "This is a collection of the materials required to purify and preserve monster corpses for later consumption." +
                "\n\nThere are enough supplies left to prepare " + curCharges + " more can" + (curCharges != 1 ? "s" : "") + ".";
    }
}
