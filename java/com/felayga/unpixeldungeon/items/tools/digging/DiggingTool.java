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

package com.felayga.unpixeldungeon.items.tools.digging;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Boulder;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.tools.ITool;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.SimpleMeleeWeapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 6/2/2016.
 */
public abstract class DiggingTool extends SimpleMeleeWeapon implements ITool {
    public static final String NAME = "digging tool";

    public DiggingTool(long delay, int damageMin, int damageMax) {
        super( delay, damageMin, damageMax );

        pickupSound = Assets.SND_ITEM_BLADE;

        defaultAction = Constant.Action.APPLY;
    }

    @Override
    public String desc() {
        return "This tool is built for digging through dirt and rock, a process which is slow " +
                "and tiring.\nMake sure you have plenty of refreshments on hand.";
    }

    private static DiggingTool curTool;

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Constant.Action.APPLY);
        return actions;
    }

    public String getToolClass() {
        return NAME;
    }

    @Override
    public boolean execute(Hero hero, String action) {
        curUser = hero;
        curTool = this;

        if (action.equals(Constant.Action.APPLY)) {
            GameScene.selectCell(applier, "Apply " + getToolClass());

            return false;
        }
        else {
            return super.execute(hero, action);
        }
    }

    public int effort(Char user, boolean random) {
        int retval;
        if (random) {
            retval = Random.Int(5);
        }
        else {
            retval = 3;
        }

        return 9 + retval + level() + user.getAttributeModifier(AttributeType.STRCON) + user.getAttributeModifier(AttributeType.DEXCHA);
    }

    public void apply(Char ch, int target) {
        if (equipIfNecessary(ch) == EquippableItem.EquipIfNecessaryState.NotEquipped) {
            return;
        }

        if (ch == Dungeon.hero) {
            Hero hero = (Hero)ch;

            Mob mob = Dungeon.level.findMob(target);

            if (Level.solid[target] || target == hero.pos()) {
                hero.curAction = new HeroAction.Dig(this, target, 101);
                hero.motivate(true);
            } else if (mob != null) {
                if (mob instanceof Boulder) {
                    hero.curAction = new HeroAction.Dig(this, (Boulder) mob, 101);
                } else {
                    hero.curAction = new HeroAction.Attack(mob);
                }
                hero.motivate(true);
            } else {
                GLog.n("Your " + getToolClass() + " can't be applied there.");
            }
        }
    }

    public static CellSelector.Listener applier = new CellSelector.Listener() {
        @Override
        public boolean onSelect(Integer target) {
            if (target != null && target != Constant.Position.NONE) {
                curTool.apply(curUser, target);
            }
            return true;
        }
    };
}
