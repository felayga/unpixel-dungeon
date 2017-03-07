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

package com.felayga.unpixeldungeon.actors.mobs.rat;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.rat.GiantRatSprite;

/**
 * Created by HELLO on 6/3/2016.
 */
public class GiantRat extends Mob {
    public GiantRat()
    {
        super(1);

        name = "giant rat";
        spriteClass = GiantRatSprite.class;

        movementSpeed(GameTime.TICK * 12 / 10);
        attackSpeed(GameTime.TICK);
        defenseMundane = 13;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 30;
        nutrition = 30;
        resistanceMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.None.value;
        characteristics = Characteristic.value(Characteristic.Animal, Characteristic.Carnivore, Characteristic.CannotUseItems, Characteristic.WarmBlooded);

        belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 1, 3));
    }

    @Override
    public String description() {
        return
                "Marsupial rats are aggressive but rather weak denizens " +
                        "of the sewers. They have a nasty bite, but are only life threatening in large numbers.";
    }
}
