package com.felayga.unpixeldungeon.items.tools.unlocking;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/14/2016.
 */
public class SkeletonKey extends UnlockingTool {
    public SkeletonKey()
    {
        name = "skeleton key";
        image = ItemSpriteSheet.IRON_KEY;
    }

    public boolean unlockDoor(Char user) {
        int testfactor = 176 + user.DEXCHA * 3;

        if (bucStatus == BUCStatus.Cursed) {
            testfactor /= 2;
        }

        return Random.PassFail(testfactor);
    }

    public boolean unlockChest(Char user) {
        int testfactor = 192 + user.DEXCHA * 3;

        if (bucStatus == BUCStatus.Cursed) {
            testfactor /= 2;
        }

        return Random.PassFail(testfactor);
    }
}
