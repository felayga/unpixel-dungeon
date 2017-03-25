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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.spellcasting.NothingSpellcaster;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/25/2017.
 */
public class WandOfNothing extends Wand {
    private NothingSpellcaster nothingSpellcaster;

    public WandOfNothing() {
        super(8);
        name = "Wand of Nothing";

        price = 100;

        nothingSpellcaster = new NothingSpellcaster() {
            @Override
            public void onZap(Char source, Ballistica path, int targetPos) {
                super.onZap(source, path, targetPos);

                WandOfNothing.this.wandUsed();
            }
        };
        spellcaster = nothingSpellcaster;

        nothingSpellcaster.setCharacteristics(randomParticleIndex, randomBeamIndex, ballisticaModeIndex);
    }

    private int randomParticleIndex;
    private int randomBeamIndex;
    private int ballisticaModeIndex;

    private static final String RANDOMPARTICLEINDEX = "randomParticleIndex";
    private static final String RANDOMBEAMINDEX = "randomBeamIndex";
    private static final String BALLISTICAMODEINDEX = "ballisticaModeIndex";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(RANDOMPARTICLEINDEX, randomParticleIndex);
        bundle.put(RANDOMBEAMINDEX, randomBeamIndex);
        bundle.put(BALLISTICAMODEINDEX, ballisticaModeIndex);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        randomParticleIndex = bundle.getInt(RANDOMPARTICLEINDEX);
        randomBeamIndex = bundle.getInt(RANDOMBEAMINDEX);
        ballisticaModeIndex = bundle.getInt(BALLISTICAMODEINDEX);

        nothingSpellcaster.setCharacteristics(randomParticleIndex, randomBeamIndex, ballisticaModeIndex);
    }


    @Override
    public Item random() {
        super.random();

        randomParticleIndex = Random.Int(NothingSpellcaster.RANDOM_PARTICLE_MAX);
        randomBeamIndex = Random.Int(NothingSpellcaster.RANDOM_BEAM_MAX);
        ballisticaModeIndex = Random.Int(NothingSpellcaster.RANDOM_BALLISTICA_MAX);

        nothingSpellcaster.setCharacteristics(randomParticleIndex, randomBeamIndex, ballisticaModeIndex);

        return this;
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }

    /*
    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //nothing
    }
    */

    @Override
    public String desc() {
        return
                "This wand produces a spray of pretty lights.";
    }
}

