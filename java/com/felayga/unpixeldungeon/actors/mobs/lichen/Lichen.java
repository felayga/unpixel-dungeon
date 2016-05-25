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

package com.felayga.unpixeldungeon.actors.mobs.lichen;

import com.felayga.unpixeldungeon.actors.buffs.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.LichenSprite;
import com.felayga.unpixeldungeon.sprites.mobs.jackal.FoxSprite;

/**
 * Created by HELLO on 5/22/2016.
 */
public class Lichen extends Mob {

    public Lichen()
    {
        super(0);

        name = "lichen";
        spriteClass = LichenSprite.class;

        experience = 4;
        movementSpeed = 1;
        attackSpeed = GameTime.TICK;
        defenseMundane = 11;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 20;
        nutrition = 200;
        immunityMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.Unrottable.value | CorpseEffect.Undecayable.value;

        belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 0, 0));
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
