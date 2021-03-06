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
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.LockedFloor;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Chains;
import com.felayga.unpixeldungeon.effects.Pushing;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class EtherealChains extends Artifact_old {

	public static final String AC_CAST       = "CAST";

	{
		name = "ethereal chains";
		image = ItemSpriteSheet.ARTIFACT_CHAINS;

		level(0);
		levelCap = 5;
		exp = 0;

		charge = 5;

		defaultAction = AC_CAST;
		usesTargeting = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped(hero) && charge > 0 && bucStatus() != BUCStatus.Cursed)
			actions.add(AC_CAST);
		return actions;
	}

	@Override
	public boolean execute(Hero hero, String action) {
		if (action.equals(AC_CAST)){
			curUser = hero;

			if      (!isEquipped( hero ))       GLog.i("You need to equip the chains to do that.");
			else if (charge < 1)                GLog.i("Your chains do not have any available charge.");
			else if (bucStatus() == BUCStatus.Cursed)                    GLog.w("You can't use cursed chains.");
			else {
				GameScene.selectCell(caster, "Choose a location to target");
			}

			return true;
		} else {
			return super.execute(hero, action);
		}
	}

	private CellSelector.Listener caster = new CellSelector.Listener(){

		@Override
		public boolean onSelect(Integer target) {
			if (target != null && (Dungeon.level.visited[target] || Dungeon.level.mapped[target])){

				//ballistica does not go through walls on pre-rework boss arenas
                Ballistica.Mode missileProperties = (Level.flags & Level.FLAG_NO_TELEPORTATION) != 0 ?
						Ballistica.Mode.Projectile : Ballistica.Mode.SeekerBolt;

				final Ballistica chain = new Ballistica(curUser.pos(), target, missileProperties.value);

				//determine if we're grabbing an enemy, pulling to a location, or doing nothing.
				if (Actor.findChar( chain.collisionPos ) != null){
					int newPos = -1;
					for (int i : chain.subPath(1, chain.dist)){
						if (!Level.solid[i] && Actor.findChar(i) == null){
							newPos = i;
							break;
						}
					}
					if (newPos < 0){
						GLog.w("That won't do anything");
					} else {
						final int newMobPos = newPos;
						final Char affected = Actor.findChar( chain.collisionPos );
						int chargeUse = Level.distance(affected.pos(), newMobPos);
						if (chargeUse > charge){
							GLog.w("Your chains do not have enough charge.");
							return true;
						} else {
							charge -= chargeUse;
							updateQuickslot();
						}
						curUser.busy();
						curUser.sprite.parent.add(new Chains(curUser.pos(), affected.pos(), new Callback() {
							public void call() {
								Actor.add(new Pushing(affected, affected.pos(), newMobPos));
								affected.pos(newMobPos);
								Dungeon.observe();
								curUser.spend_new(GameTime.TICK, true);
								Dungeon.level.press(newMobPos, affected);
							}
						}));
					}

				} else if (Level.solid[chain.path.get(chain.dist)]
						|| (chain.dist > 0 && Level.solid[chain.path.get(chain.dist-1)])
						|| (chain.path.size() > chain.dist+1 && Level.solid[chain.path.get(chain.dist+1)])
						//if the player is trying to grapple the edge of the map, let them.
						|| (chain.path.size() == chain.dist+1)) {
					int newPos = -1;
					for (int i : chain.subPath(1, chain.dist)){
						if (!Level.solid[i] && Actor.findChar(i) == null) newPos = i;
						}
					if (newPos < 0) {
						GLog.w("That won't do anything");
					} else {
						final int newHeroPos = newPos;
						int chargeUse = Level.distance(curUser.pos(), newHeroPos);
						if (chargeUse > charge){
							GLog.w("Your chains do not have enough charge.");
							return true;
						} else {
							charge -= chargeUse;
							updateQuickslot();
						}
						curUser.busy();
						curUser.sprite.parent.add(new Chains(curUser.pos(), target, new Callback() {
							public void call() {
								Actor.add(new Pushing(curUser, curUser.pos(), newHeroPos, new Callback() {
									public void call() {
										Dungeon.level.press(newHeroPos, curUser);
									}
								}));
								curUser.spend_new(GameTime.TICK, true);
								curUser.pos(newHeroPos);
								Dungeon.observe();
							}
						}));
					}

				} else {
					GLog.i("There is nothing to grab there");
				}

			}

            return true;
		}
	};

	@Override
	protected ArtifactBuff passiveBuff() {
		return new chainsRecharge();
	}

	@Override
	public String desc() {
		String desc = "These large clanky chains glow with spiritual energy. They move with a certain heft, " +
				"but are surprisingly almost weightless. These chains can be used to grab surfaces, pulling you " +
				"towards terrain or pulling enemies toward you. The ethereal nature of the chains even allows them to " +
				"extend and pull targets through walls!";

		if (isEquipped( Dungeon.hero )){
			if (bucStatus() != BUCStatus.Cursed) {
				desc += "\n\nThe chains rest around your side, slowly siphoning the spiritual energy of those you defeat. " +
						"Each charge is a link in the chain, which will extend out exactly one tile.";

			}else
				desc += "\n\nThe cursed chains are locked to your side, constantly swinging around, trying to trip or bind you";
		}
		return desc;
	}

	public class chainsRecharge extends ArtifactBuff{

		@Override
		public boolean act() {
			int chargeTarget = 5+(level()*2);
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeTarget && bucStatus() != BUCStatus.Cursed && (lock == null || lock.regenOn())) {
				partialCharge += 1 / (40f - (chargeTarget - charge)*2f);
			} else if (bucStatus() == BUCStatus.Cursed && Random.Int(100) == 0){
				Buff.prolong( target, parent().owner(), Cripple.class, GameTime.TICK * 10);
			}

			if (partialCharge >= 1) {
				partialCharge --;
				charge ++;
			}

			updateQuickslot();

            spend_new(GameTime.TICK, false);

			return true;
		}

		public void gainExp( float levelPortion ) {
			if (bucStatus() == BUCStatus.Cursed) return;

			exp += Math.round(levelPortion*100);

			//past the soft charge cap, gaining  charge from leveling is slowed.
			if (charge > 5+(level()*2)){
				levelPortion *= (5+((float)level()*2))/charge;
			}
			partialCharge += levelPortion*10f;

			if (exp > 100+level()*50 && level() < levelCap){
				exp -= 100+level()*50;
				GLog.p("Your chains grow stronger!");
				upgrade(null, 1);
			}

		}
	}
}
