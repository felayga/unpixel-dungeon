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

package com.felayga.unpixeldungeon.spellcasting;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HELLO on 3/7/2017.
 */

public abstract class Spellcaster implements Bundlable {
    public int ballisticaMode;

    public final String name;
    public final int level;

    public Spellcaster(String name, int level) {
        this.name = name;
        this.level = level;
    }


    @Override
    public void storeInBundle(Bundle bundle) {
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
    }


    protected int levelBoost;

    public void prepareZap(int levelBoost) {
        this.levelBoost = levelBoost;
    }

    protected int damage(int value) {
        return value * (levelBoost + 10) / 10;
    }

    public abstract void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback);

    public abstract void onZap(Char source, Ballistica path, int targetPos);

    protected Callback buildCallback(final Char source, final Ballistica shot, final int targetPos) {
        return new Callback() {
            @Override
            public void call() {
                onZap(source, shot, targetPos);
                if (!source.isAlive()) {
                    Dungeon.fail("Killed by a " + name + " spell");
                }
            }
        };
    }

    protected Callback buildBounceCallback(final Char source, final Ballistica shot, final List<Integer> bouncesLeft, final Callback callback) {
        return new Callback() {
            @Override
            public void call() {
                int start = bouncesLeft.remove(0);
                int end = bouncesLeft.get(0);

                if (bouncesLeft.size() == 1) {
                    fxEffect(source, shot, start, end, callback);
                } else {
                    fxEffect(source, shot, start, end, this);
                }
            }
        };
    }

    public enum Origin {
        Silent,
        Scroll,
        Spell,
        Wand
    }

    public static void cast(Char source, int target, Spellcaster params, Origin origin) {
        Ballistica shot = null;
        int targetPos;
        if (params.ballisticaMode != Ballistica.Mode.StopSelf.value && source.pos() != target) {
            shot = new Ballistica(source.pos(), target, params.ballisticaMode);
            targetPos = shot.collisionPos;
        } else {
            targetPos = source.pos();
        }

        if ((params.ballisticaMode & Ballistica.Mode.StopChars.value) != 0 && shot != null) {
            //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
            Char charTarget = Actor.findChar(target);
            if (charTarget == null) {
                charTarget = Actor.findChar(shot.collisionPos);
            }

            QuickSlotButton.target(charTarget);
        }

        Callback callback = params.buildCallback(source, shot, targetPos);

        if (shot != null) {
            if (shot.bounces.size() > 0) {
                final List<Integer> bouncesLeft = new ArrayList<>();

                bouncesLeft.add(shot.sourcePos);
                for (int n = 0; n < shot.bounces.size(); n++) {
                    bouncesLeft.add(shot.bounces.get(n));
                }
                bouncesLeft.add(shot.collisionPos);

                Callback subCallback = params.buildBounceCallback(source, shot, bouncesLeft, callback);

                subCallback.call();
            } else {
                params.fxEffect(source, shot, source.pos(), shot.collisionPos, callback);
            }
        } else {
            callback.call();
        }

        switch (origin) {
            case Scroll:
                Sample.INSTANCE.play(Assets.SND_READ);
                break;
            case Spell:
                Sample.INSTANCE.play(Assets.SND_ZAP);
                break;
            case Wand:
                Sample.INSTANCE.play(Assets.SND_ZAP);
                break;
            default:
                //nothing
                break;
        }
    }
}
