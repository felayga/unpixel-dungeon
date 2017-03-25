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
package com.felayga.unpixeldungeon.windows;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.ui.ItemSlot;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.utils.Random;

public class WndTradeItem extends Window {
	
	private static final float GAP		= 2;
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 16;
	
	private static final String TXT_SALE		= "FOR SALE: %s - %dg";
	private static final String TXT_BUY			= "Buy for %dg";
	private static final String TXT_STEAL		= "Steal with %d%% chance";
	private static final String TXT_SELL		= "Sell for %dg";
	private static final String TXT_SELL_1		= "Sell 1 for %dg";
	private static final String TXT_SELL_ALL	= "Sell all for %dg";
	private static final String TXT_CANCEL		= "Never mind";
	
	private static final String TXT_SOLD	= "You've sold your %s for %dg";
	private static final String TXT_BOUGHT	= "You've bought %s for %dg";
	private static final String TXT_STOLE	= "You've stolen the %s";
	
	private WndBackpack owner;
	
	public WndTradeItem( final Item item, WndBackpack owner ) {
		super();
		
		this.owner = owner;
		
		float pos = createDescription( item, false );
		
		if (item.quantity() == 1) {
			RedButton btnSell = new RedButton( Utils.format( TXT_SELL, item.price() ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSell.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			add( btnSell );
			
			pos = btnSell.bottom();
			
		} else {
            int price = item.price();

			RedButton btnSell1 = new RedButton( Utils.format( TXT_SELL_1, price ) ) {
				@Override
				protected void onClick() {
					sellOne( item );
					hide();
				}
			};
			btnSell1.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			add( btnSell1 );
			RedButton btnSellAll = new RedButton( Utils.format( TXT_SELL_ALL, price * item.quantity() ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSellAll.setRect( 0, btnSell1.bottom() + GAP, WIDTH, BTN_HEIGHT );
			add( btnSellAll );
			
			pos = btnSellAll.bottom();
			
		}
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
	
	public WndTradeItem( final Heap heap, boolean canBuy ) {
		super();
		
		Item item = heap.peek();
		
		float pos = createDescription( item, true );
		
		final int price = price( item );
		
		if (canBuy) {
			
			RedButton btnBuy = new RedButton( Utils.format( TXT_BUY, price ) ) {
				@Override
				protected void onClick() {
					hide();
					buy( heap );
				}
			};
			btnBuy.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			btnBuy.enable( price <= Dungeon.hero.belongings.goldQuantity() );
			add( btnBuy );

			RedButton btnCancel = new RedButton( TXT_CANCEL ) {
				@Override
				protected void onClick() {
					hide();
				}
			};

            int effectiveDEXCHA = Dungeon.hero.DEXCHA();
            int armorBonusMaximum = Dungeon.hero.belongings.getArmorBonusMaximum() * 2 + 5;

            if (effectiveDEXCHA > armorBonusMaximum) {
                effectiveDEXCHA = armorBonusMaximum;
            }

            effectiveDEXCHA = (int)Math.round(Math.pow(effectiveDEXCHA, 1.75));

            int shopkeeperIndex = item.shopkeeperRegistryIndex();

            final Shopkeeper shopkeeper = Shopkeeper.Registry.get(shopkeeperIndex);

            int shopkeepLevel = shopkeeper.level();
            shopkeepLevel *= shopkeepLevel;

            final int chance = effectiveDEXCHA * 3 - shopkeepLevel - item.weight() / Encumbrance.UNIT;


			//final MasterThievesArmband.Thievery thievery = Dungeon.hero.buff(MasterThievesArmband.Thievery.class);
			if (chance > 0) {
				final float fchance = (float)chance / 256.0f;
				RedButton btnSteal = new RedButton(Utils.format(TXT_STEAL, Math.min(100, (int)(fchance*100.0f)))) {
					@Override
					protected void onClick() {
                        boolean steal;
                        boolean notCaught = false;
                        if (chance < 1) {
                            steal = false;
                        } else if (chance > 255) {
                            steal = true;
                        } else {
                            int test = Random.Int(256);

                            if (test < chance) {
                                steal = true;
                            } else {
                                steal = false;
                                if (test < chance - 48) {
                                    notCaught = true;
                                }
                            }
                        }

                        if(steal) {
                            Hero hero = Dungeon.hero;
                            Item item = heap.pickUp();
                            GLog.i(TXT_STOLE, item.getDisplayName());
                            hide();

                            if (!item.doPickUp(hero)) {
                                Dungeon.level.drop(item, heap.pos()).sprite.drop();
                            }
                        } else if (notCaught) {
                            GLog.w("Couldn't steal it, but you didn't get caught trying.  Wew.");
						} else {
                            shopkeeper.yell(Shopkeeper.TXT_THIEF);
                            shopkeeper.flee();
							hide();
						}
					}
				};
				btnSteal.setRect(0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT);
				add(btnSteal);

				btnCancel.setRect(0, btnSteal.bottom() + GAP, WIDTH, BTN_HEIGHT);
			} else
				btnCancel.setRect( 0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT );

			add( btnCancel );
			
			resize(WIDTH, (int) btnCancel.bottom());
			
		} else {
			
			resize( WIDTH, (int)pos );
			
		}
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		if (owner != null) {
			owner.hide();
			Shopkeeper.sell(Shopkeeper.currentShopkeeper);
		}
	}
	
	private float createDescription( Item item, boolean forSale ) {
		// Title
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item ) );
		titlebar.label( forSale ?
			Utils.format( TXT_SALE, item.getDisplayName(), price( item ) ) :
			Utils.capitalize( item.getDisplayName() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		// Upgraded / degraded
		if (item.levelKnown() && item.level() > 0) {
			titlebar.color( ItemSlot.UPGRADED );
		} else if (item.levelKnown() && item.level() < 0) {
			titlebar.color( ItemSlot.DEGRADED );
		}
		
		// Description
		BitmapTextMultiline info = PixelScene.createMultiline( item.info(), 6 );
		info.maxWidth = WIDTH;
		info.measure();
		info.x = titlebar.left();
		info.y = titlebar.bottom() + GAP;
		add( info );
		
		return info.y + info.height();
	}
	
	private void sell( Item item ) {
		Hero hero = Dungeon.hero;
		
		if (item.isEquipped( hero ) && !hero.belongings.unequip((EquippableItem) item, false)) {
			return;
		}
		item = hero.belongings.remove(item);
        Shopkeeper.currentShopkeeper.belongings.collect(item);

		int price = item.price() * item.quantity();

        hero.belongings.collect(new Gold(price));
		GLog.i( TXT_SOLD, item.getDisplayName(), price );
	}
	
	private void sellOne( Item item ) {
		if (item.quantity() <= 1) {
			sell( item );
		} else {
			
			Hero hero = Dungeon.hero;
			
			item = hero.belongings.remove(item, 1);
            Shopkeeper.currentShopkeeper.belongings.collect(item);

			int price = item.price() * item.quantity();

            hero.belongings.collect(new Gold(price));
			GLog.i( TXT_SOLD, item.getDisplayName(), price );
		}
	}
	
	private int price( Item item ) {
		int price = item.price() * item.quantity() * 3;
		return price;
	}
	
	private void buy( Heap heap ) {
		
		Hero hero = Dungeon.hero;
		Item item = heap.pickUp();
		
		int price = price( item );
		Dungeon.hero.belongings.removeGold(price);

		GLog.i( TXT_BOUGHT, item.getDisplayName(), price );
		
		if (!hero.belongings.collect(item)) {
			Dungeon.level.drop( item, heap.pos() ).sprite.drop();
		}
	}
}
