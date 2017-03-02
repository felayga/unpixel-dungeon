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
package com.felayga.unpixeldungeon.ui;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.backpack.Belongings;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.weapon.ranged.RangedWeapon;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.unPixelDungeon;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.felayga.unpixeldungeon.windows.WndCatalogus;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.PointF;

import static com.felayga.unpixeldungeon.Dungeon.quickslot;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	private Tool btnInventory;
	private QuickslotTool[] btnQuick;
    private RangedTool btnRanged;
	
	private PickedUpItem pickedUp;
	
	private boolean lastEnabled = true;
	private boolean examining = false;

	private static Toolbar instance;

    public float actualTop() {
        return btnRanged.top();
    }

	public enum Mode {
		SPLIT,
		GROUP,
		CENTER
	}
	
	public Toolbar() {
		super();

		instance = this;

		height = btnInventory.height();
	}
	
	@Override
	protected void createChildren() {
		
		add(btnWait = new Tool(24, 0, 20, 26) {
			@Override
			protected void onClick() {
				examining = false;
				Dungeon.hero.rest(false);
			}

			protected boolean onLongClick() {
				examining = false;
				Dungeon.hero.rest(true);
				return true;
			}
		});
		
		add(btnSearch = new Tool(44, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (!examining) {
					GameScene.selectCell(informer);
					examining = true;
				} else {
					informer.onSelect(null);
					Dungeon.hero.search(true);
				}
			}

			@Override
			protected boolean onLongClick() {
				Dungeon.hero.search(true);
				return true;
			}
		});

		btnQuick = new QuickslotTool[4];

		add( btnQuick[3] = new QuickslotTool( 64, 0, 22, 24, 3) );

		add( btnQuick[2] = new QuickslotTool( 64, 0, 22, 24, 2) );

		add(btnQuick[1] = new QuickslotTool(64, 0, 22, 24, 1));

		add(btnQuick[0] = new QuickslotTool(64, 0, 22, 24, 0));

        add(btnRanged = new RangedTool(64, 0, 22, 24));
		
		add(btnInventory = new Tool(0, 0, 24, 26) {
			private GoldIndicator gold;

			@Override
			protected void onClick() {
				GameScene.show(new WndBackpack(Dungeon.hero.belongings.backpack1, null, WndBackpack.Mode.ALL_WITH_SPELL, null, null, false, null));
			}

			protected boolean onLongClick() {
				GameScene.show(new WndCatalogus());
				return true;
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				gold = new GoldIndicator();
				add(gold);
			}

			@Override
			protected void layout() {
				super.layout();
				gold.fill(this);
			}
		});

		add(pickedUp = new PickedUpItem());
	}
	
	@Override
	protected void layout() {

        int[] visible = new int[QuickSlotButton.MAXCOUNT];
        int slots = unPixelDungeon.quickSlots();

        for (int i = 0; i < QuickSlotButton.MAXCOUNT; i++)
            visible[i] = (int) ((slots > i) ? y + 2 : y + 25);

        btnRanged.visible = true;

        for (int i = 0; i < QuickSlotButton.MAXCOUNT; i++) {
            btnQuick[i].visible = btnQuick[i].active = slots > i;
            //decides on quickslot layout, depending on available screen size.
            if (slots == 4 && width < 150) {
                if (width < 139) {
                    if ((unPixelDungeon.flipToolbar() && i == 3) ||
                            (!unPixelDungeon.flipToolbar() && i == 0)) {
                        btnQuick[i].border(0, 0);
                        btnQuick[i].frame(88, 0, 17, 24);
                    } else {
                        btnQuick[i].border(0, 1);
                        btnQuick[i].frame(88, 0, 18, 24);
                    }
                } else {
                    if (i == 0 && !unPixelDungeon.flipToolbar() ||
                            i == 3 && unPixelDungeon.flipToolbar()) {
                        btnQuick[i].border(0, 2);
                        btnQuick[i].frame(106, 0, 19, 24);
                    } else if (i == 0 && unPixelDungeon.flipToolbar() ||
                            i == 3 && !unPixelDungeon.flipToolbar()) {
                        btnQuick[i].border(2, 1);
                        btnQuick[i].frame(86, 0, 20, 24);
                    } else {
                        btnQuick[i].border(0, 1);
                        btnQuick[i].frame(88, 0, 18, 24);
                    }
                }
            } else {
                btnQuick[i].border(2, 2);
                btnQuick[i].frame(64, 0, 22, 24);
            }

        }

        float pos;
        float right = width;
        switch (Mode.valueOf(unPixelDungeon.toolbarMode())) {
            case SPLIT:
                btnWait.setPos(x, y);
                btnSearch.setPos(btnWait.right(), y);

                btnInventory.setPos(right - btnInventory.width(), y);

                pos = btnInventory.left();
                for (int n = 0; n < QuickSlotButton.MAXCOUNT; n++) {
                    btnQuick[n].setPos(pos - btnQuick[n].width(), visible[n]);
                    pos = btnQuick[n].left();
                }

                btnRanged.setPos(btnInventory.right() - btnRanged.width(), btnInventory.top() - btnRanged.height());
                break;

            //center = group but.. well.. centered, so all we need to do is pre-emptively set the right side further in.
            case CENTER:
                float toolbarWidth = btnWait.width() + btnSearch.width() + btnInventory.width();
                for (Button slot : btnQuick) {
                    if (slot.visible) toolbarWidth += slot.width();
                }
                right = (width + toolbarWidth) / 2;

            case GROUP:
                btnWait.setPos(right - btnWait.width(), y);
                btnSearch.setPos(btnWait.left() - btnSearch.width(), y);
                btnInventory.setPos(btnSearch.left() - btnInventory.width(), y);

                pos = btnInventory.left();
                for (int n = 0; n < QuickSlotButton.MAXCOUNT; n++) {
                    btnQuick[n].setPos(pos - btnQuick[n].width(), visible[n]);
                    pos = btnQuick[n].left();
                }

                //todo: better positioning of ranged button in CENTER, GROUP layouts?
                btnRanged.setPos(btnInventory.right() - btnRanged.width(), btnInventory.top() - btnRanged.height());
                break;
        }
        right = width;

        if (unPixelDungeon.flipToolbar()) {

            btnWait.setPos((right - btnWait.right()), y);
            btnSearch.setPos((right - btnSearch.right()), y);
            btnInventory.setPos((right - btnInventory.right()), y);

            for (int i = 0; i < QuickSlotButton.MAXCOUNT; i++) {
                btnQuick[i].setPos(right - btnQuick[i].right(), visible[i]);
            }

        }

    }

	public static void updateLayout(){
		if (instance != null) instance.layout();
	}
	
	@Override
	public void update() {
		super.update();
		
		if (lastEnabled != Dungeon.hero.ready) {
			lastEnabled = Dungeon.hero.ready;
			
			for (Gizmo tool : members) {
				if (tool instanceof Tool) {
					((Tool)tool).enable( lastEnabled );
				}
			}
		}
		
		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable(true);
		}
	}
	
	public void pickup( Item item ) {
		pickedUp.reset( item,
			btnInventory.centerX(),
			btnInventory.centerY() );
	}

	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public boolean onSelect( Integer cell ) {
			instance.examining = false;
			GameScene.examineCell( cell );
            return true;
		}
		@Override
		public String prompt() {
			return "Press again to search\nPress a cell for info";
		}
	};

    public static void reset() {
        quickslot.reset();
        QuickSlotButton.reset();
        RangedTool.reset();
    }

    public static void refresh() {
        //GLog.d("Toolbar.refresh");
        QuickSlotButton.refresh();
        RangedTool.refresh();
    }


	
	private static class Tool extends Button {
		
		private static final int BGCOLOR = 0x7B8073;
		
		private Image base;
		
		public Tool( int x, int y, int width, int height ) {
			super();
			
			frame(x, y, width, height);
		}

		public void frame( int x, int y, int width, int height) {
			base.frame( x, y, width, height );

			this.width = width;
			this.height = height;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			base = new Image( Assets.TOOLBAR );
			add( base );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			base.x = x;
			base.y = y;
		}
		
		@Override
		protected void onTouchDown() {
			base.brightness( 1.4f );
		}
		
		@Override
		protected void onTouchUp() {
			if (active) {
				base.resetColor();
			} else {
				base.tint( BGCOLOR, 0.7f );
			}
		}
		
		public void enable( boolean value ) {
			if (value != active) {
				if (value) {
					base.resetColor();
				} else {
					base.tint( BGCOLOR, 0.7f );
				}
				active = value;
			}
		}
	}
	
	private static class QuickslotTool extends Tool {
		private QuickSlotButton slot;
		private int borderLeft = 2;
		private int borderRight = 2;
		
		public QuickslotTool( int x, int y, int width, int height, int slotNum ) {
			super( x, y, width, height );

			slot = new QuickSlotButton( slotNum );
			add( slot );
		}

		public void border( int left, int right ){
			borderLeft = left;
			borderRight = right;
			layout();
		}
		
		@Override
		protected void layout() {
			super.layout();
			slot.setRect( x + borderLeft, y + 2, width - borderLeft-borderRight, height - 4 );
		}
		
		@Override
		public void enable( boolean value ) {
			super.enable( value );
			slot.enable( value );
		}
    }

    private static class RangedTool extends Tool {
        private ItemButton slot;
        private int borderLeft = 2;
        private int borderRight = 2;

        private static RangedTool instance;

        public static void reset() {
            instance = null;
        }

        public static void refresh() {
            if (instance != null) {
                Belongings b = Dungeon.hero.belongings;

                if (b.ranged() != null) {
                    instance.slot.item(b.ranged());
                } else if (b.ammo() != null) {
                    instance.slot.item(b.ammo());
                } else if (b.ranOutOfAmmo() != null) {
                    instance.slot.item(b.ranOutOfAmmo());
                } else {
                    instance.slot.item(new WndBackpack.Placeholder(ItemSpriteSheet.PLACEHOLDER_RANGED));
                }
            }
        }

        public RangedTool( int x, int y, int width, int height ) {
            super( x, y, width, height );

            instance = this;

            slot = new ItemButton(null);
            //slot.topLeft.font(Font.)
            add(slot);
            refresh();
        }

        @Override
        protected void layout() {
            super.layout();
            slot.setRect( x + borderLeft, y + 2, width - borderLeft-borderRight, height - 4 );
        }

        @Override
        public void enable( boolean value ) {
            super.enable( value );
            slot.enable( value );
        }

        private class ItemButton extends ItemSlot {
            @Override
            protected PointF getFontScale() {
                return new PointF(1.0f, 1.0f);
            }

            @Override
            protected PointF getIconScale() {
                return new PointF(1.0f, 1.0f);
            }

            private static final int NORMAL = 0xFF4A4D44;
            private static final int EQUIPPED = 0xFF63665B;

            private Item item;
            private ColorBlock bg;
            private boolean validItem = false;

            public ItemButton(Item item) {
                super(item);

                this.item = item;
            }

            @Override
            protected void createChildren() {
                bg = new ColorBlock(width, height, NORMAL);
                add(bg);

                super.createChildren();
            }

            @Override
            public void enable(boolean state) {
                super.enable(state && validItem);
            }

            @Override
            protected void layout() {
                bg.x = x;
                bg.y = y;
                bg.width = width;
                bg.height = height;

                super.layout();
            }

            @Override
            public void item(Item item) {
                super.item(item);

                this.item = item;

                if (item != null) {
                    bg.texture(TextureCache.createSolid(item.isEquipped(Dungeon.hero) ? EQUIPPED : NORMAL));
                    BUCStatus.colorizeBackground(bg, item.visibleBucStatus());

                    if (item instanceof RangedWeapon) {
                        RangedWeapon weapon = (RangedWeapon)item;
                        validItem = weapon.ammoCount(Dungeon.hero) > 0;
                    } else if (item instanceof MissileWeapon) {
                        MissileWeapon ammo = (MissileWeapon)item;
                        validItem = !ammo.requiresLauncher && ammo.quantity() > 0;
                    } else {
                        validItem = false;
                    }

                    enable(true);
                } else {
                    bg.color(NORMAL);
                    validItem = false;

                    enable(false);
                }
            }

            @Override
            protected void onTouchDown() {
                //no brightness modifications
                //bg.brightness(1.5f);
            }

            protected void onTouchUp() {
                //bg.brightness(1.0f);
            }

            @Override
            protected void onClick() {
                if (item instanceof RangedWeapon) {
                    item.execute(Dungeon.hero, Constant.Action.SHOOT);
                } else {
                    item.execute(Dungeon.hero, Constant.Action.THROW);
                }
            }
        }
    }
	
	private static class PickedUpItem extends ItemSprite {
		
		private static final float DISTANCE = DungeonTilemap.SIZE;
		private static final float DURATION = 0.2f;
		
		private float dstX;
		private float dstY;
		private float left;
		
		public PickedUpItem() {
			super();
			
			originToCenter();
			
			active =
			visible =
				false;
		}
		
		public void reset( Item item, float dstX, float dstY ) {
			view( item );
			
			active =
			visible =
				true;
			
			this.dstX = dstX - ItemSprite.SIZE / 2;
			this.dstY = dstY - ItemSprite.SIZE / 2;
			left = DURATION;
			
			x = this.dstX - DISTANCE;
			y = this.dstY - DISTANCE;
			alpha( 1 );
		}
		
		@Override
		public void update() {
			super.update();
			
			if ((left -= Game.elapsed) <= 0) {
				
				visible =
				active =
					false;
				if (emitter != null) emitter.on = false;
				
			} else {
				float p = left / DURATION;
				scale( (float)Math.sqrt( p ) );
				float offset = DISTANCE * p;
				x = dstX - offset;
				y = dstY - offset;
			}
		}
	}
}
