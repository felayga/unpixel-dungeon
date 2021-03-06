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
package com.felayga.unpixeldungeon.actors.buffs.hero;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Encumbrance extends Buff implements Hero.Doom, ISpeedModifierBuff {
    public static final int UNIT = 128;

    private static final String TXT_INCREASETO_OVERLOADED = "You collapse under your load.";
    private static final String TXT_INCREASETO_OVERTAXED = "You can barely move a handspan with this load!";
    private static final String TXT_INCREASETO_STRAINED = "You stagger under your heavy load.  Movement is very hard.";
    private static final String TXT_INCREASETO_STRESSED = "You rebalance your load.  Movement is difficult.";
    private static final String TXT_INCREASETO_BURDENED = "Your movements are slowed slightly due to your load.";

    private static final String TXT_DECREASETO_STRAINED = "You stagger under your load.  Movement is still very hard.";
    private static final String TXT_DECREASETO_STRESSED = "You rebalance your load.  Movement is still difficult.";
    private static final String TXT_DECREASETO_BURDENED = "Your movements are only slowed slightly by your load.";
    private static final String TXT_DECREASETO_UNENCUMBERED = "Your movements are now unencumbered.";

    private static final String TXT_TRYLIFT_UNENCUMBERED = "%s.";
    private static final String TXT_TRYLIFT_BURDENED = "You have a little trouble lifting %s.";
    private static final String TXT_TRYLIFT_STRESSED = "You have some trouble lifting %s.";
    private static final String TXT_TRYLIFT_STRAINED = "You have much trouble lifting %s.";
    private static final String TXT_TRYLIFT_OVERTAXED = "You have extreme difficulty lifting %s.";
    private static final String TXT_TRYLIFT_OVERLOADED = "There is %s here, but you cannot lift any more.";

    private int level;
    private int strcon;
    private int hpDecreaseTick;
    private int consumptionTick;

    private static final String HPDECREASETICK = "hpDecreaseTick";
    private static final String CONSUMPTIONTICK = "consumptionTick";
    private static final String MOVEMENTMODIFIER = "movementModifier";

    public Encumbrance() {
        super();

        level = 0;
        strcon = 8;
        hpDecreaseTick = 0;
        consumptionTick = 0;

        current = getEncumbranceLevel();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(HPDECREASETICK, hpDecreaseTick);
        bundle.put(CONSUMPTIONTICK, consumptionTick);
        bundle.put(MOVEMENTMODIFIER, movementModifier);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        hpDecreaseTick = bundle.getInt(HPDECREASETICK);
        consumptionTick = bundle.getInt(CONSUMPTIONTICK);
        movementModifier = bundle.getLong(MOVEMENTMODIFIER);
    }

    @Override
    public boolean attachTo(Char target, Char source) {
        if (super.attachTo(target, source)) {
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
            spend_new(GameTime.TICK / 2, false);
        } else {
            deactivate();
        }

        return true;
    }

    public void updateAppearance(Hero hero) {
        int weight = hero.belongings.weight();

        if ((weight != level || hero.STRCON() != strcon) && weightChanged(weight, hero.STRCON())) {
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
    }

    private boolean update() {
        if (target.isAlive()) {
            Hero hero = (Hero) target;

            updateAppearance(hero);

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
                target.damage(1, MagicType.Mundane, null, null);
                if (!hero.isAlive()) {
                    Dungeon.fail(ResultDescriptions.EXHAUSTION);
                }
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
        switch (current) {
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
            default:
                GLog.d("no pickupmessage for level="+level.toString());
                break;
        }
        return null;
    }

    public static int getCapacity(int strcon) {
        return UNIT * 50 * (strcon + 1);
    }

    private EncumbranceLevel getEncumbranceLevel() {
        return EncumbranceLevel.fromInt(level, getCapacity(strcon));
    }

    private long movementModifier = GameTime.TICK;

    public long movementModifier() {
        return movementModifier;
    }

    public long attackModifier() {
        return GameTime.TICK;
    }

    private boolean weightChanged(int weight, int strcon) {
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
                        movementModifier = 0L;
                        break;
                    case OVERTAXED:
                        GLog.n(TXT_INCREASETO_OVERTAXED);
                        movementModifier = GameTime.TICK * 8;
                        break;
                    case STRAINED:
                        GLog.n(TXT_INCREASETO_STRAINED);
                        movementModifier = GameTime.TICK * 4;
                        break;
                    case STRESSED:
                        GLog.w(TXT_INCREASETO_STRESSED);
                        movementModifier = GameTime.TICK * 2;
                        break;
                    case BURDENED:
                        GLog.w(TXT_INCREASETO_BURDENED);
                        movementModifier = GameTime.TICK * 4 / 3;
                        break;
                }
            } else {
                switch (newEncumbrance) {
                    case OVERTAXED:
                        movementModifier = GameTime.TICK * 8;
                        break;
                    case STRAINED:
                        GLog.p(TXT_DECREASETO_STRAINED);
                        movementModifier = GameTime.TICK * 4;
                        break;
                    case STRESSED:
                        GLog.p(TXT_DECREASETO_STRESSED);
                        movementModifier = GameTime.TICK * 2;
                        break;
                    case BURDENED:
                        GLog.p(TXT_DECREASETO_BURDENED);
                        movementModifier = GameTime.TICK * 4 / 3;
                        break;
                    case UNENCUMBERED:
                        GLog.p(TXT_DECREASETO_UNENCUMBERED);
                        movementModifier = GameTime.TICK;
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

        switch (encumbrance) {
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

        switch (encumbrance) {
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
        switch (current) {
            case BURDENED:
                return "You're carrying too much weight, and it's slowing you down a bit.\n";
            case STRESSED:
                return "You're carrying too much weight, and movement is somewhat difficult.  However, you're getting a pretty good workout.\n";
            case STRAINED:
                return "You're carrying far too much weight, and movement is very hard.  You're getting a workout, but not a healthy one.\n";
            case OVERTAXED:
                return "You're carrying far too much weight, and you can barely move.  You're hurting yourself with the effort.\n";
            case OVERLOADED:
                return "You're carrying far too much weight, and it's impossible to move.\n";
        }

        return "";
    }

    @Override
    public void onDeath() {
    }
}
