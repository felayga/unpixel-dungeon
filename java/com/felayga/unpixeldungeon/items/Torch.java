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
package com.felayga.unpixeldungeon.items;

import java.util.ArrayList;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.particles.Emitter;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Light;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

public class Torch extends Item {

	public static final String AC_LIGHT	= "LIGHT";
	
	public static final long TIME_TO_LIGHT = GameTime.TICK;
	
	{
		name = "torch";
		image = ItemSpriteSheet.TORCH;
		
		stackable = true;
		
		defaultAction = AC_LIGHT;
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
			
			hero.spend(TIME_TO_LIGHT, false);
			hero.busy();
			
			hero.sprite.operate(hero.pos);

			hero.belongings.detach(this);
			Buff.affect(hero, Light.class, Light.DURATION);
			
			Emitter emitter = hero.sprite.centerEmitter(-1);
			emitter.start( FlameParticle.FACTORY, 0.2f, 3 );

			return false;
		} else {
			return super.execute( hero, action );
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}
	
	@Override
	public String info() {
		return
			"An adventuring staple, when a dungeon goes dark, a torch can help lead the way.";
	}
}
