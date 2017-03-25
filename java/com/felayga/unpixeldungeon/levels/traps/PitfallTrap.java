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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.particles.WindParticle;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.levels.features.Chasm;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.TrapSprite;

import java.util.Iterator;

public class PitfallTrap extends Trap {

    {
        name = "Pitfall trap";
        color = TrapSprite.RED;
        shape = TrapSprite.DIAMOND;

    }

    @Override
    public void activate() {
        Heap heap = Dungeon.level.heaps.get(pos);

        if (heap != null) {
            Dungeon.dropToChasm(heap);
        }

        Char ch = Actor.findChar(pos);

        if (ch == Dungeon.hero) {
            Chasm.heroFall(pos);
        } else if (ch != null) {
            Chasm.mobFall((Mob) ch);
        }
    }

    @Override
    protected void disarm() {
        super.disarm();

        //if making a pit here wouldn't block any paths, make a pit tile instead of a disarmed trap tile.
        if (!(Dungeon.level.solid[pos - Level.WIDTH] && Dungeon.level.solid[pos + Level.WIDTH])
                && !(Dungeon.level.solid[pos - 1] && Dungeon.level.solid[pos + 1])) {

            int c = Dungeon.level.map(pos - Level.WIDTH);

            if (c == Terrain.WALL || c == Terrain.WALL_DECO) {
                Dungeon.level.set(pos, Terrain.CHASM_WALL, true);
            } else {
                Dungeon.level.set(pos, Terrain.CHASM_FLOOR, true);
            }

            sprite.parent.add(new WindParticle.Wind(pos));
            sprite.kill();
            GameScene.updateMap(pos);
        }
    }

    @Override
    public String desc() {
        return "This pressure plate rests atop a fairly weak floor, and will likely collapse into a pit if it is pressed.";
    }
}
