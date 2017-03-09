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

package com.felayga.unpixeldungeon.items.spells.position;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.spells.Spell;
import com.felayga.unpixeldungeon.items.spells.inventory.InventorySpell;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.windows.WndBackpack;

/**
 * Created by HELLO on 3/7/2017.
 */

public abstract class PositionSpell extends Spell {
    protected WndBackpack.Mode mode = WndBackpack.Mode.ALL;

    public PositionSpell(long castTime) {
        super(castTime);
    }

    @Override
    protected void doCast() {
        GameScene.selectCell(cellSelector, "Cast " + name);
    }

    abstract protected void onPositionSelected(int pos);

    protected static CellSelector.Listener cellSelector = new CellSelector.Listener() {
        @Override
        public boolean onSelect(Integer target) {
            if (target != null && target != Constant.Position.NONE) {
                PositionSpell spell = (PositionSpell)curItem;
                spell.onPositionSelected(target);
            }
            return true;
        }
    };
}
