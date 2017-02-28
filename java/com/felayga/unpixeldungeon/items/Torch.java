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

import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.Light;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Torch extends Item implements IDecayable {
    protected long decay = -20 * GameTime.TICK;
    protected long decayTime;

    public long decay() {
        return decay;
    }

    public boolean decay(long currentTime, boolean updateTime, boolean fixTime) {
        if (fixTime || updateTime) {
            long newAmount = currentTime - decayTime;
            if (fixTime) {
                decayTime = 0;
            } else {
                decayTime = currentTime;
            }
            currentTime = newAmount;
        } else {
            decayTime = currentTime;
            currentTime = 0;
        }

        decay += currentTime;

        return false;
    }

	public static final String AC_LIGHT	= "LIGHT";
	
	public static final long TIME_TO_LIGHT = GameTime.TICK;

    public Torch()
	{
		name = "torch";
		image = ItemSpriteSheet.TORCH;
		
		stackable = true;

        hasLevels(false);
		
		defaultAction = AC_LIGHT;
        price = 10;
	}

    private static final String DECAY = "decay";
    private static final String DECAYTIME = "decayTime";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(DECAY, decay);
        bundle.put(DECAYTIME, decayTime);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        decay = bundle.getLong(DECAY);
        decayTime = bundle.getLong(DECAYTIME);
    }
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_LIGHT );
		return actions;
	}
	
	@Override
	public boolean execute( Hero hero, String action ) {
		if (action.equals( AC_LIGHT )) {
			
			hero.spend_new(TIME_TO_LIGHT, false);
			hero.busy();
			
			hero.sprite.operate(hero.pos());

			Light buff = Buff.append(hero, hero, Light.class);
            buff.ignite(decay);

			Emitter emitter = hero.sprite.centerEmitter(-1);
			emitter.start( FlameParticle.FACTORY, 0.2f, 3 );

			return false;
		} else {
			return super.execute( hero, action );
		}
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		return
			"An adventuring staple, when a dungeon goes dark, a torch can help lead the way.";
	}
}
