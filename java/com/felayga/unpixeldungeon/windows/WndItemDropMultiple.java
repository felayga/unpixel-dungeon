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
package com.felayga.unpixeldungeon.windows;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.ui.OptionSlider;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;

public class WndItemDropMultiple extends Window {
    private static final int WIDTH = 112;
    private static final int HEIGHT = 144;
    private static final int SLIDER_HEIGHT = 25;
    private static final int BTN_HEIGHT = 16;

    private static final int GAP = 2;

    OptionSlider slider;

    public WndItemDropMultiple(Item item) {
        super();

        resize(WIDTH, HEIGHT);

        BitmapTextMultiline txtTitle = PixelScene.createMultiline("Drop " + item.getDisplayName(), 9);
        txtTitle.maxWidth = WIDTH - GAP * 2;
        txtTitle.hardlight(TITLE_COLOR);
        txtTitle.measure();
        txtTitle.x = (int) (width - txtTitle.width()) / 2;
        txtTitle.y = 0;
        add(txtTitle);


        final RedButton dropButton = new RedButton("DROP " + item.quantity(), true) {
            @Override
            public void onClick() {
                doDrop(slider.getSelectedValue());
                hide();
            }
        };


        slider = new OptionSlider("How many?", "0", "" + item.quantity(), 0, item.quantity(), 5) {
            @Override
            protected void onChange() {
                dropButton.text("DROP " + getSelectedValue());
            }
        };
        slider.setSelectedValue(item.quantity());
        slider.setRect(GAP, txtTitle.y + txtTitle.height() + GAP, WIDTH - GAP * 2, SLIDER_HEIGHT);
        add(slider);


        dropButton.setRect(width / 2 + GAP / 2, slider.bottom() + GAP, width / 2 - GAP - GAP / 2, BTN_HEIGHT);
        add(dropButton);

        RedButton cancelButton = new RedButton(Constant.Action.CANCEL, true) {
            @Override
            public void onClick() {
                hide();
            }
        };
        cancelButton.setRect(GAP, slider.bottom() + GAP, width / 2 - GAP - GAP / 2, BTN_HEIGHT);
        add(cancelButton);

        resize(WIDTH, (int) cancelButton.bottom() + GAP);
    }

    public void doDrop(int quantity) {

    }

    @Override
    public void onMenuPressed() {
        hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
