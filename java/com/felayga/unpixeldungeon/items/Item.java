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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.SnipersMark;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.sprites.MissileSprite;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndItemDropMultiple;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Comparator;

public class Item implements Bundlable {
	private static final String TXT_TO_STRING = "%s";
	private static final String TXT_TO_STRING_X = "%s x%d";
	private static final String TXT_TO_STRING_LVL = "%s%+d";
	private static final String TXT_TO_STRING_LVL_X = "%s%+d x%d";

	public String defaultAction;
	public boolean usesTargeting;

	private IBag parent_whut = null;
    public IBag parent() { return parent_whut; }
    public void parent(IBag parent) { this.parent_whut = parent; }


	protected String name;
    protected String pickupSound;
	public int image;

	public boolean stackable = false;

	public boolean droppable = true;
	public boolean fragile = false;

    protected int price;

    public final int price() {
        int price = this.price;
        if (bucStatus == BUCStatus.Cursed) {
            price /= 2;
        }
        if (levelKnown) {
            if (level > 0) {
                price *= (level + 1);
            } else if (level < 0) {
                price /= (1 - level);
            }
        }
        if (price < 1) {
            price = 1;
        }

        return price * quantity;
    }

    private int weight;

    public int weight() {
        return weight;
    }

    public void weight(int newWeight) {
        if (weight == newWeight) {
            return;
        }

        int weightChange = (newWeight - weight) * quantity;

        weight = newWeight;

        if (parent() != null) {
            parent().onWeightChanged(weightChange);
        }
    }

    private int quantity;

    public int quantity() { return quantity; }

    public void quantity(int newQuantity) {
        if (quantity == newQuantity) {
            return;
        }

        int weightChange = (newQuantity - quantity) * weight;

        quantity = newQuantity;

        if (parent() != null) {
            parent().onWeightChanged(weightChange);
        }
    }

    public Item setQuantity(int newQuantity) {
        quantity(newQuantity);

        return this;
    }

    public Item() {
        name = "smth";
        image = ItemSpriteSheet.NULLWARN;
        pickupSound = Assets.SND_ITEM;
        quantity = 1;
        weight = 0;
        price = 0;
    }

	public int level = 0;
	public boolean levelKnown = false;
	public boolean hasLevels = true;

	protected BUCStatus bucStatus = BUCStatus.Uncursed;
	protected boolean bucStatusKnown = false;
	private boolean hasBuc = true;

	public BUCStatus bucStatus() {
		return bucStatus;
	}
	public boolean bucStatusKnown() { return bucStatusKnown; }
	public boolean hasBuc() { return hasBuc; }

	public Item bucStatus(Item item) {
		return bucStatus(item.bucStatus, item.bucStatusKnown);
	}

	public Item bucStatus(BUCStatus status) {
		return bucStatus(status, bucStatusKnown);
	}

	public Item bucStatus(boolean statusKnown) {
		return bucStatus(bucStatus, statusKnown);
	}

	public Item bucStatus(BUCStatus status, boolean statusKnown) {
		if (hasBuc) {
			bucStatus = status;
			bucStatusKnown = statusKnown;
		} else {
			bucStatus = BUCStatus.Uncursed;
			bucStatusKnown = true;
		}

		updateQuickslot();

		return this;
	}

	public void hasBuc(boolean state){
		if (hasBuc != state)
		{
			hasBuc = state;

			bucStatus(bucStatus, bucStatusKnown);
		}
	}

	// Unique items persist through revival
	public boolean unique = false;

	// whether an item can be included in heroes remains
	public boolean bones = false;

