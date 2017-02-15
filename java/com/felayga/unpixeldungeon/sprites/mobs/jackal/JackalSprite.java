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
package com.felayga.unpixeldungeon.sprites.mobs.jackal;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class JackalSprite extends MobSprite {
    private static int FRAMES_PER_ROW = 16;

    public JackalSprite() {
        this(0);
    }

    public JackalSprite(int index) {
        super();

        texture( Assets.Mobs.JACKAL );
        int offset = index * FRAMES_PER_ROW;

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        initAnimationIdle(frames, offset);

        run = new Animation( 12, true );
        run.frames( frames, offset + 6, offset + 7, offset + 8, offset + 9 );

        attack = new Animation( 15, false );
        attack.frames( frames, offset + 3, offset + 4, offset + 5, offset + 4, offset + 5 );

        die = new Animation( 12, false );
        die.frames( frames, offset + 10, offset + 11, offset + 12 );

        play( idle );
    }

    protected void initAnimationIdle(TextureFilm frames, int offset) {
        idle = new Animation( 2, true );
        idle.frames( frames, offset + 0, offset + 1, offset + 2, offset + 1);
    }
}
