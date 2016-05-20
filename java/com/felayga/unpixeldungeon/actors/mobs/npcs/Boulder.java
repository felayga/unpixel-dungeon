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
package com.felayga.unpixeldungeon.actors.mobs.npcs;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.Roots;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.tools.ITool;
import com.felayga.unpixeldungeon.items.tools.digging.DiggingTool;
import com.felayga.unpixeldungeon.items.weapon.missiles.Rock;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.npcs.BoulderSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Boulder extends NPC {
    public final static String TXT_PUSH = "PUSH";

    public final static String STOREDPOS = "storedPos";
    public final static String STOREDFLAG = "storedFlag";

    public Boulder() {
        super();

        name = "boulder";
        spriteClass = BoulderSprite.class;

        state = PASSIVE;
        actPriority = -8;
    }

    @Override
    public String defenseVerb() {
        return "undamaged";
    }

    @Override
    public long speed() {
        return GameTime.TICK;
    }

    @Override
    protected Char chooseEnemy() { return null; }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public void interact() {
        final Boulder boulder = this;
        final Hero hero = Dungeon.hero;

        final List<String> actions = new ArrayList<String>();
        List<Boolean> actionOptions = new ArrayList<Boolean>();

        actions.add(TXT_PUSH);
        actionOptions.add(false);

        ITool[] tools = hero.belongings.getToolTypes(DiggingTool.NAME);


        DiggingTool diggingTool = null;
        String diggingToolName = null;
        if (tools[0] != null)
        {
            diggingTool = (DiggingTool)tools[0];
            diggingToolName = diggingTool.getName().toUpperCase();

            actions.add(diggingToolName);
            actionOptions.add(false);
        }

        final DiggingTool diggingTool_WTFJAVA = diggingTool;
        final String diggingToolName_WTFJAVA = diggingToolName;


        actions.add(Constant.TXT_CANCEL);
        actionOptions.add(true);


        final int actionsLength = actions.size();

        ShatteredPixelDungeon.scene().add(
                new WndOptions("Boulder", "A large boulder is in your way.",
                        actions.toArray(new String[actionsLength]),
                        actionOptions.toArray(new Boolean[actionsLength])) {

                    @Override
                    protected void onSelect(int index) {
                        String selection = index >= 0 ? actions.get(index) : "";

                        if (selection == TXT_PUSH) {
                            int newPos = boulder.pos * 2 - Dungeon.hero.pos;
                            boolean mobBlock = false;

                            for (Char c : Actor.chars()) {
                                if (newPos == c.pos)
                                {
                                    mobBlock = true;
                                    break;
                                }
                            }

                            if (!mobBlock) {
                                if (Level.passable[newPos]) {
                                    move(newPos);
                                    Dungeon.hero.pushBoulder(newPos);
                                    CellEmitter.get(newPos).burst(Speck.factory(Speck.DUST), 2);
                                } else if (Level.pit[newPos]) {
                                    move(newPos);
                                    Dungeon.hero.pushBoulder(newPos);
                                    CellEmitter.get(newPos).burst(Speck.factory(Speck.DUST), 2);

                                    pluggingPit = true;

                                    GLog.p("The boulder settles into the pit, filling a hole.");
                                } else {
                                    GLog.n("You try to move the boulder, but in vain.");
                                }
                            }
                            else {
                                GLog.n("There's something on the other side of the boulder.");
                            }
                        }
                        else if (selection == diggingToolName_WTFJAVA) {
                            boulder.die(null);
                        }
                        else {
                            GLog.i("You leave the boulder alone.");
                        }
                    }
                });

    }

    int storedPos = -1;
    boolean storedFlag = false;
    boolean pluggingPit = false;

    @Override
    public boolean visibilityOverride(boolean state)
    {
        return true;
    }

    @Override
    protected boolean act() {
        boolean retval = super.act();

        initializationCheck();

        return retval;
    }

    @Override
    protected void onAdd() {
        super.onAdd();

        initializationCheck();
    }

    private boolean initializing = false;
    private void initializationCheck()
    {
        if (storedPos == -1 || (!Level.losBlocking[pos]))
        {
            initializing = true;
            move(pos);
            initializing = false;
        }
    }


    @Override
    public void move(int newPos)
    {
        int tempPos = storedPos;
        boolean tempFlag = storedFlag;

        storedPos = newPos;
        storedFlag = Level.losBlocking[newPos];

        if (!initializing) {
            Sample.INSTANCE.play(Assets.SND_BOULDER_SCRAPE);

            Level.losBlocking[tempPos] = tempFlag;
            GameScene.updateMap(tempPos);
        }

        Level.losBlocking[newPos] = true;
        GameScene.updateMap(newPos);

        if (!initializing) {
            Dungeon.observe();
            Dungeon.level.press(newPos, this);
        }

        if (pos != newPos) {
            Dungeon.level.press(newPos, this);
            super.move(newPos);
            sprite.move(pos, newPos);
        }
    }

    @Override
    public void onMotionComplete()
    {
        super.onMotionComplete();

        if (!pluggingPit) {
            Level.losBlocking[pos] = true;
            sprite.idle();
        }
        else {
            die(null);
            Level.set(pos, Terrain.EMPTY);
        }

        GameScene.updateMap(pos);
        Dungeon.observe();
    }

    @Override
    public void die(Object cause)
    {
        if (!pluggingPit){
            Dungeon.level.drop( new Rock( Random.Int(6, 66) ), pos );

            Level.losBlocking[storedPos] = storedFlag;
            GameScene.updateMap(storedPos);
            Dungeon.observe();
        }

        Sample.INSTANCE.play(Assets.SND_BOULDER_SMASH);
        CellEmitter.get(pos).burst(Speck.factory(Speck.DUST), 5);

        super.die(cause);
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        bundle.put(STOREDPOS, storedPos);
        bundle.put(STOREDFLAG, storedFlag);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        storedPos = bundle.getInt(STOREDPOS);
        storedFlag = bundle.getBoolean(STOREDFLAG);
    }

    @Override
    public String description() {
        return "This is a large boulder.  Maybe you could push it?";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( Roots.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

}
