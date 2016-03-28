package com.felayga.unpixeldungeon.items.gemstones.randomized;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.gemstones.Gemstone;

/**
 * Created by HELLO on 3/16/2016.
 */
public class AquamarineGemstone extends Gemstone {
    public AquamarineGemstone()
    {
        super(
                GemstoneColor.Green,
                GemstoneColor.Blue
        );
    }

    @Override
    protected void buildName() {
        name = color + " aquamarine stone";
    }

}
