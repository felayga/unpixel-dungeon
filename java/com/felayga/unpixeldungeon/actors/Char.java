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
package com.felayga.unpixeldungeon.actors;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.actors.buffs.hero.EarthImbue;
import com.felayga.unpixeldungeon.actors.buffs.hero.FireImbue;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.negative.Charm;
import com.felayga.unpixeldungeon.actors.buffs.negative.Frost;
import com.felayga.unpixeldungeon.actors.buffs.negative.Hallucination;
import com.felayga.unpixeldungeon.actors.buffs.negative.Held;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.actors.buffs.positive.Barkskin;
import com.felayga.unpixeldungeon.actors.buffs.positive.GasesImmunity;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.buffs.positive.MagicalSleep;
import com.felayga.unpixeldungeon.actors.buffs.positive.MindVision;
import com.felayga.unpixeldungeon.actors.buffs.positive.SeeInvisible;
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.equippableitem.armor.Armor;
import com.felayga.unpixeldungeon.items.equippableitem.armor.glyphs.Bounce;
import com.felayga.unpixeldungeon.items.bags.backpack.Belongings;
import com.felayga.unpixeldungeon.items.consumable.food.Corpse;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.IWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.Weapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ranged.RangedWeapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Characteristic;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.Arrays;
import java.util.HashSet;

public abstract class Char extends Actor {
    public static class Registry {
        private static final SparseArray<Char> fromIndex = new SparseArray<>();

        private static final String CHARREGISTRYINDEX = "charRegistryIndex";

        private static int index = 0;

        public static void save(Bundle bundle) {
            bundle.put(CHARREGISTRYINDEX, index);
        }

        public static void restore(Bundle bundle) {
            index = bundle.getInt(CHARREGISTRYINDEX);
        }

        public static int register(Char c) {
            if (c.charRegistryIndex >= 0) {
                GLog.d("tried to register char, already has index=" + c.charRegistryIndex);

                if (c == Dungeon.hero) {
                    GLog.d("it's fine, I guess");
                    return c.charRegistryIndex;
                } else {
                    return -1;
                }
            }

            c.charRegistryIndex = index;
            index++;

            fromIndex.put(c.charRegistryIndex, c);
            return c.charRegistryIndex;
        }

        public static int unregister(Char c) {
            if (c.charRegistryIndex < 0) {
                GLog.d("tried to unregister char, no index");

                return -1;
            }

            int oldIndex = c.charRegistryIndex;

            fromIndex.remove(c.charRegistryIndex);
            c.charRegistryIndex = -1;

            return oldIndex;
        }

        /*
        public static void polymorph(Char oldChar, Char newChar) {
            int index = unregister(oldChar);

            if (index < 0) {
                GLog.d("tried to polymorph oldChar, no index");
                return;
            }

            newChar.charRegistryIndex = index;
            fromIndex.put(index, newChar);
        }
        */

        public static Char get(int registryIndex) {
            return fromIndex.get(registryIndex);
        }

        public static void register(Level level) {
            for (Mob mob : level.mobs) {
                Char c = mob;
                if (c.charRegistryIndex != -1) {
                    fromIndex.put(c.charRegistryIndex, c);
                } else {
                    GLog.d("TRIED TO REGISTER UNKNOWN CHAR FOUND AT POS=" + c.pos());
                }
            }
        }

        public static void unregister(Level level) {
            for (Mob mob : level.mobs) {
                Char c = mob;
                if (c.charRegistryIndex != -1) {
                    fromIndex.remove(c.charRegistryIndex);
                } else {
                    GLog.d("TRIED TO UNREGISTER UNKNOWN CHAR FOUND AT POS=" + c.pos());
                }
            }
        }
    }

    protected static final String TXT_HIT = "%s hit %s";
    protected static final String TXT_TOUCH = "%s touches %s";
    protected static final String TXT_KILL = "%s killed you...";
    protected static final String TXT_DEFEAT = "%s defeated %s";
    protected static final String TXT_HELD = "being stuck to the %s, %s attack fails";

    private static final String TXT_MISSED = "%s %s %s attack";

    private static final String TXT_OUT_OF_PARALYSIS = "The pain snapped %s out of paralysis";

    private int _pos = Constant.Position.NONE;

    public int pos() {
        return _pos;
    }

    public void pos(int newPos) {
        if (_pos != newPos) {
            int lastPos = _pos;

            _pos = newPos;
            move(lastPos, _pos);
        }
    }

    public int defenseMundane = 10;
    public int defenseMagical = 0;
    public int resistanceMagical = 0;

    public CharSprite sprite;
    public CharSprite originalSprite;

    public String name = "mob";
    public String originalName;

    public int HT;
    public int HP;
    public int hpPerLevel;

    public int MT;
    public int MP;
    public int mpPerLevel;

