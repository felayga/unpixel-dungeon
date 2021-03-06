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

package com.felayga.unpixeldungeon.actors.mobs.goo;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.goo.AcidBlobSprite;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 6/8/2016.
 */
public class AcidBlob extends Mob {
    public AcidBlob() {
        super(1, AcidBlobSprite.class);

        movementSpeed(GameTime.TICK * 4);
        attackSpeed(GameTime.TICK);
        defenseMundane = 12;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 30;
        nutrition = 10;
        resistanceMagical = MagicType.value(MagicType.Sleep, MagicType.Poison, MagicType.Acid, MagicType.Stoning);
        corpseEffects = CorpseEffect.value(CorpseEffect.Unstoning, CorpseEffect.Acidic, CorpseEffect.Vegetable, CorpseEffect.Unrottable);
        corpseResistances = MagicType.None.value;
        viewDistance = 0;
        characteristics = Characteristic.value(Characteristic.CannotUseItems, Characteristic.NonBreather, Characteristic.Brainless, Characteristic.Amorphous);

        belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 0, 0));
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        int retval = super.defenseProc(enemy, damage);

        if (Level.canReach(pos(), enemy.pos()) && Random.Int(4) == 0) {
            int dmg = Random.IntRange(1, 8);
            //todo: erosion attacks (corrosion)
            enemy.damage(dmg, MagicType.Acid, this, null);
        }

        return retval;
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
