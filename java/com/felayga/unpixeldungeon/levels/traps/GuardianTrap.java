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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.Statue;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.mobs.StatueSprite;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class GuardianTrap extends Trap {

	{
		name = "Guardian trap";
		color = TrapSprite.RED;
		shape = TrapSprite.STARS;
	}

	@Override
	public void activate() {

		for (Mob mob : Dungeon.level.mobs) {
			mob.beckon( pos );
		}

		if (Dungeon.visible[pos]) {
			GLog.w("The trap emits a piercing sound that echoes throughout the dungeon!");
			CellEmitter.center(pos).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );
		}

		Sample.INSTANCE.play( Assets.SND_ALERT );

		for (int i = 0; i < (Dungeon.depth - 5)/5; i++){
			Guardian guardian = new Guardian();
			guardian.state = guardian.WANDERING;
			guardian.pos = Dungeon.level.randomRespawnCell();
			GameScene.add(guardian);
			guardian.beckon(Dungeon.hero.pos );
		}

	}

	@Override
	public String desc() {
		return "This trap is tied to a strange magical mechanism, " +
				"which will summon guardians and alert all enemies on the floor.";
	}

	public static class Guardian extends Statue {

		{
			name = "summoned guardian";
			spriteClass = GuardianSprite.class;

			EXP = 0;
			state = WANDERING;
		}

		public Guardian(){
			super();

			specialWeapon.enchant(null);
			specialWeapon.upgrade(null, -belongings.weapon.level);
		}

		@Override
		public void beckon(int cell) {
			//Beckon works on these ones, unlike their superclass.
			notice();

			if (state != HUNTING) {
				state = WANDERING;
			}
			target = cell;
		}

		@Override
		public String description() {
			return "This blue apparition seems to be a summoned echo of one of the dungeon's stone guardians." +
					"\n\nWhile the statue itself is almost incorporeal, the _" + belongings.weapon.getDisplayName() + "_, it's wielding, looks real.";
		}
	}

	public static class GuardianSprite extends StatueSprite {

		public GuardianSprite(){
			super();
			tint(0, 0, 1, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(0, 0, 1, 0.2f);
		}
	}
}
