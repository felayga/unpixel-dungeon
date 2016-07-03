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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
//import com.felayga.unpixeldungeon.items.artifacts.DriedRose;
import com.felayga.unpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.scenes.InterlevelScene;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class WarpingTrap extends Trap {

	{
		name = "Warping trap";
		color = TrapSprite.TEAL;
		shape = TrapSprite.STARS;
	}

	@Override
	public void activate() {
		CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		Sample.INSTANCE.play( Assets.SND_TELEPORT );

        Char ch = Actor.findChar( pos );

		if (ScrollOfTeleportation.canTeleport(ch)) {
			int depth = ScrollOfTeleportation.doLevelPort(ch);

			Heap heap = Dungeon.level.heaps.get(pos);
			if (heap != null) {
				ArrayList<Item> dropped = Dungeon.droppedItems.get( depth );
				if (dropped == null) {
					Dungeon.droppedItems.put( depth, dropped = new ArrayList<Item>() );
				}

                Iterator<Item> iterator = heap.iterator(false);
				while (iterator.hasNext()){
                    Item item = iterator.next();
					dropped.add(item);
				}
				heap.destroy();
			}

		}

	}

	@Override
	public String desc() {
		return "Whatever triggers this trap will be warped to some other location in the dungeon.";
	}
}
