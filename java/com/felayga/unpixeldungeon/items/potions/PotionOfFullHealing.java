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
package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;

public class PotionOfFullHealing extends PotionOfHealing {

    public PotionOfFullHealing()
	{
		name = "Potion of Full Healing";
		initials = "FHe";

		bones = true;
		isHelpful = true;

		overhealAmount = 4;
		applicationDescription = "completely";

        price = 30;
	}

    @Override
    public void heal(Char hero)
    {
        super.heal(hero);

        if (bucStatus == BUCStatus.Blessed) {
            int oldOverhealAmount = overhealAmount;
            overhealAmount /= 2;
            super.heal(hero);
            overhealAmount = oldOverhealAmount;
        }
    }

	@Override
	protected int healAmount(Char hero) {
        return hero.HT;
    }

    @Override
    protected String descAmount() {
        return "all";
    }

}
