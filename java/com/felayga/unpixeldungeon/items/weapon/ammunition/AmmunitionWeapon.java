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

package com.felayga.unpixeldungeon.items.weapon.ammunition;

import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.items.weapon.ranged.RangedWeapon;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;

/**
 * Created by HELLO on 6/12/2016.
 */
public class AmmunitionWeapon extends MissileWeapon {
    public AmmunitionWeapon(WeaponSkill weaponSkill, long delay, int damageMin, int damageMax, int quantity, boolean throwable, AmmunitionType ammunitionType) {
        super(weaponSkill, delay, damageMin, damageMax, quantity, throwable, ammunitionType);
    }

    public RangedWeapon launcher;

    @Override
    public int damageRoll() {
        int retval = super.damageRoll();

        if (launcher != null) {
            retval += launcher.damageRoll();
            launcher = null;
        }

        return retval;
    }

    @Override
    public int accuracyModifier() {
        int retval = super.accuracyModifier();

        if (launcher != null) {
            retval += launcher.accuracyModifier();
        }

        return retval;
    }
}
