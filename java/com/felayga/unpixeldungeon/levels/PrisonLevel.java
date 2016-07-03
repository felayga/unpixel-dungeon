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
package com.felayga.unpixeldungeon.levels;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.felayga.unpixeldungeon.effects.Halo;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.levels.Room.Type;
import com.felayga.unpixeldungeon.levels.traps.AlarmTrap;
import com.felayga.unpixeldungeon.levels.traps.ChillingTrap;
import com.felayga.unpixeldungeon.levels.traps.ConfusionTrap;
import com.felayga.unpixeldungeon.levels.traps.FireTrap;
import com.felayga.unpixeldungeon.levels.traps.FlashingTrap;
import com.felayga.unpixeldungeon.levels.traps.FlockTrap;
import com.felayga.unpixeldungeon.levels.traps.GrippingTrap;
import com.felayga.unpixeldungeon.levels.traps.LightningTrap;
import com.felayga.unpixeldungeon.levels.traps.OozeTrap;
import com.felayga.unpixeldungeon.levels.traps.ParalyticTrap;
import com.felayga.unpixeldungeon.levels.traps.PoisonTrap;
import com.felayga.unpixeldungeon.levels.traps.SpearTrap;
import com.felayga.unpixeldungeon.levels.traps.SummoningTrap;
import com.felayga.unpixeldungeon.levels.traps.TeleportationTrap;
import com.felayga.unpixeldungeon.levels.traps.ToxicTrap;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class PrisonLevel extends RegularLevel {

    public PrisonLevel()
	{
        super(0);

		color1 = 0x6a723d;
		color2 = 0x88924c;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}
	
	protected boolean[] water() {
		return Patch.generate( feeling == Feeling.WATER ? 0.65f : 0.45f, 4 );
	}
	
	protected boolean[] grass() {
		return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 3 );
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class[]{ ChillingTrap.class, FireTrap.class, PoisonTrap.class, SpearTrap.class, ToxicTrap.class,
				AlarmTrap.class, FlashingTrap.class, GrippingTrap.class, ParalyticTrap.class, LightningTrap.class, OozeTrap.class,
				ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class, };
	}

	@Override
	protected float[] trapChances() {
		return new float[]{ 4, 4, 4, 4,
				2, 2, 2, 2, 2, 2,
				1, 1, 1, 1 };
	}

	@Override
	protected boolean assignRoomType() {
		super.assignRoomType();
		
		for (Room r : rooms) {
			if (r.type == Type.TUNNEL) {
				r.type = Type.PASSAGE;
			}
		}

		return Wandmaker.Quest.spawn( this, roomEntrance, rooms );
	}
	
	@Override
	protected void decorate() {
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
			if (map[i] == Terrain.EMPTY) {
				
				float c = 0.05f;
				if (map[i + 1] == Terrain.WALL && map[i + WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i + WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i + 1] == Terrain.WALL && map[i - WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i - WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				
				if (Random.Float() < c) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		for (int i=0; i < WIDTH; i++) {
			if (map[i] == Terrain.WALL &&
				(map[i + WIDTH] == Terrain.EMPTY || map[i + WIDTH] == Terrain.EMPTY_SP) &&
				Random.Int( 6 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.WALL &&
				map[i - WIDTH] == Terrain.WALL &&
				(map[i + WIDTH] == Terrain.EMPTY || map[i + WIDTH] == Terrain.EMPTY_SP) &&
				Random.Int( 3 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		placeSign();
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Dark cold water.";
		default:
			return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.EMPTY_DECO:
			return "There are old blood stains on the floor.";
		default:
			return super.tileDesc(tile);
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		addPrisonVisuals(this, visuals);
		return visuals;
	}

	public static void addPrisonVisuals(Level level, Group group){
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				group.add( new Torch( i ) );
			}
		}
	}
	
	public static class Torch extends Emitter {
		public Torch( int pos ) {
			super();
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( pos, p.x - 1, p.y + 3, 2, 0 );
			
			pour( FlameParticle.FACTORY, 0.15f );
			
			add( new Halo( 16, 0xFFFFCC, 0.2f ).point( p.x, p.y ) );
		}
		
		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				super.update();
			}
		}
	}
}
