package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Ooze;
import com.felayga.unpixeldungeon.actors.mobs.Goo;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.ElmoParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class AcidChance extends MeleeMobAttack {
    public AcidChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if (Random.Int( 3 ) == 0) {
            Buff.affect( target, Ooze.class );
            target.sprite.burst( 0x000000, 5 );
        }

        if (attacker instanceof Goo)
        {
            Goo goo = (Goo)attacker;

            if (goo.pumpedUp > 0) {
                Camera.main.shake(3, 0.2f);
            }
        }

        return damage;
    }

}
