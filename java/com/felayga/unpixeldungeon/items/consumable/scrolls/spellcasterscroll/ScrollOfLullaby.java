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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.spellcasting.AreaSleepSpellcaster;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfLullaby extends SpellcasterScroll {
    public ScrollOfLullaby() {
        name = "Scroll of Lullaby";
        initials = "Lu";

        price = 50;

        spellcaster = new AreaSleepSpellcaster();
        spellcaster.ballisticaMode = Ballistica.Mode.StopSelf.value;
    }

    @Override
    protected void doRead() {
        super.doRead();

        Sample.INSTANCE.play(Assets.SND_LULLABY);

        GLog.i("The scroll utters a soothing melody. You feel very sleepy.");

        setKnown();
    }

    @Override
    public String desc() {
        return
                "A soothing melody will lull all who hear it into a deep magical sleep.";
    }

}
