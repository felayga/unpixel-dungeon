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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.*;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class CursingTrap extends Trap {

	{
		name = "Cursing trap";
		color = TrapSprite.VIOLET;
		shape = TrapSprite.WAVES;
	}

	@Override
	public void activate() {
		if (Dungeon.visible[ pos ]) {
			CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
			Sample.INSTANCE.play(Assets.SND_CURSED);
		}

		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null){
			for (Item item : heap.items){
				if (item.isUpgradable())
					item.cursed = item.cursedKnown = true;
			}
		}

		if (Dungeon.hero.pos == pos){
			Hero user = Dungeon.hero;

			KindOfWeapon weapon = user.belongings.weapon;
			KindOfWeapon offhand = user.belongings.offhand;
			Tool tool1 = user.belongings.tool1;
			Tool tool2 = user.belongings.tool2;

			Armor armor = user.belongings.armor;
			Armor gloves = user.belongings.gloves;
			Armor boots = user.belongings.boots;
			Armor cloak = user.belongings.cloak;

			KindofMisc misc1 = user.belongings.ring1;
			KindofMisc misc2 = user.belongings.ring2;
			KindofMisc amulet = user.belongings.amulet;
			KindofMisc face = user.belongings.face;

			if (weapon != null) weapon.cursed = weapon.cursedKnown = true;
			if (offhand != null) offhand.cursed = offhand.cursedKnown = true;
			if (tool1 != null) tool1.cursed = tool1.cursedKnown = true;
			if (tool2 != null) tool2.cursed = tool2.cursedKnown = true;

			if (armor != null)  armor.cursed = armor.cursedKnown = true;
			if (gloves != null) gloves.cursed = gloves.cursedKnown = true;
			if (boots != null) boots.cursed = boots.cursedKnown = true;
			if (cloak != null) cloak.cursed = cloak.cursedKnown = true;

			if (misc1 != null)  misc1.cursed = misc1.cursedKnown = true;
			if (misc2 != null)  misc2.cursed = misc2.cursedKnown = true;
			if (amulet != null) amulet.cursed = amulet.cursedKnown = true;
			if (face != null) face.cursed = face.cursedKnown = true;

			EquipableItem.equipCursed(user);
			GLog.n("Your worn equipment becomes cursed!");
		}
	}

	@Override
	public String desc() {
		return "This trap contains the same malevolent magic found in cursed equipment. " +
				"Triggering it will curse all worn items, and all items in the immediate area.";
	}
}
