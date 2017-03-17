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
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.features.Chasm;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.spellcasting.TunnelingSpellcaster;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/25/2017.
 */
public class WandOfTunneling extends Wand {
    TunnelingSpellcaster tunnelingSpellcaster;

    public WandOfTunneling() {
        super(8);
        name = "Wand of Tunneling";

        price = 150;

        tunnelingSpellcaster = new TunnelingSpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfTunneling.this.wandUsed();
            }
        };
        spellcaster = tunnelingSpellcaster;
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
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

        int maxDamage = curCharges() * 4;

        for (Integer offset : Level.NEIGHBOURS8) {
            int pos = user.pos() + offset;
            Char target = Actor.findChar(pos);

            boolean fall = tunnelingSpellcaster.digDown(pos);

            if (target == null) {
                continue;
            }

            explode(target, maxDamage, fall);
        }

        explode(user, maxDamage, false);
    }

    public void explode(Char target, int maxDamage, boolean chasm) {
        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Magic, curUser, null);

        if (chasm) {
            if (target == Dungeon.hero) {
                Chasm.heroFall(target.pos());
            } else if (target instanceof Mob) {
                Chasm.mobFall((Mob) target);
            }
        }
    }

    @Override
    public String desc() {
        return
                "This wand shoots an invisible beam that can tunnel through dungeon walls and floors.";
    }
}

