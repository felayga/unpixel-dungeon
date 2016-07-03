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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SummoningTrap extends Trap {

    private static final long DELAY = GameTime.TICK * 2;

    {
        name = "Summoning trap";
        color = TrapSprite.TEAL;
        shape = TrapSprite.WAVES;
    }

    @Override
    public void activate() {
        //todo: no summons if boss present?

        int nMobs = 1;
        if (Random.Int(2) == 0) {
            nMobs++;
            if (Random.Int(2) == 0) {
                nMobs++;
            }
        }

        ArrayList<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
            int p = pos + Level.NEIGHBOURS8[i];
            if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
                candidates.add(p);
            }
        }

        ArrayList<Integer> respawnPoints = new ArrayList<>();

        while (nMobs > 0 && candidates.size() > 0) {
            int index = Random.index(candidates);

            respawnPoints.add(candidates.remove(index));
            nMobs--;
        }

        final ArrayList<Mob> mobs = new ArrayList<>();

        for (final Integer point : respawnPoints) {
            Bestiary.spawn(Dungeon.depthAdjusted, Dungeon.hero.level, true, new Bestiary.SpawnParams() {
                @Override
                public Level level() {
                    return Dungeon.level;
                }

                @Override
                public int position() {
                    return point;
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
                    GameScene.add(mob, DELAY);
                    mobs.add(mob);
                }
            });
        }

        //important to process the visuals and pressing of cells last, so spawned mobs have a chance to occupy cells first
        for (Mob mob : mobs) {
            ScrollOfTeleportation.appear(mob, mob.pos);
        }

    }

    @Override
    public String desc() {
        return "Triggering this trap will summon a number of monsters from the surrounding floors to this location.";
    }
}
