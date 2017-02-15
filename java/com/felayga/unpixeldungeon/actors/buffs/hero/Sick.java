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

package com.felayga.unpixeldungeon.actors.buffs.hero;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

/**
 * Created by HELLO on 6/15/2016.
 */
public class Sick extends FlavourBuff {
    private static final long DURATIONMODIFIER = 2;
    private static final long DURATION = 14;

    private long left;
    private static final String LEFT = "left";

    {
        type = buffType.NEGATIVE;
        left = DURATION * DURATIONMODIFIER;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getLong(LEFT);
    }

    @Override
    public boolean act() {
        left--;

        if (target.isAlive()) {
            Char source = Char.Registry.get(ownerRegistryIndex());

            if (left >= 4 * DURATIONMODIFIER && left <= 9 * DURATIONMODIFIER) {
                Buff.prolong(target, source, Vertigo.class, 2 * DURATIONMODIFIER * GameTime.TICK);
            }
            if (left >= 4 * DURATIONMODIFIER && left <= 6 * DURATIONMODIFIER) {
                Buff.prolong(target, source, Paralysis.class, 2 * DURATIONMODIFIER * GameTime.TICK);
            }
            if (left == 3 * DURATIONMODIFIER) {
                GLog.n("You feel incredibly sick.");
            }
            if (left <= 0) {
                vomit(target, source);
            }
        } else {
            detach();
        }

        spend_new(GameTime.TICK, false);

        return true;
    }

    public static void vomit(Char target, Char source) {
        Buff.prolong(target, source, Paralysis.class, 2 * GameTime.TICK);

        if (target instanceof Hero) {
            GLog.w("You suddenly vomit!");
        } else {
            GLog.w("The " + target.name + " suddenly vomits!");
        }

        Buff.detach(target, DeathlySick.class);
        Buff.detach(target, Sick.class);

        Hunger hunger = target.buff(Hunger.class);
        if (hunger != null) {
            hunger.satisfy_new(-Food.AMOUNT_EATEN_PER_ROUND / 3);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.SICK;
    }

    @Override
    public String toString() {
        return "Sick";
    }

    public static float duration(Char ch) {
        return DURATION;
    }

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You are feeling mildly nauseated.";
        }

        return super.attachedMessage(isHero);
    }

    @Override
    public String detachedMessage(boolean isHero) {
        if (isHero) {
            return "You feel a lot better.";
        }

        return super.detachedMessage(isHero);
    }

    @Override
    public String desc() {
        return "Seems that you've eaten something which didn't agree with you or vice versa.";
    }

}
