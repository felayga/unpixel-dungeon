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
package com.felayga.unpixeldungeon.actors;

import java.util.HashSet;

import android.util.SparseArray;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Actor implements Bundlable {
	private long time;
	public long getTime() { return time; }

	private int id = 0;

	//used to determine what order actors act in.
	//hero should always act on 0, therefore negative is before hero, positive is after hero
	protected int actPriority = Integer.MAX_VALUE;

	protected abstract boolean act();
	
	protected void spend( long time, boolean andnext ) {
		this.time += time;
	}
	
	protected void postpone( long time ) {
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
	
	protected void onAdd() {}
	
	protected void onRemove() {}

	private static final String TIME    = "time";
	private static final String ID      = "id";

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( TIME, time );
		bundle.put( ID, id );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		time = bundle.getLong(TIME);
		id = bundle.getInt(ID);
	}

	private static int nextID = 1;
	public int id() {
		if (id > 0) {
			return id;
		} else {
			return (id = nextID++);
		}
	}

	// **********************
	// *** Static members ***
	
	private static HashSet<Actor> all = new HashSet<>();
	private static HashSet<Char> chars = new HashSet<>();
	private static Actor current;

	private static SparseArray<Actor> ids = new SparseArray<>();

	private static long now = 0;
	
	public static void clear() {
		
		now = 0;

		all.clear();
		chars.clear();

		ids.clear();
	}
	
	public static final long fixTime_new() {
		if (Dungeon.hero != null && all.contains( Dungeon.hero )) {
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
				Char c = (Char)a;
				c.belongings.decay(min, true, false);
			}
		}
		now = 0;

		return min;
	}
	
	public static void init() {
		
		addDelayed( Dungeon.hero, -1 ); //originally float.minvalue
		
		for (Mob mob : Dungeon.level.mobs) {
			add( mob );
		}
		
		for (Blob blob : Dungeon.level.blobs.values()) {
			add( blob );
		}
		
		current = null;
	}

	private static final String NEXTID = "nextid";

	public static void storeNextID( Bundle bundle){
		bundle.put( NEXTID, nextID );
	}

	public static void restoreNextID( Bundle bundle){
		nextID = bundle.getInt( NEXTID );
	}

	public static void resetNextID(){
		nextID = 1;
	}

	/*protected*/public void next() {
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

            if (Dungeon.level != null) {
                Dungeon.level.decay(now, true, false);
            }

        } while (doNext);
        //GLog.d("process end");
    }
	
	public static void add( Actor actor ) {
		add( actor, now );
	}
	
	public static void addDelayed( Actor actor, long delay ) {
		add( actor, now + delay );
	}
	
	private static void add( Actor actor, long time ) {
		
		if (all.contains( actor )) {
			return;
		}

		ids.put( actor.id(),  actor );

		all.add( actor );
		actor.time = time;
		actor.onAdd();
		
		if (actor instanceof Char) {
			Char ch = (Char)actor;
			chars.add( ch );
			for (Buff buff : ch.buffs()) {
				all.add( buff );
				buff.onAdd();
			}
		}
	}
	
	public static void remove( Actor actor ) {
		
		if (actor != null) {
			all.remove( actor );
			chars.remove( actor );
			actor.onRemove();

			if (actor.id > 0) {
				ids.remove( actor.id );
			}
		}
	}
	
	public static Char findChar( int pos ) {
		for (Char ch : chars){
			if (ch.pos == pos)
				return ch;
		}
		return null;
	}

	public static Actor findById( int id ) {
		return ids.get( id );
	}

	public static HashSet<Actor> all() {
		return all;
	}

	public static HashSet<Char> chars() { return chars; }
}
