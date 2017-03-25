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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.spellcasting.BlastWaveSpellcaster;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfBlastWave extends Wand {

    public WandOfBlastWave()
	{
        super(8);

		name = "Wand of Blast Wave";

        price = 150;

        spellcaster = new BlastWaveSpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfBlastWave.this.wandUsed();
            }
        };
	}



    /*
	@Override
	//a weaker knockback, not dissimilar to the glyph of bounce, but a fair bit stronger.
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		int level = Math.max(0, staff.level);

		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		if (Random.Int( level + 4 ) >= 3){
			int oppositeHero = defender.pos + (defender.pos - attacker.pos);
			Ballistica trajectory = new Ballistica(defender.pos, oppositeHero, Ballistica.MAGIC_BOLT);
			throwChar(defender, trajectory, 2);
		}
	}
	*/

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0x664422 ); particle.am = 0.6f;
		particle.setLifespan(2f);
		particle.speed.polar(Random.Float(PointF.PI2), 0.3f);
		particle.setSize( 1f, 2f);
		particle.radiateXY(3f);
	}

	@Override
	public String desc() {
		return "This wand is made of a sort of marbled stone, with gold trim and a round black gem at the tip. " +
				"It feels very weighty in your hand.\n" +
				"\n" +
				"This wand shoots a bolt which violently detonates at a target location. There is no smoke and fire, " +
				"but the force of this blast is enough to knock even the biggest of foes around.";
	}
}