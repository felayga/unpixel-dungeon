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
package com.felayga.unpixeldungeon.actors.buffs.positive;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;

public class MagicalSleep extends Buff {

    private static final long STEP = GameTime.TICK;
    public static final long SWS = GameTime.TICK * 3 / 2;

    public MagicalSleep() {

    }

    @Override
    public boolean attachTo(Char target, Char source) {
        //todo: MagicalSleep immunity/resistance
        if (super.attachTo(target, source)/* && !target.immunities().contains(Sleep.class)*/) {

            if (target == Dungeon.hero) {
                /*
				if (target.HP == target.HT) {
					GLog.i("You are too healthy, and resist the urge to sleep.");
					detach();
					return true;
				} else {
				*/
                GLog.i("You fall into a deep magical sleep.");
                /*
				}
				*/
            } else if (target instanceof Mob) {
                ((Mob) target).state = ((Mob) target).SLEEPING;
            }

            target.paralysed++;

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean act() {
        if (target == Dungeon.hero) {
            target.HP = Math.min(target.HP + 1, target.HT);
            ((Hero) target).resting = true;
            if (target.HP == target.HT) {
                GLog.p("You wake up feeling refreshed and healthy.");
                detach();
            }
        }
        spend_new(STEP, false);
        return true;
    }

    @Override
    public void detach() {
        if (target.paralysed > 0) {
            target.paralysed--;
        }
        if (target == Dungeon.hero) {
            ((Hero) target).resting = false;
        }
        super.detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.MAGIC_SLEEP;
    }

    @Override
    public String toString() {
        return "Magical Sleep";
    }

    @Override
    public String desc() {
        return "This character has fallen into a deep magical sleep which they will not wake from naturally.\n" +
                "\n" +
                "Magical sleep is similar to regular sleep, except that only damage will cause the target to wake up. \n" +
                "\n" +
                "For the hero, magical sleep has some restorative properties, allowing them to rapidly heal while resting.";
    }
}
