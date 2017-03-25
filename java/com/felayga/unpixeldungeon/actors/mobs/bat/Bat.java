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

package com.felayga.unpixeldungeon.actors.mobs.bat;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.bat.BatSprite;

/**
 * Created by HELLO on 6/3/2016.
 */
public class Bat extends Mob {
    public Bat() {
        super(0, BatSprite.class);

        movementSpeed(GameTime.TICK * 6 / 11);
        attackSpeed(GameTime.TICK);
        flying(true);
        defenseMundane = 12;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 20;
        nutrition = 20;
        resistanceMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.Stunning.value;
        characteristics = Characteristic.value(Characteristic.Animal, Characteristic.CannotUseItems, Characteristic.Carnivore);

        belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 1, 4));
    }

    @Override
    public String defenseVerb() {
        return "evaded";
    }

    @Override
    public String description() {
        return
                "These brisk and tenacious inhabitants of cave domes may defeat much larger opponents by " +
                        "replenishing their health with each successful attack.";
    }

}
