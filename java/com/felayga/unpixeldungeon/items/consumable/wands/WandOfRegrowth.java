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

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.spellcasting.RegrowthSpellcaster;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class WandOfRegrowth extends Wand {
    private RegrowthSpellcaster regrowthSpellcaster;

    public WandOfRegrowth() {
        super(15);
        name = "Wand of Regrowth";

        price = 150;

        regrowthSpellcaster = new RegrowthSpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfRegrowth.this.wandUsed();
            }
        };
        spellcaster = regrowthSpellcaster;
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(11, 15);
    }


    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(ColorMath.random(0x004400, 0x88CC44));
        particle.am = 1f;
        particle.setLifespan(1.2f);
        particle.setSize(1f, 2f);
        particle.shuffleXY(1f);
        float dst = Random.Float(11f);
        particle.x -= dst;
        particle.y += dst;
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

        regrowthSpellcaster.onZap(user, user.pos(), 24);
    }

    public void explode(Char target, int maxDamage) {
        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Magic, curUser, null);

        regrowthSpellcaster.fxEffect(curUser, null, curUser.pos(), target.pos(), null);
    }

    @Override
    public String desc() {
        return
                "This wand is made from a thin shaft of expertly carved wood. " +
                        "Somehow it is still alive and vibrant, bright green like a young tree's core.\n" +
                        "\n" +
                        "When used, this wand will consume all its charges to blast magical regrowth energy outward " +
                        "in a cone. This magic will cause grass, roots, and rare plants to spring to life.\n" +
                        "\n" +
                        "\"When life ceases new life always begins to grow... The eternal cycle always remains!\"";
    }

}
