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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Chill;
import com.felayga.unpixeldungeon.actors.buffs.negative.Frost;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/10/2017.
 */

public class FrostSpellcaster extends Spellcaster {
    public FrostSpellcaster() {
        super("Frost", 4);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicBolt);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        MagicMissile.blueLight(source.sprite.parent, sourcePos, targetPos, callback);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        Heap heap = Dungeon.level.heaps.get(targetPos);
        if (heap != null) {
            heap.freeze(source);
        }

        Char ch = Actor.findChar(targetPos);
        if (ch != null) {

            int damage = damage(Random.NormalIntRange(6, 36));

            /*
            if (ch.buff(Chill.class) != null || ch.buff(Frost.class) != null) {
                damage = (int) (damage * ch.buff(Chill.class).movementModifier() / GameTime.TICK);
            } else {
            }
            */
            ch.sprite.burst(0xFF99CCFF, 3);

            ch.damage(damage, MagicType.Cold, source, null);

            if (ch.isAlive()) {
                if (Level.puddle[ch.pos()]) {
                    if (Random.Int(4) == 0) {
                        Buff.affect(ch, source, Frost.class, Frost.duration(ch) * damage(Random.IntRange(2, 4)) * GameTime.TICK / GameTime.TICK);
                    } else {
                        Buff.prolong(ch, source, Chill.class, 12);
                    }
                } else {
                    Buff.prolong(ch, source, Chill.class, 8);
                }
            }
        }
    }
}

