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
import com.felayga.unpixeldungeon.spellcasting.FrostSpellcaster;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfFrost extends Wand {

    public WandOfFrost() {
        super(8);
        name = "Wand of Frost";

        price = 175;

        spellcaster = new FrostSpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfFrost.this.wandUsed();
            }
        };
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }

    /*
	@Override
	//TODO: balancing, this could be mighty OP
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		Chill chill = defender.buff(Chill.class);
		if (chill != null && Random.PassFail((int)chill.movementModifier())){
            final Char _attacker = attacker;
			//need to delay this through an actor so that the freezing isn't broken by taking damage from the staff hit.
			new FlavourBuff(){
				{actPriority = Integer.MIN_VALUE;}
				public boolean act() {
					Buff.affect(target, _attacker, Frost.class, Frost.duration(target) * Random.Long(GameTime.TICK, GameTime.TICK * 2) / GameTime.TICK);
					return super.act();
				}
			}.attachTo(defender, attacker);
		}
	}
	*/

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0x88CCFF);
        particle.am = 0.6f;
        particle.setLifespan(1.5f);
        float angle = Random.Float(PointF.PI2);
        particle.speed.polar(angle, 2f);
        particle.acc.set(0f, 1f);
        particle.setSize(0f, 1.5f);
        particle.radiateXY(Random.Float(2f));
    }

    @Override
    public void explode(Char user) {
        super.explode(user);

        int maxDamage = curCharges() * 4;

        for (Integer offset : Level.NEIGHBOURS8) {
            int pos = user.pos() + offset;

            Char target = Actor.findChar(pos);

            if (target != null) {
                explode(target, maxDamage);
            }

            spellcaster.onZap(user, null, pos);
        }

        explode(curUser, maxDamage);
    }

    private void explode(Char target, int maxDamage) {
        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Cold, curUser, null);
        spellcaster.onZap(curUser, null, target.pos());
    }

    @Override
    public String desc() {
        return "This wand seems to be made out of some kind of magical ice. It grows brighter towards its " +
                "rounded tip. It feels very cold when held, but somehow your hand stays warm.\n\n" +
                "This wand shoots blasts of icy energy toward your foes, dealing significant damage and chilling, " +
                "which reduces speed. The effect seems stronger in water. Chilled and frozen enemies " +
                "take less damage from this wand, as they are already cold.";
    }
}
