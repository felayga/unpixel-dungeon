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

package com.felayga.unpixeldungeon.spellcasting;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Halo;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.Game;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

/**
 * Created by HELLO on 3/10/2017.
 */

public class LightSpellcaster extends Spellcaster {
    public LightSpellcaster() {
        super("Light", 1);

        ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.StopSelf);
    }

    @Override
    public void fxEffect(Char source, Ballistica path, int sourcePos, int targetPos, Callback callback) {
        MagicMissile.haste(source.sprite.parent, sourcePos, targetPos, callback);
    }

    @Override
    public void onZap(Char source, Ballistica path, int targetPos) {
        CellEmitter.get(targetPos).start(ShaftParticle.FACTORY, 0.2f, 6);
        for (Integer offset : Level.NEIGHBOURS8) {
            CellEmitter.get(targetPos + offset).start(ShaftParticle.FACTORY, 0.4f, 1);
        }
        Dungeon.level.setLight(targetPos, 5, true);
        Dungeon.level.addVisual(new Aura(targetPos, false, true));
    }

    public static class Aura extends Emitter {
        private Halo halo;

        public Aura(int pos, boolean flames, boolean halo) {
            super();
            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld(pos);
            pos(pos, p.x - 1, p.y + 3, 2, 0);

            if (flames) {
                pour(FlameParticle.FACTORY, 0.15f);
            }
            if (halo) {
                this.halo = new Halo(16, 0xFFFFCC, 0.1f).point(p.x, p.y);
                add(this.halo);
            }
        }

        private float twitchTime = 0.0f;
        private float twitchScale = 0.325f;
        private float twitchDirection = 0.005f;

        @Override
        public void update() {
            if (visible = Dungeon.visible[pos]) {
                if (halo != null) {
                    twitchTime += Game.elapsed;
                    while(twitchTime >= 0.1) {
                        twitchTime -= 0.1;
                        twitchScale += twitchDirection;

                        if (twitchScale >= 0.35f) {
                            if (twitchDirection > 0) {
                                twitchDirection = -twitchDirection;
                            }
                        } else if (twitchScale <= 0.3f) {
                            if (twitchDirection < 0)
                                twitchDirection = -twitchDirection;
                        }
                    }
                    halo.scale(twitchScale);
                }
                super.update();
            }
        }
    }
}
