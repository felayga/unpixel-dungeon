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

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;

public class QuickSlotButton extends Button implements WndBackpack.Listener {
    public static final int MAXCOUNT = 4;

    private static final String TXT_SELECT_ITEM = "Select an item to quickslot";

    private static QuickSlotButton[] instance = new QuickSlotButton[MAXCOUNT];
    private int slotNum;

    private ItemSlot slot;

    private static Image crossB;
    private static Image crossM;

    private static boolean targeting = false;
    public static Char lastTarget = null;

    public QuickSlotButton(int slotNum) {
        super();
        this.slotNum = slotNum;
        item(select(slotNum));

        instance[slotNum] = this;
    }

    @Override
    public void destroy() {
        super.destroy();

        reset();
    }

    public static void reset() {
        instance = new QuickSlotButton[MAXCOUNT];

        lastTarget = null;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        slot = new ItemSlot() {
            @Override
            protected void onClick() {
                if (targeting) {
                    int cell = autoAim(lastTarget);

                    if (cell != -1) {
                        GameScene.handleCell(cell);
                    } else {
                        //couldn't auto-aim, just target the position and hope for the best.
                        GameScene.handleCell(lastTarget.pos());
                    }
                } else {
                    Item item = select(slotNum);
                    if (item.usesTargeting) {
                        useTargeting();
                    }
                    item.execute(Dungeon.hero);
                }
            }

            @Override
            protected boolean onLongClick() {
                return QuickSlotButton.this.onLongClick();
            }

            @Override
            protected void onTouchDown() {
                icon.lightness(0.7f);
            }

            @Override
            protected void onTouchUp() {
                icon.resetColor();
            }
        };
        slot.showParams(true, false, true);
        add(slot);

        crossB = Icons.TARGET.get();
        crossB.visible = false;
        add(crossB);

        crossM = new Image();
        crossM.copy(crossB);
    }

    @Override
    protected void layout() {
        super.layout();

        slot.fill(this);

        crossB.x = x + (width - crossB.width) / 2;
        crossB.y = y + (height - crossB.height) / 2;
    }

    @Override
    protected void onClick() {
        GameScene.selectItem(this, WndBackpack.Mode.QUICKSLOT, TXT_SELECT_ITEM, null);
    }

    @Override
    protected boolean onLongClick() {
        GameScene.selectItem(this, WndBackpack.Mode.QUICKSLOT, TXT_SELECT_ITEM, null);
        return true;
    }

    private static Item select(int slotNum) {
        return Dungeon.quickslot.getItem(slotNum);
    }

    @Override
    public void onSelect(Item item) {
        if (item != null) {
            Dungeon.quickslot.setSlot(slotNum, item);
            refresh();
        }
    }

    public void item(Item item) {
        /*
        String itemname;
        int itemquantity;
        if (item != null) {
            GLog.d("QuickSlotButton.item("+item.getDisplayName()+")");
            itemname = item.getDisplayName();
            itemquantity = item.quantity();
        } else {
            GLog.d("QuickSlotButton.item(<null>)");
            itemname = "<null>";
            itemquantity = -1;
        }
        GLog.d("QuickSlotButton item("+itemname+") quantity="+itemquantity);
        */
        slot.item(item);
        enableSlot();
    }

    public void enable(boolean value) {
        active = value;
        if (value) {
            enableSlot();
        } else {
            slot.enable(false);
        }
    }

    private void enableSlot() {
        slot.enable(Dungeon.quickslot.isNonePlaceholder(slotNum));
    }

    private void useTargeting() {
        if (lastTarget != null &&
                //Actor.chars().contains( lastTarget ) &&
                lastTarget.isAlive() &&
                Dungeon.visible[lastTarget.pos()]) {

            targeting = true;
            lastTarget.sprite.parent.add(crossM);
            crossM.point(DungeonTilemap.tileToWorld(lastTarget.pos()));
            crossB.x = x + (width - crossB.width) / 2;
            crossB.y = y + (height - crossB.height) / 2;
            crossB.visible = true;

        } else {

            lastTarget = null;
            targeting = false;

        }

    }

    public static int autoAim(Char target) {
        //first try to directly target
        if (new Ballistica(Dungeon.hero.pos(), target.pos(), Ballistica.Mode.Projectile.value).collisionPos == target.pos()) {
            return target.pos();
        }

        //Otherwise pick nearby tiles to try and 'angle' the shot, auto-aim basically.
        for (int i : Level.NEIGHBOURS9DIST2) {
            if (new Ballistica(Dungeon.hero.pos(), target.pos() + i, Ballistica.Mode.Projectile.value).collisionPos == target.pos()) {
                return target.pos() + i;
            }
        }

        //couldn't find a cell, give up.
        return -1;
    }

    public static void refresh() {
        for (int i = 0; i < instance.length; i++) {
            //GLog.d("QuickSlotButton.refresh("+i+")");
            if (instance[i] != null) {
                instance[i].item(select(i));
            }
        }
    }

    public static void target(Char target) {
        if (target != Dungeon.hero) {
            lastTarget = target;

            HealthIndicator.instance.target(target);
        }
    }

    public static void cancel() {
        if (targeting) {
            crossB.visible = false;
            crossM.remove();
            targeting = false;
        }
    }
}
