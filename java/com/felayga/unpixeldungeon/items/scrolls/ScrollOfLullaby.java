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
package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Drowsy;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfLullaby extends Scroll {

    public ScrollOfLullaby()
	{
		name = "Scroll of Lullaby";
		initials = "Lu";

        price = 50;
	}
	
	@Override
	protected void doRead() {
		
		Sample.INSTANCE.play( Assets.SND_LULLABY );

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[Dungeon.level.mobs.size()] )) {
            affect(mob, curUser);
		}

        affect(curUser, curUser);

		GLog.i( "The scroll utters a soothing melody. You feel very sleepy." );

		setKnown();
		
		curUser.spend_new( TIME_TO_READ, true );
	}

    public static void affect(Char target, Char source) {
        Buff.affect( target, source, Drowsy.class );
        if (target == Dungeon.hero || Level.fieldOfView[target.pos()]) {
            target.sprite.centerEmitter(-1).start( Speck.factory( Speck.NOTE ), 0.3f, 5 );
        }
    }
	
	@Override
	public String desc() {
		return
			"A soothing melody will lull all who hear it into a deep magical sleep ";
	}

}
