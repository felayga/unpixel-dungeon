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
        int testfactor = 72 + user.DEXCHA * 9;

        if (bucStatus == BUCStatus.Cursed) {
            testfactor /= 2;
        }

        return Random.PassFail(testfactor);
    }

    public boolean unlockChest(Char user) {
        int testfactor = 64 + user.DEXCHA * 10;

        if (bucStatus == BUCStatus.Cursed) {
            testfactor /= 2;
        }

        return Random.PassFail(testfactor);
    }
}
