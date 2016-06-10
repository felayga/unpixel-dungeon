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
package com.felayga.unpixeldungeon.actors.buffs.negative;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.text.DecimalFormat;

public class Chill extends FlavourBuff implements ISpeedModifierBuff {

	private static final String TXT_FREEZES = "%s freezes!";

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public boolean attachTo(Char target) {
		//can't chill what's frozen!
		if (target.buff(Frost.class) != null) return false;

		if (super.attachTo(target)){
			Burning.detach(target, Burning.class);

			//chance of potion breaking is the same as speed factor.
			if (Random.PassFail((int)movementModifier())) {
				Item item = target.belongings.randomUnequipped();
				if (item instanceof Potion) {
					item = target.belongings.remove(item, 1);
					GLog.w(TXT_FREEZES, item.toString());
					((Potion) item).shatter(target.pos);
				} /*else if (item instanceof MysteryMeat) {
					item = target.belongings.remove(item, 1);
					FrozenCarpaccio carpaccio = new FrozenCarpaccio();
					if (!target.belongings.collect(carpaccio)) {
						Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
					}
					GLog.w(TXT_FREEZES, item.toString());
				}*/
			}
			return true;
		} else {
			return false;
		}
	}

    @Override
    public long movementModifier() {
        return Math.max(GameTime.TICK, cooldown() / 2);
    }

    @Override
    public long attackModifier() {
        return movementModifier();
    }

	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.CHILLED);
		else target.sprite.remove(CharSprite.State.CHILLED);
	}

	@Override
	public String toString() {
		return "Chilled";
	}

	@Override
	public String desc() {
		return "Not quite frozen, but still much too cold.\n" +
				"\n" +
				"Chilled targets perform all actions more slowly, depending on how many turns are left in the effect. " +
				"At it's worst, this is equivalent to being slowed.\n" +
				"\n" +
				"This chilled will last for " + dispTurns() + ", " +
				"and is currently reducing speed to " + new DecimalFormat("#.##").format((float)GameTime.TICK / movementModifier() * 100.0f) + "%";
	}
}
