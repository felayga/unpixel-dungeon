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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.spellcasting.VenomSpellcaster;
import com.watabou.utils.Random;

public class WandOfVenom extends Wand {
    public WandOfVenom() {
        super(8);
        name = "Wand of Venom";

        price = 175;

        spellcaster = new VenomSpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfVenom.this.wandUsed();
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
		new Poison().proc(staff, attacker, defender, damage);
	}
	*/

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0x8844FF);
        particle.am = 0.6f;
        particle.setLifespan(0.6f);
        particle.acc.set(0, 40);
        particle.setSize(0f, 3f);
        particle.shuffleXY(2f);
    }

    @Override
    public String desc() {
        return
                "This wand has a purple body which opens to a brilliant green gem. " +
                        "A small amount of foul smelling gas leaks from the gem.\n\n" +
                        "This wand shoots a bolt which explodes into a cloud of vile venomous gas at a targeted location. " +
                        "Anything caught inside this cloud will take continual damage, increasing with time.";
    }
}
