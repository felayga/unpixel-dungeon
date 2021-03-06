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

package com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob;

/**
 * Created by HELLO on 3/9/2016.
 */


import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.MeleeWeapon;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class MeleeMobAttack extends MeleeWeapon {
    public MeleeMobAttack(long delay, int damageMin, int damageMax) {
        this(delay, damageMin, damageMax, MagicType.Mundane);
    }

    public MeleeMobAttack(long delay, int damageMin, int damageMax, MagicType damageType) {
        super(WeaponSkill.None, delay, damageMin, damageMax );
        this.damageType = damageType;

        name = "meleemobattack";
        image = ItemSpriteSheet.PLACEHOLDER_WEAPON;
        droppable = false;
    }

    @Override
    public String desc() {
        return "THAT'S NOT FOR YOU TO KNOW.";
    }
}
