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

package com.felayga.unpixeldungeon.items.armor.boots;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 3/23/2016.
 */
public class Boots extends Armor {
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

        if (owner instanceof Hero) {
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

        if (owner instanceof Hero) {
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
            GameScene.selectCell(kicker);

            return false;
        }
        else {
            return super.execute(hero, action);
        }
    }

    public void kick(Hero hero, int target) {
        int cell = Dungeon.level.map[target];

        if (cell == Terrain.LOCKED_DOOR || cell == Terrain.DOOR) {
            hero.curAction = new HeroAction.HandleDoor.KickDoor(target);
            hero.motivate(true);
            return;
        }

        hero.spend_new(GameTime.TICK, false);

        Heap heap = Dungeon.level.heaps.get(target);
        if (heap != null) {
            Item item = heap.peek();

            if (item != null) {
            }
        }

        if (Dungeon.level.passable[target] || Dungeon.level.pit[target])
        {
            if (Random.Int(5)==0) {
                GLog.n("Dumb move!  You strain a muscle.");
                hero.useAttribute(AttributeType.STRCON, -1);
                Cripple.prolong(hero, Cripple.class, GameTime.TICK * Random.IntRange(4, 12));
            }
            else {
                GLog.w("You kick at empty space.");
            }
            return;
        }
        else {
            GLog.n("Ouch!  That hurts.");
            hero.damage(Random.IntRange(1,4), MagicType.Mundane, null);
            if (Random.Int(3)==0) {
                Cripple.prolong(hero, Cripple.class, GameTime.TICK * Random.IntRange(4, 12));
            }
            return;
        }

    }

    protected static CellSelector.Listener kicker = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                curBoots.kick(curUser, target);
            }
        }

        @Override
        public String prompt() {
            return "Choose a place to kick";
        }
    };

}
