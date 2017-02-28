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

package com.felayga.unpixeldungeon.items.weapon.ammunition.martial;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.weapon.ammunition.AmmunitionWeapon;
import com.felayga.unpixeldungeon.items.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/16/2017.
 */
public class Arrow extends AmmunitionWeapon {
    public Arrow() {
        this( 1 );
    }

    public Arrow(int quantity) {
        super(WeaponSkill.Martial, GameTime.TICK, 1, 6, quantity, true, AmmunitionType.Arrow);

        name = "arrow";
        image = ItemSpriteSheet.MISSILE_ARROW;
        pickupSound = Assets.SND_ITEM_BLADE;

        requiresLauncher = true;

        hasLevels(true);
        hasBuc(true);

        weight(Encumbrance.UNIT * 1);
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

