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
package com.felayga.unpixeldungeon.items.spells.inventory;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.spells.Spell;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.spellcasting.Spellcaster;
import com.felayga.unpixeldungeon.windows.WndBackpack;

public abstract class InventorySpell extends Spell {
    protected WndBackpack.Mode mode = WndBackpack.Mode.ALL;

    public InventorySpell(long castTime) {
        super(castTime);
    }

    @Override
    protected void doCast() {
        Spellcaster.cast(curUser, curUser.pos(), spellcaster, Spellcaster.Origin.Spell);
    }

    abstract protected void onItemSelected(Item item);

    protected static WndBackpack.Listener itemSelector = new WndBackpack.Listener() {
        @Override
        public void onSelect(Item item) {
            InventorySpell spell = (InventorySpell) curItem;
            spell.onItemSelected(item);
        }
    };
}
