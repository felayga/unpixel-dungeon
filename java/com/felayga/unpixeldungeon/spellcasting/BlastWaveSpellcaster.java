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

package com.felayga.unpixeldungeon.spellcasting;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.effects.Effects;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.effects.Pushing;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/10/2017.
 */

public class BlastWaveSpellcaster extends Spellcaster {
    public BlastWaveSpellcaster() {
        super("Blast Wave", 1);
        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.Projectile);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        MagicMissile.slowness(source.sprite.parent, sourcePos, targetPos, callback);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        Sample.INSTANCE.play(Assets.SND_BLAST);
        BlastWave.blast(targetPos);

        int damage = damage(Random.NormalIntRange(2, 12));

        //presses all tiles in the AOE first
        for (int i : Level.NEIGHBOURS9) {
            Dungeon.level.press(targetPos + i, Actor.findChar(targetPos + i));
            Heap heap = Dungeon.level.heaps.get(targetPos + 1);
            if (heap != null) {
                heap.contentsImpact(true);
            }
        }

        int intwisModifier = source.getAttributeModifier(AttributeType.INTWIS);

        //throws other chars around the center.
        for (int i : Level.NEIGHBOURS8) {
            Char target = Actor.findChar(targetPos + i);

            if (target != null) {
                target.damage(damage, MagicType.Magic, source, null);

                if (target.isAlive()) {
                    Ballistica trajectory = new Ballistica(target.pos(), target.pos() + i, Ballistica.Mode.MagicBolt.value);
                    int strength = Random.Int(1, 4) + ((intwisModifier + 1) / 3) + levelBoost;
                    throwChar(source, target, trajectory, strength);
                }
            }
        }

        //throws the char at the center of the blast
        Char target = Actor.findChar(targetPos);
        if (target != null) {
            target.damage(damage, MagicType.Magic, source, null);

            if (target.isAlive() && path != null && path.path.size() > path.dist + 1) {
                Ballistica trajectory = new Ballistica(target.pos(), path.path.get(path.dist + 1), Ballistica.Mode.MagicBolt.value);
                int strength = Random.NormalIntRange(2, 8) + ((intwisModifier + 1) / 3);
                throwChar(source, target, trajectory, strength);
            }
        }
    }

    private void throwChar(final Char source, final Char target, final Ballistica path, int power) {
        int dist = Math.min(path.dist, power);

        /*
		//FIXME: sloppy
		if ((ch instanceof King) || (ch instanceof Golem) || (ch instanceof Yog.RottingFist))
			dist /= 2;

		if (dist == 0 || ch instanceof Yog || ch instanceof RotLasher || ch instanceof RotHeart) return;

		*/

        if (dist <= 0) {
            return;
        }

        if (Actor.findChar(path.path.get(dist)) != null) {
            dist--;
        }

        final int newPos = path.path.get(dist);

        if (newPos == target.pos()) return;

        final int finalDist = dist;

        Actor.addDelayed(new Pushing(target, target.pos(), newPos, new Callback() {
            public void call() {
                target.pos(newPos);
                if (target.pos() == path.collisionPos) {
                    target.damage(Random.NormalIntRange((finalDist + 1) / 2, finalDist), MagicType.Magic, source, null);
                    Paralysis.prolong(target, source, Paralysis.class, Random.NormalIntRange((finalDist + 1) / 2, finalDist));
                }
                Dungeon.level.press(target.pos(), target);
            }
        }), -1);
    }

    private static class BlastWave extends Image {
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

}

