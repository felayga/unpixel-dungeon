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
package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.buffs.Invisibility;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.effects.particles.EnergyParticle;
import com.felayga.unpixeldungeon.utils.GLog;

public class ScrollOfRecharging extends Scroll {

	public static final long BUFF_DURATION = GameTime.TICK * 30;

	{
		name = "Scroll of Recharging";
		initials = "Re";
	}
	
	@Override
	protected void doRead() {

		Buff.affect(curUser, Recharging.class, BUFF_DURATION);
		charge(curUser);
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();

		GLog.i( "a surge of energy courses through your body, invigorating your wands.");
		SpellSprite.show( curUser, SpellSprite.CHARGE );
		setKnown();
		
		curUser.spend( TIME_TO_READ, true );
	}
	
	@Override
	public String desc() {
		return
			"The raw magical power bound up in this parchment will, when released, " +
			"charge up all the users wands over time.";
	}
	
	public static void charge( Hero hero ) {
		hero.sprite.centerEmitter(-1).burst( EnergyParticle.FACTORY, 15 );
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}


	public static class Recharging extends FlavourBuff {

		@Override
		public int icon() {
			return BuffIndicator.RECHARGING;
		}

		@Override
		public String toString() {
			return "Recharging";
		}

		//want to process partial turns for this buff, and not count it when it's expiring.
		//firstly, if this buff has half a turn left, should give out half the benefit.
		//secondly, recall that buffs execute in random order, so this can cause a problem where we can't simply check
		//if this buff is still attached, must instead directly check its remaining time, and act accordingly.
		//otherwise this causes inconsistent behaviour where this may detach before, or after, a wand charger acts.
		public double remainder() {
			return Math.min(1f, this.cooldown());
		}

		@Override
		public String desc() {
			return "Energy is coursing through you, improving the rate that your wands and staffs charge.\n" +
					"\n" +
					"Each turn this buff will increase current charge by one quarter, in addition to regular recharge. \n" +
					"\n" +
					"The recharging will last for " + dispTurns() + ".";
		}
	}
}
