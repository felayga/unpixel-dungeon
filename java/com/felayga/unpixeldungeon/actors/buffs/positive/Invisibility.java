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
package com.felayga.unpixeldungeon.actors.buffs.positive;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.FlavourBuff;
import com.felayga.unpixeldungeon.items.artifacts.CloakOfShadows;
import com.felayga.unpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;

import java.util.ArrayList;
import java.util.List;

public class Invisibility extends FlavourBuff {

    public static final long DURATION = GameTime.TICK * 20;

    private boolean improved;

    protected void improved(boolean value) {
        this.improved = value;

        if (value) {
            name = "Improved Invisibility";
            icon = BuffIndicator.INVISIBLE_IMPROVED;
        } else {
            name = "Invisibility";
            icon = BuffIndicator.INVISIBLE;
        }
    }

    public boolean improved() {
        return improved;
    }

    {
        type = buffType.POSITIVE;
        improved = false;
    }


    @Override
    public boolean attachTo(Char target, Char source) {
        if (super.attachTo(target, source)) {
            target.invisible++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        if (target.invisible > 0) {
            target.invisible--;
        }
        super.detach();
    }

    private int icon;

    @Override
    public int icon() {
        return icon;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.INVISIBLE);
        else if (target.invisible == 0) target.sprite.remove(CharSprite.State.INVISIBLE);
    }

    private String name;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String desc() {
        return "You are completely blended into the surrounding terrain, making you impossible to see.\n" +
                "\n" +
                "While you are invisible enemies are unable to see you, though they may be able to hear you. " +
                (improved ?
                        "" :
                        "Physical attacks and magical effects (such as scrolls and wands) will immediately cancel invisibility.\n") +
                "\n" +
                descDuration();
    }

    protected String descDuration() {
        return "This invisibility will last for " + dispTurns() + ".";
    }

    public static void dispel(Char target) {
        List<Invisibility> pendingRemoval = new ArrayList<>();

        for (Buff b : target.buffs()) {
            if (b instanceof Invisibility) {
                Invisibility invisibility = (Invisibility) b;

                pendingRemoval.add(invisibility);
            }
        }

        for (Invisibility invisibility : pendingRemoval) {
            invisibility.detach();
        }

        /*
		CloakOfShadows.cloakStealth cloakBuff = Dungeon.hero.buff( CloakOfShadows.cloakStealth.class );
		if (cloakBuff != null) {
			cloakBuff.dispel();
		}
		//this isn't a form of invisibility, but it is meant to dispel at the same time as it.
		TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff( TimekeepersHourglass.timeFreeze.class );
		if (timeFreeze != null) {
			timeFreeze.detach();
		}
		*/
    }

    public static void dispelAttack(Char target) {
        List<Invisibility> pendingRemoval = new ArrayList<>();

        for (Buff b : target.buffs()) {
            if (b instanceof Invisibility) {
                Invisibility invisibility = (Invisibility) b;

                if (!invisibility.improved) {
                    pendingRemoval.add(invisibility);
                }
            }
        }

        for (Invisibility invisibility : pendingRemoval) {
            invisibility.detach();
        }
    }

    public static class Indefinite extends Invisibility {
        public Indefinite() {
        }

        public static Indefinite prolong(Char target, Char source, Class<? extends Indefinite> invisibility) {
            return Buff.prolong(target, source, invisibility, GameTime.TICK * 1024);
        }

        @Override
        public boolean act() {
            spend_new(GameTime.TICK * 1024, false);
            return true;
        }

        @Override
        protected String descDuration() {
            return "The invisibility will last indefinitely.";
        }

    }
}
