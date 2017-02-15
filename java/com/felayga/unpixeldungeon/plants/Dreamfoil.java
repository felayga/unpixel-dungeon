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

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.MagicalSleep;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.potions.IAlchemyComponent;
import com.felayga.unpixeldungeon.items.potions.PotionOfBrewing;
import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;

public class Dreamfoil extends Plant {
    private static final String TXT_NAME = "Dreamfoil";

	private static final String TXT_DESC =
			"The Dreamfoil's prickly flowers contain a chemical which is known for its " +
			"properties as a strong neutralizing agent. Most weaker creatures are overwhelmed " +
			"and knocked unconscious, which gives the plant its namesake.";

    public Dreamfoil()
	{
        super(TXT_NAME, 10);
	}

	@Override
	public void activate() {
        Char ch = Actor.findChar(pos);

        if (ch != null) {
            PotionOfHealing.cure(ch);

            if (ch instanceof Hero) {
                GLog.i("You feel refreshed.");
            }

            Buff.affect(ch, Char.Registry.get(ownerRegistryIndex()), MagicalSleep.class);
        }
    }

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed implements IAlchemyComponent {
		{
			plantName = TXT_NAME;

			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_DREAMFOIL;

			plantClass = Dreamfoil.class;
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}
	}

    public static class Brew extends PotionOfBrewing {
        {
            plantName = "seed of " + TXT_NAME;

            image = ItemSpriteSheet.ALCHEMY_DREAMFOIL;
        }
    }
}
