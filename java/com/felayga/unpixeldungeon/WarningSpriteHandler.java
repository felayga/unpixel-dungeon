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

package com.felayga.unpixeldungeon;

import com.felayga.unpixeldungeon.effects.EmoIcon;
import com.felayga.unpixeldungeon.ui.Icons;
import com.watabou.noosa.Group;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by HELLO on 11/11/2016.
 */
public class WarningSpriteHandler extends Group {

    private HashMap<Integer, WarningIcon> positions;
    private List<WarningIcon> unused;

    public WarningSpriteHandler() {
        super(-1);

        positions = new HashMap<>();
        unused = new ArrayList<>();
    }

    private WarningHandler lastUsedHandler;

    public void synchronize(WarningHandler handler) {
        if (lastUsedHandler == handler) {
            for (Integer pos : handler.pendingAddition) {
                addWarning(pos);
            }
            handler.pendingAddition.clear();

            for (Integer pos : handler.pendingRemoval) {
                removeWarning(pos);
            }
            handler.pendingRemoval.clear();
        }
        else {
            lastUsedHandler = handler;

            handler.pendingAddition.clear();
            handler.pendingRemoval.clear();

            clearWarnings();

            Iterator<Map.Entry<Integer,WarningHandler.WarningInfo>> iterator = handler.warnings.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer,WarningHandler.WarningInfo> pair = iterator.next();

                addWarning(pair.getKey());
            }
        }
    }

    private boolean addWarning(int pos) {
        if (positions.containsKey(pos)) {
            return false;
        }

        WarningIcon warning;

        if (unused.size() > 0) {
            warning = unused.remove(unused.size() - 1);
            warning.setPos(pos);
        } else {
            warning = new WarningIcon(pos);
        }

        add(warning);

        positions.put(pos, warning);

        return true;
    }

    private boolean removeWarning(int pos) {
        WarningIcon warning = positions.remove(pos);

        if (warning != null) {
            remove(warning);
            unused.add(warning);

            return true;
        }

        return false;
    }

    private void clearWarnings() {
        Iterator<Map.Entry<Integer,WarningIcon>> iterator = positions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer,WarningIcon> pair = iterator.next();

            unused.add(pair.getValue());
        }

        positions.clear();
    }


    public static class WarningIcon extends EmoIcon {

        public WarningIcon(int p) {
            super();

            copy(Icons.UNKNOWN_OBJECT.get());

            setPos(p);

            maxSize = 1.3f;
            timeScale = 2;

            origin.set(2.5f, height - 2.5f);
            scale.set(Random.Float(1, maxSize));
        }

        public void setPos(int p) {
            this.pos = p;

            PointF pos = DungeonTilemap.tileCenterToWorld(p);
            pos.offset(-width() / 2.0f, -height() / 2.0f);
            point(pos);
        }
    }
}
