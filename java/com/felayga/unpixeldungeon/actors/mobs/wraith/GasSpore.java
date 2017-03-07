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

package com.felayga.unpixeldungeon.actors.mobs.wraith;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.Bomb;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.wraith.GasSporeSprite;

/**
 * Created by HELLO on 6/9/2016.
 */
public class GasSpore extends Mob {
    public GasSpore() {
        super(1);

        name = "gas spore";
        spriteClass = GasSporeSprite.class;

        movementSpeed(GameTime.TICK * 12);
        attackSpeed(GameTime.TICK);
        flying(true);
        defenseMundane = 10;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 10;
        nutrition = 0;
        resistanceMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.None.value;
        corpseResistances = MagicType.None.value;
        characteristics = Characteristic.value(Characteristic.NonBreather, Characteristic.Brainless, Characteristic.CannotUseItems);

        belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 0, 0));
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public void die(Actor cause) {
        super.die(cause);

        Bomb.explode(this, null, pos(), false, MagicType.Mundane, 4, 24);
    }


    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
