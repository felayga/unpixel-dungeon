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

package com.felayga.unpixeldungeon.items.armor.amulet.mask;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by HELLO on 2/6/2017.
 */
public class Blindfold extends Mask {
    private Blindness.Indefinite blindness;

    public Blindfold() {
        super(0, 0, GameTime.TICK);

        name = "Blindfold";
        image = ItemSpriteSheet.TOOL_BLINDFOLD;
        material = Material.Cloth;

        hasLevels(false);

        defaultAction = Constant.Action.EQUIP;

        weight(2 * Encumbrance.UNIT);
        price = 20;
    }

    @Override
    public void onEquip(Char owner, boolean cursed) {
        super.onEquip(owner, cursed);

        blindness = Blindness.Indefinite.prolong(owner, null, BlindfoldBlindness.class);
        if (owner == Dungeon.hero) {
            Hero hero = (Hero)owner;
            hero.motivate(true);
        }

        defaultAction = Constant.Action.UNEQUIP;
    }

    @Override
    public void onUnequip(Char owner) {
        super.onUnequip(owner);

        BlindfoldBlindness.detach(blindness);
        blindness = null;

        defaultAction = Constant.Action.EQUIP;
    }

    public static class BlindfoldBlindness extends Blindness.Indefinite {
    }
}
