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

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.utils.GLog;

public class PotionOfStrength extends Potion {

    public PotionOfStrength()
	{
		name = "Potion of Strength";
		initials = "St";

		bones = true;
		isHelpful = true;

        price = 100;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();

		/*
		hero.STR++;
		hero.sprite.showStatus( CharSprite.POSITIVE, "+1 str" );
		GLog.p( "Newfound strength surges through your body." );
		*/

		GLog.p("Nope.");

		Badges.validateStrengthAttained();
	}
	
	@Override
	public String desc() {
		return
			"This powerful liquid will course through your muscles, " +
			"permanently increasing your strength by one point.";
	}

}
