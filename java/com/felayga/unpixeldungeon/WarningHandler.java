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

package com.felayga.unpixeldungeon;

import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.positive.SeeInvisible;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by HELLO on 11/12/2016.
 */
public class WarningHandler implements IDecayable, Bundlable {
    @Override
    public long decay() {
        return 0;
    }

    private enum Flags {
        None                (0x0000),
        FirstRun            (0x0001),
        VisionClears        (0x0002),
        SeeInvisClears      (0x0004);

        public final int value;

        Flags(int value) {
            this.value = value;
        }
    }

    private long decayTime;

    @Override
    public boolean decay(long currentTime, boolean updateTime, boolean fixTime) {
        if (fixTime || updateTime) {
            long newAmount = currentTime - decayTime;
            if (fixTime) {
                decayTime = 0;
            } else {
                decayTime = currentTime;
            }
            currentTime = newAmount;
        } else {
            decayTime = currentTime;
            currentTime = 0;
        }

        boolean blind = true;
        boolean seeinvis = true;

        if (Dungeon.hero != null) {
            blind = Dungeon.hero.buff(Blindness.class) != null;
            seeinvis = Dungeon.hero.buff(SeeInvisible.class) != null;
        }

        List<Integer> pendingremoval = new ArrayList<>();

        for (int n=0;n<warnings.size();n++) {
            Integer key = warnings.keyAt(n);
            WarningInfo value = warnings.valueAt(n);

            if ((value.flags & Flags.FirstRun.value) != 0) {
                value.flags ^= Flags.FirstRun.value;
            } else {
                value.timeToLive -= currentTime;
            }

            boolean clear = value.timeToLive <= 0;

            if (!clear && !blind) {
                if (Dungeon.visible[key]) {
                    if ((value.flags & Flags.VisionClears.value) != 0) {
                        clear = true;
                    } else if (seeinvis && (value.flags & Flags.SeeInvisClears.value) != 0) {
                        clear = true;
                    }
                }
            }

            if (clear) {
                pendingremoval.add(key);
            }
        }

        for (Integer remove : pendingremoval) {
            remove(remove);
        }

        return pendingremoval.size() > 0;
    }



    private static final String KEY_POSITION = "warningSpritePosition";
    private static final String KEY_TIMETOLIVE = "warningSpriteTimeToLive";
    private static final String KEY_FLAGS = "warningSpriteFlags";

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        int[] pos = bundle.getIntArray(KEY_POSITION);
        long[] ttl = bundle.getLongArray(KEY_TIMETOLIVE);
        int[] flags = bundle.getIntArray(KEY_FLAGS);

        for (int n = 0; n < pos.length; n++) {
            add(pos[n], ttl[n], flags[n]);
        }
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        int size = warnings.size();

        int[] pos = new int[size];
        long[] ttl = new long[size];
        int[] flags = new int[size];

        for (int n=0;n<warnings.size();n++) {
            int key = warnings.keyAt(n);
            WarningInfo value = warnings.valueAt(n);

            pos[n] = key;
            ttl[n] = value.timeToLive;
            flags[n] = value.flags;
        }

        bundle.put(KEY_POSITION, pos);
        bundle.put(KEY_TIMETOLIVE, ttl);
        bundle.put(KEY_FLAGS, flags);
    }

    public void add(int pos) {
        add(pos, Long.MAX_VALUE);
    }

    public void add(int pos, long ttl) {
        add(pos, ttl, true);
    }

    public void add(int pos, boolean visible) {
        add(pos, Long.MAX_VALUE, visible);
    }

    public void add(int pos, long ttl, boolean visible) {
        int flags = Flags.FirstRun.value;

        if (visible) {
            flags |= Flags.VisionClears.value;
        }
        else {
            flags |= Flags.SeeInvisClears.value;
        }

        add(pos, ttl, flags);
    }

    private void add(int pos, long ttl, int flags) {
        WarningInfo info = warnings.get(pos);

        if (info != null) {
            info.timeToLive = ttl;
            info.flags = flags;
        }
        else {
            warnings.put(pos, new WarningInfo(pos, ttl, flags));
            pendingAddition.add(pos);
            pendingRemoval.remove(pos);
        }
    }

    public void remove(int pos) {
        pendingAddition.remove(pos);
        pendingRemoval.add(pos);
        warnings.remove(pos);
    }

    public boolean findWarning(int pos) {
        return warnings.get(pos) != null;
    }

    public static class WarningInfo {
        public int pos;
        public long timeToLive;
        public int flags;

        public WarningInfo(int pos, long ttl, int flags) {
            this.pos = pos;
            this.timeToLive = ttl;
            this.flags = flags;
        }

    }


    public final SparseArray<WarningInfo> warnings;

    public final HashSet<Integer> pendingAddition;
    public final HashSet<Integer> pendingRemoval;

    public WarningHandler() {
        pendingAddition = new HashSet<>();
        pendingRemoval = new HashSet<>();

        warnings = new SparseArray<>();
    }
}
