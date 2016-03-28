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
package com.felayga.unpixeldungeon.actors.buffs;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Thief;
import com.felayga.unpixeldungeon.effects.particles.ElmoParticle;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.food.ChargrilledMeat;
import com.felayga.unpixeldungeon.items.food.MysteryMeat;
import com.felayga.unpixeldungeon.items.rings.RingOfElements.Resistance;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Burning extends Buff implements Hero.Doom {

	private static final String TXT_BURNS_UP		= "%s burns up!";
	private static final String TXT_BURNED_TO_DEATH	= "You burned to death...";
	
	private static final float DURATION = 8f;
	
	private float left;
	
	private static final String LEFT	= "left";

	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat( LEFT );
	}

	@Override
	public boolean act() {
		
		if (target.isAlive()) {
			
			target.damage( Random.Int( 1, 5 ), this );
			Buff.detach( target, Chill.class);

			if (target instanceof Char) {

				Char hero = (Char)target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof Scroll) {
					
					item = hero.belongings.detach(item);
					GLog.w( TXT_BURNS_UP, item.getDisplayName() );
					
					Heap.burnFX( hero.pos );
					
				} else if (item instanceof MysteryMeat) {
					
					item = hero.belongings.detach(item);
					Item steak = new ChargrilledMeat().bucStatus(item);
					if (!hero.belongings.collect(steak)) {
						Dungeon.level.drop( steak, hero.pos ).sprite.drop();
					}
					GLog.w( TXT_BURNS_UP, item.getDisplayName() );
					
					Heap.burnFX( hero.pos );
					
				}
				
			}

		} else {
			detach();
		}
		
		if (Level.wood[target.pos]) {
			GameScene.add( Blob.seed( target.pos, 4, Fire.class ) );
		}
		
		spend(GameTime.TICK, false );
		left -= GameTime.TICK;
		
		if (left <= 0 ||
			Random.Float() > (2 + (float)target.HP / target.HT) / 3 ||
			(Level.water[target.pos] && !target.flying)) {
			
			detach();
		}
		
		return true;
	}
	
	public void reignite( Char ch ) {
		left = duration( ch );
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.BURNING);
		else target.sprite.remove(CharSprite.State.BURNING);
	}

	@Override
	public String toString() {
		return "Burning";
	}

	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

	@Override
	public String desc() {
		return "Few things are more distressing than being engulfed in flames.\n" +
				"\n" +
				"Fire will deal damage every turn until it is put out by water, expires, or it is resisted. " +
				"Fire can be extinquished by stepping into water, or from the splash of a shattering potion. \n" +
				"\n" +
				"Additionally, the fire may ignite flammable terrain or items that it comes into contact with.\n" +
				"\n" +
				"The burning will last for " + dispTurns(left) + ", or until it is resisted or extinquished.";
	}

	@Override
	public void onDeath() {
		
		Badges.validateDeathFromFire();
		
		Dungeon.fail( ResultDescriptions.BURNING );
		GLog.n( TXT_BURNED_TO_DEATH );
	}
}
