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
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.spellcasting.FireblastSpellcaster;
import com.watabou.utils.Random;

public class WandOfFireblast extends Wand {

    public WandOfFireblast() {
        super(8);
        name = "Wand of Fireblast";

        price = 175;

        spellcaster = new FireblastSpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfFireblast.this.wandUsed();
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
		//acts like blazing enchantment, package conflict.....
		new com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Fire()
				.proc( staff, attacker, defender, damage);
	}
	*/

    /*
	@Override
	protected int chargesPerCast() {
		//consumes 40% of current charges, rounded up, with a minimum of one.
		return Math.max(1, (int)Math.ceil(curCharges*0.4f));
	}
	*/

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0xEE7722);
        particle.am = 0.5f;
        particle.setLifespan(0.6f);
        particle.acc.set(0, -40);
        particle.setSize(0f, 3f);
        particle.shuffleXY(2f);
    }

    @Override
    public void explode(Char user) {
        super.explode(user);

        //much more damage comes from onZap()
        int maxDamage = curCharges() * 2;

        for (Integer offset : Level.NEIGHBOURS8) {
            int pos = user.pos() + offset;

            Char target = Actor.findChar(pos);

            if (target != null) {
                explode(target, maxDamage);
            }

            spellcaster.onZap(user, null, pos);
        }

        GameScene.add(Blob.seed(curUser, curUser.pos(), 2, Fire.class));
        explode(curUser, maxDamage * 3);
    }

    private void explode(Char target, int maxDamage) {
        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Fire, curUser, null);
    }

    @Override
    public String desc() {
        return
                "This wand is made from red-lacquered wood with golden leaf used liberally to make it look quite regal. " +
                        "It crackles and hisses at the tip, eager to unleash its powerful magic.\n" +
                        "\n" +
                        "This wand produces a blast of fire when used, extending out into a cone shape. As this wand is upgraded " +
                        "it will consume more charges, the effect becomes significantly more powerful the more charges are consumed.";
    }
}
