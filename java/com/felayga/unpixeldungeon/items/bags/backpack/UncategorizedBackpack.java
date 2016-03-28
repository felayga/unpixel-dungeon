package com.felayga.unpixeldungeon.items.bags.backpack;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.Bag;

import java.util.ArrayList;

/**
 * Created by hello on 3/17/16.
 */
public class UncategorizedBackpack extends Backpack {

    public UncategorizedBackpack(Char owner)
    {
        super(owner);
    }

    @Override
    public boolean grab( Item item ) {
        return false;
    }

}
