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
package com.felayga.unpixeldungeon.levels.traps;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Trap implements Bundlable {

    public String name;

    public int color;
    public int shape;

    public int pos;
    private int ownerRegistryIndex;

    public int ownerRegistryIndex() {
        return ownerRegistryIndex;
    }

    public TrapSprite sprite;
    public boolean visible;
    public boolean active = true;

    public Trap set(Char owner, int pos) {
        this.pos = pos;
        if (owner != null) {
            this.ownerRegistryIndex = owner.charRegistryIndex();
        } else {
            this.ownerRegistryIndex = -1;
        }
        return this;
    }

    public Trap reveal() {
        visible = true;
        if (sprite != null && !sprite.visible) {
            sprite.visible = true;
            sprite.alpha(0);
            sprite.parent.add(new AlphaTweener(sprite, 1, 0.6f));
        }
        return this;
    }

    public Trap hide() {
        visible = false;
        if (sprite != null)
            sprite.visible = false;
        return this;
    }

    public void trigger() {
        if (active) {
            if (Dungeon.audible[pos]) {
                Sample.INSTANCE.play(Assets.SND_TRAP);
            }
            disarm();
            reveal();
            activate();
        }
    }

    public abstract void activate();

    protected void disarm() {
        Dungeon.level.disarmTrap(pos);
        active = false;
        if (sprite != null) {
            sprite.reset(this);
        }
    }

    private static final String POS = "pos";
    private static final String VISIBLE = "visible";
    private static final String ACTIVE = "active";
    private static final String OWNERREGISTRYINDEX = "ownerRegistryIndex";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        pos = bundle.getInt(POS);
        visible = bundle.getBoolean(VISIBLE);
        if (bundle.contains(ACTIVE)) {
            active = bundle.getBoolean(ACTIVE);
        }
        ownerRegistryIndex = bundle.getInt(OWNERREGISTRYINDEX);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(POS, pos);
        bundle.put(VISIBLE, visible);
        bundle.put(ACTIVE, active);
        bundle.put(OWNERREGISTRYINDEX, ownerRegistryIndex);
    }

    public String desc() {
        return "Stepping onto a hidden pressure plate will activate the trap.";
    }
}
