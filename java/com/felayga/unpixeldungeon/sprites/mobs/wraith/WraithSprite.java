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
package com.felayga.unpixeldungeon.sprites.mobs.wraith;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class WraithSprite extends MobSprite {
	private static final int FRAMES_PER_ROW = 9;

	public WraithSprite() { this(0); }

	public WraithSprite(int index) {
		super();
		
		texture( Assets.Mobs.WRAITH );
		
		TextureFilm frames = new TextureFilm( texture, 14, 15 );

		int offset = index * FRAMES_PER_ROW;
		
		idle = new Animation( 5, true );
		idle.frames( frames, offset + 0, offset + 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, offset + 0, offset + 1 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, offset + 0, offset + 2, offset + 3 );
		
		die = new Animation( 8, false );
		die.frames( frames, offset + 0, offset + 4, offset + 5, offset + 6, offset + 7 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0x88000000;
	}
}
