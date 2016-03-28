package com.felayga.unpixeldungeon.items.weapon.melee.exotic;

import com.felayga.unpixeldungeon.items.weapon.melee.MeleeWeapon;

/**
 * Created by HELLO on 3/14/2016.
 */
public class ExoticMeleeWeapon extends MeleeWeapon {
    public ExoticMeleeWeapon( long delay, int damageMin, int damageMax ) {
        super(delay, damageMin, damageMax);

        skillRequired = 3;
    }
}
