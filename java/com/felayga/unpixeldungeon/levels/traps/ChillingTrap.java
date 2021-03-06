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
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Chill;
import com.felayga.unpixeldungeon.effects.Splash;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ChillingTrap extends Trap{

	{
		name = "Chilling trap";
		color = TrapSprite.WHITE;
		shape = TrapSprite.DOTS;
	}

	@Override
	public void activate() {
		if (Dungeon.visible[ pos ]){
			Splash.at(sprite.center(), 0xFFB2D6FF, 5);
		}
        if (Dungeon.audible[pos]) {
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }

        Char owner = Char.Registry.get(ownerRegistryIndex());

        Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null) heap.freeze(owner);

		Char ch = Actor.findChar( pos );
		if (ch != null){
			Chill.prolong(ch, owner, Chill.class, GameTime.TICK * (5 + Random.Long(Dungeon.depthAdjusted)));
			ch.damage(Random.NormalIntRange(1 , Dungeon.depthAdjusted), MagicType.Cold, null, null);
			if (!ch.isAlive() && ch == Dungeon.hero){
				Dungeon.fail( Utils.format(ResultDescriptions.TRAP, name) );
				GLog.n("You succumb to the chilling trap...");
			}
		}
	}

	@Override
	public String desc() {
		return "When activated, chemicals in this trap will trigger a snap-frost at its location.";
	}
}
