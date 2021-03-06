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
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class RockfallTrap extends Trap {

	{
		name = "Rockfall trap";
		color = TrapSprite.GREY;
		shape = TrapSprite.DIAMOND;
	}

	@Override
	public void activate() {

		if (Dungeon.visible[ pos ]){
			CellEmitter.get( pos - Level.WIDTH ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
		}
        if (Dungeon.audible[pos]) {
            Camera.main.shake(3, 0.7f);
            Sample.INSTANCE.play( Assets.SND_ROCKS );
        }

		Char ch = Actor.findChar( pos );

		if (ch != null){
			int damage = Random.NormalIntRange(5+Dungeon.depthAdjusted, 10+Dungeon.depthAdjusted*3);
			ch.damage( Math.max(damage, 0) , MagicType.Mundane, null, null);

			Buff.prolong( ch, Char.Registry.get(ownerRegistryIndex()), Paralysis.class, Paralysis.duration(ch)*2);

			if (!ch.isAlive() && ch == Dungeon.hero){
				Dungeon.fail(Utils.format(ResultDescriptions.TRAP, name));
				GLog.n("You were crushed by the rockfall trap...");
			}
		}
	}

	@Override
	public String desc() {
		return "This trap is connected to a series of loose rocks above, " +
				"triggering it will cause them to come crashing down.";
	}
}
