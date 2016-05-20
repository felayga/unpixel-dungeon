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
package com.felayga.unpixeldungeon.sprites.mobs.werejackal;

import com.felayga.unpixeldungeon.sprites.MobSprite;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.watabou.noosa.TextureFilm;
import com.felayga.unpixeldungeon.Assets;

public class WerejackalSprite extends MobSprite {
    private static final int FRAMES_PER_ROW = 16;

    TextureFilm frames;
    private int transformedIndex;
    private int currentIndex;

    public WerejackalSprite() { this(1); }

    public WerejackalSprite(int index) {
        super();

        texture(Assets.Mobs.WEREJACKAL);

        frames = new TextureFilm(texture, 16, 16);

        transformedIndex = index;
        currentIndex = -1;

        transform(false);
    }

    private void updateAppearance(int index) {
        if (currentIndex == index) {
            return;
        }

        currentIndex = index;

        int offset = index * FRAMES_PER_ROW;

        idle = new Animation( 1, true );
        idle.frames(frames, offset + 0, offset + 0, offset + 0, offset + 1, offset + 0, offset + 0, offset + 2, offset + 2,
                offset + 0, offset + 0, offset + 0, offset + 1, offset + 1, offset + 0, offset + 0, offset + 2);

        run = new Animation( HeroSprite.RUN_FRAMERATE, true );
        run.frames( frames, offset + 6, offset + 7, offset + 8, offset + 9, offset + 10, offset + 11 );

        die = new Animation( 20, false );
        die.frames( frames, offset + 0, offset + 12, offset + 13, offset + 14, offset + 15 );

        attack = new Animation( 15, false );
        attack.frames(frames, offset + 3, offset + 4, offset + 5, offset + 0);

        play(idle);
    }

    public final boolean transformed() {
        return transformedIndex == currentIndex;
    }

    public final void transform(boolean transformed) {
        if (transformed) {
            updateAppearance(transformedIndex);
        }
        else {
            updateAppearance(0);
        }
    }

}
