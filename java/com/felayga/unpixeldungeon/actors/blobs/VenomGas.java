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
package com.felayga.unpixeldungeon.actors.blobs;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.effects.BlobEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.watabou.utils.Bundle;

public class VenomGas extends Blob {

	@Override
	protected void evolve() {
        super.evolve();

        if (volume != 0) {
            Char ch;
            for (int i = 0; i < LENGTH; i++) {
                if (cur[i] > 0 && (ch = Actor.findChar(i)) != null) {
                    if (ch.canBreathe()) {
                        //todo: keep venom debuff?
                        //Buff.affect(ch, Venom.class).set(2f, strength);
                    }
                }
            }
        }
    }

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory(Speck.VENOM), 0.6f );
	}

	@Override
	public String tileDesc() {
		return "A cloud of foul acidic venom is swirling here.";
	}
}
