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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.MindVision;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/4/2017.
 */
public class PotionOfMonsterDetection extends Potion {

    public PotionOfMonsterDetection()
    {
        name = "Potion of Monster Detection";
        initials = "MD";

        isHelpful = true;

        price = 35;
    }

    @Override
    public void apply( Char hero ) {
        setKnown();

        long duration = 20;
        switch(bucStatus) {
            case Blessed:
                duration += Random.Int(40);
                break;
            case Cursed:
                int pos = hero.pos();

                for (Mob mob : Dungeon.level.mobs) {
                    if ((mob.characteristics & Characteristic.Brainless.value) == 0) {
                        mob.beckon(pos);
                    }
                }

                CellEmitter.center( pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
                GLog.n("Monsters sense your presence!");

                //no break
            default:
                duration /= 4;
                break;
        }

        Buff.prolong(hero, hero, MindVision.class, duration * GameTime.TICK);
        Dungeon.observe();

        if (Dungeon.level.mobs.size() > 0) {
            GLog.i("You can somehow feel the presence of other creatures' minds!");
        } else {
            GLog.i( "You can somehow tell that you are alone on this level at the moment." );
        }
    }

    @Override
    public String desc() {
        return
                "After drinking this, your mind will become attuned to the psychic signature " +
                        "of distant creatures, enabling you to sense biological presences through walls. " +
                        "Also this potion will permit you to see through nearby walls and doors.";
    }

}
