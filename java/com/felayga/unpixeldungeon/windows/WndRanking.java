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
package com.felayga.unpixeldungeon.windows;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.backpack.Belongings;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.ui.BadgesList;
import com.felayga.unpixeldungeon.ui.Icons;
import com.felayga.unpixeldungeon.ui.ItemSlot;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.ScrollPane;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

import java.util.Locale;

public class WndRanking extends WndTabbed {
	
	private static final String TXT_ERROR		= "Unable to load additional information";
	
	private static final String TXT_STATS	= "Stats";
	private static final String TXT_ITEMS	= "Items";
	private static final String TXT_BADGES	= "Badges";
	
	private static final int WIDTH			= 115;
	private static final int HEIGHT			= 144;
	
	private static final int TAB_WIDTH	= 40;
	
	private Thread thread;
	private String error = null;
	
	private Image busy;
	
	public WndRanking( final String gameFile ) {
		
		super();
		resize( WIDTH, HEIGHT );
		
		thread = new Thread() {
			@Override
			public void run() {
				try {
					Badges.loadGlobal();
					Dungeon.loadGame( gameFile );
				} catch (Exception e ) {
					error = e.getMessage();
				}
			}
		};
		thread.start();

		busy = Icons.BUSY.get();
		busy.origin.set( busy.width / 2, busy.height / 2 );
		busy.angularSpeed = 720;
		busy.x = (WIDTH - busy.width) / 2;
		busy.y = (HEIGHT - busy.height) / 2;
		add( busy );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (thread != null && !thread.isAlive()) {
			thread = null;
			if (error == null) {
				remove( busy );
				createControls();
			} else {
				hide();
				Game.scene().add( new WndError( TXT_ERROR + "\n" + error ) );
			}
		}
	}

	private void createControls() {
		
		String[] labels =
			{TXT_STATS, TXT_ITEMS, TXT_BADGES};
		Group[] pages =
			{new StatsTab(), new ItemsTab(), new BadgesTab()};
		
		for (int i=0; i < pages.length; i++) {
			
			add( pages[i] );
			
			Tab tab = new RankingTab( labels[i], pages[i] );
			add( tab );
		}

		layoutTabs();
		
		select( 0 );
	}

	private class RankingTab extends LabeledTab {
		
		private Group page;
		
		public RankingTab( String label, Group page ) {
			super( label );
			this.page = page;
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}
	
	private class StatsTab extends Group {

		private int GAP	= 4;
		
		private static final String TXT_TITLE	= "Level %d %s";

		private static final String TXT_CHALLENGES	= "Challenges";

		private static final String TXT_HEALTH	= "Health";

        private static final String TXT_STRCON	= "Vitality";
        private static final String TXT_DEXCHA	= "Cunning";
        private static final String TXT_INTWIS	= "Sagacity";

		private static final String TXT_DURATION	= "Game Duration";
		
		private static final String TXT_DEPTH	= "Maximum Depth";
		private static final String TXT_ENEMIES	= "Mobs Killed";
		private static final String TXT_GOLD	= "Gold Collected";
		
		private static final String TXT_FOOD	= "Food Eaten";
		private static final String TXT_ALCHEMY	= "Potions Cooked";
		private static final String TXT_ANKHS	= "Ankhs Used";
		
