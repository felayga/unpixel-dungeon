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

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

/**
 * Created by HELLO on 3/10/2017.
 */

public class FireblastSpellcaster extends Spellcaster {
    public FireblastSpellcaster() {
        super("Fireblast", 4);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopTerrain);
        affectedCells = new HashSet<>();
        visualCells = new HashSet<>();
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells.clear();
        visualCells.clear();

        final int maxDist = 3;

        int dist = Math.min(path.path.size() - 1, maxDist);

        for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
            if (path.sourcePos + Level.NEIGHBOURS8[i] == path.path.get(1)) {
                direction = i;
                break;
            }
        }

        float strength = maxDist;
        for (int c : path.subPath(1, dist)) {
            strength--; //as we start at dist 1, not 0.
            affectedCells.add(c);
            if (strength > 1) {
                spreadFlames(c + Level.NEIGHBOURS8[left(direction)], strength - 1);
                spreadFlames(c + Level.NEIGHBOURS8[direction], strength - 1);
                spreadFlames(c + Level.NEIGHBOURS8[right(direction)], strength - 1);
            } else {
                visualCells.add(c);
            }
        }

        //going to call this one manually
        visualCells.remove(path.path.get(dist));

        for (int cell : visualCells) {
            //this way we only get the cells at the tip, much better performance.
            MagicMissile.fire(source.sprite.parent, path.sourcePos, cell, null);
        }
        MagicMissile.fire(source.sprite.parent, path.sourcePos, path.path.get(dist), callback);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        if (Level.burnable[targetPos]) {
            GameScene.add(Blob.seed(source, targetPos, 2, Fire.class));
        }

        for (int cell : affectedCells) {
            GameScene.add(Blob.seed(source, cell, 2, Fire.class));
            Char ch = Actor.findChar(cell);
            if (ch != null) {

                ch.damage(damage(Random.NormalIntRange(3, 18)), MagicType.Fire, source, null);
                Buff.affect(ch, source, Burning.class).reignite(ch);
                /*
				switch(1){
					case 1:
						Buff.affect(ch, curUser, Cripple.class, GameTime.TICK * 3); break;
					case 2:
						Buff.affect(ch, curUser, Cripple.class, GameTime.TICK * 6); break;
					case 3:
						Buff.affect(ch, curUser, Paralysis.class, GameTime.TICK * 3); break;
					case 4:
						Buff.affect(ch, curUser, Paralysis.class, GameTime.TICK * 6); break;
				}
				*/
            }
        }
    }

    //the actual affected cells
    private HashSet<Integer> affectedCells;
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;
    private int direction = 0;

    //burn... BURNNNNN!.....
    private void spreadFlames(int cell, float strength) {
        if (strength >= 0 && Level.passable[cell]) {
            affectedCells.add(cell);
            if (strength >= 1.5f) {
                visualCells.remove(cell);
                spreadFlames(cell + Level.NEIGHBOURS8[left(direction)], strength - 1.5f);
                spreadFlames(cell + Level.NEIGHBOURS8[direction], strength - 1.5f);
                spreadFlames(cell + Level.NEIGHBOURS8[right(direction)], strength - 1.5f);
            } else {
                visualCells.add(cell);
            }
        } else if (!Level.passable[cell]) {
            visualCells.add(cell);
        }
    }

    private int left(int direction) {
        return direction == 0 ? 7 : direction - 1;
    }

    private int right(int direction) {
        return direction == 7 ? 0 : direction + 1;
    }
}
