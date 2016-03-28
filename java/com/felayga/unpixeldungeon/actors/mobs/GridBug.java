/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.actors.mobs;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.GridBugSprite;
import com.felayga.unpixeldungeon.sprites.RatSprite;
import com.watabou.utils.Random;

public class GridBug extends Mob {

    public GridBug()
    {
        name = "grid bug";
        spriteClass = GridBugSprite.class;

        DEXCHA = 8;

        HP = HT = 8;
        defenseSkill = 2;

        maxLvl = 5;

        belongings.weapon = new MeleeMobAttack(GameTime.TICK, 1, 1);
    }

    @Override
    public String description() {
        return
                "grid bug boop";
    }
}
