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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/3/2017.
 */
public class PotionOfOil extends Potion {

    public PotionOfOil()
    {
        name = "Potion of Oil";
        initials = "Oi";

        isHarmful = true;

        price = 40;
    }

    @Override
    public boolean execute(final Hero hero, String action) {
        if (action.equals(Constant.Action.APPLY)) {
            handler.know(this);
            handler.know(PotionOfOilLit.class);

            hero.belongings.collect(new PotionOfOilLit().bucStatus(this));

            GLog.i("You ignite the potion.");

            hero.belongings.remove(this, 1);
            return false;
        } else {
            return super.execute(hero, action);
        }
    }

    @Override
    public void apply(Char target) {
        if (target.buff(Burning.class) != null) {
            applyBurn(target, target);
        } else {
            if (bucStatus() == BUCStatus.Cursed) {
                GLog.w("This tastes like castor oil.");
                bucStatus(true);
            }
            else {
                GLog.w("That was smooth!");
            }

            if (target == Dungeon.hero) {
                Hero hero = (Hero)target;

                hero.useAttribute(AttributeType.INTWIS, -4);
            } else {
                target.damageAttribute(AttributeType.INTWIS, 1);
            }
        }
    }

    protected void applyBurn(Char target, Char source) {
        if (target == Dungeon.hero) {
            Hero hero = (Hero) target;

            hero.useAttribute(AttributeType.INTWIS, -16);

            GLog.n("You burn your face.");
        } else {
            target.damageAttribute(AttributeType.INTWIS, 1);
        }

        GameScene.add(Blob.seed(source, target.pos(), 2, Fire.class));
    }

    @Override
    public void shatter( Char owner, int cell ) {
        Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
        if (fire != null) {
            shatterBurn(owner, cell);
        } else {
            super.shatter(owner, cell);
        }
    }

    public void shatterBurn(Char owner, int cell) {
        if (Dungeon.visible[cell]) {
            splash(cell);
        }
        if (Dungeon.audible[cell]) {
            Sample.INSTANCE.play(Assets.SND_SHATTER);
        }

        Char test = Actor.findChar(cell);

        if (test != null) {
            test.damage(Random.IntRange(1, 4), MagicType.Fire, null);
        }

        GLog.w("The potion explodes in flames!");

        for (int offset : Level.NEIGHBOURS9) {
            if (Level.burnable[cell + offset] || Actor.findChar(cell + offset) != null || Dungeon.level.heaps.get(cell + offset) != null) {
                GameScene.add(Blob.seed(owner, cell + offset, 2, Fire.class));
            } else {
                CellEmitter.get(cell + offset).burst(FlameParticle.FACTORY, 2);
            }
        }

        setKnown();
    }

    @Override
    public String desc() {
        return "This flask contains a flammable compound.";
    }

}
