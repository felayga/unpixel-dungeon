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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Sheep;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class FlockTrap extends Trap {

	{
		name = "Flock trap";
		color = TrapSprite.WHITE;
		shape = TrapSprite.WAVES;
	}


	@Override
	public void activate() {
		//use an actor as we want to put this on a slight delay so all chars get a chance to act this turn first.
		Actor.add(new Actor() {

			{ actPriority = 3; }

			protected boolean act() {
				int cell;
				for (int i : Level.NEIGHBOURS9DIST2) {
					cell = pos + i;
					if (Level.insideMap(cell) && Actor.findChar(cell) == null && !(Level.solid[cell] || Level.chasm[cell])) {
						Sheep sheep = new Sheep();
						sheep.lifespan = GameTime.TICK * (2 + Random.Int(Dungeon.depthAdjusted + 10));
						sheep.pos(cell);
						GameScene.add(sheep);
					}
					CellEmitter.get(cell).burst(Speck.factory(Speck.WOOL), 4);
				}
                if (Level.fieldOfSound[pos]) {
                    Sample.INSTANCE.play(Assets.SND_PUFF);
                }
				Actor.remove(this);
				return true;
			}
		});

	}

	@Override
	public String desc() {
		return "Perhaps a joke from some amateur mage, triggering this trap will create a flock of magical sheep.";
	}
}
