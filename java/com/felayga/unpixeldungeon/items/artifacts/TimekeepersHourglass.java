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
package com.felayga.unpixeldungeon.items.artifacts;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.actors.buffs.hero.LockedFloor;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TimekeepersHourglass extends Artifact_old {

	private static final String TXT_HGLASS	= "Timekeeper's Hourglass";
	private static final String TXT_STASIS	= "Put myself in stasis";
	private static final String TXT_FREEZE	= "Freeze time around me";
	private static final String TXT_DESC 	=
			"How would you like to use the hourglass's magic?\n\n" +
			"While in stasis, time will move normally while you are frozen and completely invulnerable.\n\n" +
			"When time is frozen, you can move as if your actions take no time. Note that attacking will break this.";

    public TimekeepersHourglass()
	{
		name = "Timekeeper's Hourglass";
		image = ItemSpriteSheet.ARTIFACT_HOURGLASS;

		level(0);
		levelCap = 5;

		charge = 10+level()*2;
		partialCharge = 0;
		chargeCap = 10+level()*2;

		defaultAction = AC_ACTIVATE;
        price = 20;
	}

	public static final String AC_ACTIVATE = "ACTIVATE";

	//keeps track of generated sandbags.
	public int sandBags = 0;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge > 0 && bucStatus() != BUCStatus.Cursed)
			actions.add(AC_ACTIVATE);
		return actions;
	}

	@Override
	public boolean execute( Hero hero, String action ) {
		if (action.equals(AC_ACTIVATE)){
			if (!isEquipped( hero ))        GLog.i("You need to equip your hourglass to do that.");
			else if (activeBuff != null)    GLog.i("Your hourglass is already in use.");
			else if (charge <= 1)           GLog.i("Your hourglass hasn't recharged enough to be usable yet.");
			else if (bucStatus() == BUCStatus.Cursed)                GLog.i("You cannot use a cursed hourglass.");
			else GameScene.show(
						new WndOptions(TXT_HGLASS, TXT_DESC, TXT_STASIS, TXT_FREEZE) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									GLog.i("The world seems to shift around you in an instant.");
									GameScene.flash(0xFFFFFF);
									Sample.INSTANCE.play(Assets.SND_TELEPORT);

									activeBuff = new timeStasis();
									activeBuff.attachTo(Dungeon.hero, null);
								} else if (index == 1) {
									GLog.i("everything around you suddenly freezes.");
									GameScene.flash(0xFFFFFF);
									Sample.INSTANCE.play(Assets.SND_TELEPORT);

									activeBuff = new timeFreeze();
									activeBuff.attachTo(Dungeon.hero, null);
								}
							};
						}
				);

			return false;
		} else {
			return super.execute(hero, action);
		}
	}

    @Override
    public void onEquip(Char owner, boolean cursed) {
        super.onEquip(owner, cursed);

        if (activeBuff != null) {
            activeBuff.attachTo(owner, null);
        }
    }

	@Override
	public void onUnequip(Char owner) {
		if (activeBuff != null){
			activeBuff.detach();
			activeBuff = null;
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new hourglassRecharge();
	}

	@Override
	public Item upgrade(Item source, int n) {
		chargeCap += 2 * n;

		//for artifact transmutation.
		while (level()+1 > sandBags)
			sandBags ++;

		return super.upgrade(source, n);
	}

	@Override
	public String desc() {
		String desc =
				"This large ornate hourglass looks fairly unassuming, but you feel a great power in its finely carved" +
				" frame. As you rotate the hourglass and watch the sand pour you can feel its magic tugging at you, " +
				"surely invoking this magic would give you some power over time.";

		if (isEquipped( Dungeon.hero )){
			if (bucStatus() != BUCStatus.Cursed) {
				desc += "\n\nThe hourglass rests at your side, the whisper of steadily pouring sand is reassuring.";

				if (level() < levelCap )
					desc +=
						"\n\nThe hourglass seems to have lost some sand with age. While there are no cracks, " +
						"there is a port on the top of the hourglass to pour sand in, if only you could find some...";
			}else
				desc += "\n\nThe cursed hourglass is locked to your side, " +
						"you can feel it trying to manipulate your flow of time.";
		}
		return desc;
	}


	private static final String SANDBAGS =  "sandbags";
	private static final String BUFF =      "buff";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( SANDBAGS, sandBags );

		if (activeBuff != null)
			bundle.put( BUFF , activeBuff );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		sandBags = bundle.getInt( SANDBAGS );

		//these buffs belong to hourglass, need to handle unbundling within the hourglass class.
		if (bundle.contains( BUFF )){
			Bundle buffBundle = bundle.getBundle( BUFF );

			if (buffBundle.contains( timeFreeze.PARTIALTIME ))
				activeBuff = new timeFreeze();
			else
				activeBuff = new timeStasis();

			activeBuff.restoreFromBundle(buffBundle);
		}
	}

	public class hourglassRecharge extends ArtifactBuff {
		@Override
		public boolean act() {

			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && bucStatus() != BUCStatus.Cursed && (lock == null || lock.regenOn())) {
				partialCharge += 1 / (60f - (chargeCap - charge)*2f);

				if (partialCharge >= 1) {
					partialCharge --;
					charge ++;

					if (charge == chargeCap){
						partialCharge = 0;
					}
				}
			} else if (bucStatus() == BUCStatus.Cursed && Random.Int(10) == 0)
				target.spend_new(GameTime.TICK, false);

			updateQuickslot();

            spend_new(GameTime.TICK, false);

			return true;
		}
	}

	public class timeStasis extends ArtifactBuff {

		@Override
		public boolean attachTo(Char target, Char source) {
			//buffs always act last, so the stasis buff should end a turn early.
            spend_new(GameTime.TICK * (charge - 1), false);
			target.spend_new(GameTime.TICK * charge, true);

			//shouldn't punish the player for going into stasis frequently
			Hunger hunger = target.buff(Hunger.class);
			if (hunger != null && !hunger.isStarving())
				hunger.satisfy_new(charge);

			charge = 0;

			target.invisible++;

			updateQuickslot();

			Dungeon.observe();

			return super.attachTo(target, source);
		}

		@Override
		public boolean act() {
			detach();
			return true;
		}

		@Override
		public void detach() {
			if (target.invisible > 0)
				target.invisible --;
			super.detach();
			activeBuff = null;
			Dungeon.observe();
		}
	}

	public class timeFreeze extends ArtifactBuff {

		float partialTime = 0f;

		ArrayList<Integer> presses = new ArrayList<Integer>();

		public boolean processTime(double time){
			partialTime += time;

			while (partialTime >= 1f){
				partialTime --;
				charge --;
			}

			updateQuickslot();

			if (charge <= 0){
				detach();
				return false;
			} else
				return true;

		}

		public void setDelayedPress(int cell){
			if (!presses.contains(cell))
				presses.add(cell);
		}

		public void triggerPresses(){
			for (int cell : presses)
				Dungeon.level.press(cell, null);

			presses = new ArrayList<Integer>();
		}

		@Override
		public boolean attachTo(Char target, Char source) {
			if (Dungeon.level != null)
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
					mob.sprite.add(CharSprite.State.PARALYSED);
			GameScene.freezeEmitters = true;
			return super.attachTo(target, source);
		}

		@Override
		public void detach(){
			triggerPresses();
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
				mob.sprite.remove(CharSprite.State.PARALYSED);
			GameScene.freezeEmitters = false;

			charge = 0;
			updateQuickslot();
			super.detach();
			activeBuff = null;
		}

		private static final String PRESSES = "presses";
		private static final String PARTIALTIME = "partialtime";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			int[] values = new int[presses.size()];
			for (int i = 0; i < values.length; i ++)
				values[i] = presses.get(i);
			bundle.put( PRESSES , values );

			bundle.put( PARTIALTIME , partialTime );
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			int[] values = bundle.getIntArray( PRESSES );
			for (int value : values)
				presses.add(value);

			partialTime = bundle.getFloat( PARTIALTIME );
		}
	}

	public static class sandBag extends Item {

		{
			name = "bag of magic sand";
			image = ItemSpriteSheet.SANDBAG;
		}

		@Override
		public boolean doPickUp( Hero hero ) {
			TimekeepersHourglass hourglass = hero.belongings.getItem( TimekeepersHourglass.class, true );
			if (hourglass != null && hourglass.bucStatus() != BUCStatus.Cursed) {
				hourglass.upgrade(this, 1);
				Sample.INSTANCE.play( Assets.SND_DEWDROP );
				if (hourglass.level() == hourglass.levelCap)
					GLog.p("Your hourglass is filled with magical sand!");
				else
					GLog.i("you add the sand to your hourglass.");
				hero.spend_new(Constant.Time.ITEM_PICKUP, true);
				return true;
			} else {
				GLog.w("You have no hourglass to place this sand into.");
				return false;
			}
		}

		@Override
		public String desc(){
			return "This small bag of finely ground sand should work perfectly with your hourglass.\n\n" +
					"It seems odd that the shopkeeper would have this specific item right when you need it.";
		}

	}


}
