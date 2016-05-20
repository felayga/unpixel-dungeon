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
package com.felayga.unpixeldungeon.windows.hero;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.ui.LeftRightSelector;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.List;

public class WndInitHero extends Window {
    private static final int TITLE_HEIGHT	= 12;

    private static final int WIDTH		    = 112;
    private static final int HEIGHT         = 144 + TITLE_HEIGHT;
    private static final int SLIDER_HEIGHT	= 25;
    private static final int BTN_HEIGHT	    = 16;

    private static final int GAP            = 2;

    public static int savedGameIndex;

    public static int heroClassSelected;
    public static int genderSelected;
    public static int hairSelected;
    public static int hairFaceSelected;
    public static int hairColorSelected;

    public static void setDefault()
    {
        switch(savedGameIndex) {
            default:
                heroClassSelected = 0;
                genderSelected = 0;
                hairSelected = 1;
                hairFaceSelected = 1;
                hairColorSelected = 0;
                break;
            case 1:
                heroClassSelected = 1;
                genderSelected = 0;
                hairSelected = 2;
                hairFaceSelected = 0;
                hairColorSelected = 2;
                break;
            case 2:
                heroClassSelected = 2;
                genderSelected = 0;
                hairSelected = 3;
                hairFaceSelected = 2;
                hairColorSelected = 4;
                break;
            case 3:
                heroClassSelected = 3;
                genderSelected = 1;
                hairSelected = 4;
                hairFaceSelected = 0;
                hairColorSelected = 1;
        }
    }

    HeroSprite sprite;

    public WndInitHero(final Listener listener) {
        super();

        resize(WIDTH, HEIGHT);

        BitmapText txtTitle = PixelScene.createText(Utils.capitalize("So You Want To Be A Hero"), 9);
        txtTitle.hardlight(TITLE_COLOR);
        txtTitle.measure();
        txtTitle.x = (int) (width - txtTitle.width()) / 2;
        txtTitle.y = (int) (TITLE_HEIGHT - txtTitle.height()) / 2;
        add(txtTitle);


        layoutY = txtTitle.y + txtTitle.height() + GAP;


        List<String> heroes = new ArrayList<>();

        int index = 0;
        HeroClass heroClass = HeroClass.toHeroClass(index);
        while (heroClass != HeroClass.NONE) {
            heroes.add(heroClass.toString());
            index++;
            heroClass = HeroClass.toHeroClass(index);
        }

        addLeftRightSelector("Class", heroes, heroClassSelected, new LeftRightSelector.Listener() {
            @Override
            public void onSelected(int selected) {
                heroClassSelected = selected;
            }
        });


        List<String> genders = new ArrayList<>();

        genders.add("MALE");
        genders.add("FEMALE");

        addLeftRightSelector("Gender", genders, genderSelected, new LeftRightSelector.Listener() {
            @Override
            public void onSelected(int selected) {
                genderSelected = selected;
                sprite.setGender(genderSelected);
                sprite.setArmor(genderSelected);
                sprite.idle();
            }
        });


        List<String> hairs = new ArrayList<>();

        hairs.add("BALD");
        hairs.add("WILD");
        hairs.add("CREWCUT");
        hairs.add("FLIP");
        hairs.add("PONYTAIL");

        addLeftRightSelector("Hair", hairs, hairSelected, new LeftRightSelector.Listener() {
            @Override
            public void onSelected(int selected) {
                hairSelected = selected;
                sprite.setHair(hairSelected);
            }
        });


        List<String> hairfaces = new ArrayList<>();

        hairfaces.add("NONE");
        hairfaces.add("BEARD");
        hairfaces.add("GOATEE");

        addLeftRightSelector("Face", hairfaces, hairFaceSelected, new LeftRightSelector.Listener() {
            @Override
            public void onSelected(int selected) {
                hairFaceSelected = selected;
                sprite.setHairFace(hairFaceSelected);
            }
        });


        List<String> haircolors = new ArrayList<>();

        haircolors.add("RED");
        haircolors.add("BLONDE");
        haircolors.add("BROWN");
        haircolors.add("BLACK");
        haircolors.add("WHITE");

        addLeftRightSelector("Color", haircolors, hairColorSelected, new LeftRightSelector.Listener() {
            @Override
            public void onSelected(int selected) {
                hairColorSelected = selected;
                sprite.setHairColor(hairColorSelected);
            }
        });


        Image introPlot = new Image( Assets.HERO_INTROPLOT );
        introPlot.point(new PointF(GAP, GAP + layoutY));
        add(introPlot);

        sprite = new HeroSprite(null, false);
        sprite.point(new PointF(GAP + 16.0f, GAP + layoutY + 16.0f));
        sprite.setAppearance(genderSelected, genderSelected, hairSelected, hairFaceSelected, hairColorSelected);
        sprite.idle();
        add(sprite);


        RedButton button = new RedButton("READY!", true) {
            @Override
            public void onClick() {
                WndInitHero.this.hide();
                listener.onReady();
            }
        };
        button.setRect(width/2, layoutY + GAP + introPlot.height() - BTN_HEIGHT - 16, width/2-GAP, BTN_HEIGHT);
        add(button);
    }

    public interface Listener {
        public void onReady();
    }

    private float layoutY;

    private void addLeftRightSelector(String title, List<String> options, int selectedOption, LeftRightSelector.Listener listener)
    {
        BitmapText heroClassTitle = PixelScene.createText(Utils.capitalize(title), 9);
        heroClassTitle.hardlight(TITLE_COLOR);
        heroClassTitle.measure();
        heroClassTitle.x = (int)GAP;
        heroClassTitle.y = (int) (layoutY + (BTN_HEIGHT - heroClassTitle.height()) / 2);
        add(heroClassTitle);

        LeftRightSelector heroClassSelector = new LeftRightSelector(options.toArray(new String[options.size()]), selectedOption, listener);
        heroClassSelector.setRect(WIDTH/3, layoutY, WIDTH * 2 / 3 - GAP, BTN_HEIGHT);
        add(heroClassSelector);

        layoutY += BTN_HEIGHT + GAP;
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
