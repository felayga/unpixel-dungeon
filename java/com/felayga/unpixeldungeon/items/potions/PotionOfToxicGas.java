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
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.ToxicGas;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class PotionOfToxicGas extends Potion {

    public PotionOfToxicGas() {
        name = "Potion of Toxic Gas";
        initials = "TG";

        isHarmful = true;

        price = 40;
    }

    @Override
    public void shatter(Char source, int cell) {

        if (Dungeon.visible[cell]) {
            setKnown();

            splash(cell);
        }
        if (Dungeon.audible[cell]) {
            Sample.INSTANCE.play(Assets.SND_SHATTER);
        }

        GameScene.add(Blob.seed(source, cell, 1000, ToxicGas.class));
    }

    @Override
    public String desc() {
        return
                "Uncorking or shattering this pressurized glass will cause " +
                        "its contents to explode into a deadly cloud of toxic green gas. " +
                        "You might choose to fling this potion at distant enemies " +
                        "instead of uncorking it by hand.";
    }
}
