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

package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 11/29/2016.
 */
public class PotionOfConfusion extends Potion {

    public PotionOfConfusion() {
        name = "Potion of Confusion";
        initials = "Co";

        isHarmful = true;
        price = 40;
    }

    @Override
    public void apply(Char hero) {
        apply(hero, hero, true);
    }

    private void apply(Char hero, Char source, boolean quaffed) {
        setKnown();

        int duration = Random.IntRange(1, 8);

        if (quaffed) {
            switch (bucStatus()) {
                case Cursed:
                    duration += 24;
                    break;
                case Blessed:
                    duration += 8;
                    break;
                default:
                    duration += 16;
                    break;
            }
        } else {
            duration++;
        }
        Buff.prolong(hero, source, Vertigo.class, duration * GameTime.TICK);
    }

    @Override
    public void shatter(Char owner, int cell) {
        if (Dungeon.visible[cell]) {
            splash(cell);
        }
        if (Dungeon.audible[cell]) {
            Sample.INSTANCE.play(Assets.SND_SHATTER);
        }

        Char test = Actor.findChar(cell);

        if (test != null) {
            apply(test, owner, false);
        }
    }

    @Override
    public String desc() {
        return
                "This flask contains a substance which will temporarily blind anything it comes into contact with.";
    }

}
