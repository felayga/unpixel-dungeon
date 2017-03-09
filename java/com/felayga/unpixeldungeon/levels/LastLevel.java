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
import com.felayga.unpixeldungeon.items.AmuletOfYendor;
import com.felayga.unpixeldungeon.levels.painters.Painter;
import com.watabou.noosa.Group;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Arrays;

public class LastLevel extends Level {

	private static final int SIZE = 30;

    public LastLevel()
	{
        super(0);

		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = 8;
	}

	private int pedestal;

	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	public void create() {
		super.create();
		for (int i=0; i < LENGTH; i++) {
			int flags = Terrain.flags[map(i)];
			if ((flags & Terrain.FLAG_CHASM) != 0){
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
	}

	@Override
	protected boolean build() {
        fill(Terrain.CHASM);

        Painter.fill(this, 7, 31, 19, 1, Terrain.WALL);
        Painter.fill(this, 15, 10, 3, 21, Terrain.EMPTY);
        Painter.fill(this, 13, 30, 7, 1, Terrain.EMPTY);
        Painter.fill(this, 14, 29, 5, 1, Terrain.EMPTY);

        Painter.fill(this, 14, 9, 5, 7, Terrain.EMPTY);
        Painter.fill(this, 13, 10, 7, 5, Terrain.EMPTY);

        //Painter.fill( this, 2, 2, SIZE-2, SIZE-2, Terrain.EMPTY );
        //Painter.fill( this, SIZE/2, SIZE/2, 3, 3, Terrain.EMPTY_SP );

        entrance = SIZE * WIDTH + SIZE / 2 + 1;
        map(entrance, Terrain.STAIRS_UP);

        pedestal = (SIZE / 2 + 1) * (WIDTH + 1) - 4 * WIDTH;
        map(pedestal, Terrain.PEDESTAL);

        map(pedestal - 1 - WIDTH, Terrain.STATUE_SP);
        map(pedestal + 1 - WIDTH, Terrain.STATUE_SP);
        map(pedestal - 1 + WIDTH, Terrain.STATUE_SP);
        map(pedestal + 1 + WIDTH, Terrain.STATUE_SP);

        exit = pedestal;

        int pos = pedestal;

        map(pos - WIDTH, Terrain.PUDDLE);
        map(pos - 1, Terrain.PUDDLE);
        map(pos + 1, Terrain.PUDDLE);
        map(pos - 2, Terrain.PUDDLE);
        map(pos + 2, Terrain.PUDDLE);
        pos += WIDTH;
        map(pos, Terrain.PUDDLE);
        map(pos - 2, Terrain.PUDDLE);
        map(pos + 2, Terrain.PUDDLE);
        map(pos - 3, Terrain.PUDDLE);
        map(pos + 3, Terrain.PUDDLE);
        pos += WIDTH;
        map(pos - 3, Terrain.PUDDLE);
        map(pos - 2, Terrain.PUDDLE);
        map(pos - 1, Terrain.PUDDLE);
        map(pos, Terrain.PUDDLE);
        map(pos + 1, Terrain.PUDDLE);
        map(pos + 2, Terrain.PUDDLE);
        map(pos + 3, Terrain.PUDDLE);
        pos += WIDTH;
        map(pos - 2, Terrain.PUDDLE);
        map(pos + 2, Terrain.PUDDLE);

        feeling = Feeling.NONE;
        viewDistance = 8;

        return true;
    }

	@Override
	protected void decorate() {
		for (int i=0; i < LENGTH; i++) {
			if (map(i) == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map(i, Terrain.EMPTY_DECO);
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
		drop( new AmuletOfYendor(), pedestal );
	}

	@Override
	public int randomRespawnCell() {
		int cell = entrance + NEIGHBOURS8[Random.Int(8)];
		while (!passable[cell]){
			cell = entrance + NEIGHBOURS8[Random.Int(8)];
		}
		return cell;
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.PUDDLE:
			return "Cold lava puddle";
		case Terrain.GRASS:
			return "Embermoss";
		case Terrain.HIGH_GRASS:
			return "Emberfungi";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "Pillar";
		default:
			return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.PUDDLE:
			return "It looks like lava, but it's cold and probably safe to touch.";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "The pillar is made of real humanoid skulls. Awesome.";
		default:
			return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals(this, visuals);
		return visuals;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		for (int i=0; i < LENGTH; i++) {
			int flags = Terrain.flags[map(i)];
			if ((flags & Terrain.FLAG_CHASM) != 0){
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
	}
}
