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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.sprites.MissileSprite;

public class Boomerang extends MissileWeapon {

	public Boomerang()
	{
		super(WeaponSkill.Martial, GameTime.TICK, 1, 9, true, AmmunitionType.None);

		name = "boomerang";
		image = ItemSpriteSheet.RANGED_BOOMERANG;
        material = Material.Wood;

        //STR = 10;
		
		stackable = false;

		unique = true;
		bones = false;

        weight(5 * Encumbrance.UNIT);
        price = 20;
	}

	@Override
	public int proc( Char attacker, boolean thrown, Char defender, int damage ) {
		damage = super.proc( attacker, thrown, defender, damage );

		if (attacker == Dungeon.hero && thrown) {
			circleBack( defender.pos(), attacker );
		}

		return damage;
	}

	@Override
	protected void miss( int cell, Char thrower ) {
		circleBack( cell, thrower );
	}

	private void circleBack( int from, Char owner ) {

		((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
				reset( from, curUser.pos(), curItem, null );

        if (throwEquiped) {
            owner.belongings.collectEquip(this);
            owner.spend_new(-TIME_TO_EQUIP, false);
            Dungeon.quickslot.replaceSimilar(this);
            updateQuickslot();
        } else if (!curUser.belongings.collect(this)) {
            Dungeon.level.drop(this, owner.pos()).sprite.drop();
        }
	}

	private boolean throwEquiped;

	@Override
	public void cast( Hero user, int dst ) {
		throwEquiped = isEquipped( user );
		super.cast( user, dst );
	}
	
	@Override
	public String desc() {
		String info =
			"Thrown to the enemy this flat curved wooden missile will return to the hands of its thrower.";
		/*
		switch (imbue) {
			case LIGHT:
				info += "\n\nIt was balanced to be lighter. ";
				break;
			case HEAVY:
				info += "\n\nIt was balanced to be heavier. ";
				break;
			case NONE:
		}
		*/
		return info;
	}
}
