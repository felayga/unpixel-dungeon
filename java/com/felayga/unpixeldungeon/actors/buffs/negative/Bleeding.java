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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.effects.Splash;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Bleeding extends Buff {

    public Bleeding()
	{
		type = buffType.NEGATIVE;
	}
	
	protected int level;
	
	private static final String LEVEL	= "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
		
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		level = bundle.getInt( LEVEL );
	}
	
	public void set( int level ) {
		this.level = level;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.BLEEDING;
	}
	
	@Override
	public String toString() {
		return "Bleeding";
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			
			if ((level = Random.IntRange( level / 2, level )) > 0) {
				
				target.damage(level, MagicType.Mundane, Char.Registry.get(ownerRegistryIndex()), null);
				if (target.sprite.visible) {
					Splash.at( target.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
							target.sprite.blood(), Math.min( 10 * level / target.HT, 10 ) );
				}
				
				if (target == Dungeon.hero && !target.isAlive()) {
					Dungeon.fail( ResultDescriptions.BLEEDING );
					GLog.n( "You bled to death..." );
				}

                spend_new(GameTime.TICK, false );
			} else {
				detach();
			}
			
		} else {
			
			detach();
			
		}
		
		return true;
	}

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "You are bleeding!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
	public String desc() {
		return "That wound is leaking a worrisome amount of blood.\n" +
				"\n" +
				"Bleeding causes damage every turn. Each turn the damage decreases by a random amount, " +
				"until the bleeding eventually stops.\n" +
				"\n" +
				"The bleeding can currently deal " + level + " max damage.";
	}
}
