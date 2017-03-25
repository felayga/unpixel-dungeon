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

package com.felayga.unpixeldungeon.items.equippableitem.amulet;

import com.felayga.unpixeldungeon.sprites.ItemSprite;

/**
 * Created by HELLO on 3/20/2017.
 */

public class AmuletOfYendorFake extends Amulet {
    public AmuletOfYendorFake() {
        super();

        name = "Cheap Plastic Amulet";

        price = 0;
    }

    protected static ItemSprite.Glowing MITHRIL = new ItemSprite.Glowing(0xd5dcd7);

    @Override
    public ItemSprite.Glowing glowing() {
        if (isKnown()) {
            return null;
        } else {
            return MITHRIL;
        }
    }

    @Override
    public String info() {
        if (isKnown()) {
            return "The Amulet of Yendor is the most powerful known artifact of unknown origin.  This, however, is a cheap " +
                    "plastic imitation of that artifact, and is completely worthless.";
        } else {
            return desc();
        }
    }

    public String desc() {
        return "The Amulet of Yendor is the most powerful known artifact of unknown origin. It is said that the amulet " +
                "is able to fulfil any wish if its owner's will-power is strong enough to \"persuade\" it to do it.";
    }
}
