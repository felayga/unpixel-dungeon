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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Knuckles;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class DisarmingTrap extends Trap{

	{
		name = "Disarming trap";
		color = TrapSprite.RED;
		shape = TrapSprite.LARGE_DOT;
	}

	@Override
	public void activate() {
		Heap heap = Dungeon.level.heaps.get( pos );

		if (heap != null) {
            int cell = Dungeon.level.randomRespawnCell();

            Item item = heap.pickUp();
            Dungeon.level.drop(item, cell).seen = true;
            for (int i : Level.NEIGHBOURS9)
                Dungeon.level.visited[cell + i] = true;
            Dungeon.observe();

            Sample.INSTANCE.play(Assets.SND_TELEPORT);
            CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
        }

		if (Dungeon.hero.pos() == pos){
			Hero hero = Dungeon.hero;
            Weapon weapon = (Weapon)hero.belongings.weapon();

			if (weapon != null && !(weapon instanceof Knuckles) && weapon.bucStatus() != BUCStatus.Cursed) {

				int cell = Dungeon.level.randomRespawnCell();
				if (cell != -1) {
                    hero.belongings.unequip(weapon, false);
					Dungeon.quickslot.clearItem(weapon);
					weapon.updateQuickslot();

					Dungeon.level.drop(weapon, cell).seen = true;
					for (int i : Level.NEIGHBOURS9)
						Dungeon.level.visited[cell+i] = true;
					Dungeon.observe();

					GLog.w("Your weapon is teleported away!");

					Sample.INSTANCE.play(Assets.SND_TELEPORT);
					CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);

				}

			}
		}
	}

	@Override
	public String desc() {
		return "This trap contains very specific teleportation magic, which will warp the weapon of its victim to some other location.";
	}
}
