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
import com.felayga.unpixeldungeon.actors.buffs.hero.Sick;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 11/29/2016.
 */
public class PotionOfIpecac extends Potion {

    public PotionOfIpecac()
    {
        name = "Potion of Ipecac";
        initials = "Ip";

        isHelpful = true;

        price = 40;
    }

    @Override
    public void apply( Char hero ) {
        setKnown();

        Sick.vomit(hero, hero);
        
        if (bucStatus() == BUCStatus.Cursed || (bucStatus() == BUCStatus.Uncursed && Random.Int(2)==0)) {
            Buff.prolong(hero, hero, Sick.class, 0);
        }
    }

    @Override
    public String desc() {
        return
                "Drinking this potion will cause you to forcefully vomit.";
    }

}
