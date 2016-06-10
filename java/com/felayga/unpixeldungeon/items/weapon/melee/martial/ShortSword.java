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
package com.felayga.unpixeldungeon.items.weapon.melee.martial;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.felayga.unpixeldungeon.items.weapon.missiles.martial.Boomerang;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ShortSword extends MartialMeleeWeapon {
	
	public static final String AC_REFORGE	= "REFORGE";
	
	private static final String TXT_SELECT_WEAPON	= "Select a weapon to upgrade";
	
	private static final String TXT_REFORGED =
		"you reforged the short sword to upgrade your %s";
	private static final String TXT_NOT_BOOMERANG =
		"you can't upgrade a boomerang this way";
	
	private static final long TIME_TO_REFORGE	= GameTime.TICK * 2;
	
	private boolean  equipped;
	
	public ShortSword() {
		super( GameTime.TICK, 1, 6 );

		name = "short sword";
		image = ItemSpriteSheet.SHORT_SWORD;

		unique = true;
		bones = false;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (level > 0) {
			actions.add( AC_REFORGE );
		}
		return actions;
	}
	
	@Override
	public boolean execute( Hero hero, String action ) {
		if (action == AC_REFORGE) {
			if (hero.belongings.weapon() == this) {
				equipped = true;
                hero.belongings.unequip(this, false);
			} else {
				equipped = false;
				hero.belongings.remove(this, 1);
			}
			
			curUser = hero;
			
			GameScene.selectItem( itemSelector, WndBackpack.Mode.WEAPON, TXT_SELECT_WEAPON );

			return false;
		} else {
			return super.execute( hero, action );
		}
	}
	
	@Override
	public String desc() {
		return
			"It is indeed quite short, just a few inches longer, than a dagger.";
	}
	
	private final WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && !(item instanceof Boomerang)) {
				
				Sample.INSTANCE.play( Assets.SND_EVOKE );
				ScrollOfUpgrade.upgrade( curUser );
				evoke( curUser );
				
				GLog.w( TXT_REFORGED, item.getDisplayName() );
				
				((MeleeWeapon)item).upgrade(item, 1);
				curUser.spend_new( TIME_TO_REFORGE, true );
				
				Badges.validateItemLevelAquired( item );
				
			} else {
				
				if (item instanceof Boomerang) {
					GLog.w( TXT_NOT_BOOMERANG );
				}
				
				if (equipped) {
					curUser.belongings.collectEquip(ShortSword.this);
				} else {
					curUser.belongings.collect(ShortSword.this);
				}
			}
		}
	};
}
