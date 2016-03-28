package com.felayga.unpixeldungeon.items.armor.boots;

import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

/**
 * Created by HELLO on 3/23/2016.
 */
public class LeatherBoots extends Boots {
    public LeatherBoots() {
        super(0, GameTime.TICK);

        name = "leather boots";
        image = ItemSpriteSheet.BOOTS_LEATHER;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        GLog.d("defaultaction="+defaultAction+" good="+defaultAction.equals(AC_KICK));
    }
}
