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

package com.felayga.unpixeldungeon.items.equippableitem.weapon.melee.mob;
/*
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;


//Created by HELLO on 3/9/2016.
public class ComboChance extends MeleeMobAttack {
    public ComboChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    public int combo = 0;

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        combo++;
        int effect = Random.Int(4)+combo;

        if (effect > 2) {

            if (effect >=6 && target.buff(Burning.class) == null){

                if (Level.wood[target.pos])
                    GameScene.add(Blob.seed(target.pos, 4, Fire.class));
                Buff.affect( target, Burning.class ).reignite( target );

            } else
                Buff.affect( target, Poison.class).set((effect-2) * Poison.durationFactor(target));

        }

        return damage;
    }
}
*/