    public int viewDistance = 8;
    public int hearDistance = 12;
    public int touchDistance = 1;

    public int weight;
    public int nutrition;
    public long characteristics;
    public long corpseEffects;
    public long corpseResistances;

    public Belongings belongings;

    private long _movementSpeed = GameTime.TICK;
    private long _attackSpeed = GameTime.TICK;

    public int flying = 0;
    public int paralysed = 0;
    public int invisible = 0;
    public int stealthy = 0;

    public int level;

    private int posBlockStoredPos = Constant.Position.NONE;
    private boolean posBlockFlagLosBlock = false;
    private boolean posBlockFlagPassable = false;
    private boolean posBlockFlagPathable = false;


    private int charRegistryIndex = -1;

    public int charRegistryIndex() {
        return charRegistryIndex;
    }

    private int attribute[] = new int[]{10, 10, 10};
    private int attributeDamage[] = new int[]{0, 0, 0};

    public void initAttributes(int[] attributes) {
        initAttributes(attributes, new int[]{0, 0, 0});
    }

    public void initAttributes(int[] attributes, int[] attributeDamage) {
        int oldstrcon = this.attribute[AttributeType.STRCON.value];
        int oldintwis = this.attribute[AttributeType.INTWIS.value];

        this.attribute[AttributeType.STRCON.value] = attributes[AttributeType.STRCON.value];
        this.attribute[AttributeType.DEXCHA.value] = attributes[AttributeType.DEXCHA.value];
        this.attribute[AttributeType.INTWIS.value] = attributes[AttributeType.INTWIS.value];

        this.attributeDamage[AttributeType.STRCON.value] = attributeDamage[AttributeType.STRCON.value];
        this.attributeDamage[AttributeType.DEXCHA.value] = attributeDamage[AttributeType.DEXCHA.value];
        this.attributeDamage[AttributeType.INTWIS.value] = attributeDamage[AttributeType.INTWIS.value];

        int strcon = this.attribute[AttributeType.STRCON.value];
        int intwis = this.attribute[AttributeType.INTWIS.value];

        adjustHP(oldstrcon, strcon);
        adjustMP(oldintwis, intwis);
    }

    private void adjustHP(int oldstrcon, int strcon) {
        oldstrcon = getAttributeModifier(oldstrcon);
        strcon = getAttributeModifier(strcon);

        if (oldstrcon != strcon) {
            int value = (strcon - oldstrcon) * (level - 1);
            HP += value;
            HT += value;
        }
    }

    private void adjustMP(int oldintwis, int intwis) {
        oldintwis = getAttributeModifier(oldintwis);
        intwis = getAttributeModifier(intwis);

        if (oldintwis != intwis) {
            int value = (intwis - oldintwis) * (level - 1);
            MP += value;
            MT += value;
        }
    }

    public int STRCON() {
        return attribute[AttributeType.STRCON.value];
    }

    public int DEXCHA() {
        return attribute[AttributeType.DEXCHA.value];
    }

    public int INTWIS() {
        return attribute[AttributeType.INTWIS.value];
    }

    public int STRCON_damage() {
        return attributeDamage[AttributeType.STRCON.value];
    }

    public int DEXCHA_damage() {
        return attributeDamage[AttributeType.DEXCHA.value];
    }

    public int INTWIS_damage() {
        return attributeDamage[AttributeType.INTWIS.value];
    }


    public void increaseAttribute(AttributeType type, int amount) {
        int attribute = this.attribute[type.value];
        int damage = this.attributeDamage[type.value];

        attribute += amount;

        if (attribute + damage > Constant.Attribute.MAXIMUM) {
            int overflow = attribute + damage - Constant.Attribute.MAXIMUM;

            if (damage >= overflow) {
                damage -= overflow;
                overflow = 0;
            } else if (damage < overflow) {
                overflow -= damage;
                damage = 0;
            }

            attribute -= overflow;
        }

        if (attribute < 1) {
            attribute = 1;
        }

        switch (type) {
            case STRCON:
                adjustHP(this.attribute[type.value], attribute);
                break;
            case INTWIS:
                adjustMP(this.attribute[type.value], attribute);
                break;
        }

        this.attribute[type.value] = attribute;
        this.attributeDamage[type.value] = damage;
    }

    public void damageAttribute(AttributeType type, int amount) {
        int attribute = this.attribute[type.value];
        int damage = this.attributeDamage[type.value];

        if (amount > attribute - 1) {
            amount = attribute - 1;
        }

        attribute -= amount;
        damage += amount;

        switch (type) {
            case STRCON:
                adjustHP(this.attribute[type.value], attribute);
                break;
            case INTWIS:
                adjustMP(this.attribute[type.value], attribute);
                break;
        }

        this.attribute[type.value] = attribute;
        this.attributeDamage[type.value] = damage;
    }

