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
package com.felayga.unpixeldungeon.items.books;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.actors.buffs.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemStatusHandler;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Book extends Item {

    protected static final float TIME_TO_READ	= 1f;

    private static final Class<?>[] books = {

    };
    private static final String[] runes =
            {
                    "dark gray",
                    "black",
                    "light gray",
                    "white",
                    "light red",
                    "orange",
                    "yellow",
                    "yellow-green",
                    "green",
                    "sea green",
                    "cyan",
                    "light blue",
                    "blue",
                    "lavender",
                    "pink",
                    "magenta",
                    "sienna",
                    "gold",
                    "dark sienna",
                    "red",
                    "dark red",
                    "brown",
                    "dark yellow",
                    "dark yellow-green",
                    "dark green",
                    "dark sea green",
                    "dark cyan",
                    "dark purple",
                    "dark blue",
                    "indigo",
                    "purple",
                    "dark magenta",
                    "plaid",
                    "rainbow",
                    "polkadot",
                    "checkered",
                    "striped",
                    "bloodstained",
                    "foreboding",
                    "blank"
            };
    private static final Integer[] images = {
            ItemSpriteSheet.SPELLBOOK_01,
            ItemSpriteSheet.SPELLBOOK_02,
            ItemSpriteSheet.SPELLBOOK_03,
            ItemSpriteSheet.SPELLBOOK_04,
            ItemSpriteSheet.SPELLBOOK_05,
            ItemSpriteSheet.SPELLBOOK_06,
            ItemSpriteSheet.SPELLBOOK_07,
            ItemSpriteSheet.SPELLBOOK_08,
            ItemSpriteSheet.SPELLBOOK_09,
            ItemSpriteSheet.SPELLBOOK_10,
            ItemSpriteSheet.SPELLBOOK_11,
            ItemSpriteSheet.SPELLBOOK_12,
            ItemSpriteSheet.SPELLBOOK_13,
            ItemSpriteSheet.SPELLBOOK_14,
            ItemSpriteSheet.SPELLBOOK_15,
            ItemSpriteSheet.SPELLBOOK_16,
            ItemSpriteSheet.SPELLBOOK_17,
            ItemSpriteSheet.SPELLBOOK_18,
            ItemSpriteSheet.SPELLBOOK_19,
            ItemSpriteSheet.SPELLBOOK_20,
            ItemSpriteSheet.SPELLBOOK_21,
            ItemSpriteSheet.SPELLBOOK_22,
            ItemSpriteSheet.SPELLBOOK_23,
            ItemSpriteSheet.SPELLBOOK_24,
            ItemSpriteSheet.SPELLBOOK_25,
            ItemSpriteSheet.SPELLBOOK_26,
            ItemSpriteSheet.SPELLBOOK_27,
            ItemSpriteSheet.SPELLBOOK_28,
            ItemSpriteSheet.SPELLBOOK_29,
            ItemSpriteSheet.SPELLBOOK_30,
            ItemSpriteSheet.SPELLBOOK_31,
            ItemSpriteSheet.SPELLBOOK_32,
            ItemSpriteSheet.SPELLBOOK_33,
            ItemSpriteSheet.SPELLBOOK_34,
            ItemSpriteSheet.SPELLBOOK_35,
            ItemSpriteSheet.SPELLBOOK_36,
            ItemSpriteSheet.SPELLBOOK_37,
            ItemSpriteSheet.SPELLBOOK_38,
            ItemSpriteSheet.SPELLBOOK_39,
            ItemSpriteSheet.SPELLBOOK_40
    };

    private static ItemStatusHandler<Book> handler;

    private String rune;

    public boolean ownedByBook = false;

    @SuppressWarnings("unchecked")
    public static void initLabels() {
        handler = new ItemStatusHandler<Book>( (Class<? extends Book>[])books, runes, images, 2 );
    }

    public static void save( Bundle bundle ) {
        handler.save( bundle );
    }

    @SuppressWarnings("unchecked")
    public static void restore( Bundle bundle ) {
        handler = new ItemStatusHandler<Book>( (Class<? extends Book>[])books, runes, images, bundle );
    }

    public Book() {
        super();

        weight(Encumbrance.UNIT * 50);

        stackable = true;
        defaultAction = Scroll.AC_READ;

        price = 15;

        syncVisuals();
    }

    @Override
    public void syncVisuals(){
        image = handler.image( this );
        rune = handler.label( this );
    };

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Scroll.AC_READ);
        return actions;
    }

    @Override
    public boolean execute( Hero hero, String action ) {
        if (action.equals( Scroll.AC_READ )) {
            if (hero.buff( Blindness.class ) != null) {
                GLog.w( "Being blind, you're unable to read the runes in the book." );
            } else {
                prepareRead(hero);
                doRead();
            }

            return false;
        } else {
            return super.execute( hero, action );
        }
    }

    protected void prepareRead(Hero hero) {
        curUser = hero;
        curItem = hero.belongings.remove(this, 1);
    }

    abstract protected void doRead();

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
        return isKnown() ? super.getName() : "scroll \"" + rune + "\"";
    }

    @Override
    public String info() {
        return isKnown() ?
                desc() :
                "This book is filled with indecipherable writing. Who knows what its pages contain?";
    }

    /*
    public String initials(){
        return isKnown() ? initials : null;
    }
    */

    @Override
    public void playPickupSound() {
        Sample.INSTANCE.play( Assets.SND_ITEM_PAPER );
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

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
