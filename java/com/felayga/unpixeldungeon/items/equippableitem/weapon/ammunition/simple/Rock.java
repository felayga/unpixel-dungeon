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
package com.felayga.unpixeldungeon.items.equippableitem.weapon.ammunition.simple;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ammunition.AmmunitionWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Rock extends AmmunitionWeapon {

    public IBag parent() {
        return super.parent();
    }
    public void parent(IBag parent) {
        super.parent(parent);

        GLog.d("set parent="+(parent != null ? parent.getDisplayName() : "<null>"));
    }

    public Rock() {
        super(WeaponSkill.Simple, GameTime.TICK, 0, 2, true, AmmunitionType.Stone);

        name = "rock";
        image = ItemSpriteSheet.MISSILE_ROCK;
        pickupSound = Assets.SND_ITEM_ROCK;
        material = Material.Mineral;

        requiresLauncher = false;

        hasLevels(false);
        hasBuc(false);

        weight(Encumbrance.UNIT * 3);
        price = 0;
    }

    @Override
    public String desc() {
        return "This is a simple rock.  You could throw it, I guess.";
    }

    @Override
    public Item random() {
        quantity(Random.IntRange(5, 11));
        return this;
    }

}
