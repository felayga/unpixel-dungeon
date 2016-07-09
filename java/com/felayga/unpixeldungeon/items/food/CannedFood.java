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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.DeathlySick;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Dagger;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

import javax.crypto.EncryptedPrivateKeyInfo;

/**
 * Created by HELLO on 7/8/2016.
 */
public class CannedFood extends Food {
    public CannedFood() { this(null); }

    public CannedFood(Corpse source) {
        super(0, Encumbrance.UNIT * 10);

        if (source != null) {
            name = source.corpseName();
            effects = source.effects & ~(CorpseEffect.Poisonous.value | CorpseEffect.Acidic.value);
            resistances = source.resistances;
            corpseLevel = source.corpseLevel;
        }
        else {
            name = "unknown";
            effects = CorpseEffect.None.value;
            resistances = MagicType.None.value;
        }
        opened = false;

        stackable = false;

        hasBuc(true);
        price = 5;

        determineImage();
    }

    private int nutritionType;

    public CannedFood(CannedFood source, int nutritionType) {
        super(Food.AMOUNT_EATEN_PER_ROUND, Encumbrance.UNIT * 10);

        name = source.name;
        effects = source.effects;
        resistances = source.resistances;
        corpseLevel = source.corpseLevel;
        opened = true;
        this.nutritionType = nutritionType;

        stackable = false;

        bucStatus(source);
        price = 5;

        determineImage();
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (!opened) {
            actions.remove(Constant.Action.EAT);
            actions.add(Constant.Action.OPEN);
        }
        return actions;
    }

    @Override
    public boolean execute(final Hero hero, String action) {
        if (action.equals(Constant.Action.OPEN)) {
            hero.sprite.operate(hero.pos);

            EquippableItem weapon = hero.belongings.weapon();

            long effort;

            //todo: can opener
            if (weapon instanceof Dagger) {
                Dagger dagger = (Dagger) weapon;

                effort = GameTime.TICK * 3;
                GLog.p("You pry open the can with your " + dagger.getDisplayName() + ".");
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
            }

            hero.curAction = new HeroAction.UseItem.SlowAction(this, Constant.Action.SLOWACTION, effort);
            hero.motivate(true);
            return true;
        } else if (action.equals(Constant.Action.SLOWACTION)) {
            HeroAction.UseItem.EatItem subaction = new HeroAction.UseItem.EatItem(null, Constant.Action.EAT, false);
            subaction.targetOutsideInventory = parent();

            CannedFood split = (CannedFood) parent().remove(this, 1);
            split = new CannedFood(split, Random.Int(NUTRITION_TYPES.length));
            subaction.target = split;

            hero.curAction = subaction;
            return true;
        } else {
            return super.execute(hero, action);
        }
    }

    private void determineImage() {
        int oldImage = image;

        if (opened) {
            if ((effects & CorpseEffect.Vegetable.value) != 0) {
                image = ItemSpriteSheet.FOOD_CAN_VEGETABLE;
            } else {
                image = ItemSpriteSheet.FOOD_CAN_MEAT;
            }
        } else {
            image = ItemSpriteSheet.FOOD_CAN_UNOPENED;
        }

        if (image != oldImage) {
            updateQuickslot();
        }
    }

    @Override
    protected boolean checkSimilarity(Item item) {

        return false;
    }

    protected long effects;
    protected long resistances;
    protected int corpseLevel;
    protected boolean opened;

    private static final String CORPSENAME = "corpseName";
    private static final String EFFECTS = "effects";
    private static final String RESISTANCES = "resistances";
    private static final String CORPSELEVEL = "corpseLevel";
    private static final String OPENED = "opened";
    private static final String NUTRITIONTYPE = "nutritionType";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(CORPSENAME, name);
        bundle.put(EFFECTS, effects);
        bundle.put(RESISTANCES, resistances);
        bundle.put(CORPSELEVEL, corpseLevel);
        bundle.put(OPENED, opened);
        bundle.put(NUTRITIONTYPE, nutritionType);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        name = bundle.getString(CORPSENAME);
        effects = bundle.getLong(EFFECTS);
        resistances = bundle.getLong(RESISTANCES);
        corpseLevel = bundle.getInt(CORPSELEVEL);
        opened = bundle.getBoolean(OPENED);
        nutritionType = bundle.getInt(NUTRITIONTYPE);

        determineImage();
    }

    @Override
    public void doneEating(Hero hero, boolean stuffed) {
        super.doneEating(hero, stuffed);

        Corpse.applyEffectsResistances(hero, effects, resistances, corpseLevel);
    }

    @Override
    public String getName() {
        if ((effects & CorpseEffect.Vegetable.value) != 0) {
            return "can of " + name;
        } else {
            return "can of " + name + " meat";
        }
    }

    @Override
    public String message() {
        return "Yum, " + String.format(NUTRITION_NAMES[nutritionType], name) + ".";
    }
}
