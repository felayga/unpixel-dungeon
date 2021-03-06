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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.particles.PurpleParticle;
import com.felayga.unpixeldungeon.items.equippableitem.armor.Armor;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Stylus extends Item {
	
	private static final String TXT_SELECT_ARMOR	= "Select an armor to inscribe on";
	private static final String TXT_INSCRIBED		= "you inscribed your %s with the stylus";
	
	private static final long TIME_TO_INSCRIBE = GameTime.TICK * 2;
	
	private static final String AC_INSCRIBE = "INSCRIBE";

    public Stylus()
	{
		name = "arcane stylus";
		image = ItemSpriteSheet.STYLUS;
		
		stackable = true;

        hasLevels(false);

		bones = true;

        price = 30;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_INSCRIBE );
		return actions;
	}
	
	@Override
	public boolean execute( Hero hero, String action ) {
		if (action.equals(AC_INSCRIBE)) {
			curUser = hero;
			GameScene.selectItem( itemSelector, Armor.class, TXT_SELECT_ARMOR, null );

			return false;
		} else {
			return super.execute( hero, action );
		}
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private void inscribe( Armor armor ) {
		curUser.belongings.remove(this, 1);

		GLog.w( TXT_INSCRIBED, armor.getDisplayName() );
		
		armor.enchant();
		
		curUser.sprite.operate( curUser.pos() );
		curUser.sprite.centerEmitter(-1).start( PurpleParticle.BURST, 0.05f, 10 );
		Sample.INSTANCE.play( Assets.SND_BURNING );
		
		curUser.spend_new( TIME_TO_INSCRIBE, false );
		curUser.busy();
	}
	
	@Override
	public String info() {
		return
			"This arcane stylus is made of some dark, very hard stone. Using it you can inscribe " +
			"a magical glyph on your armor, but you have no power over choosing what glyph it will be, " +
			"the stylus will decide it for you.";
	}
	
	private final WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				Stylus.this.inscribe( (Armor)item );
			}
		}
	};
}
