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

package com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.watabou.utils.Random;

//import com.felayga.unpixeldungeon.actors.mobs.Spinner;
//import com.felayga.unpixeldungeon.actors.mobs.Thief;
//import com.felayga.unpixeldungeon.items.Honeypot;

/**
 * Created by HELLO on 3/9/2016.
 */
public class PoisonChance extends MeleeMobAttack {
    private int poisonMin;
    private int poisonMax;

    public PoisonChance(long delay, int damageMin, int damageMax, int poisonMin, int poisonMax) {
        super(delay, damageMin, damageMax);

        this.poisonMin = poisonMin;
        this.poisonMax = poisonMax;
    }

    @Override
    public int proc(Char attacker, boolean ranged, Char target, int damage) {
        damage = super.proc(attacker, ranged, target, damage);

        if (Random.Int(8) == 0) {
            Poison.affect(target, attacker, Random.IntRange(poisonMin, poisonMax));

            if ((target.resistanceMagical & MagicType.Poison.value) == 0) {
                if (Random.Int(8) == 0) {
                    if (target == Dungeon.hero) {
                        Hero hero = (Hero) target;

                        if (Random.Int(3) == 0) {
                            hero.useAttribute(AttributeType.DEXCHA, -0.5f);
                        } else {
                            hero.useAttribute(AttributeType.STRCON, -0.5f);
                        }
                    } else {
                        if (Random.Int(3) == 0) {
                            target.damageAttribute(AttributeType.DEXCHA, 1);
                        } else {
                            target.damageAttribute(AttributeType.STRCON, 1);
                        }
                    }

                    if (Random.Int(4) == 0) {
                        Poison.affect(target, attacker, target.HT);
                    }
                }
            }
        }

        return damage;
    }

}
