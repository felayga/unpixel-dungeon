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

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.sprites.MissileSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/16/2016.
 */
public class Gemstone extends Item {

    public GemstoneColor color;
    public String colorName;
    public boolean hardness;
    private boolean identified = false;

    public Gemstone(int price, boolean isHard, GemstoneColor... choices) {
        super();

        this.price = price;
        this.hardness = isHard;

        int index;

        if (choices.length > 1) {
            index = Random.Int(choices.length);
        } else {
            index = 0;
        }

        setColor(choices[index]);
        stackable = true;

        weight(Encumbrance.UNIT);
        hasBuc(false);
        hasLevels(false);
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && identified;
    }

    @Override
    public Item identify(boolean updateQuickslot) {
        identified = true;

        return super.identify(updateQuickslot);
    }

    protected void buildName() {
        name = "bad gemstone";
    }


    private static final String COLOR = "color";
    private static final String IDENTIFIED = "identified";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COLOR, color.value);
        bundle.put(IDENTIFIED, identified);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        setColor(GemstoneColor.fromInt(bundle.getInt(COLOR)));
        identified = bundle.getBoolean(IDENTIFIED);
    }

    @Override
    public String getName() {
        return isIdentified() ? super.getName() : colorName + " gem";
    }

    @Override
    public String info() {
        return isIdentified() ?
                desc() :
                "This is a " + colorName + "-colored gemstone.  Who knows what it is?";
    }


    public enum GemstoneColor {
        White(0),
        Red(1),
        Orange(2),
        Brown(3),
        Yellow(4),
        Green(5),
        Blue(6),
        Violet(7),
        Black(8),
        Gray(9);

        public final int value;

        GemstoneColor(int value) {
            this.value = value;
        }

        public static GemstoneColor fromInt(int index) {
            switch (index) {
                case 0:
                    return GemstoneColor.White;
                case 1:
                    return GemstoneColor.Red;
                case 2:
                    return GemstoneColor.Orange;
                case 3:
                    return GemstoneColor.Brown;
                case 4:
                    return GemstoneColor.Yellow;
                case 5:
                    return GemstoneColor.Green;
                case 6:
                    return GemstoneColor.Blue;
                case 7:
                    return GemstoneColor.Violet;
                case 8:
                    return GemstoneColor.Black;
                default:
                    return GemstoneColor.Gray;
            }
        }

        public static String getName(GemstoneColor color) {
            switch (color) {
                case White:
                    return "white";
                case Red:
                    return "red";
                case Orange:
                    return "orange";
                case Brown:
                    return "brown";
                case Yellow:
                    return "yellow";
                case Green:
                    return "green";
                case Blue:
                    return "blue";
                case Violet:
                    return "violet";
                case Black:
                    return "black";
                default:
                    return "gray";
            }
        }

        public static int getItemSpriteSheetIndex(GemstoneColor color) {
            switch (color) {
                case White:
                    return ItemSpriteSheet.GEMSTONE_WHITE;
                case Red:
                    return ItemSpriteSheet.GEMSTONE_RED;
                case Orange:
                    return ItemSpriteSheet.GEMSTONE_ORANGE;
                case Brown:
                    return ItemSpriteSheet.GEMSTONE_BROWN;
                case Yellow:
                    return ItemSpriteSheet.GEMSTONE_YELLOW;
                case Green:
                    return ItemSpriteSheet.GEMSTONE_GREEN;
                case Blue:
                    return ItemSpriteSheet.GEMSTONE_BLUE;
                case Violet:
                    return ItemSpriteSheet.GEMSTONE_VIOLET;
                case Black:
                    return ItemSpriteSheet.GEMSTONE_BLACK;
                default:
                    return ItemSpriteSheet.GEMSTONE_GRAY;
            }
        }
    }

    private void setColor(GemstoneColor choice) {
        color = choice;
        colorName = GemstoneColor.getName(choice);
        image = GemstoneColor.getItemSpriteSheetIndex(choice);

        buildName();
    }

    @Override
    protected boolean checkSimilarity(Item item) {
        if (!super.checkSimilarity(item)) {
            return false;
        }

        if (item.getClass() == this.getClass()) {
            Gemstone gemstone = (Gemstone) item;

            return color.equals(gemstone.color);
        }

        return false;
    }


    //region black gemstones

    public static class BlackOpal extends Gemstone {
        public BlackOpal() {
            super(2500, true, GemstoneColor.Black);
        }

        @Override
        protected void buildName() {
            name = "black opal";
        }
    }

    public static class JetStone extends Gemstone {
        public JetStone() {
            super(850, false, GemstoneColor.Black);
        }

        @Override
        protected void buildName() {
            name = "jet stone";
        }
    }

    public static class Obsidian extends Gemstone {
        public Obsidian() {
            super(200, false, GemstoneColor.Black);
        }

        @Override
        protected void buildName() {
            name = "obsidian stone";
        }
    }

    //endregion
    //region red gemstones

    public static class Garnet extends Gemstone {
        public Garnet() {
            super(700, false, GemstoneColor.Red);
        }

        @Override
        protected void buildName() {
            name = "garnet";
        }
    }

    public static class Jasper extends Gemstone {
        public Jasper() {
            super(500, false, GemstoneColor.Red);
        }

        @Override
        protected void buildName() {
            name = "jasper";
        }
    }

    public static class Ruby extends Gemstone {
        public Ruby() {
            super(3500, true, GemstoneColor.Red);
        }

        @Override
        protected void buildName() {
            name = "ruby";
        }
    }

    //endregion
    //region orange gemstones

    public static class Jacinth extends Gemstone {
        public Jacinth() {
            super(3250, true, GemstoneColor.Orange);
        }

        @Override
        protected void buildName() {
            name = "jacinth";
        }
    }

    public static class Sunstone extends Gemstone {
        public Sunstone() {
            super(100, false, GemstoneColor.Orange);
        }

        @Override
        protected void buildName() {
            name = "sunstone";
        }
    }

    //endregion
    //region yellow gemstones

    public static class Chrysoberyl extends Gemstone {
        public Chrysoberyl()
        {
            super(700, false, GemstoneColor.Yellow);
        }

        @Override
        protected void buildName()
        {
            name = "chrysoberyl";
        }
    }

    public static class Citrine extends Gemstone {
        public Citrine()
        {
            super(1500, false, GemstoneColor.Yellow);
        }

        @Override
        protected void buildName()
        {
            name = "citrine stone";
        }
    }

    //endregion
    //region brown gemstones

    public static class Amber extends Gemstone {
        public Amber()
        {
            super(1000, false, GemstoneColor.Brown);
        }

        @Override
        protected void buildName() {
            name = "amber";
        }
    }

    public static class TigersEye extends Gemstone {
        public TigersEye()
        {
            super(200, false, GemstoneColor.Brown);
        }

        @Override
        protected void buildName() {
            name = "tiger's eye";
        }
    }

    public static class Topaz extends Gemstone {
        public Topaz()
        {
            super(900, true, GemstoneColor.Brown);
        }

        @Override
        protected void buildName() {
            name = "topaz";
        }
    }

    //endregion
    //region green gemstones

    public static class Emerald extends Gemstone {
        public Emerald() {
            super(2500, true, GemstoneColor.Green);
        }

        @Override
        protected void buildName() {
            name = "emerald";
        }
    }

    public static class Jade extends Gemstone {
        public Jade() {
            super(300, false, GemstoneColor.Green);
        }

        @Override
        protected void buildName() {
            name = "jade";
        }
    }

    //endregion
    //region blue gemstones

    public static class LapisLazuli extends Gemstone {
        public LapisLazuli() {
            super(100, true, GemstoneColor.Blue);
        }

        @Override
        protected void buildName() {
            name = "lapis lazuli";
        }
    }

    public static class Sapphire extends Gemstone {
        public Sapphire() {
            super(3250, true, GemstoneColor.Blue);
        }

        @Override
        protected void buildName() {
            name = "sapphire";
        }
    }

    //endregion
    //region violet gemstones

    public static class Amethyst extends Gemstone {
        public Amethyst() {
            super(600, false, GemstoneColor.Violet);
        }

        @Override
        protected void buildName() {
            name = "amethyst";
        }
    }

    //endregion
    //region white gemstones

    public static class Diamond extends Gemstone {
        public Diamond() {
            super(4000, true, GemstoneColor.White);
        }

        @Override
        protected void buildName() {
            name = "diamond";
        }
    }

    public static class Dilithium extends Gemstone {
        public Dilithium() {
            super(4500, false, GemstoneColor.White);
        }

        @Override
        protected void buildName() {
            name = "dilithium crystal";
        }
    }

    public static class Opal extends Gemstone {
        public Opal()
        {
            super(800, false, GemstoneColor.White);
        }

        @Override
        protected void buildName() {
            name = "opal";
        }
    }

    //endregion
    //region randomized gemstones

    public static class Agate extends Gemstone {
        public Agate() {
            super(200, false,
                    GemstoneColor.White,
                    GemstoneColor.Red,
                    GemstoneColor.Orange,
                    GemstoneColor.Brown,
                    GemstoneColor.Yellow,
                    GemstoneColor.Green,
                    GemstoneColor.Blue,
                    GemstoneColor.Violet,
                    GemstoneColor.Black
            );
        }

        @Override
        protected void buildName() {
            name = colorName + " agate";
        }
    }

    public static class Aquamarine extends Gemstone {
        public Aquamarine()
        {
            super(1500, true,
                    GemstoneColor.Green,
                    GemstoneColor.Blue
            );
        }

        @Override
        protected void buildName() {
            name = colorName + " aquamarine";
        }
    }

    public static class Fluorite extends Gemstone {
        public Fluorite() {
            super(400, false,
                    GemstoneColor.White,
                    GemstoneColor.Green,
                    GemstoneColor.Blue,
                    GemstoneColor.Violet
            );
        }

        @Override
        protected void buildName() {
            name = colorName + " fluorite";
        }
    }

    public static class Glass extends Gemstone {
        public Glass() {
            super(0, false,
                    GemstoneColor.White,
                    GemstoneColor.Red,
                    GemstoneColor.Orange,
                    GemstoneColor.Brown,
                    GemstoneColor.Yellow,
                    GemstoneColor.Green,
                    GemstoneColor.Blue,
                    GemstoneColor.Violet,
                    GemstoneColor.Black
            );
        }

        public int shopkeeperPriceJacking() {
            switch(color){
                case White:
                    return 800;
                case Blue:
                case Red:
                    return 700;
                case Brown:
                    return 600;
                case Orange:
                    return 500;
                case Yellow:
                    return 400;
                case Violet:
                    return 300;
                case Black:
                case Green:
                    return 200;
                default:
                    return 100;
            }
        }

        @Override
        protected void buildName() {
            name = "piece of " + colorName + " glass";
        }

        @Override
        public String desc() {
            return "This is a piece of " + color + "-colored glass. It is completely worthless.";
        }
    }

    public static class Onyx extends Gemstone {
        public Onyx() {
            super(100, false,
                    GemstoneColor.Red,
                    GemstoneColor.Black
            );
        }

        @Override
        protected void buildName() {
            name = colorName + " onyx";
        }
    }

    public static class Tanzanite extends Gemstone {
        public Tanzanite() {
            super(1000, false,
                    GemstoneColor.Blue,
                    GemstoneColor.Violet
            );
        }

        @Override
        protected void buildName() {
            name = colorName + " tanzanite";
        }
    }

    public static class Turquoise extends Gemstone {
        public Turquoise()
        {
            super(2000, false,
                    GemstoneColor.Green,
                    GemstoneColor.Blue
            );
        }

        @Override
        protected void buildName()
        {
            name = color + " turquoise stone";
        }
    }

    public static class Zircon extends Gemstone {
        public Zircon()
        {
            super(500, true,
                    GemstoneColor.Brown,
                    GemstoneColor.Yellow,
                    GemstoneColor.Green,
                    GemstoneColor.Blue,
                    GemstoneColor.White
            );
        }

        @Override
        protected void buildName()
        {
            name = color + " zircon";
        }
    }

    //endregion
    //region gray stones

    public static class Flintstone extends Gemstone {
        public Flintstone() {
            super(1, true, GemstoneColor.Gray);
            weight(Encumbrance.UNIT * 10);
        }

        @Override
        protected void buildName() {
            name = "flint stone";
        }
    }

    public static class Loadstone extends Gemstone {
        public Loadstone() {
            super(1, true, GemstoneColor.Gray);
            weight(Encumbrance.UNIT * 500);
            hasBuc(true);
            bucStatus(BUCStatus.Cursed, false);
        }

        //special pickup logic already implemented in Hero

        @Override
        protected void buildName() {
            name = "loadstone";
        }

        @Override
        public void doDrop(final Hero hero) {
            if (bucStatus() == BUCStatus.Cursed) {
                if (!bucStatusKnown()) {
                    bucStatus(true);
                }

                GLog.n("You can't drop the " + getDisplayName() + "!");
            } else {
                super.doDrop(hero);
            }
        }

        @Override
        protected void onThrow(Char thrower, int cell) {
            if (bucStatus() == BUCStatus.Cursed) {
                if (!bucStatusKnown()) {
                    bucStatus(true);
                }

                ((MissileSprite) thrower.sprite.parent.recycle(MissileSprite.class)).reset(cell, thrower.pos(), this, null);
                GLog.n("The " + getDisplayName()+" finds its way back into your backpack!");
                thrower.belongings.collect(this);
            } else {
                super.onThrow(thrower, cell);
            }
        }
    }

    public static class Luckstone extends Gemstone {
        public Luckstone() {
            super(60, true, GemstoneColor.Gray);
            weight(Encumbrance.UNIT * 10);
        }

        @Override
        protected void buildName() {
            name = "luckstone";
        }
    }

    public static class Touchstone extends Gemstone {
        public Touchstone() {
            super(45, true, GemstoneColor.Gray);
            weight(Encumbrance.UNIT * 10);
        }

        //todo: touchstone application

        @Override
        protected void buildName() {
            name = "touchstone";
        }
    }

    //endregion
}
