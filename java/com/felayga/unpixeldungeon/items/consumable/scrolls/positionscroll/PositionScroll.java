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

package com.felayga.unpixeldungeon.items.consumable.scrolls.positionscroll;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.consumable.scrolls.Scroll;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.unPixelDungeon;
import com.felayga.unpixeldungeon.windows.WndOptions;

/**
 * Created by HELLO on 7/5/2016.
 */
public abstract class PositionScroll extends Scroll {
    private static final String AC_PICKPOSITION = "PS_PICKPOS";
    private static final String AC_CANCELPICK = "PS_CANCELPOS";

    private static final String TXT_WARNING =
            "Do you really want to cancel this scroll usage? " +
                    "It will be consumed anyway.";
    private static final String TXT_YES = "Yes, I'm positive";
    private static final String TXT_NO = "No, I changed my mind";

    @Override
    protected void doRead() {
        super.doRead();

        if (!isKnown()) {
            setKnown();
            identifiedByUse = true;
        } else {
            identifiedByUse = false;
        }
    }

    @Override
    public boolean execute( Hero hero, String action ) {
        if (AC_PICKPOSITION.equals(action)) {
            hero._ready();
            curItem = this;
            curUser = hero;
            GameScene.selectCell(positionSelector, "Select a location");
            return false;
        }
        else if (AC_CANCELPICK.equals(action)) {
            hero._ready();
            confirmCancelation();
            return false;
        }

        return super.execute(hero, action);
    }

    public void doSelectCell(Hero user) {
        user.curAction = new HeroAction.UseItem(this, AC_PICKPOSITION);
        user.motivate(true);
    }

    protected abstract void onPositionSelected(Hero user, int position);

    private void confirmCancelation() {
        unPixelDungeon.scene().add(new WndOptions(getDisplayName(), TXT_WARNING, TXT_YES, TXT_NO) {
            @Override
            protected void onSelect(int index) {
                switch (index) {
                    case 0:
                        curUser.spend_new(TIME_TO_READ, true);
                        identifiedByUse = false;
                        break;
                    case 1:
                        doSelectCell(curUser);
                        break;
                }
            }

            public void onBackPressed() {
            }
        });
    }

    protected static boolean identifiedByUse = false;
    public static CellSelector.Listener positionSelector = new CellSelector.Listener() {
        @Override
        public boolean onSelect(Integer target) {
            if (target != null && target >= 0) {
                if (Dungeon.level.passable[target] && (Dungeon.level.mapped[target] || Dungeon.level.visited[target])) {
                    ((PositionScroll) curItem).onPositionSelected(curUser, target);
                    curUser._ready();
                }
                else {
                    return false;
                }
            } else {
                ((PositionScroll) curItem).confirmCancelation();
            }

            return true;
        }
    };
}
