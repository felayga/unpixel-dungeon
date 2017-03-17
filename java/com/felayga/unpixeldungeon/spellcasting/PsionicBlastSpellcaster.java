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
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.HashSet;
import java.util.List;

/**
 * Created by HELLO on 3/11/2017.
 */

public class PsionicBlastSpellcaster extends Spellcaster {
    public PsionicBlastSpellcaster() {
        super("Psionic Blast", 4);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicRay, Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        if (callback != null) {
            callback.call();
        }
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        GameScene.flash(0xFFFFFF);

        Sample.INSTANCE.play(Assets.SND_BLAST);

        //PSIONIC blast.. what the hell does line-of-sight matter?
        /*
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])) {
            if (Level.fieldOfView[mob.pos()]) {
                mob.damage(mob.HT, MagicType.Magic, curUser, null);
            }
        }
        */

        HashSet<Integer> spots = Level.getRadius(targetPos, 6 + levelBoost);
        spots.remove(source.pos());

        SparseArray<Char> chars = Actor.chars();
        for (int n = 0; n < chars.size(); n++) {
            Char target = chars.valueAt(n);
            int pos = target.pos();

            if ((target.characteristics & Characteristic.Brainless.value) == 0 && spots.contains(pos)) {
                target.damage(Random.IntRange(damage(4), damage(16)), MagicType.Magic, source, null);
                Buff.prolong(target, source, Paralysis.class, Random.IntRange(4, 6) * GameTime.TICK);
            }
        }

        Buff.prolong(source, source, Blindness.class, Random.IntRange(6, 9) * GameTime.TICK);

        Dungeon.observe();
    }
}
