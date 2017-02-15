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
package com.felayga.unpixeldungeon.sprites.mobs.unused;

import com.felayga.unpixeldungeon.items.weapon.missiles.simple.CurareDart;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.sprites.MissileSprite;
import com.watabou.utils.Callback;

public class GnollTricksterSprite extends GnollSprite {

    private Animation cast;

    public GnollTricksterSprite() {
        super(1);

        cast = attack.clone();
    }

    @Override
    public void attack(int cell) {
        if (!Level.canReach(cell, ch.pos())) {

            ((MissileSprite) parent.recycle(MissileSprite.class)).
                    reset(ch.pos(), cell, new CurareDart(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    });

            play(cast);
            turnTo(ch.pos(), cell);

        } else {

            super.attack(cell);

        }
    }
}
