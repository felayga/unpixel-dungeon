/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.Hunger;
import com.felayga.unpixeldungeon.actors.buffs.MagicalSleep;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.actors.buffs.Sleep;
import com.felayga.unpixeldungeon.actors.buffs.Vertigo;
import com.felayga.unpixeldungeon.actors.buffs.Weakness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class PotionOfBooze extends Potion {

	{
		name = "Potion of Booze";
		initials = "Bz";

		bones = true;
		isHelpful = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		GLog.p("Oof!  This tastes like liquid fire!");
		((Hunger) hero.buff(Hunger.class)).satisfy(Hunger.STARVING - Hunger.HUNGRY);
		Buff.affect(Dungeon.hero, Vertigo.class, Vertigo.duration(Dungeon.hero));
		switch (Random.Int(5)) {
			case 0:
				Buff.affect(Dungeon.hero, MagicalSleep.class);
				break;
		}
	}
	
	public static void heal( Hero hero ) {
		
		hero.HP = hero.HT;
		Buff.detach( hero, Poison.class );
		Buff.detach( hero, Cripple.class );
		Buff.detach( hero, Weakness.class );
		Buff.detach( hero, Bleeding.class );
		
		hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
	}
	
	@Override
	public String desc() {
		return
			"An elixir that will restore some hunger while making you confused.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
