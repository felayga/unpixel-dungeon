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

package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by HELLO on 2/9/2017.
 */
public class ItemStatusHandler<T extends Item> {

    private Class<? extends T>[] items;

    private HashSet<Class<? extends T>> known;

    public ItemStatusHandler(Class<? extends T>[] items) {

        this.items = items;

        known = new HashSet<Class<? extends T>>();
    }

    public ItemStatusHandler(Class<? extends T>[] items, Bundle bundle) {

        this.items = items;

        known = new HashSet<Class<? extends T>>();

        restore(bundle);
    }

    private static final String PFX_KNOWN = "_known";

    public void save(Bundle bundle) {
        for (int i = 0; i < items.length; i++) {
            String itemName = items[i].toString();
            bundle.put(itemName + PFX_KNOWN, known.contains(items[i]));
        }
    }

    private void restore(Bundle bundle) {

        for (int i = 0; i < items.length; i++) {

            Class<? extends T> item = items[i];
            String itemName = item.toString();

            if (bundle.getBoolean(itemName + PFX_KNOWN)) {
                known.add(item);
            }
        }
    }

    public boolean isKnown(T item) {
        return known.contains(item.getClass());
    }

    public void know(T item) {
        know(item.getClass());
    }

    @SuppressWarnings("unchecked")
    public void know(Class itemClass) {
        known.add(itemClass);

        if (known.size() == items.length - 1) {
            for (int i = 0; i < items.length; i++) {
                if (!known.contains(items[i])) {
                    known.add(items[i]);
                    break;
                }
            }
        }
    }


    public HashSet<Class<? extends T>> known() {
        return known;
    }

    public HashSet<Class<? extends T>> unknown() {
        HashSet<Class<? extends T>> result = new HashSet<Class<? extends T>>();
        for (Class<? extends T> i : items) {
            if (!known.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }
}


