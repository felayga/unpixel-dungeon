package com.felayga.unpixeldungeon.items.armor.gloves;

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
public class Gloves extends Armor {
    public Gloves(int armor, long speedModifier) {
        super(armor, 32767, speedModifier, GameTime.TICK / 2, 0);
    }

    @Override
    protected Armor getEquipmentSlot(Char hero)
    {
        return hero.belongings.gloves;
    }

    @Override
    protected void setEquipmentSlot(Char hero, Armor item)
    {
        hero.belongings.gloves = item;
    }

    @Override
    protected void setHeroSpriteArmor(HeroSprite heroSprite, boolean equip) {
        //empty
    }


    @Override
    public boolean isEquipped(Char hero)
    {
        return hero.belongings.gloves == this;
    }

}
