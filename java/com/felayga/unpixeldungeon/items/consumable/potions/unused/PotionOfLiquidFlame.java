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
package com.felayga.unpixeldungeon.items.consumable.potions.unused;
/*
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.FlameParticle;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class PotionOfLiquidFlame extends Potion {

    public PotionOfLiquidFlame()
	{
		name = "Potion of Oil";
		initials = "Oi";

		isHarmful = true;

        ignited(false);

        price = 40;
	}

    protected boolean checkSimilarity(Item item) {
        if (super.checkSimilarity(item)) {
            PotionOfLiquidFlame other = (PotionOfLiquidFlame)item;

            return other.ignited == this.ignited;
        }

        return false;
    }

    private boolean ignited;

    public Item ignited(boolean state) {
        ignited = state;

        if (state) {
            defaultAction = Constant.Action.THROW;
        } else {
            defaultAction = Constant.Action.DRINK;
        }

        return this;
    }

    public boolean ignited() {
        return this.ignited;
    }

    private static final String IGNITED = "ignited";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(IGNITED, ignited);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        ignited(bundle.getBoolean(IGNITED));
    }

    @Override
    public boolean execute(final Hero hero, String action) {
        if (action.equals(Constant.Action.APPLY)) {
            handler.know(this);

            if (ignited) {
                hero.belongings.collect(new PotionOfLiquidFlame().bucStatus(this));

                GLog.i("You extinguish the flame on the potion.");
            }
            else {
                hero.belongings.collect(new PotionOfLiquidFlame().ignited(true).bucStatus(this));

                GLog.i("You ignite the potion.");
            }

            hero.belongings.remove(this, 1);
            return false;
        } else {
            return super.execute(hero, action);
        }
    }
	
	@Override
	public void shatter( int cell ) {

		if (Dungeon.visible[cell]) {
			splash(cell);
		}
        if (Dungeon.audible[cell]) {
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }

        if (ignited) {
            for (int offset : Level.NEIGHBOURS9) {
                if (Level.wood[cell + offset] || Actor.findChar(cell + offset) != null || Dungeon.level.heaps.get(cell + offset) != null) {
                    GameScene.add(Blob.seed(cell + offset, 2, Fire.class));
                } else {
                    CellEmitter.get(cell + offset).burst(FlameParticle.FACTORY, 2);
                }
            }
        } else {
            GLog.w("The contents of the potion splash everywhere harmlessly.");
        }
	}
	
	@Override
	public String desc() {
        String retval = "This flask contains a flammable compound.";

        if (ignited) {
            retval += "\nIt is currently on fire.";
        }

        return retval;
	}

}
*/