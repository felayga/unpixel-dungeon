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
package com.felayga.unpixeldungeon.items.food;

import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Burning;
import com.felayga.unpixeldungeon.actors.buffs.Hunger;
import com.felayga.unpixeldungeon.actors.buffs.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.actors.buffs.Roots;
import com.felayga.unpixeldungeon.actors.buffs.Slow;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class MysteryMeat extends Food {

	{
		name = "mystery meat";
		image = ItemSpriteSheet.MEAT;
		energy = 125;
		hornValue = 1;
	}
	
	@Override
	public boolean execute( Hero hero, String action ) {
		boolean retval = super.execute( hero, action );
		
		if (action.equals( AC_EAT )) {
			switch (Random.Int( 5 )) {
			case 0:
				GLog.w( "Oh it's hot!" );
				Buff.affect( hero, Burning.class ).reignite( hero );
				break;
			case 1:
				GLog.w( "You can't feel your legs!" );
				Buff.prolong( hero, Roots.class, Paralysis.duration( hero ) );
				break;
			case 2:
				GLog.w( "You are not feeling well." );
				Buff.affect( hero, Poison.class ).set( Poison.durationFactor( hero ) * hero.HT / 5 );
				break;
			case 3:
				GLog.w( "You are stuffed." );
				Buff.prolong( hero, Slow.class, Slow.duration( hero ) );
				break;
			}
		}

		return retval;
	}
	
	@Override
	public String info() {
		return "Eat at your own risk!";
	}

	@Override
	public String message() {
		return "That food tasted... strange.";
	}
	
	public int price() {
		return 5 * quantity;
	};
}
