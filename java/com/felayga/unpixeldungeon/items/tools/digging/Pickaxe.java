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

package com.felayga.unpixeldungeon.items.tools.digging;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Boulder;
import com.felayga.unpixeldungeon.items.tools.ITool;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.SimpleMeleeWeapon;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

/**
 * Created by HELLO on 5/25/2016.
 */
public class Pickaxe extends SimpleMeleeWeapon implements ITool {
    public static final String NAME = "digging tool";

    public Pickaxe() {
        super( GameTime.TICK, 1, 6 );

        name = "pickaxe";
        image = ItemSpriteSheet.PICKAXE;

        defaultAction = AC_APPLY;
    }

    @Override
    public String desc() {
        return "This tool is built for digging through rock.";
    }

    @Override
    public void playPickupSound() {
        Sample.INSTANCE.play(Assets.SND_ITEM_BLADE);
    }


    private static Pickaxe curTool;

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_APPLY);
        return actions;
    }

    public String getToolClass() {
        return NAME;
    }

    @Override
    public boolean execute(Hero hero, String action) {
        curUser = hero;
        curTool = this;

        if (action.equals(AC_APPLY)) {
            GameScene.selectCell(applier);

            return false;
        }
        else {
            return super.execute(hero, action);
        }
    }

    public void apply(Hero hero, int target) {
        if (!hero.belongings.isEquipped(this)) {
            if (!hero.belongings.equip(this)) {
                return;
            }

            GLog.i("You ready your " + getDisplayName());
        }

        Mob mob = Dungeon.level.findMob(target);

        if (Dungeon.level.solid[target]) {
            hero.curAction = new HeroAction.Dig(this, target, 101);
        } else if (mob != null) {
            if (mob instanceof Boulder) {
                mob.die(null);
            }
            else {
                hero.curAction = new HeroAction.Attack(this, false, mob);
            }
        } else {
            GLog.n("Your " + getToolClass() + " can't be applied there.");
        }
    }

    public static CellSelector.Listener applier = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                curTool.apply(curUser, target);
            }
        }

        @Override
        public String prompt() {
            return "Apply " + curTool.getToolClass();
        }
    };
}
