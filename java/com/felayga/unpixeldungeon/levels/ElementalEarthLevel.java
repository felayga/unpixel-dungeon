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
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HELLO on 3/21/2017.
 */

public class ElementalEarthLevel extends SimplexLevel {
    public ElementalEarthLevel() {
        super();

        color1 = 0x534f3e;
        color2 = 0xb9d661;

        viewDistance = 7;

        flags = FLAG_PIT_NOT_DIGGABLE | FLAG_CHASM_NOT_DIGGABLE;
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
    protected boolean simplexThresholdValidator(double simplex, double value) {
        return simplex < value;
    }


    @Override
    protected double generatorValue() {
        return -0.6;
    }

    @Override
    protected double generatorXScale() {
        return 4.0;
    }

    @Override
    protected double generatorYScale() {
        return 4.0;
    }

    @Override
    protected double generatorOffset() {
        return 524288.0;
    }

    @Override
    protected FillMapMethod fillMapMethod(){
        return FillMapMethod.All;
    }

    @Override
    protected boolean buildEntranceExit(int[] regions, int regionTileOffset) {
        int biggest = 0;
        List<Integer> smallests = new ArrayList<>();
        int tempSmallest = 0;

        for (int n = 1; n < regions.length; n++) {
            if (regions[biggest] < regions[n]) {
                biggest = n;
            }
            if (regions[n] > 1) {
                if (regions[tempSmallest] > regions[n]) {
                    tempSmallest = n;
                    smallests.clear();
                    smallests.add(n);
                } else if (regions[tempSmallest] == regions[n]) {
                    smallests.add(n);
                }
            }
        }

        if (smallests.size() < 1) {
            return false;
        }

        int smallest = smallests.get(Random.Int(smallests.size()));

        GLog.d("biggest spots="+regions[biggest]+" smallest spots="+regions[smallest]);

        biggest += regionTileOffset;
        smallest += regionTileOffset;

        int pos = 0;

        int xminBiggest = WIDTH + 1;
        int xmaxBiggest = -1;

        int xminSmallest = WIDTH + 1;
        int xmaxSmallest = -1;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (map(pos) == biggest) {
                    if (xminBiggest > x) {
                        xminBiggest = x;
                    }
                    if (xmaxBiggest < x) {
                        xmaxBiggest = x;
                    }
                }
                if (map(pos) == smallest) {
                    if (xminSmallest > x) {
                        xminSmallest = x;
                    }
                    if (xmaxSmallest < x) {
                        xmaxSmallest = x;
                    }
                }
                pos++;
            }
        }

        entrance = randomDestination(smallest, xminSmallest, xmaxSmallest);
        exit = entrance;
        while (exit == entrance) {
            exit = randomDestination(biggest, xminBiggest, xmaxBiggest);
        }
        exitAlternate = exit;

        map(entrance, Terrain.STAIRS_UP);
        //map(exit, Terrain.STAIRS_DOWN);

        return true;
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
    protected void decorate() {
        //nothing
    }

    @Override
    protected Map.Entry<Integer, ArrayList<Integer>> decorate(int pos, RandomPositionValidator validator) {
        return null;
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

}