	public static Comparator<Item> itemComparator = new Comparator<Item>() {
		@Override
		public int compare(Item lhs, Item rhs) {
			return Generator.Category.order(lhs) - Generator.Category.order(rhs);
		}
	};

	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add(Constant.Action.DROP);
		actions.add(Constant.Action.THROW);
		return actions;
	}

    public final ArrayList<String> actionsExternal(Hero hero) {
        ArrayList<String> retval = actions(hero);

        for (int n=retval.size()-1;n>=0;n--) {
            if (!canPerformActionExternally(hero, retval.get(n))) {
                retval.remove(n);
            }
        }

        retval.add(0, Constant.Action.TAKE);

        return retval;
    }

    public final ArrayList<String> actions(Hero hero, boolean external) {
        if (external) {
            return actionsExternal(hero);
        }

        return actions(hero);
    }

    public boolean canPerformActionExternally(Hero hero, String action) {
        return Constant.Action.TAKE.equals(action);
    }

	public boolean doPickUp(Hero hero) {
		if (hero.flying) {
			GLog.w("You can't reach the floor.");
			return false;
		}

        IBag oldParent = parent();

        if (parent().remove(this) == this) {

            if (hero.belongings.collect(this)) {
                GameScene.pickUp(this);
                playPickupSound();
                hero.spend_new(Constant.Time.ITEM_PICKUP, true);
                return true;

            } else {
                oldParent.collect(this);
                return false;
            }
        }

        return false;
	}

	public final void playPickupSound() {
		Sample.INSTANCE.play(pickupSound);
	}

	public void doDrop(final Hero hero) {
		if (quantity > 1) {
			GameScene.show(new WndItemDropMultiple(this) {
				@Override
				public void doDrop(int quantity) {
                    if (quantity == Item.this.quantity) {
                        Dungeon.level.drop(hero.belongings.remove(Item.this), hero.pos).sprite.drop(hero.pos);
                    } else if (quantity > 0) {
                        Dungeon.level.drop(hero.belongings.remove(Item.this, quantity), hero.pos).sprite.drop(hero.pos);
                    } else {
                        GLog.w("You drop nothing.  The nothing clatters noisily as it impacts the ground.");
                    }
                    hero.spend_new(Constant.Time.ITEM_DROP, true);
                }
			});
		}
		else {
			Dungeon.level.drop(hero.belongings.remove(this), hero.pos).sprite.drop(hero.pos);
			hero.spend_new(Constant.Time.ITEM_DROP, true);
		}
	}

	public void syncVisuals() {
		//do nothing by default, as most items need no visual syncing.
	}

	public void doThrow(Hero hero) {
		GameScene.selectCell(thrower);
	}

	public boolean execute(Hero hero, String action) {
		curUser = hero;
		curItem = this;

		if (action.equals(Constant.Action.DROP)) {
			doDrop(hero);
		} else if (action.equals(Constant.Action.THROW)) {
			doThrow(hero);
		}

		return false;
	}

	public boolean execute(Hero hero) {
		return execute(hero, defaultAction);
	}

	protected void onThrow(int cell, Char thrower) {
		GLog.d("onthrow cell="+cell);
		Heap heap = Dungeon.level.drop(this, cell);
		if (heap.size() > 0) {
			heap.sprite.drop(cell);
		}
	}


    public final boolean isSimilar(Item item) {
		return this.checkSimilarity(item) && item.checkSimilarity(this);
	}

    public final boolean isStackableWith(Item item) {
        return stackable && item.stackable && checkSimilarity(item);
    }

    protected boolean checkSimilarity(Item item) {
        return getClass() == item.getClass() && (quantity == 0 || item.quantity == 0 || (bucStatus == item.bucStatus && bucStatusKnown == item.bucStatusKnown));
    }

	public void onDetach() {
	}


	public Item upgrade(Item source, int n) {
        GLog.d("upgrayedd");
		if (source != null) {
			GLog.d("upgrade item="+getDisplayName()+" because "+source.getDisplayName()+" said so ("+n+")");
			switch (source.bucStatus) {
				case Cursed:
					bucStatus = BUCStatus.Cursed;
					break;
				case Uncursed:
					if (bucStatus == BUCStatus.Cursed) {
						bucStatus = BUCStatus.Uncursed;
					}
					break;
				case Blessed:
					if (bucStatus == BUCStatus.Cursed) {
						bucStatus = BUCStatus.Uncursed;
					} else {
						bucStatus = BUCStatus.Blessed;
					}
					break;
			}

			if (source.bucStatusKnown) {
				bucStatusKnown = true;
			}
		}
		else {
			GLog.d("upgrade item="+getDisplayName()+" because NULL said so ("+n+")");
		}

		if (hasLevels) {
			this.level += n;
		} else {
			this.level = 0;
		}

		updateQuickslot();

		return this;
	}

	public int visiblyUpgraded() {
		return levelKnown ? level : 0;
	}

	public BUCStatus visibleBucStatus() {
		return bucStatusKnown ? bucStatus : BUCStatus.Unknown;
	}

	public boolean isUpgradable() {
		return true;
	}

	public boolean isIdentified() {
		return levelKnown && bucStatusKnown;
	}

	public boolean isEquipped(Char hero) {
		return false;
	}

	public Item identify()
	{
		return identify(false);
	}

	public Item identify(boolean updateQuickslot) {
		if (!levelKnown || !bucStatusKnown) {
			levelKnown = true;
			bucStatusKnown = true;

			updateQuickslot = true;
		}

		if (parent() != null && stackable) {
			if (parent().tryMergeStack(this)) {
				updateQuickslot = true;
			}
		}

		if (updateQuickslot)
		{
			updateQuickslot();
		}

		return this;
	}

	public static void evoke(Hero hero) {
		hero.sprite.emitter().burst(Speck.factory(Speck.EVOKE), 5);
	}

	@Override
	public String toString() {
		return "" + (1 / 0);
		/*
		//CAN I GET SOME FUCKING CONSISTENCY HERE
		if (levelKnown && level != 0) {
			if (quantity > 1) {
				return Utils.format( TXT_TO_STRING_LVL_X, name(), level, quantity );
			} else {
				return Utils.format( TXT_TO_STRING_LVL, name(), level );
			}
		} else {
			if (quantity > 1) {
				return Utils.format( TXT_TO_STRING_X, name(), quantity );
			} else {
				return Utils.format( TXT_TO_STRING, name() );
			}
		}
		*/
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		String retval = getName();

		if (hasBuc) {
			BUCStatus status = visibleBucStatus();

			if (status != BUCStatus.Unknown)
			{
				retval = BUCStatus.getName(status) + " " + retval;
			}
		}

		return retval;
	}

	public int image() {
		return image;
	}

	public ItemSprite.Glowing glowing() {
		return null;
	}

	public Emitter emitter() {
		return null;
	}

	public String info() {
		return desc();
	}

	public String desc() {
		return "";
	}

	public static Item virtual(Class<? extends Item> cl) {
		try {
			Item item = (Item) cl.newInstance();
			item.quantity = 0;
			return item;

		} catch (Exception e) {
			return null;
		}
	}

    public int randomEnchantmentMinimum = 0;
    public int randomEnchantmentMaximum = 5;

	public Item random() {
        int luck = Dungeon.hero.luck();
        float levelBonusChance = 0.5f + (luck - 2.0f) * 0.04f;

        level = randomEnchantmentMinimum;
        while (level < randomEnchantmentMaximum && Random.Float() < levelBonusChance) {
            level++;
        }

        if (Random.Float() <= 0.025f) {
            level = -level;
        }

        if (hasBuc) {
            float cursed = 1.0f - (float) Math.sqrt(levelBonusChance);
            float uncursed = (1.0f - cursed) / (1.0f + levelBonusChance) + cursed;

            float bucDetermination = Random.Float();

            if (bucDetermination <= cursed) {
                bucStatus(BUCStatus.Cursed);
                level = -level;
            }
            else if (bucDetermination <= uncursed) {
                bucStatus(BUCStatus.Uncursed);
            }
            else {
                bucStatus(BUCStatus.Blessed);
            }
        }

		return this;
	}

	public String status() {
		return quantity != 1 || stackable ? Integer.toString(quantity) : null;
	}

	public final void updateQuickslot() {
		QuickSlotButton.refresh();
	}

	private static final String QUANTITY = "quantity";
	private static final String LEVEL = "level";
	private static final String LEVEL_KNOWN = "levelKnown";
	private static final String BUCSTATUS = "bucStatus";
	private static final String BUCSTATUS_KNOWN = "bucStatusKnown";
	private static final String OLDSLOT = "quickslot";
	private static final String QUICKSLOT = "quickslotpos";
	private static final String DEFAULTACTION = "defaultAction";
    private static final String WEIGHT = "weight";

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(QUANTITY, quantity);
		bundle.put(LEVEL, level);
		bundle.put(LEVEL_KNOWN, levelKnown);
		bundle.put(BUCSTATUS, BUCStatus.ToInt(bucStatus));
		bundle.put(BUCSTATUS_KNOWN, bucStatusKnown);
		if (Dungeon.quickslot.contains(this)) {
			bundle.put(QUICKSLOT, Dungeon.quickslot.getSlot(this));
		}
		bundle.put(DEFAULTACTION, defaultAction);
        bundle.put(WEIGHT, weight);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		defaultAction = bundle.getString(DEFAULTACTION);
		quantity = bundle.getInt(QUANTITY);
		levelKnown = bundle.getBoolean(LEVEL_KNOWN);
		bucStatusKnown = bundle.getBoolean(BUCSTATUS_KNOWN);

		int level = bundle.getInt(LEVEL);
		if (level > 0) {
			upgrade(null, level);
		} else if (level < 0) {
			upgrade(null, -level);
		}

		bucStatus = BUCStatus.FromInt(bundle.getInt(BUCSTATUS));

		bucStatus(bucStatus, bucStatusKnown);

		//only want to populate slot on first load.
		if (Dungeon.hero == null) {
			//support for pre-0.2.3 saves and rankings
			if (bundle.contains(OLDSLOT)) {
				Dungeon.quickslot.setSlot(0, this);
			} else if (bundle.contains(QUICKSLOT)) {
				Dungeon.quickslot.setSlot(bundle.getInt(QUICKSLOT), this);
			}
		}
        weight = bundle.getInt(WEIGHT);
	}

	public void cast(final Hero user, int dst) {
		final int cell = new Ballistica(user.pos, dst, Ballistica.PROJECTILE).collisionPos;
		user.sprite.zap(cell);
		user.busy();

        //todo: make sure throwing item weights are right, etc.

		Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);

		Char enemy = Actor.findChar(cell);
		QuickSlotButton.target(enemy);

		// FIXME!!!
		long delay = Constant.Time.ITEM_THROW;
		if (this instanceof MissileWeapon) {
			//delay *= ((MissileWeapon)this).speedFactor( user );
			if (enemy != null) {
				SnipersMark mark = user.buff(SnipersMark.class);
				if (mark != null) {
					if (mark.object == enemy.id()) {
						delay /= 2;
					}
					user.remove(mark);
				}
			}
		}
		final long finalDelay = delay;

		((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
				reset(user.pos, cell, this, new Callback() {
					@Override
					public void call() {
						user.belongings.remove(Item.this, 1).onThrow(cell, user);
						user.spend_new(finalDelay, true);
					}
				});
	}

	protected static Hero curUser = null;
	protected static Item curItem = null;
	protected static CellSelector.Listener thrower = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer target) {
			GLog.d("onselectcell target="+target);
			if (target != null) {
				curItem.cast(curUser, target);
			}
		}

		@Override
		public String prompt() {
			return "Choose direction of throw";
		}
	};
}
