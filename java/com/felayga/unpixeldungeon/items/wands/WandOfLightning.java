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
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Lightning;
import com.felayga.unpixeldungeon.effects.particles.SparkParticle;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfLightning extends Wand {
    public WandOfLightning() {
        super(8);

        name = "Wand of Lightning";

        collisionProperties = Ballistica.Mode.MagicRay;
        price = 175;
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }

	private ArrayList<Char> affected = new ArrayList<>();

	ArrayList<Lightning.Arc> arcs = new ArrayList<>();
	
	@Override
	protected void onZap( Ballistica bolt ) {
        onZap(bolt.collisionPos);
    }

    private void onZap(int pos) {
        //lightning deals less damage per-target, the more targets that are hit.
        float multipler = 0.4f + (0.6f / affected.size());
        if (Level.puddle[pos]) multipler *= 1.5f;

        for (Char ch : affected) {
            processSoulMark(ch, curUser);
            ch.damage(Math.round(Random.NormalIntRange(6, 36) * multipler), MagicType.Shock, curUser, null);

            if (ch == Dungeon.hero) Camera.main.shake(2, 0.3f);
            ch.sprite.centerEmitter(-1).burst(SparkParticle.FACTORY, 3);
            ch.sprite.flash();
        }

        if (!curUser.isAlive()) {
            Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
            GLog.n("You killed yourself with your own Wand of Lightning...");
        }
    }

    /*
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//acts like shocking enchantment
		new Shock().proc(staff, attacker, defender, damage);
	}
	*/

	private void arc( Char ch ) {
		
		affected.add( ch );

		for (int i : Level.NEIGHBOURS8) {
			int cell = ch.pos() + i;

			Char n = Actor.findChar( cell );
			if (n != null && !affected.contains( n )) {
				arcs.add(new Lightning.Arc(ch.pos(), n.pos()));
				arc(n);
			}
		}

		if (Level.puddle[ch.pos()] && !ch.flying()){
			for (int i : Level.NEIGHBOURS8DIST2) {
				int cell = ch.pos() + i;
				//player can only be hit by lightning from an adjacent enemy.
				if (!Level.insideMap(cell) || Actor.findChar(cell) == Dungeon.hero) continue;

				Char n = Actor.findChar( ch.pos() + i );
				if (n != null && !affected.contains( n )) {
					arcs.add(new Lightning.Arc(ch.pos(), n.pos()));
					arc(n);
				}
			}
		}
	}
	
	@Override
	protected void fxEffect(int source, int destination, Callback callback ) {

		affected.clear();
		arcs.clear();
		arcs.add( new Lightning.Arc(source, destination));

		int cell = destination;

		Char ch = Actor.findChar( cell );
		if (ch != null) {
			arc(ch);
		} else {
			CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
		}

		//don't want to wait for the effect before processing damage.
		curUser.sprite.parent.add( new Lightning( arcs, null ) );
		callback.call();
	}

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
            Char target = Dungeon.level.findMob(pos);

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

        fxEffect(curUser.pos(), target.pos(), null);
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
