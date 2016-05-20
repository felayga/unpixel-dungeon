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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.Light;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.food.MysteryMeat;
import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.items.weapon.enchantments.Leech;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.CrippleChance;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.ScorpioSprite;
import com.watabou.utils.Random;

public class Scorpio extends Mob {
	
	{
		name = "scorpio";
		spriteClass = ScorpioSprite.class;
		
		HP = HT = 95;
		defenseSkill = 24;
		viewDistance = Light.DISTANCE;

		DEXCHA = 36;
		
		EXP = 14;
		maxLvl = 25;
		
		loot = new PotionOfHealing();
		lootChance = 0.2f;

		belongings.weapon = new CrippleChance(GameTime.TICK, 20, 32, 2, Cripple.DURATION);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		return !Level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}

	@Override
	protected Item createLoot() {
		//5/count+5 total chance of getting healing, failing the 2nd roll drops mystery meat instead.
		if (Random.Int( 5 /*+ Dungeon.limitedDrops.scorpioHP.count ) <= 4*/ ) < 2) {
			//Dungeon.limitedDrops.scorpioHP.count++;
			return (Item)loot;
		} else {
			return new MysteryMeat();
		}
	}

	@Override
	public String description() {
		return
			"These huge arachnid-like demonic creatures avoid close combat by all means, " +
			"firing crippling serrated spikes from long distances.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( Leech.class );
		RESISTANCES.add( Poison.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
