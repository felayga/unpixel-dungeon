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
package com.felayga.unpixeldungeon.actors.hero;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Bones;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.GamesInProgress;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.hero.DeathlySick;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.buffs.hero.Fury;
import com.felayga.unpixeldungeon.actors.buffs.hero.Hunger;
import com.felayga.unpixeldungeon.actors.buffs.hero.Sick;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.actors.buffs.negative.Drowsy;
import com.felayga.unpixeldungeon.actors.buffs.negative.Ooze;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.negative.Poison;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.actors.buffs.positive.Bless;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Boulder;
import com.felayga.unpixeldungeon.actors.mobs.npcs.NPC;
import com.felayga.unpixeldungeon.actors.mobs.snake.WaterMoccasin;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.CheckedCell;
import com.felayga.unpixeldungeon.effects.Flare;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.Amulet;
import com.felayga.unpixeldungeon.items.Ankh;
import com.felayga.unpixeldungeon.items.Dewdrop;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.Gemstone;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Heap.Type;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.artifacts.CapeOfThorns;
import com.felayga.unpixeldungeon.items.artifacts.EtherealChains;
import com.felayga.unpixeldungeon.items.artifacts.TalismanOfForesight;
import com.felayga.unpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.keys.OldKey;
import com.felayga.unpixeldungeon.items.rings.RingOfEvasion;
import com.felayga.unpixeldungeon.items.rings.RingOfMight;
import com.felayga.unpixeldungeon.items.rings.RingOfTenacity;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.items.tools.ITool;
import com.felayga.unpixeldungeon.items.tools.digging.Pickaxe;
import com.felayga.unpixeldungeon.items.tools.unlocking.UnlockingTool;
import com.felayga.unpixeldungeon.items.weapon.ammunition.AmmunitionWeapon;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.weapon.ammunition.simple.Rock;
import com.felayga.unpixeldungeon.items.weapon.ranged.RangedWeapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.levels.branches.DungeonBranch;
import com.felayga.unpixeldungeon.levels.features.AlchemyPot;
import com.felayga.unpixeldungeon.levels.features.Chasm;
import com.felayga.unpixeldungeon.levels.features.Door;
import com.felayga.unpixeldungeon.levels.features.Sign;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.WeaponSkill;
import com.felayga.unpixeldungeon.plants.Earthroot;
import com.felayga.unpixeldungeon.plants.Sungrass;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.scenes.InterlevelScene;
import com.felayga.unpixeldungeon.scenes.SurfaceScene;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.ui.AttackIndicator;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndBag;
import com.felayga.unpixeldungeon.windows.WndItem;
import com.felayga.unpixeldungeon.windows.WndMessage;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.felayga.unpixeldungeon.windows.WndTradeItem;
import com.felayga.unpixeldungeon.windows.hero.WndInitHero;
import com.felayga.unpixeldungeon.windows.hero.WndResurrect;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

//import com.felayga.unpixeldungeon.items.artifacts.DriedRose;

public class Hero extends Char {

	{
		actPriority = 0; //acts at priority 0, baseline for the rest of behaviour.
	}
	
	private static final String TXT_LEAVE = "One does not simply leave Pixel Dungeon.";
	
	public static final int MAX_LEVEL = 30;
	private static final String TXT_LEVEL_UP = "level up!";
	private static final String TXT_NEW_LEVEL =
		"Welcome to level %d! Now you are healthier and more focused. " +
		"It's easier for you to hit enemies and dodge their attacks.";
	private static final String TXT_LEVEL_CAP =
		"You can't gain any more levels, but your experiences still give you a burst of energy!";
	
	private static final String TXT_SOMETHING_ELSE	= "There is something else here";
	private static final String TXT_LOCKED_CHEST	= "This chest is locked and you don't have matching key";
	private static final String TXT_LOCKED_DOOR		= "This door is locked.";
	private static final String TXT_NOTICED_SMTH	= "You noticed something";

	public static final String TXT_YOU_NOW_HAVE		= "REPLACE TXT_YOU_NOW_HAVE %s";

    public static final String TXT_DIG_HERE         = "You can try to dig up through the ceiling or down through the floor here.  Neither is a terrific idea, generally speaking.";
    public static final String TXT_DIG_HERE_UP      = "DIG UP";
    public static final String TXT_DIG_HERE_DOWN    = "DIG DOWN";
	
	private static final String TXT_WAIT	= "...";
	private static final String TXT_SEARCH	= "search";

	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;

	public WeaponSkill weaponSkill = WeaponSkill.None;
	private int defenseSkill = 0;
	private int spellSkill = 0;

	public boolean ready = false;
	private boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;
	
	public boolean resting = false;
	private long motivation;

	protected int[] attributeUse;

	public void useAttribute(AttributeType type, int amount) {
        attributeUse[type.value] += amount;
    }

    public void useAttribute(AttributeType type, float percent) {
        useAttribute(type, (int)Math.round(percent * attributeUseRequirement(type)));
    }

    public static int _attributeUseRequirement(int attribute) {
        return (int)(Math.pow(attribute, 3.0) / 2.0);
    }

	public int attributeUseRequirement(AttributeType type)
	{
        int attribute;

        switch(type) {
            case STRCON:
                attribute = STRCON() + STRCON_damage();
                break;
            case DEXCHA:
                attribute = DEXCHA() + DEXCHA_damage();
                break;
            case INTWIS:
                attribute = INTWIS() + INTWIS_damage();
                break;
            default:
                return 0;
        }

		return _attributeUseRequirement(attribute);
	}

	private int applyAttributeUse(AttributeType type) {
        int attribute;
        switch(type) {
            case STRCON:
                attribute = STRCON() + STRCON_damage();
                break;
            case DEXCHA:
                attribute = DEXCHA() + DEXCHA_damage();
                break;
            case INTWIS:
                attribute = INTWIS() + INTWIS_damage();
                break;
            default:
                return 0;
        }

        int originalAttribute = attribute;
		int attribute_use = attributeUse[type.value];

		int attribute_use_test = _attributeUseRequirement(attribute);
		while (attribute_use >= attribute_use_test && attribute < 23) {
			attribute_use -= attribute_use_test;
			attribute++;
			attribute_use_test = _attributeUseRequirement(attribute);
		}

		while (attribute_use < 0 && attribute > 2) {
			attribute--;
			attribute_use_test = _attributeUseRequirement(attribute);
			attribute_use += attribute_use_test;
		}

		attributeUse[type.value] = attribute_use;

        int change = attribute - originalAttribute;

        increaseAttribute(type, change);

		return change;
	}

	public void resetAttributeUse()
	{
		attributeUse = new int[]{
				attributeUseRequirement(AttributeType.STRCON) / 2,
				attributeUseRequirement(AttributeType.DEXCHA) / 2,
				attributeUseRequirement(AttributeType.INTWIS) / 2
		};
	}

	int attributeIncreaseSteps = 0;
	private void attributeIncreaseCheck() {
		attributeIncreaseSteps++;

		if (attributeIncreaseSteps < 256) {
			return;
		}

		attributeIncreaseSteps = 0;

		int newvalue;


		newvalue = applyAttributeUse(AttributeType.STRCON);

		if (newvalue < 0) {
			GLog.n("You feel weak!  You must have been abusing your body.");
		} else if (newvalue > 0) {
			GLog.p("You feel strong!  You must have been exercising.");
		}


		newvalue = applyAttributeUse(AttributeType.DEXCHA);

		if (newvalue < 0) {
			GLog.n("You feel clumsy!  You haven't been working on your reflexes.");
		} else if (newvalue > 0) {
			GLog.p("You feel agile!  You must have been working on your reflexes.");
		}


		newvalue = applyAttributeUse(AttributeType.INTWIS);

		if (newvalue < 0) {
			GLog.n("You feel foolish!  You haven't been paying attention.");
		} else if (newvalue > 0) {
			GLog.p("You feel wise!  You must have been paying attention.");
		}

	}

    protected int luck = 0;

    public int luck() {
        return luck;
    }

	public boolean weakened = false;
	
	public float awareness;

	public int exp = 0;

