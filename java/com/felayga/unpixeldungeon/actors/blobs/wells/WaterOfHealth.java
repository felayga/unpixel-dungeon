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
package com.felayga.unpixeldungeon.actors.blobs.wells;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Journal;
import com.felayga.unpixeldungeon.Journal.Feature;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.BlobEmitter;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;
import com.felayga.unpixeldungeon.items.DewVial;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.consumable.potions.PotionOfFullHealing;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class WaterOfHealth extends WellWater {

	private static final String TXT_PROCCED =
		"As you take a sip, you feel your wounds heal completely.";
	
	@Override
	protected boolean affectHero( Hero hero ) {
		
		Sample.INSTANCE.play(Assets.SND_DRINK);

		PotionOfFullHealing instance = new PotionOfFullHealing();
		instance.heal(hero);
		instance.cure(hero);
		hero.belongings.bucUncurse(false, false, true, false);
		hero.buff( Hunger.class ).satisfy_new( 500 );
		
		CellEmitter.get( pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

		Dungeon.hero.interrupt();
	
		GLog.p( TXT_PROCCED );
		
		Journal.remove( Feature.WELL_OF_HEALTH );
		
		return true;
	}
	
	@Override
	protected Item affectItem( Item item ) {
		if (item instanceof DewVial && !((DewVial)item).isFull()) {
			((DewVial)item).fill();
			return item;
		}
		
		return null;
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.HEALING ), 0.5f, 0 );
	}
	
	@Override
	public String tileDesc() {
		return
			"Power of health radiates from the water of this well. " +
			"Take a sip from it to heal your wounds and satisfy hunger.";
	}
}
