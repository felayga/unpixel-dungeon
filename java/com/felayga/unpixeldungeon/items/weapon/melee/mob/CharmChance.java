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

package com.felayga.unpixeldungeon.items.weapon.melee.mob;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Charm;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/9/2016.
 */
public class CharmChance extends MeleeMobAttack {
    public CharmChance(long delay, int damageMin, int damageMax) {
        super(delay, damageMin, damageMax);
    }

    @Override
    public int proc(Char attacker, boolean thrown, Char target, int damage)
    {
        damage = super.proc(attacker, thrown, target, damage);

        if (Random.Int( 3 ) == 0) {
            Buff.affect( target, Charm.class, Charm.durationFactor( target ) * Random.LongRange(GameTime.TICK * 3, GameTime.TICK * 7) / GameTime.TICK ).object = attacker.id();
            target.sprite.centerEmitter(-1).start( Speck.factory(Speck.HEART), 0.2f, 5 );
            Sample.INSTANCE.play(Assets.SND_CHARMS);
        }

        return damage;
    }
}
