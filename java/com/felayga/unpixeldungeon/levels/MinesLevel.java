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

/**
 * Created by HELLO on 6/29/2016.
 */
public class MinesLevel extends RegularLevel {

    public MinesLevel()
    {
        super(0);

        color1 = 0x534f3e;
        color2 = 0xb9d661;

        viewDistance = 7;
    }

    @Override
    protected Feeling determineFeeling() {
        return Feeling.DARK;
    }

    @Override
    protected int fillBlockNormal() {
        return Terrain.WALL_STONE;
    }

    private int decorationPlaces;

    @Override
    public boolean build() {
        value = -0.25;
        xscale = 7.5;
        yscale = 750000.0;
        offset = Random.Float() * 524288.0;

        ghettorecursive = new ArrayList<>();
        simplexPlain();
        ghettorecursive.clear();
        int[] regions = getAndFillRegions();
        ghettorecursive = null;

        int biggest = 0;
        decorationPlaces = regions[0];
        for (int n = 1; n < regions.length; n++) {
            decorationPlaces += regions[n];
            if (regions[biggest] < regions[n]) {
                biggest = n;
            }
        }

        decorationPlaces /= 4;

        biggest += Terrain.TILE_MAX + 1;

        int pos = 0;
        int xmin = WIDTH + 1;
        int xmax = -1;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (map[pos] == biggest) {
                    if (xmin > x) {
                        xmin = x;
                    }
                    if (xmax < x) {
                        xmax = x;
                    }
                }
                pos++;
            }
        }

        entrance = randomDestination(biggest, xmin, xmax - (xmax - xmin) * 2 / 3);
        exit = entrance;
        while (exit == entrance) {
            exit = randomDestination(biggest, xmin + (xmax - xmin) * 2 / 3, xmax);
        }

        if (Random.Int(2) == 0) {
            int swap = entrance;
            entrance = exit;
            exit = swap;
        }

        map[entrance] = Terrain.STAIRS_UP;
        map[exit] = Terrain.STAIRS_DOWN;

        removeFillRegions();

        return true;
    }

    private ArrayList<Integer> ghettorecursive;

    private int[] getAndFillRegions() {
        ArrayList<Integer> retval = new ArrayList<Integer>();

        int offset = Terrain.TILE_MAX+1;
        int index = 0;

        int pos = 0;
        for (int y=0;y<HEIGHT;y++) {
            for (int x=0;x<WIDTH;x++) {
                if (map[pos] == Terrain.EMPTY) {
                    ghettorecursive.add(x + y * WIDTH);
                    setvalue = index + offset;
                    retval.add(fillRegionRecursive());
                    index++;
                }

                pos++;
            }
        }

        int[] _retval = new int[index];

        for (int n=0;n<index;n++) {
            _retval[n] = retval.get(n);
        }

        return _retval;
    }

    private int setvalue;

    private int fillRegionRecursive() {
        int retval = 0;

        while (ghettorecursive.size() > 0) {
            int pos = ghettorecursive.remove(0);

            int x = pos % WIDTH;
            int y = pos / WIDTH;

            if (map[pos] != Terrain.EMPTY) {
                //return retval;
            }
            else {
                map[pos] = setvalue;
                retval++;

                for (int suby = -1; suby <= 1; suby++) {
                    for (int subx = -1; subx <= 1; subx++) {
                        if (subx == 0 && suby == 0) {
                            continue;
                        }

                        ghettorecursive.add(x + subx + (y + suby) * WIDTH);
                        //retval += fillRegionRecursive(x + subx, y + suby, value);
                    }
                }
            }
        }

        return retval;
    }

    private void removeFillRegions() {
        int pos = 0;

        for (int y=0;y<HEIGHT;y++) {
            for (int x=0;x<WIDTH;x++) {
                if (map[pos] > Terrain.TILE_MAX) {
                    map[pos] = Terrain.EMPTY;
                }
                pos++;
            }
        }
    }

    private int randomDestination(int terrain, int xmin, int xmax) {
        int pos = -1;
        while (pos < 0 || (map[pos] != terrain)) {
            pos = Random.Int(xmin, xmax) + (Random.Int(HEIGHT - 1) + 1) * WIDTH;
        }
        return pos;
    }

    @Override
    public int randomDestination() {
        int pos = -1;
        while (pos < 0 || (!passable[pos])) {
            pos = Random.Int(WIDTH - 1) + 1 + (Random.Int(HEIGHT - 1) + 1) * WIDTH;
        }
        return pos;
    }

    private void simplexPlain() {
        int pos = 0;

        tested = new boolean[LENGTH];

        for (int n = 0; n < LENGTH; n++) {
            tested[n] = false;
        }

        for (int y = 0; y < EDGEBUFFER * 5 / 3; y++) {
            for (int x = 0; x < WIDTH; x++) {
                pos = x + y * WIDTH;
                tested[pos] = true;

                pos = (WIDTH - 1 - x) + y * WIDTH;
                tested[pos] = true;

                pos = x + (HEIGHT - 1 - y) * WIDTH;
                tested[pos] = true;

                pos = (WIDTH - 1 - x) + (HEIGHT - 1 - y) * WIDTH;
                tested[pos] = true;
            }
        }

        for (int x = 0; x < EDGEBUFFER; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                pos = x + y * WIDTH;
                tested[pos] = true;

                pos = (WIDTH - 1 - x) + y * WIDTH;
                tested[pos] = true;

                pos = x + (HEIGHT - 1 - y) * WIDTH;
                tested[pos] = true;

                pos = (WIDTH - 1 - x) + (HEIGHT - 1 - y) * WIDTH;
                tested[pos] = true;
            }
        }

        for (int y = EDGEBUFFER * 2; y < HEIGHT - EDGEBUFFER * 2; y++) {
            int x = EDGEBUFFER * 2;
            ghettorecursive.add(x + y * WIDTH);
            //simplexRecurse(x, y);

            x = WIDTH - 1 - EDGEBUFFER * 2;
            ghettorecursive.add(x + y * WIDTH);
            //simplexRecurse(x, y);
        }

        for (int x = EDGEBUFFER * 2; x < WIDTH - EDGEBUFFER * 2; x++) {
            int y = EDGEBUFFER * 2;
            ghettorecursive.add(x + y * WIDTH);
            //simplexRecurse(x, y);

            y = HEIGHT - 1 - EDGEBUFFER * 2;
            ghettorecursive.add(x + y * WIDTH);
            //simplexRecurse(x, y);
        }

        simplexRecurse();
    }

    private double xscale;
    private double yscale;
    private double offset;
    private double value;
    private boolean[] tested;

    private void simplexRecurse() {
        while (ghettorecursive.size() > 0) {
            int pos = ghettorecursive.remove(0);

            int x = pos % WIDTH;
            int y = pos / WIDTH;

            if (pos < 0 || pos >= LENGTH || x < 0 || x >= WIDTH || tested[pos]) {
                //return;
            } else {
                tested[pos] = true;

                if (SimplexNoise.noise((double) x / (double) WIDTH * xscale + offset, (double) y / (double) HEIGHT * yscale) >= value) {
                    map[pos] = Terrain.EMPTY;

                    for (int suby = -1; suby <= 1; suby++) {
                        for (int subx = -1; subx <= 1; subx++) {
                            if (subx == 0 && suby == 0) {
                                continue;
                            }

                            ghettorecursive.add(x + subx + (y + suby) * WIDTH);
                            //simplexRecurse(x + subx, y + suby);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String tilesTex() {
        return Assets.TILES_CAVES;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_CAVES;
    }

    @Override
    public String waterUnderTex() {
        //todo: not use this texture
        return Assets.WATER_UNDERPRISON;
    }

    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 6 );
    }

    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.55f : 0.35f, 3 );
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{ FireTrap.class, FrostTrap.class, PoisonTrap.class, SpearTrap.class, VenomTrap.class,
                ExplosiveTrap.class, FlashingTrap.class, GrippingTrap.class, ParalyticTrap.class, LightningTrap.class, RockfallTrap.class, OozeTrap.class,
                ConfusionTrap.class, FlockTrap.class, /*GuardianTrap.class,*/ PitfallTrap.class, SummoningTrap.class, TeleportationTrap.class,
                WarpingTrap.class};
    }

    @Override
    protected float[] trapChances() {
        return new float[]{ 8, 8, 8, 8, 8,
                4, 4, 4, 4, 4, 4, 4,
                2, 2, 2, 2, 2, 2,
                1 };
    }

    @Override
    protected boolean assignRoomType() {
        super.assignRoomType();

//        if (!Blacksmith.Quest.spawn( rooms ) && Dungeon.depth == 14)
        if (!Blacksmith.Quest.spawn( rooms ))
            return false;

        return true;
    }

    @Override
    protected void decorate() {
        int pos;

        RandomPositionValidator validator = new RandomPositionValidator() {
            @Override
            public boolean isValidPosition(int pos) {
                return map[pos] == Terrain.EMPTY;
            }
        };

        int what = 0;
        while (decorationPlaces > 0) {
            pos = randomDestination(Terrain.EMPTY, 0, WIDTH);

            switch (what % 6) {
                case 0:
                case 3:
                    for (Integer subpos : randomPositionsNear(pos, Random.IntRange(4, 16), validator)) {
                        map[subpos] = Terrain.HIGH_GRASS;
                        decorationPlaces--;
                    }
                    break;
                case 1:
                case 4:
                    for (Integer subpos : randomPositionsNear(pos, Random.IntRange(4, 8), validator)) {
                        map[subpos] = Terrain.GRASS;
                        decorationPlaces--;
                    }
                    break;
                case 2:
                    for (Integer subpos : randomPositionsNear(pos, Random.IntRange(4, 12), validator)) {
                        map[subpos] = Terrain.EMPTY_DECO;
                        decorationPlaces--;
                    }
                    break;
                default:
                    int count = Random.IntRange(4, 12);
                    while (count > 0) {
                        map[pos] = Terrain.EMPTY_DECO;
                        decorationPlaces--;
                        count--;
                        pos = randomDestination(Terrain.EMPTY, 0, WIDTH);
                    }
                    map[pos] = Terrain.EMPTY_DECO;
                    decorationPlaces--;
                    break;
            }

            what++;
        }
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
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.GRASS:
                return "Fluorescent moss";
            case Terrain.HIGH_GRASS:
                return "Fluorescent mushrooms";
            case Terrain.PUDDLE:
                return "Freezing cold water.";
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( int tile ) {
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
        addCavesVisuals( this, visuals );
        return visuals;
    }

    public static void addCavesVisuals( Level level, Group group ) {
        for (int i=0; i < LENGTH; i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new Vein( i ) );
            }
        }
    }

    private static class Vein extends Group {
        private float delay;

        public Vein( int pos ) {
            super(pos);

            delay = Random.Float( 2 );
        }

        @Override
        public void update() {

            if (visible = Dungeon.visible[pos]) {

                super.update();

                if ((delay -= Game.elapsed) <= 0) {

                    //pickaxe can remove the ore, should remove the sparkling too.
                    if (Dungeon.level.map[pos] != Terrain.WALL_DECO){
                        kill();
                        return;
                    }

                    delay = Random.Float();

                    PointF p = DungeonTilemap.tileToWorld(pos);
                    ((Sparkle)recycle( Sparkle.class )).reset(
                            p.x + Random.Float( DungeonTilemap.SIZE ),
                            p.y + Random.Float( DungeonTilemap.SIZE ) );
                }
            }
        }
    }

    public static final class Sparkle extends PixelParticle {
        public void reset( float x, float y ) {
            revive();

            this.x = x;
            this.y = y;

            left = lifespan = 0.5f;
        }

        @Override
        public void update() {
            super.update();

            float p = left / lifespan;
            size( (am = p < 0.5f ? p * 2 : (1 - p) * 2) * 2 );
        }
    }
}

