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
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.ShadowCaster;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.nio.Buffer;
import java.util.Arrays;
import java.util.List;

public class Light extends FlavourBuff {
    public static class Registry {
        private static Light[] occupado = new Light[13 + 8];

        public static void register(Light what) {
            if (what.registryFlag != 0) {
                GLog.d("tried to register already-registered light=" + what.registryFlag);
                return;
            }

            int index = -1;
            for (int n = 0; n < occupado.length; n++) {
                if (occupado[n] == null) {
                    index = n;
                    break;
                }
            }

            if (index < 0) {
                GLog.d("tried to register too many lights, failing");
                GLog.d("" + 1 / 0);
            } else {
                GLog.d("register light with index=" + index);
            }

            occupado[index] = what;

            what.registryFlag = 1 << (index + 3);
        }

        public static void unregister(Light what) {
            if (what.registryFlag == 0) {
                GLog.d("tried to unregister not-registered light");
                return;
            }

            int index = lazylog2(what.registryFlag) - 3;

            if (occupado[index] != what) {
                GLog.d("tried to unregister improperly registered light (expected light with index=" + index + ", " + "found index=" + (occupado[index] != null ? (lazylog2(occupado[index].registryFlag) - 3) + "" : "<null>"));
                return;
            }
            occupado[index] = null;

            GLog.d("unregister light with index=" + index);

            what.registryFlag = 0;
        }

        private static int lazylog2(int value) {
            return (31 - Integer.numberOfLeadingZeros(value));
        }

        public static void register(Level level) {
            GLog.d("register lights in level");
            for (int n = 0; n < Level.LENGTH; n++) {
                level.lightMap[n] &= Level.LIGHTMAP_MOBILEMASK;
            }

            GLog.d("search hero");
            for (Buff buff : Dungeon.hero.buffs()) {
                if (buff instanceof Light) {
                    GLog.d("found light");
                    Light light = (Light)buff;

                    register(light);
                    GLog.d("registered to flag="+light.registryFlag);
                    light.pos = light.target.pos();
                    light.handleArea(level, true, false);
                }
            }

            /*
            for (Mob mob : level.mobs) {
                for (Buff buff : mob.buffs()) {
                    if (buff instanceof Light) {

                    }
                }
            }
            */
        }

        public static void unregister(Level level) {
            for (int n = 0; n < occupado.length; n++) {
                if (occupado[n] == null) {
                    continue;
                }

                unregister(occupado[n]);
            }
        }
    }

    private int distance;

    public int distance() {
        return distance;
    }

    private int registryFlag;
    private int pos;

    public Light() {
        distance = 4;
        registryFlag = 0;
    }

    private static final String LIGHTDISTANCE = "lightDistance";
    private static final String LIGHTDURATION = "lightDuration";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(LIGHTDISTANCE, distance);
        bundle.put(LIGHTDURATION, duration);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        distance = bundle.getInt(LIGHTDISTANCE);
        duration = bundle.getLong(LIGHTDURATION);
    }

    @Override
    public boolean attachTo(Char target, Char source) {
        if (super.attachTo(target, source)) {
            Registry.register(this);

            pos = target.pos();

            handleArea(Dungeon.level, true, true);
            Dungeon.observe();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        handleArea(Dungeon.level, false, true);
        Dungeon.observe();

        Registry.unregister(this);
        super.detach();
    }

    private List<Integer> lightUndoList;

    private void handleArea(Level level, boolean on, boolean updateLightMap) {
        if (level != null) {
            if (on) {
                lightUndoList = ShadowCaster.castLight(pos % Level.WIDTH, pos / Level.WIDTH, level, distance, registryFlag);
            } else {
                int lightFlag = this.registryFlag ^ Level.LIGHTMAP_FULLMASK;

                if (lightUndoList != null) {
                    for (Integer pos : lightUndoList) {
                        level.lightMap[pos] &= lightFlag;
                    }
                }
            }

            if (updateLightMap) {
                level.updateLightMap();
            }
        }
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            duration -= GameTime.TICK;
            spend_new(GameTime.TICK, false);

            if (duration >= 0) {
                update(Dungeon.level);
            } else {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    protected void update(Level level) {
        if (target.pos() != pos) {
            handleArea(level, false, false);
            pos = target.pos();
            handleArea(level, true, true);
        }
    }

    private long duration;

    public void ignite(long time) {
        duration = time;
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
                "The light will last for " + dispTurns() + ".";
    }
}
