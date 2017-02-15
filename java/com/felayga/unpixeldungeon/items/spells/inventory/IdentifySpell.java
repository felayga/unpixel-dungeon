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

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.Identification;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;

/**
 * Created by hello on 3/18/16.
 */
public class IdentifySpell extends InventorySpell {
    public IdentifySpell() {
        super(1, GameTime.TICK);

        name = "Identify";
        image = ItemSpriteSheet.SPELL_IDENTIFY;
        mode = WndBackpack.Mode.UNIDENTIFED;
    }

    @Override
    protected void onItemSelected(Item item)
    {
        spellEffect(curUser, item, true);
    }

    public static void spellEffect(Char user, Item item, boolean fx) {
        if (fx) {
            user.sprite.parent.add(new Identification(user.sprite.center().offset(0, -16)));
        }

        if (item != null) {
            item.identify();
            GLog.i("It is " + item.getDisplayName());
            Badges.validateItemLevelAquired(item);
        }
    }
}
