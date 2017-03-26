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

package com.felayga.unpixeldungeon.items.consumable.food;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Spinach;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.tools.CanOpener;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.simple.dagger.Dagger;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by HELLO on 7/8/2016.
 */
public class CannedFood extends Food {
    public enum Variety {
        None(null, 0, 0, false),
        Pureed("pureed", 1, 500, false),
        Candied("candied", 2, 250, false),
        Sauteed("sauteed", 3, 250, false),
        StirFried("stir-fried", 4, 250, true),
        Broiled("broiled", 5, 250, false),
        Szechuan("szechuan", 6, 125, false),
        DeepFried("deep-fried", 7, 125, true),
        Dried("dried", 8, 125, false),
        Homemade("homemade", 9, 125, false),
        Boiled("boiled", 10, 125, false),
        Smoked("smoked", 11, 125, false),
        Pickled("pickled", 12, 125, false),
        FrenchFried("french fried", 13, 125, true),
        Rotten(null, 14, -125, false), // handled better by info
        SpinachCursed(null, 15, 250, false),
        SpinachUncursed(null, 16, 500, false),
        SpinachBlessed("fresh", 17, 750, false);

        public final String name;
        public final int value;
        public final int nutrition;
        public final boolean greasy;

        Variety(String name, int value, int nutrition, boolean greasy) {
            this.name = name;
            this.value = value;
            this.nutrition = nutrition;
            this.greasy = greasy;
        }

        public static Variety fromInt(int value) {
            switch (value) {
                case 1:
                    return Pureed;
                case 2:
                    return Candied;
                case 3:
                    return Sauteed;
                case 4:
                    return StirFried;
                case 5:
                    return Broiled;
                case 6:
                    return Szechuan;
                case 7:
                    return DeepFried;
                case 8:
                    return Dried;
                case 9:
                    return Homemade;
                case 10:
                    return Boiled;
                case 11:
                    return Smoked;
                case 12:
                    return Pickled;
                case 13:
                    return FrenchFried;
                case 14:
                    return Rotten;
                case 15:
                    return SpinachCursed;
                case 16:
                    return SpinachUncursed;
                case 17:
                    return SpinachBlessed;
                default:
                    return None;
            }
        }

    }

    private static HashSet<String> knownCans = new HashSet<String>();

    private static final String KNOWNCANS = "knownCans";

    public static void save(Bundle bundle) {
        bundle.put(KNOWNCANS, knownCans.toArray(new String[0]));
    }

    @SuppressWarnings("unchecked")
    public static void restore(Bundle bundle) {
        String[] known = bundle.getStringArray(KNOWNCANS);

        if (known != null) {
            Collections.addAll(knownCans, known);
        }
    }


    @Override
    public boolean isIdentified() {
        return knownCans.contains(name) && bucStatusKnown();
    }

    @Override
    public Item identify(boolean updateQuickslot) {
        if (!isIdentified()) {
            knownCans.add(name);
            bucStatus(true);

            updateQuickslot = true;
        }

        if (parent() != null && stackable) {
            if (parent().tryMergeExistingStack(this)) {
                updateQuickslot = true;
            }
        }

        if (updateQuickslot) {
            updateQuickslot();
        }

        return this;
    }


    public CannedFood() {
        this(null, false);
    }

    public CannedFood(Corpse source, boolean playerCanned) {
        super(0, 125, Encumbrance.UNIT * 10);

        hasBuc(true);

        if (source != null) {
            setCorpse(source);
            if (playerCanned) {
                knownCans.add(name);
                effects |= CorpseEffect.PlayerCanned.value;
            }
        } else {
            name = "unknown";
            effects = CorpseEffect.None.value;
            resistances = MagicType.None.value;
        }

        stackable = true;
        image = ItemSpriteSheet.FOOD_CAN_UNOPENED;
        material = Material.Metal;

        defaultAction = null;

        price = 5;
    }

    private void setCorpse(Corpse source) {
        name = source.corpseName();
        effects = (source.effects & ~(CorpseEffect.Poisonous.value | CorpseEffect.Acidic.value | CorpseEffect.Rotten.value)) | CorpseEffect.Canned.value;
        resistances = source.resistances;
        corpseLevel = source.corpseLevel;
        bucStatus(source);
    }

    @Override
    public Item random() {
        Corpse corpse = null;

        if (Random.Int(6) != 0) {
            int tries = 4;
            while (tries > 0 && corpse == null) {
                Mob mob = Bestiary.spawn(Dungeon.depthAdjusted, Dungeon.hero.level + 10);

                if (mob.nutrition > 0 && ((mob.characteristics & Characteristic.Corpseless.value) == 0)) {
                    corpse = new Corpse(mob);
                } else {
                    tries--;
                }
            }
        }

        if (corpse == null) {
            corpse = new Corpse(new Spinach());
        }

        setCorpse(corpse);
        bucStatus(false);

        super.random();

        return this;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(Constant.Action.EAT);
        actions.add(Constant.Action.OPEN);
        return actions;
    }

