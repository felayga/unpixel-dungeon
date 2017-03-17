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

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.Identification;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.spellcasting.Spellcaster;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/11/2017.
 */

public class IdentifySpellcaster extends ItemSpellcaster {
    public IdentifySpellcaster() {
        super("Identify", 3);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopSelf);
        backpackMode = WndBackpack.Mode.UNIDENTIFED;
    }

    @Override
    protected Callback buildCallback(final Char source, final Ballistica shot, final int targetPos) {
        return new Callback() {
            @Override
            public void call() {
                onZap(source, shot, targetPos);
            }
        };
    }

    private int usesLeft;

    @Override
    public void prepareZap(int levelBoost) {
        super.prepareZap(levelBoost);

        if (levelBoost > 0) {
            int roll = Random.Int(5);
            if (roll == 0) {
                usesLeft = -1;
            } else {
                usesLeft = roll;
            }
        } else if (levelBoost < 0) {
            usesLeft = 1;
        } else {
            int roll = Random.Int(25);
            if (roll == 0) {
                usesLeft = -1;
            } else if (roll < 4) {
                usesLeft = roll + 1;
            } else {
                usesLeft = 1;
            }
        }
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        source.sprite.parent.add(new Identification(source.sprite.center().offset(0, -16)));

        if (callback != null) {
            callback.call();
        }
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        if (usesLeft > 0) {
            super.onZap(source, path, targetPos);
        } else {
            source.belongings.identifyAll(true, true);
            GLog.p("You now know everything about your possessions.");
        }
    }

    @Override
    protected void onItemSelected(Char source, Item item) {
        item.identify();
        GLog.i("It is " + item.getDisplayName());
        Badges.validateItemLevelAquired(item);

        usesLeft--;

        if (usesLeft > 0) {
            super.onZap(source, null, source.pos());
        }
    }

}
