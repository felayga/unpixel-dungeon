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

package com.felayga.unpixeldungeon.actors.mobs.snake;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.PoisonChance;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.snake.WaterMoccasinSprite;

/**
 * Created by HELLO on 6/15/2016.
 */
public class WaterMoccasin extends Mob {
    public WaterMoccasin() {
        super(4, WaterMoccasinSprite.class);

        movementSpeed(GameTime.TICK * 4 / 5);
        attackSpeed(GameTime.TICK);
        defenseMundane = 17;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 150;
        nutrition = 80;
        resistanceMagical = MagicType.Poison.value;
        corpseEffects = CorpseEffect.Poisonous.value;
        corpseResistances = CorpseEffect.Poisonous.value;
        characteristics = Characteristic.value(Characteristic.Animal, Characteristic.Carnivore, Characteristic.CannotUseItems, Characteristic.Swimmer, Characteristic.Concealer);

        belongings.collectEquip(new PoisonChance(GameTime.TICK, 1, 6, 1, 6));
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}

