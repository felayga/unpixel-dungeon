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

import com.watabou.input.Touchscreen;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;

public class LeftRightSelector extends RedButton {
    protected Image icon2;

    protected int selected;
    protected String[] options;
    protected Listener selectedListener;

    public LeftRightSelector( String[] options, int selected, Listener selectedListener ) {
        super( options[selected] );

        this.selected = selected;
        this.options = options;
        this.selectedListener = selectedListener;

        icon = Icons.get(Icons.BUTTON_RIGHT);
        add(icon);
        icon2 = Icons.get(Icons.BUTTON_LEFT);
        add(icon2);
        layout();
    }

    protected void updateSelected()
    {
        text.text(options[selected]);
    }

    @Override
    protected void createChildren()
    {
        super.createChildren();

        remove(hotArea);
        hotArea = new TouchArea( 0, 0, 0, 0 ) {
            @Override
            protected void onClickLeftSide( Touchscreen.Touch touch ) {
                if (!processed) {
                    LeftRightSelector.this.onClickLeftSide();
                }
            };
            @Override
            protected void onClickRightSide( Touchscreen.Touch touch ) {
                if (!processed) {
                    LeftRightSelector.this.onClickRightSide();
                }
            };
        };
        add(hotArea);
    }

    @Override
    protected void layout() {
        super.layout();

        float margin = (height - text.baseLine()) / 2;

        text.x = x + margin + icon2.width;
        text.y = y + margin;

        margin = (height - icon.height) / 2;

        icon.x = x + width - margin - icon.width;
        icon.y = y + margin;
        icon2.x = x + margin;
        icon2.y = y + margin;
    }

    public int selected() {
        return selected;
    }

    public void selected( int value ) {
        if (selected != value) {
            selected = value;
            updateSelected();
        }
    }

    protected void onClickLeftSide() {
        onSelected((selected + options.length - 1) % options.length);
    }

    protected void onClickRightSide(){
        onSelected((selected + 1) % options.length);
    }

    protected void onSelected(int selected) {
        if (this.selected != selected) {
            this.selected = selected;
            updateSelected();

            if (selectedListener != null) {
                selectedListener.onSelected(selected);
            }
        }
    }

    public interface Listener
    {
        void onSelected(int selected);
    }

}
