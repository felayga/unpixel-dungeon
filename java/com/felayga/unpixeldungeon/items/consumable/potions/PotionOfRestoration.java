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

package com.felayga.unpixeldungeon.items.consumable.potions;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Bleeding;
import com.felayga.unpixeldungeon.actors.buffs.negative.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.negative.Drowsy;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.actors.buffs.negative.Slow;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.actors.buffs.negative.Weakness;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.utils.GLog;

/**
 * Created by HELLO on 11/29/2016.
 */
public class PotionOfRestoration extends Potion {

    public PotionOfRestoration() {
        name = "Potion of Restoration";
        initials = "Re";

        bones = true;
        isHelpful = true;

        overhealAmount = 1;
        applicationDescription = "somewhat";
        price = 30;
    }

    protected int overhealAmount;
    protected String applicationDescription;

    @Override
    public void apply(Char hero) {
        setKnown();
        heal(hero);
        cure(hero);
        hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
        hero.sprite.emitter().start(Speck.factory(Speck.MANAING), 0.4f, 4);
        GLog.p("You are restored " + applicationDescription + ".");
    }

    public void heal(Char hero) {
        if (hero.HP == hero.HT) {
            int overhealAmount = this.overhealAmount;

            switch (bucStatus()) {
                case Blessed:
                    overhealAmount *= 2;
                    break;
                case Cursed:
                    overhealAmount = 0;
                    break;
            }

            if (overhealAmount > 0) {
                hero.HT += overhealAmount;
                hero.HP += overhealAmount;
                hero.sprite.showStatus(CharSprite.POSITIVE, "+" + overhealAmount + " ht");
            }
        } else {
            healHealth(hero, healHealthAmount(hero));
        }

        if (hero.MP == hero.MT) {
            int overhealAmount = this.overhealAmount;

            switch (bucStatus()) {
                case Blessed:
                    overhealAmount *= 2;
                    break;
                case Cursed:
                    overhealAmount = 0;
                    break;
            }

            if (overhealAmount > 0) {
                hero.MT += overhealAmount;
                hero.MP += overhealAmount;
                hero.sprite.showStatus(CharSprite.POSITIVE, "+" + overhealAmount + " mt");
            }
        } else {
            healMana(hero, healManaAmount(hero));
        }
    }

    protected void healHealth(Char hero, int healAmount) {
        switch (bucStatus()) {
            case Blessed:
                healAmount += healAmount / 2;
                break;
            case Cursed:
                healAmount /= 2;
        }

        hero.HP = Math.min(hero.HT, hero.HP + healAmount);
    }

    protected void healMana(Char hero, int healAmount) {
        switch (bucStatus()) {
            case Blessed:
                healAmount += healAmount / 2;
                break;
            case Cursed:
                healAmount /= 2;
        }

        hero.MT = Math.min(hero.MT, hero.MP + healAmount);
    }

    protected int healHealthAmount(Char hero) {
        return (int) Math.ceil((double) hero.HT / 4.0);
    }
    protected int healManaAmount(Char hero) {
        return (int) Math.ceil((double) hero.MT / 4.0);
    }

    public static void cure(Char hero) {
        Buff.detach(hero, Poison.class);
        Buff.detach(hero, Cripple.class);
        Buff.detach(hero, Weakness.class);
        Buff.detach(hero, Bleeding.class);
        Buff.detach(hero, Drowsy.class);
        Buff.detach(hero, Slow.class);
        Buff.detach(hero, Vertigo.class);
    }

    @Override
    public String desc() {
        return
                "An elixir that will instantly restore " + descAmount() + " of your health and energy, as well as curing many status effects.";
    }

    protected String descAmount() {
        return "some";
    }

}

