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

import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.effects.Beam;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.AcidParticle;
import com.felayga.unpixeldungeon.effects.particles.BlastParticle;
import com.felayga.unpixeldungeon.effects.particles.BloodParticle;
import com.felayga.unpixeldungeon.effects.particles.EarthParticle;
import com.felayga.unpixeldungeon.effects.particles.ElmoParticle;
import com.felayga.unpixeldungeon.effects.particles.EnergyParticle;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.effects.particles.FlowParticle;
import com.felayga.unpixeldungeon.effects.particles.LeafParticle;
import com.felayga.unpixeldungeon.effects.particles.PoisonParticle;
import com.felayga.unpixeldungeon.effects.particles.PurpleParticle;
import com.felayga.unpixeldungeon.effects.particles.RainbowParticle;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;
import com.felayga.unpixeldungeon.effects.particles.SmokeParticle;
import com.felayga.unpixeldungeon.effects.particles.SnowParticle;
import com.felayga.unpixeldungeon.effects.particles.SparkParticle;
import com.felayga.unpixeldungeon.effects.particles.WebParticle;
import com.felayga.unpixeldungeon.effects.particles.WindParticle;
import com.felayga.unpixeldungeon.effects.particles.WoolParticle;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/25/2017.
 */
public class WandOfNothing extends Wand {

    public WandOfNothing() {
        super(8);
        name = "Wand of Nothing";

        isOffensive = false;
        directionalZap = false;
        usesTargeting = false;

        collisionProperties = Ballistica.Mode.StopTarget;
        price = 100;
        weight(7 * Encumbrance.UNIT);
    }

    private static final String DIRECTIONALZAP = "directionalZap";
    private static final String RANDOMPARTICLEINDEX = "randomParticleIndex";
    private static final String RANDOMBEAMINDEX = "randomBeamIndex";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DIRECTIONALZAP, directionalZap);
        bundle.put(RANDOMPARTICLEINDEX, randomParticleIndex);
        bundle.put(RANDOMBEAMINDEX, randomBeamIndex);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        directionalZap = isOffensive = usesTargeting = bundle.getBoolean(DIRECTIONALZAP);
        setParticleFactory(bundle.getInt(RANDOMPARTICLEINDEX));
        setBeamClass(bundle.getInt(RANDOMBEAMINDEX));
    }

    private int randomParticleIndex;
    private Emitter.Factory randomParticleFactory;

    private void setParticleFactory(int index) {
        randomParticleIndex = index;
        switch(randomParticleIndex) {
            case 0:
                randomParticleFactory = AcidParticle.FACTORY;
                break;
            case 1:
                randomParticleFactory = BlastParticle.FACTORY;
                break;
            case 2:
                randomParticleFactory = BloodParticle.FACTORY;
                break;
            case 3:
                randomParticleFactory = EarthParticle.FACTORY;
                break;
            case 4:
                randomParticleFactory = ElmoParticle.FACTORY;
                break;
            case 5:
                randomParticleFactory = EnergyParticle.FACTORY;
                break;
            case 6:
                randomParticleFactory = FlameParticle.FACTORY;
                break;
            case 7:
                randomParticleFactory = FlowParticle.FACTORY;
                break;
            case 8:
                randomParticleFactory = LeafParticle.GENERAL;
                break;
            case 9:
                randomParticleFactory = PoisonParticle.MISSILE;
                break;
            case 10:
                randomParticleFactory = PurpleParticle.MISSILE;
                break;
            case 11:
                randomParticleFactory = RainbowParticle.BURST;
                break;
            case 12:
                randomParticleFactory = ShadowParticle.MISSILE;
                break;
            case 13:
                randomParticleFactory = ShaftParticle.FACTORY;
                break;
            case 14:
                randomParticleFactory = SmokeParticle.FACTORY;
                break;
            case 15:
                randomParticleFactory = SnowParticle.FACTORY;
                break;
            case 16:
                randomParticleFactory = SparkParticle.FACTORY;
                break;
            case 17:
                randomParticleFactory = WebParticle.FACTORY;
                break;
            case 18:
                randomParticleFactory = WindParticle.FACTORY;
                break;
            default:
                randomParticleFactory = WoolParticle.FACTORY;
                break;
        }
    }

    private int randomBeamIndex;
    private Class<? extends Beam> randomBeamClass;

    private void setBeamClass(int index) {
        randomBeamIndex = index;
        switch(randomBeamIndex) {
            case 0:
                randomBeamClass = Beam.DeathRay.class;
                break;
            case 1:
                randomBeamClass = Beam.HealthRay.class;
                break;
            case 2:
                randomBeamClass = null;
                break;
            default:
                randomBeamClass = Beam.LightRay.class;
                break;
        }
    }

    @Override
    public Item random() {
        super.random();

        setParticleFactory(Random.Int(20));
        setBeamClass(Random.Int(4));
        directionalZap = isOffensive = usesTargeting = Random.Int(2) == 0;

        return this;
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }

    @Override
    protected void onZap(Ballistica beam) {
        for (int pos : beam.path) {
            CellEmitter.get(pos).start(randomParticleFactory, 0.2f, 6);
        }
    }

    /*
    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //nothing
    }
    */

    @Override
    protected void fxEffect(int source, int destination, Callback callback) {
        Beam beamRay = null;
        if (randomBeamClass != null) {
            try {
                beamRay = randomBeamClass.getConstructor(PointF.class, PointF.class).newInstance(curUser.sprite.center(), DungeonTilemap.tileCenterToWorld(destination));
            } catch (Exception e) {
            }
        }

        if (beamRay != null) {
            curUser.sprite.parent.add(beamRay);
        } else if (callback != null) {
            callback.call();
        }
    }

    @Override
    public String desc() {
        return
                "This wand produces a spray of pretty lights.";
    }
}

