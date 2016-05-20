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
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Beam;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class DisintegrationTrap extends Trap {

	{
		name = "Disintegration trap";
		color = TrapSprite.VIOLET;
		shape = TrapSprite.LARGE_DOT;
	}

	@Override
	public void activate() {

		if (Dungeon.visible[ pos ]) {
			sprite.parent.add( new Beam.DeathRay( DungeonTilemap.tileCenterToWorld(pos-1),
					DungeonTilemap.tileCenterToWorld(pos+1)));
			sprite.parent.add(new Beam.DeathRay(DungeonTilemap.tileCenterToWorld(pos - Level.WIDTH),
					DungeonTilemap.tileCenterToWorld(pos + Level.WIDTH)));
			Sample.INSTANCE.play( Assets.SND_RAY );
		}


		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) heap.explode();

		Char ch = Actor.findChar(pos);
		if (ch != null){
			ch.damage( Math.max( ch.HT/5, Random.Int(ch.HP / 2, 2 * ch.HP / 3) ), this );
			if (ch == Dungeon.hero){
				Hero hero = (Hero)ch;
				if (!hero.isAlive()){
					Dungeon.fail(Utils.format(ResultDescriptions.TRAP, name));
					GLog.n("You were killed by the disintegration trap...");
				} else {
					Item item = hero.belongings.randomUnequipped();

					/*
					//todo: trap needs to be overhauled
					Bag bag = hero.belongings.backpack;
					//bags do not protect against this trap
					if (item instanceof Bag){
						bag = (Bag)item;
						item = Random.element(bag.items);
					}
					if (item.level > 0 || item.unique) return;
					if (!item.stackable){
						item.detachAll(bag);
						GLog.w("the trap disintegrates your " + item.getDisplayName() + "!");
					} else {
						int n = Random.NormalIntRange(1, (item.quantity()+1)/2);
						for(int i = 1; i <= n; i++)
							item.detach(bag);
						GLog.w("the trap disintegrates some of your " + item.getDisplayName() + "!");
					}
					*/
				}
			}
		}

	}

	@Override
	public String desc() {
		return "When triggered, this trap will lance the target with beams of disintegration, " +
				"dealing significant damage and destroying items.";
	}
}
