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
package com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.martial;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Javelin extends MissileWeapon {
	public Javelin() {
		super(WeaponSkill.Martial, GameTime.TICK, 1, 6, true, AmmunitionType.None);

		name = "javelin";
		image = ItemSpriteSheet.MISSILE_JAVELIN;
        material = Material.Iron;

		weight(20 * Encumbrance.UNIT);
        price = 3;
	}
	
	@Override
	public int proc( Char attacker, boolean thrown, Char defender, int damage ) {
		damage = super.proc( attacker, thrown, defender, damage );

		Buff.prolong( defender, attacker, Cripple.class, Cripple.DURATION );

		return damage;
	}
	
	@Override
	public String desc() {
		return
			"This length of metal is weighted to keep the spike " +
			"at its tip foremost as it sails through the air.";
	}
	
	@Override
	public Item random() {
		quantity(Random.IntRange( 5, 15 ));
		return this;
	}

}
