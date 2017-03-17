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
package com.felayga.unpixeldungeon.sprites.mobs.humanoid.shopkeeper;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.HumanoidSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.PixelParticle;

public class ShopkeeperSprite extends HumanoidSprite {
	private PixelParticle coin;

    public ShopkeeperSprite() {
        this(0);
    }

	public ShopkeeperSprite(int index) {
		super(index, Assets.Mobs.HUMAN, 14, 15, 18);
	}

    @Override
    protected void initAnimationIdle(TextureFilm frames, int offset) {
        idle = new Animation( 10, true );
        idle.frames(frames, offset + 1, offset + 1, offset + 1, offset + 1, offset + 1, offset + 0, offset + 0, offset + 0, offset + 0);
    }

	@Override
	public void onComplete( Animation anim ) {
		super.onComplete(anim);
		
		if (visible && anim == idle) {
			if (coin == null) {
				coin = new PixelParticle();
				parent.add( coin );
			}
			coinReset(coin);
			coin.speed.y = -40;
			coin.acc.y = +160;
		}
	}

    protected void coinReset(PixelParticle coin) {
        coin.reset( x + (flipHorizontal ? 0 : 13), y + 7, 0xFFFF00, 1, 0.5f );
    }
}
