package com.felayga.unpixeldungeon.items.armor.light;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.mechanics.GameTime;

/**
 * Created by HELLO on 3/14/2016.
 */
public class LightArmor extends Armor {

    public LightArmor(int armor, int armorBonusMaximum, int spellFailure) {
        super(armor, armorBonusMaximum, GameTime.TICK, GameTime.TICK * 2, spellFailure);
    }
}
