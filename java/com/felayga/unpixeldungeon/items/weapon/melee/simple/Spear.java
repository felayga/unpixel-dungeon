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
package com.felayga.unpixeldungeon.items.weapon.melee.simple;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class Spear extends SimpleMeleeWeapon {
	public Spear() {
		super( GameTime.TICK, 1, 8 );

		name = "spear";
		image = ItemSpriteSheet.SPEAR;
	}
	
	@Override
	public String desc() {
		return "A slender wooden rod tipped with sharpened iron.";
	}

	@Override
	public void playPickupSound() {
		Sample.INSTANCE.play( Assets.SND_ITEM_WOOD );
	}

}
