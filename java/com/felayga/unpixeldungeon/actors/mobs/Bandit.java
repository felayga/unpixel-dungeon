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
package com.felayga.unpixeldungeon.actors.mobs;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.sprites.BanditSprite;
import com.watabou.utils.Random;

public class Bandit extends Thief {
	
	public Item item;
	
	{
		name = "crazy bandit";
		spriteClass = BanditSprite.class;

		//1 in 30 chance to be a crazy bandit, equates to overall 1/90 chance.
		lootChance = 0.333f;
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Badges.validateRare( this );
	}
}
