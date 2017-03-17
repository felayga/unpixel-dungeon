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
package com.felayga.unpixeldungeon.actors.mobs.npcs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.SuicideAttack;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.MirrorSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class MirrorImage extends NPC {

	public MirrorImage(int level)
	{
		super(level);

		name = "mirror image";
		spriteClass = MirrorSprite.class;
		
		state = HUNTING;

		belongings.collectEquip(new SuicideAttack(GameTime.TICK, 1, 4));

        HP = HT = 1;
        MP = MT = 1;
    }
	
	private static final String TIER			= "tier";

	
	public void duplicate( Hero hero ) {
        increaseAttribute(AttributeType.STRCON, hero.STRCON() - STRCON());
        increaseAttribute(AttributeType.DEXCHA, hero.DEXCHA() - DEXCHA());
        increaseAttribute(AttributeType.INTWIS, hero.INTWIS() - INTWIS());

        HP = HT = 1;
        MP = MT = 1;
	}
	
	protected Char chooseEnemy() {
		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<>();
			for (Mob mob:Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos()]) {
					enemies.add( mob );
				}
			}
			
			enemy = enemies.size() > 0 ? Random.element( enemies ) : null;
		}
		
		return enemy;
	}
	
	@Override
	public String description() {
		return
			"This illusion bears a close resemblance to you, " +
			"but it's paler and twitches a little.";
	}
	
	@Override
	public CharSprite sprite() {
		CharSprite s = super.sprite();
		//((MirrorSprite)s).updateArmor( tier );
		return s;
	}

	@Override
	public void interact() {
		int curPos = pos();
		
		moveSprite( pos(), Dungeon.hero.pos() );
		move( Dungeon.hero.pos() );
		
		Dungeon.hero.sprite.move( Dungeon.hero.pos(), curPos );
		Dungeon.hero.move( curPos );
		
		Dungeon.hero.spend_new(Dungeon.hero.movementSpeed(), false);
		Dungeon.hero.busy();
	}

}
