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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.SnipersMark;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.levels.Level;
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
    private static final String TXT_IDENTIFY = "You are now familiar enough with your %s to identify it.";

	public String defaultAction;
	public boolean usesTargeting;

	private IBag parent_whut = null;
    public IBag parent() {
        return parent_whut;
    }
    public void parent(IBag parent) {
        this.parent_whut = parent;
    }


	protected String name;
    protected String pickupSound;
	public int image;

	public boolean stackable = false;

	public boolean droppable = true;
	public boolean fragile = false;
    public boolean shopkeeperPriceJacked = false;

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

        return price;
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

    public Item quantity(int newQuantity) {
        if (quantity != newQuantity) {
            int weightChange = (newQuantity - quantity) * weight;

            quantity = newQuantity;

            if (parent() != null) {
                parent().onWeightChanged(weightChange);
            }
        }

        return this;
    }

    public Item() {
        name = "smth";
        image = ItemSpriteSheet.NULLWARN;
        pickupSound = Assets.SND_ITEM;
        quantity = 1;
        weight = 0;
        price = 0;
        shopkeeperPriceJacked = Random.Int(4) == 0;
    }

	private int level = 0;
	private boolean levelKnown = false;
	private boolean hasLevels = true;

    public int level() {
        return level;
    }
    public Item level(int level) {
        return level(level, levelKnown);
    }
    public Item level(int level, boolean levelKnown) {
        if (hasLevels) {
            this.level = level;
            this.levelKnown = levelKnown;
        } else {
            this.level = 0;
            this.levelKnown = true;
        }

        updateQuickslot();

        return this;
    }

    public boolean levelKnown() {
        return levelKnown;
    }
    public void levelKnown(boolean state, boolean verbose) {
        if (levelKnown != state) {
            levelKnown = state;

            if (state && verbose) {
                GLog.i(TXT_IDENTIFY, getDisplayName());
            }
        }
    }

    public boolean hasLevels() { return hasLevels; }
    public void hasLevels(boolean state) {
        if (hasLevels != state) {
            hasLevels = state;

            level(level, levelKnown);
        }
    }

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
		if (hasBuc != state) {
			hasBuc = state;

			bucStatus(bucStatus, bucStatusKnown);
		}
	}

    private int shopkeeperRegistryIndex = -1;

    public int shopkeeperRegistryIndex() {
        return shopkeeperRegistryIndex;
    }

    public void shopkeeper(Shopkeeper shopkeeper) {
        shopkeeperRegistryIndex = shopkeeper.shopkeeperRegistryIndex();
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
		if (hero.flying()) {
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
                        Dungeon.level.drop(hero.belongings.remove(Item.this), hero.pos()).sprite.drop(hero.pos());
                    } else if (quantity > 0) {
                        Dungeon.level.drop(hero.belongings.remove(Item.this, quantity), hero.pos()).sprite.drop(hero.pos());
                    } else {
                        GLog.w("You drop nothing.  The nothing clatters noisily as it impacts the ground.");
                    }
                    hero.spend_new(Constant.Time.ITEM_DROP, true);
                }
			});
		}
		else {
			Dungeon.level.drop(hero.belongings.remove(this), hero.pos()).sprite.drop(hero.pos());
			hero.spend_new(Constant.Time.ITEM_DROP, true);
		}
	}

	public void syncVisuals() {
		//do nothing by default, as most items need no visual syncing.
	}

	public void doThrow(Hero hero) {
        GLog.d("Item: doThrow");
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

	protected void onThrow(Char thrower, int cell) {
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
        return stackable && item.stackable && checkSimilarity(item) && this.shopkeeperRegistryIndex == item.shopkeeperRegistryIndex;
    }

    protected boolean checkSimilarity(Item item) {
        return getClass() == item.getClass() && (quantity == 0 || item.quantity == 0 || (bucStatus == item.bucStatus && bucStatusKnown == item.bucStatusKnown));
    }

	public void onDetach() {
	}

    public static Item ghettoSplitStack(Item item, int quantity, Char owner) {
        try {
            //pssh, who needs copy constructors?
            Item detached = item.getClass().newInstance();
            Bundle copy = new Bundle();
            item.storeInBundle(copy);

            copy.put(QUICKSLOT, (String) null);

            detached.restoreFromBundle(copy);
            detached.quantity(quantity);
            detached.onDetach();

            item.quantity(item.quantity() - quantity);
            item.updateQuickslot();

            return detached;
        } catch (Exception e) {
            return null;
        }
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

	public final Item identify()
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
			if (parent().tryMergeExistingStack(this)) {
				updateQuickslot = true;
			}
		}

		if (updateQuickslot)
		{
			updateQuickslot();
		}

		return this;
	}

	public static void evoke(Char hero) {
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
			Item item = cl.newInstance();
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

        if (hasLevels) {
            level = randomEnchantmentMinimum;
            while (level < randomEnchantmentMaximum && Random.Float() < levelBonusChance) {
                level++;
            }

            if (Random.Float() <= 0.025f) {
                level = -level;
            }
        }
        else {
            level = 0;
        }

        if (hasBuc) {
            float cursed = 1.0f - (float) Math.sqrt(levelBonusChance);
            float uncursed = (1.0f - cursed) / (1.0f + levelBonusChance) + cursed;

            float bucDetermination = Random.Float();

            if (bucDetermination <= cursed) {
                bucStatus(BUCStatus.Cursed);
                if (hasLevels) {
                    level = -level;
                }
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
        if (parent_whut != null && parent_whut.owner() instanceof Hero) {
            QuickSlotButton.refresh();
        }

        if (parent_whut instanceof Heap && Dungeon.hero != null) {
            Heap heap = (Heap)parent_whut;
            heap.updateImage();

            if (heap.pos == Dungeon.hero.pos()) {
                GameScene.updateLootIndicator();
            }
        }
	}

	private static final String QUANTITY = "quantity";
	private static final String LEVEL = "level";
	private static final String LEVEL_KNOWN = "levelKnown";
	private static final String BUCSTATUS = "bucStatus";
	private static final String BUCSTATUS_KNOWN = "bucStatusKnown";
	private static final String QUICKSLOT = "quickslotpos";
	private static final String DEFAULTACTION = "defaultAction";
    private static final String WEIGHT = "weight";
    private static final String SHOPKEEPERREGISTRYINDEX = "shopkeeperRegistryIndex";
    private static final String SHOPKEEPERPRICEJACKED = "shopkeeperPriceJacked";

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

        bundle.put(SHOPKEEPERREGISTRYINDEX, shopkeeperRegistryIndex);
        bundle.put(SHOPKEEPERPRICEJACKED, shopkeeperPriceJacked);
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
			if (bundle.contains(QUICKSLOT)) {
				Dungeon.quickslot.setSlot(bundle.getInt(QUICKSLOT), this);
			}
		}
        weight = bundle.getInt(WEIGHT);

        shopkeeperRegistryIndex = bundle.getInt(SHOPKEEPERREGISTRYINDEX);
        shopkeeperPriceJacked = bundle.getBoolean(SHOPKEEPERPRICEJACKED);
	}

    public void cast(final Hero user, int dst) {
        cast(user, user.pos(), dst);
    }

	public void cast(final Char user, int pos, int dst) {
        //todo: make sure throwing item weights are right, etc.

        final int endPos = new Ballistica(pos, dst, Ballistica.PROJECTILE).collisionPos;

        user.sprite.zap(endPos);
        user.busy();

		Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);

		Char enemy = Actor.findChar(endPos);
		QuickSlotButton.target(enemy);

		// FIXME!!!
		final long delay = Constant.Time.ITEM_THROW;
        /*
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
		*/

        final Item item = this.parent().remove(Item.this, 1);

        if (enemy != null && Random.Int(2)==0) {
            enemy.belongings.collect(item);
        }

		((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
				reset(pos, endPos, this, new Callback() {
					@Override
					public void call() {
                        item.onThrow(user, endPos);
                        user.spend_new(delay, true);
					}
				});
	}

	protected static Hero curUser = null;
	protected static Item curItem = null;
	protected static CellSelector.Listener thrower = new CellSelector.Listener() {
		@Override
		public boolean onSelect(Integer target) {
			GLog.d("onselectcell target="+target);
			if (target != null) {
				curItem.cast(curUser, target);
			}
            return true;
		}

		@Override
		public String prompt() {
			return "Choose direction of throw";
		}
	};
}
