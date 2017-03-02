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
package com.felayga.unpixeldungeon.effects.particles;

import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class FlameParticle extends PixelParticle.Shrinking {
	public static final Emitter.Factory FACTORY = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((FlameParticle)emitter.recycle( FlameParticle.class )).reset( x, y );
		}

		@Override
		public boolean lightMode() {
			return true;
		}
	};

    private static int flameParticleIndex = 0;
	
	public FlameParticle() {
        super();

        flameParticleIndex++;

        switch (flameParticleIndex % 8) {
            case 0:
            case 1:
            case 2:
                color(0xFF5500);
                break;
            case 3:
            case 4:
                color(0xEE7722);
                break;
            case 5:
                color(0xFF9D33);
                break;
            case 6:
                color(0xFFC852);
                break;
            case 7:
                color(0xFFE566);
                break;
        }

        //color(0xEE7722);
        lifespan = 0.6f;

        acc.set(0, -80);
    }
	
	public void reset( float x, float y ) {
		revive();
		
		this.x = x;
		this.y = y;
		
		left = lifespan;
		
		size = 4;
		speed.set( 0 );
	}
	
	@Override
	public void update() {
		super.update();
		float p = left / lifespan;
		am = p > 0.8f ? (1 - p) * 5 : 1;
	}
}
