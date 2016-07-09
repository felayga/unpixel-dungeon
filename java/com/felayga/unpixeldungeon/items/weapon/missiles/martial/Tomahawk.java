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
package com.felayga.unpixeldungeon.items.weapon.missiles.martial;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Bleeding;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Tomahawk extends MissileWeapon {

	public Tomahawk() {
		this( 1 );
	}
	
	public Tomahawk(int number) {
		super(WeaponSkill.Martial, GameTime.TICK, 4, 20, number, true, AmmunitionType.None);

		name = "tomahawk";
		image = ItemSpriteSheet.MISSILE_TOMAHAWK;

		//STR = 17;
        price = 15;
	}
	
	@Override
	public int proc( Char attacker, boolean thrown, Char defender, int damage ) {
		damage = super.proc( attacker, thrown, defender, damage );

		Buff.affect( defender, Bleeding.class ).set( damage );

		return damage;
	}
	
	@Override
	public String desc() {
		return
			"This throwing axe is not that heavy, but it still " +
			"requires significant strength to be used effectively.";
	}
	
	@Override
	public Item random() {
		quantity(Random.Int(5, 12));
		return this;
	}

}
