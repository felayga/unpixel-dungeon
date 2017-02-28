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

package com.felayga.unpixeldungeon.items.wands;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/24/2017.
 */
public class WandOfLight extends Wand {
    public WandOfLight() {
        super(15);
        name = "Wand of Light";

        isOffensive = false;
        directionalZap = false;
        usesTargeting = false;

        collisionProperties = Ballistica.Mode.StopTarget;
        price = 100;
        weight(7 * Encumbrance.UNIT);
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(11, 15);
    }

    @Override
    protected void onZap(Ballistica beam) {
        CellEmitter.get(curUser.pos()).start(ShaftParticle.FACTORY, 0.2f, 6);
        for (Integer offset : Level.NEIGHBOURS8) {
            CellEmitter.get(curUser.pos() + offset).start(ShaftParticle.FACTORY, 0.4f, 1);
        }
        Dungeon.level.setLight(curUser.pos(), 5, true);
    }

    /*
    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //nothing
    }
    */

    @Override
    public void explode(Char user) {
        super.explode(user);

        Dungeon.level.setLight(user.pos(), 9, true);

        int maxDamage = curCharges() * 4;

        explode(user, maxDamage);

        for (Integer offset : Level.NEIGHBOURS8) {
            Char target = Dungeon.level.findMob(user.pos() + offset);

            if (target == null) {
                continue;
            }

            explode(target, maxDamage);
        }
    }

    public void explode(Char target, int maxDamage) {
        int blindness = 0;
        for (int n = curCharges(); n >= 0; n--) {
            blindness += Random.IntRange(1, 25);
        }

        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Magic, curUser);
        Buff.prolong(target, curUser, Blindness.class, blindness * GameTime.TICK);
    }

    @Override
    public String desc() {
        return
                "This wand will permanently bathe a wide area in magical light.\n\n" +
                        "Also dispels magical sources of darkness.";
    }
}

