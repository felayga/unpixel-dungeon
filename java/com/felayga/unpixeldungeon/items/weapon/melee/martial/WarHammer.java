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
package com.felayga.unpixeldungeon.items.weapon.melee.martial;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class WarHammer extends MartialMeleeWeapon {
	public WarHammer() {
		super( GameTime.TICK, 1, 8 );

		name = "war hammer";
		image = ItemSpriteSheet.MELEE_WAR_HAMMER;
        pickupSound = Assets.SND_ITEM_WOOD;
	}
	
	@Override
	public String desc() {
		return
			"Few creatures can withstand the crushing blow of this towering mass of lead and steel, " +
			"but only the strongest of adventurers can use it effectively.";
	}

}
