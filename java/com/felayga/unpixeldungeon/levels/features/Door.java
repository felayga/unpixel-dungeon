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
package com.felayga.unpixeldungeon.levels.features;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Door {
    public static final long TIME_TO_INTERACT = GameTime.TICK;

	public static void open(int pos) {
        Dungeon.level.set(pos, Terrain.OPEN_DOOR, true);
        GameScene.updateMap(pos);
        Dungeon.observe();

        if (Dungeon.audible[pos]) {
            Sample.INSTANCE.play(Assets.SND_OPEN);
        }
        /*
        //todo: mob door opening ensures wrong sounds, unexpected draft will never occur for player, workaround?
        if (!Dungeon.visible[pos]) {
            if (Random.Int(3) == 0) {
                GLog.i("You feel an unexpected draft.");
            }
        }
        */
    }

	public static void kickOpen(int pos) {
        Dungeon.level.set(pos, Terrain.OPEN_DOOR, true);
		GameScene.updateMap(pos);
		Dungeon.observe();

		if (Dungeon.audible[pos]) {
			Sample.INSTANCE.play(Assets.SND_DOOR_KICKOPEN);
		}
	}

	public static void unlock(int pos) {
        Dungeon.level.set(pos, Terrain.DOOR, true);
		GameScene.updateMap(pos);
		Dungeon.observe();

		if (Dungeon.audible[pos]) {
			Sample.INSTANCE.play(Assets.SND_UNLOCK);
		}
	}

	public static void close(int pos) {
		if (Dungeon.level.heaps.get(pos) == null) {
            Dungeon.level.set(pos, Terrain.DOOR, true);
			GameScene.updateMap(pos);
			Dungeon.observe();
		}

		if (Dungeon.audible[pos]) {
			Sample.INSTANCE.play(Assets.SND_DOOR_CLOSE);
		}
	}

	public static void hit(int pos) {
		if (Dungeon.audible[pos]) {
			Sample.INSTANCE.play(Assets.SND_DOOR_THUMP);
		}
	}

    public static int tryKick(Char kicker, int pos) {
        int strcon_squared = kicker.STRCON();
        strcon_squared *= strcon_squared;

        if (Random.PassFail(strcon_squared)) {
            if (Random.PassFail(strcon_squared - 256)) {
                Door.smash(pos);
                return 2;
            } else {
                Door.kickOpen(pos);
                return 1;
            }
        } else {
            Door.hit(pos);
            return 0;
        }
    }

	public static void smash(int pos) {
        Dungeon.level.set(pos, Terrain.WOOD_DEBRIS, true);
		GameScene.updateMap(pos);
		Dungeon.observe();

		if (Dungeon.audible[pos]) {
			CellEmitter.get(pos).burst(Speck.factory(Speck.WOOD), 5);
			Sample.INSTANCE.play(Assets.SND_DOOR_SMASH);
		}
	}

}
