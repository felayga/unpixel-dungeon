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
    public String color;

    public Gemstone(GemstoneColor... choices) {
        super();

        int index;

        if (choices.length > 1) {
            index = Random.Int(choices.length);
        } else {
            index = 0;
        }

        setColor(choices[index]);
        stackable = true;

        weight = Encumbrance.UNIT;
    }

    protected void buildName()
    {
        name = "bad gemstone";
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(COLOR, color);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        setColor(GemstoneColor.fromString(bundle.getString(COLOR)));
    }

    @Override
    public String getName() {
        return levelKnown ? super.getName() : color + " gem";
    }

    @Override
    public String info() {
        return levelKnown ?
                desc() :
                "This is a " + color + "-colored gemstone.  Who knows what it is?";
    }


    protected enum GemstoneColor
    {
        White,
        Red,
        Orange,
        Brown,
        Yellow,
        Green,
        Blue,
        Violet,
        Black,
        Gray;

        public static GemstoneColor fromString(String color)
        {
            switch(color) {
                case "white":
                    return GemstoneColor.White;
                case "red":
                    return GemstoneColor.Red;
                case "orange":
                    return GemstoneColor.Orange;
                case "brown":
                    return GemstoneColor.Brown;
                case "yellow":
                    return GemstoneColor.Yellow;
                case "green":
                    return GemstoneColor.Green;
                case "blue":
                    return GemstoneColor.Blue;
                case "violet":
                    return GemstoneColor.Violet;
                case "black":
                    return GemstoneColor.Black;
                default:
                    return GemstoneColor.Gray;
            }
        }
    }

    private void setColor(GemstoneColor choice)
    {
        switch(choice)
        {
            case White:
                color = "white";
                image = ItemSpriteSheet.GEMSTONE_WHITE;
                break;
            case Red:
                color = "red";
                image = ItemSpriteSheet.GEMSTONE_RED;
                break;
            case Orange:
                color = "orange";
                image = ItemSpriteSheet.GEMSTONE_ORANGE;
                break;
            case Brown:
                color = "brown";
                image = ItemSpriteSheet.GEMSTONE_BROWN;
                break;
            case Yellow:
                color = "yellow";
                image = ItemSpriteSheet.GEMSTONE_YELLOW;
                break;
            case Green:
                color = "green";
                image = ItemSpriteSheet.GEMSTONE_GREEN;
                break;
            case Blue:
                color = "blue";
                image = ItemSpriteSheet.GEMSTONE_BLUE;
                break;
            case Violet:
                color = "violet";
                image = ItemSpriteSheet.GEMSTONE_VIOLET;
                break;
            case Black:
                color = "black";
                image = ItemSpriteSheet.GEMSTONE_BLACK;
                break;
            case Gray:
                color = "gray";
                image = ItemSpriteSheet.GEMSTONE_GRAY;
                break;
        }

        buildName();
    }

    @Override
    public boolean isSimilar(Item item)
    {
        if (!super.isSimilar(item)) {
            return false;
        }

        if (item.getClass() == this.getClass())
        {
            Gemstone gemstone = (Gemstone)item;

            return color.equals(gemstone.color);
        }

        return false;
    }
}
