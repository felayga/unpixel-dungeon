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

package com.felayga.unpixeldungeon.items.spells;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by hello on 3/18/16.
 */
public abstract class Spell extends Item {
    public long castTime;

    protected long decay = -20000 * GameTime.TICK;
    protected long decayTime;

    public void resetDecay() {
        decay = -20000 * GameTime.TICK;
    }

    public long decay() {
        return decay;
    }

    public boolean decay(long currentTime, boolean updateTime, boolean fixTime) {
        if (fixTime || updateTime) {
            long newAmount = currentTime - decayTime;
            if (fixTime) {
                decayTime = 0;
            } else {
                decayTime = currentTime;
            }
            currentTime = newAmount;
        } else {
            decayTime = currentTime;
            currentTime = 0;
        }

        decay += currentTime;

        return decay >= 0;
    }


    public Spell(long castTime)
    {
        super();

        this.castTime = castTime;

        droppable = false;
        hasBuc(false);
        hasLevels(true);
        defaultAction = Constant.Action.CAST;
    }

    private static final String DECAY = "decay";
    private static final String DECAYTIME = "decayTime";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(DECAY, decay);
        bundle.put(DECAYTIME, decayTime);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        decay = bundle.getLong(DECAY);
        decayTime = bundle.getLong(DECAYTIME);
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> retval = new ArrayList<>();
        retval.add(Constant.Action.FORGET);
        retval.add(Constant.Action.CAST);
        return retval;
    }

    @Override
    public boolean execute( Hero hero, String action ) {
        int spellLevel = level();

        if (action.equals( Constant.Action.CAST )) {
            if (hero.MP >= spellLevel * 5) {
                if (hero.tryCastSpell(spellLevel)) {
                    hero.MP -= spellLevel * 5;
                } else {
                    hero.MP -= (spellLevel * 5) / 2;
                    GLog.n("Your spell fizzles!");
                    hero.sprite.emitter().burst(Speck.factory(Speck.FIZZLE), 2);
                    return false;
                }

                hero.curAction = new HeroAction.UseItem.SlowAction(this, Constant.Action.SLOW_ACTION, castTime);
                hero.motivate(true);
                return true;
            } else {
                GLog.n("Not enough mana.");
                return false;
            }
        } else if (action.equals(Constant.Action.SLOW_ACTION)) {
            prepareCast(hero);
            doCast();
            return false;
        } else if (action.equals(Constant.Action.FORGET)) {
            GameScene.show(
                    new WndOptions("Forget " + getName(), "Are you sure you want to forget this spell?", Constant.Text.YES, Constant.Text.NO) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Spell.this.forget();
                            }
                        }
                    }
            );
            return false;
        } else {
            return super.execute( hero, action );
        }
    }

    private void forget() {
        GLog.w("You forgot how to cast " + getName()+".");
        parent().remove(this);
    }

    protected void prepareCast(Hero hero) {
        curUser = hero;
        curItem = this;
    }

    abstract protected void doCast();

}
