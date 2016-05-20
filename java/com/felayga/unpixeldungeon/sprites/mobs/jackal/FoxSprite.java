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

package com.felayga.unpixeldungeon.sprites.mobs.jackal;

import com.watabou.noosa.TextureFilm;

/**
 * Created by HELLO on 5/19/2016.
 */
public class FoxSprite extends JackalSprite {
    public FoxSprite() { super(2); }

    @Override
    protected void initAnimationIdle(TextureFilm frames, int offset) {
        idle = new Animation( 2, true );
        idle.frames( frames, offset + 0, offset + 0, offset + 0, offset + 0, offset + 1, offset + 1, offset + 0, offset + 0, offset + 2, offset + 2, offset + 2);
    }

}