    public void undamageAttributes() {
        undamageAttribute(AttributeType.STRCON);
        undamageAttribute(AttributeType.DEXCHA);
        undamageAttribute(AttributeType.INTWIS);
    }

    public void undamageAttribute(AttributeType type) {
        int oldattribute = attribute[type.value];

        attribute[type.value] += attributeDamage[type.value];
        attributeDamage[type.value] = 0;

        switch (type) {
            case STRCON:
                adjustHP(oldattribute, this.attribute[type.value]);
                break;
            case INTWIS:
                adjustMP(oldattribute, this.attribute[type.value]);
                break;
        }
    }


    public boolean tryIncreaseAttribute(AttributeType type, int value) {
        int oldattribute = attribute[type.value];
        if (attribute[type.value] >= Constant.Attribute.MAXIMUM) {
            attribute[type.value] = Constant.Attribute.MAXIMUM;
            return false;
        } else if (attribute[type.value] + value >= Constant.Attribute.MAXIMUM) {
            attribute[type.value] = Constant.Attribute.MAXIMUM;
        } else {
            attribute[type.value] += value;
        }
        switch (type) {
            case STRCON:
                adjustHP(oldattribute, this.attribute[type.value]);
                break;
            case INTWIS:
                adjustMP(oldattribute, this.attribute[type.value]);
                break;
        }
        return true;
    }

    public int getAttributeModifier(AttributeType type) {
        return getAttributeModifier(attribute[type.value]);
    }

    public int getAttributeModifier(int attribute) {
        return (attribute - 8) / 2;
    }

    public long movementSpeed() {
        long bestSlow = GameTime.TICK;
        long bestFast = GameTime.TICK;

        for (Buff buff : buffs()) {
            if (buff instanceof ISpeedModifierBuff) {
                ISpeedModifierBuff speedBuff = (ISpeedModifierBuff) buff;
                long modifier = speedBuff.movementModifier();

                if (modifier < GameTime.TICK && modifier < bestFast) {
                    bestFast = modifier;
                } else if (modifier > GameTime.TICK && modifier > bestSlow) {
                    bestSlow = modifier;
                }
            }
        }

        long armorDelay = GameTime.TICK;

        EquippableItem item = belongings.armor();

        if (item instanceof Armor) {
            Armor armor = (Armor) item;

            armorDelay = armor.speedModifier;
        }


        return _movementSpeed * bestFast / GameTime.TICK * bestSlow / GameTime.TICK * armorDelay / GameTime.TICK;
    }

    public void movementSpeed(long newMovementSpeed) {
        _movementSpeed = newMovementSpeed;
    }

    public long attackSpeed() {
        long bestSlow = GameTime.TICK;
        long bestFast = GameTime.TICK;

        for (Buff buff : buffs()) {
            if (buff instanceof ISpeedModifierBuff) {
                ISpeedModifierBuff speedBuff = (ISpeedModifierBuff) buff;
                long modifier = speedBuff.attackModifier();

                if (modifier < GameTime.TICK && modifier < bestFast) {
                    bestFast = modifier;
                } else if (modifier > GameTime.TICK && modifier > bestSlow) {
                    bestSlow = modifier;
                }
            }
        }

        long weaponDelay = GameTime.TICK;

        EquippableItem item = belongings.weapon();

        if (item instanceof Weapon) {
            Weapon weapon = (Weapon) item;

            weaponDelay = weapon.delay_new;
        }

        return _attackSpeed * bestFast / GameTime.TICK * bestSlow / GameTime.TICK * weaponDelay / GameTime.TICK;
    }

    public void attackSpeed(long newAttackSpeed) {
        this._attackSpeed = newAttackSpeed;
    }

    public boolean canBreathe() {
        if ((characteristics & Characteristic.NonBreather.value) != 0) {
            return false;
        }

        if (buff(GasesImmunity.class) != null) {
            return false;
        }

        return true;
    }

    public boolean flying() {
        return flying > 0;
    }

    protected void flying(boolean state) {
        if (state) {
            flying++;
        } else {
            flying--;
        }
    }

    public boolean canPassDoors() {
        if (isEthereal()) {
            return true;
        }
        if (isAmorphous()) {
            return true;
        }

        return (characteristics & Characteristic.Humanoid.value) != 0;
    }

    public boolean isEthereal() {
        return (characteristics & Characteristic.Ethereal.value) != 0;
    }

    public boolean isAmorphous() {
        return (characteristics & Characteristic.Amorphous.value) != 0;
    }

    private HashSet<Buff> buffs = new HashSet<Buff>();

    public Char(int level) {
        this.level = level;

        belongings = new Belongings(this);
        nutrition = 0;
        corpseEffects = CorpseEffect.None.value;

        initAttributes(new int[]{10, 10, 10});
    }

