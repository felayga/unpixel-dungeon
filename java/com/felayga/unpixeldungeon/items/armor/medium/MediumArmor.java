package com.felayga.unpixeldungeon.items.armor.medium;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.mechanics.GameTime;

/**
 * Created by HELLO on 3/14/2016.
 */
public class MediumArmor extends Armor {

    public MediumArmor(int armor, int armorBonusMaximum, int spellFailure) {
        super(armor, armorBonusMaximum, GameTime.TICK * 3 / 4, GameTime.TICK * 4, spellFailure);
    }
}
