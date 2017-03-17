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
package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Weakness;
import com.felayga.unpixeldungeon.effects.Flare;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRemoveCurse extends Scroll {

	private static final String TXT_PROCCED	=
		"Your pack glows with a cleansing light, and a malevolent energy disperses.";
	private static final String TXT_NOT_PROCCED	=
		"Your pack glows with a cleansing light, but nothing happens.";

    public ScrollOfRemoveCurse()
	{
		name = "Scroll of Remove Curse";
		initials = "RC";

        price = 30;
	}
	
	@Override
	protected void doRead() {
		super.doRead();

		new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;

		boolean procced = false;
		String passProc = null;
		String failProc = "The scroll disintegrates.";

		switch(bucStatus())
		{
			case Cursed:
				break;
			case Uncursed:
				passProc = "You feel like someone is helping you.";
				failProc = passProc;
				procced = curUser.belongings.bucUncurse(true, true, true, false) > 0;
				break;
			case Blessed:
				passProc = TXT_PROCCED;
				failProc = TXT_NOT_PROCCED;
				procced = curUser.belongings.bucUncurse(true, true, true, true) > 0;
				break;

		}
		
		Weakness.detach( curUser, Weakness.class );
		
		if (procced) {
			GLog.p( passProc );
		} else {
			GLog.i( failProc );
		}
		
		setKnown();
	}
	
	@Override
	public String desc() {
		return
			"The incantation on this scroll will instantly strip from " +
			"the reader's weapon, armor, rings and carried items any evil " +
			"enchantments that might prevent the wearer from removing them.";
	}
	
	public static boolean uncurse( Char hero, Item source, Item... items ) {
		
		boolean procced = false;
		for (Item item : items) {
			if (item != null && uncurse(source, item)) {
				procced = true;
			}
			/*
			if (item instanceof Bag){
				for (Item bagItem : ((Bag)item).items){
					if (bagItem != null && uncurse(source, bagItem)) {
						procced = true;
					}
				}
			}
			*/
		}
		
		if (procced) {
			hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
		}
		
		return procced;
	}

	private static boolean uncurse(Item source, Item item)
	{
		BUCStatus test = item.bucStatus();

		item.upgrade(source, 0);

		return test != item.bucStatus();
	}
}
