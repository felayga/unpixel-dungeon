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

package com.felayga.unpixeldungeon.items.equippableitem.armor.boots;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.equippableitem.armor.Armor;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.IWeapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 3/23/2016.
 */
public class Boots extends Armor implements IWeapon {
    //region IWeapon

    public WeaponSkill skillRequired() {
        return WeaponSkill.Martial;
    }

    public AttributeType accuracyAttribute() {
        return AttributeType.DEXCHA;
    }
    public AttributeType damageAttribute() {
        return AttributeType.STRCON;
    }

    public MagicType damageType() {
        return MagicType.Mundane;
    }

    public int accuracyModifier() {
        return level() + 2;
    }

    public int damageRoll() {
        return Random.IntRange(0, 2);
    }

    public int proc(Char attacker, boolean thrown, Char defender, int damage) {
        return damage;
    }

    //endregion

    public Boots(int armor, int armorMagic, long speedModifier) {
        super(armor, 32767, armorMagic, speedModifier, GameTime.TICK / 2, 0);

        defaultAction = null;
    }

    private static Boots curBoots;


    @Override
    public Slot[] getSlots() {
        return new Slot[]{ Slot.Boots };
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped(hero)) {
            actions.add(Constant.Action.KICK);
        }
        return actions;
    }

    @Override
    protected void setHeroSpriteArmor(HeroSprite heroSprite, boolean equip) {
        //empty
    }

    @Override
    public void onEquip(Char owner, boolean cursed) {
        super.onEquip(owner, cursed);

        defaultAction = Constant.Action.KICK;

        if (owner == Dungeon.hero) {
            int index = Dungeon.quickslot.getPlaceholder(this);
            if (index >= 0) {
                Dungeon.quickslot.setSlot(index, this);
            }
        }
    }

    @Override
    public void onUnequip(Char owner)
    {
        super.onUnequip(owner);

        defaultAction = null;

        if (owner == Dungeon.hero) {
            int index = Dungeon.quickslot.getSlot(this);
            if (index >= 0) {
                Dungeon.quickslot.convertToPlaceholder(this);
            }
        }
    }

    @Override
    public boolean execute(Hero hero, String action) {
        curUser = hero;
        curBoots = this;

        if (action.equals(Constant.Action.KICK)) {
            Encumbrance encumbrance = hero.buff(Encumbrance.class);
            switch (encumbrance.current) {
                case UNENCUMBERED:
                case BURDENED:
                case STRESSED:
                    GameScene.selectCell(kicker, "Choose a place to kick");
                    break;
                default:
                    GLog.n("You're carrying too much to balance yourself for a proper kick.");
                    break;
            }

            return false;
        }
        else {
            return super.execute(hero, action);
        }
    }

    public void kick(Char maybeHero, int target) {
        if (maybeHero == Dungeon.hero) {
            Hero hero = (Hero) maybeHero;

            Char ch;
            if (Level.fieldOfView[target] && (ch = Actor.findChar(target)) != null) {
                hero.curAction = new HeroAction.Attack(ch, this);
            } else {
                hero.curAction = new HeroAction.UseItem.Kick(this, target);
            }

            hero.motivate(true);
        }
    }

    protected static CellSelector.Listener kicker = new CellSelector.Listener() {
        @Override
        public boolean onSelect(Integer target) {
            if (target != null) {
                curBoots.kick(curUser, target);
            }
            return true;
        }
    };

}
