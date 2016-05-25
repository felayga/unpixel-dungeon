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

package com.felayga.unpixeldungeon.items.gemstones;

import com.felayga.unpixeldungeon.actors.buffs.Encumbrance;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/16/2016.
 */
public class Gemstone extends Item {
    public static final String COLOR = "color";
    public GemstoneColor color;
    public String colorName;
    public boolean hardness;

    public Gemstone(int price, boolean isHard, GemstoneColor... choices) {
        super();

        this.price = price;
        this.hardness = isHard;

        int index;

        if (choices.length > 1) {
            index = Random.Int(choices.length);
        } else {
            index = 0;
        }

        setColor(choices[index]);
        stackable = true;

        weight(Encumbrance.UNIT);
    }

    protected void buildName() {
        name = "bad gemstone";
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COLOR, color.value);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        setColor(GemstoneColor.fromInt(bundle.getInt(COLOR)));
    }

    @Override
    public String getName() {
        return levelKnown ? super.getName() : colorName + " gem";
    }

    @Override
    public String info() {
        return levelKnown ?
                desc() :
                "This is a " + colorName + "-colored gemstone.  Who knows what it is?";
    }


    protected enum GemstoneColor {
        White(0),
        Red(1),
        Orange(2),
        Brown(3),
        Yellow(4),
        Green(5),
        Blue(6),
        Violet(7),
        Black(8),
        Gray(9);

        public final int value;

        GemstoneColor(int value) {
            this.value = value;
        }

        public static GemstoneColor fromInt(int index) {
            switch (index) {
                case 0:
                    return GemstoneColor.White;
                case 1:
                    return GemstoneColor.Red;
                case 2:
                    return GemstoneColor.Orange;
                case 3:
                    return GemstoneColor.Brown;
                case 4:
                    return GemstoneColor.Yellow;
                case 5:
                    return GemstoneColor.Green;
                case 6:
                    return GemstoneColor.Blue;
                case 7:
                    return GemstoneColor.Violet;
                case 8:
                    return GemstoneColor.Black;
                default:
                    return GemstoneColor.Gray;
            }
        }

        public static String getName(GemstoneColor color) {
            switch (color) {
                case White:
                    return "white";
                case Red:
                    return "red";
                case Orange:
                    return "orange";
                case Brown:
                    return "brown";
                case Yellow:
                    return "yellow";
                case Green:
                    return "green";
                case Blue:
                    return "blue";
                case Violet:
                    return "violet";
                case Black:
                    return "black";
                default:
                    return "gray";
            }
        }

        public static int getItemSpriteSheetIndex(GemstoneColor color) {
            switch (color) {
                case White:
                    return ItemSpriteSheet.GEMSTONE_WHITE;
                case Red:
                    return ItemSpriteSheet.GEMSTONE_RED;
                case Orange:
                    return ItemSpriteSheet.GEMSTONE_ORANGE;
                case Brown:
                    return ItemSpriteSheet.GEMSTONE_BROWN;
                case Yellow:
                    return ItemSpriteSheet.GEMSTONE_YELLOW;
                case Green:
                    return ItemSpriteSheet.GEMSTONE_GREEN;
                case Blue:
                    return ItemSpriteSheet.GEMSTONE_BLUE;
                case Violet:
                    return ItemSpriteSheet.GEMSTONE_VIOLET;
                case Black:
                    return ItemSpriteSheet.GEMSTONE_BLACK;
                default:
                    return ItemSpriteSheet.GEMSTONE_GRAY;
            }
        }
    }

    private void setColor(GemstoneColor choice) {
        color = choice;
        colorName = GemstoneColor.getName(choice);
        image = GemstoneColor.getItemSpriteSheetIndex(choice);

        buildName();
    }

    @Override
    public boolean isSimilar(Item item) {
        if (!super.isSimilar(item)) {
            return false;
        }

        if (item.getClass() == this.getClass()) {
            Gemstone gemstone = (Gemstone) item;

            return color.equals(gemstone.color);
        }

        return false;
    }


    public class GemstoneBlackOpal extends Gemstone {
        public GemstoneBlackOpal() {
            super(2500, true, GemstoneColor.Black);
        }

        @Override
        protected void buildName() {
            name = "black opal";
        }
    }

    public class GemstoneJetStone extends Gemstone {
        public GemstoneJetStone() {
            super(850, false, GemstoneColor.Black);
        }

        @Override
        protected void buildName() {
            name = "jet stone";
        }
    }

    public class GemstoneObsidian extends Gemstone {
        public GemstoneObsidian() {
            super(200, false, GemstoneColor.Black);
        }

        @Override
        protected void buildName() {
            name = "obsidian stone";
        }
    }
}
