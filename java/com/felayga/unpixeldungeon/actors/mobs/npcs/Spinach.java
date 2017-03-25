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

package com.felayga.unpixeldungeon.actors.mobs.npcs;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.mobs.unused.SpinachSprite;

/**
 * Created by HELLO on 2/23/2017.
 */
public class Spinach extends NPC {
    //fake NPC for canning purposes only, should never be seen otherwise

    public Spinach() {
        super(0, SpinachSprite.class);

        defenseMundane = 32767;
        defenseMagical = 32767;
        characteristics = Characteristic.value(Characteristic.NoExperience);
        corpseEffects = CorpseEffect.value(CorpseEffect.Spinach, CorpseEffect.Vegetable);
    }

    private boolean initialized = false;

    @Override
    protected boolean act() {
        if (initialized) {
            HP = 0;

            destroy(null);
            sprite.die();

        } else {
            initialized = true;
        }
        return true;
    }

    @Override
    public int damage(int dmg, MagicType type, Char source, Item sourceItem) {
        return 0;
    }

    @Override
    public void interact() {
    }
}

