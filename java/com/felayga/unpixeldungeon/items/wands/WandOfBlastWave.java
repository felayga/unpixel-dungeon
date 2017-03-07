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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.effects.Effects;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.effects.Pushing;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfBlastWave extends Wand {

    public WandOfBlastWave()
	{
        super(8);

		name = "Wand of Blast Wave";

		collisionProperties = Ballistica.Mode.Projectile;
        price = 150;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		Sample.INSTANCE.play( Assets.SND_BLAST );
		BlastWave.blast(bolt.collisionPos);

		int damage = Random.NormalIntRange(2, 12);

		//presses all tiles in the AOE first
		for (int i : Level.NEIGHBOURS9) {
            Dungeon.level.press(bolt.collisionPos + i, Actor.findChar(bolt.collisionPos + i));
            Heap heap = Dungeon.level.heaps.get(bolt.collisionPos + 1);
            if (heap != null) {
                heap.contentsImpact(true);
            }
        }

        int intwisModifier = curUser.getAttributeModifier(AttributeType.INTWIS);

		//throws other chars around the center.
		for (int i  : Level.NEIGHBOURS8) {
            Char ch = Actor.findChar(bolt.collisionPos + i);

            if (ch != null) {
                ch.damage(damage, MagicType.Mundane, curUser, null);

                if (ch.isAlive()) {
                    Ballistica trajectory = new Ballistica(ch.pos(), ch.pos() + i, Ballistica.Mode.MagicBolt);
                    int strength = Random.Int(1, 4) + ((intwisModifier + 1) / 3);
                    throwChar(ch, trajectory, strength);
                }
            }
        }

		//throws the char at the center of the blast
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null){
			ch.damage(damage, MagicType.Mundane, curUser, null);

			if (ch.isAlive() && bolt.path.size() > bolt.dist+1) {
				Ballistica trajectory = new Ballistica(ch.pos(), bolt.path.get(bolt.dist + 1), Ballistica.Mode.MagicBolt);
				int strength = Random.NormalIntRange(2, 8) + ((intwisModifier + 1) / 3);
				throwChar(ch, trajectory, strength);
			}
		}

		if (!curUser.isAlive()) {
			Dungeon.fail( Utils.format(ResultDescriptions.ITEM, name) );
			GLog.n("You killed yourself with your own Wand of Blast Wave...");
		}
	}

	private void throwChar(final Char ch, final Ballistica trajectory, int power){
		int dist = Math.min(trajectory.dist, power);

        /*
		//FIXME: sloppy
		if ((ch instanceof King) || (ch instanceof Golem) || (ch instanceof Yog.RottingFist))
			dist /= 2;

		if (dist == 0 || ch instanceof Yog || ch instanceof RotLasher || ch instanceof RotHeart) return;

		*/

        if (dist <= 0) {
            return;
        }

		if (Actor.findChar(trajectory.path.get(dist)) != null){
			dist--;
		}

		final int newPos = trajectory.path.get(dist);

		if (newPos == ch.pos()) return;

		final int finalDist = dist;

		Actor.addDelayed(new Pushing(ch, ch.pos(), newPos, new Callback() {
			public void call() {
				ch.pos(newPos);
				if (ch.pos() == trajectory.collisionPos) {
					ch.damage(Random.NormalIntRange((finalDist + 1) / 2, finalDist), MagicType.Mundane, curUser, null);
					Paralysis.prolong(ch, curUser, Paralysis.class, Random.NormalIntRange((finalDist + 1) / 2, finalDist));
				}
				Dungeon.level.press(ch.pos(), ch);
			}
		}), -1);
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
	protected void fxEffect(int source, int destination, Callback callback) {
		MagicMissile.slowness(curUser.sprite.parent, source, destination, callback);
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0x664422 ); particle.am = 0.6f;
		particle.setLifespan(2f);
		particle.speed.polar(Random.Float(PointF.PI2), 0.3f);
		particle.setSize( 1f, 2f);
		particle.radiateXY(3f);
	}

	public static class BlastWave extends Image {

		private static final float TIME_TO_FADE = 0.2f;

		private float time;

		public BlastWave(){
			super(Effects.get(Effects.Type.RIPPLE));
			origin.set(width / 2, height / 2);
		}

		public void reset(int pos) {
			revive();

			x = (pos % Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
			y = (pos / Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

			time = TIME_TO_FADE;
		}

		@Override
		public void update() {
            super.update();

            if ((time -= Game.elapsed) <= 0) {
                kill();
            } else {
                float p = time / TIME_TO_FADE;
                alpha(p);
                scale((1 - p) * 3);
            }
        }

		public static void blast(int pos) {
			Group parent = Dungeon.hero.sprite.parent;
			BlastWave b = (BlastWave) parent.recycle(BlastWave.class);
			parent.bringToFront(b);
			b.reset(pos);
		}

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