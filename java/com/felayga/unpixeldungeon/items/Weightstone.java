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
import com.felayga.unpixeldungeon.items.equippableitem.weapon.Weapon;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.IconTitle;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Weightstone extends Item {

	private static final String TXT_SELECT_WEAPON	= "Select a weapon to balance";
	private static final String TXT_LIGHT			= "you balanced your %s to make it lighter";
	private static final String TXT_HEAVY		= "you balanced your %s to make it heavier";

	private static final long TIME_TO_APPLY = GameTime.TICK * 2;

	private static final String AC_APPLY = "APPLY";

    public Weightstone() {
		name = "weightstone";
		image = ItemSpriteSheet.WEIGHT;

		stackable = true;

        hasLevels(false);

		bones = true;
        price = 40;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_APPLY );
		return actions;
	}

	@Override
	public boolean execute( Hero hero, String action ) {
		if (action == AC_APPLY) {
			curUser = hero;
			GameScene.selectItem( itemSelector, Weapon.class, TXT_SELECT_WEAPON, null );

			return false;
		} else {
			return super.execute( hero, action );
		}
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	private void apply( Weapon weapon, boolean forSpeed ) {
		curUser.belongings.remove(this, 1);

		weapon.refined = 1;
		/*
		if (forSpeed) {
			weapon.imbue = Weapon.Imbue.LIGHT;
			GLog.p( TXT_LIGHT, weapon.name() );
		} else {
			weapon.imbue = Weapon.Imbue.HEAVY;
			GLog.p( TXT_HEAVY, weapon.name() );
		}
		*/

		curUser.sprite.operate( curUser.pos() );
		Sample.INSTANCE.play( Assets.SND_MISS );

		curUser.spend_new( TIME_TO_APPLY, false );
		curUser.busy();
	}

	@Override
	public String info() {
		return
				"Using a weightstone, you can balance your melee weapon to make it lighter or heavier, " +
				"increasing either speed or damage at the expense of the other.";
	}

	private final WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				GameScene.show( new WndBalance( (Weapon)item ) );
			}
		}
	};

	public class WndBalance extends Window {

		private static final String TXT_CHOICE = "How would you like to balance your %s?";

		private static final String TXT_LIGHT		= "Lighter";
		private static final String TXT_HEAVY   	= "Heavier";
		private static final String TXT_CANCEL		= "Never mind";

		private static final int WIDTH			= 120;
		private static final int MARGIN 		= 2;
		private static final int BUTTON_WIDTH	= WIDTH - MARGIN * 2;
		private static final int BUTTON_HEIGHT	= 20;

		public WndBalance( final Weapon weapon ) {
			super();

			IconTitle titlebar = new IconTitle( weapon );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );

			BitmapTextMultiline tfMesage = PixelScene.createMultiline( Utils.format( TXT_CHOICE, weapon.getDisplayName() ), 8 );
			tfMesage.maxWidth = WIDTH - MARGIN * 2;
			tfMesage.measure();
			tfMesage.x = MARGIN;
			tfMesage.y = titlebar.bottom() + MARGIN;
			add( tfMesage );

			float pos = tfMesage.y + tfMesage.height();

			/*
			if (weapon.imbue != Weapon.Imbue.LIGHT) {
				RedButton btnSpeed = new RedButton( TXT_LIGHT ) {
					@Override
					protected void onClick() {
						hide();
						Weightstone.this.apply( weapon, true );
					}
				};
				btnSpeed.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
				add( btnSpeed );

				pos = btnSpeed.bottom();
			}

			if (weapon.imbue != Weapon.Imbue.HEAVY) {
				RedButton btnAccuracy = new RedButton( TXT_HEAVY ) {
					@Override
					protected void onClick() {
						hide();
						Weightstone.this.apply( weapon, false );
					}
				};
				btnAccuracy.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
				add( btnAccuracy );

				pos = btnAccuracy.bottom();
			}
			*/

			RedButton btnCancel = new RedButton( TXT_CANCEL, true ) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
			add( btnCancel );

			resize( WIDTH, (int)btnCancel.bottom() + MARGIN );
		}

		protected void onSelect( int index ) {};
	}
}
