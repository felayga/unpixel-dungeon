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

package com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Terror;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.effects.Flare;
import com.felayga.unpixeldungeon.items.scrolls.spellcasterscroll.SpellcasterScroll;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.spellcasting.AreaFearSpellcaster;
import com.felayga.unpixeldungeon.spellcasting.Spellcaster;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfTerror extends SpellcasterScroll {

    public ScrollOfTerror() {
        name = "Scroll of Terror";
        initials = "Te";

        price = 50;

        spellcaster = new AreaFearSpellcaster();
    }

    @Override
    protected void doRead() {
        super.doRead();

        GLog.i("The scroll emits a brilliant flash of red light");
        if (Random.Int(5) == 0) {
            setKnown();
        }
    }

    @Override
    public String desc() {
        return
                "A flash of red light will overwhelm all creatures in your field of view with terror, " +
                        "and they will turn and flee. Attacking a fleeing enemy will dispel the effect.";
    }

}
