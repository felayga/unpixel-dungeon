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
package com.felayga.unpixeldungeon.items.equippableitem.weapon;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Death;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Fire;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Horror;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Instability;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Leech;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Luck;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Paralysis;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Poison;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Shock;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.enchantments.Slow;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Weapon extends EquippableItem implements IWeapon {
    private static final String TXT_EQUIP_CURSED    = "The %s welds itself to your hand!";

    protected static final long TIME_TO_EQUIP = GameTime.TICK;

    private static final int HITS_TO_KNOW = 20;

    private static final String TXT_INCOMPATIBLE =
            "Interaction of different types of magic has negated the enchantment on this weapon!";
    private static final String TXT_TO_STRING = "%s :%d";

    private int hitsToKnow = HITS_TO_KNOW;

    public Enchantment enchantment;

    public long delay_new = GameTime.TICK;
    public int damageMin = 1;
    public int damageMax = 4;
    public MagicType damageType = MagicType.Mundane;

    public int accuracy = 10;
    public int damage = 10;

    public WeaponSkill skillRequired;
    public WeaponSkill skillRequired() {
        return skillRequired;
    }

    public int refined = 0;

    public AttributeType accuracyAttribute = AttributeType.DEXCHA;
    public AttributeType accuracyAttribute() {
        return accuracyAttribute;
    }

    public int accuracyAttributeMaxBonus = 32767;

    public AttributeType damageAttribute = AttributeType.STRCON;
    public AttributeType damageAttribute() {
        return damageAttribute;
    }

    public Weapon(WeaponSkill weaponSkill, long delay, int damageMin, int damageMax) {
        super(TIME_TO_EQUIP);

        this.skillRequired = weaponSkill;

        this.delay_new = delay;

        this.damageMin = damageMin;
        this.damageMax = damageMax;

        pickupSound = Assets.SND_ITEM_BLADE;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( isEquipped( hero ) ? Constant.Action.UNEQUIP : Constant.Action.EQUIP );
        return actions;
    }

    @Override
    public Slot[] getSlots() {
        return new Slot[]{ Slot.Weapon };
    }

    @Override
    public void onEquip(Char owner, boolean cursed) {
        if (cursed) {
            if (owner == Dungeon.hero) {
                GLog.n( TXT_EQUIP_CURSED, getDisplayName() );
            }
            else {
                //todo: weapon cursed in enemy hands
            }
        }
    }

    @Override
    public void onUnequip(Char owner) {
    }

    public MagicType damageType() {
        return damageType;
    }

    public int damageRoll() {
        return Random.NormalIntRange( damageMin, damageMax ) + this.level();
    }

    public int accuracyModifier() {
        return this.level() + this.refined;
    }

    public int proc(Char attacker, boolean thrown, Char defender, int damage) {
        if (enchantment != null) {
            enchantment.proc(this, attacker, defender, damage);
        }

        if (attacker == Dungeon.hero && !levelKnown()) {
            if (--hitsToKnow <= 0) {
                levelKnown(true, true);
            }
        }

        return damage;
    }

    private static final String UNFAMILIRIARITY = "unfamiliarity";
    private static final String ENCHANTMENT = "enchantment";
    //private static final String IMBUE			= "imbue";
    private static final String ACCURACY = "accuracy";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, hitsToKnow);
        bundle.put(ENCHANTMENT, enchantment);
        bundle.put(ACCURACY, accuracy);
        //bundle.put( IMBUE, imbue );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if ((hitsToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0) {
            hitsToKnow = HITS_TO_KNOW;
        }
        enchantment = (Enchantment) bundle.get(ENCHANTMENT);
        //imbue = bundle.getEnum( IMBUE, Imbue.class );
        accuracy = bundle.getInt(ACCURACY);
    }


    @Override
    public String getName() {
        String name = super.getName();

        if (enchantment != null) {
            name = enchantment.name(name);
        }

        return name;
    }

    @Override
    public String getDisplayName() {
        String name = super.getDisplayName();

        if (hasLevels() && levelKnown()) {
            if (level() >= 0) {
                name = "+" + level() + " " + name;
            } else {
                name = level() + " " + name;
            }
        }

        return name;
    }

    public Weapon enchant(Enchantment ench) {
        enchantment = ench;
        return this;
    }

    public Weapon enchant() {
        Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
        Enchantment ench = Enchantment.random();
        while (ench.getClass() == oldEnchantment) {
            ench = Enchantment.random();
        }

        return enchant(ench);
    }

    public boolean isEnchanted() {
        return enchantment != null;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return enchantment != null ? enchantment.glowing() : null;
    }

    //FIXME: most enchantment names are pretty broken, should refactor
    public static abstract class Enchantment implements Bundlable {
        private static final Class<?>[] enchants = new Class<?>[]{
                Fire.class, Poison.class, Death.class, Paralysis.class, Leech.class,
                Slow.class, Shock.class, Instability.class, Horror.class, Luck.class};
        private static final float[] chances = new float[]{10, 10, 1, 2, 1, 2, 6, 3, 2, 2};

        public abstract boolean proc(Weapon weapon, Char attacker, Char defender, int damage);

        public String name(String weaponName) {
            return weaponName;
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
        }

        @Override
        public void storeInBundle(Bundle bundle) {
        }

        public ItemSprite.Glowing glowing() {
            return ItemSprite.Glowing.WHITE;
        }

        @SuppressWarnings("unchecked")
        public static Enchantment random() {
            try {
                return ((Class<Enchantment>) enchants[Random.chances(chances)]).newInstance();
            } catch (Exception e) {
                return null;
            }
        }

    }
}
