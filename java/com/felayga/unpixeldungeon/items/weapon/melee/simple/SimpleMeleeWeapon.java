package com.felayga.unpixeldungeon.items.weapon.melee.simple;

import com.felayga.unpixeldungeon.items.weapon.melee.MeleeWeapon;

/**
 * Created by HELLO on 3/14/2016.
 */
public class SimpleMeleeWeapon extends MeleeWeapon {
    public SimpleMeleeWeapon( long delay, int damageMin, int damageMax ) {
        super(delay, damageMin, damageMax);

        skillRequired = 1;
    }
}
