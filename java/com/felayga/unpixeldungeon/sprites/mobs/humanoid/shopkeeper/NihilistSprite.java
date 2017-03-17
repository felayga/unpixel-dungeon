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

import com.watabou.noosa.particles.PixelParticle;

/**
 * Created by HELLO on 2/10/2017.
 */
public class NihilistSprite extends ShopkeeperSprite {

    public NihilistSprite(int index) {
        super(index);
    }

    public NihilistSprite() {
        this(1);
    }

    @Override
    protected void coinReset(PixelParticle coin) {
        coin.reset( x + (flipHorizontal ? 1 : 12), y + 7, 0xFFFF00, 1, 0.5f );
    }
}
