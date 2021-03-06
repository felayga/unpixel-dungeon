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
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;
import com.felayga.unpixeldungeon.items.consumable.potions.IAlchemyComponent;
import com.felayga.unpixeldungeon.items.consumable.potions.PotionOfBrewing;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class Sungrass extends Plant {
    private static final String TXT_NAME = "Sungrass";

	private static final String TXT_DESC = "Sungrass is renowned for its sap's effective healing properties.";

    public Sungrass()
	{
        super(TXT_NAME, 4);
	}
	
	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

        if (ch != null) {
            ch.HP = Math.min(ch.HT, ch.HP + ch.HT / 8);
            ch.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 2);
        }

        /*
		if (ch == Dungeon.hero) {
			Buff.affect( ch, Health.class ).level = ch.HT;
		}
		*/
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );
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
			image = ItemSpriteSheet.SEED_SUNGRASS;
			
			plantClass = Sungrass.class;

			bones = true;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}

    public static class Brew extends PotionOfBrewing {
        {
            plantName = "seed of " + TXT_NAME;

            image = ItemSpriteSheet.ALCHEMY_SUNGRASS;
        }
    }

    /*
	public static class Health extends Buff {
		
		private static final long STEP = GameTime.TICK;
		
		private int pos;
		private int healCurr = 1;
		private int count = 0;
		private int level;

		{
			type = buffType.POSITIVE;
		}
		
		@Override
		public boolean attachTo( Char target ) {
			pos = target.pos;
			return super.attachTo( target );
		}
		
		@Override
		public boolean act() {
			if (target.pos != pos) {
				detach();
			}
			if (count == 5) {
				if (level <= healCurr*.025*target.HT) {
					target.HP = Math.min(target.HT, target.HP + level);
					target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
					detach();
				} else {
					target.HP = Math.min(target.HT, target.HP+(int)(healCurr*.025*target.HT));
					level -= (healCurr*.025*target.HT);
					if (healCurr < 6)
						healCurr ++;
					target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);

					if (target.HP == target.HT && target == Dungeon.hero) {
						((Hero)target).resting = false;
					}
				}
				count = 1;
			} else {
				count++;
			}
			if (level <= 0)
				detach();
            spend_new( STEP, false );
			return true;
		}

		public int absorb( int damage ) {
			level -= damage;
			if (level <= 0)
				detach();
			return damage;
		}

		public void boost( int amount ){
			level += amount;
		}
		
		@Override
		public int icon() {
			return BuffIndicator.HEALING;
		}
		
		@Override
		public String toString() {
			return "Herbal Healing";
		}

		@Override
		public String desc() {
			return "Sungrass possesses excellent healing properties, though its not as fast as a potion of healing.\n" +
					"\n" +
					"You are current slowly regenerating health from the sungrass plant. " +
					"Taking damage while healing will reduce the healing effectiveness, and moving off the plant will break the healing effect.\n" +
					"\n" +
					"You can heal for " + level + " more health, or until your health is full.";
		}

		private static final String POS	= "pos";
		private static final String HEALCURR = "healCurr";
		private static final String COUNT = "count";
		private static final String LEVEL = "level";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( POS, pos );
			bundle.put( HEALCURR, healCurr);
			bundle.put( COUNT, count);
			bundle.put( LEVEL, level);
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			pos = bundle.getInt( POS );
			healCurr = bundle.getInt( HEALCURR );
			count = bundle.getInt( COUNT );
			level = bundle.getInt( LEVEL );

		}
	}
	*/
}
