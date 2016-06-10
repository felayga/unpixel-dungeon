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
package com.felayga.unpixeldungeon.plants;

import java.util.ArrayList;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.items.artifacts.SandalsOfNature;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.audio.Sample;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.positive.Barkskin;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroSubClass;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.LeafParticle;
import com.felayga.unpixeldungeon.items.Dewdrop;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.sprites.PlantSprite;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Plant implements Bundlable {

    public String plantName;

    public int image;
    public int pos;

    public PlantSprite sprite;

    public void trigger() {

        Char ch = Actor.findChar(pos);

        if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
            Buff.affect(ch, Barkskin.class).level(ch.HT / 3);
        }

        wither();
        activate();
    }

    public abstract void activate();

    public void wither() {
        Dungeon.level.uproot(pos);

        sprite.kill();
        if (Dungeon.visible[pos]) {
            CellEmitter.get(pos).burst(LeafParticle.GENERAL, 6);
        }

        if (Dungeon.hero.subClass == HeroSubClass.WARDEN) {

            int naturalismLevel = 0;
            SandalsOfNature.Naturalism naturalism = Dungeon.hero.buff(SandalsOfNature.Naturalism.class);
            if (naturalism != null) {
                naturalismLevel = naturalism.level() + 1;
            }

            if (Random.Int(5 - (naturalismLevel / 2)) == 0) {
                Item seed = Generator.random(Generator.Category.SEED);

                if (seed instanceof BlandfruitBush.Seed) {
                    if (Random.Int(15) - Dungeon.limitedDrops.blandfruitSeed.count >= 0) {
                        Dungeon.level.drop(seed, pos).sprite.drop();
                        Dungeon.limitedDrops.blandfruitSeed.count++;
                    }
                } else
                    Dungeon.level.drop(seed, pos).sprite.drop();
            }
            if (Random.Int(5 - naturalismLevel) == 0) {
                Dungeon.level.drop(new Dewdrop(), pos).sprite.drop();
            }
        }
    }

    private static final String POS = "pos";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        pos = bundle.getInt(POS);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(POS, pos);
    }

    public String desc() {
        return null;
    }

    public static class Seed extends Item {

        public static final String AC_PLANT = "PLANT";

        private static final String TXT_INFO = "Throw this seed to the place where you want to grow %s.\n\n%s";

        private static final long TIME_TO_PLANT = GameTime.TICK;

        {
            stackable = true;
            defaultAction = AC_THROW;
        }

        protected Class<? extends Plant> plantClass;
        protected String plantName;

        public Class<? extends Item> alchemyClass;

        @Override
        public ArrayList<String> actions(Hero hero) {
            ArrayList<String> actions = super.actions(hero);
            actions.add(AC_PLANT);
            return actions;
        }

        @Override
        protected void onThrow(int cell, Char thrower) {
            if (Dungeon.level.map[cell] == Terrain.ALCHEMY || Level.pit[cell]) {
                super.onThrow(cell, thrower);
            } else {
                Dungeon.level.plant(this, cell);
            }
        }

        @Override
        public boolean execute(Hero hero, String action) {
            if (action.equals(AC_PLANT)) {
                hero.spend(TIME_TO_PLANT, false);
                hero.busy();
                ((Seed) hero.belongings.remove(this, 1)).onThrow(hero.pos, hero);

                hero.sprite.operate(hero.pos);

                return false;
            } else {
                return super.execute(hero, action);
            }
        }

        public Plant couch(int pos) {
            try {
                if (Dungeon.visible[pos]) {
                    Sample.INSTANCE.play(Assets.SND_PLANT);
                }
                Plant plant = plantClass.newInstance();
                plant.pos = pos;
                return plant;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }

        @Override
        public String info() {
            return String.format(TXT_INFO, Utils.indefinite(plantName), desc());
        }
    }
}
