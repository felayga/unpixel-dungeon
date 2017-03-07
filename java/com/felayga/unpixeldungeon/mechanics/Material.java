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

package com.felayga.unpixeldungeon.mechanics;

/**
 * Created by HELLO on 3/1/2017.
 */
public enum Material {
    None(-1, "none", false, false, false, false, false, false, false),
    Wax(0, "wax", true, false, true, false, false, false, false),
    Vegetable(1, "vegetable", true, false, true, false, false, true, false),
    Flesh(2, "flesh", true, false, true, false, false, true, false),
    Paper(3, "paper", true, false, true, false, false, true, false),
    Cloth(4, "cloth", true, false, true, false, false, true, false),
    Leather(5, "leather", true, false, true, false, false, false, false),
    Wood(6, "wood", false, false, true, false, false, true, false),
    Bone(7, "bone", false, false, false, false, false, false, false),
    DragonHide(8, "dragon hide", false, false, false, false, false, false, false),
    Iron(9, "iron", false, true, false, true, true, false, false),
    Metal(10, "metal", false, true, false, false, false, false, false),
    Copper(11, "copper", false, true, false, false, true, false, false),
    Silver(12, "silver", false, true, false, false, false, false, false),
    Gold(13, "gold", false, true, false, false, false, false, false),
    Platinum(14, "platinum", false, true, false, false, false, false, false),
    Mithril(15, "mithril", false, true, false, false, false, false, false),
    Plastic(16, "plastic", false, false, false, false, false, true, false),
    Glass(17, "glass", false, false, false, false, false, false, true),
    Gemstone(18, "gemstone", false, false, false, false, false, false, false),
    GemstoneFake(19, "fake gemstone", false, false, false, false, false, false, false),
    Mineral(20, "mineral", false, false, false, false, false, false, false);

    public final int value;
    public final String name;
    public final boolean flimsy;
    public final boolean metallic;
    public final boolean organic;
    public final boolean rustable;
    public final boolean corrodable;
    public final boolean flammable;
    public final boolean fragile;

    Material(int value, String name, boolean flimsy, boolean metallic, boolean organic, boolean rustable, boolean corrodable, boolean flammable, boolean fragile) {
        this.value = value;
        this.name = name;
        this.flimsy = flimsy;
        this.metallic = metallic;
        this.organic = organic;
        this.rustable = rustable;
        this.corrodable = corrodable;
        this.flammable = flammable;
        this.fragile = fragile;
    }

    public static Material fromInt(int value)
    {
        switch(value)
        {
            case 0:
                return Wax;
            case 1:
                return Vegetable;
            case 2:
                return Flesh;
            case 3:
                return Paper;
            case 4:
                return Cloth;
            case 5:
                return Leather;
            case 6:
                return Wood;
            case 7:
                 return Bone;
            case 8:
                return DragonHide;
            case 9:
                return Iron;
            case 10:
                return Metal;
            case 11:
                return Copper;
            case 12:
                return Silver;
            case 13:
                return Gold;
            case 14:
                return Platinum;
            case 15:
                return Mithril;
            case 16:
                return Plastic;
            case 17:
                return Glass;
            case 18:
                return Gemstone;
            case 19:
                return GemstoneFake;
            case 20:
                return Mineral;
            default:
                return None;
        }
    }

}
