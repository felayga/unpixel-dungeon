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
package com.felayga.unpixeldungeon.levels.features;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.potions.IAlchemyComponent;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;

public class AlchemyPot {
	public static Hero hero;
	public static int pos;

	public static Item curItem = null;
	
	public static void operate( Hero hero, int pos ) {
		
		AlchemyPot.hero = hero;
		AlchemyPot.pos = pos;

        GLog.d("AlchemyPot.operate");
        GameScene.selectItem(itemSelector, IAlchemyComponent.class, "Select a brewing component", null);
        /*
		Iterator<Item> items = hero.belongings.iterator();
		foundFruit = false;
		Heap heap = Dungeon.level.heaps.get( pos );

		if (heap == null)
			while (items.hasNext() && !foundFruit){
				curItem = items.next();
				if (curItem instanceof Blandfruit && ((Blandfruit) curItem).potionAttrib == null){
					GameScene.show(
						new WndOptions(TXT_POT, TXT_OPTIONS, TXT_FRUIT, TXT_POTION) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									curItem.cast( AlchemyPot.hero, AlchemyPot.pos );
								} else
									GameScene.selectItem(itemSelector, Plant.Seed.class, TXT_SELECT_SEED);
							}
						}
					);
					foundFruit = true;
				}
			}

		if (!foundFruit)
			GameScene.selectItem(itemSelector, Plant.Seed.class, TXT_SELECT_SEED);
	    */
	}
	
	private static final WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect( Item item ) {
		if (item != null) {
			item.cast( hero, pos );
		}
		}
	};
}