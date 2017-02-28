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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class GrimTrap extends Trap {

	{
		name = "Grim trap";
		color = TrapSprite.GREY;
		shape = TrapSprite.LARGE_DOT;
	}

	@Override
	public Trap hide() {
		//cannot hide this trap
		return reveal();
	}

	@Override
	public void activate() {
		Char target = Actor.findChar(pos);

		//find the closest char that can be aimed at
		if (target == null) {
            android.util.SparseArray<Char> chars = Actor.chars();
            for (int n = 0; n < chars.size(); n++) {
                Char c = chars.valueAt(n);

                Ballistica bolt = new Ballistica(pos, c.pos(), Ballistica.Mode.Projectile);
                if (bolt.collisionPos == c.pos() &&
                        (target == null || Level.distance(pos, c.pos()) < Level.distance(pos, target.pos()))) {
                    target = c;
                }
            }
        }

		if (target != null){
			final Char finalTarget = target;
			MagicMissile.shadow(target.sprite.parent, pos, target.pos(), new Callback() {
				@Override
				public void call() {

					if (finalTarget == Dungeon.hero) {
						//almost kill the player
						if (((float)finalTarget.HP/finalTarget.HT) >= 0.9f){
							finalTarget.damage((finalTarget.HP-1), MagicType.Magic, null);
						//kill 'em
						} else {
							finalTarget.damage(finalTarget.HP, MagicType.Magic, null);
						}
						Sample.INSTANCE.play(Assets.SND_CURSED);
						if (!finalTarget.isAlive()) {
							Dungeon.fail(Utils.format(ResultDescriptions.TRAP, name));
							GLog.n("You were killed by the blast of a grim trap...");
						}
					} else {
						finalTarget.damage(finalTarget.HP, MagicType.Magic, null);
						Sample.INSTANCE.play(Assets.SND_BURNING);
					}
					finalTarget.sprite.emitter().burst(ShadowParticle.UP, 10);
				}
			});
		} else {
			CellEmitter.get(pos).burst(ShadowParticle.UP, 10);
			Sample.INSTANCE.play(Assets.SND_BURNING);
		}
	}

	@Override
	public String desc() {
		return "Extremely powerful destructive magic is stored within this trap, enough to instantly kill all but the healthiest of heroes. " +
				"Triggering it will send a ranged blast of lethal magic towards the nearest character.";
	}
}
