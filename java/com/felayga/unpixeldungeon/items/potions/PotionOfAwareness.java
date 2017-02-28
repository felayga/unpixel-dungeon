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
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.IntrinsicAwareness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndInfoBuff;
import com.watabou.noosa.audio.Sample;

/**
 * Created by HELLO on 2/26/2017.
 */
public class PotionOfAwareness extends Potion {

    public PotionOfAwareness()
    {
        name = "Potion of Awareness";
        initials = "Aw";

        isHarmful = false;
        price = 200;
    }

    @Override
    public void apply( Char hero ) {
        apply(hero, hero, true);
    }

    private void apply(Char target, Char owner, boolean quaffed) {
        if (quaffed) {
            setKnown();
        }

        switch(bucStatus()) {
            case Cursed:
                if (target == Dungeon.hero) {
                    Hero hero = (Hero)target;

                    hero.useAttribute(AttributeType.INTWIS, -8);

                    GLog.w("You have an uneasy feeling...");
                }
                 else {
                    target.damageAttribute(AttributeType.INTWIS, 1);

                    GLog.w("The " + target.name + " looks uneasy.");
                }
                break;
            default:
                Buff buff = Buff.prolong(target, owner, IntrinsicAwareness.class, 16 * GameTime.TICK);
                if (target == Dungeon.hero) {
                    GameScene.show(new WndInfoBuff(buff));
                }
                break;
        }
    }

    @Override
    public void shatter( Char owner, int cell ) {

        if (Dungeon.visible[cell]) {
            splash(cell);
        }
        if (Dungeon.audible[cell]) {
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }

        Char test = Actor.findChar(cell);

        if (test != null) {
            apply(test, owner, false);
        }
    }

    @Override
    public String desc() {
        return
                "This potion will grant temporary awareness of your intrinsic resistances and abilities.";
    }

}

