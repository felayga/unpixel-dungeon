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

import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.Char;
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
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

/**
 * Created by HELLO on 3/10/2017.
 */

public class NothingSpellcaster extends Spellcaster {
    public NothingSpellcaster() {
        super("Nothing", 1);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopTarget);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        Beam beamRay = null;
        if (randomBeamClass != null) {
            try {
                beamRay = randomBeamClass.getConstructor(PointF.class, PointF.class).newInstance(DungeonTilemap.tileCenterToWorld(sourcePos), DungeonTilemap.tileCenterToWorld(targetPos));
            } catch (Exception e) {
            }
        }

        if (beamRay != null) {
            source.sprite.parent.add(beamRay);
        }

        if (callback != null) {
            callback.call();
        }
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        for (int pos : path.path) {
            CellEmitter.get(pos).start(randomParticleFactory, 0.2f, 6);
        }
    }

    private Emitter.Factory randomParticleFactory;
    private Class<? extends Beam> randomBeamClass;

    public void setCharacteristics(int particleIndex, int beamIndex, int ballisticaModeIndex) {
        setParticleFactory(particleIndex);
        setBeamClass(beamIndex);
        setBallisticaMode(ballisticaModeIndex);
    }

    public static final int RANDOM_PARTICLE_MAX     = 20;
    public static final int RANDOM_BEAM_MAX         = 5;
    public static final int RANDOM_BALLISTICA_MAX   = 9;

    public void setParticleFactory(int index) {
        switch (index) {
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

    public void setBeamClass(int index) {
        switch (index) {
            case 0:
                randomBeamClass = Beam.DeathRay.class;
                break;
            case 1:
                randomBeamClass = Beam.HealthRay.class;
                break;
            case 2:
                randomBeamClass = null;
                break;
            case 3:
                randomBeamClass = Beam.CancellationRay.class;
                break;
            default:
                randomBeamClass = Beam.LightRay.class;
                break;
        }
    }

    public void setBallisticaMode(int index) {
        switch (index / 2) {
            case 0:
                ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopTarget);
                break;
            case 1:
                ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopChars);
                break;
            case 2:
                ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopTerrain);
                break;
            case 3:
                ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicRay);
                break;
            default:
                ballisticaMode = 0;
                break;
        }

        if (index % 2 == 0) {
            ballisticaMode |= Ballistica.Mode.StopSelf.value;
        }
    }

}
