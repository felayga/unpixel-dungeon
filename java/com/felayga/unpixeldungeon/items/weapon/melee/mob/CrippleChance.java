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

package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class CrippleChance extends MeleeMobAttack {
    private int chance;
    private long duration;

    public CrippleChance(long delay, int damageMin, int damageMax, int chance, long duration) {
        super(delay, damageMin, damageMax);

        this.chance = chance;
        this.duration = duration;
    }

    @Override
    public int proc(Char attacker, boolean ranged, Char target, int damage)
    {
        damage = super.proc(attacker, ranged, target, damage);

        if ((chance > 1 && Random.Int(chance) == 0) || chance <= 1) {
            Buff.prolong(target, Cripple.class, duration);
        }

        return damage;
    }
}
