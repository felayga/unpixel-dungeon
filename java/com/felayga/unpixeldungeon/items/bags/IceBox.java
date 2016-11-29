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

package com.felayga.unpixeldungeon.items.bags;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.food.Corpse;
import com.felayga.unpixeldungeon.mechanics.Roll;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.Icons;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 7/6/2016.
 */
public class IceBox extends LargeChest {
    public IceBox() {
        super(null, false);

        name = "ice box";
        image = ItemSpriteSheet.ICEBOX;
        tabIcon = Icons.ICEBOX;

        size = 36;
        priority = 8;
        price = 42;
        weight(Encumbrance.UNIT * 900);
    }

    @Override
    public Item random() {
        float dropBonusChance = Roll.DropBonusChance(Dungeon.hero) / 2.0f;

        int min = (int)Math.floor(dropBonusChance * 12.0f + 2.0f);
        int max = (int)Math.floor(dropBonusChance * 16.0f + 8.0f);

        int count = Random.IntRange(min, max);
        int tries = count * 2;

        while (count > 0 && tries > 0) {
            Mob mob = Bestiary.spawn(Dungeon.depthAdjusted, Dungeon.hero.level + 10);

            if (mob.nutrition > 0) {
                collect(new Corpse(mob));
                count--;
            }
            else {
                tries--;
            }
        }

        return this;
    }

    @Override
    public boolean decay(long currentTime, boolean updateTime, boolean fixTime) {
        return super.decay(currentTime, false, false);
    }

    @Override
    public String info() {
        return "This heavy chest is used to store items for safe keeping.";
    }
}
