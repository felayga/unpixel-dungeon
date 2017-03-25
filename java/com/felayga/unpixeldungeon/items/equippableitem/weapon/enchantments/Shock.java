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
package com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.Lightning;
import com.felayga.unpixeldungeon.effects.particles.SparkParticle;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.Weapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Shock extends Weapon.Enchantment {

	private static final String TXT_SHOCKING	= "Shocking %s";
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		int level = Math.max( 0, weapon.level() );
		
		if (Random.Int( level + 4 ) >= 3) {
			
			affected.clear();
			affected.add(attacker);

			arcs.clear();
			arcs.add(new Lightning.Arc(attacker.pos(), defender.pos()));
			hit(defender, Random.Int(1, damage / 2));

			attacker.sprite.parent.add( new Lightning( arcs, null ) );
			
			return true;
			
		} else {
			
			return false;
			
		}
	}
	
	@Override
	public String name( String weaponName ) {
		return String.format( TXT_SHOCKING, weaponName );
	}

	private ArrayList<Char> affected = new ArrayList<>();

	private ArrayList<Lightning.Arc> arcs = new ArrayList<>();
	
	private void hit( Char ch, int damage ) {
		
		if (damage < 1) {
			return;
		}
		
		affected.add(ch);
		ch.damage(Level.puddle[ch.pos()] && !ch.flying() ? damage * 2 : damage, MagicType.Shock, null, null);
		
		ch.sprite.centerEmitter(-1).burst(SparkParticle.FACTORY, 3);
		ch.sprite.flash();
		
		HashSet<Char> ns = new HashSet<Char>();
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			Char n = Actor.findChar( ch.pos() + Level.NEIGHBOURS8[i] );
			if (n != null && !affected.contains( n )) {
				arcs.add(new Lightning.Arc(ch.pos(), n.pos()));
				hit(n, Random.Int(damage / 2, damage));
			}
		}
	}
}
