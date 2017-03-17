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

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.watabou.utils.Callback;

/**
 * Created by HELLO on 3/10/2017.
 */

public class InvisibilitySpellcaster extends Spellcaster {
    public InvisibilitySpellcaster() {
        super("Invisibility", 4);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicBolt, Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        MagicMissile.invisibility(source.sprite.parent, sourcePos, targetPos, callback);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        Char ch = Actor.findChar(targetPos);

        if (ch != null) {
            Buff.affect(ch, source, Invisibility.Indefinite.class);

            ch.sprite.burst(0xFFBCD3EB, 2);
        }
    }
}

