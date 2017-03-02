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
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/3/2017.
 */
public class PotionOfOilLit extends PotionOfOil {

    public PotionOfOilLit()
    {
        name = "Potion of Oil (lit)";
        initials = "Oi";

        isHarmful = true;
        defaultAction = Constant.Action.THROW;

        price = 0;
    }

    public Emitter emitter() {
        Emitter emitter = new Emitter();

        emitter.pour(new Emitter.Factory() {
            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                Visual target = emitter.target();
                PointF scale = target.scale();

                FlameParticle p = ((FlameParticle) emitter.recycle(FlameParticle.class));
                float xrand = target.width * 2.0f / 16.0f * scale.x;
                float yrand = target.height * 2.0f / 16.0f * scale.y;
                x += target.width * 8.0f / 16.0f * scale.x;
                y += target.height * 6.0f / 16.0f * scale.y;

                if (Random.Int(2) == 0) {
                    x += Random.Float(xrand);
                } else {
                    x -= Random.Float(xrand);
                }

                if (Random.Int(2) == 0) {
                    y += Random.Float(yrand);
                } else {
                    y -= Random.Float(yrand);
                }

                p.reset(x, y);
            }
        }, 0.1f);
        emitter.fillTarget = false;

        return emitter;
    }

    @Override
    public void syncVisuals() {
        image = handler.image(PotionOfOil.class);
        color = handler.label(PotionOfOil.class);
    }

    @Override
    public boolean execute(final Hero hero, String action) {
        if (action.equals(Constant.Action.APPLY)) {
            handler.know(this);

            hero.belongings.collect(new PotionOfOil().bucStatus(this));

            GLog.i("You extinguish the flame on the potion.");

            hero.belongings.remove(this, 1);
            return false;
        } else {
            return super.execute(hero, action);
        }
    }

    @Override
    public void apply(Char hero) {
        applyBurn(hero, hero);
    }

    @Override
    public void shatter( Char source, int cell ) {
        shatterBurn(source, cell);
    }


    @Override
    public String desc() {
        return "This flask contains a flammable compound.\n\nIt is currently on fire.";
    }

}
