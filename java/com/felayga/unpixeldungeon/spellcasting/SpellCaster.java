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
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HELLO on 3/7/2017.
 */

public class SpellCaster {
    private static List<Integer> bouncesLeft = new ArrayList<>();

    public static void cast(Char user, int target, int ballisticaMode, final ISpellCast params) {
        final Ballistica shot = ballisticaMode == Ballistica.Mode.StopSelf.value || user.pos() == target ? null : new Ballistica(user.pos(), target, ballisticaMode);

        if ((ballisticaMode & Ballistica.Mode.StopChars.value) != 0 && shot != null) {
            //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
            Char charTarget = Actor.findChar(target);
            if (charTarget == null) {
                charTarget = Actor.findChar(shot.collisionPos);
            }

            QuickSlotButton.target(charTarget);
        }

        final Callback callback = new Callback() {
            @Override
            public void call() {
                params.onZap(shot);
            }
        };

        if (shot != null) {
            if (shot.bounces.size() > 0) {
                bouncesLeft.clear();

                bouncesLeft.add(shot.sourcePos);
                for (int n = 0; n < shot.bounces.size(); n++) {
                    bouncesLeft.add(shot.bounces.get(n));
                }
                bouncesLeft.add(shot.collisionPos);

                final Callback subCallback = new Callback() {
                    @Override
                    public void call() {
                        int start = bouncesLeft.remove(0);
                        int end = bouncesLeft.get(0);

                        if (bouncesLeft.size() == 1) {
                            params.fxEffect(start, end, callback);
                        } else {
                            params.fxEffect(start, end, this);
                        }
                    }
                };

                subCallback.call();
            } else {
                params.fxEffect(shot.sourcePos, shot.collisionPos, callback);
            }
        } else {
            callback.call();
        }
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }
}
