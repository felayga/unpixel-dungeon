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
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.felayga.unpixeldungeon.levels.generators.SimplexLevel;
import com.felayga.unpixeldungeon.levels.traps.ConfusionTrap;
import com.felayga.unpixeldungeon.levels.traps.ExplosiveTrap;
import com.felayga.unpixeldungeon.levels.traps.FireTrap;
import com.felayga.unpixeldungeon.levels.traps.FlashingTrap;
import com.felayga.unpixeldungeon.levels.traps.FlockTrap;
import com.felayga.unpixeldungeon.levels.traps.FrostTrap;
import com.felayga.unpixeldungeon.levels.traps.GrippingTrap;
import com.felayga.unpixeldungeon.levels.traps.LightningTrap;
import com.felayga.unpixeldungeon.levels.traps.OozeTrap;
import com.felayga.unpixeldungeon.levels.traps.ParalyticTrap;
import com.felayga.unpixeldungeon.levels.traps.PitfallTrap;
import com.felayga.unpixeldungeon.levels.traps.PoisonTrap;
import com.felayga.unpixeldungeon.levels.traps.RockfallTrap;
import com.felayga.unpixeldungeon.levels.traps.SpearTrap;
import com.felayga.unpixeldungeon.levels.traps.SummoningTrap;
import com.felayga.unpixeldungeon.levels.traps.TeleportationTrap;
import com.felayga.unpixeldungeon.levels.traps.VenomTrap;
import com.felayga.unpixeldungeon.levels.traps.WarpingTrap;
import com.felayga.unpixeldungeon.mechanics.SimplexNoise;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by HELLO on 6/29/2016.
 */
public class MinesLevel extends SimplexLevel {
    public MinesLevel() {
        super();

        color1 = 0x534f3e;
        color2 = 0xb9d661;

        viewDistance = 7;
    }

    @Override
    protected Feeling determineFeeling() {
        return Feeling.DARK;
    }

    @Override
    protected int fillBlock() {
        return Terrain.WALL_STONE;
    }

    @Override
    protected int emptyBlock() {
        return Terrain.EMPTY;
    }

    @Override
    protected double generatorValue() {
        return 0.0;
    }

    @Override
    protected double generatorXScale() {
        return 7.5;
    }

    @Override
    protected double generatorYScale() {
        return 750000.0;
    }

    @Override
    protected double generatorOffset() {
        return 524288.0;
    }

    @Override
    protected FillMapMethod fillMapMethod(){
        return FillMapMethod.EdgeBufferSeeded;
    }

    @Override
    public String tilesTex() {
        return Assets.TILES_CAVES;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_CAVES;
    }

    protected boolean[] water() {
        return Patch.generate(feeling == Feeling.WATER ? 0.60f : 0.45f, 6);
    }

    protected boolean[] grass() {
        return Patch.generate(feeling == Feeling.GRASS ? 0.55f : 0.35f, 3);
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{FireTrap.class, FrostTrap.class, PoisonTrap.class, SpearTrap.class, VenomTrap.class,
                ExplosiveTrap.class, FlashingTrap.class, GrippingTrap.class, ParalyticTrap.class, LightningTrap.class, RockfallTrap.class, OozeTrap.class,
                ConfusionTrap.class, FlockTrap.class, /*GuardianTrap.class,*/ PitfallTrap.class, SummoningTrap.class, TeleportationTrap.class,
                WarpingTrap.class};
    }

    @Override
    protected float[] trapChances() {
        return new float[]{8, 8, 8, 8, 8,
                4, 4, 4, 4, 4, 4, 4,
                2, 2, 2, 2, 2, 2,
                1};
    }

    @Override
    protected boolean assignRoomType() {
        super.assignRoomType();

//        if (!Blacksmith.Quest.spawn( rooms ) && Dungeon.depth == 14)
        if (!Blacksmith.Quest.spawn(rooms))
            return false;

        return true;
    }

