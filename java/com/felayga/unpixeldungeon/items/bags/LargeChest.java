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

package com.felayga.unpixeldungeon.items.bags;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Roll;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.Icons;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 7/6/2016.
 */
public class LargeChest extends Bag {
    public LargeChest() {
        this(null, true);
    }

    public LargeChest(Char owner, boolean lockable) {
        super(owner, lockable, Encumbrance.UNIT * 600);

        pickupSound = Assets.SND_ITEM_BOX;

        name = "large chest";
        image = ItemSpriteSheet.CHEST;
        tabIcon = Icons.TREASURECHEST;

        size = 36;
        priority = 8;
        price = 16;
    }

    @Override
    protected void onLockedChanged() {
        if (locked()) {
            image = ItemSpriteSheet.CHEST_LOCKED;
        } else {
            image = ItemSpriteSheet.CHEST;
        }
    }

    @Override
    public Item random() {
        float dropBonusChance = Roll.DropBonusChance(Dungeon.hero) / 2.0f;

        int itemcount = 0;

        while (Random.Float() < dropBonusChance) {
            itemcount++;
        }

        while (itemcount > 0) {
            Item test = Generator.random();

            if (test instanceof Bag) {
                Bag bag = (Bag) test;
                if (bag.baseWeight > baseWeight / 2) {
                    continue;
                }
            }

            collect(test);
            itemcount--;
        }

        locked(Random.Int(5) != 0);

        return this;
    }

    @Override
    public String info() {
        String retval = "This heavy chest is used to store items for safe keeping.";

        if (locked()) {
            retval += "\nIt appears to be locked.";
        }

        return retval;
    }
}
