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
package com.felayga.unpixeldungeon.actors.buffs.hero;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class FireImbue extends Buff {

	public static final long DURATION	= 30 * GameTime.TICK;

	protected long left;

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( TIMELEFT, left );

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		left = bundle.getLong(TIMELEFT);
	}

	public void set( long duration ) {
		this.left = duration;
	}

	@Override
	public boolean act() {
		if (Dungeon.level.map[target.pos()] == Terrain.GRASS) {
			Dungeon.level.set(target.pos(), Terrain.EMBERS, true);
			GameScene.updateMap(target.pos());
		}

        spend_new(GameTime.TICK, false);
		left -= GameTime.TICK;
		if (left <= 0)
			detach();

		return true;
	}

	public void proc(Char enemy){
		if (Random.Int(2) == 0)
			Buff.affect( enemy, Char.Registry.get(ownerRegistryIndex()), Burning.class ).reignite( enemy );

		enemy.sprite.emitter().burst( FlameParticle.FACTORY, 2 );
	}

	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}

	@Override
	public String toString() {
		return "Imbued with Fire";
	}

	@Override
	public String desc() {
		return "You are imbued with the power of fire!\n" +
				"\n" +
				"All physical attacks will have a chance to light enemies ablaze. " +
				"Additionally, you are completely immune to the effects of fire.\n" +
				"\n" +
				"You are imbued for " + dispTurns(left) + ".";
	}

    /*
	{
		immunities.add( Burning.class );
	}
    */
}
