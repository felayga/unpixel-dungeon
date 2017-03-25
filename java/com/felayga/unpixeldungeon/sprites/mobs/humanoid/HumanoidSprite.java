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
package com.felayga.unpixeldungeon.sprites.mobs.humanoid;

import com.felayga.unpixeldungeon.sprites.mobs.MobSprite;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.watabou.noosa.TextureFilm;

public abstract class HumanoidSprite extends MobSprite {

    public HumanoidSprite(int index, Object textureSource, int width, int height, int framesPerRow) {
        super();

        texture(textureSource);

        TextureFilm frames = new TextureFilm(texture, width, height);

        int offset = index * framesPerRow;

        initAnimationIdle(frames, offset);
        initAnimationRun(frames, offset);
        initAnimationDie(frames, offset);
        initAnimationAttack(frames, offset);

        play(idle);
    }

    protected void initAnimationIdle(TextureFilm frames, int offset) {
        idle = new Animation( 1, true );
        idle.frames(frames, offset + 0, offset + 0, offset + 0, offset + 1, offset + 0, offset + 0, offset + 1, offset + 1);
    }

    protected void initAnimationRun(TextureFilm frames, int offset) {
        run = new Animation( HeroSprite.RUN_FRAMERATE, true );
        run.frames( frames, offset + 2, offset + 3, offset + 4, offset + 5, offset + 6, offset + 7 );
    }

    protected void initAnimationDie(TextureFilm frames, int offset) {
        die = new Animation( 20, false );
        die.frames( frames, offset + 0, offset + 11, offset + 12, offset + 13, offset + 14 );
    }

    protected void initAnimationAttack(TextureFilm frames, int offset) {
        attack = new Animation( 15, false );
        attack.frames( frames, offset + 8, offset + 9, offset + 10, offset + 0 );
    }

}
