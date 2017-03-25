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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.equippableitem.amulet.mask.Mask;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;

import java.util.ArrayList;

/**
 * Created by HELLO on 2/6/2017.
 */
public class Towel extends Mask implements ITool {
    private Blindness.Indefinite blindness;

    public Towel() {
        super(0, 0, GameTime.TICK);

        name = "Towel";
        image = ItemSpriteSheet.TOOL_TOWEL;
        material = Material.Cloth;

        hasLevels(false);

        defaultAction = Constant.Action.APPLY;

        price = 50;
        weight(2 * Encumbrance.UNIT);
    }

    public String getToolClass() {
        return "towel";
    }

    @Override
    public void onEquip(Char owner, boolean cursed) {
        super.onEquip(owner, cursed);

        GLog.i("You wrap the towel around your head.");

        blindness = Blindness.Indefinite.prolong(owner, owner, TowelBlindness.class);
        if (owner == Dungeon.hero) {
            Hero hero = (Hero)owner;
            hero.motivate(true);
        }
    }

    @Override
    public void onUnequip(Char owner) {
        super.onUnequip(owner);

        TowelBlindness.detach(blindness);
        blindness = null;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Constant.Action.APPLY);
        return actions;
    }

    private Towel curTool;

    @Override
    public boolean execute(Hero hero, String action) {
        curUser = hero;
        curTool = this;

        if (action.equals(Constant.Action.APPLY)) {
            if (isEquipped(hero)) {
                GLog.n("You can't do that while the towel is wrapped around your head.");
            } else {
                GLog.i("Your face and hands are already clean.");
            }

            return false;
        }
        else {
            return super.execute(hero, action);
        }
    }

    public static class TowelBlindness extends Blindness.Indefinite {
    }
}

