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
package com.felayga.unpixeldungeon.items.artifacts.unused;
/*
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Roots;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.EarthParticle;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.plants.Earthroot;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;

public class SandalsOfNature extends Artifact_old {

	{
		name = "Sandals of Nature";
		image = ItemSpriteSheet.ARTIFACT_SANDALS;

		level(0);
		levelCap = 3;

		charge = 0;

		defaultAction = AC_ROOT;
	}

	public static final String[] NAMES = {"Sandals of Nature", "Shoes of Nature",
										"Boots of Nature", "Greaves of Nature"};

	public static final String AC_FEED = "FEED";
	public static final String AC_ROOT = "ROOT";

	protected String inventoryTitle = "Select a seed";

	public ArrayList<String> seeds = new ArrayList<String>();

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && level() < 3 && bucStatus != BUCStatus.Cursed)
			actions.add(AC_FEED);
		if (isEquipped( hero ) && charge > 0)
			actions.add(AC_ROOT);
		return actions;
	}

	@Override
	public boolean execute( Hero hero, String action ) {
		boolean retval = super.execute(hero, action);

		if (action.equals(AC_FEED)){
			GameScene.selectItem(itemSelector, Plant.Seed.class, inventoryTitle);
		} else if (action.equals(AC_ROOT) && level > 0){
			if (!isEquipped( hero )) GLog.i("You need to equip them to do that.");
			else if (charge == 0)    GLog.i("They have no energy right now.");
			else {
				Buff.prolong(hero, Roots.class, 5);
				Buff.affect(hero, Earthroot.Armor.class).level(charge);
				CellEmitter.bottom(hero.pos).start(EarthParticle.FACTORY, 0.05f, 8);
				Camera.main.shake(1, 0.4f);
				charge = 0;
				updateQuickslot();
			}
		}

		return retval;
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Naturalism();
	}

	@Override
	public String desc() {
		String desc = "";
		if (level == 0)
			desc += "What initially seem like sandals made of twine are actually two plants! The footwear moves ever " +
				  "so slightly when being held. They seem very weak and pale, perhaps they need to be given nutrients?";
		else if (level == 1)
			desc += "The footwear has grown and now more closely resemble two tailored shoes. They seem to match the " +
				"contours of your feet exactly. Some colour has returned to them, perhaps they can still grow further?";
		else if (level == 2)
			desc += "The plants have grown again and now resembles a pair of solid tall boots. They appear to be made" +
					" of solid bark more than vine now, yet are still very flexible. The plants seem to have " +
					"regained their strength, but perhaps they can still grow further";
		else
			desc += "Now almost tall enough to make full pants, the bark-mesh artifact seems to have reached its " +
					"maximum size. Perhaps the two plants don't want to merge together? The greaves are a deep brown " +
					"and resemble a very sturdy tree.";

		if ( isEquipped ( Dungeon.hero ) ){
			desc += "\n\n";
			if (level == 0) {
				if (bucStatus != BUCStatus.Cursed)
					desc += "The sandals wrap snugly around your feet, they seem happy to be worn.";
				else
					desc += "The cursed sandals wrap tightly around your feet.";
			}
			else if (level == 1)
				desc += "The shoes fit on loosely but quickly tighten to make a perfect fit.";
			else if (level == 2)
				desc += "The boots fit snugly and add a nice heft to your step.";
			else
				desc += "The greaves are thick and weighty, but very easy to move in, as if they are moving with you.";

			if (bucStatus != BUCStatus.Cursed)
				desc += " You feel more attuned with nature while wearing them.";
			else
				desc += " They are blocking any attunement with nature.";

			if (level > 0)
				desc += "\n\nThe footwear has gained the ability to form up into a sort of immobile natural armour, " +
						"but will need to charge up for it.";
		}

		if (!seeds.isEmpty()){
			desc += "\n\nRecently Fed Seeds:";
			String[] seedsArray = seeds.toArray(new String[seeds.size()]);

			for (int i = 0; i < seedsArray.length-1; i++)
				desc += " " + seedsArray[i].substring(8) + ",";

			desc += " " + seedsArray[seedsArray.length-1].substring(8) + ".";
		}

		return desc;
	}

	@Override
	public Item upgrade(Item source, int n) {
		if (level < 0)
			image = ItemSpriteSheet.ARTIFACT_SANDALS;
		else if (level == 0)
			image = ItemSpriteSheet.ARTIFACT_SHOES;
		else if (level == 1)
			image = ItemSpriteSheet.ARTIFACT_BOOTS;
		else if (level >= 2)
			image = ItemSpriteSheet.ARTIFACT_GREAVES;
		name = NAMES[level+1];
		return super.upgrade(source, n);
	}


	private static final String SEEDS = "seeds";
	private static final String NAME = "name";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(NAME, name);
		bundle.put(SEEDS, seeds.toArray(new String[seeds.size()]));
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		name = bundle.getString( NAME );
		if (bundle.contains(SEEDS))
			Collections.addAll(seeds , bundle.getStringArray(SEEDS));
	}

	public class Naturalism extends ArtifactBuff{
		public void charge() {
			if (charge < target.HT){
				//gain 1+(1*level)% of the difference between current charge and max HP.
				charge+= (Math.round( (target.HT-charge) * (.01+ level*0.01) ));
				updateQuickslot();
			}
		}
	}

	protected WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof Plant.Seed) {
				if (seeds.contains(item.getDisplayName())){
					GLog.w("Your " + name + " have already gained nutrients from that seed recently.");
				} else {
					seeds.add(item.getDisplayName());

					Hero hero = Dungeon.hero;
					hero.sprite.operate(hero.pos);
					Sample.INSTANCE.play(Assets.SND_PLANT);
					hero.busy();
					hero.spend_new(GameTime.TICK * 2, false);
					if (seeds.size() >= 5+(level*2)){
						seeds.clear();
						upgrade(item, 1);
						if (level >= 1 && level <= 3) {
							GLog.p("Your " + NAMES[level-1] + " surge in size, they are now " + NAMES[level] + "!");
						}

					} else {
						GLog.i("Your " + name + " absorb the seed, they seem healthier.");
					}
					hero.belongings.remove(item, 1);
				}
			}
		}
	};

}
*/