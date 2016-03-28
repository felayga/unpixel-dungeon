package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class TauntChance extends MeleeMobAttack {
    public TauntChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if (target instanceof Mob) {
            ((Mob)target).aggro(attacker);
        }

        return damage;
    }
}
