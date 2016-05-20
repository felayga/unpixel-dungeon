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
package com.felayga.unpixeldungeon.actors.mobs;

import java.util.HashSet;

import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.items.weapon.enchantments.Leech;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.LeechChance;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.bat.BatSprite;

public class Bat extends Mob {

	{
		name = "vampire bat";
		spriteClass = BatSprite.class;

		DEXCHA = 16;

		HP = HT = 30;
		defenseSkill = 15;
		movementSpeed = GameTime.TICK * 2;
		
		EXP = 7;
		maxLvl = 15;
		
		flying = true;
		
		loot = new PotionOfHealing();
		lootChance = 0.1667f; //by default, see die()

		belongings.weapon = new LeechChance(GameTime.TICK, 6, 12);
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}


	/*
	@Override
	public void die( Object cause ){
		//sets drop chance
		lootChance = 1f/((6 + Dungeon.limitedDrops.batHP.count ));
		super.die( cause );
	}

	@Override
	protected Item createLoot(){
		Dungeon.limitedDrops.batHP.count++;
		return super.createLoot();
	}
	*/

	@Override
	public String description() {
		return
			"These brisk and tenacious inhabitants of cave domes may defeat much larger opponents by " +
			"replenishing their health with each successful attack.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( Leech.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
