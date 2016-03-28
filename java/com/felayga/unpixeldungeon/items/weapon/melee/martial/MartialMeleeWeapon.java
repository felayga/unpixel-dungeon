package com.felayga.unpixeldungeon.items.weapon.melee.martial;

import com.felayga.unpixeldungeon.items.weapon.melee.MeleeWeapon;

/**
 * Created by HELLO on 3/14/2016.
 */
public class MartialMeleeWeapon extends MeleeWeapon {
    public MartialMeleeWeapon( long delay, int damageMin, int damageMax ) {
        super(delay, damageMin, damageMax);

        skillRequired = 2;
    }
}
