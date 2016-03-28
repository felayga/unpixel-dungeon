package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class CrippleChance extends MeleeMobAttack {
    private int chance;
    private long duration;

    public CrippleChance(long delay, int damageMin, int damageMax, int chance, long duration) {
        super(delay, damageMin, damageMax);

        this.chance = chance;
        this.duration = duration;
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if ((chance > 1 && Random.Int(chance) == 0) || chance <= 1) {
            Buff.prolong(target, Cripple.class, duration);
        }

        return damage;
    }
}
