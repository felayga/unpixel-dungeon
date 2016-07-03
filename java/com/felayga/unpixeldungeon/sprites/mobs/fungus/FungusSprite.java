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
package com.felayga.unpixeldungeon.sprites;

import com.felayga.unpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public abstract class FungusSprite extends MobSprite {
    private static int FRAMES_PER_ROW = 8;

    public FungusSprite(int index) {
        super();

        texture( Assets.Mobs.FUNGUS);

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        int offset = index * FRAMES_PER_ROW;

        idle = new Animation( 1, true );
        idle.frames( frames, 0 + offset );

        run = new Animation( 1, true );
        run.frames( frames, 0 + offset );

        attack = new Animation( 5, false );
        attack.frames( frames, 1 + offset, 2 + offset );

        die = new Animation( 20, false );
        die.frames( frames, 3 + offset, 4 + offset, 5 + offset );

        play( idle );
    }
}
