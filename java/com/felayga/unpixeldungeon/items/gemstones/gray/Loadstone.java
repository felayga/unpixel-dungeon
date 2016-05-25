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

package com.felayga.unpixeldungeon.items.gemstones.gray;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.gemstones.Gemstone;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.sprites.MissileSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

/**
 * Created by HELLO on 5/24/2016.
 */
public class Loadstone extends Gemstone {
    public Loadstone() {
        super(1, true, GemstoneColor.Gray);
        weight(Encumbrance.UNIT * 500);
        hasBuc(true);
        bucStatus(BUCStatus.Cursed, false);
    }

    @Override
    protected void buildName() {
        name = "loadstone";
    }

    @Override
    public void doDrop(final Hero hero) {
        if (bucStatus == BUCStatus.Cursed) {
            if (!bucStatusKnown) {
                bucStatus(true);
            }

            GLog.n("You can't drop the " + getDisplayName() + "!");
        } else {
            super.doDrop(hero);
        }
    }

    @Override
    protected void onThrow(int cell, Char thrower) {
        if (bucStatus == BUCStatus.Cursed) {
            if (!bucStatusKnown) {
                bucStatus(true);
            }

            ((MissileSprite) thrower.sprite.parent.recycle(MissileSprite.class)).reset(cell, thrower.pos, this, null);
            GLog.n("The " + getDisplayName()+" finds its way back into your backpack!");
            thrower.belongings.collect(this);
        } else {
            super.onThrow(cell, thrower);
        }
    }

}
