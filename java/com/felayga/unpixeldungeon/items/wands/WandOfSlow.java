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

package com.felayga.unpixeldungeon.items.wands;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Slow;
import com.felayga.unpixeldungeon.actors.buffs.positive.Haste;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/26/2017.
 */
public class WandOfSlow extends Wand {

    public WandOfSlow() {
        super(8);
        name = "Wand of Slow";

        canTargetSelf = true;
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }

    @Override
    protected void fxEffect(Ballistica bolt, Callback callback) {
        MagicMissile.slowness(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
    }

    @Override
    protected void onZap(Ballistica bolt) {
        Char ch;

        if (bolt != null) {
            ch = Actor.findChar(bolt.collisionPos);
        } else {
            ch = curUser;
        }

        if (ch != null) {
            Buff.detach(ch, Haste.Intrinsic.class);
            Buff.affect(ch, curUser, Slow.Intrinsic.class);

            ch.sprite.burst(0xFFFF5533, 2);
        }
    }

    @Override
    public String desc() {
        return
                "This wand launches a bolt which permanently slows down its target.";
    }
}


