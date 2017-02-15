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
package com.felayga.unpixeldungeon.items.armor.glyphs;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.armor.Armor.Glyph;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSprite.Glowing;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Viscosity extends Glyph {

    private static final String TXT_VISCOSITY = "%s of viscosity";

    private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x8844CC);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        if (damage == 0) {
            return 0;
        }

        int level = Math.max(0, armor.level());

        if (Random.Int(level + 7) >= 6) {

            DeferedDamage debuff = defender.buff(DeferedDamage.class);
            if (debuff == null) {
                debuff = new DeferedDamage();
                debuff.attachTo(defender, attacker);
            }
            debuff.prolong(damage);

            defender.sprite.showStatus(CharSprite.WARNING, "deferred %d", damage);

            return 0;

        } else {
            return damage;
        }
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_VISCOSITY, weaponName);
    }

    @Override
    public Glowing glowing() {
        return PURPLE;
    }

    public static class DeferedDamage extends Buff {

        protected int damage = 0;

        private static final String DAMAGE = "damage";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DAMAGE, damage);

        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            damage = bundle.getInt(DAMAGE);
        }

        @Override
        public boolean attachTo(Char target, Char source) {
            if (super.attachTo(target, source)) {
                postpone(GameTime.TICK);
                return true;
            } else {
                return false;
            }
        }

        public void prolong(int damage) {
            this.damage += damage;
        }

        ;

        @Override
        public int icon() {
            return BuffIndicator.DEFERRED;
        }

        @Override
        public String toString() {
            return Utils.format("Defered damage", damage);
        }

        @Override
        public boolean act() {
            if (target.isAlive()) {

                target.damage(1, MagicType.Mundane, null);
                if (target == Dungeon.hero && !target.isAlive()) {

                    Glyph glyph = new Viscosity();
                    Dungeon.fail(Utils.format(ResultDescriptions.GLYPH, glyph.name()));
                    GLog.n("%s killed you...", glyph.name());

                    Badges.validateDeathFromGlyph();
                }
                spend_new(GameTime.TICK, false);

                if (--damage <= 0) {
                    detach();
                }

            } else {

                detach();

            }

            return true;
        }

        @Override
        public String desc() {
            return "While your armor's glyph has protected you from damage, it seems to be slowly paying you back for it.\n" +
                    "\n" +
                    "Damage is being dealt to you over time instead of immediately. " +
                    "You will take one damage per turn until there is no damage left.\n" +
                    "\n" +
                    "There is " + damage + " deffered damage left.";
        }
    }
}
