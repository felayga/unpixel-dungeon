package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class StealCrippleChance extends StealChance {
    public StealCrippleChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    protected boolean steal( Char attacker, Hero hero ){
        boolean retval = super.steal(attacker, hero);

        if (retval)
        {
            Buff.prolong(hero, Blindness.class, Random.Int(2, 5));
            Buff.affect( hero, Poison.class ).set(Random.Int(5, 7) * Poison.durationFactor(attacker));
            Buff.prolong( hero, Cripple.class, Random.Int( 3, 8 ) );
            Dungeon.observe();
        }

        return retval;
    }
}