    @Override
    protected Map.Entry<Integer, ArrayList<Integer>> decorate(int pos, RandomPositionValidator validator) {
        ArrayList<Integer> spots = null;
        int terrain = 0;

        switch (pos % 6) {
            case 0:
            case 3:
                spots = randomPositionsNear(pos, Random.IntRange(4, 16), validator);
                terrain = Terrain.HIGH_GRASS;
                break;
            case 1:
            case 4:
                spots = randomPositionsNear(pos, Random.IntRange(4, 8), validator);
                terrain = Terrain.GRASS;
                break;
            case 2:
                spots = randomPositionsNear(pos, Random.IntRange(4, 12), validator);
                terrain = Terrain.EMPTY_DECO;
                break;
            default:
                spots = new ArrayList<>();
                terrain = Terrain.EMPTY_DECO;
                int count = Random.IntRange(4, 12);
                while (count > 0) {
                    spots.add(pos);
                    count--;
                    pos = randomDestination(Terrain.EMPTY, 0, WIDTH);
                }
                spots.add(pos);
                break;
        }

        final int _terrain = terrain;
        final ArrayList<Integer> _spots = spots;

        return new Map.Entry<Integer, ArrayList<Integer>>() {
            @Override
            public Integer getKey() {
                return _terrain;
            }

            @Override
            public ArrayList<Integer> getValue() {
                return _spots;
            }

            @Override
            public ArrayList<Integer> setValue(ArrayList<Integer> object) {
                return null;
            }
        };
    }

    @Override
    protected void createMobs() {
    }

    @Override
    protected int randomDropCell() {
        return randomDestination();
    }

    @Override
    public int randomRespawnCell() {
        int count = 0;
        int cell = -1;

        while (true) {
            if (++count > 30) {
                return -1;
            }

            cell = randomDestination();
            if (!Dungeon.visible[cell] && Actor.findChar(cell) == null && Level.passable[cell]) {
                return cell;
            }

        }
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.GRASS:
                return "Fluorescent moss";
            case Terrain.HIGH_GRASS:
                return "Fluorescent mushrooms";
            case Terrain.PUDDLE:
                return "Freezing cold puddle.";
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.STAIRS_UP:
            case Terrain.STAIRS_UP_ALTERNATE:
                return "The ladder leads up to the upper depth.";
            case Terrain.STAIRS_DOWN:
            case Terrain.STAIRS_DOWN_ALTERNATE:
                return "The ladder leads down to the lower depth.";
            case Terrain.HIGH_GRASS:
                return "Huge mushrooms block the view.";
            case Terrain.WALL_DECO:
                return "A vein of some ore is visible on the wall. Gold?";
        /*
		case Terrain.BOOKSHELF:
			return "Who would need a bookshelf in a cave?";
			// Exactly.
		*/
            default:
                return super.tileDesc(tile);
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addCavesVisuals(this, visuals);
        return visuals;
    }

    public static void addCavesVisuals(Level level, Group group) {
        for (int i = 0; i < LENGTH; i++) {
            if (level.map(i) == Terrain.WALL_DECO) {
                group.add(new Vein(i));
            }
        }
    }

    private static class Vein extends Group {
        private float delay;

        public Vein(int pos) {
            super(pos);

            delay = Random.Float(2);
        }

        @Override
        public void update() {

            if (visible = Dungeon.visible[pos]) {

                super.update();

                if ((delay -= Game.elapsed) <= 0) {

                    //pickaxe can remove the ore, should remove the sparkling too.
                    if (Dungeon.level.map(pos) != Terrain.WALL_DECO) {
                        kill();
                        return;
                    }

                    delay = Random.Float();

                    PointF p = DungeonTilemap.tileToWorld(pos);
                    ((Sparkle) recycle(Sparkle.class)).reset(
                            p.x + Random.Float(DungeonTilemap.SIZE),
                            p.y + Random.Float(DungeonTilemap.SIZE));
                }
            }
        }
    }

    public static final class Sparkle extends PixelParticle {
        public void reset(float x, float y) {
            revive();

            this.x = x;
            this.y = y;

            left = lifespan = 0.5f;
        }

        @Override
        public void update() {
            super.update();

            float p = left / lifespan;
            size((am = p < 0.5f ? p * 2 : (1 - p) * 2) * 2);
        }
    }
}

