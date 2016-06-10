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

package com.felayga.unpixeldungeon.actors.mobs.orc;

import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.armor.helmet.Helmet;
import com.felayga.unpixeldungeon.items.armor.helmet.HelmetCrude;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.MeleeMobAttack;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.DaggerCrude;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.GoblinSprite;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 5/22/2016.
 */
public class Goblin extends Mob {

    public Goblin()
    {
        super(0);

        name = "goblin";
        spriteClass = GoblinSprite.class;

        experience = 6;
        movementSpeed(GameTime.TICK * 4 / 3);
        attackSpeed(GameTime.TICK);
        defenseMundane = 10;
        defenseMagical = 0;
        weight = Encumbrance.UNIT * 400;
        nutrition = 100;
        immunityMagical = MagicType.None.value;
        corpseEffects = CorpseEffect.None.value;

        if (Random.Int(2)==0) {
            belongings.collectEquip(new MeleeMobAttack(GameTime.TICK, 1, 4));
        }
        else {
            Weapon weapon = new DaggerCrude();
            weapon.random();

            belongings.collectEquip(weapon);
        }

        if (Random.Int(2)==0) {
            Helmet helmet = new HelmetCrude();
            helmet.random();

            belongings.collectEquip(helmet);
        }
    }

    @Override
    public String description() {
        return "A small omnivorous dog-like creature.  Fairly harmless.";
    }
}
