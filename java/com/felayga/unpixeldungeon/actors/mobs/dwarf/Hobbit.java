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

package com.felayga.unpixeldungeon.actors.mobs.dwarf;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.armor.helmet.Helmet;
import com.felayga.unpixeldungeon.items.armor.helmet.HelmetCrude;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.Dagger;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.DaggerCrude;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.DaggerRuned;
import com.felayga.unpixeldungeon.items.weapon.ranged.simple.Sling;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.dwarf.HobbitSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.GoblinSprite;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 6/9/2016.
 */
public class Hobbit extends Mob {

    public Hobbit()
    {
        super(1);

        name = "hobbit";
        spriteClass = HobbitSprite.class;

        experience = 13;
        movementSpeed = GameTime.TICK * 13 / 12;
        attackSpeed = GameTime.TICK;
        defenseMundane = 10;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 500;
        nutrition = 200;
        immunityMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.None.value;

        Weapon weapon;
        switch(Random.Int(3)) {
            case 1:
                weapon = new Dagger();
                break;
            case 2:
                weapon = new DaggerRuned();
                break;
            default:
                weapon = new Sling();
                break;
        }

        weapon.random();
        belongings.collectEquip(weapon);

        if (Random.Int(10)==0) {
            //todo: elven mithril coat
            //belongings.collectEquip(armor);
        }

        if (Random.Int(10)==0) {
            //todo: dwarvish cloak
            //belongings.collectEquip(armor);
        }
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
