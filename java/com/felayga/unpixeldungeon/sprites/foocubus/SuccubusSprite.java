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
package com.felayga.unpixeldungeon.sprites.foocubus;

import com.felayga.unpixeldungeon.sprites.foocubus.FoocubusSprite;
import com.felayga.unpixeldungeon.windows.start.WndHeroInit;
import com.watabou.noosa.TextureFilm;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;

public class SuccubusSprite extends FoocubusSprite {
	private static int FRAMES_PER_ROW = 21;

	public SuccubusSprite() {
		super(1);
	}

	@Override
	public void die() {
		super.die();
		emitter().burst(Speck.factory(Speck.HEART), 6);
		emitter().burst(ShadowParticle.UP, 8);
	}
}
