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
package com.felayga.unpixeldungeon.items.wands;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.hero.SoulMark;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroSubClass;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemRandomizationHandler;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.spellcasting.Spellcaster;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Wand extends Item {
    private static final int USAGES_TO_KNOW = 20;

    public static final String AC_ZAP = "ZAP";

    private static final String TXT_WOOD = "This thin %s wand is warm to the touch. Who knows what it will do when used?";
    private static final String TXT_DAMAGE = "When this wand is used as a melee weapon, its average damage is %d points per hit.";
    private static final String TXT_WEAPON = "You can use this wand as a melee weapon.";

    private static final String TXT_FIZZLES = "your wand fizzles; it must not have enough charge.";
    private static final String TXT_SELF_TARGET = "You can't target yourself";

    private static final String TXT_IDENTIFY = "You are now familiar with your %s.";

    private static final long TIME_TO_ZAP = GameTime.TICK;


    private static final Class<?>[] wands = {
            WandOfDisintegration.class,
            WandOfFireblast.class,
            WandOfFrost.class,
            WandOfLightning.class,
            WandOfMagicMissile.class,
            WandOfRegrowth.class,
            WandOfVenom.class,
            WandOfLight.class,
            WandOfNothing.class,
            WandOfTunneling.class,
            WandOfInvisibility.class,
            WandOfHaste.class,
            WandOfSlow.class,
            WandOfBlastWave.class,
            WandOfSleep.class,
            WandOfCreateMonster.class,
            WandOfTeleportation.class,
            WandOfDeath.class,
            WandOfChaos.class
    };
    private static final String[] wandAppearances = {
            "glass", "balsa", "crystal", "maple",
            "pine", "oak", "ebony", "marble",
            "tin", "brass", "copper", "silver",
            "aluminum", "uranium", "iron", "steel",
            "gold", "jeweled", "forked", "curved",
            "short", "spiked", "runed", "plastic"
    };
    private static final Integer[] wandImages = {
            ItemSpriteSheet.WAND_GLASS, ItemSpriteSheet.WAND_BALSA, ItemSpriteSheet.WAND_CRYSTAL, ItemSpriteSheet.WAND_MAPLE,
            ItemSpriteSheet.WAND_PINE, ItemSpriteSheet.WAND_OAK, ItemSpriteSheet.WAND_EBONY, ItemSpriteSheet.WAND_MARBLE,
            ItemSpriteSheet.WAND_TIN, ItemSpriteSheet.WAND_BRASS, ItemSpriteSheet.WAND_COPPER, ItemSpriteSheet.WAND_SILVER,
            ItemSpriteSheet.WAND_ALUMINUM, ItemSpriteSheet.WAND_URANIUM, ItemSpriteSheet.WAND_IRON, ItemSpriteSheet.WAND_STEEL,
            ItemSpriteSheet.WAND_GOLD, ItemSpriteSheet.WAND_JEWELED, ItemSpriteSheet.WAND_FORKED, ItemSpriteSheet.WAND_CURVED,
            ItemSpriteSheet.WAND_SHORT, ItemSpriteSheet.WAND_SPIKED, ItemSpriteSheet.WAND_RUNED, ItemSpriteSheet.WAND_PLASTIC
    };
    private static final Material[] wandMaterials = {
            Material.Glass, Material.Wood, Material.Glass, Material.Wood,
            Material.Wood, Material.Wood, Material.Wood, Material.Mineral,
            Material.Metal, Material.Metal, Material.Copper, Material.Silver,
            Material.Metal, Material.Metal, Material.Iron, Material.Iron,
            Material.Gold, Material.Gemstone, Material.Metal, Material.Metal,
            Material.Metal, Material.Metal, Material.Mithril, Material.Plastic
    };

    private static ItemRandomizationHandler<Wand> handler;

    private String rune;

    @SuppressWarnings("unchecked")
    public static void initLabels() {
        handler = new ItemRandomizationHandler<Wand>((Class<? extends Wand>[]) wands, wandAppearances, wandImages, wandMaterials, 0);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    @SuppressWarnings("unchecked")
    public static void restore(Bundle bundle) {
        handler = new ItemRandomizationHandler<Wand>((Class<? extends Wand>[]) wands, wandAppearances, wandImages, wandMaterials, bundle);
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    public boolean setKnown() {
        if (!isKnown()) {
            handler.know(this);

            return true;
        }

        return false;
    }


    @Override
    public Item random() {
        super.random();

        curCharges = randomCharges();

        return this;
    }

    protected int randomCharges() {
        int luck = Dungeon.hero.luck();

        float min = (float) Math.pow(((double) luck + 11.0) / 31.0, 1.5);
        float max = (float) Math.pow(((double) luck + 21.0) / 31.0, 0.5);
        double charges = Random.Float(min, max);

        return (int) Math.ceil(charges * (double) maxCharges);
    }

    private int maxCharges;
    private int curCharges;

    public int curCharges() {
        return curCharges;
    }

    private boolean curChargeKnown = false;

    protected int usagesToKnow = USAGES_TO_KNOW;
    protected Spellcaster spellcaster;

    public Wand(int maxCharges) {
        syncRandomizedProperties();

        defaultAction = AC_ZAP;
        usesTargeting = true;
        this.maxCharges = maxCharges;
        this.curCharges = maxCharges;
        hasLevels(false);
        stackable = false;

        weight(Encumbrance.UNIT * 7);
        price = 75;

        //ballisticaMode = Ballistica.Mode.value(Ballistica.Mode.MagicBolt);
    }

    @Override
    public void syncRandomizedProperties() {
        image = handler.image(this);
        rune = handler.label(this);
        material = handler.material(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ZAP);
        return actions;
    }

    //todo: exploding needs an action and prepareZap

    @Override
    public boolean execute(Hero hero, String action) {
        if (action.equals(AC_ZAP)) {
            curUser = hero;
            curItem = this;

            spellcaster.prepareZap(bucStatus().value);

            if ((spellcaster.ballisticaMode & Ballistica.Mode.IsDirected.value) != 0) {
                GameScene.selectCell(zapper, "Choose a location to zap");
            } else {
                doZap(hero, hero.pos());
            }

            return false;
        } else {
            return super.execute(hero, action);
        }
    }

	/*
	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			if (container.owner != null) {
				charge( container.owner );
			}
			return true;
		} else {
			return false;
		}
	}
	*/

	/*
	public void charge( Char owner ) {
		if (charger == null) charger = new Charger();
		charger.attachTo( owner );
	}

	public void charge( Char owner, float chargeScaleFactor ){
		charge( owner );
		charger.setScaleFactor( chargeScaleFactor );
	}
	*/

    protected void processSoulMark(Char target, Char source) {
        if (target != Dungeon.hero &&
                Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
                Random.Float() < .15f + (level() * 1 * 0.03f)) {
            SoulMark.prolong(target, source, SoulMark.class, SoulMark.DURATION);
        }
    }

	/*
	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}
	*/

	/*
	public int level() {
		if (charger != null) {
			Magic magic = charger.target.buff( Magic.class );
			return magic == null ? level : Math.max( level + magic.level, 0 );
		} else {
			return level;
		}
	}
	*/

    @Override
    public Item identify(boolean updateQuickslot) {
        if (setKnown() || !curChargeKnown) {
            curChargeKnown = true;
            updateQuickslot = true;
        }

        return super.identify(updateQuickslot);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(super.toString());

        String status = status();
        if (status != null) {
            sb.append(" (" + status + ")");
        }

        return sb.toString();
    }

    @Override
    public String getName() {
        return isKnown() ? super.getName() : rune + " wand";
    }

    @Override
    public final String info() {
        return isKnown() ?
                desc() :
                "This " + rune + " wand is an expertly crafted capacitor of magical energy." +
                        " Who knows what will happen when it's invoked?";
        /*
		return (bucStatusKnown && bucStatus == BUCStatus.Cursed) ?
				desc() + "\n\nThis wand is cursed, making its magic chaotic and random." :
				desc();
		*/
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && curChargeKnown;
    }

    @Override
    public String status() {
        if (levelKnown()) {
            if (curChargeKnown) {
                return curCharges + "";// + "/" + maxCharges;
            } else {
                return "?";
            }
        } else {
            return null;
        }
    }

    /*
    protected void fxEffect(int source, int destination, Callback callback) {
        MagicMissile.whiteLight(curUser.sprite.parent, source, destination, callback);
    }
    */

    public void staffFx(MagesStaff.StaffParticle particle) {
        //todo: remove
        particle.color(0xFFFFFF);
        particle.am = 0.3f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.PI2), 2f);
        particle.setSize(1f, 2.5f);
        particle.radiateXY(1f);
    }

    protected void wandUsed() {
        usagesToKnow--;
        curCharges--;

        if (curCharges < 0) {
            GLog.w("The " + getName() + " turns to dust!");
            curUser.belongings.remove(this);
        } else {
            if (!isIdentified() && usagesToKnow <= 0) {
                identify();
                GLog.w(TXT_IDENTIFY, getDisplayName());
            } else {
                //if (curUser.heroClass == HeroClass.MAGE) levelKnown(true, true);
            }
        }

        updateQuickslot();

        //todo: wand zap time verification
        curUser.spend_new(TIME_TO_ZAP, true);
    }

    private static final String UNFAMILIRIARITY = "unfamiliarity";
    private static final String CUR_CHARGES = "curCharges";
    private static final String CUR_CHARGE_KNOWN = "curChargeKnown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, usagesToKnow);
        bundle.put(CUR_CHARGES, curCharges);
        bundle.put(CUR_CHARGE_KNOWN, curChargeKnown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if ((usagesToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0) {
            usagesToKnow = USAGES_TO_KNOW;
        }
        curCharges = bundle.getInt(CUR_CHARGES);
        curChargeKnown = bundle.getBoolean(CUR_CHARGE_KNOWN);
    }

    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public boolean onSelect(Integer target) {
            if (target != null && target != Constant.Position.NONE) {

                final Wand curWand = (Wand) Wand.curItem;

                return curWand.doZap(curUser, target);
            }

            return true;
        }
    };

    private boolean doZap(Char curUser, int target) {
        if (target == curUser.pos()) {
            if ((spellcaster.ballisticaMode & Ballistica.Mode.IsDirected.value) != 0 && (spellcaster.ballisticaMode & Ballistica.Mode.StopSelf.value) == 0) {
                GLog.i(TXT_SELF_TARGET);
                return false;
            }
        }

        curUser.sprite.zap(target);

        boolean hasCharges = curCharges > 0;

        if (!hasCharges && Random.Int(20) == 0) {
            hasCharges = true;
            GLog.p("You wrest one last charge out of the wand.");
        }

        if (hasCharges) {
            //todo: not all wands should be dispelling invisibility
            Invisibility.dispelAttack(curUser);

            curUser.busy();

            if (bucStatus() == BUCStatus.Cursed && Random.Int(100) == 0) {
                explode(curUser);
                /*
                CursedWand.cursedZap(this, curUser, new Ballistica(curUser.pos(), target, Ballistica.Mode.MagicBolt));
                if (!bucStatusKnown()) {
                    bucStatus(true);
                    GLog.n("This " + curItem.getDisplayName() + " is cursed!");
                }
                */
            } else {
                Spellcaster.cast(curUser, target, spellcaster, Spellcaster.Origin.Wand);
            }

        } else {

            GLog.w(TXT_FIZZLES);

        }

        return true;
    }

    public void explode(Char user) {
        GLog.n("The " + getName() + " suddenly explodes!");
        user.belongings.remove(this);
    }
}