    public final boolean fxSpriteVisible() {
        if ((characteristics & Characteristic.AlwaysVisible.value) != 0) {
            return true;
        }

        if (invisible > 0 && Dungeon.hero.buff(SeeInvisible.class) == null) {
            return false;
        }
        return Dungeon.visible[pos()];
    }

    @Override
    protected boolean act() {
        belongings.decay(time(), true, false);
        Dungeon.level.updateFieldOfSenses(this);
        //posBlockInitializationCheck();
        return false;
    }

    private static final String POS = "pos";
    private static final String ATTRIBUTE0 = "attribute0";
    private static final String ATTRIBUTE1 = "attribute1";
    private static final String ATTRIBUTE2 = "attribute2";
    private static final String ATTRIBUTE0DAMAGE = "attribute0Damage";
    private static final String ATTRIBUTE1DAMAGE = "attribute1Damage";
    private static final String ATTRIBUTE2DAMAGE = "attribute2Damage";
    private static final String TAG_HP = "HP";
    private static final String TAG_HT = "HT";
    private static final String TAG_MP = "MP";
    private static final String TAG_MT = "MT";
    private static final String BUFFS = "buffs";
    private static final String CORPSEEFFECTS = "corpseEffects";
    private final static String POSBLOCKSTOREDPOS = "posBlockStoredPos";
    private final static String POSBLOCKFLAGLOSBLOCK = "posBlockFlagLosBlock";
    private final static String POSBLOCKFLAGPASSABLE = "posBlockFlagPassable";
    private final static String POSBLOCKFLAGPATHABLE = "posBlockFlagPathable";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(ATTRIBUTE0, attribute[0]);
        bundle.put(ATTRIBUTE1, attribute[1]);
        bundle.put(ATTRIBUTE2, attribute[2]);

        bundle.put(ATTRIBUTE0DAMAGE, attributeDamage[0]);
        bundle.put(ATTRIBUTE1DAMAGE, attributeDamage[1]);
        bundle.put(ATTRIBUTE2DAMAGE, attributeDamage[2]);

        bundle.put(POS, _pos);
        bundle.put(TAG_HP, HP);
        bundle.put(TAG_HT, HT);
        bundle.put(TAG_MP, MP);
        bundle.put(TAG_MT, MT);
        bundle.put(BUFFS, buffs);
        bundle.put(CORPSEEFFECTS, corpseEffects);

        bundle.put(POSBLOCKSTOREDPOS, posBlockStoredPos);
        bundle.put(POSBLOCKFLAGLOSBLOCK, posBlockFlagLosBlock);
        bundle.put(POSBLOCKFLAGPASSABLE, posBlockFlagPassable);
        bundle.put(POSBLOCKFLAGPATHABLE, posBlockFlagPathable);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        initAttributes(new int[]{
                bundle.getInt(ATTRIBUTE0),
                bundle.getInt(ATTRIBUTE1),
                bundle.getInt(ATTRIBUTE2)
        }, new int[]{
                bundle.getInt(ATTRIBUTE0DAMAGE),
                bundle.getInt(ATTRIBUTE1DAMAGE),
                bundle.getInt(ATTRIBUTE2DAMAGE)
        });

        _pos = bundle.getInt(POS);
        HP = bundle.getInt(TAG_HP);
        HT = bundle.getInt(TAG_HT);
        MP = bundle.getInt(TAG_MP);
        MT = bundle.getInt(TAG_MT);
        corpseEffects = bundle.getLong(CORPSEEFFECTS);

        for (Bundlable b : bundle.getCollection(BUFFS)) {
            if (b != null) {
                Buff buff = (Buff)b;
                buff.restore(this);
            }
        }

