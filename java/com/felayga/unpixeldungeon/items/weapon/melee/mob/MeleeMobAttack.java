package com.felayga.unpixeldungeon.items.weapon.melee.mob;

/**
 * Created by HELLO on 3/9/2016.
 */


import com.felayga.unpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class MeleeMobAttack extends MeleeWeapon {

    public MeleeMobAttack(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax );

        name = "meleemobattack";
        image = ItemSpriteSheet.WEAPON;
        droppable = false;
    }

    @Override
    public String desc() {
        return "THAT'S NOT FOR YOU TO KNOW.";
    }
}
