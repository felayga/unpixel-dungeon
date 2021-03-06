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
package com.felayga.unpixeldungeon.actors.blobs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.AcidBurning;
import com.felayga.unpixeldungeon.effects.BlobEmitter;
import com.felayga.unpixeldungeon.effects.particles.AcidParticle;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class Acid extends Blob {

    @Override
    protected void evolve() {

        boolean[] flamable = Level.burnable;

        int from = WIDTH + 1;
        int to = Level.LENGTH - WIDTH - 1;

        boolean observe = false;

        for (int pos = from; pos < to; pos++) {

            int fire;

            if (cur[pos] > 0) {

                burn(pos);

                fire = cur[pos] - 1;
                if (fire <= 0 && flamable[pos]) {

                    int oldTile = Dungeon.level.map(pos);
                    Dungeon.level.set(pos, Terrain.CHARCOAL, true);

                    observe = true;
                    GameScene.updateMap(pos);
                    if (Dungeon.visible[pos]) {
                        GameScene.discoverTile(pos, oldTile);
                    }
                }

            } else {
                fire = 0;
				/*
				if (flamable[pos] && (cur[pos-1] > 0 || cur[pos+1] > 0 || cur[pos-WIDTH] > 0 || cur[pos+WIDTH] > 0)) {
					fire = 4;
					burn( pos );
				} else {
					fire = 0;
				}
				*/
            }

            volume += (off[pos] = fire);
        }

        if (observe) {
            Dungeon.observe();
        }
    }

    public static void burnChar(Char ch, Char source) {
        Buff.affect(ch, source, AcidBurning.class).resplash(ch);

        int levelDamage = 5 + Dungeon.depthAdjusted * 5;
        int damage = (ch.HT + levelDamage) / 5;
        if (Random.Int(5) < (ch.HT + levelDamage) % 5) {
            damage++;
        }

        ch.damage(damage, MagicType.Acid, source, null);
    }

    private void burn(int pos) {
        Char ch = Actor.findChar(pos);
        if (ch != null) {
            burnChar(ch, null);
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            heap.contentsBurn(Char.Registry.get(ownerRegistryIndex()));
        }

        Plant plant = Dungeon.level.plants.get(pos);
        if (plant != null) {
            plant.wither();
        }
    }

    public void seed(int cell, int amount) {
        if (cur[cell] == 0) {
            volume += amount;
            cur[cell] = amount;
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(AcidParticle.FACTORY, 0.03f, 0);
    }

    @Override
    public String tileDesc() {
        return "Acid coats the area.";
    }
}