        posBlockStoredPos = bundle.getInt(POSBLOCKSTOREDPOS);
        posBlockFlagLosBlock = bundle.getBoolean(POSBLOCKFLAGLOSBLOCK);
        posBlockFlagPassable = bundle.getBoolean(POSBLOCKFLAGPASSABLE);
        posBlockFlagPathable = bundle.getBoolean(POSBLOCKFLAGPATHABLE);
    }

    protected boolean canAttack(Char enemy) {
        return Level.canReach(pos(), enemy.pos());
    }

    protected boolean shouldTouch(Char enemy) {
        return false;
    }

    public enum VisibilityState {
        Visible,
        NotVisible,
        Invisible
    }

    public VisibilityState canSee(Char enemy) {
        if (buff(MindVision.class) != null) {
            if ((enemy.characteristics & Characteristic.Brainless.value) == 0) {
                return VisibilityState.NotVisible;
            }
        }

        if (Dungeon.visible[enemy.pos()]) {
            if (enemy.buff(Invisibility.class) != null) {
                if (buff(SeeInvisible.class) == null) {
                    return VisibilityState.Invisible;
                }
            }

            return VisibilityState.Visible;
        } else {
            return VisibilityState.NotVisible;
        }
    }

    public boolean attack(IWeapon weapon, Char enemy) {
        return attack(weapon, false, enemy);
    }

    public boolean attack(IWeapon weapon, boolean ranged, Char enemy) {
        boolean visibleFight = Dungeon.visible[enemy.pos()];
        boolean touchableFight = Dungeon.touchable[enemy.pos()];
        boolean audibleFight = Dungeon.audible[enemy.pos()];
        boolean retval = false;

        boolean touch = shouldTouch(enemy);
        Held heldBuff = buff(Held.class);

        if (heldBuff != null && heldBuff.ownerRegistryIndex() == enemy.charRegistryIndex()) {
            heldBuff = null;
        }

        if (heldBuff != null) {
            Char host = Char.Registry.get(heldBuff.ownerRegistryIndex());
            String hostname;
            if (host != null && visibleFight) {
                hostname = host.name;
            } else {
                hostname = "something";
            }

            if (this == Dungeon.hero) {
                GLog.n(TXT_HELD, hostname, "your");
            } else if (host == Dungeon.hero) {
                GLog.n(TXT_HELD, "you", name + "'s");
            } else {
                GLog.n(TXT_HELD, hostname, name + "'s");
            }
        } else if (tryHit(this, weapon, ranged, enemy, touch)) {
            if (!touch) {
                retval = false;
                if (canSee(enemy) != VisibilityState.Visible) {
                    if (Random.Int(Constant.Chance.CHAR_MISS_INVISIBLE_ENEMY) == 0) {
                        if (this == Dungeon.hero) {
                            GLog.n("You swing wildly and miss!");
                        } else {
                            switch (Random.Int(3)) {
                                case 0:
                                    GLog.w("The " + enemy.name + " swings wildly and misses!");
                                    break;
                                case 1:
                                    GLog.w("The " + enemy.name + " strikes at thin air!");
                                    break;
                                default:
                                    GLog.w("The " + enemy.name + " attacks a spot near you!");
                                    break;
                            }
                        }
                        retval = true;
                    }
                }

                if (!retval) {
                    hit(weapon, ranged, enemy, visibleFight, touchableFight, audibleFight);
                }
            } else {
                if (visibleFight) {
                    GLog.i(TXT_TOUCH, name, enemy.name);
                } else if (touchableFight) {
                    GLog.i(TXT_TOUCH, "something", enemy.name);
                }

                touch(enemy, visibleFight, touchableFight);
            }

            retval = true;
        } else {
            if (visibleFight) {
                String defense = enemy.defenseVerb();
                enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                if (this == Dungeon.hero) {
                    GLog.i(TXT_MISSED, enemy.name, defense, "your");
                } else {
                    GLog.i(TXT_MISSED, enemy.name, defense, name + "'s");
                }
            }

            if (audibleFight) {
                Sample.INSTANCE.play(Assets.SND_MISS);
            }

            retval = false;
        }

        if (this.flying > 0) {
            Bounce.proc(32767, this, enemy);
        }
        if (enemy.flying > 0) {
            Bounce.proc(32767, enemy, this);
        }

        return retval;
    }

    protected void hit(IWeapon weapon, boolean ranged, Char enemy, boolean visible, boolean touchable, boolean audible) {
        if (visible) {
            GLog.i(TXT_HIT, name, enemy.name);
        } else if (touchable) {
            if (enemy == Dungeon.hero) {
                GLog.i(TXT_HIT, "something", enemy.name);
            } else if (this == Dungeon.hero) {
                GLog.i(TXT_HIT, name, "something");
            }
        }

        int effectiveDamage;

        if (weapon != null) {
            effectiveDamage = weapon.damageRoll();
            effectiveDamage += getAttributeModifier(weapon.damageAttribute());
        } else {
            effectiveDamage = Random.IntRange(1, 4);
            effectiveDamage += getAttributeModifier(AttributeType.STRCON);
        }

        if (effectiveDamage < 0) {
            effectiveDamage = 0;
        }

        if (weapon != null) {
            effectiveDamage = weapon.proc(this, ranged, enemy, effectiveDamage);
        }
        effectiveDamage = enemy.defenseProc(this, effectiveDamage);

        if (audible) {
            Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
        }

        // If the enemy is already dead, interrupt the attack.
        // This matters as defence procs can sometimes inflict self-damage, such as armor glyphs.
        if (enemy.isAlive()) {
            if (effectiveDamage < 0) {
                effectiveDamage = 0;
            }

            //TODO: consider revisiting this and shaking in more cases.
            float shake = 0f;
            if (enemy == Dungeon.hero) {
                shake = effectiveDamage / (enemy.HT / 4);
            }

            if (shake > 1f) {
                Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
            }

            if (weapon != null) {
                enemy.damage(effectiveDamage, weapon.damageType(), this, weapon.self());
            } else {
                Item gloves = belongings.gloves();
                if (gloves != null) {
                    enemy.damage(effectiveDamage, MagicType.Mundane, this, gloves);
                } else {
                    Item ring = null;

                    Item ring1 = belongings.ring1();
                    Item ring2 = belongings.ring2();
                    if (ring1 != null && ring2 != null) {
                        ring = ring1.material().value > ring2.material().value ? ring1 : ring2;
                    } else if (ring1 != null) {
                        ring = ring1;
                    } else if (ring2 != null) {
                        ring = ring2;
                    }

                    enemy.damage(effectiveDamage, MagicType.Mundane, this, ring);
                }
            }

            if ((enemy.characteristics & Characteristic.SilverVulnerable.value) != 0) {
                Material material = Material.Flesh;

                EquippableItem item;

                if (weapon != null) {
                    material = weapon.material();
                } else if ((item = belongings.gloves()) != null) {
                    material = item.material();
                }
            }

            if (buff(FireImbue.class) != null) {
                buff(FireImbue.class).proc(enemy);
            }
            if (buff(EarthImbue.class) != null) {
                buff(EarthImbue.class).proc(enemy);
            }

            enemy.sprite.bloodBurstA(sprite.center(), effectiveDamage);
            enemy.sprite.flash();

            if (!enemy.isAlive() && visible) {
                if (enemy == Dungeon.hero) {
                    if (Bestiary.isUnique(this)) {
                        Dungeon.fail(Utils.format(ResultDescriptions.UNIQUE, name));
                    } else {
                        Dungeon.fail(Utils.format(ResultDescriptions.MOB, Utils.indefinite(name)));
                    }

                    GLog.n(TXT_KILL, name);

                } else {
                    GLog.i(TXT_DEFEAT, name, enemy.name);
                }
            }
        }
    }

    protected void touch(Char enemy, boolean visible, boolean touchable) {
        if (visible) {
            GLog.i(TXT_TOUCH, name, enemy.name);
        } else if (touchable) {
            if (enemy == Dungeon.hero) {
                GLog.i(TXT_TOUCH, "something", enemy.name);
            } else if (this == Dungeon.hero) {
                GLog.i(TXT_TOUCH, name, "something");
            }
        }
    }

    public void busy() {
    }

    public boolean zap() {
        //todo: zap behavior
        return false;
    }

    public static boolean tryHit(Char attacker, IWeapon weapon, boolean thrown, Char defender, boolean touch) {
        Invisibility.dispelAttack(attacker);

        int roll = Random.IntRange(1, 20);
        int skill = attacker.attackSkill(weapon, thrown, defender);
        int defense = defender.defenseMundane(attacker, touch);

        GLog.d("roll=" + roll + " + " + skill + " >= " + defense + "?");

        if (defender == Dungeon.hero) {
            if (defender.buff(Blindness.class) != null) {
                Dungeon.level.warnings.add(attacker.pos());
            } else if (attacker.invisible > 0) {
                Dungeon.level.warnings.add(attacker.pos(), false);
            }
        }

        return roll + skill >= defense;

        /*
		float acuRoll = Random.Float( attacker.attackSkill( defender ) );
		float defRoll = Random.Float( defender.defenseSkill( attacker ) );
		if (attacker.buff(Bless.class) != null) acuRoll *= 1.20f;
		if (defender.buff(Bless.class) != null) defRoll *= 1.20f;
		return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
		*/
    }

    public int attackSkill(IWeapon weapon, boolean thrown, Char target) {
        int retval = 0;

        if (buff(Blindness.class) != null || target.buff(Invisibility.class) != null) {
            GLog.d("blind attack");
            retval -= 2;
        }

        if (Level.distance(pos(), target.pos()) <= 1) {
            if (thrown) {
                GLog.d("thrown weapon in melee");
                retval -= 4;
            } else {
                if (weapon instanceof RangedWeapon) {
                    GLog.d("ranged weapon in melee");
                    retval -= 4;
                }
            }
        }

        return retval;
    }

    public int defenseMundane(Char enemy, boolean touch) {
		/*
		//todo: ring defense buff shit
		for (Buff buff : buffs( RingOfEvasion.Evasion.class )) {
			defense += ((RingOfEvasion.Evasion)buff).effectiveLevel;
		}
		*/
        int armor = belongings.getArmor(getAttributeModifier(AttributeType.DEXCHA), touch);

        Barkskin barkskin = buff(Barkskin.class);
        if (barkskin != null) {
            armor = Math.max(armor, barkskin.level());
        }

        return defenseMundane + armor;
    }


    public final int defenseMagical(Char enemy, MagicType type) {
        int retval = getResistance(type);

        if (type != MagicType.Mundane) {
            retval += belongings.getResistance();
        }

        return Math.max(Math.min(retval, 9), -9);
    }

    protected int getResistance(MagicType type) {
        if ((resistanceMagical & type.value) != 0) {
            return 6;
        } else {
            return defenseMagical;
        }
    }

    public String defenseVerb() {
        return "dodged";
    }

    public int defenseProc(Char enemy, int damage) {
        return damage;
    }

    public int damage(int dmg, MagicType type, Char source, Item weapon) {
        if (HP <= 0 || dmg < 0) {
            return 0;
        }
        if (this.buff(Frost.class) != null) {
            Buff.detach(this, Frost.class);
        }
        if (this.buff(MagicalSleep.class) != null) {
            Buff.detach(this, MagicalSleep.class);
        }

        int modifier = defenseMagical(source, type);
        dmg = (int) Math.round(Math.sqrt((9.0 - (double) modifier) / 9.0) * (double) dmg);

        //else if (resistances().contains( srcClass )) {
        //	dmg = Random.IntRange( 0, dmg );
        //}

        if (buff(Paralysis.class) != null) {
            if (Random.Int(dmg) >= Random.Int(HP)) {
                Buff.detach(this, Paralysis.class);
                GLog.i(TXT_OUT_OF_PARALYSIS, name);
            }
        }

        HP -= dmg;
        if (dmg > 0 || source != null) {
            sprite.showStatus(HP > HT / 2 ?
                            CharSprite.WARNING :
                            CharSprite.NEGATIVE,
                    Integer.toString(dmg));
        }
        if (HP <= 0) {
            die(source);
        }

        return dmg;
    }


    public void polymorph(Class<? extends Char> targetClass) {
        Char replacement = null;
        try {
            replacement = targetClass.newInstance();
        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {

        }

        if (replacement != null) {
            //Registry.polymorph(this, replacement);

            int[] swap = Arrays.copyOf(this.attribute, this.attribute.length);
            int[] swap2 = Arrays.copyOf(this.attributeDamage, this.attributeDamage.length);

            initAttributes(replacement.attribute);
            replacement.attribute = swap;


            int flying = replacement.flying;
            replacement.flying = -flying;

            this.flying += flying;


            int swapViewDistance = this.viewDistance;
            this.viewDistance = replacement.viewDistance;
            replacement.viewDistance = swapViewDistance;

            int swapHearDistance = this.hearDistance;
            this.hearDistance = replacement.hearDistance;
            replacement.hearDistance = swapHearDistance;

            int swapTouchDistance = this.touchDistance;
            this.touchDistance = replacement.touchDistance;
            replacement.touchDistance = swapTouchDistance;


        }
    }

    public boolean shoot(Char enemy, MissileWeapon wep) {
        boolean result = attack(wep, true, enemy);
        Invisibility.dispelAttack(this);

        //todo: strange bug here needs to be traced: ready state failing in the chain somewhere occasionally if: enemy dies (every occurrence of bug), player had one ammo item left (most occurrences) or player had more than one ammo item left (rare occurrences)
        //hasn't occurred recently, various overhauls of logic probably took care of it

        return result;
    }

    public void destroy(Actor cause) {
        HP = 0;
        Actor.remove(this);
    }

    protected boolean shouldDropCorpse() {
        return ((Characteristic.Corpseless.value & characteristics) == 0) && Random.Int(Constant.Chance.CORPSE_DROP) == 0;
    }

    public void die(Actor src) {
        if (nutrition > 0 && shouldDropCorpse()) {
            Dungeon.level.drop(new Corpse(this), pos());
        } else {
            //GLog.d("no corpse because nutrition=" + nutrition);
        }

        dropAll();

        if ((characteristics & Characteristic.PositionBlocking.value) != 0) {
            if (posBlockStoredPos >= 0) {
                Level.losBlocking[posBlockStoredPos] = posBlockFlagLosBlock;
                Level.passable[posBlockStoredPos] = posBlockFlagPassable;
                Level.pathable[posBlockStoredPos] = posBlockFlagPathable;

                GameScene.updateMap(posBlockStoredPos);

                posBlockStoredPos = Constant.Position.NONE;
            }
        }

        destroy(src);
        sprite.die();
    }

    protected void dropAll() {
        belongings.dropAll(pos(), 0);
    }

    public boolean isAlive() {
        return HP > 0;
    }

    public HashSet<Buff> buffs() {
        return buffs;
    }

    @SuppressWarnings("unchecked")
    public <T extends Buff> HashSet<T> buffs(Class<T> c) {
        HashSet<T> filtered = new HashSet<T>();
        for (Buff b : buffs) {
            if (c.isInstance(b)) {
                filtered.add((T) b);
            }
        }
        return filtered;
    }

    @SuppressWarnings("unchecked")
    public <T extends Buff> T buff(Class<T> c) {
        for (Buff b : buffs) {
            if (c.isInstance(b)) {
                return (T) b;
            }
        }
        return null;
    }

    public boolean isCharmedBy(Char ch) {
        for (Buff b : buffs) {
            if (b instanceof Charm) {
                Charm charm = (Charm) b;
                if (charm.ownerRegistryIndex() == ch.charRegistryIndex()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void add(Buff buff) {
        buffs.add(buff);
        Actor.add(buff);

        if (sprite != null)
            switch (buff.type) {
                case POSITIVE:
                    sprite.showStatus(CharSprite.POSITIVE, buff.toString());
                    break;
                case NEGATIVE:
                    sprite.showStatus(CharSprite.NEGATIVE, buff.toString());
                    break;
                case NEUTRAL:
                    sprite.showStatus(CharSprite.NEUTRAL, buff.toString());
                    break;
                case SILENT:
                default:
                    break; //show nothing
            }

    }

    public void remove(Buff buff) {
        buffs.remove(buff);
        Actor.remove(buff);
    }

    /*
    public void remove(Class<? extends Buff> buffClass) {
        for (Buff buff : buffs(buffClass)) {
            remove(buff);
        }
    }
    */

    @Override
    protected void onRemove() {
        for (Buff buff : buffs.toArray(new Buff[0])) {
            buff.detach();
        }
    }

    public void updateSpriteState() {
        for (Buff buff : buffs) {
            buff.fx(true);
        }
    }

    @Override
    protected void onAdd() {
        super.onAdd();

        posBlockInitializationCheck();
    }

    private void posBlockInitializationCheck() {
        if ((characteristics & Characteristic.PositionBlocking.value) != 0) {
            GLog.d("losBlockInitializationCheck()");
            int pos = pos();

            if (posBlockStoredPos < 0) {
                posBlockStoredPos = pos;
                GLog.d("init posBlockStoredPos x=" + (pos % Level.WIDTH) + " y=" + (pos / Level.WIDTH));

                posBlockFlagLosBlock = Level.losBlocking[pos];
                posBlockFlagPassable = Level.passable[pos];
                posBlockFlagPathable = Level.pathable[pos];

                Level.losBlocking[pos] = true;
                Level.passable[pos] = false;
                Level.pathable[pos] = false;

                GameScene.updateMap(pos);
            }
        }
    }

    public void move(int step) {
        int oldPos = pos();

        if (Level.canStep(step, oldPos, Level.diagonal) && buff(Vertigo.class) != null) {
            sprite.interruptMotion();
            int newPos = oldPos + Level.NEIGHBOURS8[Random.Int(8)];
            if (!(Level.passable[newPos] || Level.pathable[newPos] || Level.avoid[newPos]) || Actor.findChar(newPos) != null)
                return;
            else {
                sprite.move(oldPos, newPos);
                step = newPos;
            }
        }

		/*
		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}
		*/

        if (_pos != step) {
            super.move(_pos, step);
            _pos = step;

            if ((characteristics & Characteristic.PositionBlocking.value) != 0) {
                if (posBlockStoredPos >= 0) {
                    GLog.d("set old posBlockStoredPos x=" + (posBlockStoredPos % Level.WIDTH) + " y=" + (posBlockStoredPos / Level.WIDTH));

                    Level.losBlocking[posBlockStoredPos] = posBlockFlagLosBlock;
                    Level.passable[posBlockStoredPos] = posBlockFlagPassable;
                    Level.pathable[posBlockStoredPos] = posBlockFlagPathable;

                    GameScene.updateMap(posBlockStoredPos);
                }

                posBlockStoredPos = step;
                GLog.d("set new posBlockStoredPos x=" + (posBlockStoredPos % Level.WIDTH) + " y=" + (posBlockStoredPos / Level.WIDTH));

                posBlockFlagLosBlock = Level.losBlocking[step];
                posBlockFlagPassable = Level.passable[step];
                posBlockFlagPathable = Level.pathable[step];

                Level.losBlocking[step] = true;
                Level.passable[step] = false;
                Level.pathable[step] = false;

                GameScene.updateMap(step);

                Dungeon.observe();
            }
        }
		/*
		if (flying && Dungeon.level.map[pos] == Terrain.DOOR) {
			Door.enter( pos );
		}
		*/
        if (this != Dungeon.hero) {
            sprite.visible = fxSpriteVisible();
        }
    }

    public int distance(Char other) {
        return Level.distance(pos(), other.pos());
    }

    public void onMotionComplete() {
        next();

        /*
        if ((characteristics & Characteristic.PositionBlocking.value) != 0) {
            int pos = pos();

            Level.losBlocking[pos] = true;
            GameScene.updateMap(pos);
            Dungeon.observe();
        }
        */
    }

    public void onAttackComplete() {
        next();
    }

    public void onZapComplete() {
        next();
    }

    public void onOperateComplete() {
        next();

    }

}
