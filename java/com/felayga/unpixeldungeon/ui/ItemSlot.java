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
package com.felayga.unpixeldungeon.ui;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.potions.PotionOfMight;
import com.felayga.unpixeldungeon.items.potions.PotionOfStrength;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.PointF;

public class ItemSlot extends Button {

	public static final int DEGRADED	= 0xFF4444;
	public static final int UPGRADED	= 0x44FF44;
	public static final int FADED       = 0x999999;
	public static final int WARNING		= 0xFF8800;
	
	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;

	protected ItemSprite icon;
	protected BitmapText topLeft;
	protected BitmapText topRight;
	protected BitmapText bottomRight;
	
	private static final String TXT_STRENGTH	= ":%d";
	private static final String TXT_TYPICAL_STR	= "%d?";
	private static final String TXT_KEY_DEPTH	= "\u007F%d";

	private static final String TXT_LEVEL	= "%+d";
	private static final String TXT_CURSED    = "";//"-";

	// Special "virtual items"
	public static final Item TOMB = new Item() {
		public int image() { return ItemSpriteSheet.TOMB; };
	};
	public static final Item SKELETON = new Item() {
		public int image() { return ItemSpriteSheet.BONES; };
	};
	public static final Item REMAINS = new Item() {
		public int image() { return ItemSpriteSheet.REMAINS; };
	};
	
	public ItemSlot() {
		super();
	}
	
	public ItemSlot( Item item ) {
		this();
		item(item);
	}

	protected PointF getFontScale() {
		return new PointF(1.0f, 1.0f);
	}

	protected PointF getIconScale() {
		return new PointF(1.0f, 1.0f);
	}
		
	@Override
	protected void createChildren() {
		
		super.createChildren();
		
		icon = new ItemSprite();
		icon.scale = getIconScale();
		add(icon);
		
		topLeft = new BitmapText(PixelScene.pixelFont);
		topLeft.scale = getFontScale();
		add(topLeft);
		
		topRight = new BitmapText(PixelScene.pixelFont);
		topRight.scale = getFontScale();
		add(topRight);
		
		bottomRight = new BitmapText(PixelScene.pixelFont);
		bottomRight.scale = getFontScale();
		add(bottomRight);
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		icon.x = x + (width - icon.width * icon.scale.x) / 2;
		icon.y = y + (height - icon.height * icon.scale.y) / 2;
		
		if (topLeft != null) {
			topLeft.x = x;
			topLeft.y = y;
		}
		
		if (topRight != null) {
			topRight.x = x + (width - topRight.width());
			topRight.y = y;
		}
		
		if (bottomRight != null) {
			bottomRight.x = x + (width - bottomRight.width());
			bottomRight.y = y + (height - bottomRight.height()) + 1;
		}
	}

	
	public void item( Item item ) {
		if (item == null) {
			active = false;
			topLeft.visible = topRight.visible = bottomRight.visible = false;
			icon.visible(false);
		} else {
			active = true;
			topLeft.visible = topRight.visible = bottomRight.visible = true;
			icon.visible(true);
			
			icon.view( item );
			
			topLeft.text( item.status()  );

            /*
			boolean isArmor = item instanceof Armor;
			boolean isWeapon = item instanceof Weapon;
			if (isArmor || isWeapon) {
				
				if (item.levelKnown || (isWeapon && !(item instanceof MeleeWeapon))) {
					int str = 0;//isArmor ? ((Armor)item).STR : ((Weapon)item).STR;
					topRight.text( Utils.format( TXT_STRENGTH, str ) );
					if (str > Dungeon.hero.STRCON) {
						topRight.hardlight( DEGRADED );
					} else {
						topRight.resetColor();
					}
					
				} else {
					topRight.text( Utils.format( TXT_TYPICAL_STR, isArmor ?
						((Armor)item).typicalSTR() :
						((MeleeWeapon)item).typicalSTR() ) );
					topRight.hardlight( WARNING );
				}
				topRight.measure();

			} else if (item instanceof OldKey && !(item instanceof SkeletonOldKey)) {
				topRight.text(Utils.format(TXT_KEY_DEPTH, ((OldKey) item).depth));
				topRight.measure();
			} else {
				
				topRight.text( null );
				
			}
	        */

			int level = item.visiblyUpgraded();

			if (level != 0) {
				bottomRight.text( item.levelKnown ? Utils.format( TXT_LEVEL, level ) : TXT_CURSED );
				bottomRight.measure();
				bottomRight.hardlight( level > 0 ? UPGRADED : DEGRADED );
			} else if (item instanceof Scroll || item instanceof Potion) {
				if (item instanceof Scroll) bottomRight.text(((Scroll) item).initials());
				else bottomRight.text(((Potion) item).initials());

				bottomRight.measure();

				if (item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion
						|| item instanceof PotionOfStrength || item instanceof PotionOfMight)
					bottomRight.hardlight( UPGRADED );
				else
					bottomRight.hardlight( FADED );

			} else {
				bottomRight.text( null );
			}
			
			layout();
		}
	}
	
	public void enable( boolean value ) {
		
		active = value;
		
		float alpha = value ? ENABLED : DISABLED;
		icon.alpha( alpha );
		topLeft.alpha( alpha );
		topRight.alpha( alpha );
		bottomRight.alpha( alpha );
	}

	public void showParams( boolean TL, boolean TR, boolean BR ) {
		if (TL) add( topLeft );
		else remove( topLeft );

		if (TR) add( topRight );
		else remove( topRight );

		if (BR) add( bottomRight );
		else remove( bottomRight );
	}
}
