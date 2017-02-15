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
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.levels.traps.AlarmTrap;
import com.felayga.unpixeldungeon.levels.traps.ChillingTrap;
import com.felayga.unpixeldungeon.levels.traps.FlockTrap;
import com.felayga.unpixeldungeon.levels.traps.OozeTrap;
import com.felayga.unpixeldungeon.levels.traps.SummoningTrap;
import com.felayga.unpixeldungeon.levels.traps.TeleportationTrap;
import com.felayga.unpixeldungeon.levels.traps.ToxicTrap;
import com.felayga.unpixeldungeon.levels.traps.WornTrap;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

//import com.felayga.unpixeldungeon.actors.mobs.npcs.Ghost;

public class SewerLevel extends RegularLevel {

    public SewerLevel()
	{
        super(0);

		color1 = 0x48763c;
		color2 = 0x59994a;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

	@Override
	public String waterUnderTex() {
		return Assets.WATER_UNDERSEWERS;
	}
	
	protected boolean[] water() {
		return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 5 );
	}
	
	protected boolean[] grass() {
		return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 4 );
	}

	@Override
	protected Class<?>[] trapClasses() {
		return Dungeon.depth() == 1 ?
				new Class<?>[]{WornTrap.class} :
				new Class<?>[]{ChillingTrap.class, ToxicTrap.class, WornTrap.class,
						AlarmTrap.class, OozeTrap.class,
						FlockTrap.class, SummoningTrap.class, TeleportationTrap.class, };
}

	@Override
	protected float[] trapChances() {
		return Dungeon.depth() == 1 ?
				new float[]{1} :
				new float[]{4, 4, 4,
						2, 2,
						1, 1, 1};
	}

	@Override
	protected void decorate() {
		
		for (int i=0; i < WIDTH; i++) {
			if (map[i] == Terrain.WALL &&
				map[i + WIDTH] == Terrain.PUDDLE &&
				Random.Int( 4 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.WALL &&
				map[i - WIDTH] == Terrain.WALL &&
				map[i + WIDTH] == Terrain.PUDDLE &&
				Random.Int( 2 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
			if (map[i] == Terrain.EMPTY) {
				
				int count =
					(map[i + 1] == Terrain.WALL ? 1 : 0) +
					(map[i - 1] == Terrain.WALL ? 1 : 0) +
					(map[i + WIDTH] == Terrain.WALL ? 1 : 0) +
					(map[i - WIDTH] == Terrain.WALL ? 1 : 0);
				
				if (Random.Int( 16 ) < count * count) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}

		//hides all doors in the entrance room on floor 2, teaches the player to search.
		if (Dungeon.depth() == 2)
			for (Room r : roomEntrance.connected.keySet()){
				Room.Door d = roomEntrance.connected.get(r);
				if (d.type == Room.Door.Type.REGULAR) {
                    map[d.x + d.y * WIDTH] = Terrain.SECRET_DOOR;
                }
                else if (d.type == Room.Door.Type.LOCKED) {
                    map[d.x + d.y * WIDTH] = Terrain.SECRET_LOCKED_DOOR;
                }
			}
		
		placeSign();
	}
	
	@Override
	protected void createItems() {
		//Ghost.Quest.spawn( this );
		
		super.createItems();
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		addSewerVisuals(this, visuals);
		return visuals;
	}
	
	public static void addSewerVisuals( Level level, Group group ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				group.add( new Sink( i ) );
			}
		}
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.PUDDLE:
			return "Murky water";
		default:
			return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.EMPTY_DECO:
			return "Wet yellowish moss covers the floor.";
		default:
			return super.tileDesc( tile );
		}
	}
	
	public static class Sink extends Emitter {
        private float rippleDelay = 0;

        private static final Emitter.Factory factory = new Factory() {

            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                WaterParticle p = (WaterParticle) emitter.recycle(WaterParticle.class);
                p.reset(x, y);
            }
        };

        public Sink(int pos) {
            this(pos, -2.0f, 1.0f);
        }

        public Sink(int pos, float xOffset, float yOffset) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld(pos);
            pos(pos, p.x + xOffset, p.y + yOffset, 4, 0);

            pour(factory, 0.05f);
        }

        @Override
        public void update() {
            if (visible = Dungeon.visible[pos]) {

                super.update();

                if ((rippleDelay -= Game.elapsed) <= 0) {
                    GameScene.ripple(pos + WIDTH).y -= DungeonTilemap.SIZE / 2;
                    rippleDelay = Random.Float(0.2f, 0.3f);
                }
            }
        }
    }
	
	public static final class WaterParticle extends PixelParticle {
		
		public WaterParticle() {
			super();
			
			acc.y = 50;
			am = 0.5f;
			
			color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
			size( 2 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			speed.set( Random.Float( -2, +2 ), 0 );
			
			left = lifespan = 0.5f;
		}
	}
}
