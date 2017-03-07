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

package com.felayga.unpixeldungeon.items.potions;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.food.Blandfruit;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.plants.BlandfruitBush;
import com.felayga.unpixeldungeon.plants.Blindweed;
import com.felayga.unpixeldungeon.plants.Bloodleaf;
import com.felayga.unpixeldungeon.plants.Deathroot;
import com.felayga.unpixeldungeon.plants.Dreamfoil;
import com.felayga.unpixeldungeon.plants.Earthroot;
import com.felayga.unpixeldungeon.plants.Fadeleaf;
import com.felayga.unpixeldungeon.plants.Firebloom;
import com.felayga.unpixeldungeon.plants.Icecap;
import com.felayga.unpixeldungeon.plants.Moongrass;
import com.felayga.unpixeldungeon.plants.Sorrowmoss;
import com.felayga.unpixeldungeon.plants.Starflower;
import com.felayga.unpixeldungeon.plants.Stoneberry;
import com.felayga.unpixeldungeon.plants.Stormvine;
import com.felayga.unpixeldungeon.plants.Sungrass;
import com.felayga.unpixeldungeon.plants.Swampweed;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HELLO on 2/4/2017.
 */
public abstract class PotionOfBrewing extends PotionOfWater {

    protected String plantName;

    public PotionOfBrewing() {
        name = "Potion of Brewing";
        initials = "BRW";

        bones = true;
        color = "clear";

        price = 30;

        material = Material.Glass;

        setKnown();
    }

    @Override
    public void syncRandomizedProperties() {
        //nope
    }

    @Override
    public String desc() {
        return "This is a flask of water with a " + plantName + " in it.\nSeems pretty mundane.";
    }


    private static final Class<?>[] potionsAlchemy = {
            Deathroot.Seed.class, Stormvine.Seed.class,
            Blindweed.Seed.class, Sorrowmoss.Seed.class,
            Stormvine.Seed.class, Sorrowmoss.Seed.class,
            Moongrass.Seed.class, Fadeleaf.Seed.class,
            Icecap.Seed.class, Stoneberry.Seed.class,
            Moongrass.Seed.class, Swampweed.Seed.class,
            Starflower.Seed.class, Sungrass.Seed.class,
            Starflower.Seed.class, Stoneberry.Seed.class,
            Bloodleaf.Seed.class, Blindweed.Seed.class,
            Bloodleaf.Seed.class, Swampweed.Seed.class,
            Swampweed.Seed.class, Blindweed.Seed.class,
            Sungrass.Seed.class, Fadeleaf.Seed.class,
            Fadeleaf.Seed.class, Starflower.Seed.class,
            Deathroot.Seed.class, Bloodleaf.Seed.class,
            Stormvine.Seed.class, Moongrass.Seed.class,
            Stoneberry.Seed.class, Dreamfoil.Seed.class,
            Stoneberry.Seed.class, Earthroot.Seed.class,
            Firebloom.Seed.class, Icecap.Seed.class,
            Fadeleaf.Seed.class, Dreamfoil.Seed.class,
            Dreamfoil.Seed.class, Bloodleaf.Seed.class,
            Icecap.Seed.class, Stormvine.Seed.class,
            Sungrass.Seed.class, Moongrass.Seed.class,
            Firebloom.Seed.class, Sungrass.Seed.class,
            Blindweed.Seed.class, Firebloom.Seed.class,
            Dreamfoil.Seed.class, Earthroot.Seed.class,
            Swampweed.Seed.class, Starflower.Seed.class,
            Sorrowmoss.Seed.class, Deathroot.Seed.class,
            Earthroot.Seed.class, Icecap.Seed.class,
            Earthroot.Seed.class, Firebloom.Seed.class,
            Stoneberry.Seed.class, Swampweed.Seed.class
    };
    private static final Class<?>[] basicAlchemy = {
            BlandfruitBush.Brew.class, Blandfruit.class,
            Blindweed.Brew.class, Blindweed.Seed.class,
            Bloodleaf.Brew.class, Bloodleaf.Seed.class,
            Deathroot.Brew.class, Deathroot.Seed.class,
            Dreamfoil.Brew.class, Dreamfoil.Seed.class,
            Earthroot.Brew.class, Earthroot.Seed.class,
            Fadeleaf.Brew.class, Fadeleaf.Seed.class,
            Firebloom.Brew.class, Firebloom.Seed.class,
            Icecap.Brew.class, Icecap.Seed.class,
            Moongrass.Brew.class, Moongrass.Seed.class,
            Sorrowmoss.Brew.class, Sorrowmoss.Seed.class,
            Starflower.Brew.class, Starflower.Seed.class,
            Stoneberry.Brew.class, Stoneberry.Seed.class,
            Stormvine.Brew.class, Stormvine.Seed.class,
            Sungrass.Brew.class, Sungrass.Seed.class,
            Swampweed.Brew.class, Swampweed.Seed.class
    };

