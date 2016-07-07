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
package com.felayga.unpixeldungeon.actors;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.ISpeedModifierBuff;
import com.felayga.unpixeldungeon.actors.buffs.hero.EarthImbue;
import com.felayga.unpixeldungeon.actors.buffs.hero.FireImbue;
import com.felayga.unpixeldungeon.actors.buffs.negative.Charm;
import com.felayga.unpixeldungeon.actors.buffs.negative.Frost;
import com.felayga.unpixeldungeon.actors.buffs.negative.Held;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.buffs.positive.MagicalSleep;
import com.felayga.unpixeldungeon.actors.hero.Belongings;
import com.felayga.unpixeldungeon.actors.mobs.Bestiary;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.armor.glyphs.Bounce;
import com.felayga.unpixeldungeon.items.food.Corpse;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.weapon.ranged.RangedWeapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.CorpseEffect;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.HashSet;

public abstract class Char extends Actor {

    protected static final String TXT_HIT               = "%s hit %s";
    protected static final String TXT_TOUCH             = "%s touches %s";
    protected static final String TXT_KILL              = "%s killed you...";
    protected static final String TXT_DEFEAT            = "%s defeated %s";
    protected static final String TXT_HELD              = "being stuck to the %s, %s attack fails";

    private static final String TXT_MISSED              = "%s %s %s attack";

    private static final String TXT_OUT_OF_PARALYSIS    = "The pain snapped %s out of paralysis";

    public int pos = 0;

    public int defenseMundane = 10;
    public int defenseMagical = 0;
    public int immunityMagical = 0;

    public CharSprite sprite;

    public String name = "mob";

    public int HT;
    public int HP;

    private int attribute[];

    public int STRCON() {
        return attribute[AttributeType.STRCON.value];
    }

    public int DEXCHA() {
        return attribute[AttributeType.DEXCHA.value];
    }

    public int INTWIS() {
        return attribute[AttributeType.INTWIS.value];
    }

    private int attributeDamage[];

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

        if (attribute + damage > 25) {
            int overflow = attribute + damage - 25;

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

        this.attribute[type.value] = attribute;
        this.attributeDamage[type.value] = damage;
    }

    public void undamageAttributes() {
        undamageAttribute(AttributeType.STRCON);
        undamageAttribute(AttributeType.DEXCHA);
        undamageAttribute(AttributeType.INTWIS);
    }

    public void undamageAttribute(AttributeType type) {
        attribute[type.value] += attributeDamage[type.value];
        attributeDamage[type.value] = 0;
    }


    public int weight;
    public int nutrition;
    public long corpseEffects;
    public long corpseResistances;

    public int getAttributeModifier(AttributeType type) {
        return (attribute[type.value] - 8) / 2;
    }

    public Belongings belongings;

    private long _movementSpeed = GameTime.TICK;

    public long movementSpeed() {
        long bestSlow = GameTime.TICK;
        long bestFast = GameTime.TICK;

        HashSet<Buff> buffs = buffs();

        for(Buff buff : buffs) {
            if (buff instanceof ISpeedModifierBuff) {
                ISpeedModifierBuff speedBuff = (ISpeedModifierBuff)buff;
                long modifier = speedBuff.movementModifier();

                if (modifier < GameTime.TICK && modifier < bestFast) {
                    bestFast = modifier;
                }
                else if (modifier > GameTime.TICK && modifier > bestSlow) {
                    bestSlow = modifier;
                }
            }
        }

        long armorDelay = GameTime.TICK;

        EquippableItem item = belongings.armor();

        if (item instanceof Armor) {
            Armor armor = (Armor)item;

            armorDelay = armor.speedModifier;
        }


        return _movementSpeed * bestFast / GameTime.TICK * bestSlow / GameTime.TICK * armorDelay / GameTime.TICK;
    }

    public void movementSpeed(long newMovementSpeed) {
        _movementSpeed = newMovementSpeed;
    }

