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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.weapon.ammunition.simple.Rock;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.npcs.BoulderSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Boulder extends NPC {
    public Boulder() {
        super(0);

        name = "boulder";
        spriteClass = BoulderSprite.class;
        HP = HT = 101;
        characteristics = Characteristic.value(Characteristic.Brainless, Characteristic.NonBreather, Characteristic.NoExperience);

        state = PASSIVE;
        actPriority = -1; // gotta be before the hero if pushed
    }

    @Override
    public String defenseVerb() {
        return "undamaged";
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public int damage(int dmg, MagicType type, Char source, Item sourceItem) {
        return 0;
    }

    @Override
    public void add(Buff buff) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public void interact() {
        int newPos = this.pos() * 2 - Dungeon.hero.pos();
        boolean mobBlock = Actor.findChar(newPos) != null;

        if (!mobBlock) {
            if (Level.chasm[newPos] || Level.pit[newPos]) {
                pluggingPit = true;

                move(newPos);
                Dungeon.hero.pushBoulder(this);
                CellEmitter.get(newPos).burst(Speck.factory(Speck.DUST), 2);

                if (Level.chasm[newPos]) {
                    GLog.p("The boulder settles into the chasm, neatly plugging the hole.");
                } else {
                    GLog.p("The boulder settles into the pit, neatly plugging the hole.");
                }
            } else if (Level.passable[newPos]) {
                move(newPos);
                Dungeon.hero.pushBoulder(this);
                CellEmitter.get(newPos).burst(Speck.factory(Speck.DUST), 2);
            } else {
                GLog.n("You try to move the boulder, but in vain.");
            }
        } else {
            GLog.n("The boulder won't budge.");
        }
    }

    private int losBlockStoredPos = Constant.Position.NONE;
    private boolean losBlockStoredFlag = false;
    private boolean pluggingPit = false;

    @Override
    public boolean visibilityOverride(boolean state) {
        return true;
    }

    @Override
    protected boolean act() {
        boolean retval = super.act();

        losBlockInitializationCheck();

        return retval;
    }

    @Override
    protected void onAdd() {
        super.onAdd();

        losBlockInitializationCheck();
    }

    private boolean initializing = false;

    private void losBlockInitializationCheck() {
        if (losBlockStoredPos < 0 || (!Level.losBlocking[pos()])) {
            initializing = true;
            move(pos());
            initializing = false;
        }
    }


    @Override
    public void move(int newPos) {
        int tempPos = losBlockStoredPos;
        boolean tempFlag = losBlockStoredFlag;

        losBlockStoredPos = newPos;
        losBlockStoredFlag = Level.losBlocking[newPos];

        if (!initializing) {
            if (!pluggingPit) {
                Sample.INSTANCE.play(Assets.SND_BOULDER_SCRAPE);
            } else {
                Sample.INSTANCE.play(Assets.SND_BOULDER_PLUG);
            }
        }

        if (!initializing) {
            Level.losBlocking[tempPos] = tempFlag;
            GameScene.updateMap(tempPos);
        }

        Level.losBlocking[newPos] = true;
        GameScene.updateMap(newPos);

        if (!initializing) {
            Dungeon.observe();
        }

        int oldPos = pos();
        if (oldPos != newPos) {
            //GLog.d("boulder moving to " + newPos);
            super.move(newPos);
            sprite.move(oldPos, newPos);
        }
    }

    @Override
    public void onMotionComplete() {
        super.onMotionComplete();

        if (!pluggingPit) {
            Level.losBlocking[pos()] = true;
            sprite.idle();
        } else {
            die(null);

            Dungeon.level.setDirt(pos(), true, true);
        }

        GameScene.updateMap(pos());
        Dungeon.observe();
    }

    @Override
    public void die(Actor cause) {
        sprite.interruptMotion();

        if (!pluggingPit) {
            Sample.INSTANCE.play(Assets.SND_BOULDER_SMASH);
            //Dungeon.level.spawnGemstones(pos);
            Dungeon.level.drop(new Rock().quantity(Random.IntRange(3, 23)), pos()).rockBottom();

            Level.losBlocking[losBlockStoredPos] = losBlockStoredFlag;
            GameScene.updateMap(losBlockStoredPos);
            Dungeon.observe();
        } else {
            sprite.alpha(0.0f);
        }

        CellEmitter.get(pos()).burst(Speck.factory(Speck.DUST), 5);

        destroy(cause);
        sprite.die();
    }

    private final static String LOSBLOCKSTOREDPOS = "losBlockStoredPos";
    private final static String LOSBLOCKSTOREDFLAG = "losBlockStoredFlag";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(LOSBLOCKSTOREDPOS, losBlockStoredPos);
        bundle.put(LOSBLOCKSTOREDFLAG, losBlockStoredFlag);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        losBlockStoredPos = bundle.getInt(LOSBLOCKSTOREDPOS);
        losBlockStoredFlag = bundle.getBoolean(LOSBLOCKSTOREDFLAG);
    }

    @Override
    public String description() {
        return "This is a large boulder.  Maybe you could push it?";
    }


}
