package com.felayga.unpixeldungeon.items.gemstones.randomized;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.gemstones.Gemstone;

/**
 * Created by HELLO on 3/16/2016.
 */
public class TurquoiseGemstone extends Gemstone {
    public TurquoiseGemstone()
    {
        super(
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
