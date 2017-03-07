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
package com.felayga.unpixeldungeon.items.quest;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.ElmoParticle;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

//import com.felayga.unpixeldungeon.actors.mobs.NewbornElemental;


public class CeremonialCandle extends Item {

    //generated with the wandmaker quest
    public static int ritualPos;

    public CeremonialCandle()
    {
        name = "ceremonial candle";
        image = ItemSpriteSheet.CANDLE;

        hasLevels(false);

        unique = true;
        stackable = true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    protected void doDrop(Hero hero, Item item) {
        super.doDrop(hero, item);
        checkCandles();
    }

    @Override
    protected void doDrop(Hero hero, Item item, int quantity) {
        super.doDrop(hero, item, quantity);
        checkCandles();
    }

    @Override
    protected void onThrow(Char thrower, int cell) {
        super.onThrow(thrower, cell);
        checkCandles();
    }

    private static void checkCandles() {
        Heap heapTop = Dungeon.level.heaps.get(ritualPos - Level.WIDTH);
        Heap heapRight = Dungeon.level.heaps.get(ritualPos + 1);
        Heap heapBottom = Dungeon.level.heaps.get(ritualPos + Level.WIDTH);
        Heap heapLeft = Dungeon.level.heaps.get(ritualPos - 1);

        if (heapTop != null &&
                heapRight != null &&
                heapBottom != null &&
                heapLeft != null) {

            if (heapTop.peek() instanceof CeremonialCandle &&
                    heapRight.peek() instanceof CeremonialCandle &&
                    heapBottom.peek() instanceof CeremonialCandle &&
                    heapLeft.peek() instanceof CeremonialCandle) {

                heapTop.pickUp();
                heapRight.pickUp();
                heapBottom.pickUp();
                heapLeft.pickUp();

				/*
				NewbornElemental elemental = new NewbornElemental();
				elemental.pos = ritualPos;
				elemental.state = elemental.HUNTING;
				GameScene.add(elemental, 1);
				*/

                for (int i : Level.NEIGHBOURS9) {
                    CellEmitter.get(ritualPos + i).burst(ElmoParticle.FACTORY, 10);
                }
                Sample.INSTANCE.play(Assets.SND_BURNING);
            }
        }

    }

    @Override
    public String info() {
        return
                "A set of candles, melted down and fused together through use.\n\n" +
                        "Alone they are worthless, but used with other candles in a pattern, " +
                        "they can focus the energy for a summoning ritual.";
    }
}
