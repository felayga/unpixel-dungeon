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

import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.hero.Sick;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/16/2017.
 */
public class TripeRation extends Food {
    public TripeRation() {
        super(200, 200/2, Encumbrance.UNIT * 10);

        stackable = true;
        name = "tripe ration";
        image = ItemSpriteSheet.FOOD_TRIPE;
        material = Material.Flesh;

        bones = true;

        price = 15;
    }

    @Override
    protected void doneEating(Hero hero, boolean stuffed) {
        int experience = 1;

        hero.sprite.showStatus(CharSprite.POSITIVE, Mob.TXT_EXP, experience);
        hero.earnExp(experience);
        if (Random.Int(2) == 0) {
            Buff.prolong(hero, null, Sick.class, 7 + Random.Int(8));
        }
    }

    @Override
    public String info() {
        return "Lower-quality cuts of meat usually either thrown out or fed to carnivorous animals." + super.info();
    }

    public String message() {
        //todo: alternative tripe messages
        return "Yuck, " + (Random.Int(2) == 0 ? "dog" : "cat") + " food.";
    }
}

