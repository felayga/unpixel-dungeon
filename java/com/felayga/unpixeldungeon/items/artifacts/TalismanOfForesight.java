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
package com.felayga.unpixeldungeon.items.artifacts;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.positive.Awareness;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.LockedFloor;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TalismanOfForesight extends Artifact_old {

	{
		name = "Talisman of Foresight";
		image = ItemSpriteSheet.ARTIFACT_TALISMAN;

		level = 0;
		exp = 0;
		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 100;

		defaultAction = AC_SCRY;
	}

	public static final String AC_SCRY = "SCRY";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge == chargeCap && bucStatus != BUCStatus.Cursed)
			actions.add(AC_SCRY);
		return actions;
	}

	@Override
	public boolean execute( Hero hero, String action ) {
		boolean retval = super.execute(hero, action);

		if (action.equals(AC_SCRY)){
			if (!isEquipped(hero))        GLog.i("You need to equip your talisman to do that.");
			else if (charge != chargeCap) GLog.i("Your talisman isn't full charged yet.");
			else {
				hero.sprite.operate(hero.pos);
				hero.busy();
				Sample.INSTANCE.play(Assets.SND_BEACON);
				charge = 0;
				for (int i = 0; i < Level.LENGTH; i++) {

					int terr = Dungeon.level.map[i];
					if ((Terrain.flags[terr] & Terrain.FLAG_SECRET) != 0) {

						GameScene.updateMap(i);

						if (Dungeon.visible[i]) {
							GameScene.discoverTile(i, terr);
						}
					}
				}

				GLog.p("The Talisman floods your mind with knowledge about the current floor.");

				Buff.affect(hero, Awareness.class, Awareness.DURATION);
				Dungeon.observe();
			}
		}

		return retval;
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Foresight();
	}

	@Override
	public String desc() {
		String desc = "A smooth stone, almost too big for your pocket or hand, with strange engravings on it. " +
				"You feel like it's watching you, assessing your every move.";
		if ( isEquipped( Dungeon.hero ) ){
			if (bucStatus != BUCStatus.Cursed) {
				desc += "\n\nWhen you hold the talisman you feel like your senses are heightened.";
				if (charge == chargeCap)
					desc += "\n\nThe talisman is radiating energy, prodding at your mind. You wonder what would " +
							"happen if you let it in.";
			} else {
				desc += "\n\nThe cursed talisman is intently staring into you, making it impossible to concentrate.";
			}
		}

		return desc;
	}

	public class Foresight extends ArtifactBuff{
		private int warn = 0;

		@Override
		public boolean act() {
			spend(GameTime.TICK, false );

			boolean smthFound = false;

			int distance = 3;

			int cx = target.pos % Level.WIDTH;
			int cy = target.pos / Level.WIDTH;
			int ax = cx - distance;
			if (ax < 0) {
				ax = 0;
			}
			int bx = cx + distance;
			if (bx >= Level.WIDTH) {
				bx = Level.WIDTH - 1;
			}
			int ay = cy - distance;
			if (ay < 0) {
				ay = 0;
			}
			int by = cy + distance;
			if (by >= Level.HEIGHT) {
				by = Level.HEIGHT - 1;
			}

			for (int y = ay; y <= by; y++) {
				for (int x = ax, p = ax + y * Level.WIDTH; x <= bx; x++, p++) {

					if (Dungeon.visible[p] && Level.secret[p] && Dungeon.level.map[p] != Terrain.SECRET_DOOR)
							smthFound = true;
				}
			}

			if (smthFound && bucStatus != BUCStatus.Cursed){
				if (warn == 0){
					GLog.w("You feel uneasy.");
					if (target instanceof Hero){
						((Hero)target).interrupt();
					}
				}
				warn = 3;
			} else {
				if (warn > 0){
					warn --;
				}
			}
			BuffIndicator.refreshHero();

			//fully charges in 2500 turns at lvl=0, scaling to 1000 turns at lvl = 10.
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && bucStatus != BUCStatus.Cursed && (lock == null || lock.regenOn())) {
				partialCharge += 0.04+(level*0.006);

				if (partialCharge > 1 && charge < chargeCap) {
					partialCharge--;
					charge++;
				} else if (charge >= chargeCap) {
					partialCharge = 0;
					GLog.p("Your Talisman is fully charged!");
				}
			}

			return true;
		}

		public void charge(){
			charge = Math.min(charge+(2+(level/3)), chargeCap);
			exp++;
			if (exp >= 4 && level < levelCap) {
				upgrade(null, 1);
				GLog.p("Your Talisman grows stronger!");
				exp -= 4;
			}
		}

		@Override
		public String toString() {
			return "Foresight";
		}

		@Override
		public String desc() {
			return "You feel very nervous, as if there is nearby unseen danger.";
		}

		@Override
		public int icon() {
			if (warn == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.FORESIGHT;
		}
	}
}
