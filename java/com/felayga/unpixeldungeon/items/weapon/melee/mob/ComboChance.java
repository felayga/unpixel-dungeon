package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Burning;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class ComboChance extends MeleeMobAttack {
    public ComboChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    public int combo = 0;

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        combo++;
        int effect = Random.Int(4)+combo;

        if (effect > 2) {

            if (effect >=6 && target.buff(Burning.class) == null){

                if (Level.wood[target.pos])
                    GameScene.add(Blob.seed(target.pos, 4, Fire.class));
                Buff.affect( target, Burning.class ).reignite( target );

            } else
                Buff.affect( target, Poison.class).set((effect-2) * Poison.durationFactor(target));

        }

        return damage;
    }
}