		public StatsTab() {
			super(-1);

			if (Dungeon.challenges > 0) GAP--;
			
			String heroClass = Dungeon.hero.className();
			
			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar( 0, 0 ) );
			title.label( Utils.format( TXT_TITLE, Dungeon.hero.level, heroClass ).toUpperCase( Locale.ENGLISH ) );
			title.color(Window.SHPX_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add( title );
			
			float pos = title.bottom();

			if (Dungeon.challenges > 0) {
				RedButton btnCatalogus = new RedButton( TXT_CHALLENGES ) {
					@Override
					protected void onClick() {
						Game.scene().add( new WndChallenges( Dungeon.challenges, false ) );
					}
				};
				btnCatalogus.setRect( 0, pos, btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
				add( btnCatalogus );

				pos = btnCatalogus.bottom();
			}

			pos += GAP + GAP;
			
			pos = statSlot( this, TXT_STRCON, Integer.toString( Dungeon.hero.STRCON() ), pos );
            pos = statSlot( this, TXT_DEXCHA, Integer.toString( Dungeon.hero.DEXCHA() ), pos );
            pos = statSlot( this, TXT_INTWIS, Integer.toString( Dungeon.hero.INTWIS() ), pos );
			pos = statSlot( this, TXT_HEALTH, Integer.toString( Dungeon.hero.HT ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, TXT_DURATION, Integer.toString( (int)Statistics.duration ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, TXT_DEPTH, "nope", pos );
			pos = statSlot( this, TXT_ENEMIES, Integer.toString( Statistics.enemiesSlain ), pos );
			pos = statSlot( this, TXT_GOLD, Integer.toString( Statistics.goldCollected ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, TXT_FOOD, Integer.toString( Statistics.foodEaten ), pos );
			pos = statSlot( this, TXT_ALCHEMY, Integer.toString( Statistics.potionsCooked ), pos );
			pos = statSlot( this, TXT_ANKHS, Integer.toString( Statistics.ankhsUsed ), pos );
		}
		
		private float statSlot( Group parent, String label, String value, float pos ) {
			
			BitmapText txt = PixelScene.createText( label, 7 );
			txt.y = pos;
			parent.add( txt );
			
			txt = PixelScene.createText( value, 7 );
			txt.measure();
			txt.x = WIDTH * 0.65f;
			txt.y = pos;
			parent.add( txt );
			
			return pos + GAP + txt.baseLine();
		}
	}
	
	private class ItemsTab extends Group {
		
		private float pos;
		
		public ItemsTab() {
			super(-1);
			
			Belongings stuff = Dungeon.hero.belongings;
			if (stuff.weapon() != null) {
				addItem(stuff.weapon());
			}
			if (stuff.offhand() != null) {
				addItem(stuff.offhand());
			}

            if (stuff.armor() != null) {
                addItem(stuff.armor());
            }
            if (stuff.helmet() != null) {
                addItem(stuff.helmet());
            }
            if (stuff.gloves() != null) {
                addItem(stuff.gloves());
            }
            if (stuff.boots() != null) {
                addItem(stuff.boots());
            }

            if (stuff.ranged() != null) {
                addItem(stuff.ranged());
            }
            if (stuff.ammo() != null) {
                addItem(stuff.ammo());
            }

            if (stuff.cloak() != null) {
                addItem(stuff.cloak());
            }
            if (stuff.amulet() != null) {
                addItem(stuff.amulet());
            }
			if (stuff.ring1() != null) {
				addItem(stuff.ring1());
			}
			if (stuff.ring2() != null) {
				addItem(stuff.ring2());
			}

			pos = 0;
			for (int i = 0; i < 4; i++){
				if (Dungeon.quickslot.getItem(i) != null){
					QuickSlotButton slot = new QuickSlotButton(Dungeon.quickslot.getItem(i));

					slot.setRect( pos, 116, 28, 28 );

					add(slot);

				} else {
					ColorBlock bg = new ColorBlock( 28, 28, 0xFF4A4D44);
					bg.x = pos;
					bg.y = 116;
					add(bg);
				}
				pos += 29;
			}
		}
		
		private void addItem( Item item ) {
			ItemButton slot = new ItemButton( item );
			slot.setRect( 0, pos, width, ItemButton.HEIGHT );
			add( slot );
			
			pos += slot.height() + 1;
		}
	}
	
	private class BadgesTab extends Group {
		
		public BadgesTab() {
			super(-1);
			
			camera = WndRanking.this.camera;
			
			ScrollPane list = new BadgesList( false );
			add( list );
			
			list.setSize( WIDTH, HEIGHT );
		}
	}
	
	private class ItemButton extends Button {
		
		public static final int HEIGHT	= 28;
		
		private Item item;
		
		private ItemSlot slot;
		private ColorBlock bg;
		private BitmapText name;
		
		public ItemButton( Item item ) {
			
			super();

			this.item = item;
			
			slot.item( item );
			BUCStatus.colorizeBackground(bg, item.visibleBucStatus());
		}
		
		@Override
		protected void createChildren() {
			
			bg = new ColorBlock( HEIGHT, HEIGHT, 0xFF4A4D44 );
			add( bg );
			
			slot = new ItemSlot();
			add( slot );
			
			name = PixelScene.createText( "?", 7 );
			add( name );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			slot.setRect( x, y, HEIGHT, HEIGHT );
			
			name.x = slot.right() + 2;
			name.y = y + (height - name.baseLine()) / 2;
			
			String str = Utils.capitalize( item.getDisplayName() );
			name.text( str );
			name.measure();
			if (name.width() > width - name.x) {
				do {
					str = str.substring( 0, str.length() - 1 );
					name.text( str + "..." );
					name.measure();
				} while (name.width() > width - name.x);
			}
			
			super.layout();
		}
		
		@Override
		protected void onTouchDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		};
		
		protected void onTouchUp() {
			bg.brightness( 1.0f );
		};
		
		@Override
		protected void onClick() {
			Game.scene().add( new WndItem( null, item, null ) );
		}
	}

	private class QuickSlotButton extends ItemSlot{

		public static final int HEIGHT	= 28;

		private Item item;
		private ColorBlock bg;

		QuickSlotButton(Item item){
			super(item);
			this.item = item;
		}

		@Override
		protected void createChildren() {
			bg = new ColorBlock( HEIGHT, HEIGHT, 0xFF4A4D44 );
			add( bg );

			super.createChildren();
		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;

			super.layout();
		}

		@Override
		protected void onTouchDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		};

		protected void onTouchUp() {
			bg.brightness( 1.0f );
		};

		@Override
		protected void onClick() {
			Game.scene().add(new WndItem(null, item, null));
		}
	}
}
