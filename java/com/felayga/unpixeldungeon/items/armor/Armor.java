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
package com.felayga.unpixeldungeon.items.armor;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.armor.glyphs.Affection;
import com.felayga.unpixeldungeon.items.armor.glyphs.AntiEntropy;
import com.felayga.unpixeldungeon.items.armor.glyphs.Bounce;
import com.felayga.unpixeldungeon.items.armor.glyphs.Displacement;
import com.felayga.unpixeldungeon.items.armor.glyphs.Entanglement;
import com.felayga.unpixeldungeon.items.armor.glyphs.Metabolism;
import com.felayga.unpixeldungeon.items.armor.glyphs.Multiplicity;
import com.felayga.unpixeldungeon.items.armor.glyphs.Potential;
import com.felayga.unpixeldungeon.items.armor.glyphs.Stench;
import com.felayga.unpixeldungeon.items.armor.glyphs.Viscosity;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Armor extends EquippableItem {

	private static final int HITS_TO_KNOW    = 10;

	private static final String TXT_EQUIP_CURSED_HERO	= "your %s constricts around you painfully";
	private static final String TXT_EQUIP_CURSED_OTHER	= "the %s winces as their armor tightens";
	
	private int hitsToKnow = HITS_TO_KNOW;
	
	public Glyph glyph;

	public int armor;
	public int armorMagic;
	public int armorBonusMaximum;
	public long speedModifier;
	public int price;
	public int textureIndex;
	public int spellFailure;


	public Armor(int armor, int armorBonusMaximum, int armorMagic, long speedModifier, long equipTime, int spellFailure) {
		super(equipTime);

        pickupSound = Assets.SND_ITEM_CLOTH;

		price = 0;

		this.armor = armor;
		this.armorBonusMaximum = armorBonusMaximum;
		this.armorMagic = armorMagic;
		this.speedModifier = speedModifier;
		this.spellFailure = spellFailure;
		textureIndex = 1;
	}

	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String GLYPH			= "glyph";
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(UNFAMILIRIARITY, hitsToKnow);
		bundle.put(GLYPH, glyph);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		enchant((Glyph) bundle.get(GLYPH));
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(isEquipped(hero) ? Constant.Action.UNEQUIP : Constant.Action.EQUIP);
		return actions;
	}

	public int defense() {
		return armor + level();
	}

	public Slot[] getSlots() {
		return new Slot[]{ Slot.Armor };
	}

	@Override
	public void onEquip(Char owner, boolean cursed) {
		super.onEquip(owner, cursed);

		if (cursed) {
			if (owner == Dungeon.hero) {
				GLog.n(TXT_EQUIP_CURSED_HERO, getDisplayName());
				setHeroSpriteArmor((HeroSprite)owner.sprite, true);
			}
			else {
                //todo: fix cursed message visibility name
				GLog.d(TXT_EQUIP_CURSED_OTHER, getDisplayName());
			}
		}
        else {
            if (owner == Dungeon.hero) {
                setHeroSpriteArmor((HeroSprite)owner.sprite, true);
            }
        }
	}

	@Override
	public void onUnequip(Char owner) {
		super.onUnequip(owner);

		if (owner == Dungeon.hero) {
			setHeroSpriteArmor((HeroSprite) owner.sprite, false);
		}
	}


	protected void setHeroSpriteArmor(HeroSprite heroSprite, boolean equip) {
        if (heroSprite == null) {
            return;
        }
		if (equip) {
			heroSprite.setArmor(this.textureIndex);
		}
		else {
			heroSprite.setArmor(0);
		}
	}

	
	public int proc( Char attacker, Char defender, int damage ) {
		
		if (glyph != null) {
			damage = glyph.proc( this, attacker, defender, damage );
		}
		
		if (!levelKnown()) {
			if (--hitsToKnow <= 0) {
				levelKnown(true, true);
			}
		}
		
		return damage;
	}

	
	@Override
	public String getName() {
		String name = super.getName();

		if (glyph != null)
		{
			name = glyph.name(name);
		}

		return name;
	}

	@Override
	public String getDisplayName() {
		String name = super.getDisplayName();

		if (hasLevels() && levelKnown()) {
			if (level() >= 0) {
				name = "+" + level() + " " + name;
			} else {
				name = level() + " " + name;
			}
		}

		return name;
	}
	
	@Override
	public String info() {
		String name = getDisplayName();
		StringBuilder info = new StringBuilder( desc() );
		
		if (levelKnown()) {
			/*
			info.append(
				"\n\nThis " + name + " provides damage absorption up to " +
				"" + Math.max( DR, 0 ) + " points per attack. " );
			*/
			/*
			if (STR > Dungeon.hero.STR()) {
				
				if (isEquipped( Dungeon.hero )) {
					info.append(
						"\n\nBecause of your inadequate strength your " +
						"movement speed and defense skill is decreased. " );
				} else {
					info.append(
						"\n\nBecause of your inadequate strength wearing this armor " +
						"will decrease your movement speed and defense skill. " );
				}
				
			}
			*/
		} else {
			/*
			info.append(
				"\n\nTypical " + name + " provides damage absorption up to " + typicalDR() + " points per attack " +
				" and requires " + typicalSTR() + " points of strength. " );
			*/
			/*
			if (typicalSTR() > Dungeon.hero.STR()) {
				info.append( "Probably this armor is too heavy for you. " );
			}
			*/
		}
		
		if (glyph != null) {
			info.append( "It is inscribed." );
		}
		
		if (isEquipped( Dungeon.hero )) {
			info.append( "\n\nYou are wearing the " + name +
				(bucStatus() == BUCStatus.Cursed ? ", and because it is cursed, you are powerless to remove it." : ".") );
		} else {
			if (bucStatusKnown() && bucStatus() == BUCStatus.Cursed) {
				info.append("\n\nYou can feel a malevolent magic lurking within the " + name + ".");
			}
		}
		
		return info.toString();
	}

	@Override
	public Item random() {
		super.random();
		
		if (Random.Int( 10 ) == 0) {
			enchant();
		}
		
		return this;
	}

	public Armor enchant( Glyph glyph ) {
		this.glyph = glyph;

		return this;
	}

	public Armor enchant() {

		Class<? extends Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Glyph gl = Glyph.random();
		while (gl.getClass() == oldGlyphClass) {
			gl = Armor.Glyph.random();
		}

		return enchant(gl);
	}

	public boolean isInscribed() {
		return glyph != null;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null ? glyph.glowing() : null;
	}
	
	public static abstract class Glyph implements Bundlable {
		
		private static final Class<?>[] glyphs = new Class<?>[]{
			Bounce.class, Affection.class, AntiEntropy.class, Multiplicity.class,
			Potential.class, Metabolism.class, Stench.class, Viscosity.class,
			Displacement.class, Entanglement.class };
		
		private static final float[] chances= new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
		public abstract int proc( Armor armor, Char attacker, Char defender, int damage );
		
		public String name() {
			return name( "glyph" );
		}
		
		public String name( String armorName ) {
			return armorName;
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}
		
		public ItemSprite.Glowing glowing() {
			return ItemSprite.Glowing.WHITE;
		}
		
		public boolean checkOwner( Char owner ) {
			if (!owner.isAlive() && owner == Dungeon.hero) {

				Dungeon.fail( Utils.format( ResultDescriptions.GLYPH, name() ) );
				GLog.n( "%s killed you...", name() );

				Badges.validateDeathFromGlyph();
				return true;
				
			} else {
				return false;
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph random() {
			try {
				return ((Class<Glyph>)glyphs[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		
	}
}
