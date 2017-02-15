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
package com.felayga.unpixeldungeon.plants;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.food.Blandfruit;
import com.felayga.unpixeldungeon.items.potions.PotionOfBrewing;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class BlandfruitBush extends Plant {
    private static final String TXT_NAME = "Blandfruit";

	private static final String TXT_DESC =
			"Distant cousin of the Rotberry, the pear-shaped produce of the Blandfruit bush tastes like caked dust. " +
			"The fruit is gross and unsubstantial but isn't poisonous. perhaps it could be cooked.";

    public BlandfruitBush()
	{
        super(TXT_NAME, 8);
	}

	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

		Dungeon.level.drop( new Blandfruit(), pos ).sprite.drop();
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = TXT_NAME;

			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_BLANDFRUIT;

			plantClass = BlandfruitBush.class;
		}

        @Override
        public ArrayList<String> actions(Hero hero) {
            ArrayList<String> retval = super.actions(hero);
            retval.remove(Constant.Action.BREW);
            return retval;
        }

        @Override
		public String desc() {
			return TXT_DESC;
		}
	}

    public static class Brew extends PotionOfBrewing {
        {
            plantName = TXT_NAME;

            image = ItemSpriteSheet.ALCHEMY_BLANDFRUIT;
        }
    }

}
