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
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Boulder;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.weapon.ammunition.simple.Rock;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.levels.features.Chasm;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/10/2017.
 */

public class TunnelingSpellcaster extends Spellcaster {
    public TunnelingSpellcaster() {
        super("Tunneling", 5);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.NoCollision, Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        callback.call();
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        if ((Level.flags & Level.FLAG_WALLS_NOT_DIGGABLE) != 0) {
            return;
        }

        if (path != null) {
            int distance = Random.IntRange(4, 6);
            for (int digPos : path.subPath(1, distance)) {
                int x = digPos % Level.WIDTH;
                int y = digPos / Level.WIDTH;

                if (x <= 0 || x >= Level.WIDTH - 1 || y <= 0 || y >= Level.HEIGHT - 1) {
                    break;
                }

                int terrain = Dungeon.level.map(digPos);

                if (Level.stone[digPos]) {
                    if (terrain == Terrain.WALL_STONE) {
                        Dungeon.level.setDirt(digPos, true, true);
                    } else {
                        Dungeon.level.setEmpty(digPos, true, true);
                    }

                    switch (Random.Int(12)) {
                        case 0:
                            GLog.w("You've dug out a boulder!");
                            Boulder npc = new Boulder();
                            npc.pos(digPos);
                            Dungeon.level.mobs.add(npc);
                            GameScene.add(npc);
                            break;
                        case 1:
                        case 2:
                            Dungeon.level.drop(new Rock().random(), digPos).rockBottom();
                            break;
                        default:
                            //nothing
                            break;
                    }

                    if (Dungeon.audible[digPos]) {
                        Sample.INSTANCE.play(Assets.SND_WALL_SMASH);
                    }

                    CellEmitter.get(digPos).burst(Speck.factory(Speck.DUST), 5);
                } else if (Level.burnable[digPos]) {
                    if (terrain == Terrain.DOOR || terrain == Terrain.LOCKED_DOOR || terrain == Terrain.OPEN_DOOR || terrain == Terrain.SECRET_DOOR || terrain == Terrain.SECRET_LOCKED_DOOR || terrain == Terrain.BARRICADE) {
                        Dungeon.level.setWoodDebris(digPos, true, true);

                        if (Dungeon.audible[digPos]) {
                            Sample.INSTANCE.play(Assets.SND_DOOR_SMASH);
                        }

                        CellEmitter.get(digPos).burst(Speck.factory(Speck.WOOD), 5);
                    }
                }
            }
        } else {
            if (digDown(targetPos)) {
                Char target = Actor.findChar(targetPos);

                if (target instanceof Hero) {
                    Chasm.heroFall(targetPos);
                } else if (target instanceof Mob) {
                    Chasm.mobFall((Mob)target);
                }
            }
        }
    }

    public boolean digDown(int pos) {
        if ((Terrain.flags[Dungeon.level.map(pos)] & Terrain.FLAG_UNDIGGABLE) != 0) {
            return false;
        }

        boolean retval;

        if ((Level.flags & Level.FLAG_CHASM_NOT_DIGGABLE) == 0) {
            Dungeon.level.setDirtChasm(pos, true, true);
            retval = true;
        } else {
            Dungeon.level.setDirtPit(pos, true, true);
            retval = false;
        }
        Dungeon.observe();

        if (Dungeon.audible[pos]) {
            Sample.INSTANCE.play(Assets.SND_WALL_SMASH);
        }

        CellEmitter.get(pos).burst(Speck.factory(Speck.DUST), 5);

        return retval;
    }
}
