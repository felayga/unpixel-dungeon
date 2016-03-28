/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.sprites;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.watabou.noosa.TextureFilm;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;

public class BoulderSprite extends MobSprite {

    public BoulderSprite() {
        super();

        texture( Assets.BOULDER );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0 );

        run = new Animation( 10, true );
        run.frames( frames, 0, 1, 2, 3 );

        attack = new Animation( 10, false );
        attack.frames( frames, 0 );

        die = new Animation( 8, false );
        die.frames( frames, 0, 4, 5, 6, 7 );

        play( idle );
    }

    /*
    //doesn't work for getting sprite visible at all times
    @Override
    public boolean isVisible()
    {
        return true;
    }
    */

    @Override
    public void die() {
        super.die();
    }

    @Override
    public int blood() {
        return 0xFFFFFF;
    }
}
