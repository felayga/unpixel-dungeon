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
package com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.simple;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class IncendiaryDart extends MissileWeapon {
    public IncendiaryDart() {
		super(WeaponSkill.Simple, GameTime.TICK, 1, 2, true, AmmunitionType.None);

		name = "incendiary dart";
		image = ItemSpriteSheet.MISSILE_INCENDIARY_DART;
        material = Material.Iron;

        weight(1 * Encumbrance.UNIT);
        price = 6;
	}
	
	@Override
	protected void onThrow( Char thrower, int cell ) {
		Char enemy = Actor.findChar( cell );
		if ((enemy == null || enemy == curUser) && Level.burnable[cell])
			GameScene.add( Blob.seed( thrower, cell, 4, Fire.class ) );
		else
			super.onThrow( thrower, cell );
	}
	
	@Override
	public int proc( Char attacker, boolean thrown, Char defender, int damage ) {
		damage = super.proc( attacker, thrown, defender, damage);

		Buff.affect( defender, attacker, Burning.class ).reignite( defender );

		return damage;
	}
	
	@Override
	public String desc() {
		return
			"The spike on each of these darts is designed to pin it to its target " +
			"while the unstable compounds strapped to its length burst into brilliant flames.";
	}
	
	@Override
	public Item random() {
		quantity(Random.IntRange(3, 6));
		return this;
	}

}
