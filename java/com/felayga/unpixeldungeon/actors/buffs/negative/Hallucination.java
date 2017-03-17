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
package com.felayga.unpixeldungeon.actors.buffs.negative;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

public class Hallucination extends FlavourBuff {

    public Hallucination()
    {
        type = buffType.NEGATIVE;
    }

    @Override
    public void fx(boolean on) {
        if (target != null && target == Dungeon.hero) {
            if (on) {
                GameScene.startHallucinating();
            }
            else {
                GameScene.stopHallucinating();
            }
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.HALLUCINATION;
    }

    @Override
    public String toString() {
        return "Hallucinating";
    }

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "Whoa!  Everything's all trippy!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
    public String desc() {
        return "Whoa.  Everything's all, like, trippy, dude.";
    }
}