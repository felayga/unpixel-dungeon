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
 * Created by HELLO on 2/13/2017.
 */
public enum Characteristic {
    None                (0x000000),
    Ethereal            (0x000001),
    WarmBlooded         (0x000002),
    Carnivore           (0x000004),
    Herbivore           (0x000008),
    Omnivore            (Carnivore.value + Herbivore.value),
    Humanoid            (0x000010),
    Animal              (0x000020),
    Corpseless          (0x000040),
    CannotUseItems      (0x000080),
    Swimmer             (0x000100),
    WaterBreather       (0x000200),
    NonBreather         (0x000400),
    Brainless           (0x000800),
    Amorphous           (0x001000),
    Orc                 (0x002000),
    Concealer           (0x004000),
    NoExperience        (0x008000),
    SilverVulnerable    (0x010000);

    public final int value;

    Characteristic(int value) {
        this.value = value;
    }

    public static int value(Characteristic... characteristics) {
        int retval = 0;

        for (Characteristic characteristic : characteristics) {
            retval |= characteristic.value;
        }

        return retval;
    }
}