    public static class BrewingHandler {
        private class BrewingRecipe {
            public final Class<? extends IAlchemyComponent> seed1;
            public final Class<? extends IAlchemyComponent> seed2;

            public final Class<?> result;

            public BrewingRecipe(Class<? extends IAlchemyComponent> seed, Class<? extends Potion> result) {
                this(seed, seed, result);
            }

            public BrewingRecipe(Class<? extends IAlchemyComponent> seed1, Class<? extends IAlchemyComponent> seed2, Class<? extends Potion> result) {
                this.seed1 = seed1;
                this.seed2 = seed2;
                this.result = result;
            }
        }

        Map<Class<?>, BrewingRecipe> fromPotion;
        Map<Class<?>, Map<Class<?>, BrewingRecipe>> fromSeeds;
        Map<Class<?>, Integer> componentIndex;

        public BrewingHandler() {
            fromPotion = new HashMap<>();
            fromSeeds = new HashMap<>();
            componentIndex = new HashMap<>();

            for (int n = 0; n < potionsAlchemy.length / 2; n++) {
                BrewingRecipe recipe = new BrewingRecipe(
                        (Class<? extends IAlchemyComponent>)potionsAlchemy[n * 2 + 0],
                        (Class<? extends IAlchemyComponent>)potionsAlchemy[n * 2 + 1],
                        (Class<? extends Potion>)potions[n]);

                fromPotion.put(recipe.result, recipe);


                Map<Class<?>, BrewingRecipe> fromSeed = fromSeeds.get(recipe.seed1);

                if (fromSeed == null) {
                    fromSeed = new HashMap<>();
                    fromSeeds.put(recipe.seed1, fromSeed);
                }

                fromSeed.put(recipe.seed2, recipe);


                fromSeed = fromSeeds.get(recipe.seed2);

                if (fromSeed == null) {
                    fromSeed = new HashMap<>();
                    fromSeeds.put(recipe.seed2, fromSeed);
                }

                fromSeed.put(recipe.seed1, recipe);
            }

            for (int n = 0; n < basicAlchemy.length; n += 2) {
                BrewingRecipe recipe = new BrewingRecipe(
                        (Class<? extends IAlchemyComponent>)basicAlchemy[n + 1],
                        (Class<? extends Potion>)basicAlchemy[n + 0]);

                fromPotion.put(recipe.result, recipe);

                Map<Class<?>, BrewingRecipe> fromSeed = fromSeeds.get(recipe.seed1);

                if (fromSeed == null) {
                    fromSeed = new HashMap<>();
                    fromSeeds.put(recipe.seed1, fromSeed);
                }

                fromSeed.put(recipe.seed2, recipe);

                componentIndex.put(recipe.seed1, n / 2);
            }
        }

        private static Hero curUser;
        private static IAlchemyComponent curComponent;

        public void brew(Hero hero, IAlchemyComponent component) {
            curUser = hero;
            curComponent = component;
            GameScene.selectItem(itemBrewer, WndBackpack.Mode.INSTANCEOF, Potion.class, "Brew " + component.getName(), true, null, (Item) component);
        }

        protected static WndBackpack.Listener itemBrewer = new WndBackpack.Listener() {
            @Override
            public void onSelect(Item item) {
                if (item != null) {
                    handle(curUser, curUser.belongings.backpack1, curComponent, (Potion) item);
                }
            }
        };

