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
package com.felayga.unpixeldungeon.actors.buffs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Ooze extends Buff {
	
	private static final String TXT_HERO_KILLED = "%s killed you...";

	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.OOZE;
	}
	
	@Override
	public String toString() {
		return "Caustic ooze";
	}

	@Override
	public String desc() {
		return "This sticky acid clings to flesh, slowly melting it away.\n" +
				"\n" +
				"Ooze will deal consistent damage until it is washed off in water.\n" +
				"\n" +
				"Ooze does not expire on its own and must be removed with water.";
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {
			if (Dungeon.depth > 4)
				target.damage( Dungeon.depth/5, MagicType.Acid, null );
			else if (Random.Int(2) == 0)
				target.damage( 1, MagicType.Acid, null );
			if (!target.isAlive() && target == Dungeon.hero) {
				Dungeon.fail( ResultDescriptions.OOZE );
				GLog.n( TXT_HERO_KILLED, toString() );
			}
			spend(GameTime.TICK, false );
		}
		if (Level.water[target.pos]) {
			detach();
		}
		return true;
	}
}
