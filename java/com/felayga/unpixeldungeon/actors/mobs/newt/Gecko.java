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

package com.felayga.unpixeldungeon.actors.mobs.newt;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.newt.GeckoSprite;

/**
 * Created by HELLO on 6/3/2016.
 */
public class Gecko extends Mob {
    public Gecko()
    {
        super(1);

        name = "gecko";
        spriteClass = GeckoSprite.class;

        experience = 8;
        movementSpeed = GameTime.TICK / 2;
        attackSpeed = GameTime.TICK;
        defenseMundane = 12;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 10;
        nutrition = 20;
        immunityMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.ManaBoost.value;

        belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 1, 3));
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
