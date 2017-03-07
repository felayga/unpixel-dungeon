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
package com.felayga.unpixeldungeon.items.food;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Food extends Item {
    public static final int WELL_SUSTENANCE = 125;
    public static final int VOMIT_SUSTENANCE = -75;

    private int energy;
    private int amountEatenPerRound;

    private void subtractEnergy(int amount) {
        energy -= amount;
        weight((int) Math.ceil((float) energy / (float) energyOriginal * (float) weightOriginal));
    }

    public boolean energyLeft() {
        return energy > 0;
    }

    public boolean partiallyEaten = false;
    private int energyOriginal;
    private int weightOriginal;

    public static final String TXT_EATING_RESUMED = "You resume eating the %s.";
    public static final String TXT_EATING_DONE = "You finish eating the %s.";
    public static final String TXT_OVEREATING_DONE = "You're finally finished.";
    public static final String TXT_OVEREATING_DEAD = "You choke on your %s.";

    protected boolean checkSimilarity(Item item) {
        if (partiallyEaten) {
            return false;
        }

        return super.checkSimilarity(item);
    }

    public Food(int energy, int amountEatenPerRound, int weight) {
        this.energy = energy;
        this.amountEatenPerRound = amountEatenPerRound;
        this.energyOriginal = energy;
        this.weightOriginal = weight;

        pickupSound = Assets.SND_ITEM_FOOD;

        stackable = true;
        name = "food";
        image = ItemSpriteSheet.NULLWARN;
        hasLevels(false);

        weight(weight);
        price = 10;
    }

    @Override
    public boolean canPerformActionExternally(Hero hero, String action) {
        if (action.equals(Constant.Action.EAT)) {
            return true;
        }

        return super.canPerformActionExternally(hero, action);
    }


    private static final String PARTIALLYEATEN = "partiallyEaten";
    private static final String ENERGY = "energy";
    private static final String STACKABLE = "stackable";
    private static final String ENERGYORIGINAL = "energyOriginal";
    private static final String WEIGHTORIGINAL = "weightOriginal";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(PARTIALLYEATEN, partiallyEaten);
        bundle.put(ENERGY, energy);
        bundle.put(STACKABLE, stackable);
        bundle.put(ENERGYORIGINAL, energyOriginal);
        bundle.put(WEIGHTORIGINAL, weightOriginal);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        partiallyEaten = bundle.getBoolean(PARTIALLYEATEN);
        energy = bundle.getInt(ENERGY);
        stackable = bundle.getBoolean(STACKABLE);
        energyOriginal = bundle.getInt(ENERGYORIGINAL);
        weightOriginal = bundle.getInt(WEIGHTORIGINAL);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(Constant.Action.EAT);
        return actions;
    }

    @Override
    public boolean execute(final Hero hero, String action) {
        if (action.equals(Constant.Action.EAT_START)) {
            if (!partiallyEaten) {
                GLog.i(message());
            } else {
                GLog.i(TXT_EATING_RESUMED, name);
            }

            hero.sprite.operate(hero.pos());
            SpellSprite.show(hero, SpellSprite.FOOD);
            Sample.INSTANCE.play(Assets.SND_EAT);

            action = Constant.Action.EAT;
        }

        if (action.equals(Constant.Action.EAT)) {
            long time;
            int subEnergy;
            Hunger hunger = hero.buff(Hunger.class);

            if (energy > amountEatenPerRound) {
                subEnergy = amountEatenPerRound;
                time = GameTime.TICK;
            } else {
                subEnergy = energy;
                time = Math.max(GameTime.TICK * subEnergy / amountEatenPerRound, 1);
            }

            if (subEnergy > 125) {
                time = (time * 125 / subEnergy);
                subEnergy = 125;
            }

            if (!partiallyEaten) {
                startedEating(hero);
            } else {
                continuedEating(hero);
            }

            subtractEnergy(subEnergy);
            hunger.satisfy_new(subEnergy);

            if (hunger.isStuffed() && hunger.choke(hero)) {
                Dungeon.fail("Choked on a " + getName(false));
                GLog.n(TXT_OVEREATING_DEAD, name);
                hero.damage(hero.HT, MagicType.Mundane, null, null);
                hero.sprite.die();
                return false;
            }

            //GLog.d("foodtick energy="+energy+" subenergy="+subenergy+"time="+time);
            hero.spend_new(time, false);

            if (!energyLeft()) {
                doneEating(hero, hunger.isStuffed());

                return false;
            }

            return true;
			/*
			int subenergy = Math.min(AMOUNT_EATEN_PER_ROUND, energy);
			int newenergy = energy - subenergy;

			if (newenergy <= 0) {
				detach(hero.belongings.backpack);
				GLog.i(TXT_EATING_DONE, name);

				Statistics.foodEaten++;
				Badges.validateFoodEaten();
			}
			else if (!partiallyEaten) {
				partiallyEaten = true;
				detach(hero.belongings.backpack);

				stackable = false;
				name = "partially eaten " + name;

				collect(hero.belongings.backpack);
			}

			energy = newenergy;

			((Hunger)hero.buff( Hunger.class)).satisfy_new(subenergy);

			float time = (float)subenergy / (float)AMOUNT_EATEN_PER_ROUND;
			*/

			/*
			switch (hero.heroClass) {
			case WARRIOR:
				if (hero.HP < hero.HT) {
					hero.HP = Math.min( hero.HP + 5, hero.HT );
					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				}
				break;
			case MAGE:
				//1 charge
				Buff.affect( hero, ScrollOfRecharging.Recharging.class, 4f );
				ScrollOfRecharging.charge( hero );
				break;
			case ROGUE:
			case HUNTRESS:
				break;
			}
			*/
        } else {
            return super.execute(hero, action);
        }
    }

    protected void startedEating(Hero hero) {
        partiallyEaten = true;
    }

    protected void continuedEating(Hero hero) {
    }

    protected void doneEating(Hero hero, boolean stuffed) {
        //GLog.d("doneEating");
        if (stuffed) {
            GLog.i(TXT_OVEREATING_DONE);
        } else {
            partiallyEaten = false;
            GLog.i(TXT_EATING_DONE, getName());
        }

        Statistics.foodEaten++;
        Badges.validateFoodEaten();
    }

    public void stoppedEating(Hero hero) {
        GLog.p("You stop eating.");
    }

    @Override
    public final String getName() {
        return getName(partiallyEaten);
    }

    protected String getName(boolean partiallyEaten) {
        if (partiallyEaten) {
            return "partially eaten " + super.getName();
        } else {
            return super.getName();
        }
    }

    @Override
    public String info() {
        if (partiallyEaten) {
            float amount = (float) energy / (float) energyOriginal;

            if (amount >= 0.8) {
                return "\n\nA few bites have been taken out of it.";
            } else if (amount >= 0.6) {
                return "\n\nMore than half of it is left uneaten.";
            } else if (amount >= 0.4) {
                return "\n\nAbout half of it has been eaten.";
            } else if (amount >= 0.2) {
                return "\n\nLess than half of it remains.";
            } else {
                return "\n\nNot much of it is left.";
            }
        } else {
            return "";
        }
    }

    public String message() {
        return "Om nom nom.";
    }
}
