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

package com.felayga.unpixeldungeon.sprites.mobs.rat;

import com.watabou.noosa.TextureFilm;

import java.util.Calendar;

/**
 * Created by HELLO on 3/22/2017.
 */

public class RatKingSprite extends GiantRatSprite {
    public boolean festive;

    private static int spriteIndex() {
        final Calendar calendar = Calendar.getInstance();
        //once a year the rat king feels a bit festive!
        if ((calendar.get(Calendar.MONTH) == 11 && calendar.get(Calendar.WEEK_OF_MONTH) > 2)) {
            return 6;
        }
        return 5;
    }

    public RatKingSprite() {
        super(spriteIndex());
    }

    @Override
    protected void isFestive(int index) {
        festive = index == 6;
    }
}
