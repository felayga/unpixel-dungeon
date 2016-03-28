package com.felayga.unpixeldungeon.items.armor.boots;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.bags.Bag;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.CellSelector;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by HELLO on 3/23/2016.
 */
public class Boots extends Armor {
    public Boots(int armor, long speedModifier) {
        super(armor, 32767, speedModifier, GameTime.TICK / 2, 0);

        defaultAction = null;
    }

    private static Boots curBoots;

    @Override
    protected Armor getEquipmentSlot(Char hero)
    {
        return hero.belongings.boots;
    }

    @Override
    protected void setEquipmentSlot(Char hero, Armor item)
    {
        hero.belongings.boots = item;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped(hero)) {
            actions.add(AC_KICK);
        }
        return actions;
    }

    @Override
    protected void setHeroSpriteArmor(HeroSprite heroSprite, boolean equip) {
        //empty
    }

    @Override
    public boolean doEquip( Char ch ) {
        boolean retval = super.doEquip(ch);

        if (retval) {
            defaultAction = AC_KICK;

            if (ch instanceof Hero) {
                int index = Dungeon.quickslot.getPlaceholder(this);
                if (index >= 0) {
                    Dungeon.quickslot.setSlot(index, this);
                }
            }
        }

        return retval;
    }

    @Override
    public boolean doUnequip( Char ch, boolean collect, boolean single ) {
        boolean retval = super.doUnequip(ch, collect, single);

        if (retval) {
            defaultAction = null;

            if (ch instanceof Hero) {
                int index = Dungeon.quickslot.getSlot(this);
                if (index >= 0) {
                    Dungeon.quickslot.convertToPlaceholder(this);
                }
            }
        }

        return retval;
    }

    @Override
    public boolean isEquipped(Char hero)
    {
        return hero.belongings.boots == this;
    }

    @Override
    public boolean execute(Hero hero, String action) {
        curUser = hero;
        curBoots = this;

        if (action.equals(AC_KICK)) {
            GameScene.selectCell(kicker);

            return false;
        }
        else {
            return super.execute(hero, action);
        }
    }

    public void kick(Hero hero, int target) {
        int cell = Dungeon.level.map[target];

        if (cell == Terrain.LOCKED_DOOR || cell == Terrain.DOOR) {
            hero.curAction = new HeroAction.HandleDoor.KickDoor(target);
            hero.spend(1, true);
            return;
        }

        hero.spend(GameTime.TICK, false);

        Heap heap = Dungeon.level.heaps.get(target);
        if (heap != null) {
            Item item = heap.peek();

            if (item != null) {
            }
        }

        if (Dungeon.level.passable[target] || Dungeon.level.pit[target])
        {
            if (Random.Int(5)==0) {
                GLog.n("Dumb move!  You strain a muscle.");
                hero.useAttribute(AttributeType.STRCON, -1);
                Cripple.prolong(hero, Cripple.class, GameTime.TICK * Random.IntRange(4, 12));
            }
            else {
                GLog.w("You kick at empty space.");
            }
            return;
        }
        else {
            GLog.n("Ouch!  That hurts.");
            hero.damage(Random.IntRange(1,4), cell);
            if (Random.Int(3)==0) {
                Cripple.prolong(hero, Cripple.class, GameTime.TICK * Random.IntRange(4, 12));
            }
            return;
        }

    }

    protected static CellSelector.Listener kicker = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                curBoots.kick(curUser, target);
            }
        }

        @Override
        public String prompt() {
            return "Kick";
        }
    };

}
