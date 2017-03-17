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
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.spellcasting.MagicMappingSpellcaster;
import com.felayga.unpixeldungeon.spellcasting.Spellcaster;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfMagicMapping extends SpellcasterScroll {

	private static final String TXT_LAYOUT = "A map coalesces in your mind!";
	private static final String TXT_LAYOUT_CURSED = "Unfortunately, you can't grasp the details.";

    public ScrollOfMagicMapping()
	{
		name = "Scroll of Magic Mapping";
		initials = "MM";

        price = 25;

        spellcaster = new MagicMappingSpellcaster();
	}


	
	@Override
	protected void doRead() {
        super.doRead();

        switch (bucStatus()) {
            case Cursed:
                GLog.n(TXT_LAYOUT + " " + TXT_LAYOUT_CURSED);
                break;
            default:
                GLog.i(TXT_LAYOUT);
                break;
        }

		setKnown();
	}
	
	@Override
	public String desc() {
		return
			"When this scroll is read, an image of crystal clarity will be etched into your memory, " +
			"alerting you to the precise layout of the level and revealing all hidden secrets. " +
			"The locations of items and creatures will remain unknown.";
	}

}
