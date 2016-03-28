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
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;

public class KindOfWeapon extends EquipableItem {
	private static final String TXT_EQUIP_CURSED	= "The %s welds itself to your hand!";
	
	protected static final long TIME_TO_EQUIP = GameTime.TICK;

	public long delay_new = GameTime.TICK;
    public int damageMin = 1;
    public int damageMax = 4;

    public int accuracy = 10;
    public int damage = 10;

	public int skillRequired = 0;
	public int refined = 0;

    public AttributeType accuracyAttribute = AttributeType.DEXCHA;
	public int accuracyAttributeMaxBonus = 32767;
    public AttributeType damageAttribute = AttributeType.STRCON;

	public KindOfWeapon(long delay,  int damageMin, int damageMax)
	{
		this.delay_new = delay;

		this.damageMin = damageMin;
		this.damageMax = damageMax;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}
	
	@Override
	public boolean isEquipped( Char hero ) {
		return hero.belongings.weapon == this;
	}
	
	@Override
	public boolean doEquip( Char hero ) {
		hero.belongings.detachAll(this);

		if (hero.belongings.weapon == null || hero.belongings.weapon.doUnequip( hero, true )) {
			
			hero.belongings.weapon = this;
			activate( hero );

			updateQuickslot();
			
			if (bucStatus == BUCStatus.Cursed) {
				equipCursed( hero );
				GLog.n( TXT_EQUIP_CURSED, getDisplayName() );
				bucStatusKnown = true;
			}
			
			hero.spend( TIME_TO_EQUIP, true );
			return true;
			
		} else {
			hero.belongings.collect(this);
			return false;
		}
	}

	@Override
	public boolean doUnequip( Char hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			hero.belongings.weapon = null;
			return true;

		} else {

			return false;

		}
	}
	
	public void activate( Char hero ) {
	}
	
	public int damageRoll() {
		return Random.NormalIntRange( damageMin, damageMax ) + this.level;
	}
	
	public int accuracyFactor() {
		return this.level + this.refined;
	}

	/*
	public float speedFactor( Hero hero ) {
		return 1f;
	}
	*/
	
	public int proc( Char attacker, boolean thrown, Char defender, int damage ) {
		return damage;
	}
	
}
