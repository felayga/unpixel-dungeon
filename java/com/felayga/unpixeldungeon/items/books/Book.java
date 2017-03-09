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
package com.felayga.unpixeldungeon.items.books;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemRandomizationHandler;
import com.felayga.unpixeldungeon.items.books.spellbook.IdentifyBook;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Book extends Item {
    private static final Class<?>[] books = {
            IdentifyBook.class,
            BlankBook.class
    };
    private static final String[] bookRunes = {
            "dark gray", "black", "light gray", "white",
            "light red", "orange", "yellow", "yellow-green",
            "green", "sea green", "cyan", "light blue",
            "blue", "lavender", "pink", "magenta",
            "sienna", "gold", "dark sienna", "red",
            "dark red", "brown", "dark yellow", "dark yellow-green",
            "dark green", "dark sea green", "dark cyan", "dark purple",
            "dark blue", "indigo", "purple", "dark magenta",
            "plaid", "rainbow", "polkadot", "checkered",
            "striped", "bloodstained",
            //non-randomized
            "foreboding", "blank"
    };
    private static final Integer[] bookImages = {
            ItemSpriteSheet.SPELLBOOK_01, ItemSpriteSheet.SPELLBOOK_02, ItemSpriteSheet.SPELLBOOK_03, ItemSpriteSheet.SPELLBOOK_04,
            ItemSpriteSheet.SPELLBOOK_05, ItemSpriteSheet.SPELLBOOK_06, ItemSpriteSheet.SPELLBOOK_07, ItemSpriteSheet.SPELLBOOK_08,
            ItemSpriteSheet.SPELLBOOK_09, ItemSpriteSheet.SPELLBOOK_10, ItemSpriteSheet.SPELLBOOK_11, ItemSpriteSheet.SPELLBOOK_12,
            ItemSpriteSheet.SPELLBOOK_13, ItemSpriteSheet.SPELLBOOK_14, ItemSpriteSheet.SPELLBOOK_15, ItemSpriteSheet.SPELLBOOK_16,
            ItemSpriteSheet.SPELLBOOK_17, ItemSpriteSheet.SPELLBOOK_18, ItemSpriteSheet.SPELLBOOK_19, ItemSpriteSheet.SPELLBOOK_20,
            ItemSpriteSheet.SPELLBOOK_21, ItemSpriteSheet.SPELLBOOK_22, ItemSpriteSheet.SPELLBOOK_23, ItemSpriteSheet.SPELLBOOK_24,
            ItemSpriteSheet.SPELLBOOK_25, ItemSpriteSheet.SPELLBOOK_26, ItemSpriteSheet.SPELLBOOK_27, ItemSpriteSheet.SPELLBOOK_28,
            ItemSpriteSheet.SPELLBOOK_29, ItemSpriteSheet.SPELLBOOK_30, ItemSpriteSheet.SPELLBOOK_31, ItemSpriteSheet.SPELLBOOK_32,
            ItemSpriteSheet.SPELLBOOK_33, ItemSpriteSheet.SPELLBOOK_34, ItemSpriteSheet.SPELLBOOK_35, ItemSpriteSheet.SPELLBOOK_36,
            ItemSpriteSheet.SPELLBOOK_37, ItemSpriteSheet.SPELLBOOK_38,
            //non-randomized
            ItemSpriteSheet.SPELLBOOK_39, ItemSpriteSheet.SPELLBOOK_40
    };
    private static final Material[] bookMaterials = {
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper, Material.Paper, Material.Paper,
            Material.Paper, Material.Paper,
            //non-randomized
            Material.Paper, Material.Paper,
    };

    private static ItemRandomizationHandler<Book> handler;

    private String rune;

    public boolean ownedByBook = false;

    @SuppressWarnings("unchecked")
    public static void initLabels() {
        handler = new ItemRandomizationHandler<>( (Class<? extends Book>[])books, bookRunes, bookImages, bookMaterials, 2 );
    }

    public static void save( Bundle bundle ) {
        handler.save( bundle );
    }

    @SuppressWarnings("unchecked")
    public static void restore( Bundle bundle ) {
        handler = new ItemRandomizationHandler<>( (Class<? extends Book>[])books, bookRunes, bookImages, bookMaterials, bundle );
    }

    protected long readTime;

    public Book(long readTime) {
        super();

        syncRandomizedProperties();

        pickupSound = Assets.SND_ITEM_PAPER;

        weight(Encumbrance.UNIT * 50);

        this.readTime = readTime;
        stackable = true;
        defaultAction = Scroll.AC_READ;

        hasLevels(false);

        price = 15;
    }


    @Override
    public void syncRandomizedProperties() {
        image = handler.image(this);
        rune = handler.label(this);
        material = handler.material(this);
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Scroll.AC_READ);
        return actions;
    }

    @Override
    public boolean execute(Hero hero, String action ) {
        if (action.equals(Scroll.AC_READ)) {
            if (hero.buff(Blindness.class) != null) {
                GLog.w("Being blind, you're unable to read the runes in the book.");
                return false;
            } else {
                hero.curAction = new HeroAction.UseItem.SlowAction(this, Constant.Action.SLOW_ACTION, readTime);
                hero.motivate(true);
                return true;
            }
        } else if (action.equals(Constant.Action.SLOW_ACTION)) {
            prepareRead(hero);
            doRead(hero);
            return false;
        } else {
            return super.execute(hero, action);
        }
    }

    protected void prepareRead(Hero hero) {
        curUser = hero;
        curItem = this;
    }

    protected void doRead(Char user) {
    }

    public boolean isKnown() {
        return handler.isKnown( this );
    }

    public boolean setKnown() {
        if (!isKnown() && !ownedByBook) {
            handler.know( this );

            Badges.validateAllScrollsIdentified();

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
        return isKnown() ? super.getName() : rune + " book";
    }

    @Override
    public String info() {
        return isKnown() ?
                desc() :
                "This book is filled with nearly indecipherable writing. Who knows what its pages contain?";
    }

    /*
    public String initials(){
        return isKnown() ? initials : null;
    }
    */

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && isKnown();
    }

    public static HashSet<Class<? extends Book>> getKnown() {
        return handler.known();
    }

    public static HashSet<Class<? extends Book>> getUnknown() {
        return handler.unknown();
    }

    public static boolean allKnown() {
        return handler.known().size() == books.length;
    }

}
