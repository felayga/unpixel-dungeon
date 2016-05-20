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

import com.felayga.unpixeldungeon.actors.buffs.Amok;
import com.felayga.unpixeldungeon.actors.buffs.Sleep;
import com.felayga.unpixeldungeon.actors.buffs.Terror;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Imp;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.golem.GolemSprite;

public class Golem extends Mob {
	
	{
		name = "golem";
		spriteClass = GolemSprite.class;

		canOpenDoors = true;

		DEXCHA = 28;

		HP = HT = 85;
		defenseSkill = 18;
		
		EXP = 12;
		maxLvl = 22;

		belongings.weapon = new MeleeMobAttack(GameTime.TICK * 3 / 2, 20, 40);
	}
	
	@Override
	public String defenseVerb() {
		return "blocked";
	}
	
	@Override
	public void die( Object cause ) {
		Imp.Quest.process( this );
		
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"The Dwarves tried to combine their knowledge of mechanisms with their newfound power of elemental binding. " +
			"They used spirits of earth as the \"soul\" for the mechanical bodies of golems, which were believed to be " +
			"most controllable of all. Despite this, the tiniest mistake in the ritual could cause an outbreak.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Terror.class );
		IMMUNITIES.add( Sleep.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
