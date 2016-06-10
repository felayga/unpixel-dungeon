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
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.GreenMoldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.ShriekerSprite;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 6/9/2016.
 */
public class Shrieker extends Mob {
    public Shrieker() {
        super(3);

        name = "shrieker";
        spriteClass = ShriekerSprite.class;

        experience = 28;
        movementSpeed = 1;
        attackSpeed = GameTime.TICK;
        defenseMundane = 13;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 100;
        nutrition = 100;
        immunityMagical = MagicType.Poison.value;
        corpseEffects = CorpseEffect.Vegetable.value;
        corpseResistances = MagicType.Poison.value;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        int retval = super.defenseProc(enemy, damage);

        if (Level.canReach(pos, enemy.pos) && Random.Int(2) == 0) {
            int dmg = 0;
            for (int n=0;n<=level;n++) {
                dmg += Random.IntRange(1, 4);
            }
            dmg = enemy.damage(dmg, MagicType.Acid, null);
        }

        return retval;
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
