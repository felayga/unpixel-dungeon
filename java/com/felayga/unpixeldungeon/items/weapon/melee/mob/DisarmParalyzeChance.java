package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Paralysis;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/10/2016.
 */
public class DisarmParalyzeChance extends DisarmChance {
    public DisarmParalyzeChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    public int hitsToDisarm = 0;

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if (Random.Int(10) == 0) {
            Buff.prolong(target, Paralysis.class, GameTime.TICK * 11 / 10);
        }

        return damage;
    }}
