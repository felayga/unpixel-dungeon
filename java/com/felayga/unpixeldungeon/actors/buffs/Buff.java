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
package com.felayga.unpixeldungeon.actors.buffs;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.text.DecimalFormat;

public class Buff extends Actor {
    protected static final String TIMELEFT = "timeLeft";

    public Char target;

    private int ownerRegistryIndex;

    public int ownerRegistryIndex() {
        return ownerRegistryIndex;
    }

    {
        actPriority = 3; //low priority, at the end of a turn
    }

    private static final String OWNERREGISTRYINDEX = "ownerRegistryIndex";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(OWNERREGISTRYINDEX, ownerRegistryIndex);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        ownerRegistryIndex = bundle.getInt(OWNERREGISTRYINDEX);
    }

    //determines how the buff is announced when it is shown.
    //buffs that work behind the scenes, or have other visual indicators can usually be silent.
    public enum buffType {
        POSITIVE, NEGATIVE, NEUTRAL, SILENT
    }

    public buffType type = buffType.SILENT;

    /*
	public HashSet<Class<?>> resistances = new HashSet<>();

	public HashSet<Class<?>> immunities = new HashSet<>();
	*/

    public String attachedMessage(boolean isHero) {
        return null;
    }

    public boolean attachTo(Char target, Char source) {
        if (source != null) {
            this.ownerRegistryIndex = source.charRegistryIndex();
        } else {
            this.ownerRegistryIndex = -1;
        }

        return restore(target);
    }

    public boolean restore(Char target) {
        //todo: Buff attachment immunity/resistance
		/*
		if (target.immunities().contains( getClass() )) {
			return false;
		}
		*/

        this.target = target;
        target.add(this);

        if (target.buffs().contains(this)) {
            if (target.sprite != null) fx(true);
            return true;
        } else {
            return false;
        }
    }

    public String detachedMessage(boolean isHero) {
        return null;
    }

    public void detach() {
        fx(false);
        target.remove(this);
    }

    @Override
    public boolean act() {
        deactivate();
        return true;
    }

    public int icon() {
        return BuffIndicator.NONE;
    }

    public void fx(boolean on) {
        //do nothing by default
    }

    public String desc() {
        return "";
    }

    //to handle the common case of showing how many turns are remaining in a buff description.
    protected String dispTurns(long input) {
        float rounds = (float) input / GameTime.TICK;
        return rounds == 1 ? "1 more turn" : new DecimalFormat("#.##").format(rounds) + " more turns";
    }

    //creates a fresh instance of the buff and attaches that, this allows duplication.
    public static <T extends Buff> T append(Char target, Char source, Class<T> buffClass) {
        try {
            T buff = buffClass.newInstance();

            Buff b = buff;
            if (source != null) {
                b.ownerRegistryIndex = source.charRegistryIndex();
            } else {
                b.ownerRegistryIndex = -1;
            }

            buff.attachTo(target, source);
            return buff;
        } catch (Exception e) {
            GLog.d("buff failure class=" + buffClass.toString());
            GLog.d(e);
            return null;
        }
    }

    public static <T extends FlavourBuff> T append(Char target, Char source, Class<T> buffClass, long duration) {
        T buff = append(target, source, buffClass);
        buff.spend_new(duration, false);
        return buff;
    }

    //same as append, but prevents duplication.
    public static <T extends Buff> T affect(Char target, Char source, Class<T> buffClass) {
        T buff = target.buff(buffClass);
        if (buff != null) {
            Buff b = buff;

            if (source != null) {
                b.ownerRegistryIndex = source.charRegistryIndex();
            } else {
                b.ownerRegistryIndex = -1;
            }

            return buff;
        } else {
            return append(target, source, buffClass);
        }
    }

    public static <T extends FlavourBuff> T affect(Char target, Char source, Class<T> buffClass, long duration) {
        T buff = affect(target, source, buffClass);
        buff.spend_new(duration, false);
        return buff;
    }

    //postpones an already active buff, or creates & attaches a new buff and delays that.
    public static <T extends FlavourBuff> T prolong(Char target, Char source, Class<T> buffClass, long duration) {
        T buff = affect(target, source, buffClass);
        buff.postpone(duration);
        return buff;
    }

    public static boolean detach(Buff buff) {
        if (buff != null) {
            buff.detach();
            return true;
        }
        return false;
    }

    public static boolean detach(Char target, Class<? extends Buff> cl) {
        return detach(target.buff(cl));
    }
}
