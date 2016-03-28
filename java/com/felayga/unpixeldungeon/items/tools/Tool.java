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
package com.felayga.unpixeldungeon.items.tools;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.EquipableItem;
import com.felayga.unpixeldungeon.items.KindofMisc;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.WndOptions;

import java.util.ArrayList;

/**
 * Created by hello on 12/21/15.
 */
public abstract class Tool extends EquipableItem {
    private static final String TXT_EQUIP_CURSED = "You become fixated with your %s and can't seem to put it away.";

    protected static final long TIME_TO_EQUIP = GameTime.TICK;

    private static final String TXT_UNEQUIP_TITLE = "Unequip one tool";
    private static final String TXT_UNEQUIP_MESSAGE = "You can only have two tools readied at a time.";

    private static final String TXT_REPLACE_TITLE = "Replace tool";
    private static final String TXT_REPLACE_MESSAGE_A = "You can only have one ";
    private static final String TXT_REPLACE_MESSAGE_B = " equipped at a time.";

    private static Tool curTool;


    private boolean canApply;

    public Tool(boolean canApply) {
        this.canApply = canApply;

        if (canApply) {
            defaultAction = null;
        }
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
        return actions;
    }

    @Override
    public String status() {
        return null;
    }

    public abstract String getToolClass();

    @Override
    public boolean doEquip( final Char hero ) {
        final Tool tool1 = hero.belongings.tool1;
        final Tool tool2 = hero.belongings.tool2;

        if (tool1 != null && tool1.getToolClass() == getToolClass()) {
            ShatteredPixelDungeon.scene().add(
                    new WndOptions(TXT_REPLACE_TITLE, TXT_REPLACE_MESSAGE_A + getToolClass() + TXT_REPLACE_MESSAGE_B,
                            Utils.capitalize(tool1.getDisplayName()),
                            Constant.TXT_CANCEL) {

                        @Override
                        protected void onSelect(int index) {
                            Tool equipped = null;
                            switch(index) {
                                case 0:
                                    equipped = tool1;
                                    break;
                            }
                            if (equipped != null) {
                                if (equipped.doUnequip(hero, true, false)) {
                                    doEquip(hero);
                                }
                            }
                        }
                    });
            return false;
        } else if (tool2 != null && tool2.getToolClass() == getToolClass()) {
            ShatteredPixelDungeon.scene().add(
                    new WndOptions(TXT_REPLACE_TITLE, TXT_REPLACE_MESSAGE_A + getToolClass() + TXT_REPLACE_MESSAGE_B,
                            Utils.capitalize(tool2.getDisplayName()),
                            Constant.TXT_CANCEL) {

                        @Override
                        protected void onSelect(int index) {
                            Tool equipped = null;
                            switch(index) {
                                case 0:
                                    equipped = tool2;
                                    break;
                            }
                            if (equipped != null) {
                                if (equipped.doUnequip(hero, true, false)) {
                                    doEquip(hero);
                                }
                            }
                        }
                    });
            return false;
        } else if (tool1 != null && tool2 != null) {
            ShatteredPixelDungeon.scene().add(
                    new WndOptions(TXT_UNEQUIP_TITLE, TXT_UNEQUIP_MESSAGE,
                            Utils.capitalize(tool1.getDisplayName()),
                            Utils.capitalize(tool2.getDisplayName()),
                            Constant.TXT_CANCEL) {

                        @Override
                        protected void onSelect(int index) {
                            Tool equipped = null;
                            switch(index) {
                                case 0:
                                    equipped = tool1;
                                    break;
                                case 1:
                                    equipped = tool2;
                                    break;
                            }
                            if (equipped != null) {
                                if (equipped.doUnequip(hero, true, false)) {
                                    doEquip(hero);
                                }
                            }
                        }
                    });

            return false;

        } else {

            if (tool1 == null) {
                hero.belongings.tool1 = this;
            }
            else {
                hero.belongings.tool2 = this;
            }

            hero.belongings.detach(this);

            if (bucStatus == BUCStatus.Cursed) {
                bucStatusKnown = true;
                equipCursed( hero );
                GLog.n( TXT_EQUIP_CURSED, this.getName() );
            }

            hero.spend( TIME_TO_EQUIP, true );

            if (canApply) {
                defaultAction = AC_APPLY;

                if (hero instanceof Hero) {
                    int index = Dungeon.quickslot.getPlaceholder(this);
                    if (index >= 0) {
                        Dungeon.quickslot.setSlot(index, this);
                    }
                }
            }

            return true;

        }

    }

    @Override
    public boolean doUnequip( Char hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {
            if (hero.belongings.tool1 == this) {
                hero.belongings.tool1 = null;
            }
            else {
                hero.belongings.tool2 = null;
            }

            if (canApply) {
                defaultAction = null;

                if (hero instanceof Hero) {
                    int index = Dungeon.quickslot.getSlot(this);
                    if (index >= 0) {
                        Dungeon.quickslot.convertToPlaceholder(this);
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isEquipped( Char hero ) {
        return hero.belongings.tool1 == this || hero.belongings.tool2 == this;
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

    public abstract void apply(Hero hero, int target);

    protected static CellSelector.Listener applier = new CellSelector.Listener() {
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
