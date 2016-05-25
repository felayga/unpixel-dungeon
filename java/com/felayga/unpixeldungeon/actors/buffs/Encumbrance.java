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
package com.felayga.unpixeldungeon.actors.buffs;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Encumbrance extends Buff implements Hero.Doom {
    public static final int UNIT = 128;

    private static final String TXT_INCREASETO_OVERLOADED	= "You collapse under your load.";
    private static final String TXT_INCREASETO_OVERTAXED	= "You can barely move a handspan with this load!";
    private static final String TXT_INCREASETO_STRAINED	    = "You stagger under your heavy load.  Movement is very hard.";
    private static final String TXT_INCREASETO_STRESSED		= "You rebalance your load.  Movement is difficult.";
    private static final String TXT_INCREASETO_BURDENED		= "Your movements are slowed slightly because of your load.";

    private static final String TXT_DECREASETO_STRAINED		= "You stagger under your load.  Movement is still very hard.";
    private static final String TXT_DECREASETO_STRESSED		= "You rebalance your load.  Movement is still difficult.";
    private static final String TXT_DECREASETO_BURDENED		= "Your movements are only slowed slightly by your load.";
    private static final String TXT_DECREASETO_UNENCUMBERED	= "Your movements are now unencumbered.";

    private static final String TXT_TRYLIFT_UNENCUMBERED    = "%s.";
    private static final String TXT_TRYLIFT_BURDENED        = "You have a little trouble lifting %s.";
    private static final String TXT_TRYLIFT_STRESSED        = "You have some trouble lifting %s.";
    private static final String TXT_TRYLIFT_STRAINED        = "You have much trouble lifting %s.";
    private static final String TXT_TRYLIFT_OVERTAXED       = "You have extreme difficulty lifting %s.";
    private static final String TXT_TRYLIFT_OVERLOADED      = "There is %s here, but you cannot lift any more.";

    private int level;
    private int strcon;
    private int hpDecreaseTick;
    private int consumptionTick;

    private static final String HPDECREASETICK  = "hpDecreaseTick";
    private static final String CONSUMPTIONTICK = "consumptionTick";

    public Encumbrance()
    {
        super();

        level = 0;
        strcon = 8;
        hpDecreaseTick = 0;
        consumptionTick = 0;

        current = getEncumbranceLevel();
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(HPDECREASETICK, hpDecreaseTick);
        bundle.put(CONSUMPTIONTICK, consumptionTick);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        hpDecreaseTick = bundle.getInt(HPDECREASETICK);
        consumptionTick = bundle.getInt(CONSUMPTIONTICK);
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            this.level = 0;
            this.strcon = 24;
            current = EncumbranceLevel.UNENCUMBERED;

            update();

            return true;
        }

        return false;
    }

    public EncumbranceLevel current;


    @Override
    public boolean act() {
        if (update()) {
            spend( GameTime.TICK / 2, false );
        } else {
            deactivate();
        }

        return true;
    }

    private boolean update() {
        if (target.isAlive()) {
            Hero hero = (Hero) target;

            if ((hero.belongings.weight != level || hero.STRCON != strcon) && weightChanged(hero.belongings.weight, hero.STRCON)) {
                current = getEncumbranceLevel();

                Hunger hunger = hero.buff(Hunger.class);

                switch (current) {
                    case UNENCUMBERED:
                    case BURDENED:
                        consumptionTick = 0;
                        //intentional fall-through
                    case STRESSED:
                        hunger.regenActive = true;
                        hpDecreaseTick = 0;
                        break;
                    case STRAINED:
                    case OVERTAXED:
                    case OVERLOADED:
                        hunger.regenActive = false;
                        break;
                }
            }

            switch (current) {
                case STRESSED:
                    hero.useAttribute(AttributeType.STRCON, 1);
                    consumptionTick++;
                    break;
                case STRAINED:
                    hero.useAttribute(AttributeType.STRCON, 1);
                    hero.useAttribute(AttributeType.DEXCHA, -1);
                    hpDecreaseTick++;
                    consumptionTick++;
                    break;
                case OVERTAXED:
                case OVERLOADED:
                    hero.useAttribute(AttributeType.STRCON, -1);
                    hero.useAttribute(AttributeType.DEXCHA, -1);
                    hpDecreaseTick += 3;
                    consumptionTick++;
                    break;
            }

            if (hpDecreaseTick >= 60) {
                target.damage(1, MagicType.Mundane, null);
                hpDecreaseTick -= 30;
            }

            if (consumptionTick >= 2) {
                Hunger hunger = hero.buff(Hunger.class);
                if (hunger != null) {
                    hunger.satisfy_new(-1);
                }
            }

            return true;
        }

        return false;
    }

    public void isAttacking() {
        switch(current) {
            case STRAINED:
                hpDecreaseTick += 20;
                break;
        }
    }

    public EncumbranceLevel testWeight(int weightIncrease) {
        return EncumbranceLevel.fromInt(level + weightIncrease, getCapacity(strcon));
    }

    public static String getPickUpMessage(EncumbranceLevel level) {
        switch (level) {
            case UNENCUMBERED:
                return TXT_TRYLIFT_UNENCUMBERED;
            case BURDENED:
                return TXT_TRYLIFT_BURDENED;
            case STRESSED:
                return TXT_TRYLIFT_STRESSED;
            case STRAINED:
                return TXT_TRYLIFT_STRAINED;
            case OVERTAXED:
                return TXT_TRYLIFT_OVERTAXED;
            case OVERLOADED:
                return TXT_TRYLIFT_OVERLOADED;
        }
        return null;
    }

    public static int getCapacity(int strcon) {
        return UNIT * 50 * (strcon + 1);
    }

    private EncumbranceLevel getEncumbranceLevel() {
        return EncumbranceLevel.fromInt(level, getCapacity(strcon));
    }

    private boolean weightChanged( int weight, int strcon ) {
        int oldValue = getCapacity(this.strcon);
        EncumbranceLevel oldEncumbrance = EncumbranceLevel.fromInt(this.level, oldValue);
        oldValue -= this.level;

        this.level = weight;
        this.strcon = strcon;

        int newValue = getCapacity(this.strcon);
        EncumbranceLevel newEncumbrance = EncumbranceLevel.fromInt(this.level, newValue);
        newValue -= this.level;

        if (oldEncumbrance != newEncumbrance) {
            if (oldValue > newValue) {
                switch (newEncumbrance) {
                    case OVERLOADED:
                        GLog.n(TXT_INCREASETO_OVERLOADED);
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, 0L);
                        break;
                    case OVERTAXED:
                        GLog.n(TXT_INCREASETO_OVERTAXED);
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, GameTime.TICK / 8);
                        break;
                    case STRAINED:
                        GLog.n(TXT_INCREASETO_STRAINED);
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, GameTime.TICK / 4);
                        break;
                    case STRESSED:
                        GLog.w(TXT_INCREASETO_STRESSED);
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, GameTime.TICK / 2);
                        break;
                    case BURDENED:
                        GLog.w(TXT_INCREASETO_BURDENED);
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, GameTime.TICK * 3 / 4);
                        break;
                }
            } else {
                switch (newEncumbrance) {
                    case OVERTAXED:
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, GameTime.TICK / 8);
                        break;
                    case STRAINED:
                        GLog.p(TXT_DECREASETO_STRAINED);
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, GameTime.TICK / 4);
                        break;
                    case STRESSED:
                        GLog.p(TXT_DECREASETO_STRESSED);
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, GameTime.TICK / 2);
                        break;
                    case BURDENED:
                        GLog.p(TXT_DECREASETO_BURDENED);
                        target.crippled.put(Constant.DEBUFF_ENCUMBRANCE, GameTime.TICK * 3 / 4);
                        break;
                    case UNENCUMBERED:
                        GLog.p(TXT_DECREASETO_UNENCUMBERED);
                        target.crippled.remove(Constant.DEBUFF_ENCUMBRANCE);
                        break;
                }
            }

            BuffIndicator.refreshHero();

            return true;
        }

        return false;
    }

    public enum EncumbranceLevel {
        OVERLOADED,
        OVERTAXED,
        STRAINED,
        STRESSED,
        BURDENED,
        UNENCUMBERED;

        public static EncumbranceLevel fromInt(int weight, int capacity) {
            if (weight >= capacity * 3) {
                return EncumbranceLevel.OVERLOADED;
            } else if (weight >= capacity * 5 / 2) {
                return EncumbranceLevel.OVERTAXED;
            } else if (weight >= capacity * 2) {
                return EncumbranceLevel.STRAINED;
            } else if (weight >= capacity * 3 / 2) {
                return EncumbranceLevel.STRESSED;
            } else if (weight >= capacity + 1) {
                return EncumbranceLevel.BURDENED;
            } else {
                return EncumbranceLevel.UNENCUMBERED;
            }
        }
    }

    @Override
    public int icon() {
        EncumbranceLevel encumbrance = EncumbranceLevel.fromInt(level, getCapacity(strcon));

        switch(encumbrance)
        {
            case OVERLOADED:
                return BuffIndicator.ENCUMBRANCE_OVERLOADED;
            case OVERTAXED:
                return BuffIndicator.ENCUMBRANCE_OVERTAXED;
            case STRAINED:
                return BuffIndicator.ENCUMBRANCE_STRAINED;
            case STRESSED:
                return BuffIndicator.ENCUMBRANCE_STRESSED;
            case BURDENED:
                return BuffIndicator.ENCUMBRANCE_BURDENED;
        }

        return BuffIndicator.NONE;
    }

    @Override
    public String toString() {
        EncumbranceLevel encumbrance = EncumbranceLevel.fromInt(level, getCapacity(strcon));

        switch(encumbrance)
        {
            case OVERLOADED:
                return "Overloaded";
            case OVERTAXED:
                return "Overtaxed";
            case STRAINED:
                return "Strained";
            case STRESSED:
                return "Stressed";
            case BURDENED:
                return "Burdened";
        }

        return "Unencumbered";
    }

    @Override
    public String desc() {
        switch(current) {
            case BURDENED:
                return "You're carrying too much weight, and it's slowing you down a bit.";
            case STRESSED:
                return "You're carrying too much weight, and movement is somewhat difficult.  However, you're getting a pretty good workout.";
            case STRAINED:
                return "You're carrying far too much weight, and movement is very hard.  You're getting a workout, but not a healthy one.";
            case OVERTAXED:
                return "You're carrying far too much weight, and you can barely move.  You're hurting yourself with the effort.";
            case OVERLOADED:
                return "You're carrying far too much weight, and it's impossible to move.";
        }

        return "";
    }

    @Override
    public void onDeath() {

        Badges.validateDeathFromHunger();

        Dungeon.fail( ResultDescriptions.HUNGER );
        //GLog.n( TXT_DECREASETO_DEAD );
    }
}
