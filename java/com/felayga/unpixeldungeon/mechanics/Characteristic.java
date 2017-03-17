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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HELLO on 2/13/2017.
 */
public enum Characteristic {
    None                ("None", 0x000000),
    Ethereal            ("Ethereal", 0x000001),
    WarmBlooded         ("Warm-blooded", 0x000002),
    Carnivore           ("Carnivorous", 0x000004),
    Herbivore           ("Herbivorous", 0x000008),
    Omnivore            ("Omnivorous", Carnivore.value + Herbivore.value),
    Humanoid            ("Humanoid", 0x000010),
    Animal              ("Animal", 0x000020),
    Corpseless          ("Leaves no corpse", 0x000040),
    CannotUseItems      ("Cannot use items", 0x000080),
    Swimmer             ("Swimmer", 0x000100),
    WaterBreather       ("Water breather", 0x000200),
    NonBreather         ("Doesn't breathe", 0x000400),
    Brainless           ("Brainless", 0x000800),
    Amorphous           ("Amorphous", 0x001000),
    Orc                 ("Orc", 0x002000),
    Concealer           ("Hides under items", 0x004000),
    NoExperience        ("Not worth fighting", 0x008000),
    SilverVulnerable    ("Silver-vulnerable", 0x010000),
    LOSBlocking         (null, 0x020000);

    public final String name;
    public final int value;

    Characteristic(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static int value(Characteristic... characteristics) {
        int retval = 0;

        for (Characteristic characteristic : characteristics) {
            retval |= characteristic.value;
        }

        return retval;
    }

    public static final int MAXVALUE = 19;

    public static Characteristic fromInt(int value) {
        switch (value) {
            case 0:
                return Ethereal;
            case 1:
                return WarmBlooded;
            case 2:
                return Omnivore;
            case 3:
                return Carnivore;
            case 4:
                return Herbivore;
            case 5:
                return Humanoid;
            case 6:
                return Animal;
            case 7:
                return Corpseless;
            case 8:
                return CannotUseItems;
            case 9:
                return Swimmer;
            case 10:
                return WaterBreather;
            case 11:
                return NonBreather;
            case 12:
                return Brainless;
            case 13:
                return Amorphous;
            case 14:
                return Orc;
            case 15:
                return Concealer;
            case 16:
                return NoExperience;
            case 17:
                return SilverVulnerable;
            case 18:
                return LOSBlocking;
            default:
                return None;
        }
    }

    public static List<Characteristic> toList(long flags) {
        List<Characteristic> retval = new ArrayList<>();

        for (int n = 0; n < MAXVALUE; n++) {
            Characteristic type = fromInt(n);
            if ((flags & type.value) == type.value) {
                retval.add(type);
                flags ^= type.value;
            }
        }

        if (retval.size() < 1) {
            retval.add(None);
        }

        return retval;
    }
}
