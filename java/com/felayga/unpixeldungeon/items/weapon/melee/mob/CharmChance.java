package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Charm;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class CharmChance extends MeleeMobAttack {
    public CharmChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if (Random.Int( 3 ) == 0) {
            Buff.affect( target, Charm.class, Charm.durationFactor( target ) * Random.LongRange(GameTime.TICK * 3, GameTime.TICK * 7) / GameTime.TICK ).object = attacker.id();
            target.sprite.centerEmitter(-1).start( Speck.factory(Speck.HEART), 0.2f, 5 );
            Sample.INSTANCE.play(Assets.SND_CHARMS);
        }

        return damage;
    }
}
