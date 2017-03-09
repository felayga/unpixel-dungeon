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
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Regrowth;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.items.Dewdrop;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.plants.Seedpod;
import com.felayga.unpixeldungeon.plants.Starflower;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class WandOfRegrowth extends Wand {
    public WandOfRegrowth() {
        super(15);
        name = "Wand of Regrowth";

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.SplasherBolt, Ballistica.Mode.StopSelf);
        price = 150;
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(11, 15);
    }

    @Override
    protected void onZap(Ballistica bolt) {
        onZap(bolt.collisionPos, Random.IntRange(3, 12));
    }

    private static Level.RandomPositionValidator onZapSpotValidator = new Level.RandomPositionValidator() {
        @Override
        public boolean isValidPosition(int pos) {
            int c = Dungeon.level.map(pos);
            return c == Terrain.EMPTY ||
                    c == Terrain.EMBERS ||
                    c == Terrain.EMPTY_DECO ||
                    c == Terrain.GRASS ||
                    c == Terrain.HIGH_GRASS;
        }
    };

    private void onZap(int pos, int spotCount) {
        ArrayList<Integer> spots = Dungeon.level.randomPositionsNear(pos, spotCount, onZapSpotValidator);

        if (spots != null) {
            for (Integer subPos : spots) {
                int terrain;
                switch(Random.Int(3)) {
                    case 0:
                        terrain = Terrain.HIGH_GRASS;
                        break;
                    default:
                        terrain = Terrain.GRASS;
                        break;
                }
                Dungeon.level.set(subPos, terrain, true);
                GameScene.add(Blob.seed(curUser, subPos, 10, Regrowth.class));

                if (Random.Int(4) == 0) {
                    Plant.Seed seed;
                    if (Random.Int(16)==0) {
                        seed = new Seedpod.Seed();
                    } else {
                        seed = (Plant.Seed) Generator.random(Generator.Category.SEED);
                    }

                    Dungeon.level.plant(curUser, seed, subPos);
                }
            }

            for (Integer subPos : spots) {
                if (Dungeon.visible[subPos]) {
                    MagicMissile.foliage(curUser.sprite.parent, pos, subPos, null);
                }
            }
        }
    }

    @Override
    protected void fxEffect(int source, int destination, Callback callback) {
        MagicMissile.foliage(curUser.sprite.parent, source, destination, callback);
    }


    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(ColorMath.random(0x004400, 0x88CC44));
        particle.am = 1f;
        particle.setLifespan(1.2f);
        particle.setSize(1f, 2f);
        particle.shuffleXY(1f);
        float dst = Random.Float(11f);
        particle.x -= dst;
        particle.y += dst;
    }

    @Override
    public void explode(Char user) {
        super.explode(user);

        int maxDamage = curCharges() * 4;

        for (Integer offset : Level.NEIGHBOURS8) {
            int pos = user.pos() + offset;
            Char target = Dungeon.level.findMob(pos);

            if (target == null) {
                continue;
            }

            explode(target, maxDamage);
        }

        explode(user, maxDamage);

        onZap(user.pos(), 24);
    }

    public void explode(Char target, int maxDamage) {
        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Magic, curUser, null);

        fxEffect(curUser.pos(), target.pos(), null);
    }

    @Override
    public String desc() {
        return
                "This wand is made from a thin shaft of expertly carved wood. " +
                        "Somehow it is still alive and vibrant, bright green like a young tree's core.\n" +
                        "\n" +
                        "When used, this wand will consume all its charges to blast magical regrowth energy outward " +
                        "in a cone. This magic will cause grass, roots, and rare plants to spring to life.\n" +
                        "\n" +
                        "\"When life ceases new life always begins to grow... The eternal cycle always remains!\"";
    }

}
