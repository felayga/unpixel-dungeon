package com.felayga.unpixeldungeon.mechanics;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;

import java.util.Iterator;

/**
 * Created by hello on 3/19/16.
 */
public class GameTime {
    public static final long TICK = 32;

    public static void fix()
    {
        long timefix = Actor.fixTime_new();

        if (Dungeon.level != null) {
            Dungeon.level.decay(timefix, false, true);
        }
    }
}
