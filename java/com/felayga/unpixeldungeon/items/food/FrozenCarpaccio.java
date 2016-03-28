/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.items.food;

import com.felayga.unpixeldungeon.actors.buffs.Barkskin;
import com.felayga.unpixeldungeon.actors.buffs.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.Drowsy;
import com.felayga.unpixeldungeon.actors.buffs.Hunger;
import com.felayga.unpixeldungeon.actors.buffs.Invisibility;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.actors.buffs.Slow;
import com.felayga.unpixeldungeon.actors.buffs.Vertigo;
import com.felayga.unpixeldungeon.actors.buffs.Weakness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class FrozenCarpaccio extends Food {

	{
		name = "frozen carpaccio";
		image = ItemSpriteSheet.CARPACCIO;
		energy = 125;
		hornValue = 1;
	}
	
	@Override
	public boolean execute( Hero hero, String action ) {
		boolean retval = super.execute( hero, action );
		
		if (action.equals( AC_EAT )) {
			switch (Random.Int( 5 )) {
			case 0:
				GLog.i( "You see your hands turn invisible!" );
				Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
				break;
			case 1:
				GLog.i( "You feel your skin harden!" );
				Buff.affect( hero, Barkskin.class ).level( hero.HT / 4 );
				break;
			case 2:
				GLog.i( "Refreshing!" );
				Buff.detach( hero, Poison.class );
				Buff.detach( hero, Cripple.class );
				Buff.detach( hero, Weakness.class );
				Buff.detach( hero, Bleeding.class );
				Buff.detach( hero, Drowsy.class );
				Buff.detach( hero, Slow.class );
				Buff.detach( hero, Vertigo.class);
				break;
			case 3:
				GLog.i( "You feel better!" );
				if (hero.HP < hero.HT) {
					hero.HP = Math.min( hero.HP + hero.HT / 4, hero.HT );
					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				}
				break;
			}
		}

		return retval;
	}
	
	@Override
	public String info() {
		return
			"It's a piece of frozen raw meat. The only way to eat it is " +
			"by cutting thin slices of it. And this way it's suprisingly good.";
	}
	
	public int price() {
		return 10 * quantity;
	};
	
	public static Food cook( MysteryMeat ingredient ) {
		FrozenCarpaccio result = new FrozenCarpaccio();
		result.quantity = ingredient.quantity();
		return result;
	}
}