    private long _attackSpeed = GameTime.TICK;

    public long attackSpeed() {
        long bestSlow = GameTime.TICK;
        long bestFast = GameTime.TICK;

        HashSet<Buff> buffs = buffs();

        for(Buff buff : buffs) {
            if (buff instanceof ISpeedModifierBuff) {
                ISpeedModifierBuff speedBuff = (ISpeedModifierBuff)buff;
                long modifier = speedBuff.attackModifier();

                if (modifier < GameTime.TICK && modifier < bestFast) {
                    bestFast = modifier;
                }
                else if (modifier > GameTime.TICK && modifier > bestSlow) {
                    bestSlow = modifier;
                }
            }
        }

        long weaponDelay = GameTime.TICK;

        EquippableItem item = belongings.weapon();

        if (item instanceof KindOfWeapon) {
            KindOfWeapon weapon = (KindOfWeapon)item;

            weaponDelay = weapon.delay_new;
        }

        return _attackSpeed * bestFast / GameTime.TICK * bestSlow / GameTime.TICK * weaponDelay / GameTime.TICK;
    }

    public void attackSpeed(long newAttackSpeed) {
        this._attackSpeed = newAttackSpeed;
    }

    public int paralysed = 0;
    public boolean flying = false;
    public int invisible = 0;

    public int viewDistance = 8;

    public boolean canOpenDoors = false;
    public boolean isEthereal = false;

    private HashSet<Buff> buffs = new HashSet<Buff>();

    public int level;

    public Char(int level) {
        this.level = level;

        belongings = new Belongings(this);
        nutrition = 0;
        corpseEffects = CorpseEffect.None.value;

        attribute = new int[]{10, 10, 10};
        attributeDamage = new int[]{0, 0, 0};
    }

    public boolean visibilityOverride(boolean state) {
        return state;
    }

    @Override
    protected boolean act() {
        belongings.decay(getTime(), true, false);
        Dungeon.level.updateFieldOfView(this);
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
    private static final String BUFFS = "buffs";
    private static final String CORPSEEFFECTS = "corpseEffects";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(ATTRIBUTE0, attribute[0]);
        bundle.put(ATTRIBUTE1, attribute[1]);
        bundle.put(ATTRIBUTE2, attribute[2]);

        bundle.put(ATTRIBUTE0DAMAGE, attributeDamage[0]);
        bundle.put(ATTRIBUTE1DAMAGE, attributeDamage[1]);
        bundle.put(ATTRIBUTE2DAMAGE, attributeDamage[2]);

        bundle.put(POS, pos);
        bundle.put(TAG_HP, HP);
        bundle.put(TAG_HT, HT);
        bundle.put(BUFFS, buffs);
        bundle.put(CORPSEEFFECTS, corpseEffects);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        attribute = new int[]{
            bundle.getInt(ATTRIBUTE0),
            bundle.getInt(ATTRIBUTE1),
            bundle.getInt(ATTRIBUTE2)
        };

        attributeDamage = new int[]{
            bundle.getInt(ATTRIBUTE0DAMAGE),
            bundle.getInt(ATTRIBUTE1DAMAGE),
            bundle.getInt(ATTRIBUTE2DAMAGE)
        };

        pos = bundle.getInt(POS);
        HP = bundle.getInt(TAG_HP);
        HT = bundle.getInt(TAG_HT);
        corpseEffects = bundle.getLong(CORPSEEFFECTS);

        for (Bundlable b : bundle.getCollection(BUFFS)) {
            if (b != null) {
                ((Buff) b).attachTo(this);
            }
        }
    }

    protected boolean shouldAttack(Char enemy) {
        return true;
    }

    protected boolean canAttack(Char enemy) {
        return Level.canReach(pos, enemy.pos);
    }

    protected boolean shouldTouch(Char enemy) {
        return false;
    }

