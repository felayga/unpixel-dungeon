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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.BrownMoldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.RedMoldSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 6/9/2016.
 */
public class RedMold extends Mob {
    public RedMold() {
        super(1);

        name = "red mold";
        spriteClass = RedMoldSprite.class;

        experience = 9;
        movementSpeed = 0;
        attackSpeed = GameTime.TICK;
        defenseMundane = 11;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 50;
        nutrition = 30;
        immunityMagical = MagicType.Poison.value | MagicType.Fire.value;
        corpseEffects = CorpseEffect.Vegetable.value;
        corpseResistances = MagicType.Poison.value | MagicType.Fire.value;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        int retval = super.defenseProc(enemy, damage);

        if (Level.canReach(pos, enemy.pos) && Random.Int(2) == 0) {
            int dmg = 0;
            for (int n=0;n<=level;n++) {
                dmg += Random.IntRange(1, 4);
            }
            dmg = enemy.damage(dmg, MagicType.Fire, null);
        }

        return retval;
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}


