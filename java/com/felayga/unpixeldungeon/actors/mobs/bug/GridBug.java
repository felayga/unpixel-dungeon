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
package com.felayga.unpixeldungeon.actors.mobs.bug;

import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Roll;
import com.felayga.unpixeldungeon.sprites.mobs.bug.GridBugSprite;

public class GridBug extends Mob {

    public GridBug()
    {
        super(0);

        name = "grid bug";
        spriteClass = GridBugSprite.class;

        DEXCHA = 8;

        experience = 1;
        movementSpeed = GameTime.TICK;
        attackSpeed = GameTime.TICK;
        defenseMundane = 9;
        defenseMagical = 0;

        maxLvl = 5;

        belongings.weapon = new MeleeMobAttack(GameTime.TICK, 1, 1);
    }

    @Override
    public String description() {
        return "grid bug boop";
    }
}
