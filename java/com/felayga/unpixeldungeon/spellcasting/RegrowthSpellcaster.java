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
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Regrowth;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.plants.Seedpod;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 3/10/2017.
 */

public class RegrowthSpellcaster extends Spellcaster {
    public RegrowthSpellcaster() {
        super("Regrowth", 3);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.SplasherBolt, Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        MagicMissile.foliage(source.sprite.parent, sourcePos, targetPos, callback);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        onZap(source, targetPos, Random.IntRange(3, 12));
    }

    public void onZap(Char source, int targetPos, int regrowthCount) {
        ArrayList<Integer> spots = Dungeon.level.randomPositionsNear(targetPos, regrowthCount, onZapSpotValidator);

        if (spots != null) {
            for (Integer subPos : spots) {
                int terrain;
                switch (Random.Int(3)) {
                    case 0:
                        terrain = Terrain.HIGH_GRASS;
                        break;
                    default:
                        terrain = Terrain.GRASS;
                        break;
                }
                Dungeon.level.set(subPos, terrain, true);
                GameScene.add(Blob.seed(source, subPos, 10, Regrowth.class));

                if (Random.Int(4) == 0) {
                    Plant.Seed seed;
                    if (Random.Int(16) == 0) {
                        seed = new Seedpod.Seed();
                    } else {
                        seed = (Plant.Seed) Generator.random(Generator.Category.SEED);
                    }

                    Dungeon.level.plant(source, seed, subPos);
                }
            }

            for (Integer subPos : spots) {
                if (Dungeon.visible[subPos]) {
                    MagicMissile.foliage(source.sprite.parent, targetPos, subPos, null);
                }
            }
        }
    }

    private static Level.RandomPositionValidator onZapSpotValidator = new Level.RandomPositionValidator() {
        @Override
        public boolean isValidPosition(int pos) {
            int c = Dungeon.level.map(pos);
            return c == Terrain.EMPTY ||
                    c == Terrain.CHARCOAL ||
                    c == Terrain.EMPTY_DECO ||
                    c == Terrain.GRASS ||
                    c == Terrain.HIGH_GRASS;
            //todo: dirtgrass regrowth?
        }
    };

}
