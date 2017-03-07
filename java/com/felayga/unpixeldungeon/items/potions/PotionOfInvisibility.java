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
package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRage;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Random;

public class PotionOfInvisibility extends Potion {

    public PotionOfInvisibility() {
        name = "Potion of Invisibility";
        initials = "In";

        isHelpful = true;

        price = 40;
    }

    @Override
    public void apply(Char hero) {
        setKnown();

        switch(bucStatus()) {
            case Blessed:
                Buff.affect(hero, hero, Invisibility.Indefinite.class);
                break;
            case Cursed:
                ScrollOfRage.enrage(curUser, curUser.pos(), 16);
                //passthrough
            default:
                Buff.prolong(hero, hero, Invisibility.class, Random.IntRange(30, 45) * GameTime.TICK);
                break;
        }

        GLog.i("You see your hands turn invisible!");
        Sample.INSTANCE.play(Assets.SND_MELD);
    }

    @Override
    public String desc() {
        return
                "Drinking this potion will render you temporarily invisible. While invisible, " +
                        "enemies will be unable to see you. Attacking an enemy, as well as using a wand or a scroll " +
                        "before enemy's eyes, will dispel the effect.";
    }

}
