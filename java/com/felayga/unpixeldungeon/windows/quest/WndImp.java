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
package com.felayga.unpixeldungeon.windows.quest;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.quest.DwarfToken;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.IconTitle;
import com.watabou.noosa.BitmapTextMultiline;

//import com.felayga.unpixeldungeon.actors.mobs.npcs.Imp;

public class WndImp extends Window {

    private static final String TXT_MESSAGE =
            "Oh yes! You are my hero!\n" +
                    "Regarding your reward, I don't have cash with me right now, but I have something better for you. " +
                    "This is my family heirloom ring: my granddad took it off a dead paladin's finger.";
    private static final String TXT_REWARD = "Take the ring";

    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP = 2;

    public WndImp(final Char imp, final DwarfToken tokens) {

        super();

        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(tokens.image(), null));
        titlebar.label(Utils.capitalize(tokens.getDisplayName()));
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        BitmapTextMultiline message = PixelScene.createMultiline(TXT_MESSAGE, 6);
        message.maxWidth = WIDTH;
        message.measure();
        message.y = titlebar.bottom() + GAP;
        add(message);

        RedButton btnReward = new RedButton(TXT_REWARD) {
            @Override
            protected void onClick() {
                takeReward(imp, tokens, imp.belongings.randomUnequipped());
            }
        };
        btnReward.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
        add(btnReward);

        resize(WIDTH, (int) btnReward.bottom());
    }

    private void takeReward(Char imp, DwarfToken tokens, Item reward) {

        hide();

        Dungeon.hero.belongings.remove(tokens, 1);

        reward.identify();
        if (reward.doPickUp(Dungeon.hero)) {
            GLog.i(Hero.TXT_YOU_NOW_HAVE, reward.getDisplayName());
        } else {
            Dungeon.level.drop(reward, imp.pos()).sprite.drop();
        }

        //imp.flee();

        //Imp.Quest.complete();
    }
}
