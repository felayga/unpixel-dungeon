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
import com.felayga.unpixeldungeon.spellcasting.ChaosSpellcaster;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/3/2017.
 */
public class WandOfChaos extends Wand {
    private WandOfChaosSpellcaster chaosSpell;
    public WandOfChaos() {
        super(8);

        name = "Wand of Chaos";

        price = 500;

        chaosSpell = new WandOfChaosSpellcaster();
        spellcaster = chaosSpell;
    }

    private class WandOfChaosSpellcaster extends ChaosSpellcaster {
        @Override
        public void onZap(Char source, Ballistica path, int targetPos) {
            super.onZap(source, path, targetPos);

            WandOfChaos.this.wandUsed();
        }

        public void prepareExplode() {
            while (!randomizeEffect()) ;
        }
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }



    /*
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//less likely Grim proc
		if (Random.Int(3) == 0)
			new Death().proc( staff, attacker, defender, damage);
	}
	*/

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0x220022);
        particle.am = 0.6f;
        particle.setLifespan(0.6f);
        particle.acc.set(40, -40);
        particle.setSize(0f, 3f);
        particle.shuffleXY(2f);
    }

    @Override
    public void explode(Char user) {
        super.explode(user);

        int maxDamage = curCharges() * 16;

        for (Integer offset : Level.NEIGHBOURS8) {
            int pos = user.pos() + offset;
            Char target = Actor.findChar(pos);

            if (target == null) {
                chaosSpell.prepareExplode();
                chaosSpell.onZap(user, null, pos);
                continue;
            }

            explode(user, target, maxDamage);
        }

        explode(user, user, maxDamage);
    }

    public void explode(Char user, Char target, int maxDamage) {
        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Magic, curUser, null);

        chaosSpell.prepareExplode();
        chaosSpell.onZap(user, null, target.pos());
    }

    @Override
    public String desc() {
        return
                "This wand is made from a solid smooth chunk of obsidian, with a deep purple light running up its side, " +
                        "ending at the tip. It glows with destructive energy, waiting to shoot forward.\n\n" +
                        "This wand shoots a beam that pierces any obstacle, and will go farther the more it is upgraded.\n\n" +
                        "This wand deals bonus damage the more enemies and terrain it penetrates.";
    }

}
