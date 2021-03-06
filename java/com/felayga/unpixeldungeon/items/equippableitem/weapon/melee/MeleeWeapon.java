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
package com.felayga.unpixeldungeon.items.equippableitem.weapon.melee;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.Weapon;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon {
	public MeleeWeapon(WeaponSkill weaponSkill, long delay, int damageMin, int damageMax) {
		super(weaponSkill, delay, damageMin, damageMax);

        price = 20;
		
		//ACU = acu;

		//STR = typicalSTR();
		
		//MIN = min();
		//MAX = max();
	}

	/*
	private int min() {
		return tier;
	}
	
	private int max() {
		return (int)((tier * tier - tier + 10) / ACU * DLY);
	}
	*/
	
	@Override
	public String info() {
		
		final String p = "\n\n";
		
		StringBuilder info = new StringBuilder( desc() );
		
		String quality = levelKnown() && level() != 0 ? (level() > 0 ? "upgraded" : "degraded") : "";
		info.append( p );
		/*
		info.append( "This " + name + " is " + Utils.indefinite( quality ) );
		info.append( " tier-" + tier + " melee weapon. " );
		*/
		/*
		if (levelKnown) {
			info.append( "Its average damage is " +
				Math.round((MIN + (MAX - MIN) / 2)*(imbue == Imbue.LIGHT ? 0.7f : (imbue == Imbue.HEAVY ? 1.5f : 1)))
				+ " points per hit. " );
		} else {
			info.append(
				"Its typical average damage is " + (min() + (max() - min()) / 2) + " points per hit " +
				"and usually it requires " + typicalSTR() + " points of strength. " );
			if (typicalSTR() > Dungeon.hero.STR()) {
				info.append( "Probably this weapon is too heavy for you. " );
			}
		}
		*/
		/*
		if (DLY != 1f) {
			info.append( "This is a rather " + (DLY < 1f ? "fast" : "slow") );
			if (ACU != 1f) {
				if ((ACU > 1f) == (DLY < 1f)) {
					info.append( " and ");
				} else {
					info.append( " but ");
				}
				info.append( ACU > 1f ? "accurate" : "inaccurate" );
			}
			info.append( " weapon. ");
		} else if (ACU != 1f) {
			info.append( "This is a rather " + (ACU > 1f ? "accurate" : "inaccurate") + " weapon. " );
		}
		switch (imbue) {
			case LIGHT:
				info.append( "It was balanced to be lighter. " );
				break;
			case HEAVY:
				info.append( "It was balanced to be heavier. " );
				break;
			case NONE:
		}
		*/
		if (enchantment != null) {
			info.append( "It is enchanted." );
		}
		/*
		if (levelKnown && Dungeon.hero.belongings.backpack.items.contains( this )) {
			if (STR > Dungeon.hero.STR()) {
				info.append( p );
				info.append(
					"Because of your inadequate strength the accuracy and speed " +
					"of your attack with this " + name + " is decreased." );
			}
			if (STR < Dungeon.hero.STR()) {
				info.append( p );
				info.append(
					"Because of your excess strength the damage " +
					"of your attack with this " + name + " is increased." );
			}
		}
		*/
		if (isEquipped( Dungeon.hero )) {
			info.append( p );
			info.append( "You hold the " + name + " at the ready" +
				(bucStatus() == BUCStatus.Cursed ? ", and because it is cursed, you are powerless to let go." : ".") );
		} else {
			if (bucStatusKnown() && bucStatus() == BUCStatus.Cursed) {
				info.append( p );
				info.append( "You can feel a malevolent magic lurking within the " + name +"." );
			}
		}
		
		return info.toString();
	}

	@Override
	public Item random() {
		super.random();
		
		if (Random.Int( 10 + level() ) == 0) {
			enchant();
		}
		
		return this;
	}
}
