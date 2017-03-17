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
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.Beam;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.PurpleParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 3/10/2017.
 */

public class DisintegrationSpellcaster extends Spellcaster {
    public DisintegrationSpellcaster() {
        super("Disintegration", 6);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.NoCollision);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        source.sprite.parent.add(new Beam.DeathRay(DungeonTilemap.tileCenterToWorld(sourcePos), DungeonTilemap.tileCenterToWorld(targetPos)));
        callback.call();
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        boolean terrainAffected = false;

        ArrayList<Char> chars = new ArrayList<>();

        if (path != null) {
            int maxDistance = Math.min(6, path.dist);

            for (int c : path.subPath(1, maxDistance)) {
                Char ch;
                if ((ch = Actor.findChar(c)) != null) {
                    chars.add(ch);
                }

                if (Level.burnable[c]) {
                    Dungeon.level.set(c, Terrain.CHARCOAL, true);
                    GameScene.updateMap(c);
                    terrainAffected = true;
                }

                CellEmitter.center(c).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            }
        } else {
            chars.add(source);
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        int dmgMin = 8;
        int dmgMax = 20;

        for (Char ch : chars) {
            ch.damage(damage(Random.NormalIntRange(dmgMin, dmgMax)), MagicType.Magic, source, null);
            ch.sprite.centerEmitter(-1).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            ch.sprite.flash();

            dmgMin = (dmgMin * 4) / 5;
            dmgMax = (dmgMax * 3) / 5;
        }
    }
}
