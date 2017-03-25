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
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.blobs.Freezing;
import com.felayga.unpixeldungeon.items.consumable.potions.IAlchemyComponent;
import com.felayga.unpixeldungeon.items.consumable.potions.PotionOfBrewing;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;

public class Icecap extends Plant {
    private static final String TXT_NAME = "Icecap";

	private static final String TXT_DESC =
		"Upon being touched, an Icecap lets out a puff of freezing pollen. " +
		"The freezing effect is much stronger if the environment is wet.";

    public Icecap()
	{
        super(TXT_NAME, 1);
	}
	
	@Override
	public void activate() {
		
		PathFinder.buildDistanceMap( pos, BArray.not( Level.losBlocking, null ), 1 );
		
		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
		
		for (int i=0; i < Level.LENGTH; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Freezing.affect(Char.Registry.get(ownerRegistryIndex()), i, fire );
			}
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
			image = ItemSpriteSheet.SEED_ICECAP;
			
			plantClass = Icecap.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}

    public static class Brew extends PotionOfBrewing {
        {
            plantName = "seed of " + TXT_NAME;

            image = ItemSpriteSheet.ALCHEMY_ICECAP;
        }
    }
}
