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
package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.Hunger;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.actors.buffs.Weakness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.utils.GLog;

public class PotionOfWater extends Potion {

	{
		name = "Potion of Water";
		initials = "H2O";

		bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		hero.buff(Hunger.class).satisfy_new(125);
		GLog.p("That was refreshing.");
	}
	
	@Override
	public String desc() {
		return
			"This is a flask of water.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
