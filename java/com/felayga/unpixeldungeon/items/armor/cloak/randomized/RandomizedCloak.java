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

package com.felayga.unpixeldungeon.items.armor.cloak.randomized;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemRandomizationHandler;
import com.felayga.unpixeldungeon.items.armor.cloak.Cloak;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

/**
 * Created by HELLO on 2/12/2017.
 */
public class RandomizedCloak extends Cloak {
    protected static final Class<?>[] cloaks = {
            CloakOfDisplacement.class,
            CloakOfInvisibility.class,
            CloakOfMagicResistance.class,
            CloakOfProtection.class
    };

    private static final String[] cloakTypes = {
            "simple cloak", "opera cloak", "fancy cloak", "tattered cape"
    };
    private static final Integer[] cloakImages = {
            ItemSpriteSheet.CLOAK_PLAIN, ItemSpriteSheet.CLOAK_OPERA, ItemSpriteSheet.CLOAK_FANCY, ItemSpriteSheet.CLOAK_TATTERED
    };
    private static final Material[] cloakMaterials = {
            Material.Cloth, Material.Cloth, Material.Cloth, Material.Cloth
    };

    protected static ItemRandomizationHandler<RandomizedCloak> handler;

    protected String cloakType;

    @SuppressWarnings("unchecked")
    public static void initNames() {
        handler = new ItemRandomizationHandler<>((Class<? extends RandomizedCloak>[]) cloaks, cloakTypes, cloakImages, cloakMaterials, 0);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    @SuppressWarnings("unchecked")
    public static void restore(Bundle bundle) {
        handler = new ItemRandomizationHandler<>((Class<? extends RandomizedCloak>[]) cloaks, cloakTypes, cloakImages, cloakMaterials, bundle);
    }


    public RandomizedCloak(int armor, int armorMagic, long speedModifier) {
        super(armor, armorMagic, speedModifier);
        syncRandomizedProperties();
    }

    @Override
    public void syncRandomizedProperties() {
        image = handler.image(this);
        cloakType = handler.label(this);
        material = handler.material(this);
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    public boolean setKnown() {
        if (!isKnown()) {
            handler.know(this);

            return true;
        }

        return false;
    }

    @Override
    public Item identify(boolean updateQuickslot) {
        if (setKnown()) {
            updateQuickslot = true;
        }

        return super.identify(updateQuickslot);
    }

    @Override
    public String getName() {
        return isKnown() ? super.getName() : cloakType;
    }

    @Override
    public String info() {
        if (isKnown()) {
            return desc();
        }

        return "This is a " + cloakType+ ". " +
                "Who knows what effects it will have when worn?";
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && isKnown();
    }
}
