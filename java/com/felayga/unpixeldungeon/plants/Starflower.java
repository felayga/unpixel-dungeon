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

public class Starflower extends Plant {
    private static final String TXT_NAME = "Starflower";

	private static final String TXT_DESC =
			"A highly coveted plant, " +
			"Starflower is said to restore the mind and body of whomever touches it.";

    public Starflower()
	{
        super(TXT_NAME, 11);
	}

	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

        if (ch != null) {
            //Buff.prolong(ch, Char.Registry.get(ownerRegistryIndex()), Bless.class, GameTime.TICK * 30);

            ch.HP = Math.min(ch.HT, ch.HP + ch.HT / 8);
            ch.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 2);

            ch.MP = Math.min(ch.MT, ch.MP + ch.MT / 8);
            ch.sprite.emitter().start(Speck.factory(Speck.MANAING), 0.4f, 2);
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.get(pos).start( ShaftParticle.FACTORY, 0.2f, 3 );
        }
    }

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed implements IAlchemyComponent {

		{
			plantName = TXT_NAME;

			name = "Seed of " + plantName;
			image = ItemSpriteSheet.SEED_STARFLOWER;

			plantClass = Starflower.class;
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}
	}

    public static class Brew extends PotionOfBrewing {
        {
            plantName = "seed of " + TXT_NAME;

            image = ItemSpriteSheet.ALCHEMY_STARFLOWER;
        }
    }
}
