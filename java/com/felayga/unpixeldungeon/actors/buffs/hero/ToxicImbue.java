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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.ToxicGas;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class ToxicImbue extends Buff {

	public static final long DURATION	= 30 * GameTime.TICK;

	protected long left;

    public ToxicImbue() {

    }

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( TIMELEFT, left );

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		left = bundle.getLong(TIMELEFT );
	}

	public void set( long duration ) {
		this.left = duration;
	}


	@Override
	public boolean act() {
		GameScene.add(Blob.seed(Char.Registry.get(ownerRegistryIndex()), target.pos(), 50, ToxicGas.class));

        spend_new(GameTime.TICK, false);
		left -= GameTime.TICK;
		if (left <= 0)
			detach();

		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.IMMUNITY;
	}

	@Override
	public String toString() {
		return "Imbued with Toxicity";
	}

	@Override
	public String desc() {
		return "You are imbued with poisonous energy!\n" +
				"\n" +
				"As you move around toxic gas will constantly billow forth from you, damaging your enemies. " +
				"You are immune to toxic gas and poison for the duration of the effect.\n" +
				"\n" +
				"You are imbued for " + dispTurns(left) + ".";
	}

    /*
	{
		immunities.add( ToxicGas.class );
		immunities.add( Poison.class );
	}
    */
}
