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

package com.felayga.unpixeldungeon.actors.buffs.negative;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/7/2017.
 */
public class AcidBurning extends Buff implements Hero.Doom {
    private static final long DURATION = 4 * GameTime.TICK;

    private long left;

    public AcidBurning()
    {
        type = buffType.NEGATIVE;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( TIMELEFT, left );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        left = bundle.getLong( TIMELEFT );
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            target.damage(Random.IntRange(1, 4), MagicType.Acid, Char.Registry.get(ownerRegistryIndex()), null);
        } else {
            detach();
        }

        spend_new(GameTime.TICK, false);
        left -= GameTime.TICK;

        if (left <= 0 ||
                /*Random.Float() > (2 + (float) target.HP / target.HT) / 3 ||*/
                (Level.puddle[target.pos()] && !target.flying())) {

            detach();
        }

        return true;
    }

    public void resplash( Char ch ) {
        left = 4 * GameTime.TICK;
    }

    @Override
    public int icon() {
        return BuffIndicator.FIRE;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.ACIDBURNING);
        else target.sprite.remove(CharSprite.State.ACIDBURNING);
    }

    @Override
    public String toString() {
        return "Acid";
    }

    @Override
    public String attachedMessage(boolean isHero) {
        if (isHero) {
            return "Acid is burning your flesh!";
        }

        return super.attachedMessage(isHero);
    }

    @Override
    public String desc() {
        return "Few things are more distressing than being drenched in acid.\n" +
                "\n" +
                "Acid will deal damage every turn until it is washed off by water, burnt off with fire, expires, or it is resisted. " +
                "Acid can be removed by stepping into water, or from the splash of a shattering potion. \n" +
                "\n" +
                "Additionally, the acid may eat away items that it comes into contact with.\n" +
                "\n" +
                "The acid will last for " + dispTurns(left) + ", or until it is resisted or washed off.";
    }

    @Override
    public void onDeath() {
        Dungeon.fail( ResultDescriptions.BURNING );
        GLog.n( "Your flesh melts away..." );
    }
}


