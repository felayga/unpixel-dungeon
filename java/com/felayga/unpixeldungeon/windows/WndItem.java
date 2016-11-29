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
package com.felayga.unpixeldungeon.windows;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.ui.ItemSlot;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.BitmapTextMultiline;

public class WndItem extends Window {

	private static final float BUTTON_WIDTH		= 36;
	private static final float BUTTON_HEIGHT	= 16;

	private static final float GAP	= 2;

	private static final int WIDTH = 120;

	public WndItem( final WndTabbed owner, final Item item, final WndBackpack.Listener listener ) {
		super();

		IconTitle titlebar = new IconTitle( item );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		if (item.levelKnown && item.level > 0) {
			titlebar.color( ItemSlot.UPGRADED );
		} else if (item.levelKnown && item.level < 0) {
			titlebar.color( ItemSlot.DEGRADED );
		}

		BitmapTextMultiline info = PixelScene.createMultiline( item.info(), 6 );
		info.maxWidth = WIDTH;
		info.measure();
		info.x = titlebar.left();
		info.y = titlebar.bottom() + GAP;
		add( info );

		float y = info.y + info.height() + GAP;
		float x = 0;

		if (Dungeon.hero.isAlive()) {
            boolean external = listener != null;

			for (final String action:item.actions(Dungeon.hero, external)) {
                RedButton btn = new RedButton(action) {
                    @Override
                    protected void onClick() {
                        hide();
                        if (owner != null) {
                            owner.hide();
                        }

                        if (action == Constant.Action.EAT) {
                            Dungeon.hero.curAction = new HeroAction.UseItem.EatItem(item, action);
                            GLog.d("wnditem deposit action");
                            Dungeon.hero.motivate(true);
                            //while (Dungeon.hero.act()) ;
                        } else if (action == Constant.Action.TAKE) {
                            listener.onSelect(item);
                        } else {
                            item.execute(Dungeon.hero, action);
                        }
                    }

                    ;
                };
                btn.setSize(Math.max(BUTTON_WIDTH, btn.reqWidth()), BUTTON_HEIGHT);
                if (x + btn.width() > WIDTH) {
                    x = 0;
                    y += BUTTON_HEIGHT + GAP;
                }
                btn.setPos(x, y);
                add(btn);

                if ((!external && action.equals(item.defaultAction)) || (external && action.equals(Constant.Action.TAKE))) {
                    btn.textColor(TITLE_COLOR);
                }

                x += btn.width() + GAP;
            }
		}

		resize( WIDTH, (int)(y + (x > 0 ? BUTTON_HEIGHT : 0)) );
	}

    /*
	public WndItem( final WndBag owner, final Item item, final WndBackpack.Listener listener ) {
		super();

		IconTitle titlebar = new IconTitle( item );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		if (item.levelKnown && item.level > 0) {
			titlebar.color( ItemSlot.UPGRADED );
		} else if (item.levelKnown && item.level < 0) {
			titlebar.color( ItemSlot.DEGRADED );
		}

		BitmapTextMultiline info = PixelScene.createMultiline( item.info(), 6 );
		info.maxWidth = WIDTH;
		info.measure();
		info.x = titlebar.left();
		info.y = titlebar.bottom() + GAP;
		add( info );

		float y = info.y + info.height() + GAP;
		float x = 0;

		if (Dungeon.hero.isAlive() && owner != null) {
            ArrayList<String> actions = item.actions(Dungeon.hero);

			RedButton btn = new RedButton( Item.AC_DROP ) {
				@Override
				protected void onClick() {
					hide();
					owner.hide();
					listener.onSelect(item);
				};
			};
			btn.setSize( Math.max( BUTTON_WIDTH, btn.reqWidth() ), BUTTON_HEIGHT );
			if (x + btn.width() > WIDTH) {
				x = 0;
				y += BUTTON_HEIGHT + GAP;
			}
			btn.setPos( x, y );
			add( btn );

			btn.textColor( TITLE_COLOR );

			x += btn.width() + GAP;
		}

		resize( WIDTH, (int)(y + (x > 0 ? BUTTON_HEIGHT : 0)) );
	}
    */
}
