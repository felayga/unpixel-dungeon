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
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.items.scrolls.positionscroll.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by HELLO on 3/10/2017.
 */

public class CreateMonsterSpellcaster extends Spellcaster {
    public CreateMonsterSpellcaster() {
        super("Create Monster", 2);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.Projectile);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        MagicMissile.whiteLight(source.sprite.parent, sourcePos, targetPos, callback);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        spawnMobs(targetPos, 1);
    }

    private static final long READINESS_DELAY = GameTime.TICK * 2;

    public static void spawnMobs(int pos, int count) {
        //GLog.d("spawnMobs x="+(pos%Level.WIDTH)+" y="+(pos/Level.WIDTH)+" count="+count);

        HashSet<Integer> tested = new HashSet<>();

        HashSet<Integer> candidates = new HashSet<>();

        int originGood = 0;

        if (Actor.findChar(pos) == null && Level.passable[pos]) {
            originGood = 1;
        }

        for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
            int p = pos + Level.NEIGHBOURS8[i];
            Char test = Actor.findChar(p);
            if (test == null && Level.passable[p] && !Level.avoid[p]) {
                candidates.add(p);
            } else {
                tested.add(p);
            }
        }

        ArrayList<Integer> spawnPoints = new ArrayList<>();

        for(Integer candidate : candidates) {
            spawnPoints.add(candidate);
            tested.add(candidate);

            count--;
            if (count <= originGood) {
                break;
            }
        }

        Collections.shuffle(spawnPoints);

        if (count > originGood) {
            //not enough spawn points, check surrounding tiles of valid points

            candidates.clear();

            for (Integer spawnPoint : spawnPoints) {
                for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
                    int p = spawnPoint + Level.NEIGHBOURS8[i];

                    if (tested.contains(p)) {
                        continue;
                    }

                    Char test = Actor.findChar(p);
                    if (test == null && Level.passable[p] && !Level.avoid[p]) {
                        candidates.add(p);
                    }
                }
            }

            for (Integer candidate : candidates) {
                spawnPoints.add(candidate);
                count--;
                if (count <= originGood) {
                    break;
                }
            }
        }

        if (originGood > 0) {
            spawnPoints.add(0, pos);
        }

        final ArrayList<Mob> mobs = new ArrayList<>();

        for (final Integer spawnPoint : spawnPoints) {
            Bestiary.spawn(Dungeon.depthAdjusted, Dungeon.hero.level, true, new Bestiary.SpawnParams() {
                @Override
                public Level level() {
                    return Dungeon.level;
                }

                @Override
                public int position() {
                    return spawnPoint;
                }

                @Override
                public Class<?> type(Class<?> type) {
                    return type;
                }

                @Override
                public int quantity(int quantity) {
                    return quantity;
                }

                @Override
                public void initialize(Mob mob) {
                    mob.state = mob.WANDERING;
                    GameScene.add(mob, READINESS_DELAY);
                    mobs.add(mob);
                }
            });
        }

        //important to process the visuals and pressing of cells last, so spawned mobs have a chance to occupy cells first
        for (Mob mob : mobs) {
            ScrollOfTeleportation.appear(mob, mob.pos());
        }
    }
}