	public int MT;
	public int MP;

	private ArrayList<Mob> visibleEnemies;
	
	public Hero() {
		super(1);

		name = "you";

		HP = HT = 20;
		MP = MT = 20;

		resetAttributeUse();

		awareness = 0.1f;

		motivation = 0;

		belongings = new Belongings(this);

		visibleEnemies = new ArrayList<>();
	}

    public void updateEncumbrance() {
        Encumbrance encumbrance = buff(Encumbrance.class);
        encumbrance.updateAppearance(this);
    }

	/*
	private int STR() {
		int STR = this.STRCON;

		for (Buff buff : buffs(RingOfMight.Might.class)) {
			STR += ((RingOfMight.Might)buff).level;
		}

		return weakened ? STR - 2 : STR;
	}
	*/

	private static final String DEFENSE						= "defenseSkill";
	private static final String WEAPONSKILL 				= "weaponSkill";
	private static final String ATTRIBUTE0USE               = "attribute0Use";
    private static final String ATTRIBUTE1USE               = "attribute1Use";
    private static final String ATTRIBUTE2USE               = "attribute2Use";
	private static final String LEVEL						= "lvl";
	private static final String EXPERIENCE					= "exp";
	private static final String MOTIVATION					= "motivation";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);

		heroClass.storeInBundle(bundle);
		subClass.storeInBundle(bundle);

		bundle.put(DEFENSE, defenseSkill);

		bundle.put(WEAPONSKILL, weaponSkill.value);

		bundle.put(ATTRIBUTE0USE, attributeUse[0]);
		bundle.put(ATTRIBUTE1USE, attributeUse[1]);
		bundle.put(ATTRIBUTE2USE, attributeUse[2]);

		bundle.put(LEVEL, level);
		bundle.put(EXPERIENCE, exp);

		bundle.put(MOTIVATION, motivation);

		belongings.storeInBundle(bundle);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		
		heroClass = HeroClass.restoreInBundle(bundle);
		subClass = HeroSubClass.restoreInBundle(bundle);

		defenseSkill = bundle.getInt(DEFENSE);

		weaponSkill = WeaponSkill.fromInt(bundle.getInt(WEAPONSKILL));

		attributeUse = new int[]{
			bundle.getInt(ATTRIBUTE0USE),
			bundle.getInt(ATTRIBUTE1USE),
			bundle.getInt(ATTRIBUTE2USE)
		};

		updateAwareness();

		level = bundle.getInt( LEVEL );
		exp = bundle.getInt( EXPERIENCE );

		motivation = bundle.getLong(MOTIVATION);
		
		belongings.restoreFromBundle(bundle);
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.level = bundle.getInt( LEVEL );
	}
	
	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

	public String givenName(){
		return name.equals("you") ? className() : name;
	}
	
	public void live() {
		Buff.affect(this, Hunger.class);
		Buff.affect(this, Encumbrance.class);
	}

    private Boulder lastBoulder;
    private long lastBoulderTime;

	public void pushBoulder(Boulder boulder) {
        spend_new(movementSpeed(), true);
		useAttribute(AttributeType.STRCON, 1);

        if (lastBoulder != boulder || getTime() - lastBoulderTime > GameTime.TICK * 8) {
            GLog.i("With great effort you move the boulder.");
            lastBoulder = boulder;
        }
        lastBoulderTime = getTime();

		curAction = new HeroAction.Move(boulder.pos);
		lastAction = null;

		act(); //keeps appearance of moving with pushed boulder
	}

	@Override
	public int attackSkill( KindOfWeapon weapon, boolean thrown, Char target ) {
		int accuracy = super.attackSkill(weapon, thrown, target);

		if (weapon != null)
		{
            int skill = weaponSkill.value;

            if (weapon instanceof AmmunitionWeapon) {
                AmmunitionWeapon ammo = (AmmunitionWeapon)weapon;

                if (ammo.launcher != null) {
                    skill = ammo.launcher.skillRequired.value;
                }
            }

			if (skill < weapon.skillRequired.value)
			{
				accuracy -= (weapon.skillRequired.value - skill) * 2;
			}

			accuracy += getAttributeModifier(weapon.accuracyAttribute);
			accuracy += weapon.accuracyFactor();

			GLog.d("skill=" + skill + " required=" + weapon.skillRequired.value + " modifier=" + getAttributeModifier(weapon.accuracyAttribute) + " factor=" + weapon.accuracyFactor());
		}

		//GLog.d("result="+accuracy);

        return accuracy;
	}

	public void motivate(boolean andnext) {
		if (andnext)
		{
			busy();
		}

		super.spend_new(1, false);
		motivation++;

		if (andnext)
		{
			next();
		}
	}

	@Override
	public void spend_new( long time, boolean andnext ) {
		if (andnext)
		{
			busy();
		}

		if (motivation < time) {
			super.spend_new(time - motivation, false);
			motivation = 0;
		}
		else {
			super.spend_new(1, false);
			motivation -= time - 1;
		}

		if (andnext)
		{
			next();
		}
	}
	
	@Override
	public boolean act() {
        super.act();

        if (paralysed > 0) {
            curAction = null;

            spend_new(GameTime.TICK, true);
            return false;
        }

        checkVisibleMobs();

        if (curAction == null) {

            if (resting) {
                spend_new(Constant.Time.HERO_REST, false);
                next();
                return false;
            }

            ready();
            return false;

        } else {

            resting = false;

            ready = false;

            if (curAction instanceof HeroAction.Move) {
                return actMove((HeroAction.Move) curAction);
            } else if (curAction instanceof HeroAction.Interact) {
                return actInteract((HeroAction.Interact) curAction);
            } else if (curAction instanceof HeroAction.Buy) {
                return actBuy((HeroAction.Buy) curAction);
            } else if (curAction instanceof HeroAction.HandleHeap) {
                GLog.d("curaction is HandleHeap");
                if (curAction instanceof HeroAction.HandleHeap.PickUp) {
                    GLog.d("curaction is PickUp");
                    return actPickUp((HeroAction.HandleHeap.PickUp) curAction);
                } else if (curAction instanceof HeroAction.HandleHeap.OpenBag) {
                    GLog.d("curaction is OpenBag");
                    return actOpenBag((HeroAction.HandleHeap.OpenBag) curAction);
                } else if (curAction instanceof HeroAction.HandleHeap.InteractItem) {
                    GLog.d("curaction is InteractItem");
                    return actInteractItem((HeroAction.HandleHeap.InteractItem) curAction);
                } else {
                    GLog.d("curaction is base");
                    return actHandleHeap((HeroAction.HandleHeap) curAction);
                }
            } else if (curAction instanceof HeroAction.OpenChest) {
                return actOpenChest((HeroAction.OpenChest) curAction);
            } else if (curAction instanceof HeroAction.HandleDoor) {
                if (curAction instanceof HeroAction.HandleDoor.OpenCloseDoor) {
                    return actOpenCloseDoor((HeroAction.HandleDoor.OpenCloseDoor) curAction);
                } else if (curAction instanceof HeroAction.HandleDoor.KickDoor) {
                    return actKickDoor((HeroAction.HandleDoor.KickDoor) curAction);
                } else if (curAction instanceof HeroAction.HandleDoor.UnlockDoor) {
                    return actUnlockDoor((HeroAction.HandleDoor.UnlockDoor) curAction);
                } else {
                    return actHandleDoor((HeroAction.HandleDoor) curAction);
                }
            } else if (curAction instanceof HeroAction.MoveLevel) {
                return actMoveLevel((HeroAction.MoveLevel) curAction);
            } else if (curAction instanceof HeroAction.Attack) {
                return actAttack((HeroAction.Attack) curAction);
            } else if (curAction instanceof HeroAction.InteractPosition) {
                return actInteractPosition((HeroAction.InteractPosition)curAction);
            } else if (curAction instanceof HeroAction.UseItem) {
                return actUseItem((HeroAction.UseItem) curAction);
            } else if (curAction instanceof HeroAction.Dig) {
                return actDig((HeroAction.Dig) curAction);
            }
        }

        return false;
    }
	
	public void busy() {
		ready = false;
	}
	
	private void ready() {
		sprite.idle();
		curAction = null;
		damageInterrupt = true;
		ready = true;

		AttackIndicator.updateState();
		
		GameScene.ready();

        GLog.d("ready");
	}
	
	public void interrupt() {
		if (isAlive() && curAction != null && (curAction instanceof HeroAction.Move && curAction.dst != pos)) {
			lastAction = curAction;
		} else if (curAction instanceof HeroAction.EatItem) {
			HeroAction.EatItem action = (HeroAction.EatItem) curAction;

			if (!action.stoppedEating) {
				Food food = (Food) action.target;
				food.stoppedEating(this);
			}

			if (action.targetOutsideInventory != null) {
				if (!action.targetOutsideInventory.collect(action.target)) {
					Dungeon.level.drop(action.target, pos).sprite.drop();
				}
			}
		}
		curAction = null;
	}
	
	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		act();
	}

	private boolean actEatItem(HeroAction.EatItem action) {
		Food food = (Food)action.target;

		//food.identify();

		if (food.stackable)
		{
			if (food.quantity() > 1)
			{
                action.targetOutsideInventory = food.parent();
				action.target = food.parent().remove(food, 1);
				food = (Food)action.target;

				//GLog.d("split food stack energy="+food.energy);
			}

			food.stackable = false;
		}

		String trueAction;
		if (!action.startedEating){
			action.startedEating = true;
			trueAction = Food.AC_EAT_START;
		}
		else {
			trueAction = Food.AC_EAT;
		}

		if (action.target.execute(this, trueAction))
		{
			Hunger hunger = buff(Hunger.class);
			final Food foodItem = (Food)action.target;

			if (hunger.isStuffed() && food.energyLeft() && !action.forced)
			{
				ShatteredPixelDungeon.scene().add(
						new WndOptions(foodItem.getName(), "You're having a hard time getting it all down.  Stop eating?", new String[]{"YES", "NO"}) {
							@Override
							protected void onSelect(int index) {
								switch (index) {
									case 0:
										foodItem.stoppedEating(Hero.this);
										break;
									case 1:
										curAction = new HeroAction.EatItem(foodItem, Food.AC_EAT, true);
										motivate(true);
										break;
								}
							}
						});

				action.stoppedEating = true;
				interrupt();
				return false;
			}

			return true;
		}
		else {
			if (action.targetOutsideInventory == null) {
                IBag parent = action.target.parent();
                if (parent != null) {
                    parent.remove(action.target, 1);
                }
			}

			//ready();
			return false;
		}
	}

	public boolean tryCastSpell(int spellLevel) {
		int difficulty = spellLevel * 4 - spellSkill * 6 - level / 3 - 5;
		int chance = INTWIS() * 12 - belongings.getSpellFailure();

		if (difficulty > 0) {
			chance += -(int)(Math.sqrt(difficulty/2) * 100);
		}
		else {
			chance += -difficulty * 42 / spellLevel;
		}

		return Random.PassFail(chance);
	}

	private boolean actUseItem(HeroAction.UseItem action) {
        if (action instanceof HeroAction.EatItem) {
            return actEatItem((HeroAction.EatItem) action);
        } else if (curAction instanceof HeroAction.UnlockBag) {
            return actLockUnlockBag((HeroAction.UnlockBag) curAction);
        } else {
            return useItem(action);
        }
    }

    private boolean useItem(HeroAction.UseItem action) {
        return action.target.execute(this, action.action);
    }

	private boolean actDig( final HeroAction.Dig action ) {
        int digPos = action.pos;

        if (action.effort > 0 && (Level.canReach(pos, digPos) || pos == digPos)) {
            boolean digfailurebedrock = false;

            if (pos == digPos && action.direction == 0) {
                if ((Level.flags & Level.FLAG_UNDIGGABLEFLOOR) != 0) {
                    digfailurebedrock = true;
                } else {
                    ShatteredPixelDungeon.scene().add(
                            new WndOptions("Dig Here", TXT_DIG_HERE, TXT_DIG_HERE_DOWN, TXT_DIG_HERE_UP, Constant.Action.CANCEL) {

                                @Override
                                protected void onSelect(int index) {
                                    switch (index) {
                                        case 0:
                                            curAction = new HeroAction.Dig(action.tool, action.pos, action.effort, 1);
                                            motivate(true);
                                            break;
                                        case 1:
                                            curAction = new HeroAction.Dig(action.tool, action.pos, action.effort, -1);
                                            motivate(true);
                                            break;
                                        default:
                                            ready();
                                            break;
                                    }
                                }
                            });

                    return false;
                }
            }

            if (!digfailurebedrock) {
                int x = digPos % Level.WIDTH;
                int y = digPos / Level.WIDTH;

                if (x <= 0 || x >= Level.WIDTH - 1 || y <= 0 || y >= Level.HEIGHT - 1) {
                    digfailurebedrock = true;
                }

                int currentEffort = action.tool.effort(this, true);
                action.effort -= currentEffort;
                useAttribute(AttributeType.STRCON, 1);

                Hunger hunger = buff(Hunger.class);
                if (hunger != null) {
                    hunger.satisfy_new(-1);
                }

                spend_new(attackSpeed(), false);

                if (!digfailurebedrock) {
                    if (action.boulder != null) {
                        if ((Level.flags & Level.FLAG_UNDIGGABLEBOULDERS) != 0) {
                            digfailurebedrock = true;
                        } else {
                            action.boulder.HP -= currentEffort;

                            if (action.boulder.HP <= 0) {
                                sprite.turnTo(pos, digPos);

                                sprite.attack(digPos);

                                action.boulder.die(this);

                                return false;
                            }
                        }
                    } else {
                        if ((Level.flags & Level.FLAG_UNDIGGABLEWALLS) != 0) {
                            digfailurebedrock = true;
                        } else {
                            int terrain = Dungeon.level.map[digPos];

                            if (pos != digPos && (Terrain.flags[terrain] & (Terrain.FLAG_WOOD | Terrain.FLAG_STONE)) == 0) {
                                GLog.n("Your " + action.tool.getName() + " isn't strong enough for this.");
                                ready();
                                return false;
                            }

                            if (action.effort <= 0) {
                                if (pos != digPos) {
                                    sprite.turnTo(pos, digPos);

                                    sprite.attack(digPos);
                                    spend_new(attackSpeed(), false);

                                    if (Dungeon.level.stone[digPos]) {
                                        if (terrain == Terrain.WALL_STONE) {
                                            Level.setDirt(digPos, true);
                                        } else {
                                            Level.setEmpty(digPos, true);
                                        }

                                        if (Random.Int(12) == 0) {
                                            GLog.w("You've dug out a boulder!");
                                            Boulder npc = new Boulder();
                                            npc.pos = digPos;
                                            Dungeon.level.mobs.add(npc);
                                            GameScene.add(npc);
                                        } else {
                                            Dungeon.level.drop(new Rock(Random.Int(3, 23)), digPos);
                                        }

                                        Sample.INSTANCE.play(Assets.SND_WALL_SMASH);
                                        CellEmitter.get(digPos).burst(Speck.factory(Speck.DUST), 5);
                                    } else if (Dungeon.level.wood[digPos]) {
                                        Level.setWoodDebris(digPos, true);

                                        Sample.INSTANCE.play(Assets.SND_DOOR_SMASH);
                                        CellEmitter.get(digPos).burst(Speck.factory(Speck.WOOD), 5);
                                    }

                                    Dungeon.level.removeVisuals(digPos);

                                    Dungeon.observe();
                                } else {
                                    CellEmitter.get(digPos).burst(Speck.factory(Speck.DUST), 5);

                                    if (action.direction == -1) {
                                        GLog.n("You loosen some rocks from the ceiling.  They fall on your head!");
                                        EquippableItem _helmet = belongings.helmet();

                                        Armor helmet = null;
                                        if (_helmet instanceof Armor) {
                                            helmet = (Armor) _helmet;
                                        }

                                        if (helmet != null && helmet.armor > 0) {
                                            GLog.p("Good thing you were wearing a sturdy helmet.");
                                        } else {
                                            Rock rock = new Rock();
                                            rock.random();

                                            int damage = 1;
                                            for (int n = 0; n < rock.quantity(); n++) {
                                                damage += rock.damageRoll();
                                            }

                                            damage(damage, MagicType.Mundane, null);

                                            Dungeon.level.drop(rock, pos);
                                        }
                                    } else {
                                        GLog.w("You dig through the floor!");

                                        Level.setDirtDeep(pos, true);

                                        Dungeon.observe();
                                    }

                                    ready();
                                }

                                return false;
                            }
                        }
                    }
                }
            }

            if (digfailurebedrock) {
                GLog.n("You seem to have hit bedrock.");
                ready();
                return false;
            }

            return true;
        } else if (Level.fieldOfView[digPos] && getCloser(digPos)) {
            return true;
        } else {
            ready();
            return false;
        }
    }

	private boolean actMove( HeroAction.Move action ) {

		if (getCloser(action.dst)) {
			return true;

		} else {
			if (Dungeon.level.map[pos] == Terrain.SIGN) {
				Sign.read(pos);
			}

			ready();

			return false;
		}
	}
	
	private boolean actInteract( HeroAction.Interact action ) {

		NPC npc = action.npc;

		if (Level.canReach(pos, npc.pos)) {
			ready();
			sprite.turnTo(pos, npc.pos);
			npc.interact();

			return false;
		} else {
			if (Level.fieldOfView[npc.pos] && getCloser( npc.pos )) {
				return true;
			} else {
				ready();

				return false;
			}
		}
	}
	
	private boolean actBuy( HeroAction.Buy action ) {
		int dst = action.dst;

		if (pos == dst || Level.canReach(pos, dst)) {
			ready();
			
			Heap heap = Dungeon.level.heaps.get(dst);
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				GameScene.show(new WndTradeItem(heap, true));
			}

			return false;
		} else if (getCloser( dst )) {
			return true;
		} else {
			ready();

			return false;
		}
	}

	private boolean actInteractPosition( HeroAction.InteractPosition action ) {
		int dst = action.dst;
		if (Level.canReach(pos, dst)) {
            if (action instanceof HeroAction.InteractPosition.Cook) {
                return actInteractPositionCook((HeroAction.InteractPosition.Cook) action);
            }
            else if (action instanceof HeroAction.InteractPosition.Well) {
                return actInteractPositionWell((HeroAction.InteractPosition.Well) action);
            }
			return false;
		} else if (getCloser( dst )) {
			return true;
		} else {
			ready();

			return false;
		}
	}

    private boolean actInteractPositionCook(HeroAction.InteractPosition.Cook action) {
        ready();
        AlchemyPot.operate(this, action.dst);
        return false;
    }

    private boolean actInteractPositionWell(HeroAction.InteractPosition.Well action) {
        final int dst = action.dst;

        if (action.action != null) {
            if (Constant.Action.DRINK.equals(action.action)) {
                spend_new(GameTime.TICK, true);

                if (Dungeon.level.map[dst] == Terrain.WELL_MAGIC) {
                    if (Random.Int(10) < 7) {
                        GLog.p("Wow!  This makes you feel great!");
                        useAttribute(AttributeType.INTWIS, 16);
                        switch(Random.Int(3)) {
                            case 0:
                                increaseAttribute(AttributeType.STRCON, 1);
                                break;
                            case 1:
                                increaseAttribute(AttributeType.DEXCHA, 1);
                                break;
                            default:
                                increaseAttribute(AttributeType.INTWIS, 1);
                                break;
                        }
                        GLog.i("A wisp of vapor escapes from the fountain...");
                        Level.set(dst, Terrain.WELL);
                    }
                    else {
                        GLog.p("The cool draught refreshes you.");
                        Hunger hunger = buff( Hunger.class );
                        if (hunger != null) {
                            hunger.satisfy_new(Food.AMOUNT_EATEN_PER_ROUND);
                        }
                    }
                } else {
                    switch(Random.Int(3)) {
                        case 0:
                            GLog.p("The cool draught refreshes you.");
                            Hunger hunger = buff( Hunger.class );
                            if (hunger != null) {
                                hunger.satisfy_new(Food.AMOUNT_EATEN_PER_ROUND);
                            }
                            break;
                        case 1:
                            GLog.n("This tepid water is tasteless.");
                            break;
                        default:
                            switch(Random.Int(11)) {
                                case 0:
                                    Buff.prolong(this, Sick.class, 2);
                                    break;
                                case 1:
                                    GLog.w("The water is contaminated!");
                                    damage(Random.IntRange(1, 10), MagicType.Poison, null);
                                    if ((immunityMagical & MagicType.Poison.value) != 0) {
                                        GLog.w("Perhaps it is runoff from a nearby farm.");
                                    } else {
                                        useAttribute(AttributeType.STRCON, -Random.Float(1.0f, 3.0f));
                                    }
                                    break;
                                case 2:
                                    GLog.n("An endless stream of snakes pour forth!");
                                    Dungeon.level.spawnMob(dst, WaterMoccasin.class, Random.IntRange(1, 6));
                                    break;
                                case 3:
                                    GLog.d("water demon");
                                    break;
                                case 4:
                                    GLog.d("water nymph");
                                    break;
                                case 5:
                                    GLog.d("curse 1/5 items, increase hunger, abuse strcon");
                                    break;
                                case 6:
                                    GLog.d("see invisible, exercise intwis");
                                    break;
                                case 7:
                                    GLog.d("see monsters, exercise intwis");
                                    break;
                                case 8:
                                    GLog.d("find gem");
                                    break;
                                case 9:
                                    GLog.d("monsters flee");
                                    break;
                                case 10:
                                    GLog.d("create pools, wet 1/5 items");
                                    break;
                            }
                            break;
                    }

                    if (Random.Int(3)==0) {
                        GLog.w("The fountain dries up!");
                        Level.set(dst, Terrain.EMPTY_WELL);
                        GameScene.updateMap(dst);
                        Dungeon.level.removeVisuals(dst);
                    }
                }
                ready();
            } else if (Constant.Action.DIP.equals(action.action)) {

            }
        }
        else {
            ShatteredPixelDungeon.scene().add(
                    new WndOptions("Fountain", "The water of the fountain looks cool and refreshing.",
                            Constant.Action.DRINK, Constant.Action.DIP, Constant.Action.CANCEL) {

                        @Override
                        protected void onSelect(int index) {
                            switch (index) {
                                case 0:
                                    curAction = new HeroAction.InteractPosition.Well(dst, Constant.Action.DRINK);
                                    motivate(true);
                                    break;
                                case 1:
                                    curAction = new HeroAction.InteractPosition.Well(dst, Constant.Action.DIP);
                                    motivate(true);
                                    break;
                                default:
                                    ready();
                                    break;
                            }
                        }
                    });
        }
        return false;
    }

	private boolean actLockUnlockBag(HeroAction.UnlockBag action) {
		int dst = action.dst;
		if (Level.canReach(pos, dst) || pos == dst) {
			Heap heap = Dungeon.level.heaps.get( dst );
			Item item = heap.peek();
			Bag bag = (Bag)item;

			if (bag.locked()) {

			}
			else {

			}

			ready();

			return false;
		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

    private boolean actHandleHeap(HeroAction.HandleHeap action) {
        final int dst = action.dst;

        if (pos == dst) {
            Heap heap = Dungeon.level.heaps.get(pos);

            if (heap != null) {
                if (heap.size() == 1) {
                    Item item = heap.peek();
                    ArrayList<String> actions = item.actions(Dungeon.hero, true);

                    if (actions.size() == 1) {
                        GLog.d("pickup");
                        curAction = new HeroAction.HandleHeap.PickUp(dst, item);
                        return true;
                    }
                    else {
                        GLog.d("interactitem");
                        curAction = new HeroAction.HandleHeap.InteractItem(dst, item);
                        return true;
                    }
                }
                else {
                    GLog.d("openbag");
                    curAction = new HeroAction.HandleHeap.OpenBag(dst, heap);
                    return true;
                }
            }

            return false;
        } else if (getCloser(dst)) {
            return true;
        } else {
            ready();

            return false;
        }
    }

	private boolean actPickUp( HeroAction.HandleHeap.PickUp action ) {
		final int dst = action.dst;

        GLog.d("actPickUp go");

		if (pos == dst) {
            final Item item = action.item;
            String pickupMessage = null;

            if (item instanceof Gemstone.Loadstone) {
                action.forced = true;
            }

            Encumbrance encumbrance = buff(Encumbrance.class);
            Encumbrance.EncumbranceLevel encumbranceLevel = encumbrance.testWeight(item.weight() * item.quantity());

            if (encumbranceLevel == Encumbrance.EncumbranceLevel.OVERLOADED) {
                Encumbrance.EncumbranceLevel testLevel = encumbrance.current;
                if (testLevel != encumbranceLevel) {
                    encumbranceLevel = Encumbrance.EncumbranceLevel.OVERTAXED;
                }
            }

            if (!action.forced) {
                pickupMessage = Encumbrance.getPickUpMessage(encumbranceLevel);

                switch (encumbranceLevel) {
                    case STRAINED:
                    case OVERTAXED:
                        ShatteredPixelDungeon.scene().add(
                                new WndOptions("Pick Up Item", Utils.format(pickupMessage, item.getDisplayName()) + "  Continue?",
                                        Constant.Action.YES,
                                        Constant.Action.CANCEL) {

                                    @Override
                                    protected void onSelect(int index) {
                                        switch (index) {
                                            case 0:
                                                curAction = new HeroAction.HandleHeap.PickUp(dst, item, true);
                                                motivate(true);
                                                break;
                                        }
                                    }
                                });
                        ready();
                        return false;
                    case OVERLOADED:
                        GLog.n(pickupMessage, item.getDisplayName());
                        ready();
                        return false;
                }
            }

            GLog.d("item.dopickup...");
            if (item.doPickUp(this)) {
                if (item instanceof Dewdrop
                        || item instanceof TimekeepersHourglass.sandBag
                    /*|| item instanceof DriedRose.Petal*/) {
                    //Do Nothing
                } else {
                    switch (encumbranceLevel) {
                        case BURDENED:
                        case STRESSED:
                        case STRAINED:
                        case OVERTAXED:
                            GLog.w(pickupMessage, item.getDisplayName());
                            break;
                        default:
                            GLog.i(pickupMessage, item.getDisplayName());
                            break;
                    }
                }

                curAction = null;
            } else {
                Dungeon.level.drop(item, pos).sprite.drop();
                ready();
            }

			return false;
		} else {
            return actHandleHeap(action);
		}
	}

    private boolean actOpenBag(HeroAction.HandleHeap.OpenBag action){
        int dst = action.dst;
        if (Level.canReach(pos, dst) || pos == dst) {
            IBag bag = action.bag;

            if (bag.locked()) {

            }
            else {
                GameScene.show(new WndBag(bag, null, WndBackpack.Mode.ALL, null, dst));
            }

            ready();

            return false;
        } else {
            return actHandleHeap(action);
        }
    }

    private boolean actInteractItem(HeroAction.HandleHeap.InteractItem action) {
        final int dst = action.dst;
        if (Level.canReach(pos, dst) || pos == dst) {
            final Item item = action.item;

            GameScene.show(new WndItem(null, item, new WndBackpack.Listener() {
                @Override
                public void onSelect(Item item) {
                    curAction = new HeroAction.HandleHeap.PickUp(dst, item);
                    motivate(true);
                }
            }));

            ready();

            return false;
        } else {
            return actHandleHeap(action);
        }
    }

    private boolean actOpenChest( HeroAction.OpenChest action ) {
		int dst = action.dst;
		if (Level.canReach(pos, dst) || pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {
				/*
				theKey = null;
				
				if (heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST) {

					theKey = belongings.getKey( GoldenOldKey.class, Dungeon.depth );
					
					if (theKey == null) {
						GLog.w( TXT_LOCKED_CHEST );
						ready();
						return false;
					}
				}
				*/

				switch (heap.type) {
				case TOMB:
					Sample.INSTANCE.play( Assets.SND_TOMB );
					Camera.main.shake( 1, 0.5f );
					break;
				case SKELETON:
				case REMAINS:
					break;
				default:
					Sample.INSTANCE.play( Assets.SND_UNLOCK );
				}
				
				spend_new(OldKey.TIME_TO_UNLOCK, false);
				sprite.operate( dst );
				
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actOpenCloseDoor(final HeroAction.HandleDoor.OpenCloseDoor action) {
		final int doorCell = action.dst;
		if (Level.canStep(pos, doorCell, Level.diagonal)) {
			int door = Dungeon.level.map[doorCell];
			final Hero hero = this;

			if (Random.PassFail(280 - 512 / STRCON())) {
				if (door == Terrain.DOOR) {
					Door.open(doorCell);
				} else if (door == Terrain.OPEN_DOOR) {
					Door.close(doorCell);
				}
			} else {
				GLog.w("The door resists!");
				useAttribute(AttributeType.STRCON, 1);
			}

			spend_new(OldKey.TIME_TO_UNLOCK, true);
			ready();

			return false;
		} else if (getCloser(doorCell)) {
			return true;
		} else {
			ready();

			return false;
		}
	}

	private boolean actKickDoor(final HeroAction.HandleDoor.KickDoor action) {
		final int doorCell = action.dst;
		if (Level.canStep(pos, doorCell, Level.diagonal)) {
			int door = Dungeon.level.map[doorCell];
			final Hero hero = this;

            int strcon_squared = STRCON();
            strcon_squared *= strcon_squared;

			if (Random.PassFail(strcon_squared)) {
				if (Random.PassFail(strcon_squared - 256)) {
					GLog.p("As you kick the door, it shatters to pieces!");
					Door.smash(doorCell);
				} else {
					GLog.p("As you kick the door, it crashes open!");
					Door.kickOpen(doorCell);
				}
				useAttribute(AttributeType.STRCON, 1);
			} else {
				GLog.n("WHAMMM!!!");
				useAttribute(AttributeType.DEXCHA, 1);
				Door.hit(doorCell);
			}

			spend_new(OldKey.TIME_TO_UNLOCK, false);
			sprite.attack(doorCell);

			return false;
		} else if (getCloser(doorCell)) {
			return true;
		} else {
			ready();

			return false;
		}
	}

	private boolean actUnlockDoor(final HeroAction.HandleDoor.UnlockDoor action) {
		final int doorCell = action.dst;
		if (Level.canStep(pos, doorCell, Level.diagonal)) {
			final Hero hero = this;

			action.successful = action.tool.unlockDoor(hero);

			sprite.operate(doorCell);

			if (action.successful) {
				GLog.p("You pick the lock.");
				Door.unlock(doorCell);
			} else {
				GLog.n("You fail to pick the lock.");
			}

			useAttribute(AttributeType.DEXCHA, 1);
			spend_new(OldKey.TIME_TO_UNLOCK, false);
			ready();

			return false;
		} else if (getCloser(doorCell)) {
			return true;
		} else {
			ready();

			return false;
		}
	}

	private boolean actHandleDoor( final HeroAction.HandleDoor action ) {
		final int doorCell = action.dst;
		if (Level.canStep(pos, doorCell, Level.diagonal)) {
			int door = Dungeon.level.map[doorCell];
			final Hero hero = this;

			if (door == Terrain.LOCKED_DOOR) {
				final List<String> actions = new ArrayList<>();
				List<Boolean> actionOptions = new ArrayList<>();

				if (belongings.boots() != null) {
					actions.add(Constant.Action.KICK);
					actionOptions.add(false);
				}

				ITool[] tools = belongings.getToolTypes(true, true, UnlockingTool.NAME, Pickaxe.NAME);

				UnlockingTool unlockingTool = null;
				String unlockingToolName = null;
				if (tools[0] != null) {
					unlockingTool = (UnlockingTool)tools[0];
					unlockingToolName = unlockingTool.getName().toUpperCase();

					actions.add(unlockingToolName);
					actionOptions.add(false);
				}

				Pickaxe diggingTool = null;
				String diggingToolName = null;
				if (tools[1] != null) {
					diggingTool = (Pickaxe)tools[1];
					diggingToolName = diggingTool.getName().toUpperCase();

					actions.add(diggingToolName);
					actionOptions.add(false);
				}


				actions.add(Constant.Action.CANCEL);
				actionOptions.add(true);


				final int actionsLength = actions.size();

				if (actionsLength > 1) {
					final UnlockingTool unlockingTool_WTFJAVA = unlockingTool;
					final String unlockingToolName_WTFJAVA = unlockingToolName;
					final Pickaxe diggingTool_WTFJAVA = diggingTool;
					final String diggingToolName_WTFJAVA = diggingToolName;

					ShatteredPixelDungeon.scene().add(
							new WndOptions("Locked Door", TXT_LOCKED_DOOR,
									actions.toArray(new String[actionsLength]),
									actionOptions.toArray(new Boolean[actionsLength])) {

								@Override
								protected void onSelect(int index) {
									String selection = index >= 0 ? actions.get(index) : "";

									if (selection.equals(Constant.Action.KICK)) {
										curAction = new HeroAction.HandleDoor.KickDoor(doorCell);
										motivate(true);
									} else if (selection.equals(unlockingToolName_WTFJAVA)) {
										curAction = new HeroAction.HandleDoor.UnlockDoor(doorCell, unlockingTool_WTFJAVA);
										motivate(true);
									} else if (selection.equals(diggingToolName_WTFJAVA)) {
                                        if (diggingTool_WTFJAVA.equipIfNecessary(Hero.this) != EquippableItem.EquipIfNecessaryState.NotEquipped) {
                                            curAction = new HeroAction.Dig(diggingTool_WTFJAVA, doorCell, 101);
                                            motivate(true);
                                        }
                                        else {
                                            ready();
                                        }
									} else {
										GLog.i("You leave the door alone.");
										ready();
									}
								}
							});
				}
				else {
					GLog.n(TXT_LOCKED_DOOR);
					ready();
				}
				return false;
			} else if (door == Terrain.LOCKED_EXIT) {
				/*
				theKey = belongings.getKey( SkeletonOldKey.class, Dungeon.depth );

                if (theKey != null) {

                    spend( OldKey.TIME_TO_UNLOCK, false );
                    sprite.operate( doorCell );

                    Sample.INSTANCE.play( Assets.SND_UNLOCK );

                } else {
                */
				GLog.w(TXT_LOCKED_DOOR);
				ready();
                /*}*/
			} else if (door == Terrain.DOOR || door == Terrain.OPEN_DOOR) {
				curAction = new HeroAction.HandleDoor.OpenCloseDoor(doorCell);
				return true;
			}

			return false;
		} else if (getCloser(doorCell)) {
			return true;
		} else {
			ready();

			return false;
		}
	}

    private boolean actMoveLevel(HeroAction.MoveLevel action) {
        int stairs = action.dst;

        if (pos == stairs) {
            /*
            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] ))
                if (mob instanceof DriedRose.GhostHero) mob.destroy();
            */

            Hunger hunger = buff(Hunger.class);
            if (hunger != null) {
                hunger.satisfy_new(-2);
            }

            Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();

            curAction = null;

            DungeonBranch branch = DungeonBranch.returnBranch(Dungeon._depth, action.direction);

            if (branch != null) {
                InterlevelScene.teleportDepth = branch.branchLevel();

                if (action.direction > 0) {
                    InterlevelScene.teleportPos = Constant.Position.ENTRANCE_ALTERNATE;
                } else if (action.direction < 0) {
                    InterlevelScene.teleportPos = Constant.Position.EXIT_ALTERNATE;
                }
            } else {
                if (action.alternate) {
                    branch = DungeonBranch.destinationBranch(Dungeon._depth);

                    if (branch.branchDown) {
                        InterlevelScene.teleportDepth = branch.levelMin;
                    }
                    else {
                        InterlevelScene.teleportDepth = branch.levelMax;
                    }
                }
                else {
                    InterlevelScene.teleportDepth = Dungeon._depth + action.direction;
                }

                if (action.direction > 0) {
                    InterlevelScene.teleportPos = Constant.Position.ENTRANCE;
                } else if (action.direction < 0) {
                    InterlevelScene.teleportPos = Constant.Position.EXIT;
                }
            }

            if (action.direction > 0) {
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                Game.switchScene(InterlevelScene.class);
            } else if (action.direction < 0) {
                if (Dungeon._depth == 1) {
                    if (belongings.getItem(Amulet.class) == null) {
                        GameScene.show(new WndMessage(TXT_LEAVE));
                        ready();
                    } else {
                        Dungeon.win(ResultDescriptions.WIN);
                        Dungeon.deleteGame(WndInitHero.savedGameIndex, false, true);
                        Game.switchScene(SurfaceScene.class);
                    }
                } else {
                    InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                    Game.switchScene(InterlevelScene.class);
                }
            } else {
                GLog.d("whut" + 1 / 0);
            }

            return false;
        } else if (getCloser(stairs)) {
            return true;
        } else {
            ready();
            return false;
        }
    }
	
	private boolean actAttack( HeroAction.Attack action ) {
        EquippableItem item = belongings.weapon();

        if (item instanceof RangedWeapon) {
            EquippableItem ammo = belongings.offhand();

            if (ammo instanceof AmmunitionWeapon) {
                action.launcher = (RangedWeapon) item;
                action.ammo = (AmmunitionWeapon) ammo;

                return actAttackRanged(action);
            }
        }
        else if (item instanceof MissileWeapon) {
            action.missile = (MissileWeapon)item;
            return actAttackMissile(action);
        }

        return actAttackMelee(action);
	}

    private boolean actAttackMelee(HeroAction.Attack action) {
        enemy = action.target;

        useAttribute(AttributeType.STRCON, 1);

        if (Level.canReach(pos, enemy.pos) && enemy.isAlive() && !isCharmedBy( enemy )) {
            Hunger hunger = buff(Hunger.class);
            hunger.satisfy_new(-1);
            Encumbrance encumbrance = buff(Encumbrance.class);
            encumbrance.isAttacking();

            spend_new(attackSpeed(), false);
            sprite.attack(enemy.pos);

            return false;
        } else {
            if (Level.fieldOfView[enemy.pos] && getCloser( enemy.pos )) {
                return true;
            } else {
                ready();
                return false;
            }
        }
    }

    private boolean actAttackRanged(HeroAction.Attack action) {
        action.launcher.shoot(this, action.ammo, action.target.pos);
        ready();
        return false;
    }

    private boolean actAttackMissile(HeroAction.Attack action) {
        action.missile.cast(this, action.target.pos);
        ready();
        return false;
    }
	
	public void rest( boolean fullRest ) {
		spend_new(Constant.Time.HERO_REST, true);
		if (!fullRest) {
			sprite.showStatus(CharSprite.DEFAULT, TXT_WAIT);
		}
		resting = fullRest;
	}


	/*
	@Override
	public int attackProc( KindOfWeapon weapon, boolean thrown, Char enemy, int damage ) {
		damage = super.attackProc(weapon, thrown, enemy, damage);

		switch (subClass) {
		case GLADIATOR:
			if (weapon instanceof MeleeWeapon || weapon == null) {
				damage += Buff.affect( this, Combo.class ).hit( enemy, damage );
			}
			break;
		case SNIPER:
			if (thrown) {
				Buff.prolong( this, SnipersMark.class, weapon.attackDelay(thrown) * 1.1f ).object = enemy.id();
			}
			break;
		default:
		}

		return damage;
	}
	*/

	@Override
	public int defenseProc( Char enemy, int damage ) {
		
		Earthroot.Armor earthroot = buff( Earthroot.Armor.class );
		if (earthroot != null) {
			damage = earthroot.absorb(damage);
		}

		Sungrass.Health health = buff( Sungrass.Health.class );
		if (health != null) {
			health.absorb(damage);
		}

        Armor armor = (Armor)belongings.armor();
		if (armor != null) {
			damage = armor.proc(enemy, this, damage);
		}
		
		return damage;
	}
	
	@Override
	public int damage(int dmg, MagicType type, Actor source) {
		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return 0;

		//todo: make sure this is right
		//if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage || src instanceof Food) && damageInterrupt) {
		if (source != null) {
			interrupt();
			resting = false;
		}

		if (this.buff(Drowsy.class) != null){
			Buff.detach(this, Drowsy.class);
			GLog.w("The pain helps you resist the urge to sleep.");
		}

		CapeOfThorns.Thorns thorns = buff( CapeOfThorns.Thorns.class );
		if (thorns != null) {
			dmg = thorns.proc(dmg, source, this);
		}

		int tenacity = 0;
		for (Buff buff : buffs(RingOfTenacity.Tenacity.class)) {
			tenacity += ((RingOfTenacity.Tenacity)buff).level;
		}
		if (tenacity != 0) //(HT - HP)/HT = heroes current % missing health.
			dmg = (int)Math.ceil((float)dmg * Math.pow(0.9, tenacity*((float)(HT - HP)/HT)));

		dmg = super.damage(dmg, type, source);
		
		if (subClass == HeroSubClass.BERSERKER && 0 < HP && HP <= HT * Fury.LEVEL) {
			Buff.affect( this, Fury.class );
		}

        return dmg;
	}
	
	private void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		Mob target = null;
		for (Mob m : Dungeon.level.mobs) {
			if (Level.fieldOfView[ m.pos ] && m.hostile) {
				visible.add(m);
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}

				if (QuickSlotButton.autoAim(m) != -1){
					if (target == null){
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
				}
			}
		}

		if (target != null && (QuickSlotButton.lastTarget == null ||
				!QuickSlotButton.lastTarget.isAlive() ||
				!Dungeon.visible[QuickSlotButton.lastTarget.pos])){
			QuickSlotButton.target(target);
		}
		
		if (newMob) {
			interrupt();
			resting = false;
		}
		
		visibleEnemies = visible;
	}
	
	public int visibleEnemies() {
		return visibleEnemies.size();
	}
	
	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}

    protected boolean[] getCloserStepCandidate() {
        int len = Level.LENGTH;
        boolean[] retval = new boolean[len];

        boolean[] passable = Level.passable;
        boolean[] pathable = Level.pathable;

        boolean[] visited = Dungeon.level.visited;
        boolean[] mapped = Dungeon.level.mapped;
        boolean[] avoid = Dungeon.level.avoid;

        for (int i=0; i < len; i++) {
            retval[i] = (passable[i] || pathable[i]) && (visited[i] || mapped[i]) && (!avoid[i]);
        }

        return retval;
    }
	
	private boolean getCloser( final int target ) {
		long speed = movementSpeed();

		if (speed == 0) {
			Camera.main.shake( 1, 1f );
			return false;
		}
		
		int step = -1;
		
		if (Level.canStep(pos, target, Level.diagonal)) {
			if (Actor.findChar( target ) == null) {
				if (Level.pit[target] && !flying && !Level.solid[target]) {
					if (!Chasm.jumpConfirmed){
						Chasm.heroJump(this);
						interrupt();
					} else {
						Chasm.heroFall(target);
					}
					return false;
				}
				if (Level.passable[target] || Level.avoid[target]) {
					step = target;
				}
			}
		} else {
            boolean[] candidate = getCloserStepCandidate();
            boolean[] diagonal = Level.diagonal;

			step = Dungeon.findPath( this, pos, target, candidate, diagonal, Level.fieldOfView );
		}

		if (step != -1 && step != pos) {
            //GLog.d("step");
            //GLog.d(pos, step);
            if (Dungeon.level.map[step] == Terrain.DOOR){
				if (!Random.PassFail(280 - 512 / STRCON())) {
    	            GLog.w("Ouch! You bump into a door.");
					useAttribute(AttributeType.DEXCHA, -1);
				}
                return false;
            }

            sprite.move(pos, step);
            move(step);
			attributeIncreaseCheck();
			spend_new(speed, false);

            return true;
		} else {
			return false;
		}
	}
	
	public boolean handle(int cell, boolean fromGameScene) {
		if (cell == -1) {
			return false;
		}

		Char ch;
		Heap heap;
		ITool[] tools;

		if (Dungeon.level.map[cell] == Terrain.ALCHEMY) {
            curAction = new HeroAction.InteractPosition.Cook(cell);
        } else if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.map[cell] == Terrain.WELL_MAGIC) {
            curAction = new HeroAction.InteractPosition.Well(cell);
		} else if (Level.fieldOfView[cell] && (ch = Actor.findChar(cell)) instanceof Mob) {
			if (ch instanceof NPC) {
				curAction = new HeroAction.Interact((NPC) ch);
			} else {
                curAction = new HeroAction.Attack(ch);
			}
		} else if ((heap = Dungeon.level.heaps.get(cell)) != null && !Dungeon.level.solid[cell]) {
			switch (heap.type) {
				case HEAP:
                    if ((!fromGameScene) || ShatteredPixelDungeon.gameplay_autoPickup()) {
                        curAction = new HeroAction.HandleHeap(cell);
                    }
                    else {
                        curAction = new HeroAction.Move(cell);
                    }
					break;
				case FOR_SALE:
					curAction = heap.size() == 1 && heap.peek().price() > 0 ?
							new HeroAction.Buy(cell) :
							new HeroAction.HandleHeap(cell);
					break;
				default:
					curAction = new HeroAction.OpenChest(cell);
			}
		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT || Dungeon.level.map[cell] == Terrain.DOOR || Dungeon.level.map[cell] == Terrain.OPEN_DOOR) {
			curAction = new HeroAction.HandleDoor(cell);
		} else if (cell == Dungeon.level.exit) {
			curAction = new HeroAction.MoveLevel(cell, 1, false);
		} else if (cell == Dungeon.level.entrance) {
            curAction = new HeroAction.MoveLevel(cell, -1, false);
        } else if (cell == Dungeon.level.exitAlternate) {
            curAction = new HeroAction.MoveLevel(cell, 1, true);
        } else if (cell == Dungeon.level.entranceAlternate) {
            curAction = new HeroAction.MoveLevel(cell, -1, true);
		} else if (Dungeon.level.solid[cell] && ((tools = belongings.getToolTypes(true, false, Pickaxe.NAME)) != null && tools[0] != null)) {
			curAction = new HeroAction.Dig((Pickaxe) tools[0], cell, 101);
		} else {
			curAction = new HeroAction.Move(cell);
			lastAction = null;
		}

		//GLog.d("handle() set curAction="+(curAction != null ? curAction.getClass().toString() : "null"));

		return act();
	}
	
	public void earnExp( int exp ) {
		
		this.exp += exp;
		float percent = exp/(float)maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if (chains != null) chains.gainExp(percent);
		
		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();
			if (level < MAX_LEVEL) {
				level++;
				levelUp = true;

				//todo: level up bonuses

				//HT += 5;
				//HP += 5;
				//attackSkill++;
				//defenseSkill++;

			} else {
				Buff.prolong(this, Bless.class, 30 * GameTime.TICK);
				this.exp = 0;

				GLog.p( "You cannot grow stronger, but your experiences do give you a surge of power!");
				Sample.INSTANCE.play( Assets.SND_LEVELUP );
			}
			
			if (level < 10) {
				updateAwareness();
			}
		}
		
		if (levelUp) {
			
			GLog.p( TXT_NEW_LEVEL, level );
			sprite.showStatus( CharSprite.POSITIVE, TXT_LEVEL_UP );
			Sample.INSTANCE.play( Assets.SND_LEVELUP );
			
			Badges.validateLevelReached();
		}
	}
	
	public int maxExp() {
        if (level < 20) {
            return (2 << level) * 10;
        } else if (level < 30) {
            return (level - 19) * 10000000;
        } else {
            return Integer.MAX_VALUE;
        }
    }
	
	void updateAwareness() {
		awareness = (float)(1 - Math.pow(
			(heroClass == HeroClass.ROGUE ? 0.85 : 0.90),
			(1 + Math.min( level,  9 )) * 0.5
		));
	}
	
	public boolean isStarving() {
		return buff(Hunger.class) != null && ((Hunger)buff( Hunger.class )).isStarving();
	}
	
	@Override
	public void add( Buff buff ) {
		if (buff(TimekeepersHourglass.timeStasis.class) != null) {
			return;
		}

		super.add(buff);

		if (sprite != null) {
            String message = buff.attachedMessage(true);

            if (message != null) {
                GLog.w(message);
            }

			if (buff instanceof Burning) {
				interrupt();
			} else if (buff instanceof Paralysis) {
				interrupt();
			} else if (buff instanceof Poison) {
				interrupt();
			} else if (buff instanceof DeathlySick) {
				interrupt();
			} else if (buff instanceof Ooze) {
                interrupt();
			} else if (buff instanceof RingOfMight.Might){
				if (((RingOfMight.Might)buff).level > 0) {
					HT += ((RingOfMight.Might) buff).level * 5;
				}
			} else if (buff instanceof Vertigo) {
				interrupt();
			}

		}
		
		BuffIndicator.refreshHero();
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove(buff);
		
		if (buff instanceof RingOfMight.Might){
			if (((RingOfMight.Might)buff).level > 0){
				HT -= ((RingOfMight.Might) buff).level * 5;
				HP = Math.min(HT, HP);
			}
		}
		
		BuffIndicator.refreshHero();
	}
	
	@Override
	public int stealth() {
		int stealth = super.stealth();
		for (Buff buff : buffs( RingOfEvasion.Evasion.class )) {
			stealth += ((RingOfEvasion.Evasion)buff).effectiveLevel;
		}
		return stealth;
	}
	
	@Override
	public void die( Actor cause  ) {
		
		curAction = null;

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for (Item item : belongings){
			if (item instanceof Ankh) {
				if (ankh == null || ((Ankh) item).isBlessed()) {
					ankh = (Ankh) item;
				}
			}
		}

		if (ankh != null && ankh.isBlessed()) {
			this.HP = HT/4;

			//ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dieing again.
			Buff.detach(this, Paralysis.class);
			spend_new(-cooldown(), false);

			new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
			CellEmitter.get(this.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

			belongings.remove(ankh, 1);

			Sample.INSTANCE.play( Assets.SND_TELEPORT );
			GLog.w( ankh.TXT_REVIVE );
			Statistics.ankhsUsed++;

			return;
		}

		GameTime.fix();
		super.die(cause);

		if (ankh == null) {
			
			reallyDie( cause );
			
		} else {
			
			Dungeon.deleteGame( WndInitHero.savedGameIndex, true, false );
			GameScene.show(new WndResurrect(ankh, cause));
			
		}
	}
	
	public static void reallyDie( Object cause ) {
		
		int length = Level.LENGTH;
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Level.discoverable;
		
		for (int i=0; i < length; i++) {
			
			int terr = map[i];
			
			if (discoverable[i]) {
				
				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.FLAG_SECRET) != 0) {
					Dungeon.level.discover( i );
				}
			}
		}
		
		Bones.leave();
		
		Dungeon.observe();
				
		Dungeon.hero.belongings.identify();

		GameScene.gameOver();
		
		if (cause instanceof Hero.Doom) {
			((Hero.Doom)cause).onDeath();
		}
		
		Dungeon.deleteGame( WndInitHero.savedGameIndex, true, true );
	}
	
	@Override
	public void move( int step ) {
		super.move(step);
		
		if (!flying) {
			if (Level.water[pos]) {
				Sample.INSTANCE.play( Assets.SND_WATER, 1, 1, Random.Float( 0.8f, 1.25f ) );
			} else {
				Sample.INSTANCE.play( Assets.SND_STEP );
			}
			Dungeon.level.press(pos, this);
		}
	}
	
	@Override
	public void onMotionComplete() {
		Dungeon.observe();
		search(false);
			
		super.onMotionComplete();
	}
	
	@Override
	public void onAttackComplete() {
		
		AttackIndicator.target(enemy);

		if (curAction instanceof HeroAction.Attack) {
			HeroAction.Attack action = (HeroAction.Attack) curAction;
			attack(enemy);
		}
		curAction = null;
		
		Invisibility.dispel();

		super.onAttackComplete();
	}
	
	@Override
	public void onOperateComplete() {
		GLog.d("onOperateComplete");
		/*
		if (curAction instanceof HeroAction.HandleDoor) {
			HeroAction.HandleDoor action = (HeroAction.HandleDoor)curAction;

			//if (theKey != null) {
			//	belongings.detach(theKey);
			//	theKey = null;
			//}

			if (action.successful) {
				int doorCell = ((HeroAction.HandleDoor) curAction).dst;
				int door = Dungeon.level.map[doorCell];

				Level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT);
				GameScene.updateMap(doorCell);
			}
		} else */
			if (curAction instanceof HeroAction.OpenChest) {
			/*
			if (theKey != null) {
				belongings.detach(theKey);
				theKey = null;
			}
			*/
			Heap heap = Dungeon.level.heaps.get( ((HeroAction.OpenChest)curAction).dst );
			if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
				Sample.INSTANCE.play( Assets.SND_BONES );
			}
			heap.open( this );
		}
		curAction = null;

		super.onOperateComplete();
	}
	
	public boolean search( boolean intentional ) {

        boolean smthFound = false;

        int positive = 0;
        int negative = 0;

        int distance = 1 + positive + negative;

        float level = intentional ? (2 * awareness - awareness * awareness) : awareness;
        if (distance <= 0) {
            level /= 2 - distance;
            distance = 1;
        }

        int cx = pos % Level.WIDTH;
        int cy = pos / Level.WIDTH;
        int ax = cx - distance;
        if (ax < 0) {
            ax = 0;
        }
        int bx = cx + distance;
        if (bx >= Level.WIDTH) {
            bx = Level.WIDTH - 1;
        }
        int ay = cy - distance;
        if (ay < 0) {
            ay = 0;
        }
        int by = cy + distance;
        if (by >= Level.HEIGHT) {
            by = Level.HEIGHT - 1;
        }

        TalismanOfForesight.Foresight foresight = buff(TalismanOfForesight.Foresight.class);

        //cursed talisman of foresight makes unintentionally finding things impossible.
        if (foresight != null && foresight.isCursed()) {
            level = -1;
        }

        for (int y = ay; y <= by; y++) {
            for (int x = ax, p = ax + y * Level.WIDTH; x <= bx; x++, p++) {

                if (Dungeon.visible[p]) {

                    if (intentional) {
                        sprite.parent.addToBack(new CheckedCell(p));
                    }

                    if (Level.secret[p] && (intentional || Random.Float() < level)) {

                        int oldValue = Dungeon.level.map[p];

                        GameScene.discoverTile(p, oldValue);

                        Dungeon.level.discover(p);

                        ScrollOfMagicMapping.discover(p);

                        smthFound = true;

                        if (foresight != null && !foresight.isCursed())
                            foresight.charge();
                    }
                }
            }
        }


        if (intentional) {
            sprite.showStatus(CharSprite.DEFAULT, TXT_SEARCH);
            sprite.operate(pos);
            if (foresight != null && foresight.isCursed()) {
                GLog.n("You can't concentrate, searching takes a while.");
                spend_new(Constant.Time.HERO_SEARCH * 3, true);
            } else {
                spend_new(Constant.Time.HERO_SEARCH, true);
            }

        }

        if (smthFound) {
            GLog.w(TXT_NOTICED_SMTH);
            Sample.INSTANCE.play(Assets.SND_SECRET);
            interrupt();
        }

        return smthFound;
    }
	
	public void resurrect( int resetLevel ) {
		
		HP = HT;
		//Dungeon.gold = 0;
		exp = 0;
		
		belongings.resurrect( resetLevel );

		live();
	}

	@Override
	public void next() {
		super.next();
	}

	public interface Doom {
		void onDeath();
	}
}
