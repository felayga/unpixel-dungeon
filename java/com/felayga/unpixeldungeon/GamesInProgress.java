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
package com.felayga.unpixeldungeon;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.scenes.AboutScene;
import com.felayga.unpixeldungeon.windows.hero.WndInitHero;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

public class GamesInProgress {
    public static final int MAXIMUM = 4;

    private static SparseArray<Info> state = new SparseArray<>();

    public static Info check(int index) {
        Info retval;
        if ((retval = state.get(index)) != null) {
            return retval;
        } else {
            try {
                Bundle bundle = Dungeon.gameBundle(Dungeon.gameFile(index));
                retval = new Info();
                retval.restoreFromBundle(bundle);
            } catch (Exception e) {
                retval = null;
            }

            state.put(index, retval);
            return retval;
        }
    }

    public static void set(int index, HeroClass heroClass, int level, int depth) {
        Info info = new Info();
        info.heroClass = heroClass.value;
        info.gender = WndInitHero.genderSelected;
        info.hair = WndInitHero.hairSelected;
        info.hairFace = WndInitHero.hairFaceSelected;
        info.hairColor = WndInitHero.hairColorSelected;
        info.level = level;
        info.depth = depth;
        info.dead = false;
        state.put(index, info);
    }

    public static void setUnknown(int index) {
        Info test = state.get(index);
        if (test != null) {
            test.dead = true;
        }
    }

    public static void delete(int index, boolean dead) {
        Info test = state.get(index);
        if (test != null) {
            test.dead = true;
            test.ascended = !dead;
        }
    }

    public static class Info implements Bundlable {
        public int heroClass;
        public int gender;
        public int hair;
        public int hairFace;
        public int hairColor;

        public int level;
        public int depth;
        public boolean dead;
        public boolean ascended;

        public void toWndHeroInit() {
            WndInitHero.heroClassSelected = heroClass;
            WndInitHero.genderSelected = gender;
            WndInitHero.hairSelected = hair;
            WndInitHero.hairFaceSelected = hairFace;
            WndInitHero.hairColorSelected = hairColor;
        }

        public void fromWndHeroInit() {
            heroClass = WndInitHero.heroClassSelected;
            gender = WndInitHero.genderSelected;
            hair = WndInitHero.hairSelected;
            hairFace = WndInitHero.hairFaceSelected;
            hairColor = WndInitHero.hairColorSelected;
        }

        private static final String CLASS = "hero_class";
        private static final String GENDER = "hero_gender";
        private static final String HAIR = "hero_hair";
        private static final String HAIRFACE = "hero_hairFace";
        private static final String HAIRCOLOR = "hero_hairColor";
        private static final String DEAD = "hero_dead";
        private static final String ASCENDED = "hero_ascended";

        public void storeInBundle(Bundle bundle) {
            bundle.put(CLASS, heroClass);
            bundle.put(GENDER, gender);
            bundle.put(HAIR, hair);
            bundle.put(HAIRFACE, hairFace);
            bundle.put(HAIRCOLOR, hairColor);

            bundle.put(DEAD, dead);
            bundle.put(ASCENDED, ascended);
        }

        public void restoreFromBundle(Bundle bundle) {
            heroClass = bundle.getInt(CLASS);
            gender = bundle.getInt(GENDER);
            hair = bundle.getInt(HAIR);
            hairFace = bundle.getInt(HAIRFACE);
            hairColor = bundle.getInt(HAIRCOLOR);

            Hero.preview(this, bundle.getBundle(Dungeon.HERO));
            depth = bundle.getInt(Dungeon.DEPTH);
            dead = bundle.getBoolean(DEAD);
            ascended = bundle.getBoolean(ASCENDED);

            if (depth == -1) {
                depth = bundle.getInt("maxDepth");
            }
        }
    }
}
