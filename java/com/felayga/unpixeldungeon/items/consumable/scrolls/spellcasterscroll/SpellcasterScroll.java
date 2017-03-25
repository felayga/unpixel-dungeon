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

package com.felayga.unpixeldungeon.items.consumable.scrolls.spellcasterscroll;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.consumable.scrolls.Scroll;
import com.felayga.unpixeldungeon.spellcasting.Spellcaster;

/**
 * Created by HELLO on 3/11/2017.
 */

public class SpellcasterScroll extends Scroll {
    protected Spellcaster spellcaster;

    @Override
    protected void prepareRead(Hero hero) {
        super.prepareRead(hero);

        spellcaster.prepareZap(bucStatus().value);
    }

    @Override
    protected void doRead() {
        curUser.spend_new(TIME_TO_READ, true);
        Spellcaster.cast(curUser, curUser.pos(), spellcaster, Spellcaster.Origin.Scroll);
    }
}

