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

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Dart extends MissileWeapon {
    public Dart() {
        super(WeaponSkill.Simple, GameTime.TICK, 1, 4, true, AmmunitionType.None);

        name = "dart";
        image = ItemSpriteSheet.MISSILE_DART;
        material = Material.Iron;

        weight(1 * Encumbrance.UNIT);
        price = 2;
    }

    @Override
    public void parent(IBag what) {
        super.parent(what);

        GLog.d("this="+getDisplayName()+" parent="+(parent() != null ? parent().getDisplayName() : "null"));
    }

    @Override
    public IBag parent() {
        IBag retval = super.parent();

        return retval;
    }
	
	@Override
	public String desc() {
		return
			"These simple metal spikes are weighted to fly true and " +
			"sting their prey with a flick of the wrist.";
	}
	
	@Override
	public Item random() {
		quantity(Random.IntRange( 5, 15 ));
		return this;
	}

}
