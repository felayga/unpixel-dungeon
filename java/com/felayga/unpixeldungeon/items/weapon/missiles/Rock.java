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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.buffs.Encumbrance;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Rock extends MissileWeapon {

    public Rock() {
        this( 1 );
    }

    public Rock(int quantity)
    {
        super(WeaponSkill.None, GameTime.TICK, 0, 2, quantity);

        name = "rock";
        image = ItemSpriteSheet.ROCK;

        bones = false; //Finding them in bones would be semi-frequent and disappointing.

        hasLevels = false;

        weight(Encumbrance.UNIT * 10);
        price = 0;
    }


    @Override
    public String desc() {
        return "This is a simple rock.  You could throw it, I guess.";
    }

    @Override
    public Item random() {
        quantity = Random.Int( 5, 11 );
        return this;
    }


    @Override
    public void playPickupSound() {
        Sample.INSTANCE.play( Assets.SND_ITEM_ROCK );
    }
}
