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

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.ui.Icons;

import java.util.AbstractList;
import java.util.Iterator;

/**
 * Created by HELLO on 3/29/2016.
 */
public interface IBag {
    Item self();
    String action();
    String getDisplayName();
    Icons tabIcon();

    int size();
    int pos();

    void onWeightChanged(int change);

    boolean tryMergeStack(Item test);

    boolean collect(Item collectItem);
    Item remove(Item item);
    Item remove(Item item, int quantity);

    boolean contains(Item item);

    boolean locked();

    Item randomItem();

    Iterator<Item> iterator(boolean allowNested);
}
