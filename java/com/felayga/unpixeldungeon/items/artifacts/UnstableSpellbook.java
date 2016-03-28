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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.LockedFloor;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.particles.ElmoParticle;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class UnstableSpellbook extends Artifact {

	{
		name = "Unstable Spellbook";
		image = ItemSpriteSheet.ARTIFACT_SPELLBOOK;

		level = 0;
		levelCap = 10;

		charge = ((level/2)+3);
		partialCharge = 0;
		chargeCap = ((level/2)+3);

		defaultAction = AC_READ;
	}

	public static final String AC_READ = "READ";
	public static final String AC_ADD = "ADD";

	private final ArrayList<String> scrolls = new ArrayList<String>();

	protected String inventoryTitle = "Select a scroll";
	protected WndBackpack.Mode mode = WndBackpack.Mode.SCROLL;

	public UnstableSpellbook() {
		super();

		Class<?>[] scrollClasses = Generator.Category.SCROLL.classes;
		float[] probs = Generator.Category.SCROLL.probs.clone(); //array of primitives, clone gives deep copy.
		int i = Random.chances(probs);

		while (i != -1){
			scrolls.add(convertName(scrollClasses[i].getSimpleName()));
			probs[i] = 0;

			i = Random.chances(probs);
		};
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge > 0 && bucStatus != BUCStatus.Cursed)
			actions.add(AC_READ);
		if (isEquipped( hero ) && level < levelCap && bucStatus != BUCStatus.Cursed)
			actions.add(AC_ADD);
		return actions;
	}

	@Override
	public boolean execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			if (hero.buff( Blindness.class ) != null) GLog.w("You cannot read from the book while blinded.");
			else if (!isEquipped( hero ))             GLog.i("You need to equip your spellbook to do that.");
			else if (charge == 0)                     GLog.i("Your spellbook is out of energy for now.");
			else if (bucStatus == BUCStatus.Cursed)   GLog.i("Your cannot read from a cursed spellbook.");
			else {
				charge--;

				Scroll scroll;
				do {
					scroll = (Scroll) Generator.random(Generator.Category.SCROLL);
				} while (scroll == null ||
						//gotta reduce the rate on these scrolls or that'll be all the item does.
						((scroll instanceof ScrollOfIdentify ||
							scroll instanceof ScrollOfRemoveCurse ||
							scroll instanceof ScrollOfMagicMapping) && Random.Int(2) == 0));

				scroll.ownedByBook = true;
				scroll.execute(hero, AC_READ);
			}
			return false;
		} else if (action.equals( AC_ADD )) {
			GameScene.selectItem(itemSelector, mode, inventoryTitle);
			return false;
		} else {
			return super.execute(hero, action);
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new bookRecharge();
	}

	@Override
	public Item upgrade(Item source, int n) {
		chargeCap = (((level+1)/2)+3);

		//for artifact transmutation.
		while (scrolls.size() > (levelCap-1-level))
			scrolls.remove(0);

		return super.upgrade(source, n);
	}

	@Override
	public String desc() {
		String desc = "This Tome is in surprising good condition given its age. ";

		if (level < 3)
			desc += "It emanates a strange chaotic energy. ";
		else if (level < 7)
			desc += "It glows with a strange chaotic energy. ";
		else
			desc += "It fizzes and crackles as you move the pages, surging with unstable energy. ";

		desc += "It seems to contains a list of spells, but the order and position of them in the index is " +
				"constantly shifting. If you read from this book, there's no telling what spell you might cast.";

		desc += "\n\n";

		if (isEquipped (Dungeon.hero)) {

			if (bucStatus != BUCStatus.Cursed)
				desc += "The book fits firmly at your side, sending you the occasional zip of static energy.";
			else
				desc += "The cursed book has bound itself to you, it is inhibiting your ability to use most scrolls.";

			desc += "\n\n";

		}

			if (level < levelCap)
				if (scrolls.size() > 1)
					desc += "The book's index points to some pages which are blank. " +
							"Those pages are listed as: " + scrolls.get(0) + " and "
							+ scrolls.get(1) + ". Perhaps adding to the book will increase its power";
				else
					desc += "The book's index has one remaining blank page. " +
							"That page is listed as " + scrolls.get(0) + ".";
			 else
				desc += "The book's index is full, it doesn't look like you can add anything more to it.";

		return desc;
	}

	private static final String SCROLLS =   "scrolls";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( SCROLLS, scrolls.toArray(new String[scrolls.size()]) );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		scrolls.clear();
		Collections.addAll(scrolls, bundle.getStringArray(SCROLLS));
	}

	public class bookRecharge extends ArtifactBuff{
		@Override
		public boolean act() {
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && bucStatus != BUCStatus.Cursed && (lock == null || lock.regenOn())) {
				partialCharge += 1 / (150f - (chargeCap - charge)*15f);

				if (partialCharge >= 1) {
					partialCharge --;
					charge ++;

					if (charge == chargeCap){
						partialCharge = 0;
					}
				}
			}

			updateQuickslot();

			spend(GameTime.TICK, false );

			return true;
		}
	}

	protected WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof Scroll && item.isIdentified()){
				String scroll = convertName(item.getClass().getSimpleName());
				Hero hero = Dungeon.hero;
				for (int i = 0; ( i <= 1 && i < scrolls.size() ); i++){
					if (scrolls.get(i).equals(scroll)){
						hero.sprite.operate(hero.pos);
						hero.busy();
						hero.spend(GameTime.TICK * 2, false );
						Sample.INSTANCE.play(Assets.SND_BURNING);
						hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);

						scrolls.remove(i);
						hero.belongings.detach(item);

						upgrade(item, 1);
						GLog.i("You infuse the scroll's energy into the book.");
						return;
					}
				}
				if (item != null)
					GLog.w("You are unable to add this scroll to the book.");
			} else if (item instanceof Scroll && !item.isIdentified())
				GLog.w("You're not sure what type of scroll this is yet.");
		}
	};
}
