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

import com.felayga.unpixeldungeon.items.Item;

import java.util.AbstractList;
import java.util.Iterator;

/**
 * Created by HELLO on 5/26/2016.
 */
public class ItemIterator implements Iterator<Item> {
    private int index = 0;
    private IBag host;
    private AbstractList<Item> items;
    private Iterator<Item> nested = null;
    private boolean allowNested;

    public ItemIterator(IBag host, AbstractList<Item> items, boolean allowNested) {
        this.host = host;
        this.items = items;
        this.allowNested = allowNested;
    }

    @Override
    public boolean hasNext() {
        if (nested != null) {
            return nested.hasNext() || index < items.size();
        } else {
            return items != null && index < items.size();
        }
    }

    @Override
    public Item next() {
        if (nested != null && nested.hasNext()) {
            return nested.next();
        } else {
            nested = null;

            Item item = items.get(index++);

            if (allowNested && item instanceof IBag) {
                nested = ((IBag) item).iterator(true);
            }

            return item;
        }
    }

    @Override
    public void remove() {
        if (nested != null) {
            nested.remove();
        } else {
            index--;
            host.remove(items.get(index));
        }
    }
}
