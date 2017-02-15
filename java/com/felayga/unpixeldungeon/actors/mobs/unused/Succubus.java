/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * unPixel Dungeon
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
 *
 */
package com.felayga.unpixeldungeon.actors.mobs.unused;
/*
import java.util.ArrayList;
import java.util.HashSet;

import com.felayga.unpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.CharmChance;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.buffs.positive.Light;
import com.felayga.unpixeldungeon.actors.buffs.negative.Sleep;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.felayga.unpixeldungeon.items.weapon.enchantments.Leech;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.sprites.mobs.foocubus.IncubusSprite;
import com.felayga.unpixeldungeon.sprites.mobs.foocubus.SuccubusSprite;
import com.felayga.unpixeldungeon.windows.hero.WndInitHero;
import com.watabou.utils.Random;

public class Succubus extends Mob {
	private static final String DESCRIPTION_FEMALE = "The succubi are demons that look like seductive (in a slightly gothic way) girls. Using its magic, the succubus " +
			"can charm a hero, who will become unable to attack anything until the charm wears off.";
	private static final String DESCRIPTION_MALE = "The incubi are demons that look like seductive (in a slightly gothic way) men. Using its magic, the incubus " +
			"can charm a hero, who will become unable to attack anything until the charm wears off.";
	
	private static final int BLINK_DELAY	= 5;
	
	private int delay = 0;

	private String description;

	public Succubus()
	{
		if (WndInitHero.genderSelected == 0) {
			name = "succubus";
			spriteClass = SuccubusSprite.class;
			description = DESCRIPTION_FEMALE;
		}
		else {
			name = "incubus";
			spriteClass = IncubusSprite.class;
			description = DESCRIPTION_MALE;
		}

		canOpenDoors = true;

		DEXCHA = 40;

		HP = HT = 80;
		defenseSkill = 25;
		viewDistance = Light.DISTANCE;
		
		EXP = 12;
		maxLvl = 25;
		
		loot = new ScrollOfLullaby();
		lootChance = 0.05f;

		belongings.weapon = new CharmChance(GameTime.TICK, 15, 25);
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (Level.fieldOfView[target] && Level.distance( pos, target ) > 2 && delay <= 0) {
			
			blink( target );
			spend( -GameTime.TICK * GameTime.TICK / speed(), false );
			return true;
			
		} else {
			
			delay--;
			return super.getCloser( target );
			
		}
	}
	
	private void blink( int target ) {
		
		Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
		int cell = route.collisionPos;

		//can't occupy the same cell as another char, so move back one.
		if (Actor.findChar( cell ) != null && cell != this.pos)
			cell = route.path.get(route.dist-1);

		if (Level.avoid[ cell ]){
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			for (int n : Level.NEIGHBOURS8) {
				cell = route.collisionPos + n;
				if (Level.passable[cell] && Actor.findChar( cell ) == null) {
					candidates.add( cell );
				}
			}
			if (candidates.size() > 0)
				cell = Random.element(candidates);
			else {
				delay = BLINK_DELAY;
				return;
			}
		}
		
		ScrollOfTeleportation.appear( this, cell );
		
		delay = BLINK_DELAY;
	}
	
	@Override
	public String description() {
		return description;
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( Leech.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Sleep.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
*/