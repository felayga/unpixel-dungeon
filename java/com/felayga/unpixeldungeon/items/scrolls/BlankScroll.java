package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.utils.GLog;

/**
 * Created by hello on 3/16/16.
 */
public class BlankScroll extends Scroll {
    public BlankScroll()
    {
        name = "Blank Scroll";
        initials = "--";
    }

    @Override
    public boolean execute(Hero hero, String action) {
        if (action.equals(AC_READ)) {
            GLog.n("This scroll is blank.");
            identify();
            return false;
        } else {
            return super.execute(hero, action);
        }
    }

    @Override
    public void doRead() {
    }
}
