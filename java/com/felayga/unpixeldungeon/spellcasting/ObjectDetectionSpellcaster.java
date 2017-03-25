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

package com.felayga.unpixeldungeon.spellcasting;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/20/2017.
 */

public class ObjectDetectionSpellcaster extends Spellcaster {
    public ObjectDetectionSpellcaster() {
        super("Object Detection", 5);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        SpellSprite.show(source, SpellSprite.ITEMS);

        if (callback != null) {
            callback.call();
        }
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        Level level = Dungeon.level;

        for (Heap heap : level.heaps.values()) {
            heap.seen = true;

            int p = heap.pos();

            int x = p % Level.WIDTH;
            int y = p / Level.WIDTH;

            level.mapped[p] = true;

            if (x > 1) {
                level.mapped[p - 1] = true;
                if (y > 1) {
                    level.mapped[p - Level.WIDTH - 1] = true;
                }
                if (y < Level.HEIGHT - 1) {
                    level.mapped[p + Level.WIDTH - 1] = true;
                }
            }
            if (x < Level.WIDTH - 1) {
                level.mapped[p + 1] = true;
                if (y > 1) {
                    level.mapped[p - Level.WIDTH + 1] = true;
                }
                if (y < Level.HEIGHT - 1) {
                    level.mapped[p + Level.WIDTH + 1] = true;
                }
            }

            if (y > 1) {
                level.mapped[p - Level.WIDTH] = true;
            }
            if (y < Level.HEIGHT - 1) {
                level.mapped[p + Level.WIDTH] = true;
            }
        }

        Dungeon.observe();
    }

}
