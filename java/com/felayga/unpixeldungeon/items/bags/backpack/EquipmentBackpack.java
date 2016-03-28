package com.felayga.unpixeldungeon.items.bags.backpack;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.rings.Ring;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.items.weapon.Weapon;

/**
 * Created by hello on 3/17/16.
 */
public class EquipmentBackpack extends Backpack {

    public EquipmentBackpack(Char owner)
    {
        super(owner);

        priority = 15;
    }

    @Override
    public boolean grab( Item item ) {
        return item instanceof Weapon
                || item instanceof Armor
                || item instanceof Ring
                || item instanceof Tool;
    }

}
