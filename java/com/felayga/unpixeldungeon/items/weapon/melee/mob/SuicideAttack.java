package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Poison;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Spinner;
import com.felayga.unpixeldungeon.actors.mobs.Thief;
import com.felayga.unpixeldungeon.items.Honeypot;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/11/2016.
 */
public class SuicideAttack extends MeleeMobAttack {

    public SuicideAttack(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        attacker.destroy();
        attacker.sprite.die();

        return damage;
    }

}
