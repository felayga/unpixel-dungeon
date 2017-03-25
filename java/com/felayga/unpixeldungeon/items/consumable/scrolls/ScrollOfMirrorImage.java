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
package com.felayga.unpixeldungeon.items.consumable.scrolls;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.felayga.unpixeldungeon.items.consumable.scrolls.positionscroll.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfMirrorImage extends Scroll {

	private static final int NIMAGES	= 3;
	
	{
		name = "Scroll of Mirror Image";
		initials = "MI";
	}
	
	@Override
	protected void doRead() {
        super.doRead();

		ArrayList<Integer> respawnPoints = new ArrayList<>();
		
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = curUser.pos() + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				respawnPoints.add( p );
			}
		}
		
		int nImages = NIMAGES;
		while (nImages > 0 && respawnPoints.size() > 0) {
			int index = Random.index( respawnPoints );
            int pos = respawnPoints.get(index);
            respawnPoints.remove( index );

			MirrorImage mob = new MirrorImage(curUser.level);
            mob.pos(pos);
			mob.duplicate( curUser );
            GameScene.add( mob );
			ScrollOfTeleportation.appear( mob, pos );

			nImages--;
		}
		
		if (nImages < NIMAGES) {
			setKnown();
		}
	}
	
	@Override
	public String desc() {
		return
			"The incantation on this scroll will create illusionary twins of the reader, which will chase his enemies.";
	}
}
