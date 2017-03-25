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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemRandomizationHandler;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by HELLO on 2/15/2017.
 */
public class Amulet extends EquippableItem {
    protected static final Class<?>[] amulets = {

            AmuletOfYendor.class, AmuletOfYendorFake.class
    };

    private static final String[] amuletTypes = {
            "circular amulet", "triangular amulet", "oval amulet", "square amulet",
            "jeweled amulet", "pyramidal amulet", "mithril amulet", "concave amulet",
            "spherical amulet",

            "Amulet of Yendor", "Amulet of Yendor"
    };
    private static final Integer[] amuletImages = {
            ItemSpriteSheet.AMULET_CIRCULAR, ItemSpriteSheet.AMULET_DELTA, ItemSpriteSheet.AMULET_OVAL, ItemSpriteSheet.AMULET_SQUARE,
            ItemSpriteSheet.AMULET_OCTAGONAL, ItemSpriteSheet.AMULET_PYRAMIDAL, ItemSpriteSheet.AMULET_MITHRIL, ItemSpriteSheet.AMULET_CONCAVE,
            ItemSpriteSheet.AMULET_SPHERICAL,

            ItemSpriteSheet.AMULET_YENDOR, ItemSpriteSheet.AMULET_YENDOR_FAKE
    };
    private static final Material[] amuletMaterials = {
            Material.Gemstone, Material.Iron, Material.Copper, Material.Bone,
            Material.Silver, Material.Iron, Material.Mithril, Material.Silver,
            Material.Gold,

            Material.Mithril, Material.Plastic
    };

    protected static ItemRandomizationHandler<Amulet> handler;

    protected String amuletType;

    @SuppressWarnings("unchecked")
    public static void initNames() {
        handler = new ItemRandomizationHandler<>((Class<? extends Amulet>[]) amulets, amuletTypes, amuletImages, amuletMaterials, 2);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    @SuppressWarnings("unchecked")
    public static void restore(Bundle bundle) {
        handler = new ItemRandomizationHandler<>((Class<? extends Amulet>[]) amulets, amuletTypes, amuletImages, amuletMaterials, bundle);
    }


    public Amulet() {
        super(GameTime.TICK);
        syncRandomizedProperties();

        hasLevels(false);

        weight(Encumbrance.UNIT * 20);
        price = 75;
    }

    @Override
    public void syncRandomizedProperties() {
        image = handler.image(this);
        amuletType = handler.label(this);
        material = handler.material(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(isEquipped(hero) ? Constant.Action.UNEQUIP : Constant.Action.EQUIP);
        return actions;
    }

    @Override
    public Slot[] getSlots() {
        return new Slot[]{Slot.Amulet};
    }

    @Override
    public void onEquip(Char owner, boolean cursed) {
        if (cursed) {
            if (owner == Dungeon.hero) {
                GLog.n("your " + this.getDisplayName() + " tightens around your neck painfully");
            } else {
                //todo: amulet cursed in enemy hands description
            }
        }
    }

    @Override
    public void onUnequip(Char owner) {
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
        return isKnown() ? super.getName() : amuletType;
    }

    @Override
    public String info() {
        if (isKnown()) {
            return desc();
        }

        return "This is a " + amuletType + ". " +
                "Who knows what effects it will have when worn?";
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && isKnown();
    }
}
