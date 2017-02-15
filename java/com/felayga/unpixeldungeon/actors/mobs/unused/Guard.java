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
package com.felayga.unpixeldungeon.actors.mobs.unused;
/*
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.effects.Chains;
import com.felayga.unpixeldungeon.effects.Pushing;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.potions.PotionOfHealing;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.mobs.GuardSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Guard extends Mob {

	//they can only use their chains once
	private boolean chainsUsed = false;

	{
		name = "prison guard";
		spriteClass = GuardSprite.class;

		canOpenDoors = true;

		DEXCHA = 14;

		HP = HT = 30;
		defenseSkill = 10;

		EXP = 6;
		maxLvl = 14;

		loot = null;    //see createloot.
		lootChance = 1;

		belongings.weapon = new MeleeMobAttack(GameTime.TICK, 4, 10);
	}

	@Override
	protected boolean act() {
		Dungeon.level.updateFieldOfView( this );

		if (state == HUNTING &&
				enemy != null &&
				Level.fieldOfView[enemy.pos] &&
				Level.distance( pos, enemy.pos ) < 5 && !Level.adjacent( pos, enemy.pos ) &&
				Random.Int(3) == 0 &&

				chain(enemy.pos)) {

			return false;

		} else {
			return super.act();
		}
	}

	private boolean chain(int target){
		if (chainsUsed)
			return false;

		Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

		if (chain.collisionPos != enemy.pos || Level.pit[chain.path.get(1)])
			return false;
		else {
			int newPos = -1;
			for (int i : chain.subPath(1, chain.dist)){
				if (!Level.solid[i] && Actor.findChar(i) == null){
					newPos = i;
					break;
				}
			}

			if (newPos < 0){
				return false;
			} else {
				final int newPosFinal = newPos;
				yell("get over here!");
				sprite.parent.add(new Chains(pos, enemy.pos, new Callback() {
					public void call() {
						Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal), -1);
						enemy.pos = newPosFinal;
						Dungeon.level.press(newPosFinal, enemy);
						Cripple.prolong(enemy, Cripple.class, GameTime.TICK * 4);
						if (enemy == Dungeon.hero) {
							Dungeon.hero.interrupt();
							Dungeon.observe();
						}
						next();
					}
				}));
			}
		}
		chainsUsed = true;
		return true;
	}

	@Override
	public String defenseVerb() {
		return "blocked";
	}

	@Override
	protected Item createLoot() {
		//first see if we drop armor, chance is 1/8 (0.125f)
		if (Random.Int(8) == 0){
			return Generator.randomArmor();
		//otherwise, we may drop a health potion. Chance is 1/(7+potions dropped)
		//including the chance for armor before it, effective droprate is ~1/(8 + (1.15*potions dropped))
		} else {
			//removed (+ Dungeon.limitedDrops.guardHP.count) from rand result below
			if (Random.Int(7) == 0){
				//Dungeon.limitedDrops.guardHP.drop();
				return new PotionOfHealing();
			}
		}

		return null;
	}

	@Override
	public String description() {
		return "Once keepers of the prison, these guards have long since become no different than the inmates. " +
				"They shamble like zombies, brainlessly roaming through the halls in search of anything out of place, like you!\n\n" +
				"They carry chains around their hip, possibly used to pull in enemies to close range.";
	}

	private final String CHAINSUSED = "chainsused";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHAINSUSED, chainsUsed);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		chainsUsed = bundle.getBoolean(CHAINSUSED);
	}
}
*/