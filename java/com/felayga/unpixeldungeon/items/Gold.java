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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class Gold extends Item {

	private static final String TXT_COLLECT	= "Collect gold coins to spend them later in a shop.";
	private static final String TXT_INFO	= "A pile of %d gold coins. " + TXT_COLLECT;
	private static final String TXT_INFO_1	= "One gold coin. " + TXT_COLLECT;
	private static final String TXT_VALUE	= "%+d";

	public Gold(int quantity) {
		name = "gold";
		image = ItemSpriteSheet.GOLD;
        pickupSound = Assets.SND_GOLD;
        material = Material.Gold;

		stackable = true;
		hasBuc(false);
        hasLevels(false);

		quantity(quantity);
		weight(1);
	}
	
	public Gold() {
		this( 1 );
	}
	
	/*
	@Override
	public boolean doPickUp( Hero hero ) {
		
		Dungeon.gold += quantity;
		Statistics.goldCollected += quantity;
		Badges.validateGoldCollected();

		MasterThievesArmband.Thievery thievery = hero.buff(MasterThievesArmband.Thievery.class);
		if (thievery != null)
			thievery.collect(quantity);

		GameScene.pickUp( this );
		hero.sprite.showStatus( CharSprite.NEUTRAL, TXT_VALUE, quantity );
		hero.spend( TIME_TO_PICK_UP, true );
		
		Sample.INSTANCE.play( Assets.SND_GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
		
		return true;
	}
	*/
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		switch (quantity()) {
		case 0:
			return TXT_COLLECT;
		case 1:
			return TXT_INFO_1;
		default:
			return Utils.format( TXT_INFO, quantity() );
		}
	}

	@Override
	public String getDisplayName()
	{
		return quantity() + " " + super.getDisplayName();
	}
	
	@Override
	public Item random() {
        GLog.d("depth="+Dungeon.depth()+" adjusted="+Dungeon.depthAdjusted);
		quantity(Random.IntRange(
                30 + (Dungeon.depthAdjusted * 3 / 8 + Dungeon.hero.level / 8) * 10,
                60 + (Dungeon.depthAdjusted * 3 / 8 + Dungeon.hero.level / 8) * 20
        ));
		return this;
	}
	
	private static final String VALUE	= "value";

}
