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

package com.felayga.unpixeldungeon.items.weapon.ranged;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;

import java.util.ArrayList;

/**
 * Created by HELLO on 6/9/2016.
 */
public class RangedWeapon extends Weapon {
    protected AmmunitionType ammunitionType;

    public RangedWeapon(WeaponSkill weaponSkill, long delay, int damageMin, int damageMax, AmmunitionType ammunitionType) {
        super(weaponSkill, delay, damageMin, damageMax);

        this.ammunitionType = ammunitionType;

        stackable = false;
        levelKnown = false;

        defaultAction = Constant.Action.SHOOT;

        usesTargeting = true;
    }

    public Slot[] getSlots() {
        return new Slot[]{ Slot.Weapon };
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );

        if (isEquipped(hero)) {
            actions.add(Constant.Action.SHOOT);
        }

        return actions;
    }

    @Override
    public String info() {

        StringBuilder info = new StringBuilder( desc() );

        info.append( "\n\nAverage damage of this weapon equals to " + (damageMin + (damageMax - damageMin) / 2) + " points per hit. " );

		/*
		if (Dungeon.hero.belongings.backpack.items.contains( this )) {
			if (STR > Dungeon.hero.STR()) {
				info.append(
					"\n\nBecause of your inadequate strength the accuracy and speed " +
					"of your attack with this " + name + " is decreased." );
			}
			if (STR < Dungeon.hero.STR() && Dungeon.hero.heroClass == HeroClass.HUNTRESS) {
				info.append(
					"\n\nBecause of your excess strength the damage " +
					"of your attack with this " + name + " is increased." );
			}
		}
		*/
        info.append( "\n\nAs this weapon is designed to be used at a distance, it is much less accurate if used at melee range.");

        if (isEquipped( Dungeon.hero )) {
            info.append( "\n\nYou hold the " + name + " at the ready." );
        }

        return info.toString();
    }
}
