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

package com.felayga.unpixeldungeon.actors.mobs.kobold;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.kobold.KoboldZombieSprite;

/**
 * Created by HELLO on 5/21/2016.
 */
public class KoboldZombie extends Mob {
    public KoboldZombie()
    {
        super(0);

        name = "kobold zombie";
        spriteClass = KoboldZombieSprite.class;

        movementSpeed(GameTime.TICK * 2);
        attackSpeed(GameTime.TICK);
        defenseMundane = 10;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 400;
        nutrition = 50;
        immunityMagical = MagicType.Cold.value | MagicType.Poison.value | MagicType.Sleep.value;
        corpseEffects = CorpseEffect.value(CorpseEffect.Rotten, CorpseEffect.Poisonous);
        characteristics = Characteristic.value(Characteristic.CannotUseItems, Characteristic.NonBreather, Characteristic.Brainless, Characteristic.Humanoid);

        belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 1, 4));
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
