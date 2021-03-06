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
import java.util.HashSet;

import com.felayga.unpixeldungeon.actors.buffs.hero.LockedFloor;
import com.felayga.unpixeldungeon.actors.hero.HeroSubClass;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.artifacts.LloydsBeacon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.levels.PrisonBossLevel;
import com.felayga.unpixeldungeon.levels.traps.SpearTrap;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.ui.BossHealthBar;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.ToxicGas;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.TomeOfMastery;
import com.felayga.unpixeldungeon.items.consumable.scrolls.spellscroll.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.items.consumable.scrolls.ScrollOfPsionicBlast;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Death;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.mobs.unused.TenguSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Tengu extends Mob {
	
	{
		name = "Tengu";
		spriteClass = TenguSprite.class;

		canOpenDoors = true;

		DEXCHA = 20;

		HP = HT = 120;
		EXP = 20;
		defenseSkill = 20;

		HUNTING = new Hunting();

		flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards

		belongings.weapon = new MeleeMobAttack(GameTime.TICK, 6, 15);
	}

	@Override
	public void damage(int dmg, Object src) {

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) {
			int multiple = HP > HT/2 ? 1 : 4;
			lock.addTime(dmg*multiple);
		}

		//phase 2 of the fight is over
		if (dmg >= HP) {
			((PrisonBossLevel)Dungeon.level).progress();
			return;
		}

		int beforeHitHP = HP;
		super.damage(dmg, src);

		int hpBracket = HP > HT/2 ? 12 : 20;

		//phase 1 of the fight is over
		if (beforeHitHP > HT/2 && HP <= HT/2){
			HP = (HT/2)-1;
			yell("Let's make this interesting...");
			((PrisonBossLevel)Dungeon.level).progress();
			BossHealthBar.bleed(true);

		//if tengu has lost a certain amount of hp, jump
		} else if (beforeHitHP / hpBracket != HP / hpBracket) {
			jump();
		}
	}

	@Override
	public void die( Object cause ) {
		
		if (Dungeon.hero.subClass == HeroSubClass.NONE) {
			Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();
		}
		
		GameScene.bossSlain();
		super.die( cause );
		
		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade(null, 1);
			GLog.p("Your beacon grows stronger!");
		}
		
		yell( "Free at last..." );
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	//tengu's attack is always visible
	@Override
	protected boolean doAttack(boolean thrown, Char enemy) {
		sprite.attack( enemy.pos );
		spend( attackDelay(((KindOfWeapon)belongings.weapon).delay_new), false );
		return true;
	}

	private void jump() {

		for (int i=0; i < 4; i++) {
			int trapPos;
			do {
				trapPos = Random.Int( Level.LENGTH );
			} while (!Level.fieldOfView[trapPos] || Level.solid[trapPos]);
			
			if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
				Dungeon.level.setTrap( new SpearTrap().reveal(), trapPos );
				Level.set( trapPos, Terrain.TRAP );
				ScrollOfMagicMapping.discover( trapPos );
			}
		}
		
		int newPos;
		//if we're in phase 1, want to warp around within the room
		if (HP > HT/2) {
			do {
				newPos = Random.Int(Level.LENGTH);
			} while (
					!Level.fieldOfView[newPos] ||
							Level.solid[newPos] ||
							Level.adjacent(newPos, enemy.pos) ||
							Actor.findChar(newPos) != null);

		//otherwise go wherever, as long as it's a little bit away
		} else {
			do {
				newPos = Random.Int(Level.LENGTH);
			} while (
					Level.solid[newPos] ||
					Level.distance(newPos, enemy.pos) < 8 ||
					Actor.findChar(newPos) != null);
		}
		
		if (Dungeon.visible[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );


		sprite.move( pos, newPos );
		move( newPos );
		
		if (Dungeon.visible[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
		Sample.INSTANCE.play( Assets.SND_PUFF );
		
		spend( GameTime.TICK * GameTime.TICK / speed(), false );
	}
	
	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		if (HP <= HT/2) BossHealthBar.bleed(true);
		if (HP == HT) {
			yell("You're mine, " + Dungeon.hero.givenName() + "!");
		} else {
			yell("Face me, " + Dungeon.hero.givenName() + "!");
		}
	}
	
	@Override
	public String description() {
		return
			"A famous and enigmatic assassin, named for the mask grafted to his face.\n\n" +
			"Tengu is held down with large clasps on his wrists and knees, though he seems to have gotten rid of his chains long ago.\n\n" +
			"He will try to use traps, deceptive magic, and precise attacks to eliminate the only thing stopping his escape: you.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
	static {
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Poison.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		if (HP <= HT/2) BossHealthBar.bleed(true);
	}

	//tengu is always hunting
	private class Hunting extends Mob.Hunting{

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				return doAttack( true, enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				}

				spend( GameTime.TICK, false );
				return true;

			}
		}
	}
}
*/