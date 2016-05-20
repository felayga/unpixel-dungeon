/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */
package com.felayga.unpixeldungeon.items;

import java.util.ArrayList;

import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.audio.Sample;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.armor.ClassArmor;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;

public class ArmorKit extends Item {
	
	private static final String TXT_SELECT_ARMOR	= "Select an armor to upgrade";
	private static final String TXT_UPGRADED		= "you applied the armor kit to upgrade your %s";
	
	private static final long TIME_TO_UPGRADE = GameTime.TICK * 2;
	
	private static final String AC_APPLY = "APPLY";
	
	{
		name = "armor kit";
		image = ItemSpriteSheet.KIT;
		
		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_APPLY );
		return actions;
	}
	
	@Override
	public boolean execute( Hero hero, String action ) {
		if (action.equals(AC_APPLY)) {
			curUser = hero;
			GameScene.selectItem( itemSelector, WndBackpack.Mode.ARMOR, TXT_SELECT_ARMOR );

			return false;
		} else {
			return super.execute( hero, action );
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private void upgrade( Armor armor ) {
		
		curUser.belongings.remove(this, 1);
		
		curUser.sprite.centerEmitter(-1).start( Speck.factory( Speck.KIT ), 0.05f, 10 );
		curUser.spend( TIME_TO_UPGRADE, false );
		curUser.busy();
		
		GLog.w( TXT_UPGRADED, armor.getDisplayName() );
		
		ClassArmor classArmor = ClassArmor.upgrade( curUser, armor );
		if (curUser.belongings.armor == armor) {
			
			curUser.belongings.armor = classArmor;
			((HeroSprite)curUser.sprite).setArmor(classArmor.textureIndex);
			
		} else {
			curUser.belongings.remove(armor, 1);
			curUser.belongings.collect(classArmor);
		}
		
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_EVOKE );
	}
	
	@Override
	public String info() {
		return
			"Using this kit of small tools and materials anybody can transform any armor into an \"epic armor\", " +
			"which will keep all properties of the original armor, but will also provide its wearer a special ability " +
			"depending on his class. No skills in tailoring, leatherworking or blacksmithing are required.";
	}
	
	private final WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				ArmorKit.this.upgrade( (Armor)item );
			}
		}
	};
}
