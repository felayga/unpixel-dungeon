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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.*;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.Iterator;

public class CursingTrap extends Trap {

	{
		name = "Cursing trap";
		color = TrapSprite.VIOLET;
		shape = TrapSprite.WAVES;
	}

	@Override
	public void activate() {
		if (Dungeon.visible[ pos ]) {
			CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
			Sample.INSTANCE.play(Assets.SND_CURSED);
		}

		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null){
            Iterator<Item> iterator = heap.iterator(false);
			while (iterator.hasNext()){
                Item item = iterator.next();

				if (item.isUpgradable()) {
					item.bucStatus(BUCStatus.Cursed, false);
				}
			}
		}

		if (Dungeon.hero.pos == pos){
			Hero user = Dungeon.hero;

			user.belongings.bucChange(true, BUCStatus.Cursed, true, true, true, false);

			//EquippableItem.equipCursed(user);
			GLog.n("Your worn equipment becomes cursed!");
		}
	}

	@Override
	public String desc() {
		return "This trap contains the same malevolent magic found in cursed equipment. " +
				"Triggering it will curse all worn items, and all items in the immediate area.";
	}
}
