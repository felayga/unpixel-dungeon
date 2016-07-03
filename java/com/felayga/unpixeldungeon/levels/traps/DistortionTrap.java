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
import com.felayga.unpixeldungeon.scenes.InterlevelScene;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.Game;

public class DistortionTrap extends Trap{

	{
		name = "Distortion trap";
		color = TrapSprite.TEAL;
		shape = TrapSprite.LARGE_DOT;
	}

	@Override
	public void activate() {
		InterlevelScene.returnDepth = Dungeon.depthAdjusted;

		/*
		//todo: fix distortion trap if cared about
		for (Item item : Dungeon.hero.belongings.backpack.items.toArray( new Item[Dungeon.hero.belongings.backpack.items.size()])){
			if (item instanceof OldKey && ((OldKey)item).depth == Dungeon.depth){
				item.detachAll(Dungeon.hero.belongings.backpack);
			}
		}
		*/
		InterlevelScene.mode = InterlevelScene.Mode.RESET;
		Game.switchScene(InterlevelScene.class);
	}

	@Override
	public String desc() {
		return "Built from strange magic of unknown origin, this trap will shift and morph the world around you.";
	}
}
