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
package com.felayga.unpixeldungeon.actors.mobs.unused;
/*
import java.util.HashSet;

import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.actors.buffs.negative.Chill;
import com.felayga.unpixeldungeon.actors.buffs.negative.Frost;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.consumable.potions.PotionOfLiquidFlame;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfFireblast;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Fire;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.IgniteChance;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.unused.ElementalSprite;
import com.watabou.utils.Random;

public class Elemental extends Mob {

	{
		name = "fire elemental";
		spriteClass = ElementalSprite.class;

		DEXCHA = 25;

		HP = HT = 65;
		defenseSkill = 20;

		EXP = 10;
		maxLvl = 20;
		
		flying = true;
		
		loot = new PotionOfLiquidFlame();
		lootChance = 0.1f;

		belongings.weapon = new IgniteChance(GameTime.TICK, 16, 20);
	}

	
	@Override
	public void add( Buff buff ) {
		if (buff instanceof Burning) {
			if (HP < HT) {
				HP++;
				sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			}
		} else if (buff instanceof Frost || buff instanceof Chill) {
				if (Level.water[this.pos])
					damage( Random.NormalIntRange( HT / 2, HT ), buff );
				else
					damage( Random.NormalIntRange( 1, HT * 2 / 3 ), buff );
		} else {
			super.add( buff );
		}
	}
	
	@Override
	public String description() {
		return
			"Wandering fire elementals are a byproduct of summoning greater entities. " +
			"They are too chaotic in their nature to be controlled by even the most powerful demonologist.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Burning.class );
		IMMUNITIES.add( Fire.class );
		IMMUNITIES.add( WandOfFireblast.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
*/