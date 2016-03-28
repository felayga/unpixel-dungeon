/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;

public abstract class EquipableItem extends Item {
	public enum Slot {
		Weapon,
		Offhand,
		Armor,
		Gloves,
		Boots,
		Ring,
		Amulet,
		Cloak,
		Face
	}

	public static final String AC_EQUIP		= "EQUIP";
	public static final String AC_UNEQUIP	= "UNEQUIP";

	public long equipTime;

	public EquipableItem(long equipTime)
	{
		this.equipTime = equipTime;

		bones = true;
	}

	@Override
	public boolean execute( Hero hero, String action ) {
		switch (action) {
			case AC_EQUIP:
				hero.belongings.equip(this);
				return false;
			case AC_UNEQUIP:
				hero.belongings.unequip(this, true);
				return false;
			default:
				return super.execute(hero, action);
		}
	}

	public abstract Slot[] getSlots();

	public void onEquip(Char owner, boolean cursed) { }
	public void onUnequip(Char owner) { }

	@Override
	public void doDrop( Hero hero ) {
		if (!isEquipped( hero ) || hero.belongings.unequip(this, true)) {
			super.doDrop( hero );
		}
	}

	@Override
	public void cast( final Hero user, int dst ) {

		if (isEquipped( user )) {
			if (quantity == 1 && !user.belongings.unequip(this, true)) {
				return;
			}
		}

		super.cast( user, dst );
	}

}
