package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Knuckles;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class DisarmChance extends MeleeMobAttack {
    public static final String TXT_DISARM	= "%s has knocked the %s from your hands!";

    public DisarmChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    public int hitsToDisarm = 0;

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if (target == Dungeon.hero) {

            Hero hero = Dungeon.hero;
            KindOfWeapon weapon = hero.belongings.weapon;

            if (weapon != null && !(weapon instanceof Knuckles) && weapon.bucStatus() != BUCStatus.Cursed) {
                if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(4, 8);

                if (--hitsToDisarm == 0) {
                    hero.belongings.weapon = null;
                    Dungeon.quickslot.clearItem(weapon);
                    weapon.updateQuickslot();
                    Dungeon.level.drop(weapon, hero.pos).sprite.drop();
                    GLog.w(TXT_DISARM, name, weapon.getDisplayName());
                }
            }
        }

        return damage;
    }

}
