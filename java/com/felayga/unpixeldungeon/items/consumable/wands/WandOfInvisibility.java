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

package com.felayga.unpixeldungeon.items.consumable.wands;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.spellcasting.InvisibilitySpellcaster;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/25/2017.
 */
public class WandOfInvisibility extends Wand {

    public WandOfInvisibility() {
        super(8);
        name = "Wand of Invisibility";

        price = 150;

        spellcaster = new InvisibilitySpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfInvisibility.this.wandUsed();
            }
        };
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }

    @Override
    public void explode(Char user) {
        super.explode(user);

        int maxDamage = curCharges() * 4;

        for (Integer offset : Level.NEIGHBOURS8) {
            int pos = user.pos() + offset;
            Char target = Actor.findChar(pos);

            if (target == null) {
                continue;
            }

            explode(target, maxDamage);
        }

        explode(user, maxDamage);
    }

    public void explode(Char target, int maxDamage) {
        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Magic, curUser, null);

        if (Random.Int(10) == 0) {
            if (target == Dungeon.hero) {
                Buff.affect(target, curUser, Invisibility.Intrinsic.class);
            } else {
                Buff.affect(target, curUser, Invisibility.Improved.class, Random.IntRange(curCharges(), curCharges() * 125) * GameTime.TICK);
            }
        } else {
            if (target == Dungeon.hero) {
                Buff.affect(target, curUser, Invisibility.class, Random.IntRange(curCharges(), curCharges() * 250) * GameTime.TICK);
            } else {
                Buff.affect(target, curUser, Invisibility.class, Random.IntRange(curCharges(), curCharges() * 125) * GameTime.TICK);
            }
        }

        target.sprite.burst(0xFFBCD3EB, 2);
    }

    /*
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//gain 1 turn of recharging buff per level of the wand.
		if (level() > 0) {
			Buff.prolong( attacker, attacker, ScrollOfRecharging.Recharging.class, GameTime.TICK * staff.level());
			SpellSprite.show(attacker, SpellSprite.CHARGE);
		}
	}
	*/

    @Override
    public String desc() {
        return "This wand launches a bolt which will render its target invisible to the naked eye.";
    }
}


