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
import com.felayga.unpixeldungeon.Journal;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Shadows;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.BlobEmitter;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.scenes.GameScene;

public class Foliage extends Blob {
	
	@Override
	protected void evolve() {

		int from = WIDTH + 1;
		int to = Level.LENGTH - WIDTH - 1;

		boolean regrowth = false;
		
		boolean visible = false;
		
		for (int pos=from; pos < to; pos++) {
			if (cur[pos] > 0) {
				off[pos] = cur[pos];
				volume += off[pos];

                int cell = Dungeon.level.map(pos);

                //todo: foliage dirtgrass growth?
				if (cell == Terrain.CHARCOAL) {
					Dungeon.level.set(pos, Terrain.GRASS, false);
					regrowth = true;
				}
				
				visible = visible || Dungeon.visible[pos];
				
			} else {
				off[pos] = 0;
			}
		}
		
		Hero hero = Dungeon.hero;
		if (hero.isAlive() && hero.visibleEnemies() == 0 && cur[hero.pos()] > 0) {
			Buff.affect( hero, null, Shadows.class ).prolong();
		}
		
		if (regrowth) {
			GameScene.updateMap();
		}
		
		if (visible) {
			Journal.add( Journal.Feature.GARDEN );
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( ShaftParticle.FACTORY, 0.9f, 0 );
	}
	
	@Override
	public String tileDesc() {
		return "Shafts of light pierce the gloom of the underground garden.";
	}
}
