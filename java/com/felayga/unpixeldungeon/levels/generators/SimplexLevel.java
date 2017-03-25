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

package com.felayga.unpixeldungeon.levels.generators;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.RegularLevel;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.SimplexNoise;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by HELLO on 3/21/2017.
 */
public abstract class SimplexLevel extends RegularLevel {

    public SimplexLevel() {
        super(0);
    }

    private int decorationPlaces;

    protected abstract double generatorValue();
    protected abstract double generatorXScale();
    protected abstract double generatorYScale();
    protected abstract double generatorOffset();
    protected abstract FillMapMethod fillMapMethod();

    protected abstract int emptyBlock();

    protected enum FillMapMethod {
        All,
        EdgeBufferSeeded
    }

    @Override
    public boolean build() {
        value = generatorValue();
        xscale = generatorXScale();
        yscale = generatorYScale();
        offset = generatorOffset() * Random.Float();
        emptyTerrain = emptyBlock();

        ghettoRecursive = new LinkedList<>();

        switch (fillMapMethod()) {
            case EdgeBufferSeeded:
                simplexFillSeeded();
                break;
            default:
                simplexFillAll();
                break;
        }

        ghettoRecursive.clear();
        int regionTileOffset = Terrain.TILE_MAX + 1;
        int[] regions = getAndFillRegions(regionTileOffset);
        ghettoRecursive = null;

        decorationPlaces = regions[0];
        for (int n = 1; n < regions.length; n++) {
            decorationPlaces += regions[n];
        }

        decorationPlaces /= 4;


        if (!buildEntranceExit(regions, regionTileOffset)) {
            return false;
        }

        removeFillRegions();

        return true;
    }

    protected boolean buildEntranceExit(int[] regions, int regionTileOffset) {
        int biggest = 0;
        for (int n = 1; n < regions.length; n++) {
            if (regions[biggest] < regions[n]) {
                biggest = n;
            }
        }

        biggest += regionTileOffset;

        int pos = 0;

        int xmin = WIDTH + 1;
        int xmax = -1;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (map(pos) == biggest) {
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

        map(entrance, Terrain.STAIRS_UP);
        map(exit, Terrain.STAIRS_DOWN);

        return true;
    }

    private LinkedList<Integer> ghettoRecursive;

    private int[] getAndFillRegions(int regionTileOffset) {
        LinkedList<Integer> retval = new LinkedList<>();

        int offset = regionTileOffset;
        int index = 0;

        int pos = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (map(pos) == Terrain.EMPTY) {
                    ghettoRecursive.add(x + y * WIDTH);
                    setValue = index + offset;
                    retval.add(fillRegionRecursive());
                    index++;
                }

                pos++;
            }
        }

        int[] _retval = new int[index];

        for (int n = 0; n < index; n++) {
            _retval[n] = retval.get(n);
        }

