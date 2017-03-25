/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * unPixel Dungeon
 * Copyright (C) 2015-2016 Randall Foudray
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.unused;
/*
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Honeypot;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

// Created by HELLO on 3/9/2016.
public class StealChance extends MeleeMobAttack {
    protected static final String TXT_STOLE	= "%s stole %s from you!";

    public StealChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if (target == Dungeon.hero && steal( attacker, (Hero)target )) {
            if (attacker instanceof Thief) {
                Thief thief = (Thief)attacker;
                thief.flee();
            }
        }

        return damage;
    }

    protected boolean steal( Char attacker, Hero hero ) {
        int tries = 8;
        Item item = null;
        while (tries > 0 && item == null) {
            item = hero.belongings.randomUnequipped();
            if (item.unique || (!item.droppable))
            {
                item = null;
            }
            else {
                break;
            }
            tries--;
        }

        if (item != null) {
            if (attacker.belongings.collect(item)) {
                GLog.w(TXT_STOLE, this.name, item.getDisplayName());
                Dungeon.quickslot.clearItem(item);
                item.updateQuickslot();

                //todo: steal honeypot shit if it's worth keeping
                if (item instanceof Honeypot) {
                    Item temp = ((Honeypot) item).shatter(attacker, attacker.pos);
                    item.detach(hero.belongings.backpack);
                    item = temp;
                } else {
                    item = item.detach(hero.belongings.backpack);
                    if (item instanceof Honeypot.ShatteredPot) {
                        ((Honeypot.ShatteredPot) item).setHolder(attacker);
                    }
                }
                return true;
            }
        }

        return false;
    }
}
*/