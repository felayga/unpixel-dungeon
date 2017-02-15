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
package com.felayga.unpixeldungeon.sprites.mobs.goo;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class GooSprite extends MobSprite {
    private static int FRAMES_PER_ROW = 12;

    public GooSprite(int index) {
        super();

        texture( Assets.Mobs.GOO );

        TextureFilm frames = new TextureFilm( texture, 20, 14 );

        int offset = index * FRAMES_PER_ROW;

        initAnimations(frames, offset);

        play(idle);
    }

    protected void initAnimations(TextureFilm frames, int offset) {
        idle = new Animation( 10, true );
        idle.frames( frames, offset + 2, offset + 1, offset + 0, offset + 0, offset + 1 );

        run = new Animation( 15, true );
        run.frames( frames, offset + 3, offset + 2, offset + 1, offset + 2 );

        attack = new Animation( 10, false );
        attack.frames( frames, offset + 8, offset + 9, offset + 10 );

        die = new Animation( 10, false );
        die.frames( frames, offset + 5, offset + 6, offset + 7 );
    }

}
