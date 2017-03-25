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
package com.felayga.unpixeldungeon.levels;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.Actor;
import com.watabou.utils.Random;

import java.util.Arrays;

public class DeadEndLevel extends Level {

	private static final int SIZE = 5;

    public DeadEndLevel()
	{
        super(FLAG_BOULDERS_NOT_DIGGABLE | FLAG_CHASM_NOT_DIGGABLE | FLAG_WALLS_NOT_DIGGABLE);

		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	protected boolean build() {
		fill(Terrain.WALL);
		
		for (int i=2; i < SIZE; i++) {
			for (int j=2; j < SIZE; j++) {
				map(i * WIDTH + j, Terrain.EMPTY);
			}
		}
		
		for (int i=1; i <= SIZE; i++) {
            map(WIDTH + i, Terrain.PUDDLE);
            map(WIDTH * SIZE + i, Terrain.PUDDLE);
            map(WIDTH * i + 1, Terrain.PUDDLE);
            map(WIDTH * i + SIZE, Terrain.PUDDLE);
		}
		
		entrance = SIZE * WIDTH + SIZE / 2 + 1;
        exit = entrance;

        entranceAlternate = entrance;
        exitAlternate = exit;

		map(entrance, Terrain.STAIRS_UP);
		
		map((SIZE / 2 + 1) * (WIDTH + 1), Terrain.SIGN);
		
		return true;
	}

	@Override
	protected void decorate() {
		for (int i=0; i < LENGTH; i++) {
            int terrain = map(i);
			if (terrain == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map(i, Terrain.EMPTY_DECO);
			} else if (terrain == Terrain.WALL && Random.Int( 8 ) == 0) {
				map(i, Terrain.WALL_DECO);
			}
		}
	}

	@Override
	protected void createMobs() {
	}

	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
	}
	
	@Override
	public int randomRespawnCell() {
		return entrance-WIDTH;
	}

}
