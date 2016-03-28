package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Burning;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class IgniteChance extends MeleeMobAttack {
    public IgniteChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if (Random.Int( 2 ) == 0) {
            Buff.affect( target, Burning.class ).reignite(target);
        }

        return damage;
    }
}
