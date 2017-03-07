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
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class ItemRandomizationHandler<T extends Item> {

    private Class<? extends T>[] items;

    private HashMap<Class<? extends T>, Integer> images;
    private HashMap<Class<? extends T>, String> labels;
    private HashMap<Class<? extends T>, Material> materials;

    private HashSet<Class<? extends T>> known;

    public ItemRandomizationHandler(Class<? extends T>[] items, String[] allLabels, Integer[] allImages, Material[] allMaterials, int endlistnonrandomcount) {
        this.items = items;

        this.images = new HashMap<>();
        this.labels = new HashMap<>();
        this.materials = new HashMap<>();

        known = new HashSet<>();

        ArrayList<String> labelsLeft = new ArrayList<>(Arrays.asList(allLabels));
        ArrayList<Integer> imagesLeft = new ArrayList<>(Arrays.asList(allImages));
        ArrayList<Material> materialsLeft = new ArrayList<>(Arrays.asList(allMaterials));

        int index;
        for (int i = 0; i < items.length - endlistnonrandomcount; i++) {

            Class<? extends T> item = items[i];

            index = Random.Int(labelsLeft.size() - endlistnonrandomcount);

            labels.put(item, labelsLeft.get(index));
            labelsLeft.remove(index);

            images.put(item, imagesLeft.get(index));
            imagesLeft.remove(index);

            materials.put(item, materialsLeft.get(index));
            materialsLeft.remove(index);
        }

        index = labelsLeft.size() - 1;
        for (int i = items.length - 1; i >= items.length - endlistnonrandomcount; i--) {
            Class<? extends T> item = items[i];

            labels.put(item, labelsLeft.get(index));
            labelsLeft.remove(index);

            images.put(item, imagesLeft.get(index));
            imagesLeft.remove(index);

            materials.put(item, materialsLeft.get(index));
            materialsLeft.remove(index);

            index--;
        }
    }

    public ItemRandomizationHandler(Class<? extends T>[] items, String[] labels, Integer[] images, Material[] materials, Bundle bundle) {

        this.items = items;

        this.images = new HashMap<>();
        this.labels = new HashMap<>();
        this.materials = new HashMap<>();
        known = new HashSet<>();

        restore(bundle, labels, images, materials);
    }

    private static final String SUFFIX_IMAGE = "_image";
    private static final String SUFFIX_LABEL = "_label";
    private static final String SUFFIX_KNOWN = "_known";
    private static final String SUFFIX_MATERIAL = "_material";

    public void save(Bundle bundle) {
        for (int i = 0; i < items.length; i++) {
            String itemName = items[i].toString();
            bundle.put(itemName + SUFFIX_IMAGE, images.get(items[i]));
            bundle.put(itemName + SUFFIX_LABEL, labels.get(items[i]));
            bundle.put(itemName + SUFFIX_KNOWN, known.contains(items[i]));
            bundle.put(itemName + SUFFIX_MATERIAL, materials.get(items[i]).value);
        }
    }

    private void restore(Bundle bundle, String[] allLabels, Integer[] allImages, Material[] allMaterials) {

        ArrayList<String> labelsLeft = new ArrayList<>(Arrays.asList(allLabels));
        ArrayList<Integer> imagesLeft = new ArrayList<>(Arrays.asList(allImages));
        ArrayList<Material> materialsLeft = new ArrayList<>(Arrays.asList(allMaterials));

        for (int i = 0; i < items.length; i++) {

            Class<? extends T> item = items[i];
            String itemName = item.toString();

            if (bundle.contains(itemName + SUFFIX_LABEL) && Dungeon.version > 4) {

                String label = bundle.getString(itemName + SUFFIX_LABEL);
                labels.put(item, label);
                labelsLeft.remove(label);

                Integer image = bundle.getInt(itemName + SUFFIX_IMAGE);
                images.put(item, image);
                imagesLeft.remove(image);

                Material material = Material.fromInt(bundle.getInt(itemName + SUFFIX_MATERIAL));
                materials.put(item, material);
                materialsLeft.remove(material);

                if (bundle.getBoolean(itemName + SUFFIX_KNOWN)) {
                    known.add(item);
                }

                //if there's a new item, give it a random image
                //or.. if we're loading from an untrusted version, randomize the image to be safe.
            } else {

                int index = Random.Int(labelsLeft.size());

                labels.put(item, labelsLeft.get(index));
                labelsLeft.remove(index);

                images.put(item, imagesLeft.get(index));
                imagesLeft.remove(index);

                if (bundle.contains(itemName + SUFFIX_KNOWN) && bundle.getBoolean(itemName + SUFFIX_KNOWN)) {
                    known.add(item);
                }
            }
        }
    }

    public int image(T item) {
        return image(item.getClass());
    }

    public int image(Class itemClass) {
        try {
            return images.get(itemClass);
        } catch(Exception e) {
            GLog.d("random gen failed on itemClass=" + itemClass.toString());
            GLog.d(e);
            throw e;
        }
    }

    public String label(T item) {
        return label(item.getClass());
    }

    public String label(Class itemClass) {
        return labels.get(itemClass);
    }

    public Material material(T item) {
        return material(item.getClass());
    }

    public Material material(Class itemClass) {
        return materials.get(itemClass);
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

