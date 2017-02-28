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
package com.felayga.unpixeldungeon.actors.buffs.negative;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.rings.RingOfElements.Resistance;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;

public class Frost extends FlavourBuff {

    private static final String TXT_FREEZES = "%s freezes!";

    private static final long DURATION = GameTime.TICK * 5;

    public Frost()
    {
        type = buffType.NEGATIVE;
    }

    @Override
    public boolean attachTo(Char target, Char source) {
        if (super.attachTo(target, source)) {

            target.paralysed++;
            Buff.detach(target, Burning.class);
            Buff.detach(target, Chill.class);

            Item item = target.belongings.randomUnequipped();
            if (item instanceof Potion) {

                item = target.belongings.remove(item, 1);
                GLog.w(TXT_FREEZES, item.toString());
                ((Potion) item).shatter(source, target.pos());

            } /*else if (item instanceof MysteryMeat) {

				item = target.belongings.remove(item, 1);
				FrozenCarpaccio carpaccio = new FrozenCarpaccio();
				if (!target.belongings.collect(carpaccio)) {
					Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
				}
				GLog.w(TXT_FREEZES, item.toString());

			}*/


            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        super.detach();
        if (target.paralysed > 0) {
            target.paralysed--;
        }
        if (Level.puddle[target.pos()]) {
            Buff.prolong(target, Char.Registry.get(ownerRegistryIndex()), Chill.class, GameTime.TICK * 4);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.FROST;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.FROZEN);
        else target.sprite.remove(CharSprite.State.FROZEN);
    }

    @Override
    public String toString() {
        return "Frozen";
    }

    @Override
    public String desc() {
        return "Not to be confused with freezing solid, this more benign freezing simply encases the target in ice.\n" +
                "\n" +
                "Freezing acts similarly to paralysis, making it impossible for the target to act. " +
                "Unlike paralysis, freezing is immediately cancelled if the target takes damage, as the ice will shatter.\n" +
                "\n" +
                "The freeze will last for " + dispTurns() + ", or until the target takes damage.\n";
    }

    public static long duration(Char ch) {
        Resistance r = ch.buff(Resistance.class);
        return r != null ? r.durationFactor() * DURATION / GameTime.TICK : DURATION;
    }
}
