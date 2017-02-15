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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Hallucination;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 11/15/2016.
 */
public class PotionOfSickness extends Potion {

    public PotionOfSickness() {
        name = "Potion of Sickness";
        initials = "Si";

        bones = true;
        isHelpful = true;

        price = 30;
    }

    @Override
    public void apply(Char hero) {
        setKnown();

        GLog.n("Yecch!  This stuff tastes like poison.");

        int healthDamage;
        int attributeDamage;

        switch (bucStatus) {
            case Blessed:
                healthDamage = 1;
                attributeDamage = 0;
                GLog.w("Must be stale fruit juice.");
                break;
            case Cursed:
                healthDamage = Random.Int(1, 16);
                attributeDamage = Random.Int(1,5);
                break;
            default:
                healthDamage = Random.Int(1, 10);
                attributeDamage = Random.Int(1,3);
                break;
        }

        hero.damage(healthDamage, MagicType.Poison, null);
        if (attributeDamage > 1 && hero.hasImmunity(MagicType.Poison)) {
            attributeDamage = 1;
        }

        if (hero.buff(Hallucination.class) != null) {
            Buff.detach(hero, Hallucination.class);
            GLog.p("You are shocked back to your senses!");
        }

        if (attributeDamage > 0) {
            int test = Random.Int(6);
            switch (test / 2) {
                case 0:
                    hero.damageAttribute(AttributeType.STRCON, attributeDamage);
                    if (test % 2 == 0) {
                        GLog.n("You feel weaker.");
                    } else {
                        GLog.n("You feel very sick.");
                    }
                    break;
                case 1:
                    hero.damageAttribute(AttributeType.DEXCHA, attributeDamage);
                    if (test % 2 == 0) {
                        GLog.n("Your muscles won't obey you.");
                    } else {
                        GLog.n("You break out in hives.");
                    }
                    break;
                default:
                    hero.damageAttribute(AttributeType.INTWIS, attributeDamage);
                    if (test % 2 == 0) {
                        GLog.n("Your brain is on fire.");
                    } else {
                        GLog.n("Your judgement is impaired.");
                    }
                    break;
            }
        }
    }

    @Override
    public String desc() {
        return
                "An elixir that will instantly restore " + descAmount() + " of your health and cure many status effects.";
    }

    protected String descAmount() {
        return "some";
    }

}

