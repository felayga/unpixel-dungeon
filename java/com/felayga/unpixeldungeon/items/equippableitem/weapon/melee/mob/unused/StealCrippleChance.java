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

package com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.unused;
/*
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.watabou.utils.Random;

// Created by HELLO on 3/9/2016.
public class StealCrippleChance extends StealChance {
    public StealCrippleChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    protected boolean steal( Char attacker, Hero hero ){
        boolean retval = super.steal(attacker, hero);

        if (retval)
        {
            Buff.prolong(hero, Blindness.class, Random.Int(2, 5));
            Buff.affect( hero, Poison.class ).set(Random.Int(5, 7) * Poison.durationFactor(attacker));
            Buff.prolong( hero, Cripple.class, Random.Int( 3, 8 ) );
            Dungeon.observe();
        }

        return retval;
    }
}
*/