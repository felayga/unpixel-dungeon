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
package com.felayga.unpixeldungeon.actors.buffs;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DeathlySick extends Buff implements Hero.Doom {
    private long roundsLeft;

    public DeathlySick()
    {
        super();

        roundsLeft = GameTime.TICK * Random.Int(10, 20);
    }

    private static final String ROUNDSLEFT = "roundsLeft";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        bundle.put(ROUNDSLEFT, roundsLeft);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        roundsLeft = bundle.getLong(ROUNDSLEFT);
    }

    @Override
    public boolean act() {

        if (target.isAlive()) {
            Hero hero = (Hero)target;

            spend(GameTime.TICK, false);
            roundsLeft -= GameTime.TICK;

            if (roundsLeft <= 0) {
                target.damage(target.HP, this);
                target.sprite.die();
            }
        } else {
            deactivate();
        }

        return true;
    }


    @Override
    public int icon() {
        return BuffIndicator.DEATHLYSICK;
    }

    @Override
    public String toString() {
        return "Deathly sick";
    }

    @Override
    public String desc() {
        String result = "";
		/*
		if (level < STARVING) {
			result = "You can feel your stomach calling out for food, but it's not too urgent yet.\n\n";
		} else {
			result = "You're so hungry it hurts.\n\n";
		}
		*/
        result += "You've eaten something tainted and you are going to die.";

        return result;
    }

    @Override
    public void onDeath() {
        GLog.d("died from tainted food");
    }
}