        return _retval;
    }

    private int setValue;

    private int fillRegionRecursive() {
        int retval = 0;

        while (ghettoRecursive.size() > 0) {
            int pos = ghettoRecursive.remove(0);

            int x = pos % WIDTH;
            int y = pos / WIDTH;

            if (map(pos) == emptyTerrain) {
                map(pos, setValue);
                retval++;

                for (int subY = -1; subY <= 1; subY++) {
                    for (int subX = -1; subX <= 1; subX++) {
                        if (subX == 0 && subY == 0) {
                            continue;
                        }

                        ghettoRecursive.add(x + subX + (y + subY) * WIDTH);
                    }
                }
            }
        }

        return retval;
    }

    private void removeFillRegions() {
        int pos = 0;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (map(pos) > Terrain.TILE_MAX) {
                    map(pos, Terrain.EMPTY);
                }
                pos++;
            }
        }
    }

    protected int randomDestination(int terrain, int xmin, int xmax) {
        int pos = -1;
        while (pos < 0 || (map(pos) != terrain)) {
            pos = Random.IntRange(xmin, xmax) + (Random.IntRange(1, HEIGHT - 2)) * WIDTH;
        }
        return pos;
    }

    @Override
    public int randomDestination() {
        int pos = -1;
        while (pos < 0 || (!passable[pos])) {
            pos = Random.IntRange(1, WIDTH - 2) + (Random.IntRange(1, HEIGHT - 2)) * WIDTH;
        }
        return pos;
    }

    private void simplexFillSeeded() {
        int pos;

        tested = new boolean[LENGTH];

        for (int n = 0; n < LENGTH; n++) {
            tested[n] = false;
        }

        for (int y = 0; y < EDGEBUFFER; y++) {
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
            ghettoRecursive.add(x + y * WIDTH);
            //simplexRecurse(x, y);

            x = WIDTH - 1 - EDGEBUFFER * 2;
            ghettoRecursive.add(x + y * WIDTH);
            //simplexRecurse(x, y);
        }

        for (int x = EDGEBUFFER * 2; x < WIDTH - EDGEBUFFER * 2; x++) {
            int y = EDGEBUFFER * 2;
            ghettoRecursive.add(x + y * WIDTH);
            //simplexRecurse(x, y);

            y = HEIGHT - 1 - EDGEBUFFER * 2;
            ghettoRecursive.add(x + y * WIDTH);
            //simplexRecurse(x, y);
        }

        while (ghettoRecursive.size() > 0) {
            pos = ghettoRecursive.remove(0);

            int x = pos % WIDTH;
            int y = pos / WIDTH;

            if (pos < 0 || pos >= LENGTH || x < 0 || x >= WIDTH || tested[pos]) {
                //return;
            } else {
                tested[pos] = true;

                if (simplexThresholdValidator(SimplexNoise.noise((double) x / (double) WIDTH * xscale + offset, (double) y / (double) HEIGHT * yscale), value)) {
                    map(pos, emptyTerrain);

                    for (int suby = -1; suby <= 1; suby++) {
                        for (int subx = -1; subx <= 1; subx++) {
                            if (subx == 0 && suby == 0) {
                                continue;
                            }

                            ghettoRecursive.add(x + subx + (y + suby) * WIDTH);
                        }
                    }
                }
            }
        }
    }

    private void simplexFillAll() {
        for (int y = EDGEBUFFER; y < HEIGHT - EDGEBUFFER; y++) {
            int pos = y * WIDTH + EDGEBUFFER;
            for (int x = EDGEBUFFER; x < WIDTH - EDGEBUFFER; x++) {
                if (simplexThresholdValidator(SimplexNoise.noise((double) x / (double) WIDTH * xscale + offset, (double) y / (double) HEIGHT * yscale), value)) {
                    map(pos, emptyTerrain);
                }
                pos++;
            }
        }
    }

    private double xscale;
    private double yscale;
    private double offset;
    private double value;
    private boolean[] tested;
    private int emptyTerrain;


    protected boolean simplexThresholdValidator(double simplex, double value) {
        return simplex >= value;
    }

    @Override
    protected void decorate() {
        int pos;

        RandomPositionValidator validator = new RandomPositionValidator() {
            @Override
            public boolean isValidPosition(int pos) {
                return map(pos) == Terrain.EMPTY;
            }
        };

        while (decorationPlaces > 0) {
            pos = randomDestination(Terrain.EMPTY, 0, WIDTH);

            Map.Entry<Integer, ArrayList<Integer>> decoration = decorate(pos, validator);

            if (decoration != null) {
                int terrain = decoration.getKey();
                ArrayList<Integer> spots = decoration.getValue();

                for (Integer subPos : spots) {
                    map(subPos, terrain);
                    decorationPlaces--;
                }
            }
        }
    }

    protected abstract Map.Entry<Integer, ArrayList<Integer>> decorate(int pos, RandomPositionValidator validator);

    @Override
    protected int randomDropCell() {
        return randomDestination();
    }

    @Override
    public int randomRespawnCell() {
        int count = 0;
        int cell;

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

}


