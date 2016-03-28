package com.felayga.unpixeldungeon.items.bags.backpack;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.ui.Icons;

/**
 * Created by hello on 3/17/16.
 */
public class Backpack extends Bag {
    public Backpack(Char owner) {
        super(owner);

        tabIcon = Icons.BACKPACK;
    }

}
