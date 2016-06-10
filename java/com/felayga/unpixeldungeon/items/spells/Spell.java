/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */

package com.felayga.unpixeldungeon.items.spells;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.utils.GLog;

import java.util.ArrayList;

/**
 * Created by hello on 3/18/16.
 */
public abstract class Spell extends Item {
    protected static final String AC_CAST = "CAST";
    protected static final String AC_FORGET = "FORGET";

    public long castTime;

    public int spellLevel;

    public Spell(int spellLevel, long castTime)
    {
        super();

        this.spellLevel = spellLevel;
        this.castTime = castTime;

        droppable = false;
        hasBuc(false);
        levelKnown = true;
        hasLevels = false;
        defaultAction = AC_CAST;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> retval = new ArrayList<>();
        retval.add(AC_FORGET);
        retval.add(AC_CAST);
        return retval;
    }

    @Override
    public boolean execute( Hero hero, String action ) {
        if (action.equals( AC_CAST )) {
            if (hero.MP >= spellLevel * 5) {
                if (hero.tryCastSpell(spellLevel)) {
                    hero.MP -= spellLevel * 5;
                }
                else {
                    hero.MP -= (spellLevel * 5) / 2;
                    GLog.n("Your spell fizzles!");
                    hero.sprite.emitter().burst(Speck.factory(Speck.FIZZLE), 2);
                    return false;
                }

                if (!startedCasting) {
                    startedCasting = true;
                    castTimeLeft = castTime;
                }

                if (castTimeLeft > GameTime.TICK) {
                    hero.spend(GameTime.TICK, false);
                    castTimeLeft -= GameTime.TICK;

                    return true;
                } else {
                    hero.spend(castTimeLeft, false);
                    castTimeLeft = 0;

                    prepareCast(hero);
                    doCast();

                    return false;
                }
            } else {
                GLog.n("Not enough mana.");
                return false;
            }
        } else {
            return super.execute( hero, action );
        }
    }

    private boolean startedCasting;
    private long castTimeLeft;

    public void interrupt()
    {
        startedCasting = false;
        GLog.w("Your spellcasting was interrupted!");
    }

    protected void prepareCast(Hero hero) {
        curUser = hero;
        curItem = this;
    }

    abstract protected void doCast();

}
