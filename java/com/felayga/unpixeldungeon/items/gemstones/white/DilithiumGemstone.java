package com.felayga.unpixeldungeon.items.gemstones.white;

import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.gemstones.Gemstone;

/**
 * Created by HELLO on 3/16/2016.
 */
public class DilithiumGemstone extends Gemstone {
    public DilithiumGemstone()
    {
        super(GemstoneColor.White);
    }

    @Override
    protected void buildName()
    {
        name = "dilithium crystal";
    }

}
