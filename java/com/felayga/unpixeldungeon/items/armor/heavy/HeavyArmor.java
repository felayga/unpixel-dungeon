package com.felayga.unpixeldungeon.items.armor.heavy;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.mechanics.GameTime;

/**
 * Created by HELLO on 3/14/2016.
 */
public class HeavyArmor extends Armor {

    public HeavyArmor(int armor, int armorBonusMaximum, int spellFailure) {
        super(armor, armorBonusMaximum, GameTime.TICK / 2, GameTime.TICK * 8, spellFailure);
    }
}
