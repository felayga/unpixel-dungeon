/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.windows;

import android.graphics.RectF;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Belongings;
import com.felayga.unpixeldungeon.items.EquipableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.PotionBandolier;
import com.felayga.unpixeldungeon.items.bags.ScrollHolder;
import com.felayga.unpixeldungeon.items.bags.SeedPouch;
import com.felayga.unpixeldungeon.items.bags.WandHolster;
import com.felayga.unpixeldungeon.items.bags.backpack.Backpack;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.spells.Spell;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.felayga.unpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.felayga.unpixeldungeon.items.weapon.missiles.Boomerang;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.plants.Plant.Seed;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.Icons;
import com.felayga.unpixeldungeon.ui.ItemSlot;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

import java.util.Iterator;

public class WndBackpack extends WndTabbed {

	public static enum Mode {
		ALL,
		UNIDENTIFED,
		UPGRADEABLE,
		QUICKSLOT,
		FOR_SALE,
		WEAPON,
		ARMOR,
		ENCHANTABLE,
		WAND,
		SEED,
		FOOD,
		POTION,
		SCROLL,
		EQUIPMENT,
		SPELL,
		ALL_WITH_SPELL
	}

	protected static final int COLS_P = 6; //width original=4
	protected static final int COLS_L = 12; //height original=6

	protected static final int SLOT_SIZE = 18; //original=28
	protected static final int SLOT_MARGIN = 1;

	protected static final int TITLE_HEIGHT = 12; //original=12

	private Listener listener;
	private WndBackpack.Mode mode;
	private String title;

	private int nCols;
	private int nRows;

	protected int count;
	protected int col;
	protected int row;

	private static Mode lastMode;
	private static Bag lastBag;

	private Item excluded;

	public WndBackpack(Bag bag, Listener listener, Mode mode, String title, Item excluded) {

		super();
		Belongings stuff = Dungeon.hero.belongings;

		this.listener = listener;
		this.mode = mode;
		this.title = title;
		this.excluded = excluded;

		lastMode = mode;
		lastBag = bag;

		nCols = ShatteredPixelDungeon.landscape() ? COLS_L : COLS_P;
		nRows = (Belongings.BACKPACK_SIZE + 12) / nCols + ((Belongings.BACKPACK_SIZE + 12) % nCols > 0 ? 1 : 0);

		int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
		int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1);

		BitmapText txtTitle = PixelScene.createText(title != null ? title : Utils.capitalize(bag.getDisplayName()), 9);
		txtTitle.hardlight(TITLE_COLOR);
		txtTitle.measure();
		txtTitle.x = (int) (slotsWidth - txtTitle.width()) / 2;
		txtTitle.y = (int) (TITLE_HEIGHT - txtTitle.height()) / 2;
		add(txtTitle);

		placeItems(bag);

		resize(slotsWidth, slotsHeight + TITLE_HEIGHT);

		Bag[] bags = {
				stuff.backpack1,
				stuff.backpack2,
				stuff.backpack3,
				stuff.backpack4
				//stuff.getItem( SeedPouch.class ),
				//stuff.getItem( ScrollHolder.class ),
				//stuff.getItem( PotionBandolier.class ),
				//stuff.getItem( WandHolster.class )
		};


		for (Bag b : bags) {
			if (b != null) {
				BagTab tab = new BagTab(b);
				add(tab);
				tab.select(b == bag);
			}
		}

