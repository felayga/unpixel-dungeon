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
package com.felayga.unpixeldungeon.items.artifacts;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.KindofMisc;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Artifact extends KindofMisc {

	private static final long TIME_TO_EQUIP = GameTime.TICK;

	private static final String TXT_TO_STRING		        = "%s";
	private static final String TXT_TO_STRING_CHARGE		= "%s (%d/%d)";
	private static final String TXT_TO_STRING_LVL	        = "%s%+d";
	private static final String TXT_TO_STRING_LVL_CHARGE	= "%s%+d (%d/%d)";

	private static final String TXT_UNEQUIP_TITLE = "Unequip one item";
	private static final String TXT_UNEQUIP_MESSAGE =
			"You can only wear two misc items at a time.";

	protected Buff passiveBuff;
	protected Buff activeBuff;

	//level is used internally to track upgrades to artifacts, size/logic varies per artifact.
	//already inherited from item superclass
	//exp is used to count progress towards levels for some artifacts
	protected int exp = 0;
	//levelCap is the artifact's maximum level
	protected int levelCap = 0;

	//the current artifact charge
	protected int charge = 0;
	//the build towards next charge, usually rolls over at 1.
	//better to keep charge as an int and use a separate float than casting.
	protected float partialCharge = 0;
	//the maximum charge, varies per artifact, not all artifacts use this.
	protected int chargeCap = 0;

	//used by some artifacts to keep track of duration of effects or cooldowns to use.
	protected int cooldown = 0;


	public Artifact(){
		super();
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}

	@Override
	public boolean doEquip( final Char hero ) {

		if ((hero.belongings.ring1 != null && hero.belongings.ring1.getClass() == this.getClass())
				|| (hero.belongings.ring2 != null && hero.belongings.ring2.getClass() == this.getClass())
				){

			GLog.w("you cannot wear two of the same artifact");
			return false;

		} else if (hero.belongings.ring1 != null && hero.belongings.ring2 != null) {

			final KindofMisc m1 = hero.belongings.ring1;
			final KindofMisc m2 = hero.belongings.ring2;
			final Artifact art = this;

			ShatteredPixelDungeon.scene().add(
					new WndOptions(TXT_UNEQUIP_TITLE, TXT_UNEQUIP_MESSAGE,
							Utils.capitalize(m1.toString()),
							Utils.capitalize(m2.toString())) {

						@Override
						protected void onSelect(int index) {
							KindofMisc equipped;
                            switch(index) {
                                case 0:
                                    equipped = m1;
                                    break;
                                default:
                                    equipped = m2;
                                    break;
                            }
							if (equipped.doUnequip(hero, true, false)) {
								int slot = Dungeon.quickslot.getSlot( art );
								doEquip(hero);
								if (slot != -1) {
									Dungeon.quickslot.setSlot( slot, art );
									updateQuickslot();
								}
							}
						}
					});

			return false;

		} else {

			if (hero.belongings.ring1 == null) {
				hero.belongings.ring1 = this;
			} else {
				hero.belongings.ring2 = this;
			}

			hero.belongings.detach(this);

			activate( hero );

			bucStatusKnown = true;
			identify();
			if (bucStatus == BUCStatus.Cursed) {
				equipCursed( hero );
				GLog.n( "the " + this.name + " painfully binds itself to you" );
			}

			hero.spend( TIME_TO_EQUIP, true );
			return true;

		}

	}

	public void activate( Char ch ) {
		passiveBuff = passiveBuff();
		passiveBuff.attachTo(ch);
	}

	@Override
	public boolean doUnequip( Char hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

		if (hero.belongings.ring1 == this) {
			hero.belongings.ring1 = null;
		} else {
			hero.belongings.ring2 = null;
		}

		passiveBuff.detach();
		passiveBuff = null;

		if (activeBuff != null){
			activeBuff.detach();
			activeBuff = null;
		}

		return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped( Char hero ) {
		return hero.belongings.ring1 == this || hero.belongings.ring2 == this;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public int visiblyUpgraded() {
		return ((level*10)/levelCap);
	}

	//transfers upgrades from another artifact, transfer level will equal the displayed level
	public void transferUpgrade(int transferLvl) {
		upgrade(null, Math.round((float)(transferLvl*levelCap)/10));
	}

	@Override
	public String info() {
		if (bucStatus == BUCStatus.Cursed && bucStatusKnown && !isEquipped( Dungeon.hero )) {

			return desc() + "\n\nYou can feel a malevolent magic lurking within the " + getDisplayName() + ".";

		} else {

			return desc();

		}
	}

	@Override
	public String toString() {

		if (levelKnown && level/levelCap != 0) {
			if (chargeCap > 0) {
				return Utils.format( TXT_TO_STRING_LVL_CHARGE, getDisplayName(), visiblyUpgraded(), charge, chargeCap );
			} else {
				return Utils.format( TXT_TO_STRING_LVL, getDisplayName(), visiblyUpgraded() );
			}
		} else {
			if (chargeCap > 0) {
				return Utils.format( TXT_TO_STRING_CHARGE, getDisplayName(), charge, chargeCap );
			} else {
				return Utils.format( TXT_TO_STRING, getDisplayName() );
			}
		}
	}

	@Override
	public String status() {

		//display the current cooldown
		if (cooldown != 0)
			return Utils.format( "%d", cooldown );

		//display as percent
		if (chargeCap == 100)
			return Utils.format( "%d%%", charge );

		//display as #/#
		if (chargeCap > 0)
			return Utils.format( "%d/%d", charge, chargeCap );

		//if there's no cap -
		//- but there is charge anyway, display that charge
		if (charge != 0)
			return Utils.format( "%d", charge );

		//otherwise, if there's no charge, return null.
		return null;
	}

	//converts class names to be more concise and readable.
	protected String convertName(String className){
		//removes known redundant parts of names.
		className = className.replaceFirst("ScrollOf|PotionOf", "");

		//inserts a space infront of every uppercase character
		className = className.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");

		return className;
	};

	@Override
	public Item random() {
		if (Random.Float() < 0.3f) {
			bucStatus = BUCStatus.Cursed;
		}
		return this;
	}

	@Override
	public int price() {
		int price = 100;
		if (level > 0)
			price += 50*((level*10)/levelCap);
		if (bucStatus == BUCStatus.Cursed) {
			price /= 2;
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}


	protected ArtifactBuff passiveBuff() {
		return null;
	}

	protected ArtifactBuff activeBuff() {return null; }

	public class ArtifactBuff extends Buff {

		public int level() {
			return level;
		}

		public boolean isCursed() {
			return bucStatus == BUCStatus.Cursed;
		}

	}

	private static final String IMAGE = "image";
	private static final String EXP = "exp";
	private static final String CHARGE = "charge";
	private static final String PARTIALCHARGE = "partialcharge";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( IMAGE, image );
		bundle.put( EXP , exp );
		bundle.put( CHARGE , charge );
		bundle.put( PARTIALCHARGE , partialCharge );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains( IMAGE )) image = bundle.getInt( IMAGE );
		exp = bundle.getInt( EXP );
		charge = bundle.getInt( CHARGE );
		partialCharge = bundle.getFloat( PARTIALCHARGE );
	}
}
