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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.Weapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.simple.Knuckles;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class DisarmChance extends MeleeMobAttack {
    public static final String TXT_DISARM	= "%s has knocked the %s from your hands!";

    public DisarmChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    public int hitsToDisarm = 0;

    @Override
    public int proc(Char attacker, boolean ranged, Char target, int damage)
    {
        damage = super.proc(attacker, ranged, target, damage);

        if (target == Dungeon.hero) {

            Hero hero = Dungeon.hero;
            Weapon weapon = (Weapon)hero.belongings.weapon();

            if (weapon != null && !(weapon instanceof Knuckles) && weapon.bucStatus() != BUCStatus.Cursed) {
                if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(4, 8);

                if (--hitsToDisarm == 0) {
                    hero.belongings.unequip(weapon, false);
                    Dungeon.quickslot.clearItem(weapon);
                    weapon.updateQuickslot();
                    Dungeon.level.drop(weapon, hero.pos()).sprite.drop();
                    GLog.w(TXT_DISARM, name, weapon.getDisplayName());
                }
            }
        }

        return damage;
    }

}
