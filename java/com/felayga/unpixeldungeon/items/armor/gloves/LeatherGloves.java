package com.felayga.unpixeldungeon.items.armor.gloves;

import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

/**
 * Created by HELLO on 3/23/2016.
 */
public class LeatherGloves extends Gloves {
    public LeatherGloves() {
        super(0, GameTime.TICK);

        name = "leather gloves";
        image = ItemSpriteSheet.GLOVES_LEATHER;
    }

}
