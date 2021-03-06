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
package com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.simple;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroSubClass;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.consumable.scrolls.ScrollOfRecharging;
import com.felayga.unpixeldungeon.items.consumable.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.items.consumable.wands.Wand;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfDisintegration;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfFireblast;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfFrost;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfLightning;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfMagicMissile;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfRegrowth;
import com.felayga.unpixeldungeon.items.consumable.wands.WandOfVenom;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MagesStaff extends SimpleMeleeWeapon {

	private Wand wand;

	public static final String AC_IMBUE = "IMBUE";
	public static final String AC_ZAP	= "ZAP";

	private static final String TXT_SELECT_WAND	= "Select a wand to consume";

	private static final float STAFF_SCALE_FACTOR = 0.75f;

	public MagesStaff() {
		super(GameTime.TICK, 1, 6);

		wand = null;

		name = "staff";
		image = ItemSpriteSheet.MELEE_MAGES_STAFF;
        pickupSound = Assets.SND_ITEM_WOOD;
        material = Material.Wood;

		defaultAction = AC_ZAP;
		usesTargeting = true;

		unique = true;
		bones = false;
	}

	public MagesStaff(Wand wand){
		this();
		wand.identify();
		wand.bucStatus(BUCStatus.Uncursed);
		this.wand = wand;
		//wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
		//wand.curCharges = wand.maxCharges;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_IMBUE);
        actions.add( AC_ZAP );
		return actions;
	}

	/*
	@Override
	public void activate() {
		if(wand != null) wand.charge( hero, STAFF_SCALE_FACTOR );
	}
	*/

	@Override
	public boolean execute(Hero hero, String action) {
		if (action.equals(AC_IMBUE)) {
			curUser = hero;
			GameScene.selectItem(itemSelector, Wand.class, TXT_SELECT_WAND, null);

			return false;
		} else if (action.equals(AC_ZAP)){
			if (wand == null) {
				return false;
			}

			return wand.execute(hero, AC_ZAP);
		} else {
			return super.execute(hero, action);
		}
	}

	@Override
	public int proc(Char attacker, boolean ranged, Char defender, int damage) {
		damage = super.proc(attacker, ranged, defender, damage);

		if (wand != null && Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE) {
			//if (wand.curCharges < wand.maxCharges) wand.partialCharge += 0.33f;
			ScrollOfRecharging.charge(attacker);
			//wand.onHit(this, attacker, defender, damage);
		}

		return damage;
	}

	/*
	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)) {
			if (container.owner != null && wand != null) {
				wand.charge(container.owner, STAFF_SCALE_FACTOR);
			}
			return true;
		} else {
			return false;
		}
	}
	*/

	/*
	@Override
	public void onDetach( ) {
		if (wand != null) wand.stopCharging();
	}
	*/

	public Item imbueWand(Wand wand, Char owner){

		this.wand = null;

		GLog.p("You imbue your staff with the " + wand.getDisplayName());

		if (enchantment != null) {
			GLog.w("The conflicting magics erase the enchantment on your staff.");
			enchant(null);
		}

		//syncs the level of the two items.
		int targetLevel = Math.max(this.level(), wand.level());

		int staffLevelDiff = targetLevel - this.level();
		if (staffLevelDiff > 0)
			this.upgrade(null, staffLevelDiff);
		else if (staffLevelDiff < 0)
			this.upgrade(null, -Math.abs(staffLevelDiff));

		int wandLevelDiff = targetLevel - wand.level();
		if (wandLevelDiff > 0)
			wand.upgrade(null, wandLevelDiff);
		else if (wandLevelDiff < 0)
			wand.upgrade(null, -Math.abs(wandLevelDiff));

		this.wand = wand;
		//wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
		//wand.curCharges = wand.maxCharges;
		wand.identify();
		wand.bucStatus(BUCStatus.Uncursed);
		//wand.charge(owner);

		updateQuickslot();

		return this;

	}

	@Override
	public Item upgrade(Item source, int n) {
		super.upgrade( source, n );
		//STR = 10;
		//does not lose strength requirement

		if (wand != null) {
            /*
			int curCharges = wand.curCharges;
			wand.upgrade(source, n);
			//gives the wand one additional charge
			wand.maxCharges = Math.min(wand.maxCharges + n, 10);
			wand.curCharges = Math.min(wand.curCharges + n, 10);
			updateQuickslot();
			*/
		}

		return this;
	}

	@Override
	public String status() {
		if (wand == null) return super.status();
		else return wand.status();
	}

	@Override
	public String getName(){
		if (wand == null)
			return "mage's staff";
		else {
			String name = wand.getName().replace("Wand", "Staff");
			return enchantment == null ? name : enchantment.name( name );
		}
	}

	@Override
	public String info() {
		return super.info();
	}

	@Override
	public Emitter emitter() {
		if (wand == null) return null;
		Emitter emitter = new Emitter();
		emitter.pos(-1, 12.5f, 2.5f);
		emitter.fillTarget = false;
		emitter.pour(StaffParticleFactory, 0.06f);
		return emitter;
	}

	private static final String WAND = "wand";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND, wand);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		wand = (Wand) bundle.get(WAND);
		//if (wand != null) wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
	}

	@Override
	public String desc() {
		String result = "Crafted by the mage himself, this extraordinary staff is a one of a kind multi-purpose magical weapon.\n" +
				"\n" +
				"Rather than having an innate magic in it, this staff is instead imbued with magical energy from a wand, permanently granting it new power.\n" +
				"\n";

		if (wand == null) {
			result += "The staff is currently a slightly magical stick, it needs a wand!";
		} else if (wand instanceof WandOfMagicMissile){
			result += "The staff radiates consistent magical energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfFireblast){
			result += "The staff burns and sizzles with fiery energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfLightning){
			result += "The staff fizzes and crackles with electrical energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfDisintegration){
			result += "The staff hums with deep and powerful energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfVenom){
			result += "The staff drips and hisses with corrosive energy from the wand it is imbued with.";
		//} else if (wand instanceof WandOfPrismaticLight){
		//	result += "The staff glows and shimmers with bright energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfFrost){
			result += "The staff chills the air with the cold energy from the wand it is imbued with.";
		//} else if (wand instanceof WandOfBlastWave){
		//	result += "The staff pops and crackles with explosive energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfRegrowth){
			result += "The staff flourishes and grows with natural energy from the wand it is imbued with.";
		//} else if (wand instanceof WandOfTransfusion){
		//	result += "The staff courses and flows with life energy from the wand it is imbued with.";
		}

		return result;
	}

	private final WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect( final Item item ) {
			if (item != null) {

				if (!item.isIdentified()) {
					GLog.w("You'll need to identify this wand first.");
					return;
				} else if (item.bucStatus() == BUCStatus.Cursed){
					GLog.w("You can't use a cursed wand.");
					return;
				}

				GameScene.show(
						new WndOptions("",
								"Are you sure you want to imbue your staff with this " + item.getDisplayName() + "?\n\n" +
										"Your staff will inherit the highest level between it and the wand, " +
										"and all magic currently affecting the staff will be lost.",
								"Yes, i'm sure.",
								"No, I changed my mind") {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									Sample.INSTANCE.play(Assets.SND_EVOKE);
									ScrollOfUpgrade.upgrade(curUser);
									evoke(curUser);

									Dungeon.quickslot.clearItem(item);

									curUser.belongings.remove(item, 1);

									imbueWand((Wand) item, curUser);

									curUser.spend_new(GameTime.TICK * 2, true);

									updateQuickslot();
								}
							}
						}
				);
			}
		}
	};

	private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
		@Override
		//reimplementing this is needed as instance creation of new staff particles must be within this class.
		public void emit( Emitter emitter, int index, float x, float y ) {
			StaffParticle c = (StaffParticle)emitter.getFirstAvailable(StaffParticle.class);
			if (c == null) {
				c = new StaffParticle();
				emitter.add(c);
			}
			c.reset(x, y);
		}

		@Override
		//some particles need light mode, others don't
		public boolean lightMode() {
			return !((wand instanceof WandOfDisintegration)
					//|| (wand instanceof WandOfCorruption)
					|| (wand instanceof WandOfRegrowth));
		}
	};

	//determines particle effects to use based on wand the staff owns.
	public class StaffParticle extends PixelParticle{

		private float minSize;
		private float maxSize;
		public float sizeJitter = 0;

		public StaffParticle(){
			super();
		}

		public void reset( float x, float y ) {
			revive();

			speed.set(0);

			this.x = x;
			this.y = y;

			if (wand != null)
				wand.staffFx( this );

		}

		public void setSize( float minSize, float maxSize ){
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public void setLifespan( float life ){
			lifespan = left = life;
		}

		public void shuffleXY(float amt){
			x += Random.Float(-amt, amt);
			y += Random.Float(-amt, amt);
		}

		public void radiateXY(float amt){
			float hypot = (float)Math.hypot(speed.x, speed.y);
			this.x += speed.x/hypot*amt;
			this.y += speed.y/hypot*amt;
		}

		@Override
		public void update() {
			super.update();
			size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
		}
	}
}
