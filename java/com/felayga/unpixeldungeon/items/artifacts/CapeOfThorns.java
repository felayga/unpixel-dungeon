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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class CapeOfThorns extends Artifact_old {

	{
		name = "Cape of Thorns";
		image = ItemSpriteSheet.ARTIFACT_CAPE;

		level(0);
		levelCap = 10;

		charge = 0;
		chargeCap = 100;
		cooldown = 0;

		defaultAction = "NONE";
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Thorns();
	}

	@Override
	public String desc() {
		String desc = "These collapsed sheets of metal from the DM-300 have formed together into a rigid wearable " +
				"cape. The metal is old and coated in thick flakes of rust. It seems to store a deep energy, " +
				"perhaps it has some of the DM-300's power?";
		if (isEquipped( Dungeon.hero )) {
			desc += "\n\n";
			if (cooldown == 0)
				desc += "The cape feels reassuringly heavy on your shoulders. You're not sure if it will directly " +
						"help you in a fight, but it seems to be gaining energy from the damage you take.";
			else
				desc += "The cape seems to be releasing some stored energy, " +
						"it is radiating a protective power at all angles. ";
		}

		return desc;
	}

	public class Thorns extends ArtifactBuff{

		@Override
		public boolean act(){
			if (cooldown > 0) {
				cooldown--;
				if (cooldown == 0) {
					BuffIndicator.refreshHero();
					GLog.w("Your Cape becomes inert again.");
				}
				updateQuickslot();
			}
            spend_new(GameTime.TICK, false);
			return true;
		}

		public int proc(int damage, Actor attacker, Char defender){
			if (cooldown == 0){
				charge += damage*(0.5+level()*0.05);
				if (charge >= chargeCap){
					charge = 0;
					cooldown = 10+level();
					GLog.p("Your Cape begins radiating energy, you feel protected!");
					BuffIndicator.refreshHero();
				}
			}

			if (cooldown != 0){
				int deflected = Random.NormalIntRange(0, damage);
				damage -= deflected;

				if (attacker instanceof Char) {
					Char c = (Char)attacker;
					if (Level.canReach(c.pos(), defender.pos())) {
						c.damage(deflected, MagicType.Mundane, null, CapeOfThorns.this);
					}
				}

				exp+= deflected;

				if (exp >= (level()+1)*5 && level() < levelCap){
					exp -= (level()+1)*5;
					upgrade(null, 1);
					GLog.p("Your Cape grows stronger!");
				}

			}
			updateQuickslot();
			return damage;
		}

		@Override
		public String toString() {
				return "Thorns";
		}

		@Override
		public String desc() {
			return "Your cape is radiating energy, surrounding you in a field of deflective force!\n" +
					"\n" +
					"All damage you receive is reduced while the thorns effect is active. Additionally, " +
					"if the attacker is next to you, the reduced amount is deflected back at the attacker.\n" +
					"\n" +
					"Your cape will continue radiating energy for " + dispTurns(cooldown) + ".";
		}

		@Override
		public int icon() {
			if (cooldown == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.THORNS;
		}

		@Override
		public void detach(){
			cooldown = 0;
			charge = 0;
			super.detach();
		}

	}


}
