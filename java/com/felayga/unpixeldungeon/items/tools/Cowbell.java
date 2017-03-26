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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

/**
 * Created by HELLO on 3/25/2017.
 */

public class Cowbell extends Tool {
    public Cowbell() {
        super(false, false);

        name = "cowbell";
        image = ItemSpriteSheet.TOOL_COWBELL;
        material = Material.Copper;

        hasLevels(false);

        defaultAction = Constant.Action.APPLY;

        price = 50;
        weight(30 * Encumbrance.UNIT);
    }

    @Override
    public Item random() {
        super.random();

        bucStatus(BUCStatus.Uncursed);

        return this;
    }

    public String getToolClass() {
        return "cowbell";
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(Constant.Action.APPLY);
        return actions;
    }

    @Override
    public void apply(Hero hero, int pos) {
        //nothing
    }

    @Override
    public void apply(Hero hero, Item item){
        //nothing
    }

    @Override
    public void apply(Hero hero) {
        Sample.INSTANCE.play(Assets.SND_ITEM_COWBELL);
        hero.sprite.operate(hero.pos());

        for (Mob mob : Dungeon.level.mobs) {
            if (Dungeon.audible[mob.pos()]) {
                mob.beckon(hero.pos());
            }
        }
    }

    @Override
    public String desc() {
        return "A bell worn by free-roaming livestock to make them easier to locate.";
    }

}

