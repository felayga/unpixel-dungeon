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

/**
 * Created by HELLO on 2/12/2017.
 */
public class FerretSprite extends GiantRatSprite {
    public FerretSprite() {
        super(3);
    }

    @Override
    protected void initRunFrames(TextureFilm frames, int offset) {
        run = new Animation(10, true);
        run.frames(frames, offset + 5, offset + 6, offset + 7, offset + 8, offset + 9, offset + 10);
    }

    @Override
    protected void initAttackFrames(TextureFilm frames, int offset) {
        attack = new Animation(15, false);
        attack.frames(frames, offset + 2, offset + 3, offset + 4, offset + 0);
    }

}
