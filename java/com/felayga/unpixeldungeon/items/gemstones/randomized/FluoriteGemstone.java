package com.felayga.unpixeldungeon.items.gemstones.randomized;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.gemstones.Gemstone;

/**
 * Created by HELLO on 3/16/2016.
 */
public class FluoriteGemstone extends Gemstone {
    public FluoriteGemstone()
    {
        super(
                GemstoneColor.White,
                GemstoneColor.Green,
                GemstoneColor.Blue,
                GemstoneColor.Violet
        );
    }

    @Override
    protected void buildName() {
        name = color + " fluorite stone";
    }

}
