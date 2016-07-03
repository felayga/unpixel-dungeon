/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */
package com.felayga.unpixeldungeon.items.scrolls;

//import com.felayga.unpixeldungeon.actors.mobs.Mimic;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Amok;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRage extends Scroll {

	{
		name = "Scroll of Rage";
		initials = "Ra";
	}

    public static int enrage(int pos, int range) {
        int retval = 0;

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[Dungeon.level.mobs.size()] )) {
            if (Level.distance(mob.pos, pos) <= range) {
                mob.beckon(pos);
                if (Level.fieldOfView[mob.pos]) {
                    Buff.prolong(mob, Amok.class, GameTime.TICK * 5);
                }
            }
            retval++;
        }

        return retval;
    }
	
	@Override
	protected void doRead() {
        enrage(curUser.pos, Math.max(Dungeon.level.WIDTH, Dungeon.level.HEIGHT));

		GLog.w( "The scroll emits an enraging roar that echoes throughout the dungeon!" );
		setKnown();
		
		curUser.sprite.centerEmitter(-1).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
		Sample.INSTANCE.play( Assets.SND_CHALLENGE );
		Invisibility.dispel();
		
		curUser.spend_new( TIME_TO_READ, true );
	}
	
	@Override
	public String desc() {
		return
			"When read aloud, this scroll will unleash a great roar " +
			"that draws all enemies to the reader, and enrages nearby ones.";
	}
}
