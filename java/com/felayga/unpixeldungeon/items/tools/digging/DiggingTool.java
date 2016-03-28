package com.felayga.unpixeldungeon.items.tools.digging;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.tools.Tool;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.utils.GLog;

/**
 * Created by HELLO on 3/14/2016.
 */
public class DiggingTool extends Tool {
    public static final String NAME = "digging tool";

    public DiggingTool() {
        super(true);
    }

    @Override
    public final String getToolClass() {
        return NAME;
    }

    @Override
    public void apply(Hero hero, int target) {
        int cell = Dungeon.level.map[target];

        {
            GLog.n("Your " + NAME + " can't be applied there.");
        }
    }
}
