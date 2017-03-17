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
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.Beam;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.PurpleParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/10/2017.
 */

public class DeathSpellcaster extends Spellcaster {
    public DeathSpellcaster() {
        super("Death", 7);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicRay, Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        source.sprite.parent.add(new Beam.DeathRay(DungeonTilemap.tileCenterToWorld( sourcePos ), DungeonTilemap.tileCenterToWorld( targetPos )));
        callback.call();
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        burn(targetPos);

        Char ch = Actor.findChar(targetPos);
        if (ch != null) {
            ch.damage(ch.HP, MagicType.Magic, source, null);
            ch.sprite.centerEmitter(-1).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            ch.sprite.flash();
        }
    }

    public void burn(int targetPos) {
        if (Level.burnable[targetPos]) {
            Dungeon.level.set(targetPos, Terrain.CHARCOAL, true);
            GameScene.updateMap(targetPos);
            CellEmitter.center(targetPos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
            Dungeon.observe();
        }
    }

}

