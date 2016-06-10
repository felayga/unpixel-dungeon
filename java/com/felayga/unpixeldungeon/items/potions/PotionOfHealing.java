/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */
package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.negative.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.actors.buffs.negative.Weakness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class PotionOfHealing extends Potion {

    public PotionOfHealing()
	{
		name = "Potion of Healing";
		initials = "He";

		bones = true;
		isHelpful = true;

        overhealAmount = 1;
        applicationDescription = "somewhat";
        price = 30;
	}

    protected int overhealAmount;
    protected String applicationDescription;
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		heal( Dungeon.hero );
        cure( Dungeon.hero );
		GLog.p( "Your wounds heal "+applicationDescription+"." );
	}
	
	public void heal( Hero hero ) {
		if (hero.HP == hero.HT) {
			hero.HT += overhealAmount;
			hero.HP += overhealAmount;
			hero.sprite.showStatus( CharSprite.POSITIVE, "+" + overhealAmount + " ht" );
		}
		else {
			hero.HP = Math.min(hero.HT, hero.HP + healAmount(hero));
		}

	}

    protected int healAmount(Hero hero) {
        return Random.IntRange(1, 4)+Random.IntRange(1, 4)+Random.IntRange(1, 4)+Random.IntRange(1, 4)+Random.IntRange(1, 4)+Random.IntRange(1, 4);
    }

    public void cure( Hero hero ) {
        Buff.detach( hero, Poison.class );
        Buff.detach( hero, Cripple.class );
        Buff.detach(hero, Weakness.class);
        Buff.detach(hero, Bleeding.class);

        hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
    }
	
	@Override
	public String desc() {
		return
			"An elixir that will instantly restore some your health and cure poison.";
	}

}
