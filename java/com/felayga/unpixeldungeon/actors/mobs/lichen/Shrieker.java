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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRage;
import com.felayga.unpixeldungeon.items.weapon.IWeapon;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.ShriekerSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 6/9/2016.
 */
public class Shrieker extends Mob {
    public Shrieker() {
        super(3);

        name = "shrieker";
        spriteClass = ShriekerSprite.class;

        movementSpeed(GameTime.TICK * 12);
        attackSpeed(GameTime.TICK);
        defenseMundane = 13;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 100;
        nutrition = 100;
        resistanceMagical = MagicType.Poison.value;
        corpseEffects = CorpseEffect.Vegetable.value;
        corpseResistances = MagicType.Poison.value;
        viewDistance = 0;
        characteristics = Characteristic.value(Characteristic.NonBreather, Characteristic.CannotUseItems, Characteristic.Brainless);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return true;
    }

    @Override
    public boolean attack(IWeapon weapon, boolean ranged, Char enemy) {
        shriek();
        return false;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        shriek();
        return super.defenseProc(enemy, damage);
    }

    protected void shriek() {
        int distance = Math.max(Dungeon.level.WIDTH, Dungeon.level.HEIGHT) / 8;
        int enrageDistance = 0;

        for (int n = 0; n < 8; n++) {
            enrageDistance += Random.IntRange(0, distance);
        }


        ScrollOfRage.enrage(this, pos(), enrageDistance);

        if (Random.Int(10) == 0) {
            GLog.n("The shrieker shrieks!");

            Dungeon.level.spawnMob();
        } else {
            GLog.w("The shrieker shrieks!");
        }
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
