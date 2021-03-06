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

package com.felayga.unpixeldungeon.items.equippableitem.weapon;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;

/**
 * Created by HELLO on 7/7/2016.
 */
public interface IWeapon {
    WeaponSkill skillRequired();

    AttributeType accuracyAttribute();
    AttributeType damageAttribute();

    MagicType damageType();
    Material material();

    int accuracyModifier();
    int damageRoll();

    Item self();

    int proc(Char attacker, boolean thrown, Char defender, int damage);
}
