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

package com.felayga.unpixeldungeon.actors.buffs.hero;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Chill;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.rings.RingOfElements;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 6/15/2016.
 */
public class Sick extends FlavourBuff {
    private static final long DURATION = 14;

    private long left;
    private static final String LEFT = "left";

    {
        type = buffType.NEGATIVE;
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
            if (left >= 6 && left <= 11) {
                Buff.prolong(target, Vertigo.class, Random.IntRange(2, 8));
            }
            if (left >= 6 && left <= 8) {
                Buff.prolong(target, Paralysis.class, Random.IntRange(2, 8));
            }
            if (left == 5) {
                GLog.n("You feel incredibly sick.");
            }
            if (left==2) {
                GLog.n("You suddenly vomit!");
                Buff.prolong(target, Paralysis.class, 2);
                Buff.detach(target, DeathlySick.class);

                Hunger hunger = target.buff(Hunger.class);
                if (hunger != null) {
                    hunger.satisfy_new(-Food.AMOUNT_EATEN_PER_ROUND / 3);
                }
            }
        } else {
            detach();
        }

        spend_new(GameTime.TICK, false);

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.FIRE;
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
    public String desc() {
        return "Seems that you've eaten something which didn't agree with you or vice versa.";
    }

}
