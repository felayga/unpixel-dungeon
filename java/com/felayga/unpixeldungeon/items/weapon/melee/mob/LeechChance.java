package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.effects.Speck;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class LeechChance extends MeleeMobAttack {
    public LeechChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        int reg = Math.min( damage, attacker.HT - attacker.HP );

        if (reg > 0) {
            attacker.HP += reg;
            attacker.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
        }

        return damage;
    }
}
