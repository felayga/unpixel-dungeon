package com.felayga.unpixeldungeon.items.gemstones.randomized;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.gemstones.Gemstone;

/**
 * Created by HELLO on 3/16/2016.
 */
public class GlassGemstone extends Gemstone {
    public GlassGemstone()
    {
        super(
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
    protected void buildName()
    {
        name = "piece of " + color + " glass";
    }

    @Override
    public String desc()
    {
        return "This is a piece of "+color+"-colored glass. It is completely worthless.";
    }

}