    public boolean attack(KindOfWeapon weapon, Char enemy) {
        return attack(weapon, false, enemy);
    }

    public boolean attack(KindOfWeapon weapon, boolean ranged, Char enemy) {
        boolean visibleFight = Dungeon.visible[pos] || Dungeon.visible[enemy.pos];
        boolean retval = false;

        boolean touch = shouldTouch(enemy);
        Held heldBuff = buff(Held.class);

        if (heldBuff != null && heldBuff.host == enemy) {
            heldBuff = null;
        }

        if (heldBuff != null) {
            String hostname;
            if (heldBuff.host != null) {
                hostname = heldBuff.host.name;
            }
            else {
                hostname = "something";
            }

            if (visibleFight) {
                if (this == Dungeon.hero) {
                    GLog.n(TXT_HELD, hostname, "your");
                } else if (heldBuff.host == Dungeon.hero) {
                    GLog.n(TXT_HELD, "you", name + "'s");
                } else {
                    GLog.n(TXT_HELD, hostname, name + "'s");
                }
            }
        } else if (tryHit(this, weapon, ranged, enemy, touch)) {
            if (!touch) {
                hit(weapon, enemy, visibleFight);
            } else {
                if (visibleFight) {
                    GLog.i(TXT_TOUCH, name, enemy.name);
                }

                touch(enemy, visibleFight);
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

                Sample.INSTANCE.play(Assets.SND_MISS);
            }

            retval = false;
        }

        if (this.flying) {
            Bounce.proc(32767, this, enemy);
        }
        if (enemy.flying) {
            Bounce.proc(32767, enemy, this);
        }

        return retval;
    }

