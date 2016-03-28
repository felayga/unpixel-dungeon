package com.felayga.unpixeldungeon.items.tools.unlocking;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.QuickSlot;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.items.bags.TreasureChest;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.ui.QuickSlotButton;
import com.felayga.unpixeldungeon.utils.GLog;

import java.util.ArrayList;

/**
 * Created by hello on 3/13/16.
 */
public class UnlockingTool extends Tool {
    public static final String NAME = "unlocking tool";

    public UnlockingTool()
    {
        super(true);
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped(hero)) {
            actions.add(AC_APPLY);
        }
        return actions;
    }

    public boolean unlockDoor(Char user) {
        return false;
    }

    public boolean unlockChest(Char user) {
        return false;
    }

    @Override
    public final String getToolClass() {
        return NAME;
    }

    @Override
    public void apply(Hero hero, int target) {
        int cell = Dungeon.level.map[target];

        if (cell == Terrain.LOCKED_DOOR) {
            hero.curAction = new HeroAction.HandleDoor.UnlockDoor(target, this);
            hero.spend(1, true);
            return;
        }

        Heap heap = Dungeon.level.heaps.get(target);
        if (heap != null) {
            Item heapItem = heap.peek();

            if (heapItem != null && heapItem instanceof Bag) {
                Bag bag = (Bag)heapItem;

                hero.curAction = new HeroAction.UnlockBag(this, bag, cell);
                hero.spend(1, true);
                return;
            }
        }

        GLog.n("Your " + NAME + " can't be applied there.");
    }

}
