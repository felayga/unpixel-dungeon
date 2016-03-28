package com.felayga.unpixeldungeon.items.bags.backpack;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.rings.Ring;
import com.felayga.unpixeldungeon.items.spells.Spell;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.ui.Icons;

/**
 * Created by hello on 3/18/16.
 */
public class Spellbook extends Backpack {

    public Spellbook(Char owner)
    {
        super(owner);

        priority = 15;
        tabIcon = Icons.SPELLBOOK;
    }

    @Override
    public boolean grab( Item item ) {
        return item instanceof Spell;
    }

}
