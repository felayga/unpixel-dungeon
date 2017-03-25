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

package com.felayga.unpixeldungeon.items.equippableitem;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.utils.GLog;

public abstract class EquippableItem extends Item {
	public enum Slot {
        Weapon(0),
        Offhand(1),

        Armor(2),
        Helmet(3),
        Gloves(4),
        Boots(5),

        Ranged(6),
        Ammo(7),

        Cloak(8),
        Amulet(9),
        Ring1(10),
        Ring2(11),

        None(-1);

        public static final int MAXEQUIPPED = 12;

        public final int value;

        Slot(int value) {
            this.value = value;
        }

        public static Slot fromInt(int value) {
            switch (value) {
                case 0:
                    return Weapon;
                case 1:
                    return Offhand;
                case 2:
                    return Armor;
                case 3:
                    return Helmet;
                case 4:
                    return Gloves;
                case 5:
                    return Boots;
                case 6:
                    return Ranged;
                case 7:
                    return Ammo;
                case 8:
                    return Cloak;
                case 9:
                    return Amulet;
                case 10:
                    return Ring1;
                case 11:
                    return Ring2;
                default:
                    return None;
            }
        }
    }

	public long equipTime;
	public boolean twoHanded = false;
    public boolean cursedCannotUnequip = true;

	public EquippableItem(long equipTime)
	{
		this.equipTime = equipTime;

		bones = true;
	}

	@Override
	public boolean execute( Hero hero, String action ) {
        if (Constant.Action.EQUIP.equals(action)) {
            hero.belongings.equip(this);
            return false;
        }
        else if (Constant.Action.UNEQUIP.equals(action)) {
            hero.belongings.unequip(this, true);
            return false;
        }
        else {
            return super.execute(hero, action);
		}
	}

	public abstract Slot[] getSlots();

	public abstract void onEquip(Char owner, boolean cursed);
	public abstract void onUnequip(Char owner);

	@Override
	public final boolean isEquipped( Char hero ) {
		return hero.belongings.isEquipped(this);
	}

    public enum EquipIfNecessaryState {
        NotEquipped,
        JustEquipped,
        AlreadyEquipped
    }

    public EquipIfNecessaryState equipIfNecessary(Char hero) {
        if (!hero.belongings.isEquipped(this)) {
            if (!hero.belongings.equip(this)) {
                return EquipIfNecessaryState.NotEquipped;
            }

            GLog.i(Constant.Text.HERO_READIED, getDisplayName());

            return EquipIfNecessaryState.JustEquipped;
        }

        return EquipIfNecessaryState.AlreadyEquipped;
    }

	@Override
	protected void doDrop( Hero hero, Item item ) {
        if (!isEquipped(hero)) {
            super.doDrop(hero, item);
        } else if (hero.belongings.unequip(this, true)) {
            super.doDrop(hero, item);
        }
    }

	@Override
	public void cast( final Hero user, int dst ) {
		if (cursedCannotUnequip && isEquipped( user )) {
			if (quantity() == 1 && !user.belongings.unequip(this, true)) {
                GLog.d("quantity=1, failed unequip, returned");
				return;
			}
		}

        GLog.d("EquippableItem: cast");
		super.cast( user, dst );
	}

}
