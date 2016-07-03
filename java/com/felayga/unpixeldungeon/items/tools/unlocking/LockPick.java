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

package com.felayga.unpixeldungeon.items.tools.unlocking;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/14/2016.
 */
public class LockPick extends UnlockingTool {
    public LockPick()
    {
        name = "lockpick";
        image = ItemSpriteSheet.LOCKPICK;
    }

    public boolean unlockDoor(Char user) {
        int testfactor = 72 + user.DEXCHA() * 9;

        if (bucStatus == BUCStatus.Cursed) {
            testfactor /= 2;
        }

        return Random.PassFail(testfactor);
    }

    public boolean unlockChest(Char user) {
        int testfactor = 64 + user.DEXCHA() * 10;

        if (bucStatus == BUCStatus.Cursed) {
            testfactor /= 2;
        }

        return Random.PassFail(testfactor);
    }
}
