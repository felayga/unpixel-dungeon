package com.felayga.unpixeldungeon.items.bags.backpack;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Bomb;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.food.Food;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.items.wands.Wand;

/**
 * Created by hello on 3/17/16.
 */
public class ConsumablesBackpack extends Backpack {

    public ConsumablesBackpack(Char owner)
    {
        super(owner);

        priority = 15;
    }

    @Override
    public boolean grab( Item item ) {
        return item instanceof Potion
                || item instanceof Scroll
                || item instanceof Food
                || item instanceof Bomb
                || item instanceof Wand;
    }

}
