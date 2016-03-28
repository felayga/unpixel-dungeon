/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.items.food;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.DeathlySick;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Corpse extends Food implements IDecayable {

    public Corpse() {
        this(null);
    }

    public Corpse(Char source) {
        super();

        if (source != null) {
            name = source.name + " corpse";
            energy = source.nutrition;
            decayTime = source.getTime();
        } else {
            name = "unknown corpse";
            energy = 1;
        }
        image = ItemSpriteSheet.MEAT;
        stackable = false;

        hornValue = 1;
        hasBuc(false);
    }


    @Override
    public Item bucStatus(BUCStatus status, boolean statusKnown) {
        BUCStatus oldStatus = bucStatus();

        Item retval = super.bucStatus(status, statusKnown);

        BUCStatus newStatus = bucStatus();

        switch (oldStatus) {
            case Cursed:
                switch (newStatus) {
                    case Uncursed:
                        rottenness -= 2;
                        break;
                    case Blessed:
                        rottenness -= 4;
                        break;
                }
                break;
            case Uncursed:
                switch (newStatus) {
                    case Cursed:
                        rottenness += 2;
                        break;
                    case Blessed:
                        rottenness -= 2;
                        break;
                }
                break;
            case Blessed:
                switch (newStatus) {
                    case Cursed:
                        rottenness += 4;
                        break;
                    case Uncursed:
                        rottenness += 2;
                        break;
                }
                break;
        }

        return retval;
    }

    protected long decay = -30 * GameTime.TICK;
    protected long decayMark = 0;
    protected int rottenness = 0;
    protected long decayTime;

    public long decay() {
        return decay;
    }

    public void decay(long amount, boolean updateTime, boolean fixTime) {
        if (fixTime || updateTime) {
            long newAmount = amount - decayTime;
            if (fixTime) {
                decayTime = 0;
            } else {
                decayTime = amount;
            }
            amount = newAmount;
        }

        decay += amount;

        //GLog.d("decay="+decay+" rottenness="+rottenness);
        while (decayMark <= decay) {
            decayMark += Random.Long(GameTime.TICK * 10, GameTime.TICK * 30);
            rottenness++;
        }
    }

    public boolean decayed() {
        return decay > GameTime.TICK * 250;
    }

    public boolean isOld() {
        return decay >= GameTime.TICK * 20;
    }

    public boolean isRotten() {
        return rottenness >= 4;
    }

    public boolean isTainted() {
        return rottenness >= 6;
    }


    @Override
    public boolean isSimilar(Item item) {
        if (super.isSimilar(item)) {
            if (item instanceof Corpse) {
                Corpse corpse = (Corpse) item;

                return name.equals(corpse.name);
            }
        }

        return false;
    }


    private static final String CORPSENAME = "corpseName";
    private static final String DECAY = "decay";
    private static final String DECAYMARK = "decayMark";
    private static final String ROTTENNESS = "rottenness";
    private static final String DECAYTIME = "decayTime";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(CORPSENAME, name);
        bundle.put(DECAY, decay);
        bundle.put(DECAYMARK, decayMark);
        bundle.put(ROTTENNESS, rottenness);
        bundle.put(DECAYTIME, decayTime);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        name = bundle.getString(CORPSENAME);
        decay = bundle.getLong(DECAY);
        decayMark = bundle.getLong(DECAYMARK);
        rottenness = bundle.getInt(ROTTENNESS);
        decayTime = bundle.getLong(DECAYTIME);
    }

    private void qualityCheck(Hero hero){
        if (isTainted()) {
            GLog.n("Ulch - that food was tainted!");
            Buff.affect(hero, DeathlySick.class);
        }
        else if (isRotten()) {
            GLog.w("You feel sick.");
            hero.damage(Random.Int(0, 8) + 1, this);
        }
    }

    @Override
    public void stoppedEating(Hero hero) {
        super.stoppedEating(hero);

        qualityCheck(hero);
    }

    @Override
    public void doneEating(Hero hero, boolean stuffed) {
        super.doneEating(hero, stuffed);

        qualityCheck(hero);
    }

    @Override
    public String info() {
        return "Eat at your own risk!";
    }

    @Override
    public String message()
    {
        return "This " + name + " tastes terrible!";
    }

    public int price() {
        return 5 * quantity;
    }

}
