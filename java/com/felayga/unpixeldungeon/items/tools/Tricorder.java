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

package com.felayga.unpixeldungeon.items.tools;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.hero.IntrinsicAwareness;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.food.CannedFood;
import com.felayga.unpixeldungeon.items.food.Corpse;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndInfoBuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HELLO on 3/5/2017.
 */

public class Tricorder extends Tool {
    public Tricorder() {
        super(true, false);

        name = "tricorder";
        image = ItemSpriteSheet.TOOL_TRICORDER;
        material = Material.Plastic;

        hasLevels(false);
        hasBuc(false);

        defaultAction = Constant.Action.APPLY;

        price = 200;
        weight(2 * Encumbrance.UNIT);
    }

    public String getToolClass() {
        return "tricorder";
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Constant.Action.APPLY);
        return actions;
    }

    @Override
    public void apply(Hero hero, int pos) {
        GLog.d("apply pos");
        Char c = Dungeon.level.findMob(pos);

        if (c != null) {
            scan(c);
        } else {
            Heap heap = Dungeon.level.heaps.get(pos);

            if (heap != null && heap.size() > 0) {
                HashMap<Material, IntegerWrapper> materials = new HashMap<>();

                Iterator<Item> iterator = heap.iterator(true);
                while (iterator.hasNext()) {
                    Item bagItem = iterator.next();

                    Material material = bagItem.material();

                    IntegerWrapper wrapper = materials.get(material);
                    if (wrapper == null) {
                        wrapper = new IntegerWrapper(0);
                        materials.put(material, wrapper);
                    }
                    wrapper.value += bagItem.weight() * bagItem.quantity();
                }

                String message = "The tricorder scans the area.\n";

                if (heap.peek() instanceof Corpse) {
                    message += "\nIt's dead, Jim.\n";
                }

                Iterator<Map.Entry<Material, IntegerWrapper>> subIterator = materials.entrySet().iterator();
                while (subIterator.hasNext()) {
                    Map.Entry<Material, IntegerWrapper> pair = subIterator.next();

                    message += "\n" + ((float) pair.getValue().value / (float) Encumbrance.UNIT) + " unit(s) of " + pair.getKey().name;
                }

                Buff buff = new TricorderBuff(message);
                GameScene.show(new WndInfoBuff(buff));
                return;
            }

            GLog.n("There's nothing there to scan.");
        }
    }

    @Override
    public void apply(Hero hero, Item item){
        GLog.d("apply item");
        //nothing
    }

    @Override
    public void apply(Hero hero) {
        GLog.d("apply hero");
        scan(hero);
    }

    private void scan(Char target) {
        Buff buff = Buff.prolong(target, curUser, IntrinsicAwareness.class, 16 * GameTime.TICK);
        GameScene.show(new WndInfoBuff(buff));
    }

    @Override
    public String desc() {
        return "This futuristic device is capable of probing for detailed information about a creature.";
    }

    private class IntegerWrapper {
        public int value;

        public IntegerWrapper() {
            this(0);
        }

        public IntegerWrapper(int value) {
            this.value = value;
        }
    }

    private class TricorderBuff extends IntrinsicAwareness {
        @Override
        public String toString() {
            return "Tricorder Scan";
        }

        private String info;

        public TricorderBuff(String info) {
            this.info = info;
        }

        @Override
        public String desc() {
            if (info != null) {
                return info;
            }

            return super.desc();
        }
    }
}

