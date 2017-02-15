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
package com.felayga.unpixeldungeon.actors.buffs.negative;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.PoisonParticle;
import com.felayga.unpixeldungeon.items.rings.RingOfElements.Resistance;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Poison extends Buff implements Hero.Doom {
    protected int damageLeft;

    private static final String DAMAGELEFT = "damageLeft";

    public Poison(int damage) {
        damageLeft = damage;
        type = buffType.NEGATIVE;
    }

    //creates a fresh instance of the buff and attaches that, this allows duplication.
    public static Poison append(Char target, Char source, int damage) {
        Poison buff = new Poison(damage);
        buff.attachTo(target, source);
        return buff;
    }

    //same as append, but prevents duplication.
    public static Poison affect(Char target, Char source, int damage) {
        Poison buff = target.buff(Poison.class);
        if (buff != null) {
            buff.damageLeft += damage;
            return buff;
        } else {
            return append(target, source, damage);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DAMAGELEFT, damageLeft);

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        damageLeft = bundle.getInt(DAMAGELEFT);
    }

    @Override
    public int icon() {
        return BuffIndicator.POISON;
    }

    @Override
    public String toString() {
        return "Poisoned";
    }

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You are poisoned!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
    public String desc() {
        return "Poison works its way through the body, slowly impairing its internal functioning.\n" +
                "\n" +
                "Poison deals damage each turn proportional to how long until it expires."/* +
				"\n\n" +
				"This poison will last for " + dispTurns(left)  + "."*/;
    }

    @Override
    public boolean attachTo(Char target, Char source) {
        if (super.attachTo(target, source) && target.sprite != null) {
            CellEmitter.center(target.pos()).burst(PoisonParticle.SPLASH, 5);
            return true;
        } else
            return false;
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            int curDamage = Random.IntRange(1, damageLeft / 2);
            if (curDamage < 1) {
                curDamage = 1;
            }

            target.damage(curDamage, MagicType.Poison, null);
            spend_new(GameTime.TICK, false);

            damageLeft -= curDamage;

            if (damageLeft <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    public static float durationFactor(Char ch) {
        Resistance r = ch.buff(Resistance.class);
        return r != null ? r.durationFactor() : 1;
    }

    @Override
    public void onDeath() {
        Badges.validateDeathFromPoison();

        Dungeon.fail(ResultDescriptions.POISON);
        GLog.n("You died from poison...");
    }
}
