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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfPsionicBlast extends Scroll {

    public ScrollOfPsionicBlast()
	{
		name = "Scroll of Psionic Blast";
		initials = "PB";

		bones = true;

        price = 80;
	}
	
	@Override
	protected void doRead() {
		
		GameScene.flash( 0xFFFFFF );
		
		Sample.INSTANCE.play( Assets.SND_BLAST );
		Invisibility.dispel();
		
		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[Dungeon.level.mobs.size()] )) {
			if (Level.fieldOfView[mob.pos]) {
				mob.damage(mob.HT, MagicType.Magic, null );
			}
		}

		curUser.damage(Math.max(curUser.HT/5, curUser.HP/2), MagicType.Magic, null);
		Buff.prolong( curUser, Paralysis.class, Random.Int( 4, 6 ) * GameTime.TICK );
		Buff.prolong( curUser, Blindness.class, Random.Int( 6, 9 ) * GameTime.TICK );
		Dungeon.observe();
		
		setKnown();
		
		curUser.spend_new( TIME_TO_READ, true );

		if (!curUser.isAlive()) {
			Dungeon.fail( Utils.format(ResultDescriptions.ITEM, name ));
			GLog.n("The Psionic Blast tears your mind apart...");
		}
	}
	
	@Override
	public String desc() {
		return
			"This scroll contains destructive energy that can be psionically channeled to tear apart " +
			"the minds of all visible creatures. The power unleashed by the scroll will also temporarily " +
			"blind, stun, and seriously harm the reader.";
	}

}
