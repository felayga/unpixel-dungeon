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
package com.felayga.unpixeldungeon.actors.mobs;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Challenges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.actors.buffs.hero.SoulMark;
import com.felayga.unpixeldungeon.actors.buffs.negative.Amok;
import com.felayga.unpixeldungeon.actors.buffs.negative.Corruption;
import com.felayga.unpixeldungeon.actors.buffs.negative.Sleep;
import com.felayga.unpixeldungeon.actors.buffs.negative.Terror;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroSubClass;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.Surprise;
import com.felayga.unpixeldungeon.effects.Wound;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.felayga.unpixeldungeon.items.rings.RingOfWealth;
import com.felayga.unpixeldungeon.items.weapon.IWeapon;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Level.Feeling;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.levels.features.Door;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.Roll;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public abstract class Mob extends Char {

    public Mob(int level) {
        super(level);

        actPriority = 2; //hero gets priority over mobs.

        HP = HT = Roll.MobHP(level);
    }

    private static final String TXT_DIED_NEAR = "You hear something die nearby.";
    private static final String TXT_DIED_FAR = "You hear something die in the distance.";

    protected static final String TXT_NOTICE1 = "?!";
    protected static final String TXT_RAGE = "#$%^";
    protected static final String TXT_EXP = "%+dEXP";

    public AiState SLEEPING = new Sleeping();
    public AiState HUNTING = new Hunting();
    public AiState WANDERING = new Wandering();
    public AiState FLEEING = new Fleeing();
    public AiState FIDGETING = new Fidgeting();
    public AiState PASSIVE = new Passive();

    public AiState state = SLEEPING;

    public Class<? extends CharSprite> spriteClass;

    protected int lastPos;

    protected int target = Constant.Position.NONE;

    protected int level;

    public int level() {
        return level;
    }

    protected Char enemy;
    protected boolean enemySeen;
    protected boolean alerted = false;

    protected static final long TIME_TO_WAKE_UP = GameTime.TICK;

    public boolean hostile = true;
    public boolean ally = false;

    private static final String STATE = "state";
    private static final String SEEN = "seen";
    private static final String TARGET = "target";
    private static final String MOVEMENTBUDGET = "movementBudget";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(STATE, storeAIState());

        bundle.put(SEEN, enemySeen);
        bundle.put(TARGET, target);
        bundle.put(MOVEMENTBUDGET, movementBudget);
    }

    protected String storeAIState() {
        if (state == SLEEPING) {
            return Sleeping.TAG;
        } else if (state == WANDERING) {
            return Wandering.TAG;
        } else if (state == HUNTING) {
            return Hunting.TAG;
        } else if (state == FLEEING) {
            return Fleeing.TAG;
        } else if (state == FIDGETING) {
            return Fidgeting.TAG;
        } else if (state == PASSIVE) {
            return Passive.TAG;
        }

        return null;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        state = restoreAIState(bundle.getString(STATE));
        enemySeen = bundle.getBoolean(SEEN);
        target = bundle.getInt(TARGET);
        movementBudget = bundle.getLong(MOVEMENTBUDGET);
    }

    public AiState restoreAIState(String state) {
        if (state.equals(Sleeping.TAG)) {
            return SLEEPING;
        } else if (state.equals(Wandering.TAG)) {
            return WANDERING;
        } else if (state.equals(Hunting.TAG)) {
            return HUNTING;
        } else if (state.equals(Fleeing.TAG)) {
            return FLEEING;
        } else if (state.equals(Fidgeting.TAG)) {
            return FIDGETING;
        } else if (state.equals(Passive.TAG)) {
            return PASSIVE;
        }

        return null;
    }

    public CharSprite sprite() {
        CharSprite sprite = null;
        try {
            sprite = spriteClass.newInstance();
        } catch (Exception e) {
        }
        return sprite;
    }

    @Override
    protected boolean act() {
        super.act();

        boolean justAlerted = alerted;
        alerted = false;

        sprite.hideAlert();

        if (paralysed > 0) {
            enemySeen = false;
            spend_new(GameTime.TICK, false);
            return true;
        }

        enemy = chooseEnemy();

        boolean enemySeen = false;
        boolean enemyHeard = false;
        boolean enemyFelt = false;

        if (enemy != null && enemy.isAlive()) {
            enemySeen = enemy.invisible <= 0 && Level.fieldOfView[enemy.pos()];
            enemyHeard = enemy.stealthy <= 0 && Level.fieldOfSound[enemy.pos()];
            enemyFelt = Level.fieldOfTouch[enemy.pos()];
        }

        return state.act(enemySeen, enemyHeard, enemyFelt, justAlerted);
    }

    protected Char chooseEnemy() {
        Terror terror = buff(Terror.class);
        if (terror != null) {
            Char source = (Char) Char.Registry.get(terror.ownerRegistryIndex());
            if (source != null) {
                return source;
            }
        }

        //resets target if: the target is dead, the target has been lost (wandering)
        //or if the mob is amoked/corrupted and targeting the hero (will try to target something else)
        if (enemy != null &&
                !enemy.isAlive() || state == WANDERING ||
                ((buff(Amok.class) != null || buff(Corruption.class) != null) && enemy == Dungeon.hero)) {
            enemy = null;
        }

        //if there is no current target, find a new one.
        if (enemy == null) {
            HashSet<Char> enemies = new HashSet<Char>();

            //if the mob is amoked or corrupted...
            if (buff(Amok.class) != null || buff(Corruption.class) != null) {
                //try to find an enemy mob to attack first.
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob != this && Level.fieldOfView[mob.pos()] && mob.hostile) {
                        enemies.add(mob);
                    }
                }
                if (enemies.size() > 0) {
                    return Random.element(enemies);
                }

                //try to find ally mobs to attack second.
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob != this && Level.fieldOfView[mob.pos()] && mob.ally) {
                        enemies.add(mob);
                    }
                }
                if (enemies.size() > 0) {
                    return Random.element(enemies);
                }

                //if there is nothing, go for the hero, unless corrupted, then go for nothing.
                if (buff(Corruption.class) != null) {
                    return null;
                } else {
                    return Dungeon.hero;
                }
                //if the mob is not amoked...
            } else {
                //try to find ally mobs to attack.
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob != this && Level.fieldOfView[mob.pos()] && mob.ally) {
                        enemies.add(mob);
                    }
                }

                //and add the hero to the list of targets.
                enemies.add(Dungeon.hero);

                //target one at random.
                return Random.element(enemies);
            }
        } else {
            return enemy;
        }
    }

    protected void moveSprite(int from, int to) {
        if (sprite.isVisible() && (Dungeon.visible[from] || Dungeon.visible[to])) {
            sprite.move(from, to);
        } else {
            sprite.place(to);
        }
    }

    @Override
    public void add(Buff buff) {
        super.add(buff);
        if (buff instanceof Amok) {
            if (sprite != null) {
                sprite.showStatus(CharSprite.NEGATIVE, TXT_RAGE);
            }
            state = HUNTING;
        } else if (buff instanceof Terror) {
            state = FLEEING;
        } else if (buff instanceof Sleep) {
            state = SLEEPING;
            this.sprite().showSleep();
            postpone(Sleep.SWS);
        }
    }

    @Override
    public void remove(Buff buff) {
        super.remove(buff);
        if (buff instanceof Terror) {
            sprite.showStatus(CharSprite.NEGATIVE, TXT_RAGE);
            state = HUNTING;
        }
    }

    protected boolean[] getCloserStepCandidate() {
        int len = Level.LENGTH;
        boolean[] retval = new boolean[len];
        boolean canPassDoors = canPassDoors();

        if ((characteristics & Characteristic.Ethereal.value) == 0) {
            boolean[] passable = Level.passable;
            boolean[] pathable = Level.pathable;

            for (int i = 0; i < len; i++) {
                retval[i] = passable[i] || (pathable[i] && canPassDoors);
            }
        } else {
            Arrays.fill(retval, true);
        }

        return retval;
    }

    private long movementBudget = 0;

    protected boolean getCloser_new(int target) {
        long speed = movementSpeed();

        if (speed == 0) {
            return false;
        }

        //movement budget here is to let enemies attack in between movement steps, otherwise the enemy (especially slower ones) wouldn't be able to at all
        //so don't remove it again, you idiot
        long bestSpeed = Math.min(speed, attackSpeed());
        movementBudget += bestSpeed;

        if (movementBudget >= speed) {
            movementBudget -= speed;

            boolean[] candidate = getCloserStepCandidate();
            boolean[] diagonal = Level.diagonal;

            int step = Dungeon.findPath(this, pos(), target, candidate, diagonal, Level.fieldOfView);

            if (step != Constant.Position.NONE) {
                if (Dungeon.level.map[step] == Terrain.DOOR) {
                    if (isEthereal()) {
                        //nothing
                    } else if (isAmorphous()) {
                        //todo: invisible when squeezing through door logic
                    } else {
                        if ((characteristics & Characteristic.CannotUseItems.value) != 0) {
                            if ((characteristics & Characteristic.Brainless.value) != 0) {
                                int result = Door.tryKick(this, step);

                                if (Dungeon.visible[step]) {
                                    switch (result) {
                                        case 2:
                                            GLog.w("The " + name + " attacks the door.  It shatters to pieces!");
                                            break;
                                        case 1:
                                            GLog.w("The " + name + " attacks the door.  It crashes open!");
                                            break;
                                        case 0:
                                            GLog.w("WHAMMM!!!");
                                            break;
                                    }
                                }

                                spend_new(Door.TIME_TO_INTERACT, false);
                                //sprite.attack(step);
                                return true;
                            } else {
                                return false;
                            }
                        }

                        Door.open(step);
                        spend_new(Door.TIME_TO_INTERACT, false);
                        return true;
                    }
                }
                /*else if (Dungeon.level.map[lastPos] == Terrain.OPEN_DOOR) {
                    if (!enemySeen) {
                        Door.close(lastPos);
                        spend_new(Door.TIME_TO_INTERACT, false);
                        return true;
                    }
                }*/

                moveSprite(pos(), step);
                move(step);
            } else {
                return false;
            }
        }

        spend_new(bestSpeed, false);
        return true;
    }

    protected boolean getFurther(int target) {
        boolean[] candidate = getCloserStepCandidate();
        boolean[] diagonal = Level.diagonal;

        int step = Dungeon.flee(this, pos(), target,
                candidate, diagonal,
                Level.fieldOfView);

        if (step != Constant.Position.NONE) {
            moveSprite(pos(), step);
            move(step);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateSpriteState() {
        super.updateSpriteState();
        if (Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) != null)
            sprite.add(CharSprite.State.PARALYSED);
    }

    @Override
    public void move(int step) {
        super.move(step);

        if (!flying()) {
            Dungeon.level.mobPress(this);
        }
    }

    protected boolean doAttack(Char enemy) {
        boolean visible = Dungeon.visible[pos()];

        if (visible) {
            sprite.attack(enemy.pos());
        } else {
            EquippableItem item = belongings.weapon();

            Weapon weapon = null;
            if (item instanceof Weapon) {
                weapon = (Weapon) item;
            }

            attack(weapon, enemy);
        }

        spend_new(attackSpeed(), false);

        return !visible;
    }

    @Override
    public void onAttackComplete() {
        EquippableItem item = belongings.weapon();

        Weapon weapon = null;
        if (item instanceof Weapon) {
            weapon = (Weapon) item;
        }

        attack(weapon, enemy);
        super.onAttackComplete();
    }


    @Override
    public int defenseProc(Char enemy, int damage) {
        if (!enemySeen && enemy == Dungeon.hero) {
            if (((Hero) enemy).subClass == HeroSubClass.ASSASSIN) {
                damage *= 1.34f;
                Wound.hit(this);
            } else {
                Surprise.hit(this);
            }
        }

        //become aggro'd by a corrupted enemy
        if (enemy.buff(Corruption.class) != null) {
            aggro(enemy);
            target = enemy.pos();
            if (state == SLEEPING || state == WANDERING)
                state = HUNTING;
        }

        if (buff(SoulMark.class) != null) {
            int restoration = Math.max(damage, HP);
            Dungeon.hero.buff(Hunger.class).satisfy_new(restoration);
            Dungeon.hero.HP = (int) Math.ceil(Math.min(Dungeon.hero.HT, Dungeon.hero.HP + (restoration * 0.25f)));
            Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
        }

        return damage;
    }

    @Override
    public int attackSkill(IWeapon weapon, boolean thrown, Char target) {
        int retval = super.attackSkill(weapon, thrown, target);

        retval += level;

        return retval;
    }

    public void aggro(Char ch) {
        enemy = ch;
        if (state != PASSIVE) {
            state = HUNTING;
        }
    }

    @Override
    public int damage(int dmg, MagicType type, Actor source) {
        Terror.recover(this);

        if (state == SLEEPING) {
            state = WANDERING;
        }
        alerted = true;

        return super.damage(dmg, type, source);
    }


    @Override
    public void destroy(Actor cause) {
        super.destroy(cause);

        Buff.remove(this);
        Dungeon.level.mobs.remove(this);

        if (Dungeon.hero == cause && Dungeon.hero.isAlive()) {
            Statistics.enemiesSlain++;
            Badges.validateMonstersSlain();
            Statistics.qualifiedForNoKilling = false;

            if (Dungeon.level.feeling == Feeling.DARK) {
                Statistics.nightHunt++;
            } else {
                Statistics.nightHunt = 0;
            }
            Badges.validateNightHunter();

            int bonus;

            int experience = level * level + 1;

            long movementSpeed = movementSpeed();

            if (movementSpeed > 0) {
                bonus = (int) ((GameTime.TICK - movementSpeed) * 16 / GameTime.TICK);
                if (bonus > 0) {
                    experience += bonus;
                }
            }

            if (defenseMundane > 17) {
                bonus = (int) Math.pow(defenseMundane - 17, 0.8) * 5;

                if (bonus > 0) {
                    experience += bonus;
                }
            }

            if (level >= 9) {
                experience += 50;
            }

            //todo: rest of XP bonuses
            //special attacks (10 spellcasting, 5 weapon, 3 others except [passive, claw, bite, kick, headbutt])
            //damage types (50-level for sliming, stoning, drain life; level for fire, cold, shock, sleep, disintegration, magic missiles, acid, stat draining poison)
            //damage (level if maximum damage >=24)

            //removed dungeon hero level check (Dungeon.hero.lvl <= Hero.MAX_LEVEL)
            if (experience > 0) {
                Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, TXT_EXP, experience);
                Dungeon.hero.earnExp(experience);
            }
        }
    }

    @Override
    public void die(Actor cause) {
        super.die(cause);

        float lootChance = this.lootChance;
        int bonus = 0;
        for (Buff buff : Dungeon.hero.buffs(RingOfWealth.Wealth.class)) {
            bonus += ((RingOfWealth.Wealth) buff).level;
        }

        lootChance *= Math.pow(1.1, bonus);

        //removed dungeon hero level check (Dungeon.hero.lvl <= Hero.MAX_LEVEL)
        if (Random.Float() < lootChance) {
            Item loot = createLoot();
            if (loot != null)
                Dungeon.level.drop(loot, pos()).sprite.drop();
        }

        if (Dungeon.hero.isAlive() && !Dungeon.visible[pos()]) {
            if (Dungeon.audible[pos()]) {
                GLog.i(TXT_DIED_NEAR);
                Dungeon.level.warnings.remove(pos());
            } else {
                GLog.i(TXT_DIED_FAR);
            }
        }
    }

    protected Object loot = null;
    protected float lootChance = 0;

    @SuppressWarnings("unchecked")
    protected Item createLoot() {
        Item item;
        if (loot instanceof Generator.Category) {

            item = Generator.random((Generator.Category) loot);

        } else if (loot instanceof Class<?>) {

            item = Generator.random((Class<? extends Item>) loot);

        } else {

            item = (Item) loot;

        }
        return item;
    }

    public boolean reset() {
        return false;
    }

    public void beckon(int cell) {
        notice();

        if (state != HUNTING) {
            state = WANDERING;
        }
        target = cell;
    }

    public String description() {
        return "Real description is coming soon!";
    }

    public void notice() {
        sprite.showAlert();
    }

    public void yell(String str) {
        GLog.n("%s: \"%s\" ", name, str);
    }

    //returns true when a mob sees the hero, and is currently targeting them.
    public boolean focusingHero() {
        return enemySeen && (target == Dungeon.hero.pos());
    }

    public interface AiState {
        boolean act(boolean enemyVisible, boolean enemyAudible, boolean enemyTouchable, boolean justAlerted);

        String status();
    }

    protected class Sleeping implements AiState {
        public static final String TAG = "SLEEPING";

        @Override
        public boolean act(boolean enemyVisible, boolean enemyAudible, boolean enemyTouchable, boolean justAlerted) {
            //todo: better sleeping logic
            if (enemyVisible || enemyTouchable) {
                enemySeen = true;

                notice();
                state = HUNTING;
                target = enemy.pos();

                if (Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE)) {
                    for (Mob mob : Dungeon.level.mobs) {
                        if (mob != Mob.this) {
                            mob.beckon(target);
                        }
                    }
                }

                spend_new(TIME_TO_WAKE_UP, false);
            } else {
                enemySeen = false;

                spend_new(GameTime.TICK, false);
            }
            return true;
        }

        @Override
        public String status() {
            return Utils.format("This %s is sleeping", name);
        }
    }

    protected class Wandering implements AiState {
        public static final String TAG = "WANDERING";

        @Override
        public boolean act(boolean enemyVisible, boolean enemyAudible, boolean enemyTouchable, boolean justAlerted) {
            //enemyInFOV && (justAlerted || Random.Int( distance( enemy ) / 2 + enemy.stealth() ) == 0)
            if (enemyVisible || enemyTouchable) {
                lastPos = Constant.Position.NONE;
                enemySeen = true;

                notice();
                state = HUNTING;
                target = enemy.pos();
            } else {
                enemySeen = false;

                int lastLastPos = lastPos;

                lastPos = pos();
                if (target != Constant.Position.NONE && getCloser_new(target)) {
                    if (lastLastPos == pos()) {
                        state = FIDGETING;
                    }
                    //spend( GameTime.TICK * GameTime.TICK / speed(), false );
                    return true;
                } else {
                    target = Dungeon.level.randomDestination();
                    spend_new(GameTime.TICK, false);
                }

            }
            return true;
        }

        @Override
        public String status() {
            return Utils.format("This %s is wandering", name);
        }
    }

    protected class Hunting implements AiState {
        public static final String TAG = "HUNTING";

        @Override
        public boolean act(boolean enemyVisible, boolean enemyAudible, boolean enemyTouchable, boolean justAlerted) {
            enemySeen = enemyVisible;
            if ((enemyVisible || enemyTouchable) && !isCharmedBy(enemy) && canAttack(enemy)) {
                return doAttack(enemy);
            } else {
                if (enemyVisible || enemyTouchable) {
                    target = enemy.pos();
                }

                if (target != Constant.Position.NONE && getCloser_new(target)) {
                    return true;
                } else {
                    spend_new(GameTime.TICK, false);
                    state = WANDERING;
                    target = Dungeon.level.randomDestination();
                    return true;
                }
            }
        }

        @Override
        public String status() {
            return Utils.format("This %s is hunting", name);
        }
    }

    protected class Fleeing implements AiState {
        public static final String TAG = "FLEEING";

        @Override
        public boolean act(boolean enemyVisible, boolean enemyAudible, boolean enemyTouchable, boolean justAlerted) {
            enemySeen = enemyVisible;
            if (enemyVisible) {
                target = enemy.pos();
                //loses target when 0-dist rolls a 6 or greater.
            } else if (1 + Random.Int(Level.distance(pos(), target)) >= 6) {
                target = Constant.Position.NONE;
            }

            int oldPos = pos();
            if (target != Constant.Position.NONE && getFurther(target)) {
                //spend( GameTime.TICK * GameTime.TICK / speed(), false );
                return true;
            } else {
                spend_new(GameTime.TICK, false);
                nowhereToRun();

                return true;
            }
        }

        protected void nowhereToRun() {
        }

        @Override
        public String status() {
            return Utils.format("This %s is fleeing", name);
        }
    }

    protected class Fidgeting implements AiState {
        public static final String TAG = "FIDGETING";

        @Override
        public boolean act(boolean enemyVisible, boolean enemyAudible, boolean enemyTouchable, boolean justAlerted) {
            //enemyInFOV && (justAlerted || Random.Int( distance( enemy ) / 2 + enemy.stealth() ) == 0)
            if (enemyVisible || enemyTouchable) {
                lastPos = Constant.Position.NONE;
                enemySeen = true;

                notice();
                state = HUNTING;
                target = enemy.pos();
            } else {
                enemySeen = false;

                if (Random.Int(32) == 0) {
                    state = WANDERING;
                    return true;
                }
                target = Dungeon.level.randomStep(pos());

                lastPos = pos();
                if (target != Constant.Position.NONE && getCloser_new(target)) {
                    //spend( GameTime.TICK * GameTime.TICK / speed(), false );
                    return true;
                } else {
                    spend_new(GameTime.TICK, false);
                }

            }
            return true;
        }

        @Override
        public String status() {
            return Utils.format("This %s is fidgeting", name);
        }
    }

    protected class Passive implements AiState {
        public static final String TAG = "PASSIVE";

        @Override
        public boolean act(boolean enemyVisible, boolean enemyAudible, boolean enemyTouchable, boolean justAlerted) {
            enemySeen = false;
            spend_new(GameTime.TICK, false);
            return true;
        }

        @Override
        public String status() {
            return Utils.format("This %s is passive", name);
        }
    }

}

