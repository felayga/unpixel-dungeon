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
package com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.PinCushion;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.equippableitem.ring.RingOfSharpshooting;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.Weapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.martial.Boomerang;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ranged.AmmunitionType;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MissileWeapon extends Weapon {
    public boolean throwable;
    public AmmunitionType ammunitionType;
    public boolean requiresLauncher;

	public MissileWeapon(WeaponSkill weaponSkill, long delay, int damageMin, int damageMax, boolean throwable, AmmunitionType ammunitionType) {
        super(weaponSkill, delay, damageMin, damageMax);

        stackable = true;

        this.throwable = throwable;
        this.ammunitionType = ammunitionType;
        this.cursedCannotUnequip = false;

        defaultAction = null;

        usesTargeting = true;
    }

    @Override
    public EquippableItem.Slot[] getSlots() {
        return new Slot[]{ Slot.Ammo };
    }

    @Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);

        if (throwable && isEquipped(hero)) {
            actions.add(Constant.Action.THROW);
        }

        /*
		if (hero.heroClass != HeroClass.HUNTRESS && hero.heroClass != HeroClass.ROGUE) {
			actions.remove( AC_EQUIP );
			actions.remove( AC_UNEQUIP );
		}
		*/
		return actions;
	}

	@Override
	protected void onThrow( Char thrower, int cell ) {
		Char enemy = Actor.findChar(cell);
		if (enemy == null || enemy == thrower) {
			if (this instanceof Boomerang)
				super.onThrow( thrower, cell );
			else
				miss( cell, thrower );
		} else {
			if (!thrower.shoot( enemy, this )) {
				miss( cell, thrower );
			} else if (!(this instanceof Boomerang)){
				int bonus = 0;

				for (Buff buff : thrower.buffs(RingOfSharpshooting.Aim.class)) {
                    bonus += ((RingOfSharpshooting.Aim) buff).level;
                }

                /*
				if (thrower.heroClass == HeroClass.HUNTRESS && enemy.buff(PinCushion.class) == null) {
                    bonus += 3;
                }
                */

				if (Random.Float() > Math.pow(0.7, bonus)) {
                    Buff.affect(enemy, thrower, PinCushion.class).stick(this);
                }
			}
		}
	}
	
	protected void miss( int cell, Char thrower ) {
        super.onThrow(thrower, cell);

        /*
        //todo: determine if any of this is cared about
        int bonus = 0;
		for (Buff buff : curUser.buffs(RingOfSharpshooting.Aim.class)) {
			bonus += ((RingOfSharpshooting.Aim)buff).level;
		}

		//degraded ring of sharpshooting will even make missed shots break.
		if (Random.Float() < Math.pow(0.6, -bonus)) {
            super.onThrow(cell, thrower);
        }
        */
	}

    /*
	@Override
	public int proc( Char attacker, boolean thrown, Char defender, int damage ) {
		damage = super.proc( attacker, thrown, defender, damage );

		if (attacker == Dungeon.hero) {
			Hero hero = (Hero) attacker;
			if (stackable) {
				quantity(quantity()-1);

				//if (quantity == 1) {
				//	GLog.d("MissileWeapon:doUnequip()");
				//	doUnequip(hero, false, false);
				//} else {
				//	GLog.d("MissileWeapon:detach()");
				//	detach(null);
				//}
			}
		}

		return damage;
	}
    */

	/*
	@Override
	public boolean doEquip( final Char hero ) {
		GameScene.show(
			new WndOptions( TXT_MISSILES, TXT_R_U_SURE, TXT_YES, TXT_NO) {
				@Override
				protected void onSelect(int index) {
					if (index == 0) {
						MissileWeapon.super.doEquip( hero );
					}
				};
			}
		);
		
		return false;
	}
	*/
	
	@Override
	public String info() {
		
		StringBuilder info = new StringBuilder( desc() );
		
		info.append( "\n\nAverage damage of this weapon equals to " + (damageMin + (damageMax - damageMin) / 2) + " points per hit. " );

		/*
		if (Dungeon.hero.belongings.backpack.items.contains( this )) {
			if (STR > Dungeon.hero.STR()) {
				info.append(
					"\n\nBecause of your inadequate strength the accuracy and speed " +
					"of your attack with this " + name + " is decreased." );
			}
			if (STR < Dungeon.hero.STR() && Dungeon.hero.heroClass == HeroClass.HUNTRESS) {
				info.append(
					"\n\nBecause of your excess strength the damage " +
					"of your attack with this " + name + " is increased." );
			}
		}
		*/
		info.append( "\n\nAs this weapon is designed to be used at a distance, it is much less accurate if used at melee range.");
		
		if (isEquipped( Dungeon.hero )) {
			info.append( "\n\nYou hold the " + name + " at the ready." );
		}
		
		return info.toString();
	}
}
