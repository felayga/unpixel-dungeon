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
package com.felayga.unpixeldungeon.plants;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.positive.Barkskin;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroSubClass;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.LeafParticle;
import com.felayga.unpixeldungeon.items.IFlammable;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.potions.IAlchemyComponent;
import com.felayga.unpixeldungeon.items.potions.PotionOfBrewing;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.PlantSprite;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class Plant implements Bundlable {
    public static final int EFFECTDURATION = 6;

    public final String plantName;
    public final int image;

    private int ownerRegistryIndex;
    public int ownerRegistryIndex() {
        return ownerRegistryIndex;
    }

    public Plant(String name, int image) {
        this.plantName = name;
        this.image = image;
    }

    public int pos;

    public PlantSprite sprite;

    public void trigger() {

        Char ch = Actor.findChar(pos);

        if (ch == Dungeon.hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
            Buff.affect(ch, null, Barkskin.class).level(ch.HT / 3);
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

        /*
        if (Dungeon.hero.subClass == HeroSubClass.WARDEN) {

            int naturalismLevel = 0;
            SandalsOfNature.Naturalism naturalism = Dungeon.hero.buff(SandalsOfNature.Naturalism.class);
            if (naturalism != null) {
                naturalismLevel = naturalism.level() + 1;
            }

            if (Random.Int(5 - (naturalismLevel / 2)) == 0) {
                Item seed = Generator.random(Generator.Category.SEED);

                Dungeon.level.drop(seed, pos).sprite.drop();
            }
            if (Random.Int(5 - naturalismLevel) == 0) {
                Dungeon.level.drop(new Dewdrop(), pos).sprite.drop();
            }
        }
        */
    }

    private static final String POS = "pos";
    private static final String OWNERREGISTRYINDEX = "ownerRegistryIndex";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        pos = bundle.getInt(POS);
        ownerRegistryIndex = bundle.getInt(OWNERREGISTRYINDEX);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(POS, pos);
        bundle.put(OWNERREGISTRYINDEX, ownerRegistryIndex);
    }

    public String desc() {
        return null;
    }

    public static class Seed extends Item implements IFlammable {

        private static final String TXT_INFO = "Throw this seed to the place where you want to grow %s.\n\n%s";

        private static final long TIME_TO_PLANT = GameTime.TICK;

        public Seed() {
            stackable = true;
            defaultAction = Constant.Action.THROW;
            pickupSound = Assets.SND_ITEM_PLANT;
            material = Material.Vegetable;

            hasBuc(false);
            hasLevels(false);

            price = 20;
            weight(Encumbrance.UNIT / 16);
        }

        @Override
        public boolean burn(Char cause) {
            parent().remove(this, 1);
            return false;
        }

        public Item getSelf() {
            return this;
        }

        protected Class<? extends Plant> plantClass;
        protected String plantName;

        @Override
        public ArrayList<String> actions(Hero hero) {
            ArrayList<String> actions = super.actions(hero);
            actions.add(Constant.Action.PLANT);
            actions.add(Constant.Action.BREW);
            return actions;
        }

        @Override
        protected void onThrow(Char thrower, int cell) {
            if (Dungeon.level.map(cell) == Terrain.ALCHEMY || Level.chasm[cell]) {
                super.onThrow(thrower, cell);
            } else {
                Dungeon.level.plant(thrower, this, cell);
            }
        }

        @Override
        public boolean execute(Hero hero, String action) {
            if (action.equals(Constant.Action.PLANT)) {
                hero.spend_new(TIME_TO_PLANT, false);
                hero.busy();
                ((Seed) hero.belongings.remove(this, 1)).onThrow(hero, hero.pos());

                hero.sprite.operate(hero.pos());

                return false;
            } else if (action.equals(Constant.Action.BREW)) {
                PotionOfBrewing.Handler.brew(hero, (IAlchemyComponent) this);
                return false;
            } else {
                return super.execute(hero, action);
            }
        }

        public Plant couch(int pos) {
            try {
                if (Dungeon.audible[pos]) {
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
        public boolean isIdentified() {
            return true;
        }

        @Override
        public String info() {
            return String.format(TXT_INFO, Utils.indefinite(plantName), desc());
        }
    }
}
