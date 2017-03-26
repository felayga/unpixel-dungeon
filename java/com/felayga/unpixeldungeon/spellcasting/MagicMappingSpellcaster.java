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
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Halo;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/11/2017.
 */

public class MagicMappingSpellcaster extends Spellcaster {
    public MagicMappingSpellcaster() {
        super("Magic Mapping", 5);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        SpellSprite.show(source, SpellSprite.MAP);

        if (callback != null) {
            callback.call();
        }
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        int length = Level.LENGTH;

        boolean noticed = false;

        if (levelBoost < 0) {
            for (int i = 0; i < length / 14; i++) {
                int index = Random.Int(Level.WIDTH - 1) + Random.Int(Level.HEIGHT - 1) * Level.WIDTH;
                noticed |= discoverCell(index, false, false);
                noticed |= discoverCell(index + 1, false, false);
                noticed |= discoverCell(index + Level.WIDTH, false, false);
                noticed |= discoverCell(index + Level.WIDTH + 1, false, false);
            }
        } else if (levelBoost > 0) {
            for (int i = 0; i < length; i++) {
                noticed |= discoverCell(i, true, false);
            }
        } else {
            for (int i = 0; i < length; i++) {
                noticed |= discoverCell(i, false, false);
            }
        }

        Dungeon.observe();

        if (noticed) {
            Sample.INSTANCE.play(Assets.SND_SECRET);
        }
    }

    public static boolean discoverCell(int index, boolean findTraps, boolean forced) {
        int terr = Dungeon.level.map(index);

        if (Level.discoverable[index] || forced) {
            Dungeon.level.mapped[index] = true;

            if (findTraps && (Terrain.flags[terr] & Terrain.FLAG_SECRET) != 0) {
                Dungeon.level.discover(index, true, findTraps);

                if (Dungeon.visible[index]) {
                    GameScene.discoverTile(index, terr);
                    CellEmitter.get(index).start(Speck.factory(Speck.DISCOVER), 0.1f, 4);

                    return true;
                }
            }
        }

        return false;
    }
}