    @Override
    public boolean execute(final Hero hero, String action) {
        if (action.equals(Constant.Action.OPEN)) {
            EquippableItem weapon = hero.belongings.weapon();

            long effort;

            if (weapon instanceof Dagger) {
                Dagger dagger = (Dagger) weapon;

                effort = GameTime.TICK * 3;
                GLog.p("You pry open the can with your " + dagger.getName() + ".");
            } else if (hero.belongings.contains(CanOpener.class, false)) {
                effort = GameTime.TICK;
                GLog.p("You open the can with your can opener.");
            } else {
                switch (bucStatus()) {
                    case Blessed:
                        effort = GameTime.TICK;
                        break;
                    case Cursed:
                        effort = GameTime.TICK * Random.IntRange(10, 10 + 250 / hero.STRCON()) * 2;
                        break;
                    default:
                        effort = GameTime.TICK * Random.IntRange(10, 10 + 250 / hero.STRCON());
                        break;
                }
                GLog.w("You wrestle the can open with your bare hands.");
            }

            hero.curAction = new HeroAction.UseItem.SlowAction(this, Constant.Action.SLOW_ACTION, effort, HeroAction.Display.Operate);
            hero.motivate(true);
            return true;
        } else if (action.equals(Constant.Action.SLOW_ACTION)) {
            IBag parent = parent();
            Item removed = parent.remove(this, 1);
            CannedFood split = (CannedFood) removed;

            Variety variation;

            if ((split.effects & CorpseEffect.Spinach.value) != 0) {
                switch (split.bucStatus()) {
                    case Cursed:
                        variation = Variety.SpinachCursed;
                        break;
                    case Blessed:
                        variation = Variety.SpinachBlessed;
                        break;
                    default:
                        variation = Variety.SpinachUncursed;
                        break;
                }
            } else if ((split.effects & CorpseEffect.PlayerCanned.value) != 0) {
                switch (split.bucStatus()) {
                    case Cursed:
                        variation = Variety.Rotten;
                        break;
                    case Blessed:
                        variation = Variety.Homemade;
                        break;
                    default:
                        if (Random.Int(7) == 0) {
                            variation = Variety.Rotten;
                        } else {
                            variation = Variety.Homemade;
                        }
                }
            } else {
                variation = Variety.None;

                switch (split.bucStatus()) {
                    case Cursed:
                        variation = Variety.Rotten;
                        break;
                    case Blessed:
                        //nothing
                        break;
                    case Uncursed:
                        if (Random.Int(14) == 0) {
                            variation = Variety.Rotten;
                        }
                        break;
                }

                if (variation == Variety.None) {
                    switch (Random.Int(15)) {
                        case 1:
                            variation = Variety.Pureed;
                            break;
                        case 2:
                            variation = Variety.Candied;
                            break;
                        case 3:
                            variation = Variety.Sauteed;
                            break;
                        case 4:
                            variation = Variety.StirFried;
                            break;
                        case 5:
                            variation = Variety.Broiled;
                            break;
                        case 6:
                            variation = Variety.Szechuan;
                            break;
                        case 7:
                            variation = Variety.DeepFried;
                            break;
                        case 8:
                            variation = Variety.Dried;
                            break;
                        case 10:
                            variation = Variety.Boiled;
                            break;
                        case 11:
                            variation = Variety.Smoked;
                            break;
                        case 12:
                            variation = Variety.Pickled;
                            break;
                        case 13:
                            variation = Variety.FrenchFried;
                            break;
                        default:
                            variation = Variety.Homemade;
                            break;
                    }
                }
            }

            if (variation == Variety.Rotten && (split.effects & CorpseEffect.Unrottable.value) != 0) {
                variation = Variety.Homemade;
            }

            Corpse opened = new Corpse(split, variation);
            hero.belongings.collect(opened);
            return false;
            /*
            HeroAction.UseItem.EatItem subaction = new HeroAction.UseItem.EatItem(null, Constant.Action.EAT, false);
            subaction.targetOutsideInventory = parent();

            CannedFood split = (CannedFood) parent().remove(this, 1);
            split = new CannedFood(split, Random.Int(NUTRITION_TYPES.length));
            subaction.target = split;

            hero.curAction = subaction;
            return true;
            */
        } else {
            return super.execute(hero, action);
        }
    }

    @Override
    protected boolean checkSimilarity(Item item) {
        if (super.checkSimilarity(item)) {
            CannedFood can = (CannedFood) item;

            if (can.effects == effects && can.resistances == resistances && can.corpseLevel == corpseLevel && can.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    protected long effects;
    protected long resistances;
    protected int corpseLevel;

    private static final String CORPSENAME = "corpseName";
    private static final String EFFECTS = "effects";
    private static final String RESISTANCES = "resistances";
    private static final String CORPSELEVEL = "corpseLevel";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(CORPSENAME, name);
        bundle.put(EFFECTS, effects);
        bundle.put(RESISTANCES, resistances);
        bundle.put(CORPSELEVEL, corpseLevel);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        name = bundle.getString(CORPSENAME);
        effects = bundle.getLong(EFFECTS);
        resistances = bundle.getLong(RESISTANCES);
        corpseLevel = bundle.getInt(CORPSELEVEL);
    }

    public String corpseName() {
        return name;
    }

    @Override
    public String getName(boolean partiallyEaten) {
        if (knownCans.contains(name)) {
            if ((effects & CorpseEffect.Vegetable.value) != 0) {
                return "can of " + name;
            } else {
                return "can of " + name + " meat";
            }
        } else {
            return "can";
        }
    }

    @Override
    public String info() {
        return "A sealed rigid metal container used to preserve all sorts of foods.\n" +
                "\n" +
                (knownCans.contains(name) ?
                        "This can contains a preparation of " + name + ((effects & CorpseEffect.Vegetable.value) != 0 ? "" : " meat") + "." :
                        "There's no way to know what's in this can before opening it.") +
                super.info();
    }
}
