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
package com.felayga.unpixeldungeon.items.keys;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class OldKey extends Item {

	public static final long TIME_TO_UNLOCK = GameTime.TICK;
	
	{
		stackable = true;
		unique = true;
	}


	@Override
	public void playPickupSound() {
		Sample.INSTANCE.play( Assets.SND_ITEM_KEY );
	}

	public int depth;
	
	@Override
	public boolean isSimilar( Item item ) {
		return item.getClass() == getClass() && ((OldKey)item).depth == depth;
	}
	
	private static final String DEPTH = "depth";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DEPTH, depth );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		depth = bundle.getInt( DEPTH );
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

}
