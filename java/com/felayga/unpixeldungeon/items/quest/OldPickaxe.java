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
package com.felayga.unpixeldungeon.items.quest;

import java.util.ArrayList;

import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.watabou.noosa.audio.Sample;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.actors.hero.Hero;
//import com.felayga.unpixeldungeon.actors.mobs.Bat;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.sprites.ItemSprite.Glowing;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class OldPickaxe extends Weapon {
	
	public static final String AC_MINE	= "MINE";
	
	public static final long TIME_TO_MINE = GameTime.TICK * 2;
	
	private static final String TXT_NO_VEIN = "There is no dark gold vein near you to mine";
	
	private static final Glowing BLOODY = new Glowing( 0x550000 );

	public OldPickaxe()
	{
		super(WeaponSkill.None, GameTime.TICK, 3, 12);

		name = "pickaxe";
		image = ItemSpriteSheet.PICKAXE;
		
		unique = true;
		
		defaultAction = AC_MINE;
		
		//STR = 14;
	}
	
	public boolean bloodStained = false;
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_MINE );
		return actions;
	}
	
	@Override
	public boolean execute( final Hero hero, String action ) {
		if (action == AC_MINE) {
			if (Dungeon.depth < 11 || Dungeon.depth > 15) {
				GLog.w( TXT_NO_VEIN );
				return false;
			}
			
			for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
				final int pos = hero.pos + Level.NEIGHBOURS8[i];
				if (Dungeon.level.map[pos] == Terrain.WALL_DECO) {
				
					hero.spend( TIME_TO_MINE, false );
					hero.busy();
					
					hero.sprite.attack( pos, new Callback() {
						
						@Override
						public void call() {
							CellEmitter.center( pos ).burst( Speck.factory( Speck.STAR ), 7 );
							Sample.INSTANCE.play( Assets.SND_EVOKE );
							
							Level.set( pos, Terrain.WALL );
							GameScene.updateMap( pos );
							
							DarkGold gold = new DarkGold();
							if (gold.doPickUp( Dungeon.hero )) {
								GLog.i( Hero.TXT_YOU_NOW_HAVE, gold.getDisplayName() );
							} else {
								Dungeon.level.drop( gold, hero.pos ).sprite.drop();
							}
							
							Hunger hunger = hero.buff( Hunger.class );
							if (hunger != null && !hunger.isStarving()) {
								hunger.satisfy_new( 2 );
								BuffIndicator.refreshHero();
							}
							
							hero.onOperateComplete();
						}
					} );
					
					return false;
				}
			}
			
			GLog.w( TXT_NO_VEIN );

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
	public int proc( Char attacker, boolean thrown, Char defender, int damage ) {
		damage = super.proc(attacker, thrown, defender, damage);

		/*
		if (!bloodStained && defender instanceof Bat && (defender.HP <= damage)) {
			bloodStained = true;
			updateQuickslot();
		}
		*/

		return damage;
	}
	
	private static final String BLOODSTAINED = "bloodStained";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		
		bundle.put( BLOODSTAINED, bloodStained );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		bloodStained = bundle.getBoolean( BLOODSTAINED );
	}
	
	@Override
	public Glowing glowing() {
		return bloodStained ? BLOODY : null;
	}
	
	@Override
	public String info() {
		return
			"This is a large and sturdy tool for breaking rocks. Probably it can be used as a weapon.";
	}
}
