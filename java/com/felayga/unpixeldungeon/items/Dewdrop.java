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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroSubClass;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Dewdrop extends Item {

	private static final String TXT_VALUE	= "%+dHP";
	
	{
		name = "dewdrop";
		image = ItemSpriteSheet.DEWDROP;
		
		stackable = true;
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		DewVial vial = hero.belongings.getItem( DewVial.class );
		
		if (hero.HP < hero.HT || vial == null || vial.isFull()) {
			
			int value = 1 + (Dungeon.depth - 1) / 5;
			if (hero.subClass == HeroSubClass.WARDEN) {
				value+=2;
			}
			
			int effect = Math.min( hero.HT - hero.HP, value * quantity() );
			if (effect > 0) {
				hero.HP += effect;
				hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				hero.sprite.showStatus( CharSprite.POSITIVE, TXT_VALUE, effect );
			} else {
				GLog.i("You already have full health.");
				return false;
			}
			
		} else {
			
			vial.collectDew( this );
			
		}
		
		Sample.INSTANCE.play( Assets.SND_DEWDROP );
		hero.spend_new(Constant.Time.ITEM_PICKUP, true);
		
		return true;
	}

	@Override
	//max of one dew in a stack
	public Item setQuantity(int value) {
		quantity(Math.min(value, 1));
		return this;
	}

	@Override
	public String info() {
		return "A crystal clear dewdrop.\n\nDue to the magic of this place, pure water has minor restorative properties.";
	}
}
