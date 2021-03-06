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
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Roots;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.EarthParticle;
import com.felayga.unpixeldungeon.items.consumable.potions.IAlchemyComponent;
import com.felayga.unpixeldungeon.items.consumable.potions.PotionOfBrewing;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;

public class Earthroot extends Plant {
    private static final String TXT_NAME = "Earthroot";

	private static final String TXT_DESC =
		"When a creature touches an Earthroot, its roots " +
		"entangle the creature, rendering it temporarily immobile.";

    public Earthroot()
	{
        super(TXT_NAME, 5);
	}
	
	@Override
	public void activate() {
        Char ch = Actor.findChar(pos);

        if (ch != null) {
            Buff.prolong(ch, Char.Registry.get(ownerRegistryIndex()), Roots.class, GameTime.TICK * EFFECTDURATION);
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.bottom(pos).start(EarthParticle.FACTORY, 0.05f, 8);
            if (ch == Dungeon.hero) {
                Camera.main.shake(1, 0.4f);
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
			image = ItemSpriteSheet.SEED_EARTHROOT;
			
			plantClass = Earthroot.class;

			bones = true;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}

    /*
	public static class Armor extends Buff {
		
		private static final long STEP = GameTime.TICK;
		
		private int pos;
		private int level;

		{
			type = buffType.POSITIVE;
		}
		
		@Override
		public boolean attachTo( Char target, Char source ) {
			pos = target.pos();
			return super.attachTo( target, source );
		}
		
		@Override
		public boolean act() {
			if (target.pos() != pos) {
				detach();
			}
            spend_new(STEP, false);
			return true;
		}
		
		public int absorb( int damage ) {
			if (level <= damage-damage/2) {
				detach();
				return damage - level;
			} else {
				level -= damage-damage/2;
				return damage/2;
			}
		}
		
		public void level( int value ) {
			if (level < value) {
				level = value;
			}
		}
		
		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}
		
		@Override
		public String toString() {
			return Utils.format("Herbal armor", level);
		}

		@Override
		public String desc() {
			return "A kind of natural, immobile armor is protecting you. " +
					"The armor forms plates of bark and twine, wrapping around your body.\n" +
					"\n" +
					"This herbal armor will absorb 50% of all physical damage you take, " +
					"until it eventually runs out of durability and collapses. The armor is also immobile, " +
					"if you attempt to move it will break apart and be lost.\n" +
					"\n" +
					"The herbal armor can absorb " + level + " more damage before breaking.";
		}

		private static final String POS		= "pos";
		private static final String LEVEL	= "level";
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( POS, pos );
			bundle.put( LEVEL, level );
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			pos = bundle.getInt( POS );
			level = bundle.getInt( LEVEL );
		}
	}
	*/

    public static class Brew extends PotionOfBrewing {
        {
            plantName = "seed of " + TXT_NAME;

            image = ItemSpriteSheet.ALCHEMY_EARTHROOT;
        }
    }
}
