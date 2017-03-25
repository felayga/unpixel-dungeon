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

package com.felayga.unpixeldungeon.items.consumable.potions;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 11/28/2016.
 */
public class PotionOfBlindness extends Potion {

    public PotionOfBlindness()
    {
        name = "Potion of Blindness";
        initials = "Bl";

        isHarmful = true;
        price = 40;
    }

    @Override
    public void apply( Char hero ) {
        apply(hero, hero, true);
    }

    private void apply(Char hero, Char owner, boolean quaffed) {
        if (quaffed) {
            setKnown();
        }

        int duration = Random.Int(250);
        switch(bucStatus()) {
            case Cursed:
                duration += 375;
                break;
            case Blessed:
                duration += 125;
                break;
            default:
                duration += 250;
                break;
        }
        Buff.prolong(hero, owner, Blindness.class, duration * GameTime.TICK);
    }

    @Override
    public void shatter( Char owner, int cell ) {

        if (Dungeon.visible[cell]) {
            splash(cell);
        }
        if (Dungeon.audible[cell]) {
            Sample.INSTANCE.play( Assets.SND_SHATTER );
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
