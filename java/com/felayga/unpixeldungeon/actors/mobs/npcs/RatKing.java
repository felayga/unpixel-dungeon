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
package com.felayga.unpixeldungeon.actors.mobs.npcs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.rat.RatKingSprite;

public class RatKing extends NPC {

    public RatKing() {
        super(5, RatKingSprite.class);

        movementSpeed(GameTime.TICK / 2);

        state = SLEEPING;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public int damage(int dmg, MagicType type, Char source, Item sourceItem) {
        return 0;
    }

    @Override
    public void add(Buff buff) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public void interact() {
        sprite.turnTo(pos(), Dungeon.hero.pos());
        if (state == SLEEPING) {
            notice();
            yell("I'm not sleeping!");
            state = WANDERING;
        } else {
            yell("What is it? I have no time for this nonsense. My kingdom won't rule itself!");
        }
    }

    @Override
    public String description() {
        return ((RatKingSprite) sprite).festive ?
                "This rat is a little bigger than a regular marsupial rat. " +
                        "It's wearing a tiny festive hat instead of its usual crown. Happy Holidays!"
                : "This rat is a little bigger than a regular marsupial rat " +
                "and it's wearing a tiny crown on its head.";
    }
}
