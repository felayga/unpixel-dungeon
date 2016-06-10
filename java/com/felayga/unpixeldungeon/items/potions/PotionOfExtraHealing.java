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

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.watabou.utils.Random;

public class PotionOfExtraHealing extends PotionOfHealing {

    public PotionOfExtraHealing()
	{
		name = "Potion of Extra Healing";
		initials = "XHe";

		bones = true;
		isHelpful = true;

		overhealAmount = 2;
        applicationDescription = "significantly";
        price = 30;
	}

	@Override
	public void heal(Hero hero)
	{
		hero.useAttribute(AttributeType.STRCON, 1);
		super.heal(hero);
	}

	@Override
	protected int healAmount(Hero hero) {
		return Random.IntRange(1, 8)+Random.IntRange(1, 8)+Random.IntRange(1, 8)+Random.IntRange(1, 8)+Random.IntRange(1, 8)+Random.IntRange(1, 8);
	}

	@Override
	public String desc() {
		return
			"An elixir that will instantly restore a lot of health and cure poison.";
	}
}
