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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.spellcasting.CreateMonsterSpellcaster;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.watabou.utils.Random;

public class SummoningTrap extends Trap {

    {
        name = "Summoning trap";
        color = TrapSprite.TEAL;
        shape = TrapSprite.WAVES;
    }

    @Override
    public void activate() {
        //todo: no summons if boss present?

        int nMobs = 1;
        while (Random.Int(2) == 0 && nMobs < 5) {
            nMobs++;
        }

        CreateMonsterSpellcaster.spawnMobs(pos, nMobs);
    }

    @Override
    public String desc() {
        return "Triggering this trap will summon a number of monsters from the surrounding floors to this location.";
    }
}
