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
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.PoisonParticle;
import com.felayga.unpixeldungeon.sprites.TrapSprite;

public class PoisonTrap extends Trap{

	{
		name = "Poison trap";
		color = TrapSprite.VIOLET;
		shape = TrapSprite.CROSSHAIR;
	}

	@Override
	public void activate() {

		Char ch = Actor.findChar( pos );

		if (ch != null) {
            //todo: poison damage rebalancing after buff update
            Poison.affect(ch, 4 + Dungeon.depthAdjusted / 2);
		}

		CellEmitter.center( pos ).burst( PoisonParticle.SPLASH, 3 );

	}

	@Override
	public String desc() {
		return "A small dart-blower must be hidden nearby, activating this trap will cause it to shoot a poisoned dart at you.";
	}
}
