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

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.hero.SoulMark;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.actors.hero.HeroSubClass;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.ItemRandomizationHandler;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Wand extends Item {

	private static final int USAGES_TO_KNOW    = 20;

	public static final String AC_ZAP	= "ZAP";
	
	private static final String TXT_WOOD	= "This thin %s wand is warm to the touch. Who knows what it will do when used?";
	private static final String TXT_DAMAGE	= "When this wand is used as a melee weapon, its average damage is %d points per hit.";
	private static final String TXT_WEAPON	= "You can use this wand as a melee weapon.";
			
	private static final String TXT_FIZZLES		= "your wand fizzles; it must not have enough charge.";
	private static final String TXT_SELF_TARGET	= "You can't target yourself";

	private static final String TXT_IDENTIFY    = "You are now familiar with your %s.";

	private static final long TIME_TO_ZAP	= GameTime.TICK;


    private static final Class<?>[] wands = {
            WandOfDisintegration.class,
            WandOfFireblast.class,
            WandOfFrost.class,
            WandOfLightning.class,
            WandOfMagicMissile.class,
            WandOfRegrowth.class,
            WandOfVenom.class
    };
    private static final String[] appearances = {
            "glass",
            "balsa",
            "crystal",
            "maple",
            "pine",
            "oak",
            "ebony",
            "marble",
            "tin",
            "brass",
            "copper",
            "silver",
            "aluminum",
            "uranium",
            "iron",
            "steel",
            "gold",
            "jeweled",
            "forked",
            "curved"
    };
    private static final Integer[] images = {
            ItemSpriteSheet.WAND_01,
            ItemSpriteSheet.WAND_02,
            ItemSpriteSheet.WAND_03,
            ItemSpriteSheet.WAND_04,
            ItemSpriteSheet.WAND_05,
            ItemSpriteSheet.WAND_06,
            ItemSpriteSheet.WAND_07,
            ItemSpriteSheet.WAND_08,
            ItemSpriteSheet.WAND_09,
            ItemSpriteSheet.WAND_10,
            ItemSpriteSheet.WAND_11,
            ItemSpriteSheet.WAND_12,
            ItemSpriteSheet.WAND_13,
            ItemSpriteSheet.WAND_14,
            ItemSpriteSheet.WAND_15,
            ItemSpriteSheet.WAND_16,
            ItemSpriteSheet.WAND_17,
            ItemSpriteSheet.WAND_18,
            ItemSpriteSheet.WAND_19,
            ItemSpriteSheet.WAND_20
    };

    private static ItemRandomizationHandler<Wand> handler;

    private String rune;

    @SuppressWarnings("unchecked")
    public static void initLabels() {
        handler = new ItemRandomizationHandler<Wand>( (Class<? extends Wand>[])wands, appearances, images, 0 );
    }

    public static void save( Bundle bundle ) {
        handler.save( bundle );
    }

    @SuppressWarnings("unchecked")
    public static void restore( Bundle bundle ) {
        handler = new ItemRandomizationHandler<Wand>( (Class<? extends Wand>[])wands, appearances, images, bundle );
    }

    public boolean isKnown() {
        return handler.isKnown( this );
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

        int luck = Dungeon.hero.luck();

        float min = (float)Math.pow(((double) luck + 11.0) / 31.0, 1.5);
        float max = (float)Math.pow(((double) luck + 21.0) / 31.0, 0.5);
        double charges = Random.Float(min, max);

        curCharges = (int)Math.ceil(charges * (double)maxCharges);

        return this;
    }

    private int maxCharges;
	private int curCharges;

    public boolean hasCharges() {
        return curCharges > 0;
    }
	
	private boolean curChargeKnown = false;

	protected int usagesToKnow = USAGES_TO_KNOW;

	protected int collisionProperties = Ballistica.MAGIC_BOLT;

	public Wand(int maxCharges)
	{
        syncVisuals();

		defaultAction = AC_ZAP;
		usesTargeting = true;
        this.maxCharges = maxCharges;
        this.curCharges = maxCharges;
        hasLevels(false);

		weight(Encumbrance.UNIT * 7);
        price = 75;
	}

    @Override
    public void syncVisuals() {
        image = handler.image(this);
        rune = handler.label(this);
    }
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hasCharges() || !curChargeKnown) {
			actions.add( AC_ZAP );
		}

		return actions;
	}
	
	@Override
	public boolean execute( Hero hero, String action ) {
		if (action.equals( AC_ZAP )) {
			curUser = hero;
			curItem = this;
			GameScene.selectCell( zapper );

			return false;
		} else {
			return super.execute( hero, action );
		}
	}
	
	protected abstract void onZap( Ballistica attack );

	public abstract void onHit( MagesStaff staff, Char attacker, Char defender, int damage);

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

	protected void processSoulMark(Char target, Char source){
		if (target != Dungeon.hero &&
				Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
				Random.Float() < .15f + (level()*1*0.03f)){
			SoulMark.prolong(target, source, SoulMark.class, SoulMark.DURATION);
		}
	}

	@Override
	public void onDetach( ) {
		/*
		stopCharging();
		*/
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
		
		StringBuilder sb = new StringBuilder( super.toString() );
		
		String status = status();
		if (status != null) {
			sb.append( " (" + status +  ")" );
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
                return curCharges + "/" + maxCharges;
            } else {
                return "???";
            }
		} else {
			return null;
		}
	}

	
	protected void fx( Ballistica bolt, Callback callback ) {
		MagicMissile.whiteLight( curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	public void staffFx( MagesStaff.StaffParticle particle ){
		particle.color(0xFFFFFF); particle.am = 0.3f;
		particle.setLifespan( 1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2.5f );
		particle.radiateXY(1f);
	}

	protected void wandUsed() {
		usagesToKnow -= 1;
		curCharges -= 1;
		if (!isIdentified() && usagesToKnow <= 0) {
			identify();
			GLog.w( TXT_IDENTIFY, getDisplayName() );
		} else {
			//if (curUser.heroClass == HeroClass.MAGE) levelKnown(true, true);
		}

        //todo: wand zap time verification
		curUser.spend_new(TIME_TO_ZAP, true);
	}

	private static final String UNFAMILIRIARITY        = "unfamiliarity";
	private static final String CUR_CHARGES			= "curCharges";
	private static final String CUR_CHARGE_KNOWN	= "curChargeKnown";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle);
        bundle.put( UNFAMILIRIARITY, usagesToKnow );
		bundle.put( CUR_CHARGES, curCharges );
		bundle.put( CUR_CHARGE_KNOWN, curChargeKnown );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((usagesToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			usagesToKnow = USAGES_TO_KNOW;
		}
		curCharges = bundle.getInt( CUR_CHARGES);
        curChargeKnown = bundle.getBoolean( CUR_CHARGE_KNOWN);
    }
	
	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {
		
		@Override
		public boolean onSelect( Integer target ) {
			if (target != null && target != Constant.Position.NONE) {

				final Wand curWand = (Wand)Wand.curItem;

				final Ballistica shot = new Ballistica( curUser.pos(), target, curWand.collisionProperties);
				int cell = shot.collisionPos;
				
				if (target == curUser.pos() || cell == curUser.pos()) {
					GLog.i( TXT_SELF_TARGET );
					return false;
				}

				curUser.sprite.zap(cell);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));
				
				if (curWand.hasCharges()) {
					
					curUser.busy();

					if (curWand.bucStatus == BUCStatus.Cursed){
						CursedWand.cursedZap(curWand, curUser, new Ballistica( curUser.pos(), target, Ballistica.MAGIC_BOLT));
						if (!curWand.bucStatusKnown){
							curWand.bucStatusKnown = true;
							GLog.n("This " + curItem.getDisplayName() + " is cursed!");
						}
					} else {
						curWand.fx(shot, new Callback() {
							public void call() {
								curWand.onZap(shot);
								curWand.wandUsed();
							}
						});
					}

                    //todo: not all wands should be dispelling invisibility
					Invisibility.dispelAttack(curUser);
					
				} else {

					GLog.w( TXT_FIZZLES );

				}
				
			}

            return true;
		}
		
		@Override
		public String prompt() {
			return "Choose a location to zap";
		}
	};
}
