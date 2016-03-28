/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.watabou.noosa.audio.Sample;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Invisibility;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class ScrollOfMagicMapping extends Scroll {

	private static final String TXT_LAYOUT = "A map coalesces in your mind!";
	private static final String TXT_LAYOUT_CURSED = "Unfortunately, you can't grasp the details.";

	{
		name = "Scroll of Magic Mapping";
		initials = "MM";
	}

	private boolean discoverCell(int index, boolean allowDiscovery) {
		int terr = Dungeon.level.map[index];

		if (Level.discoverable[index]) {

			Dungeon.level.mapped[index] = true;

			if (allowDiscovery && (Terrain.flags[terr] & Terrain.FLAG_SECRET) != 0) {

				Dungeon.level.discover(index);

				if (Dungeon.visible[index]) {
					GameScene.discoverTile(index, terr);
					discover(index);

					return true;
				}
			}
		}

		return false;
	}
	
	@Override
	protected void doRead() {
		
		int length = Level.LENGTH;
		
		boolean noticed = false;


		if (bucStatus == BUCStatus.Cursed) {
			for (int i=0;i<length/14;i++) {
				int index = Random.Int(Level.WIDTH - 1) + Random.Int(Level.HEIGHT - 1) * Level.WIDTH;
				noticed |= discoverCell(index, false);
				noticed |= discoverCell(index+1, false);
				noticed |= discoverCell(index+Level.WIDTH, false);
				noticed |= discoverCell(index+Level.WIDTH+1, false);
			}

			GLog.n(TXT_LAYOUT + " " + TXT_LAYOUT_CURSED);
		}
		else if (bucStatus == BUCStatus.Blessed) {
			for (int i = 0; i < length; i++) {
				noticed |= discoverCell(i, true);
			}

			GLog.i( TXT_LAYOUT );
		}
		else {
			for (int i = 0; i < length; i++) {
				noticed |= discoverCell(i, false);
			}

			GLog.i( TXT_LAYOUT );
		}

		Dungeon.observe();
		
		if (noticed) {
			Sample.INSTANCE.play( Assets.SND_SECRET );
		}
		
		SpellSprite.show( curUser, SpellSprite.MAP );
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		setKnown();
		
		curUser.spend( TIME_TO_READ, true );
	}
	
	@Override
	public String desc() {
		return
			"When this scroll is read, an image of crystal clarity will be etched into your memory, " +
			"alerting you to the precise layout of the level and revealing all hidden secrets. " +
			"The locations of items and creatures will remain unknown.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 25 * quantity : super.price();
	}
	
	public static void discover( int cell ) {
		CellEmitter.get( cell ).start( Speck.factory( Speck.DISCOVER ), 0.1f, 4 );
	}
}
