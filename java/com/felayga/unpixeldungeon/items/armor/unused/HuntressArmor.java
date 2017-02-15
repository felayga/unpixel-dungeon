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
package com.felayga.unpixeldungeon.items.armor.unused;
/*
import java.util.HashMap;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.weapon.missiles.martial.Shuriken;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.sprites.MissileSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class HuntressArmor extends ClassArmor {
	
	private static final String TXT_NO_ENEMIES 		= "No enemies in sight";
	private static final String TXT_NOT_HUNTRESS	= "Only huntresses can use this armor!";
	
	private static final String AC_SPECIAL = "SPECTRAL BLADES";

	public HuntressArmor(int armor, int armorBonusMaximum, long speedModifier, long equipTime, int spellFailure) {
		super(armor, armorBonusMaximum, speedModifier, equipTime, spellFailure);
		name = "huntress cloak";
		image = ItemSpriteSheet.ARMOR_HUNTRESS;
	}
	
	private HashMap<Callback, Mob> targets = new HashMap<>();
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {
		final KindOfWeapon proto = new Shuriken();

		for (Mob mob : Dungeon.level.mobs) {
			if (Level.fieldOfView[mob.pos]) {
				
				Callback callback = new Callback() {
					@Override
					public void call() {
						curUser.attack( proto, true, targets.get( this ) );
						targets.remove( this );
						if (targets.isEmpty()) {
							//todo: attack delay, this should probably disappear anyway
							curUser.spend(proto.delay_new, true);
						}
					}
				};
				
				((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
					reset( curUser.pos, mob.pos, proto, callback );
				
				targets.put( callback, mob );
			}
		}
		
		if (targets.size() == 0) {
			GLog.w( TXT_NO_ENEMIES );
			return;
		}
		
		curUser.HP -= (curUser.HP / 3);
		
		curUser.sprite.zap( curUser.pos );
		curUser.busy();
	}

	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.HUNTRESS) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_HUNTRESS );
			return false;
		}
	}

	@Override
	public String desc() {
		return
			"A huntress in such cloak can create a fan of spectral blades. Each of these blades " +
			"will target a single enemy in the huntress's field of view, inflicting damage depending " +
			"on her currently equipped melee weapon.";
	}
}
*/