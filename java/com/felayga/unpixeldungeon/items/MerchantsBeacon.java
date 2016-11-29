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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class MerchantsBeacon extends Item {

	private static final String AC_USE = "USE";

    public MerchantsBeacon()
	{
		name = "merchant's beacon";
		image = ItemSpriteSheet.BEACON;

		stackable = true;

		defaultAction = AC_USE;

		bones = true;

        price = 5;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_USE);
		return actions;
	}

	@Override
	public boolean execute(Hero hero, String action) {
		if (action.equals(AC_USE)) {
			hero.belongings.remove(this, 1);
			//Shopkeeper.sell();
			Sample.INSTANCE.play( Assets.SND_BEACON );

			return false;
		} else {
			return super.execute(hero, action);
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return "This odd piece of dwarven technology allows you to communicate from great distances." +
				"\n\nAfter being activated, this beacon will let you sell items to Pixel Mart from anywhere in the dungeon." +
				"\n\nHowever, the magic within the beacon will only last for one session, so use it wisely.";
	}

}
