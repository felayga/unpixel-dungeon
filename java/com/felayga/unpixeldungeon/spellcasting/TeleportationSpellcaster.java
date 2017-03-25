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
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.consumable.scrolls.positionscroll.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.watabou.utils.Callback;

/**
 * Created by HELLO on 3/10/2017.
 */

public class TeleportationSpellcaster extends Spellcaster {
    public TeleportationSpellcaster() {
        super("Teleportation", 5);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicRay, Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        MagicMissile.whiteLight(source.sprite.parent, sourcePos, targetPos, callback);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        Char target = Actor.findChar(targetPos);

        if (target != null) {
            if (ScrollOfTeleportation.canTeleport(target)) {
                ScrollOfTeleportation.doTeleport(target, Constant.Position.RANDOM);
            }
            return;
        }

        Heap heap = Dungeon.level.heaps.get(targetPos);

        if (heap != null) {
            if (ScrollOfTeleportation.canTeleport(heap)) {
                ScrollOfTeleportation.doTeleport(heap, Constant.Position.RANDOM);
            }
        }
    }
}
