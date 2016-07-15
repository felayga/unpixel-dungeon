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
package com.felayga.unpixeldungeon.items.food;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.DeathlySick;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.hero.Sick;
import com.felayga.unpixeldungeon.actors.buffs.negative.Hallucination;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Corpse extends Food implements IDecayable {

    public Corpse() {
        this((Char) null);
    }

    public Corpse(Char source) {
        super(source != null ? source.nutrition : 1, source != null ? source.weight : Encumbrance.UNIT);

        if (source != null) {
            name = source.name;
            effects = source.corpseEffects;
            resistances = source.corpseResistances;
            decayTime = source.getTime();
            corpseLevel = source.level;
        } else {
            name = "unknown";
            effects = CorpseEffect.None.value;
            resistances = MagicType.None.value;
        }

        image = getSprite(isRotten(), isVegetable(), isCanned());
        stackable = false;

        hasBuc(false);

        price = 5;
    }

    public Corpse(CannedFood source) {
        super(Food.AMOUNT_EATEN_PER_ROUND, Encumbrance.UNIT * 10);

        hasBuc(true);

        if (source != null) {
            name = source.corpseName();
            effects = source.effects;
            resistances = source.resistances;
            decayTime = Dungeon.hero.getTime();
            corpseLevel = source.corpseLevel;
            bucStatus(source);
        } else {
            name = "unknown";
            effects = CorpseEffect.None.value;
            resistances = MagicType.None.value;
        }

        image = getSprite(isRotten(), isVegetable(), isCanned());
        stackable = false;

        price = 5;
    }

    private static int getSprite(boolean rotten, boolean vegetable, boolean canned) {
        if (canned) {
            if (vegetable) {
                return ItemSpriteSheet.FOOD_CAN_VEGETABLE;
            }

            return ItemSpriteSheet.FOOD_CAN_MEAT;
        } else {
            if (rotten) {
                if (vegetable) {
                    return ItemSpriteSheet.FOOD_LETTUCE_ROTTEN;
                }

                return ItemSpriteSheet.FOOD_MEAT_ROTTEN;
            } else {
                if (vegetable) {
                    return ItemSpriteSheet.FOOD_LETTUCE;
                }

                return ItemSpriteSheet.FOOD_MEAT;
            }
        }
    }


    @Override
    public Item bucStatus(BUCStatus status, boolean statusKnown) {
        BUCStatus oldStatus = bucStatus();

        Item retval = super.bucStatus(status, statusKnown);

        BUCStatus newStatus = bucStatus();

        switch (oldStatus) {
            case Cursed:
                switch (newStatus) {
                    case Uncursed:
                        rot(-2);
                        break;
                    case Blessed:
                        rot(-4);
                        break;
                }
                break;
            case Uncursed:
                switch (newStatus) {
                    case Cursed:
                        rot(2);
                        break;
                    case Blessed:
                        rot(-2);
                        break;
                }
                break;
            case Blessed:
                switch (newStatus) {
                    case Cursed:
                        rot(4);
                        break;
                    case Uncursed:
                        rot(2);
                        break;
                }
                break;
        }

        return retval;
    }

    protected long decay = -30 * GameTime.TICK;
    protected long decayMark = 0;
    private int rottenness = 0;
    protected long decayTime;
    public long effects;
    public long resistances;
    public int corpseLevel;

    protected void rot(int amount) {
        boolean rotten = isRotten();

        rottenness += amount;

        boolean newRotten = isRotten();

        if (rotten != newRotten || amount == 0) {
            image = getSprite(newRotten, isVegetable(), isCanned());
            updateQuickslot();
        }
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

        //GLog.d("decay="+decay+" rottenness="+rottenness);
        while (decayMark <= decay) {
            decayMark += Random.Long(GameTime.TICK * 10, GameTime.TICK * 30);
            rot(1);
        }

        return (effects & CorpseEffect.Undecayable.value) == 0 && decay > GameTime.TICK * 250;
    }

    public boolean isOld() {
        return decay >= GameTime.TICK * 20;
    }

    public boolean isRotten() {
        if ((effects & CorpseEffect.Rotten.value) != 0) {
            return true;
        }

        return (effects & CorpseEffect.Unrottable.value) == 0 && rottenness >= 4;
    }

    public boolean isTainted() {
        if ((effects & CorpseEffect.Rotten.value) != 0) {
            return true;
        }

        return (effects & CorpseEffect.Unrottable.value) == 0 && rottenness >= 6;
    }

    public boolean isVegetable() {
        return (effects & CorpseEffect.Vegetable.value) != 0;
    }

    public boolean isCanned() {
        return (effects & CorpseEffect.Canned.value) != 0;
    }


    @Override
    protected boolean checkSimilarity(Item item) {
        /*
        if (super.isSimilar(item)) {
            if (item instanceof Corpse) {
                Corpse corpse = (Corpse) item;

                return name.equals(corpse.name);
            }
        }
        */

        return false;
    }


    private static final String CORPSENAME = "corpseName";
    private static final String DECAY = "decay";
    private static final String DECAYMARK = "decayMark";
    private static final String ROTTENNESS = "rottenness";
    private static final String DECAYTIME = "decayTime";
    private static final String EFFECTS = "effects";
    private static final String RESISTANCES = "resistances";
    private static final String CORPSELEVEL = "corpseLevel";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(CORPSENAME, name);
        bundle.put(DECAY, decay);
        bundle.put(DECAYMARK, decayMark);
        bundle.put(ROTTENNESS, rottenness);
        bundle.put(DECAYTIME, decayTime);
        bundle.put(EFFECTS, effects);
        bundle.put(RESISTANCES, resistances);
        bundle.put(CORPSELEVEL, corpseLevel);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        name = bundle.getString(CORPSENAME);
        decay = bundle.getLong(DECAY);
        decayMark = bundle.getLong(DECAYMARK);
        rottenness = bundle.getInt(ROTTENNESS);
        decayTime = bundle.getLong(DECAYTIME);
        effects = bundle.getLong(EFFECTS);
        resistances = bundle.getLong(RESISTANCES);
        corpseLevel = bundle.getInt(CORPSELEVEL);

        rot(0);
    }

    private void qualityCheck(Hero hero) {
        if (isTainted()) {
            GLog.n("Ulch - that food was tainted!");
            Buff.affect(hero, DeathlySick.class);
        } else if (isRotten()) {
            hero.damage(Random.Int(0, 8) + 1, MagicType.Mundane, null);
            Buff.prolong(hero, Sick.class, 14);
        }
    }

    @Override
    public void stoppedEating(Hero hero) {
        super.stoppedEating(hero);

        qualityCheck(hero);
    }

    @Override
    public void doneEating(Hero hero, boolean stuffed) {
        super.doneEating(hero, stuffed);

        qualityCheck(hero);

        applyEffectsResistances(hero, effects, resistances, corpseLevel);
    }

    public static void applyEffectsResistances(Hero hero, long effects, long resistances, int corpseLevel) {
        //region effects

        if ((effects & CorpseEffect.Poisonous.value) != 0) {
            GLog.w("Ecch - that must have been poisonous!");
            if ((hero.immunityMagical & MagicType.Poison.value) != 0) {
                GLog.p("You seem unaffected by the poison.");
            } else {
                hero.damage(Random.Int(1, 15), MagicType.Poison, null);

                hero.useAttribute(AttributeType.STRCON, -Random.Float(1.0f, 2.0f));
            }
        }

        if ((effects & CorpseEffect.ManaBoost.value) != 0) {
            if (hero.MP <= hero.MT * 2 / 3 || Random.Int(3) == 0) {
                GLog.p("You feel a mild buzz.");

                hero.MP += Random.Int(3);
                if (hero.MP > hero.MT) {
                    hero.MT++;
                    hero.MP = hero.MT;
                }
            }
        }

        if ((effects & CorpseEffect.Stunning.value) != 0) {
            long rounds = 15 * GameTime.TICK;
            for (int n = 0; n < 3; n++) {
                rounds += Random.Long(0, 6) * GameTime.TICK;
            }

            Buff.prolong(hero, Paralysis.class, rounds);
        }

        if ((effects & CorpseEffect.Hallucinogenic.value) != 0) {
            long rounds = 15 * GameTime.TICK;
            for (int n = 0; n < 3; n++) {
                rounds += Random.Long(0, 6) * GameTime.TICK;
            }

            Buff.prolong(hero, Hallucination.class, rounds);
        }

        if ((effects & CorpseEffect.Unstoning.value) != 0) {
            //todo: CorpseEffect.Unstoning
            GLog.d("remove petrification");
        }

        if ((effects & CorpseEffect.Acidic.value) != 0) {
            if ((hero.immunityMagical & MagicType.Acid.value) == 0) {
                GLog.p("Ecch.  That really burns!");
                hero.damage(Random.Int(1, 15), MagicType.Acid, null);
            }
        }

        if ((effects & CorpseEffect.Sickening.value) != 0) {
            Buff.prolong(hero, Sick.class, 14);
        }

        //endregion
        //region resistances

        if ((resistances & MagicType.Poison.value) != 0 && Random.Float() <= (float) corpseLevel / 15.0) {
            int test = hero.immunityMagical;
            hero.immunityMagical |= MagicType.Poison.value;

            if (test != hero.immunityMagical) {
                if ((hero.immunityMagical & MagicType.Poison.value) != 0) {
                    GLog.p("You feel healthy.");
                } else {
                    GLog.n("You feel unhealthy.");
                }
            }
        }

        //endregion
    }

    public String corpseName() {
        return name;
    }

    @Override
    public String getName() {
        if (isCanned()) {
            if (partiallyEaten) {
                if (isVegetable()) {
                    return "partially eaten can of " + name;
                } else {
                    return "partially eaten can of " + name + " meat";
                }
            } else {
                if (isVegetable()) {
                    return "can of " + name;
                } else {
                    return "can of " + name + " meat";
                }
            }
        } else {
            if (partiallyEaten) {
                return "partially eaten " + name + " corpse";
            } else {
                return name;
            }
        }
    }


    @Override
    public String info() {
        String retval;

        if (isCanned()) {
            if (isVegetable()) {
                retval = "This is a can of " + name + ".";
            } else {
                retval = "This is a can of " + name + " meat.";
            }
        } else {
            retval = "This is a " + name + "'s corpse.";
        }

        if (isRotten()) {
            retval += "\n\nIt doesn't seem very fresh.";
        } else {
            retval += "  Tasty!";
        }

        return retval + super.info();// + "\n" + "decay=" + decay + " decayTime=" + decayTime;
    }

    @Override
    public String message() {
        return "This " + name + " tastes terrible!";
    }
}
