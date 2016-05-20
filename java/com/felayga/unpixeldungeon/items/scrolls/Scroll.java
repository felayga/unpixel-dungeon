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
package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.actors.buffs.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemStatusHandler;
import com.felayga.unpixeldungeon.items.artifacts.UnstableSpellbook;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Scroll extends Item {

	private static final String TXT_BLINDED	= "You can't read a scroll while blinded";

	private static final String TXT_CURSED	= "Your cursed spellbook prevents you from invoking this scroll's magic! " +
											  "A scroll of remove curse might be strong enough to still work though...";
	
	public static final String AC_READ	= "READ";
	
	protected static final long TIME_TO_READ	= GameTime.TICK;

	protected String initials;

	private static final Class<?>[] scrolls = {
		ScrollOfIdentify.class,
		ScrollOfMagicMapping.class,
		ScrollOfRecharging.class,
		ScrollOfRemoveCurse.class,
		ScrollOfTeleportation.class,
		ScrollOfUpgrade.class,
		ScrollOfRage.class,
		ScrollOfTerror.class,
		ScrollOfLullaby.class,
		ScrollOfMagicalInfusion.class,
		ScrollOfPsionicBlast.class,
		ScrollOfMirrorImage.class,
		BlankScroll.class
	};
	private static final String[] runes =
		//{"KAUNAN", "SOWILO", "LAGUZ", "YNGVI", "GYFU", "RAIDO", "ISAZ", "MANNAZ", "NAUDIZ", "BERKANAN", "ODAL", "TIWAZ"};
		{
				"ZELGO MER",
				"JUYED AWK YACC",
				"NR 9",
				"XIXAXA XOXAXA XUXAXA",
				"PRATYAVAYAH",
				"DAIYEN FOOELS",
				"LEP GEX VEN ZEA",
				"PRIRUTSENIE",
				"ELBIB YLOH",
				"VERR YED HORRE",
				"VENZAR BORGAVVE",
				"THARR",
				"YUM YUM",
				"KERNOD WEL",
				"ELAM EBOW",
				"DUAM XNAHT",
				"ANDOVA BEGARIN",
				"KIRJE",
				"VE FORBRYDERNE",
				"HACKEM MUCHE",
				"VELOX NEB",
				"FOOBIE BLETCH",
				"TEMOV",
				"GARVEN DEH",
				"READ ME",
				"blank"
		};
	private static final Integer[] images = {
			ItemSpriteSheet.SCROLL_01,
			ItemSpriteSheet.SCROLL_02,
			ItemSpriteSheet.SCROLL_03,
			ItemSpriteSheet.SCROLL_04,
			ItemSpriteSheet.SCROLL_05,
			ItemSpriteSheet.SCROLL_06,
			ItemSpriteSheet.SCROLL_07,
			ItemSpriteSheet.SCROLL_08,
			ItemSpriteSheet.SCROLL_09,
			ItemSpriteSheet.SCROLL_10,
			ItemSpriteSheet.SCROLL_11,
			ItemSpriteSheet.SCROLL_12,
			ItemSpriteSheet.SCROLL_13,
			ItemSpriteSheet.SCROLL_14,
			ItemSpriteSheet.SCROLL_15,
			ItemSpriteSheet.SCROLL_16,
			ItemSpriteSheet.SCROLL_17,
			ItemSpriteSheet.SCROLL_18,
			ItemSpriteSheet.SCROLL_19,
			ItemSpriteSheet.SCROLL_20,
			ItemSpriteSheet.SCROLL_21,
			ItemSpriteSheet.SCROLL_22,
			ItemSpriteSheet.SCROLL_23,
			ItemSpriteSheet.SCROLL_24,
			ItemSpriteSheet.SCROLL_25,
			ItemSpriteSheet.SCROLL_BLANK
	};
	
	private static ItemStatusHandler<Scroll> handler;
	
	private String rune;

	public boolean ownedByBook = false;
	
	@SuppressWarnings("unchecked")
	public static void initLabels() {
		handler = new ItemStatusHandler<Scroll>( (Class<? extends Scroll>[])scrolls, runes, images, 1 );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Scroll>( (Class<? extends Scroll>[])scrolls, runes, images, bundle );
	}
	
	public Scroll() {
		super();
		syncVisuals();

		stackable = true;
		defaultAction = AC_READ;

		weight = Encumbrance.UNIT * 5;
	}

	@Override
	public void syncVisuals(){
		image = handler.image( this );
		rune = handler.label( this );
	};
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_READ);
		return actions;
	}
	
	@Override
	public boolean execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			if (hero.buff( Blindness.class ) != null) {
				GLog.w( TXT_BLINDED );
			} else if (hero.buff(UnstableSpellbook.bookRecharge.class) != null
					&& hero.buff(UnstableSpellbook.bookRecharge.class).isCursed()
					&& !(this instanceof ScrollOfRemoveCurse)) {
				GLog.n( TXT_CURSED );
			} else {
				prepareRead(hero);
				doRead();
			}

			return false;
		} else {
			return super.execute( hero, action );
		}
	}

	protected void prepareRead(Hero hero) {
		curUser = hero;
		curItem = hero.belongings.remove(this, 1);
	}

	abstract protected void doRead();
	
	public boolean isKnown() {
		return handler.isKnown( this );
	}
	
	public boolean setKnown() {
		if (!isKnown() && !ownedByBook) {
			handler.know( this );

			Badges.validateAllScrollsIdentified();

			return true;
		}

		return false;
	}
	
	@Override
	public Item identify(boolean updateQuickslot) {
		if (setKnown()) {
			updateQuickslot = true;
		}
		return super.identify(updateQuickslot);
	}
	
	@Override
	public String getName() {
		return isKnown() ? super.getName() : "scroll \"" + rune + "\"";
	}
	
	@Override
	public String info() {
		return isKnown() ?
			desc() :
			"This parchment is covered with indecipherable writing, and bears a title " +
			"of \"" + rune + "\". Who knows what it will do when read aloud?";
	}

	public String initials(){
		return isKnown() ? initials : null;
	}

	@Override
	public void playPickupSound() {
		Sample.INSTANCE.play( Assets.SND_ITEM_PAPER );
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown();
	}
	
	public static HashSet<Class<? extends Scroll>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Scroll>> getUnknown() {
		return handler.unknown();
	}
	
	public static boolean allKnown() {
		return handler.known().size() == scrolls.length;
	}
	
	@Override
	public int price() {
		return 15 * quantity;
	}
}
