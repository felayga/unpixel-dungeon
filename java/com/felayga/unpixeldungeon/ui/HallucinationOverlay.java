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
package com.felayga.unpixeldungeon.ui;

import android.opengl.GLES20;

import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.FogOfWar;
import com.watabou.noosa.Image;

import java.util.Arrays;

import javax.microedition.khronos.opengles.GL10;

public class HallucinationOverlay extends Image {
    private int[] pixels;

    private int subWidth;
    private int subHeight;

    public HallucinationOverlay(int mapWidth, int mapHeight) {
        super();

        subWidth = 1;
        subHeight = 1;

        float size = DungeonTilemap.SIZE;
        width = subWidth * size;
        height = subHeight * size;

        texture(new FogOfWar.FogTexture(subWidth, subHeight, HallucinationOverlay.class));

        scale.set(
                DungeonTilemap.SIZE * mapWidth,
                DungeonTilemap.SIZE * mapHeight);

        x = y = -size / 2;

        pixels = new int[subWidth * subHeight];
        Arrays.fill(pixels, 0xFFFFFFFF);
        texture.pixels(subWidth, subHeight, pixels);
        visible = false;
    }

    private double hue = 0.0;
    private double saturation = 1.0;
    private double value = 0.0;
    private double valueStep = 0.0055;

    private void HSV(double hue, double saturation, double value) {
        int hi = (int) Math.floor(hue / 60.0) % 6;
        double f = hue / 60.0 - Math.floor(hue / 60.0);

        double v = value;
        double p = value * (1.0 - saturation);
        double q = value * (1.0 - f * saturation);
        double t = value * (1.0 - (1.0 - f) * saturation);

        switch (hi) {
            case 0:
                rm = (float) v;
                gm = (float) t;
                bm = (float) p;
                break;
            case 1:
                rm = (float) q;
                gm = (float) v;
                bm = (float) p;
                break;
            case 2:
                rm = (float) p;
                gm = (float) v;
                bm = (float) t;
                break;
            case 3:
                rm = (float) p;
                gm = (float) q;
                bm = (float) v;
                break;
            case 4:
                rm = (float) t;
                gm = (float) p;
                bm = (float) v;
                break;
            default:
                rm = (float) v;
                gm = (float) p;
                bm = (float) q;
                break;
        }
    }

    private boolean stopHallucinating = true;

    public void startHallucinating() {
        stopHallucinating = false;
        visible = true;
        hue = 0.0;
        value = 0.0;
    }

    public void stopHallucinating() {
        stopHallucinating = true;
    }

    @Override
    public void update() {
        hue += 1.0;
        value += valueStep;
        if (hue >= 360.0) {
            hue -= 360.0;
        }
        if (value > 1.0) {
            value = 1.0;
            valueStep = -valueStep;
        } else if (value < 0.0) {
            value = 0.0;
            valueStep = -valueStep;
            if (stopHallucinating) {
                this.visible = false;
            }
        }

        HSV(hue, saturation, value);
    }

    public void surfaceChanged() {
        //todo: what the fuck is screwing up the texture on phone sleep/wake?  ghetto fix here
        texture.pixels(subWidth, subHeight, pixels);
    }

    @Override
    public void draw() {
        GLES20.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ONE);
        //GLES10.glColor4f(Random.Float(), Random.Float(), Random.Float(), 1.0f);
        //GLES10.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
        super.draw();
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }
}
