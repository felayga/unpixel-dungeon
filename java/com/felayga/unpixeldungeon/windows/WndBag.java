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

import android.graphics.RectF;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Belongings;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.bags.LargeChest;
import com.felayga.unpixeldungeon.items.bags.PotionBandolier;
import com.felayga.unpixeldungeon.items.bags.ScrollHolder;
import com.felayga.unpixeldungeon.items.bags.SeedPouch;
import com.felayga.unpixeldungeon.items.bags.WandHolster;
import com.felayga.unpixeldungeon.items.bags.backpack.Backpack;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.ui.Icons;
import com.felayga.unpixeldungeon.ui.ItemSlot;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.unPixelDungeon;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WndBag extends WndTabbed {
    private static final int SLOTS_DISPLAY_MAX = 36;

    private static final String BTN_DEPOSIT = "DEPOSIT";
    private static final String BTN_TAKEALL = "TAKE ALL";
    private static final String BTN_PICKUP = "PICK UP";
    private static final String BTN_NOACTION = "NO ACTION";
    private static final String BTN_CLOSE = "CLOSE";

    protected static final int COLS_P = 6; //width original=4
    protected static final int COLS_L = 12; //height original=6

    protected static final int SLOT_SIZE = 18; //original=28
    protected static final int SLOT_MARGIN = 1;

    protected static final int TITLE_HEIGHT = 12; //original=12

    private Listener listener;
    private WndBackpack.Mode mode;
    private String title;
    private Class<? extends Item> classMatch;
    private Class<? extends Item> classUnmatch;

    private int nCols;
    private int nRows;

    protected int count;
    protected int col;
    protected int row;

    private static WndBackpack.Mode lastMode;
    private static IBag lastBag;
    private static int lastPos;
    private static Class<? extends Item> lastClassMatch;
    private static Class<? extends Item> lastClassUnmatch;

    private static IBag nestBag;

    private int pos;

    public WndBag(IBag bag, Listener listener, WndBackpack.Mode mode, Class<? extends Item> classMatch, String title, int pos) {
        this(bag, listener, mode, classMatch, title, pos, null, null);
    }

    public WndBag(IBag bag, Listener listener, WndBackpack.Mode mode, Class<? extends Item> classMatch, String title, int pos, IBag nestBag, Class<? extends Item> classUnmatch) {
        super();

        this.listener = listener;
        this.mode = mode;
        this.title = title;
        this.pos = pos;
        this.classMatch = classMatch;
        this.classUnmatch = classUnmatch;

        lastMode = mode;
        lastBag = bag;
        lastPos = pos;
        lastClassMatch = classMatch;
        lastClassUnmatch = classUnmatch;

        nCols = unPixelDungeon.landscape() ? COLS_L : COLS_P;
        nRows = (Belongings.BACKPACK_SIZE + 12) / nCols + ((Belongings.BACKPACK_SIZE + 12) % nCols > 0 ? 1 : 0);

        int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
        int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1);

        BitmapText txtTitle = PixelScene.createText(title != null ? title : Utils.capitalize(bag.getDisplayName()), 9);
        txtTitle.hardlight(TITLE_COLOR);
        txtTitle.measure();
        txtTitle.x = (int) (slotsWidth - txtTitle.width()) / 2;
        txtTitle.y = (int) (TITLE_HEIGHT - txtTitle.height()) / 2;
        add(txtTitle);

        placeItems(bag);

        resize(slotsWidth, slotsHeight + TITLE_HEIGHT);

        List<IBag> bags = new ArrayList<>();

        IBag cur = nestBag;

        if (cur == null) {
            cur = bag;
        }

        this.nestBag = cur;

        while (cur != null && !(cur instanceof Backpack)) {
            bags.add(0, cur);

            cur = cur.parent();
        }

        for (IBag b : bags) {
            if (b != null) {
                BagTab tab = new BagTab(b);
                add(tab);
                if (b == bag) {
                    tab.select(true);
                }
            }
        }

        layoutTabs();
    }

    protected void placeItems(IBag container) {
        count = 0;

        // Items in the bag
        Iterator<Item> iterator;
        int itemCount = 0;

        GLog.d("container size=" + container.size());

        if (container.size() > 0) {
            iterator = container.iterator(false);
            while (iterator.hasNext() && count < SLOTS_DISPLAY_MAX) {
                Item item = iterator.next();
                placeItem(item);
                itemCount++;
            }
        }

        // Free Space
        while (count < SLOTS_DISPLAY_MAX) {
            placeItem(null);
        }

        if (col != 0) {
            col = 0;
            row++;
        }

        IBag parent = lastBag.parent();

        placeButton(BTN_DEPOSIT, itemCount < lastBag.size(), false);
        placeButton(BTN_TAKEALL, itemCount > 0, false);
        placeButton(BTN_PICKUP, pos >= 0 && lastBag.self() != null, false);
        if (parent != null) {
            placeButton(BTN_CLOSE, true, true);
        } else {
            placeButton(BTN_NOACTION, false, false);
        }
    }

    protected void placeItem(final Item item) {
        int x = col * (SLOT_SIZE + SLOT_MARGIN);
        int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN);

        add(new ItemButton(item).setPos(x, y));

        if (++col >= nCols) {
            col = 0;
            row++;
        }

        count++;
    }

    private static WndBackpack.Listener withdrawItemSelector = new WndBackpack.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null) {
                int pos = lastBag.pos();
                if (pos == Constant.Position.NONE) {
                    pos = Dungeon.hero.pos();
                }

                Item test = lastBag.remove(item);
                if (test == item) {
                    if (Dungeon.hero.belongings.collect(item)) {
                        Dungeon.hero.spend_new(Constant.Time.ITEM_PICKUP, true);
                    } else {
                        Dungeon.level.drop(item, pos);
                    }
                } else {
                    GLog.d("failure withdrawing item from bag");
                    GLog.d("" + 1 / 0);
                }
            }

            if (lastBag instanceof Heap && lastBag.size() < 1) {
            } else {
                GameScene.show(new WndBag(lastBag, null, lastMode, lastClassMatch, null, lastPos, nestBag, null));
            }
        }
    };

    private static WndBackpack.Listener depositItemSelector = new WndBackpack.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null) {
                item = item.parent().remove(item);
                lastBag.collect(item);
            }
            GameScene.show(new WndBag(lastBag, null, lastMode, lastClassMatch, null, lastPos, nestBag, LargeChest.class));
        }
    };

    protected void placeButton(final String buttonText, boolean enabled, boolean isDefault) {
        int x = col * (SLOT_SIZE + SLOT_MARGIN);
        int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN);

        final Hero hero = Dungeon.hero;
        final int position = this.pos;
        final IBag self = lastBag;

        final RedButton button = new RedButton(buttonText, true) {
            @Override
            protected void onClick() {
                switch (buttonText) {
                    case BTN_DEPOSIT:
                        hide();
                        GameScene.selectItem(depositItemSelector, WndBackpack.Mode.ALL, "Deposit an item", LargeChest.class, self.self());
                        break;
                    case BTN_PICKUP:
                        hide();
                        hero.curAction = new HeroAction.HandleHeap.PickUp(position, self.self());
                        hero.motivate(true);
                        break;
                    case BTN_TAKEALL:
                        hide();
                        Iterator<Item> iterator = lastBag.iterator(false);
                        while (iterator.hasNext()) {
                            Item item = iterator.next();
                            if (hero.belongings.collect(item)) {
                                iterator.remove();
                            }
                        }
                        break;
                    case BTN_CLOSE:
                        hide();
                        IBag parent = lastBag.parent();

                        if (parent instanceof Backpack) {
                            GameScene.show(new WndBackpack(parent, null, WndBackpack.Mode.ALL_WITH_SPELL, null, null, false, null));
                        } else {
                            GameScene.show(new WndBag(parent, null, WndBackpack.Mode.ALL, null, null, Constant.Position.NONE));
                        }
                        break;
                }
            }
        };
        button.setRect(x, y, SLOT_SIZE * 3 + SLOT_MARGIN * 2, SLOT_SIZE);
        button.enable(enabled);

        if (isDefault) {
            button.textColor(Window.TITLE_COLOR);
        }


        add(button);

        col += 3;
        if (col >= nCols) {
            col -= nCols;
            row++;
        }
    }

    @Override
    public void onMenuPressed() {
        if (listener == null) {
            hide();
        }
    }

    @Override
    public void onBackPressed() {
        if (listener != null) {
            listener.onSelect(null);
        }
        super.onBackPressed();
    }

    @Override
    protected void onClick(Tab tab) {
        hide();
        GameScene.show(new WndBag(((BagTab) tab).bag, listener, mode, classMatch, title, pos, nestBag, classUnmatch));
    }

    @Override
    protected int tabHeight() {
        return 20;
    }

    private class BagTab extends Tab {

        private Image icon;

        private IBag bag;

        public BagTab(IBag bag) {
            super();

            this.bag = bag;

            icon = icon();
            add(icon);
        }

        @Override
        protected void select(boolean value) {
            super.select(value);
            icon.am = selected ? 1.0f : 0.6f;
        }

        @Override
        protected void layout() {
            super.layout();

            icon.copy(icon());
            icon.x = x + (width - icon.width) / 2;
            icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
            if (!selected && icon.y < y + CUT) {
                RectF frame = icon.frame();
                frame.top += (y + CUT - icon.y) / icon.texture.height;
                icon.frame(frame);
                icon.y = y + CUT;
            }
        }

        private Image icon() {
            if (bag.tabIcon() != null) {
                return Icons.get(bag.tabIcon());
            } else if (bag instanceof SeedPouch) {
                return Icons.get(Icons.SEED_POUCH);
            } else if (bag instanceof ScrollHolder) {
                return Icons.get(Icons.SCROLL_HOLDER);
            } else if (bag instanceof WandHolster) {
                return Icons.get(Icons.WAND_HOLSTER);
            } else if (bag instanceof PotionBandolier) {
                return Icons.get(Icons.POTION_BANDOLIER);
            } else {
                return Icons.get(Icons.BACKPACK);
            }
        }
    }

    private class ItemButton extends ItemSlot {
        @Override
        protected PointF getFontScale() {
            return new PointF(0.75f, 0.75f);
        }

        @Override
        protected PointF getIconScale() {
            return new PointF(0.75f, 0.75f);
        }

        private static final int NORMAL = 0xFF4A4D44;
        private static final int EQUIPPED = 0xFF63665B;

        private Item item;
        private ColorBlock bg;

        public ItemButton(Item item) {
            super(item);

            this.item = item;

            width = height = SLOT_SIZE;
        }

        @Override
        protected void createChildren() {
            bg = new ColorBlock(SLOT_SIZE, SLOT_SIZE, NORMAL);
            add(bg);

            super.createChildren();
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;

            super.layout();
        }

        @Override
        public void item(Item item) {

            super.item(item);
            if (item != null) {

                bg.texture(TextureCache.createSolid(item.isEquipped(Dungeon.hero) ? EQUIPPED : NORMAL));
                BUCStatus.colorizeBackground(bg, item.visibleBucStatus());

                if (item.getDisplayName() == null) {
                    enable(false);
                } else {
                    enable(WndBackpack.Mode.Qualify(mode, item, classMatch, classUnmatch));
                }
            } else {
                bg.color(NORMAL);
            }
        }

        @Override
        protected void onTouchDown() {
            bg.brightness(1.5f);
            Sample.INSTANCE.play(Assets.SND_CLICK, 0.7f, 0.7f, 1.2f);
        }

        ;

        protected void onTouchUp() {
            bg.brightness(1.0f);
        }

        ;

        @Override
        protected void onClick() {
            if (listener != null) {
                hide();
                listener.onSelect(item);
            } else {
                WndBag.this.add(new WndItem(WndBag.this, item, withdrawItemSelector));
            }
        }

        @Override
        protected boolean onLongClick() {
            return false;
        }
    }

    public interface Listener {
        void onSelect(Item item);
    }
}
