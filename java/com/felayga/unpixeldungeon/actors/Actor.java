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
package com.felayga.unpixeldungeon.actors;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

import java.util.HashSet;

public abstract class Actor implements Bundlable {
    private long time;

    public long getTime() {
        return time;
    }

    private int id = 0;

    //used to determine what order actors act in.
    //hero should always act on 0, therefore negative is before hero, positive is after hero
    protected int actPriority = Integer.MAX_VALUE;

    protected abstract boolean act();

    public void spend_new(long time, boolean andnext) {
        this.time += time;
    }

    protected void postpone(long time) {
        if (this.time < now + time) {
            this.time = now + time;
        }
    }

    protected long cooldown() {
        return time - now;
    }

    protected void deactivate() {
        time = Long.MAX_VALUE;
    }

    protected void onAdd() {
    }

    protected void onRemove() {
    }

    private static final String TIME = "time";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(TIME, time);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        time = bundle.getLong(TIME);
    }

    // **********************
    // *** Static members ***

    private static HashSet<Actor> all = new HashSet<>();
    private static SparseArray<Char> chars = new SparseArray<>();
    private static Actor current;

    private static long now = 0;

    public static void clear() {
        now = 0;

        all.clear();
        chars.clear();
    }

    public static final long fixTime_new() {
        if (Dungeon.hero != null && all.contains(Dungeon.hero)) {
            Statistics.duration += now;
        }

        long min = Long.MAX_VALUE;
        for (Actor a : all) {
            if (min > a.time) {
                min = a.time;
            }
        }
        for (Actor a : all) {
            a.time -= min;
            if (a instanceof Char) {
                Char c = (Char) a;
                c.belongings.decay(min, false, true);
            }
        }
        now = 0;

        return min;
    }

    public static void init() {

        addDelayed(Dungeon.hero, -1); //originally float.minvalue

        for (Mob mob : Dungeon.level.mobs) {
            add(mob);
        }

        for (Blob blob : Dungeon.level.blobs.values()) {
            add(blob);
        }

        current = null;
    }

    /*protected*/
    public void next() {
        if (current == this) {
            current = null;
        }
    }

    public static void process() {
        if (current != null) {
            return;
        }

        boolean doNext;

        //GLog.d("process start");
        do {
            now = Long.MAX_VALUE;
            current = null;

            for (Actor actor : all) {
                //some actors will always go before others if time is equal.
                if (actor.time < now || actor.time == now && (current == null || actor.actPriority < current.actPriority)) {
                    now = actor.time;
                    current = actor;
                }
            }

            if (current != null) {
                if (current instanceof Char && ((Char) current).sprite.isMoving) {
                    // If it's character's turn to act, but its sprite
                    // is moving, wait till the movement is over
                    current = null;
                    break;
                }

                doNext = current.act();
                if (doNext && !Dungeon.hero.isAlive()) {
                    doNext = false;
                    current = null;
                }
            } else {
                doNext = false;
            }

            Dungeon.decay(now, true, false);

        } while (doNext);
        //GLog.d("process end");
    }

    public static void add(Actor actor) {
        add(actor, now);
    }

    public static void addDelayed(Actor actor, long delay) {
        add(actor, now + delay);
    }

    private static void add(Actor actor, long time) {
        if (all.contains(actor)) {
            return;
        }

        all.add(actor);
        actor.time = time;
        actor.onAdd();

        if (actor instanceof Char) {
            Char ch = (Char) actor;
            GLog.d("add char at pos=" + ch.pos() + " type=" + ch.getClass().toString());
            chars.put(ch.pos(), ch);
            Char.Registry.register(ch);
            for (Buff buff : ch.buffs()) {
                all.add(buff);
                buff.onAdd();
            }
        }
    }

    /*
    //incomplete
    public static void polymorph(Actor oldChar, Actor newChar) {
        all.add(newChar);
        newChar.time = oldChar.time;
        newChar.onAdd();

        if (newChar instanceof Char) {
            Char ch = (Char) newChar;
            GLog.d("polymorph char at pos=" + ch.pos() + " type=" + ch.getClass().toString());
            chars.put(ch.pos(), ch);
            for (Buff buff : ch.buffs()) {
                all.add(buff);
                buff.onAdd();
            }
        }

        remove(oldChar);
    }
    */

    public static void remove(Actor actor) {
        if (actor != null) {
            all.remove(actor);

            if (actor instanceof Char) {
                Char c = (Char) actor;
                chars.remove(c.pos());
            }
            actor.onRemove();
        }
    }

    public void move(int lastPos, int pos) {
        if (lastPos == pos) {
            return;
        }

        if (lastPos >= 0) {
            Char c = chars.get(lastPos);
            if (c == this) {
                chars.remove(lastPos);
                chars.put(pos, c);
            } else {
                GLog.d("UNEXPECTED WRONG CHARACTER FOUND="+(c!=null?c.name:"<null>")+" EXPECTED="+((Char)this).name);
                chars.put(pos, (Char)this);
            }
        } else {
            chars.put(pos, (Char) this);
        }
    }

    public static Char findChar(int pos) {
        return chars.get(pos);

        /*
        HashMap<Integer, Integer> test = new HashMap<>();

        Char retval = null;

		for (Char ch : chars){
			if (ch.pos == pos)
            {
                retval = ch;
            }
            if (test.containsKey(ch.pos)) {
                test.put(ch.pos, test.get(ch.pos)+1);
            } else {
                test.put(ch.pos, 1);
            }
		}

        GLog.d("findchar with " + test.size()+" entries");

        Iterator<Map.Entry<Integer, Integer>> iterator = test.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();

            if (entry.getValue() > 1) {
                GLog.d("pos="+entry.getKey()+" has "+entry.getValue()+" items?");
            }
        }

		return retval;
		*/
    }

    public static HashSet<Actor> all() {
        return all;
    }

    public static SparseArray<Char> chars() {
        return chars;
    }
}
