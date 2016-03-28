package com.felayga.unpixeldungeon.items.tools.digging;

import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by HELLO on 3/15/2016.
 */
public class Pickaxe extends DiggingTool {
    public Pickaxe()
    {
        name = "pickaxe";
        image = ItemSpriteSheet.PICKAXE;
    }

    @Override
    public String desc() {
        return "This tool is built for smashing rock walls and wooden barricades.";
    }

}
