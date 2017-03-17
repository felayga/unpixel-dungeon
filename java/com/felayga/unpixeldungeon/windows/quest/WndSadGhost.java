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

import com.felayga.unpixeldungeon.Challenges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.mobs.unused.GreatCrabSprite;
import com.felayga.unpixeldungeon.sprites.mobs.unused.GnollTricksterSprite;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.IconTitle;
import com.watabou.noosa.BitmapTextMultiline;

//import com.felayga.unpixeldungeon.actors.mobs.npcs.Ghost;
//import com.felayga.unpixeldungeon.sprites.mobs.rat.MarsupialRatFetid;

public class WndSadGhost extends Window {

    private static final String TXT_RAT =
            "Thank you, that horrid rat is slain and I can finally rest..." +
                    "I wonder what twisted magic created such a foul creature...\n\n";
    private static final String TXT_GNOLL =
            "Thank you, that scheming gnoll is slain and I can finally rest..." +
                    "I wonder what twisted magic made it so smart...\n\n";
    private static final String TXT_CRAB =
            "Thank you, that giant crab is slain and I can finally rest..." +
                    "I wonder what twisted magic allowed it to live so long...\n\n";
    private static final String TXT_GIVEITEM =
            "Please take one of these items, they are useless to me now... " +
                    "Maybe they will help you in your journey...\n\n" +
                    "Also... There is an item lost in this dungeon that is very dear to me..." +
                    "If you ever... find my... rose......";
    private static final String TXT_WEAPON = "Ghost's weapon";
    private static final String TXT_ARMOR = "Ghost's armor";

    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 20;
    private static final float GAP = 2;

    public WndSadGhost(final Char ghost, final int type) {

        super();

        IconTitle titlebar = new IconTitle();
        BitmapTextMultiline message;
        switch (type) {
            case 1:
            default:
                //titlebar.icon( new MarsupialRatFetid() );
                titlebar.label("DEFEATED FETID RAT");
                message = PixelScene.createMultiline(TXT_RAT + TXT_GIVEITEM, 6);
                break;
            case 2:
                titlebar.icon(new GnollTricksterSprite());
                titlebar.label("DEFEATED GNOLL TRICKSTER");
                message = PixelScene.createMultiline(TXT_GNOLL + TXT_GIVEITEM, 6);
                break;
            case 3:
                titlebar.icon(new GreatCrabSprite());
                titlebar.label("DEFEATED GREAT CRAB");
                message = PixelScene.createMultiline(TXT_CRAB + TXT_GIVEITEM, 6);
                break;

        }


        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        message.maxWidth = WIDTH;
        message.measure();
        message.y = titlebar.bottom() + GAP;
        add(message);

        RedButton btnWeapon = new RedButton(TXT_WEAPON) {
            @Override
            protected void onClick() {
                selectReward(ghost, ghost.belongings.weapon());
            }
        };
        btnWeapon.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
        add(btnWeapon);

        if (!Dungeon.isChallenged(Challenges.NO_ARMOR)) {
            RedButton btnArmor = new RedButton(TXT_ARMOR) {
                @Override
                protected void onClick() {
                    selectReward(ghost, ghost.belongings.armor());
                }
            };
            btnArmor.setRect(0, btnWeapon.bottom() + GAP, WIDTH, BTN_HEIGHT);
            add(btnArmor);

            resize(WIDTH, (int) btnArmor.bottom());
        } else {
            resize(WIDTH, (int) btnWeapon.bottom());
        }
    }

    private void selectReward(Char ghost, Item reward) {

        hide();

        if (reward.doPickUp(Dungeon.hero)) {
            GLog.i(Hero.TXT_YOU_NOW_HAVE, reward.getDisplayName());
        } else {
            Dungeon.level.drop(reward, ghost.pos()).sprite.drop();
        }

        //ghost.yell( "Farewell, adventurer!" );
        ghost.die(null);

        //todo: fix ghost quest completion bit
        //Ghost.Quest.complete();
    }
}