		layoutTabs();
	}

	public static WndBackpack lastBag(Listener listener, Mode mode, String title, Item excluded) {
		//todo: make sure this is right
		//commented out section is for scroll of identify multiple uses
		if (mode == lastMode && lastBag != null/* &&
			Dungeon.hero.belongings.contains( lastBag )*/) {

			return new WndBackpack(lastBag, listener, mode, title, excluded);

		} else {

			return new WndBackpack(Dungeon.hero.belongings.backpack1, listener, mode, title, excluded);

		}
	}

	public static WndBackpack getBag(Class<? extends Bag> bagClass, Listener listener, Mode mode, String title) {
		Bag bag = Dungeon.hero.belongings.getItem(bagClass);
		return bag != null ?
				new WndBackpack(bag, listener, mode, title, null) :
				lastBag(listener, mode, title, null);
	}

	protected void placeItems(Bag container) {
		// Equipped items
		Belongings stuff = Dungeon.hero.belongings;
		placeItem(stuff.weapon != null ? stuff.weapon : new Placeholder(ItemSpriteSheet.WEAPON));
		placeItem(stuff.offhand != null ? stuff.offhand : new Placeholder(ItemSpriteSheet.SHIELD));

		placeItem(stuff.armor != null ? stuff.armor : new Placeholder(ItemSpriteSheet.ARMOR));
		placeItem(stuff.gloves != null ? stuff.gloves : new Placeholder(ItemSpriteSheet.GLOVES));

		placeItem(stuff.ring1 != null ? stuff.ring1 : new Placeholder(ItemSpriteSheet.RING));
		placeItem(stuff.ring2 != null ? stuff.ring2 : new Placeholder(ItemSpriteSheet.RING));

		placeItem(stuff.tool1 != null ? stuff.tool1 : new Placeholder(ItemSpriteSheet.TOOL));
		placeItem(stuff.tool2 != null ? stuff.tool2 : new Placeholder(ItemSpriteSheet.TOOL));

		placeItem(stuff.boots != null ? stuff.boots : new Placeholder(ItemSpriteSheet.BOOTS));
		placeItem(stuff.cloak != null ? stuff.cloak : new Placeholder(ItemSpriteSheet.CLOAK));

		placeItem(stuff.amulet != null ? stuff.amulet : new Placeholder(ItemSpriteSheet.AMULETB));
		placeItem(stuff.face != null ? stuff.face : new Placeholder(ItemSpriteSheet.FACE));

		if (col != 0) {
			col = 0;
			row++;
		}

		/*
		//todo: th' fuck does this accomplish?
		boolean backpack = (container == Dungeon.hero.belongings.backpack1);
		if (!backpack) {
			//count = nCols;
			col = 0;
			row = 2;
		}
		*/

		count = 0;

		// Items in the bag
		Iterator<Item> iterator;

		iterator = container.iterator(false);
		while (iterator.hasNext()) {
			Item item = iterator.next();
			placeItem(item);
		}

		// Free Space
		while (count < container.size) {
			placeItem(null);
		}

		// Gold
		/*
		if (container == Dungeon.hero.belongings.backpack) {
			row = nRows - 1;
			col = nCols - 1;
			placeItem( new Gold( Dungeon.gold ) );
		}
		*/
	}

	protected void placeItem(final Item item) {
		int x = col * (SLOT_SIZE + SLOT_MARGIN);
		int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN);

		add(new ItemButton(item).setPos(x, y));

		if (++col >= nCols) {
			col = 0;
			row++;
		}

		count++;
	}

	@Override
	public void onMenuPressed() {
		if (listener == null) {
			hide();
		}
	}

	@Override
	public void onBackPressed() {
		if (listener != null) {
			listener.onSelect(null);
		}
		super.onBackPressed();
	}

	@Override
	protected void onClick(Tab tab) {
		hide();
		GameScene.show(new WndBackpack(((BagTab) tab).bag, listener, mode, title, excluded));
	}

	@Override
	protected int tabHeight() {
		return 20;
	}

	private class BagTab extends Tab {

		private Image icon;

		private Bag bag;

		public BagTab(Bag bag) {
			super();

			this.bag = bag;

			icon = icon();
			add(icon);
		}

		@Override
		protected void select(boolean value) {
			super.select(value);
			icon.am = selected ? 1.0f : 0.6f;
		}

		@Override
		protected void layout() {
			super.layout();

			icon.copy(icon());
			icon.x = x + (width - icon.width) / 2;
			icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
			if (!selected && icon.y < y + CUT) {
				RectF frame = icon.frame();
				frame.top += (y + CUT - icon.y) / icon.texture.height;
				icon.frame(frame);
				icon.y = y + CUT;
			}
		}

		private Image icon() {
			if (bag instanceof Backpack) {
				Backpack backpack = (Backpack) bag;

				return Icons.get(backpack.tabIcon);
			} else if (bag instanceof SeedPouch) {
				return Icons.get(Icons.SEED_POUCH);
			} else if (bag instanceof ScrollHolder) {
				return Icons.get(Icons.SCROLL_HOLDER);
			} else if (bag instanceof WandHolster) {
				return Icons.get(Icons.WAND_HOLSTER);
			} else if (bag instanceof PotionBandolier) {
				return Icons.get(Icons.POTION_BANDOLIER);
			} else {
				return Icons.get(Icons.BACKPACK);
			}
		}
	}

	private static class Placeholder extends Item {
		{
			name = null;

			bucStatus = BUCStatus.Uncursed;
			bucStatusKnown = true;
		}

		public Placeholder(int image) {
			this.image = image;
		}

		@Override
		public boolean isIdentified() {
			return true;
		}

		@Override
		public boolean isEquipped(Char hero) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return null;
		}
	}

	private class ItemButton extends ItemSlot {
		@Override
		protected PointF getFontScale() {
			return new PointF(0.75f, 0.75f);
		}

		@Override
		protected PointF getIconScale() {
			return new PointF(0.75f, 0.75f);
		}

		private static final int NORMAL = 0xFF4A4D44;
		private static final int EQUIPPED = 0xFF63665B;

		private Item item;
		private ColorBlock bg;

		public ItemButton(Item item) {
			super(item);

			this.item = item;
			/*
			if (item instanceof Gold) {
				bg.visible = false;
			}
			*/

			width = height = SLOT_SIZE;
		}

		@Override
		protected void createChildren() {
			bg = new ColorBlock(SLOT_SIZE, SLOT_SIZE, NORMAL);
			add(bg);

			super.createChildren();
		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;

			super.layout();
		}

		@Override
		public void item(Item item) {

			super.item(item);
			if (item != null) {

				bg.texture(TextureCache.createSolid(item.isEquipped(Dungeon.hero) ? EQUIPPED : NORMAL));
				BUCStatus.colorizeBackground(bg, item.visibleBucStatus());

				if (item.getDisplayName() == null || item == excluded) {
					enable(false);
				} else {
					enable(
							mode == Mode.FOR_SALE && (item.price() > 0) && (!item.isEquipped(Dungeon.hero) || item.bucStatus() != BUCStatus.Cursed) ||
									mode == Mode.UPGRADEABLE && item.isUpgradable() ||
									mode == Mode.UNIDENTIFED && !item.isIdentified() ||
									mode == Mode.QUICKSLOT && (item.defaultAction != null) ||
									mode == Mode.WEAPON && (item instanceof MeleeWeapon || item instanceof Boomerang) ||
									mode == Mode.ARMOR && (item instanceof Armor) ||
									mode == Mode.ENCHANTABLE && (item instanceof MeleeWeapon || item instanceof Boomerang || item instanceof Armor) ||
									mode == Mode.WAND && (item instanceof Wand) ||
									mode == Mode.SEED && (item instanceof Seed) ||
									mode == Mode.FOOD && (item instanceof Food) ||
									mode == Mode.POTION && (item instanceof Potion) ||
									mode == Mode.SCROLL && (item instanceof Scroll) ||
									mode == Mode.EQUIPMENT && (item instanceof EquipableItem) ||
									(mode == Mode.ALL && !(item instanceof Spell)) ||
									mode == Mode.SPELL && (item instanceof Spell) ||
									mode == Mode.ALL_WITH_SPELL
					);
				}
			} else {
				bg.color(NORMAL);
			}
		}

		@Override
		protected void onTouchDown() {
			bg.brightness(1.5f);
			Sample.INSTANCE.play(Assets.SND_CLICK, 0.7f, 0.7f, 1.2f);
		}

		;

		protected void onTouchUp() {
			bg.brightness(1.0f);
		}

		;

		@Override
		protected void onClick() {
			if (listener != null) {

				hide();
				listener.onSelect(item);

			} else {

				WndBackpack.this.add(new WndItem(WndBackpack.this, item));

			}
		}

		@Override
		protected boolean onLongClick() {
			if (listener == null && item.defaultAction != null) {
				hide();
				Dungeon.quickslot.setSlot(0, item);
				QuickSlotButton.refresh();
				return true;
			} else {
				return false;
			}
		}
	}

	public interface Listener {
		void onSelect(Item item);
	}
}
