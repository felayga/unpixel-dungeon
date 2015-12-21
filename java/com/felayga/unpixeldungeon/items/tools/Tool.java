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

import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.EquipableItem;
import com.felayga.unpixeldungeon.items.KindofMisc;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.WndOptions;

/**
 * Created by hello on 12/21/15.
 */
public class Tool extends EquipableItem {
    protected static final float TIME_TO_EQUIP = 1f;

    private static final String TXT_UNEQUIP_TITLE = "Unequip one tool";
    private static final String TXT_UNEQUIP_MESSAGE =
            "You can only have two tools readied at a time.";

    @Override
    public boolean doEquip( final Hero hero ) {

        if (hero.belongings.tool1 != null && hero.belongings.tool2 != null) {

            final Tool m1 = hero.belongings.tool1;
            final Tool m2 = hero.belongings.tool2;

            ShatteredPixelDungeon.scene().add(
                    new WndOptions(TXT_UNEQUIP_TITLE, TXT_UNEQUIP_MESSAGE,
                            Utils.capitalize(m1.toString()),
                            Utils.capitalize(m2.toString())) {

                        @Override
                        protected void onSelect(int index) {
                            Tool equipped;
                            switch(index) {
                                case 0:
                                    equipped = m1;
                                    break;
                                default:
                                    equipped = m2;
                                    break;
                            }
                            if (equipped.doUnequip(hero, true, false)) {
                                doEquip(hero);
                            }
                        }
                    });

            return false;

        } else {

            if (hero.belongings.tool1 == null) {
                hero.belongings.tool1 = this;
            }
            else {
                hero.belongings.tool2 = this;
            }

            detach(hero.belongings.backpack);

            cursedKnown = true;
            if (cursed) {
                equipCursed( hero );
                GLog.n( "you become fixated by your " + this + " and can't seem to put it away" );
            }

            hero.spendAndNext( TIME_TO_EQUIP );
            return true;

        }

    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {

            if (hero.belongings.tool1 == this) {
                hero.belongings.tool1 = null;
            }
            else {
                hero.belongings.tool2 = null;
            }

            return true;

        } else {

            return false;
        }
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.tool1 == this || hero.belongings.tool2 == this;
    }

}
