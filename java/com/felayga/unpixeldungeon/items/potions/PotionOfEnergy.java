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

/**
 * Created by HELLO on 11/15/2016.
 */

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class PotionOfEnergy extends Potion {

    public PotionOfEnergy()
    {
        name = "Potion of Energy";
        initials = "En";

        bones = true;
        isHelpful = true;

        applicationDescription = "somewhat";
        price = 150;
    }

    protected String applicationDescription;

    @Override
    public void apply( Char hero ) {
        setKnown();
        heal(hero);
        cure(hero);
        GLog.p("You feel " + applicationDescription + " restored.");
    }

    public void heal( Char hero ) {
        if (hero.MP != hero.MT) {
            heal(hero, healAmount(hero));
        }

        int bonus = Random.IntRange(7, 12);
        switch (bucStatus()) {
            case Blessed:
                bonus += 5;
                break;
            case Cursed:
                bonus = -bonus;
                GLog.n("You feel lackluster.");
                break;
            default:
                //nope
                break;
        }

        hero.MT += bonus;
        hero.MP += bonus;

        if (hero.MT < 1) {
            hero.MT = 1;
            hero.MP = Math.max(hero.MP, 0);
        }
    }

    protected void heal(Char hero, int healAmount) {
        switch(bucStatus()) {
            case Blessed:
                healAmount += healAmount / 2;
                break;
            case Cursed:
                healAmount /= 2;
        }

        hero.MP = Math.min(hero.MT, hero.MP + healAmount);
    }

    protected int healAmount(Char hero) {
        return (int)Math.ceil((double)hero.MT / 2.0);
    }

    public void cure( Char hero ) {
        hero.sprite.emitter().start(Speck.factory(Speck.MANAING), 0.4f, 4);
    }

    @Override
    public String desc() {
        return "An elixir that will instantly restore " + descAmount() + " of your energy.";
    }

    protected String descAmount() {
        return "some";
    }

}

