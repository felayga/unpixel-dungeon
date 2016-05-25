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
package com.felayga.unpixeldungeon.items.weapon.missiles;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Paralysis;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class CurareDart extends MissileWeapon {

	public static final long DURATION	= GameTime.TICK * 3;
	
	public CurareDart() {
		this( 1 );
	}
	
	public CurareDart( int number ) {
		super(WeaponSkill.Simple, GameTime.TICK, 1, 3, number);

		name = "curare dart";
		image = ItemSpriteSheet.CURARE_DART;

		//STR = 14;
        price = 8;
	}
	
	@Override
	public int proc( Char attacker, boolean thrown, Char defender, int damage ) {
		damage = super.proc(attacker, thrown, defender, damage);

		Buff.prolong( defender, Paralysis.class, DURATION );

		return damage;
	}
	
	@Override
	public String desc() {
		return
			"These little evil darts don't do much damage but they can paralyze " +
			"the target leaving it helpless and motionless for some time.";
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 2, 5 );
		return this;
	}

}
