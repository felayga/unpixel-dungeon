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

package com.felayga.unpixeldungeon.actors.mobs.lichen;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.PurpleMoldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.YellowMoldSprite;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/14/2017.
 */

public class PurpleMold extends Mob {
    public PurpleMold() {
        super(1);

        name = "purple mold";
        spriteClass = PurpleMoldSprite.class;

        movementSpeed(0);
        attackSpeed(GameTime.TICK);
        defenseMundane = 11;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 50;
        nutrition = 30;
        resistanceMagical = MagicType.Poison.value;
        corpseEffects = CorpseEffect.value(CorpseEffect.Poisonous, CorpseEffect.Sickening, CorpseEffect.Vegetable);
        corpseResistances = MagicType.Poison.value;
        viewDistance = 0;
        characteristics = Characteristic.value(Characteristic.NonBreather, Characteristic.CannotUseItems, Characteristic.Brainless);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}

