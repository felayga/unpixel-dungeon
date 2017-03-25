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

package com.felayga.unpixeldungeon.items.equippableitem.amulet;

import com.felayga.unpixeldungeon.sprites.ItemSprite;

/**
 * Created by HELLO on 3/2/2017.
 */
public class AmuletOfYendor extends AmuletOfYendorFake {
    public AmuletOfYendor() {
        super();

        name = "Amulet of Yendor";

        unique = true;

        price = 30000;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return MITHRIL;
    }

    @Override
    public String info() {
        return desc();
    }

    /*
    private static final String AC_END = "END THE GAME";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_END );
        return actions;
    }

    @Override
    public boolean execute(Hero hero, String action ) {
        if (action.equals(AC_END)) {
            showAmuletScene( false );
            return false;
        } else {
            return super.execute( hero, action );
        }
    }

    @Override
    public boolean doPickUp( Hero hero ) {
        if (super.doPickUp( hero )) {

            if (!Statistics.amuletObtained) {
                Statistics.amuletObtained = true;
                Badges.validateVictory();

                showAmuletScene( true );
            }

            return true;
        } else {
            return false;
        }
    }

    private void showAmuletScene( boolean showText ) {
        try {
            Dungeon.saveAll();
            AmuletScene.noText = !showText;
            Game.switchScene( AmuletScene.class );
        } catch (IOException e) {
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
    */

}

