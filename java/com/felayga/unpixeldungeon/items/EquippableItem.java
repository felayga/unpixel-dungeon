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

public abstract class EquippableItem extends Item {
	public enum Slot {
		Weapon(0),
		Offhand(1),

        Face(2),
        Amulet(3),
        Ring1(4),
        Ring2(5),

        Cloak(6),
		Armor(7),
        Helmet(8),
		Gloves(9),
        Pants(10),
        Boots(11);

        public final int value;

        Slot(int value) {
            this.value = value;
        }

        public static int HIGHEST() { return Boots.value; }

        public static Slot fromInt(int value) {
            switch(value) {
                case 0:
                    return Weapon;
                case 1:
                    return Offhand;
                case 2:
                    return Face;
                case 3:
                    return Amulet;
                case 4:
                    return Ring1;
                case 5:
                    return Ring2;
                case 6:
                    return Cloak;
                case 7:
                    return Armor;
                case 8:
                    return Helmet;
                case 9:
                    return Gloves;
                case 10:
                    return Pants;
                default:
                    return Boots;
            }
        }
	}

	public static final String AC_EQUIP		= "EQUIP";
	public static final String AC_UNEQUIP	= "UNEQUIP";

	public long equipTime;
	public boolean twoHanded = false;

	public EquippableItem(long equipTime)
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
	public final boolean isEquipped( Char hero ) {
		return hero.belongings.isEquipped(this);
	}

	@Override
	public void doDrop( Hero hero ) {
		if (!isEquipped( hero ) || hero.belongings.unequip(this, true)) {
			super.doDrop( hero );
		}
	}

	@Override
	public void cast( final Hero user, int dst ) {

		if (isEquipped( user )) {
			if (quantity() == 1 && !user.belongings.unequip(this, true)) {
				return;
			}
		}

		super.cast( user, dst );
	}

}
