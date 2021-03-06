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
package com.felayga.unpixeldungeon.items.consumable.potions;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.actors.buffs.negative.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.actors.buffs.negative.Weakness;
import com.felayga.unpixeldungeon.actors.buffs.positive.MagicalSleep;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class PotionOfBooze extends Potion {

    public PotionOfBooze()
	{
		name = "Potion of Booze";
		initials = "Bz";

		bones = true;
		isHelpful = true;

        price = 30;
    }
	
	@Override
	public void apply( Char hero ) {
		setKnown();
		GLog.p("Oof!  This tastes like liquid fire!");

        switch(bucStatus()) {
            case Cursed:
                hero.buff(Hunger.class).satisfy_new(125);
                break;
            case Blessed:
                hero.buff(Hunger.class).satisfy_new(375);
                //no more effects
                return;
            default:
                hero.buff(Hunger.class).satisfy_new(250);
                break;
        }

        hero.HP = Math.min(hero.HT, hero.HP + 1);
		Buff.prolong(hero, hero, Vertigo.class, Random.IntRange(3, 8) * GameTime.TICK);

        switch(bucStatus()) {
            case Cursed:
                Buff.affect(hero, hero, MagicalSleep.class);
                break;
            default:
                if (Random.Int(5) == 0) {
                    Buff.affect(hero, hero, MagicalSleep.class);
                }
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
			"A measure of embenzalmine nitrotomine, better known as whiskey.";
	}

}
