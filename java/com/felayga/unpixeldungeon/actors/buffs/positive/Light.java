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
package com.felayga.unpixeldungeon.actors.buffs.positive;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.tools.Torch;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.ShadowCaster;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Light extends FlavourBuff {
    private int distance;

    public int distance() {
        return distance;
    }

    public int registryFlag;
    private int pos;

    public Light() {
        distance = 4;
        registryFlag = 0;
    }

    private static final String DISTANCE = "distance";
    private static final String DURATION = "duration";
    private static final String REGISTRYFLAG = "registryFlag";
    private static final String LIGHTUNDOLIST = "lightUndoList";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(DISTANCE, distance);
        bundle.put(DURATION, duration);
        bundle.put(REGISTRYFLAG, registryFlag);

        int[] undo = new int[lightUndoList.size()];
        for (int n=0;n<lightUndoList.size();n++) {
            undo[n] = lightUndoList.get(n);
        }
        bundle.put(LIGHTUNDOLIST, undo);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        distance = bundle.getInt(DISTANCE);
        duration = bundle.getLong(DURATION);
        registryFlag = bundle.getInt(REGISTRYFLAG);

        int[] undo = bundle.getIntArray(LIGHTUNDOLIST);
        lightUndoList.clear();
        for (int n = 0; n < undo.length; n++) {
            lightUndoList.add(undo[n]);
        }
    }

    @Override
    public void detach() {
        detach(Dungeon.level);
    }

    public void detach(Level level) {
        handleArea(level, false, true);
        Dungeon.observe();

        super.detach();
    }

    private List<Integer> lightUndoList = new ArrayList<>();

    private void handleArea(Level level, boolean on, boolean updateLightMap) {
        handleArea(pos, distance, registryFlag, lightUndoList, level, on, updateLightMap);
    }

    public static void handleArea(int pos, int distance, int registryFlag, List<Integer> lightUndoList, Level level, boolean on, boolean updateLightMap) {
        //GLog.d("Light.handleArea pos="+pos+" on="+on);
        if (level != null) {
            if (on) {
                ShadowCaster.castLight(lightUndoList, pos % Level.WIDTH, pos / Level.WIDTH, level, distance, registryFlag);
            } else {
                int lightFlag = registryFlag ^ Level.LIGHTMAP_FULLMASK;

                if (lightUndoList != null) {
                    for (Integer subPos : lightUndoList) {
                        level.lightMap[subPos] &= lightFlag;
                    }
                }

                lightUndoList.clear();
            }

            if (updateLightMap) {
                level.updateLightMap();
            }
        }
    }

    @Override
    public boolean act() {
        spend_new(target.movementSpeed(), false);
        if (target.pos() != pos) {
            handleArea(Dungeon.level, false, false);
            pos = target.pos();
            handleArea(Dungeon.level, true, true);
        }
        return true;
    }

    private long duration;

    public void ignite(int distance, int registryFlag) {
        ignite(Dungeon.level, distance, registryFlag);
    }

    public void ignite(Level level, int distance, int registryFlag) {
        this.distance = distance;
        this.registryFlag = registryFlag;
        pos = target.pos();

        handleArea(level, true, true);
        Dungeon.observe();
    }

    @Override
    public int icon() {
        return BuffIndicator.LIGHT;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.ILLUMINATED);
        else target.sprite.remove(CharSprite.State.ILLUMINATED);
    }

    @Override
    public String toString() {
        return "Illuminated";
    }

    @Override
    public String desc() {
        return "Even in the Darkest Dungeon, a steady light at your side is always comforting.\n" +
                "\n" +
                "Light helps keep darkness at bay, allowing you to see a reasonable distance despite the environment.\n" +
                "\n" +
                descDuration();//"The light will last for " + dispTurns() + ".";
    }

    protected String descDuration() {
        Item item = Torch.Registry.get(registryFlag);

        if (item != null) {
            return "The light will last as long as the " + item.getName() + " is lit.";
        } else {
            return "You're not sure how long the light will last.";
        }
    }
}
