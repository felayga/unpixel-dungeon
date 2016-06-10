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
package com.felayga.unpixeldungeon.actors.buffs.hero;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.negative.Roots;
import com.felayga.unpixeldungeon.actors.buffs.negative.Slow;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.EarthParticle;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class EarthImbue extends FlavourBuff {

	public static final long DURATION	= GameTime.TICK * 30;

	public void proc(Char enemy){
		Buff.affect(enemy, Roots.class, GameTime.TICK * 2);
		CellEmitter.bottom(enemy.pos).start(EarthParticle.FACTORY, 0.05f, 8);
	}

	@Override
	public int icon() {
		return BuffIndicator.ROOTS;
	}

	@Override
	public String toString() {
		return "Imbued with Earth";
	}

	@Override
	public String desc() {
		return "You are inbued with the power of earth!\n" +
				"\n" +
				"All physical attacks will command roots to lock the enemy in place while the effect lasts.\n" +
				"\n" +
				"You are imbued for " + dispTurns() + ".";
	}

	{
		immunities.add( Paralysis.class );
		immunities.add( Roots.class );
		immunities.add( Slow.class );
	}
}
