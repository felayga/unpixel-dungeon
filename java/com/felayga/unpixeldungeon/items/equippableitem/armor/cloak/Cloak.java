/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * unPixel Dungeon
 * Copyright (C) 2015-2016 Randall Foudray
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.felayga.unpixeldungeon.items.equippableitem.armor.cloak;

import com.felayga.unpixeldungeon.items.equippableitem.armor.Armor;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;

/**
 * Created by HELLO on 2/11/2017.
 */
public class Cloak extends Armor {
    public Cloak(int armor, int armorMagic, long speedModifier) {
        super(armor, 32767, armorMagic, speedModifier, GameTime.TICK / 2, 0);
    }

    @Override
    public Slot[] getSlots() {
        return new Slot[]{ Slot.Cloak };
    }

    @Override
    protected void setHeroSpriteArmor(HeroSprite heroSprite, boolean equip) {
        if (heroSprite == null) {
            return;
        }
        if (equip) {
            heroSprite.setCloak(this.spriteTextureIndex);
        }
        else {
            heroSprite.setCloak(HeroSprite.ArmorIndex.CloakNone);
        }
    }


}
