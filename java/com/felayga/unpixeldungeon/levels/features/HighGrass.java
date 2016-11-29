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
package com.felayga.unpixeldungeon.levels.features;

import com.felayga.unpixeldungeon.Challenges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.LeafParticle;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class HighGrass {

    public static void trample(Level level, int pos, Char ch) {
        Dungeon.level.set(pos, Terrain.GRASS, true);
        GameScene.updateMap(pos);

        if (!Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
            int luck = 0;

            if (ch instanceof Hero) {
                Hero hero = (Hero) ch;

                luck = hero.luck();
            }

            if (Random.PassFail((int)Math.pow(luck + 10.0, 1.75))) {
                Item seed = Generator.random(Generator.Category.SEED);
                level.drop(seed, pos).sprite.drop();
            }


            /*
            int naturalismLevel = 0;

            if (ch != null) {
                SandalsOfNature.Naturalism naturalism = ch.buff(SandalsOfNature.Naturalism.class);
                if (naturalism != null) {
                    if (!naturalism.isCursed()) {
                        naturalismLevel = naturalism.level() + 1;
                        naturalism.charge();
                    } else {
                        naturalismLevel = -1;
                    }
                }
            }

            if (naturalismLevel >= 0) {
                // Seed, scales from 1/16 to 1/4
                if (Random.Int(16 - ((int) (naturalismLevel * 3))) == 0) {
                    Item seed = Generator.random(Generator.Category.SEED);

                    level.drop(seed, pos).sprite.drop();
                }

                // Dew, scales from 1/6 to 1/3
                if (Random.Int(24 - naturalismLevel * 3) <= 3) {
                    level.drop(new Dewdrop(), pos).sprite.drop();
                }
            }
            */
        }

        int leaves = 4;

        /*
        // Barkskin
        if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
            Buff.affect(ch, Barkskin.class).level(ch.HT / 3);
            leaves = 8;
        }
        */

        CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, leaves);
        Dungeon.observe();
    }
}
