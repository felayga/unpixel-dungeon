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

package com.felayga.unpixeldungeon.spellcasting.itemspellcaster;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.spellcasting.Spellcaster;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndOptions;

/**
 * Created by HELLO on 3/11/2017.
 */

public abstract class ItemSpellcaster extends Spellcaster {
    public ItemSpellcaster(String name, int level) {
        super(name, level);

        backpackMode = WndBackpack.Mode.ALL;
    }

    private static ItemSpellcaster curSpellcaster;

    protected static WndBackpack.Mode backpackMode;

    public void prepareZap(int levelBoost) {
        super.prepareZap(levelBoost);

        curSpellcaster = this;
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        playerSelectItem(source);
    }

    private static Char curSource;

    private void playerSelectItem(Char source) {
        curSource = source;
        GameScene.selectItem(itemSelector, backpackMode, "Select an item", null);
    }

    protected static WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null) {
                curSpellcaster.onItemSelected(curSource, item);
            } else {
                curSpellcaster.confirmCancelation();
            }
        }
    };

    protected abstract void onItemSelected(Char source, Item item);

    private void confirmCancelation() {
        GameScene.show(new WndOptions(
                name + " spell",
                "Do you really want to cancel this spell usage? It will be consumed anyway.",
                "Yes, I'm positive",
                "No, I changed my mind") {
            @Override
            protected void onSelect(int index) {
                switch (index) {
                    case 0:
                        //stop usage
                        break;
                    case 1:
                        playerSelectItem(curSource);
                        break;
                }
            }

            public void onBackPressed() {
            }
        });
    }

}
