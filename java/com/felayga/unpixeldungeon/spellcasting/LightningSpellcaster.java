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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Lightning;
import com.felayga.unpixeldungeon.effects.particles.SparkParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 3/10/2017.
 */

public class LightningSpellcaster extends Spellcaster {
    public LightningSpellcaster() {
        super("Lightning", 5);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicRay, Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        affected.clear();
        arcs.clear();

        arcs.add(new Lightning.Arc(sourcePos, targetPos));

        Char ch = Actor.findChar(targetPos);
        if (ch != null) {
            arc(ch);
        } else {
            CellEmitter.center(targetPos).burst(SparkParticle.FACTORY, 3);
        }

        //don't want to wait for the effect before processing damage.
        source.sprite.parent.add(new Lightning(arcs, null));
        callback.call();
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        //lightning deals less damage per-target, the more targets that are hit.
        float multipler = 0.4f + (0.6f / affected.size());
        if (Level.puddle[targetPos]) multipler *= 1.5f;

        for (Char ch : affected) {
            ch.damage(Math.round(damage(Random.NormalIntRange(6, 36)) * multipler), MagicType.Shock, source, null);

            if (ch == Dungeon.hero) Camera.main.shake(2, 0.3f);
            ch.sprite.centerEmitter(-1).burst(SparkParticle.FACTORY, 3);
            ch.sprite.flash();
        }
    }

    private ArrayList<Char> affected = new ArrayList<>();
    private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

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

}
