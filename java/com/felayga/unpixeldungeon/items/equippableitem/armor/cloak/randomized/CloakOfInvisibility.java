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

package com.felayga.unpixeldungeon.items.equippableitem.armor.cloak.randomized;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;

/**
 * Created by HELLO on 2/12/2017.
 */
public class CloakOfInvisibility extends RandomizedCloak {
    private Invisibility.Indefinite invisibility;

    public CloakOfInvisibility() {
        super(1, 2, GameTime.TICK);

        name = "cloak of invisibility";
        price = 60;

        weight(Encumbrance.UNIT * 10);
    }

    @Override
    public void onEquip(Char owner, boolean cursed) {
        super.onEquip(owner, cursed);

        invisibility = Invisibility.Indefinite.prolong(owner, null, CloakOfInvisibilityInvisibility.class);
        if (owner == Dungeon.hero) {
            Hero hero = (Hero)owner;
            hero.motivate(true);
        }

        defaultAction = Constant.Action.UNEQUIP;
    }

    @Override
    public void onUnequip(Char owner) {
        super.onUnequip(owner);

        CloakOfInvisibilityInvisibility.detach(invisibility);
        invisibility = null;

        defaultAction = Constant.Action.EQUIP;
    }

    public static class CloakOfInvisibilityInvisibility extends Invisibility.Indefinite {
        {
            improved(true);
        }
    }
}
