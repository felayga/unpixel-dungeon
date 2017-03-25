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
package com.felayga.unpixeldungeon.actors.buffs.negative;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.consumable.scrolls.Scroll;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Burning extends Buff implements Hero.Doom {
	private static final String TXT_BURNS_UP		= "%s burns up!";
	private static final String TXT_BURNED_TO_DEATH	= "You burned to death...";
	
	private long left;

    public Burning()
	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( TIMELEFT, left );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		left = bundle.getLong( TIMELEFT );
	}

	@Override
	public boolean act() {
        if (target.isAlive()) {

            target.damage(Random.IntRange(1, 4), MagicType.Fire, Char.Registry.get(ownerRegistryIndex()), null);
            Buff.detach(target, Chill.class);

            if (target instanceof Char) {
                Item item = target.belongings.randomUnequipped();
                if (item instanceof Scroll) {

                    item = target.belongings.remove(item, 1);
                    GLog.w(TXT_BURNS_UP, item.getDisplayName());

                    Heap.burnFX(target.pos(), Dungeon.visible[target.pos()], Dungeon.audible[target.pos()]);

                } /*else if (item instanceof MysteryMeat) {
					
					item = hero.belongings.remove(item, 1);
					Item steak = new ChargrilledMeat().bucStatus(item);
					if (!hero.belongings.collect(steak)) {
						Dungeon.level.drop( steak, hero.pos ).sprite.drop();
					}
					GLog.w( TXT_BURNS_UP, item.getDisplayName() );
					
					Heap.burnFX( hero.pos );
					
				}*/

            }

        } else {
            detach();
        }

        if (Level.burnable[target.pos()]) {
            GameScene.add(Blob.seed(Char.Registry.get(ownerRegistryIndex()), target.pos(), 4, Fire.class));
        }

        spend_new(GameTime.TICK, false);
        left -= GameTime.TICK;

        if (left <= 0 ||
                /*Random.Float() > (2 + (float) target.HP / target.HT) / 3 ||*/
                (Level.puddle[target.pos()] && !target.flying())) {

            detach();
        }

        return true;
    }
	
	public void reignite( Char ch ) {
		left = 4 * GameTime.TICK;
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

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You catch fire!";
        }

        return super.attachedMessage(isHero);
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
