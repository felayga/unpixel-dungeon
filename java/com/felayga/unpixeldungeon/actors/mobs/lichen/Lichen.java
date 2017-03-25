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

package com.felayga.unpixeldungeon.actors.mobs.lichen;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.negative.Held;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.LichenSprite;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by HELLO on 5/22/2016.
 */
public class Lichen extends Mob {
    public Lichen() {
        super(0, LichenSprite.class);

        movementSpeed(GameTime.TICK * 12 / 1);
        attackSpeed(GameTime.TICK);
        defenseMundane = 11;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 20;
        nutrition = 200;
        resistanceMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.value(CorpseEffect.Unrottable, CorpseEffect.Undecayable, CorpseEffect.Vegetable);
        viewDistance = 0;
        characteristics = Characteristic.value(Characteristic.NonBreather, Characteristic.CannotUseItems, Characteristic.Brainless);
    }

    private static final String TOUCHEDVICTIMS = "touchedVictims";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        int[] _touchedVictims = new int[touchedVictims.size()];
        int n = 0;
        for (Integer victim : touchedVictims) {
            _touchedVictims[n] = victim;
            n++;
        }

        bundle.put(TOUCHEDVICTIMS, _touchedVictims);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        int[] _touchedVictims = bundle.getIntArray(TOUCHEDVICTIMS);

        for (int n = 0; n < _touchedVictims.length; n++) {
            touchedVictims.add(_touchedVictims[n]);
        }
    }

    @Override
    protected boolean shouldTouch(Char enemy) {
        return true;
    }

    private HashSet<Integer> touchedVictims = new HashSet<>();

    @Override
    protected void touch(Char enemy, boolean visible, boolean touchable) {
        Buff.prolong(enemy, this, Held.class, movementSpeed());
        touchedVictims.add(enemy.charRegistryIndex());
    }

    @Override
    public void die(Actor cause) {
        List<Held> pendingRemoval = new ArrayList<Held>();

        for (Integer victim : touchedVictims) {
            Char c = Char.Registry.get(victim);

            if (c != null) {
                for (Buff buff : c.buffs()) {
                    if (buff instanceof Held) {
                        Held held = (Held) buff;

                        if (held.ownerRegistryIndex() == charRegistryIndex()) {
                            pendingRemoval.add(held);
                        }
                    }
                }
            }
        }

        for (Held held : pendingRemoval) {
            Buff.detach(held);
        }

        super.die(cause);
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
