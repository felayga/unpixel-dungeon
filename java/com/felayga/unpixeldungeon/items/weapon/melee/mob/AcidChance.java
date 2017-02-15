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

package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.AcidBurning;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class AcidChance extends MeleeMobAttack {
    public AcidChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean ranged, Char target, int damage) {
        damage = super.proc(attacker, ranged, target, damage);

        if (Random.Int(3) == 0) {
            Buff.affect(target, attacker, AcidBurning.class).resplash(target);
            target.sprite.burst(0x000000, 5);
        }

        /*
        if (attacker instanceof Goo)
        {
            Goo goo = (Goo)attacker;

            if (goo.pumpedUp > 0) {
                Camera.main.shake(3, 0.2f);
            }
        }
        */

        return damage;
    }

}
