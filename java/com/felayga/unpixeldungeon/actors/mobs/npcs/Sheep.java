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
package com.felayga.unpixeldungeon.actors.mobs.npcs;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.SheepSprite;
import com.watabou.utils.Random;

public class Sheep extends NPC {

	private static final String[] QUOTES = {"Baa!", "Baa?", "Baa.", "Baa..."};

	public Sheep()
	{
		super(0);

		name = "sheep";
		spriteClass = SheepSprite.class;
	}

	public long lifespan;

	private boolean initialized = false;

	@Override
	protected boolean act() {
		if (initialized) {
			HP = 0;

			destroy();
			sprite.die();

		} else {
			initialized = true;
			spend( lifespan + Random.Long(GameTime.TICK * 3), false );
		}
		return true;
	}

	@Override
	public void damage( int dmg, MagicType type, Actor source) {
	}

	@Override
	public String description() {
		return
				"This is a magic sheep. What's so magical about it? You can't kill it. " +
						"It will stand there until it magcially fades away, all the while chewing cud with a blank stare.";
	}

	@Override
	public void interact() {
		yell( Random.element( QUOTES ) );
	}
}
