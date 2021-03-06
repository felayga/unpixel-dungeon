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
package com.felayga.unpixeldungeon.items.equippableitem.ring;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemRandomizationHandler;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Ring extends EquippableItem {

    private static final int TICKS_TO_KNOW = 200;

    private static final long TIME_TO_EQUIP = GameTime.TICK;

    private static final String TXT_IDENTIFY =
            "you are now familiar enough with your %s to identify it.";

    protected Buff buff;

    private static final Class<?>[] rings = {
            RingOfAccuracy.class,
            RingOfEvasion.class,
            RingOfElements.class,
            RingOfForce.class,
            RingOfFuror.class,
            RingOfHaste.class,
            RingOfMagic.class,
            RingOfMight.class,
            RingOfSharpshooting.class,
            RingOfTenacity.class,
            RingOfWealth.class
    };
    private static final String[] ringGems = {
            "diamond", "opal", "garnet", "ruby",
            "amethyst", "topaz", "onyx", "tourmaline",
            "emerald", "sapphire", "quartz", "agate",
            "copper", "brass", "bronze", "iron",
            "steel", "silver", "gold", "coral",
            "tigereye", "wooden", "ivory", "twisted",
            "moonstone", "jade", "pearl", "granite",
            "vine", "ammolite", "spinel"
    };
    private static final Integer[] ringImages = {
            ItemSpriteSheet.RING_DIAMOND, ItemSpriteSheet.RING_OPAL, ItemSpriteSheet.RING_GARNET, ItemSpriteSheet.RING_RUBY,
            ItemSpriteSheet.RING_AMETHYST, ItemSpriteSheet.RING_TOPAZ, ItemSpriteSheet.RING_ONYX, ItemSpriteSheet.RING_TOURMALINE,
            ItemSpriteSheet.RING_EMERALD, ItemSpriteSheet.RING_SAPPHIRE, ItemSpriteSheet.RING_QUARTZ, ItemSpriteSheet.RING_AGATE,
            ItemSpriteSheet.RING_COPPER, ItemSpriteSheet.RING_BRASS, ItemSpriteSheet.RING_BRONZE, ItemSpriteSheet.RING_IRON,
            ItemSpriteSheet.RING_STEEL, ItemSpriteSheet.RING_SILVER, ItemSpriteSheet.RING_GOLD, ItemSpriteSheet.RING_CORAL,
            ItemSpriteSheet.RING_TIGEREYE, ItemSpriteSheet.RING_WOODEN, ItemSpriteSheet.RING_IVORY, ItemSpriteSheet.RING_TWISTED,
            ItemSpriteSheet.RING_MOONSTONE, ItemSpriteSheet.RING_JADE, ItemSpriteSheet.RING_PEARL, ItemSpriteSheet.RING_GRANITE,
            ItemSpriteSheet.RING_VINE, ItemSpriteSheet.RING_AMMOLITE, ItemSpriteSheet.RING_SPINEL
    };
    private static final Material[] ringMaterials = {
            Material.Gemstone, Material.Gemstone, Material.Gemstone, Material.Gemstone,
            Material.Gemstone, Material.Gemstone, Material.Gemstone, Material.Gemstone,
            Material.Gemstone, Material.Gemstone, Material.Gemstone, Material.Gemstone,
            Material.Copper, Material.Metal, Material.Metal, Material.Iron,
            Material.Iron, Material.Silver, Material.Gold, Material.Mineral,
            Material.Gemstone, Material.Wood, Material.Bone, Material.Metal,
            Material.Gemstone, Material.Gemstone, Material.Mineral, Material.Mineral,
            Material.Vegetable, Material.Gemstone, Material.Gemstone
    };

    private static ItemRandomizationHandler<Ring> handler;

    private String gem;

    private int ticksToKnow = TICKS_TO_KNOW;

    @SuppressWarnings("unchecked")
    public static void initGems() {
        handler = new ItemRandomizationHandler<>((Class<? extends Ring>[]) rings, ringGems, ringImages, ringMaterials, 1);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    @SuppressWarnings("unchecked")
    public static void restore(Bundle bundle) {
        handler = new ItemRandomizationHandler<>((Class<? extends Ring>[]) rings, ringGems, ringImages, ringMaterials, bundle);
    }

    public Ring() {
        super(GameTime.TICK);
        syncRandomizedProperties();

        weight(Encumbrance.UNIT * 3);
        price = 75;

        randomEnchantmentMinimum = 1;
    }

    public void syncRandomizedProperties() {
        image = handler.image(this);
        gem = handler.label(this);
        material = handler.material(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(isEquipped(hero) ? Constant.Action.UNEQUIP : Constant.Action.EQUIP);
        return actions;
    }

    @Override
    public Slot[] getSlots() {
        return new Slot[]{Slot.Ring1, Slot.Ring2};
    }

    @Override
    public void onEquip(Char owner, boolean cursed) {
        buff = buff();
        buff.attachTo(owner, null);

        if (cursed) {
            if (owner == Dungeon.hero) {
                GLog.n("your " + this.getDisplayName() + " tightens around your finger painfully");
            } else {
                //todo: ring cursed in enemy hands description
            }
        }
    }

    @Override
    public void onUnequip(Char owner) {
        owner.remove(buff);
        buff = null;
    }

    @Override
    public Item upgrade(Item source, int n) {
        super.upgrade(source, n);

        if (buff != null) {
            Char owner = buff.target;
            buff.detach();
            if ((buff = buff()) != null) {
                buff.attachTo(owner, null);
            }
        }

        return this;
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    protected boolean setKnown() {
        if (!isKnown()) {
            handler.know(this);

            Badges.validateAllRingsIdentified();

            return true;
        }

        return false;
    }

    @Override
    public String getName() {
        return isKnown() ? super.getName() : gem + " ring";
    }

    @Override
    public String desc() {
        return
                "This metal band is adorned with a large " + gem + " gem " +
                        "that glitters in the darkness. Who knows what effect it has when worn?";
    }

    @Override
    public String info() {
        if (isEquipped(Dungeon.hero)) {

            return desc() + "\n\n" + "The " + getDisplayName() + " is on your finger" +
                    (bucStatus() == BUCStatus.Cursed ? ", and because it is cursed, you are powerless to remove it." : ".");

        } else if (bucStatusKnown() && bucStatus() == BUCStatus.Cursed) {

            return desc() + "\n\nYou can feel a malevolent magic lurking within the " + getDisplayName() + ".";

        } else {

            return desc();

        }
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && isKnown();
    }

    @Override
    public Item identify(boolean updateQuickslot) {
        if (setKnown()) {
            updateQuickslot = true;
        }

        return super.identify(updateQuickslot);
    }


    public static boolean allKnown() {
        return handler.known().size() == rings.length - 2;
    }

    protected RingBuff buff() {
        return null;
    }

    private static final String UNFAMILIRIARITY = "unfamiliarity";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, ticksToKnow);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if ((ticksToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0) {
            ticksToKnow = TICKS_TO_KNOW;
        }
    }

    public class RingBuff extends Buff {

        private static final String TXT_KNOWN = "This is a %s";

        public int level;

        public RingBuff() {
            level = Ring.this.level();
        }

        @Override
        public boolean attachTo(Char target, Char source) {
            if (target == Dungeon.hero && ((Hero) target).heroClass == HeroClass.ROGUE && !isKnown()) {
                setKnown();
                GLog.i(TXT_KNOWN, getDisplayName());
                Badges.validateItemLevelAquired(Ring.this);
            }

            return super.attachTo(target, source);
        }

        @Override
        public boolean act() {
            if (!isIdentified() && --ticksToKnow <= 0) {
                String gemName = getDisplayName();
                identify();
                GLog.w(TXT_IDENTIFY, gemName, Ring.this.getDisplayName());
                Badges.validateItemLevelAquired(Ring.this);
            }

            spend_new(GameTime.TICK, false);

            return true;
        }
    }
}
