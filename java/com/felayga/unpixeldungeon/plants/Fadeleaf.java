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
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.potions.IAlchemyComponent;
import com.felayga.unpixeldungeon.items.potions.PotionOfBrewing;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class Fadeleaf extends Plant {
    private static final String TXT_NAME = "Fadeleaf";

	private static final String TXT_DESC =
		"Touching a Fadeleaf will temporarily paralyze any creature.";

    public Fadeleaf()
	{
        super(TXT_NAME, 6);
	}
	
	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

        if (ch != null) {
            Buff.prolong( ch, Char.Registry.get(ownerRegistryIndex()), Paralysis.class, GameTime.TICK * EFFECTDURATION );
            if (ch instanceof Mob) {
                if (((Mob)ch).state == ((Mob)ch).HUNTING) ((Mob)ch).state = ((Mob)ch).WANDERING;
                ((Mob)ch).beckon( Dungeon.level.randomDestination() );
            }
        }

        /*
		if (ch == Dungeon.hero) {

			if (ScrollOfTeleportation.canTeleport(ch)) {
				ScrollOfTeleportation.doTeleport(ch, Constant.Position.RANDOM);
			}
			((Hero)ch).curAction = null;
			
		} else if (ch instanceof Mob) {

			int count = 10;
			int newPos;
			do {
				newPos = Dungeon.level.randomRespawnCell();
				if (count-- <= 0) {
					break;
				}
			} while (newPos < 0);
			
			if (newPos != -1 && (Dungeon.level.flags & Level.FLAG_NOTELEPORTATION) == 0) {
				ch.pos(newPos);
				ch.sprite.place( ch.pos() );
				ch.sprite.visible = Dungeon.visible[pos];
				
			}
						
		}
		*/
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
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
			image = ItemSpriteSheet.SEED_FADELEAF;
			
			plantClass = Fadeleaf.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}

    public static class Brew extends PotionOfBrewing {
        {
            plantName = "seed of " + TXT_NAME;

            image = ItemSpriteSheet.ALCHEMY_FADELEAF;
        }
    }
}
