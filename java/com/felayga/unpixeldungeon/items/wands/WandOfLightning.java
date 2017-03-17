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

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.spellcasting.LightningSpellcaster;
import com.watabou.utils.Random;

public class WandOfLightning extends Wand {
    public WandOfLightning() {
        super(8);
        name = "Wand of Lightning";

        price = 175;

        spellcaster = new LightningSpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfLightning.this.wandUsed();
            }
        };
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }

    /*
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//acts like shocking enchantment
		new Shock().proc(staff, attacker, defender, damage);
	}
	*/

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0xFFFFFF);
		particle.am = 0.6f;
		particle.setLifespan(0.6f);
		particle.acc.set(0, +10);
		particle.speed.polar(-Random.Float(3.1415926f), 6f);
		particle.setSize(0f, 1.5f);
		particle.sizeJitter = 1f;
		particle.shuffleXY(2f);
		float dst = Random.Float(2f);
		particle.x -= dst;
		particle.y += dst;
	}

    @Override
    public void explode(Char user) {
        super.explode(user);

        int maxDamage = curCharges() * 16;

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

        target.damage(damage, MagicType.Shock, curUser, null);

        spellcaster.fxEffect(curUser, null, curUser.pos(), target.pos(), null);
        spellcaster.onZap(curUser, null, target.pos());
    }

	@Override
	public String desc() {
		return
			"This wand is made out of solid metal, making it surprisingly heavy. " +
			"Two prongs curve together at the top, and electricity arcs between them.\n\n" +
			"This wand sends powerful lightning arcing through whatever it is shot at. " +
			"This electricity can bounce between many adjacent foes, and is more powerful in water. " +
			"If you're too close, you may get shocked as well.";
	}
}