        public static Potion handle(Heap container, IAlchemyComponent component1, IAlchemyComponent component2, boolean collect) {
            Potion result;

            BrewingRecipe recipe = Handler.fromSeeds.get(component1.getClass()).get(component2.getClass());

            if (recipe != null) {
                try {
                    result = (Potion) recipe.result.newInstance();
                } catch (Exception e) {
                    GLog.d("new instance fail");
                    result = null;
                }
            } else {
                result = handleFailure(component1.getClass(), component2.getClass());
            }

            container.remove(component1.getSelf(), 1);
            container.remove(component2.getSelf(), 1);

            if (result != null) {
                resultSfx(container.pos(), true);

                result.random();
                if (collect) {
                    container.collect(result);
                    GameScene.show(new WndBackpack(container, null, WndBackpack.Mode.ALL_WITH_SPELL, null, null, false, null));
                }
            } else {
                resultSfx(container.pos(), false);
            }

            return result;
        }

        public static void handle(Hero user, IBag container, IAlchemyComponent component, Potion potion) {
            Potion result;

            BrewingRecipe recipe = Handler.fromPotion.get(potion.getClass());
            if (recipe != null) {
                GLog.d("seed1=" + recipe.seed1.getName() + " seed2=" + recipe.seed2.getName());

                Class<? extends IAlchemyComponent> test;
                if (recipe.seed1 == component.getClass()) {
                    test = recipe.seed1;
                } else if (recipe.seed2 == component.getClass()) {
                    test = recipe.seed2;
                } else {
                    test = Random.Int(2) == 0 ? recipe.seed1 : recipe.seed2;
                }

                recipe = Handler.fromSeeds.get(test).get(component.getClass());

                if (recipe != null) {
                    try {
                        result = (Potion) recipe.result.newInstance();
                    } catch (Exception e) {
                        GLog.d("new instance fail");
                        result = null;
                    }
                } else {
                    result = handleFailure(test, component.getClass());
                }

            } else {
                GLog.d("recipe null?");

                recipe = Handler.fromSeeds.get(component.getClass()).get(component.getClass());
                try {
                    result = (Potion) recipe.result.newInstance();
                } catch (Exception e) {
                    GLog.d("simple instance fail?  " + component.getClass().getName());
                    GLog.d(e);
                    result = null;
                }
            }

            user.belongings.remove(component.getSelf(), 1);
            user.belongings.remove(potion, 1);

            if (result != null) {
                resultSfx(user.pos(), true);

                result.bucStatus(potion);
                container.collect(result);
                GameScene.show(new WndBackpack(container, null, WndBackpack.Mode.ALL_WITH_SPELL, null, null, false, null));
            } else {
                resultSfx(user.pos(), false);
            }
        }
    }

    private static Potion handleFailure(Class<? extends IAlchemyComponent> seed1, Class<? extends IAlchemyComponent> seed2) {
        Potion retval;

        int seedfail = Handler.componentIndex.get(seed1) + Handler.componentIndex.get(seed2);
        switch (seedfail % 6) {
            case 0:
            case 1:
            case 2:
                retval = new PotionOfFruitJuice();
                break;
            case 3:
            case 4:
                retval = new PotionOfBooze();
                break;
            default:
                retval = new PotionOfSickness();
                break;
        }

        return retval;
    }

    private static void resultSfx(int pos, boolean good) {
        if (good) {
            if (Dungeon.visible[pos]) {
                GLog.w("The flask bubbles briefly.");
            }
            if (Dungeon.audible[pos]) {
                Sample.INSTANCE.play(Assets.SND_POTION_BREW);
            }
        } else {
            if (Dungeon.visible[pos]) {
                GLog.w("The flask shatters!");
            } else {
                GLog.w("You feel the flask shatter!");
            }
            if (Dungeon.audible[pos]) {
                Sample.INSTANCE.play(Assets.SND_SHATTER);
            }
        }
    }

    public static final BrewingHandler Handler = new BrewingHandler();
}

