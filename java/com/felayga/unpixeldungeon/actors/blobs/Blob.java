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
package com.felayga.unpixeldungeon.actors.blobs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.BlobEmitter;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.unPixelDungeon;
import com.felayga.unpixeldungeon.utils.BArray;
import com.watabou.utils.Bundle;

import java.util.Arrays;

public class Blob extends Actor {

    {
        actPriority = 1; //take priority over mobs, but not the hero
    }

    public static final int WIDTH = Level.WIDTH;
    public static final int HEIGHT = Level.HEIGHT;
    public static final int LENGTH = Level.LENGTH;

    public int volume = 0;

    public int[] cur;
    protected int[] off;

    public BlobEmitter emitter;

    private int ownerRegistryIndex;

    public int ownerRegistryIndex() {
        return ownerRegistryIndex;
    }

    protected Blob() {

        cur = new int[LENGTH];
        off = new int[LENGTH];

        volume = 0;
    }

    private static final String CUR = "cur";
    private static final String START = "start";
    private static final String OWNERREGISTRYINDEX = "ownerRegistryIndex";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        if (volume > 0) {

            int start;
            for (start = 0; start < LENGTH; start++) {
                if (cur[start] > 0) {
                    break;
                }
            }
            int end;
            for (end = LENGTH - 1; end > start; end--) {
                if (cur[end] > 0) {
                    break;
                }
            }

            bundle.put(START, start);
            bundle.put(CUR, trim(start, end + 1));
        }

        bundle.put(OWNERREGISTRYINDEX, ownerRegistryIndex);
    }

    private int[] trim(int start, int end) {
        int len = end - start;
        int[] copy = new int[len];
        System.arraycopy(cur, start, copy, 0, len);
        return copy;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        int[] data = bundle.getIntArray(CUR);
        if (data != null) {
            int start = bundle.getInt(START);
            for (int i = 0; i < data.length; i++) {
                cur[i + start] = data[i];
                volume += data[i];
            }
        }

        if (Level.resizingNeeded) {
            int[] cur = new int[Level.LENGTH];
            Arrays.fill(cur, 0);

            int loadedMapSize = Level.loadedMapSize;
            for (int i = 0; i < loadedMapSize; i++) {
                System.arraycopy(this.cur, i * loadedMapSize, cur, i * Level.WIDTH, loadedMapSize);
            }

            this.cur = cur;
        }

        ownerRegistryIndex = bundle.getInt(OWNERREGISTRYINDEX);
    }

    @Override
    public boolean act() {
        spend_new(GameTime.TICK, false);

        if (volume > 0) {

            volume = 0;
            evolve();

            int[] tmp = off;
            off = cur;
            cur = tmp;

        }

        return true;
    }

    public void use(BlobEmitter emitter) {
        this.emitter = emitter;
    }

    protected void evolve() {
        boolean[] notBlocking = BArray.not(Level.solid, null);

        for (int i = 1; i < HEIGHT - 1; i++) {

            int from = i * WIDTH + 1;
            int to = from + WIDTH - 2;

            for (int pos = from; pos < to; pos++) {
                if (notBlocking[pos]) {

                    int count = 1;
                    int sum = cur[pos];

                    if (notBlocking[pos - 1]) {
                        sum += cur[pos - 1];
                        count++;
                    }
                    if (notBlocking[pos + 1]) {
                        sum += cur[pos + 1];
                        count++;
                    }
                    if (notBlocking[pos - WIDTH]) {
                        sum += cur[pos - WIDTH];
                        count++;
                    }
                    if (notBlocking[pos + WIDTH]) {
                        sum += cur[pos + WIDTH];
                        count++;
                    }

                    int value = sum >= count ? (sum / count) - 1 : 0;
                    off[pos] = value;

                    volume += value;
                } else {
                    off[pos] = 0;
                }
            }
        }
    }

    public void seed(int cell, int amount) {
        cur[cell] += amount;
        volume += amount;
    }

    public void clear(int cell) {
        volume -= cur[cell];
        cur[cell] = 0;
    }

    public String tileDesc() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Blob> T seed(Char owner, int cell, int amount, Class<T> type) {
        try {
            T gas = (T) Dungeon.level.blobs.get(type);
            if (gas == null) {
                gas = type.newInstance();
                Dungeon.level.blobs.put(type, gas);
            }

            Blob b = gas;
            if (owner != null) {
                b.ownerRegistryIndex = owner.charRegistryIndex();
            } else {
                b.ownerRegistryIndex = -1;
            }

            gas.seed(cell, amount);

            return gas;

        } catch (Exception e) {
            unPixelDungeon.reportException(e);
            return null;
        }
    }
}
