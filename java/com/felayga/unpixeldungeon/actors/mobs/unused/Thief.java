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
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Terror;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.artifacts.MasterThievesArmband;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.StealChance;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.mobs.unused.ThiefSprite;
import com.watabou.utils.Random;

public class Thief extends Mob {

	protected static final String TXT_CARRIES	= "\n\n%s is carrying a _%s_. Stolen obviously.";

	public Thief()
	{
		name = "crazy thief";
		spriteClass = ThiefSprite.class;

		canOpenDoors = true;

		DEXCHA = 12;

		HP = HT = 20;
		defenseSkill = 12;
		
		EXP = 5;
		maxLvl = 10;
		
		loot = new MasterThievesArmband().identify();
		lootChance = 0.01f;

		FLEEING = new Fleeing();

		belongings.weapon = new StealChance(GameTime.TICK / 2, 1, 7);

		belongings.backpack1.size = 0;
		belongings.backpack2.size = 4;
		belongings.backpack3.size = 0;
	}

	@Override
	protected Item createLoot(){
		if (!Dungeon.limitedDrops.armband.dropped()) {
			Dungeon.limitedDrops.armband.drop();
			return super.createLoot();
		} else {
			return new Gold(Random.NormalIntRange(100, 250));
		}
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		if (state == FLEEING) {
			Dungeon.level.drop( new Gold(), pos ).sprite.drop();
		}

		return super.defenseProc(enemy, damage);
	}

	@Override
	public String description() {
		String desc =
				"Though these inmates roam free of their cells, this place is still their prison. " +
				"Over time, this place has taken their minds as well as their freedom. " +
				"Long ago, these crazy thieves and bandits have forgotten who they are and why they steal.\n\n" +
				"These enemies are more likely to steal and run than they are to fight.";
				//"Make sure to keep them in sight, of you might never see your stolen item again.";

		///if (belongings.backpack.co != null) {
		///	desc += String.format( TXT_CARRIES, Utils.capitalize( this.name ), weapon.stolenItem.name() );
		///}

		return desc;
	}

	public void flee()
	{
		this.state = FLEEING;
	}

	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff( Terror.class ) == null) {
				if (enemySeen) {
					sprite.showStatus(CharSprite.NEGATIVE, TXT_RAGE);
					state = HUNTING;
				} else {
					//if (weapon.stolenItem != null) GLog.n("The thief gets away with your " + weapon.stolenItem.name() + "!");
					//weapon.stolenItem = null;
					state = WANDERING;
				}
			} else {
				super.nowhereToRun();
			}
		}
	}
}
*/