    protected void hit(KindOfWeapon weapon, Char enemy, boolean visible) {
        if (visible) {
            GLog.i(TXT_HIT, name, enemy.name);
        }

        int effectiveDamage;

        if (weapon != null) {
            effectiveDamage = weapon.damageRoll();
            effectiveDamage += getAttributeModifier(weapon.damageAttribute);
        } else {
            effectiveDamage = Random.IntRange(1, 4);
            effectiveDamage += getAttributeModifier(AttributeType.STRCON);
        }

        if (effectiveDamage < 0) {
            effectiveDamage = 0;
        }

        if (weapon != null) {
            effectiveDamage = weapon.proc(this, false, enemy, effectiveDamage);
        }
        effectiveDamage = enemy.defenseProc(this, effectiveDamage);

        if (visible) {
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

            enemy.damage(effectiveDamage, weapon.damageType(), this);

            if (buff(FireImbue.class) != null)
                buff(FireImbue.class).proc(enemy);
            if (buff(EarthImbue.class) != null)
                buff(EarthImbue.class).proc(enemy);

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

    protected void touch(Char enemy, boolean visible) {
        if (visible) {
            GLog.i(TXT_TOUCH, name, enemy.name);
        }
    }

    public boolean zap() {
        //todo: zap behavior
        return false;
    }

    public static boolean tryHit(Char attacker, KindOfWeapon weapon, boolean thrown, Char defender, boolean touch) {
        int roll = Random.Int(1, 20);
        int skill = attacker.attackSkill(weapon, thrown, defender);
        int defense = defender.defenseMundane(attacker, touch);

        GLog.d("roll=" + roll + " + " + skill + " >= " + defense + "?");

        return roll + skill >= defense;

        /*
		float acuRoll = Random.Float( attacker.attackSkill( defender ) );
		float defRoll = Random.Float( defender.defenseSkill( attacker ) );
		if (attacker.buff(Bless.class) != null) acuRoll *= 1.20f;
		if (defender.buff(Bless.class) != null) defRoll *= 1.20f;
		return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
		*/
    }

    public int attackSkill(KindOfWeapon weapon, boolean thrown, Char target) {
        if (Level.distance(pos, target.pos) <= 1) {
            if (thrown) {
                GLog.d("thrown weapon in melee");
                return -4;
            }
            else {
                if (weapon instanceof RangedWeapon) {
                    GLog.d("ranged weapon in melee");
                    return -4;
                }
            }
        }

        return 0;
    }

    public int defenseMundane(Char enemy, boolean touch) {
		/*
		//todo: ring defense buff shit
		for (Buff buff : buffs( RingOfEvasion.Evasion.class )) {
			defense += ((RingOfEvasion.Evasion)buff).effectiveLevel;
		}
		*/

        return defenseMundane + belongings.getArmor(getAttributeModifier(AttributeType.DEXCHA), touch);
    }

    public int defenseMagical(Char enemy, MagicType type) {
        if ((immunityMagical & type.value) != 0) {
            return 7;
        }

        return defenseMagical + belongings.getResistance();
    }

    public String defenseVerb() {
        return "dodged";
    }

    public int defenseProc(Char enemy, int damage) {
        return damage;
    }

    public int damage(int dmg, MagicType type, Actor source) {
        if (HP <= 0 || dmg < 0) {
            return 0;
        }
        if (this.buff(Frost.class) != null) {
            Buff.detach(this, Frost.class);
        }
        if (this.buff(MagicalSleep.class) != null) {
            Buff.detach(this, MagicalSleep.class);
        }

        if ((immunityMagical & type.value) != 0) {
            dmg /= 2;
        }
        //else if (resistances().contains( srcClass )) {
        //	dmg = Random.IntRange( 0, dmg );
        //}

        if (buff(Paralysis.class) != null) {
            if (Random.Int(dmg) >= Random.Int(HP)) {
                Buff.detach(this, Paralysis.class);
                if (Dungeon.visible[pos]) {
                    GLog.i(TXT_OUT_OF_PARALYSIS, name);
                }
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

    public boolean shoot(Char enemy, MissileWeapon wep) {
        boolean result = attack(wep, true, enemy);
        Invisibility.dispel();

        //todo: strange bug here needs to be traced: ready state failing in the chain somewhere occasionally if: enemy dies (every occurrence of bug), player had one ammo item left (most occurrences) or player had more than one ammo item left (rare occurrences)

        return result;
    }

    public void destroy() {
        HP = 0;
        Actor.remove(this);
    }

    protected boolean shouldDropCorpse() {
        return Random.Int(2) == 0;
    }

    public void die(Actor src) {
        if (nutrition > 0 && shouldDropCorpse()) {
            Dungeon.level.drop(new Corpse(this), pos);
        } else {
            GLog.d("no corpse because nutrition=" + nutrition);
        }

        belongings.dropAll(pos);

        destroy();
        sprite.die();
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
        int chID = ch.id();
        for (Buff b : buffs) {
            if (b instanceof Charm && ((Charm) b).object == chID) {
                return true;
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

    public void remove(Class<? extends Buff> buffClass) {
        for (Buff buff : buffs(buffClass)) {
            remove(buff);
        }
    }

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

    public int stealth() {
        return 0;
    }

    public void move(int step) {
        if (Level.canStep(step, pos, Level.diagonal) && buff(Vertigo.class) != null) {
            sprite.interruptMotion();
            int newPos = pos + Level.NEIGHBOURS8[Random.Int(8)];
            if (!(Level.passable[newPos] || Level.pathable[newPos] || Level.avoid[newPos]) || Actor.findChar(newPos) != null)
                return;
            else {
                sprite.move(pos, newPos);
                step = newPos;
            }
        }

		/*
		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}
		*/
        pos = step;
		/*
		if (flying && Dungeon.level.map[pos] == Terrain.DOOR) {
			Door.enter( pos );
		}
		*/
        if (this != Dungeon.hero) {
            sprite.visible = visibilityOverride(Dungeon.visible[pos]);
        }
    }

    public int distance(Char other) {
        return Level.distance(pos, other.pos);
    }

    public void onMotionComplete() {
        next();
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
