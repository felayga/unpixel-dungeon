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
package com.felayga.unpixeldungeon.items.artifacts;


import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.LockedFloor;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class CloakOfShadows extends Artifact_old {

    {
        name = "Cloak of Shadows";
        image = ItemSpriteSheet.ARTIFACT_CLOAK;

        level(0);
        exp = 0;
        levelCap = 15;

        charge = level() + 5;
        partialCharge = 0;
        chargeCap = level() + 5;

        cooldown = 0;

        defaultAction = AC_STEALTH;

        unique = true;
        bones = false;
    }

    private boolean stealthed = false;

    public static final String AC_STEALTH = "STEALTH";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && charge > 1)
            actions.add(AC_STEALTH);
        return actions;
    }

    @Override
    public boolean execute(Hero hero, String action) {
        if (action.equals(AC_STEALTH)) {
            if (!stealthed) {
                if (!isEquipped(hero)) GLog.i("You need to equip your cloak to do that.");
                else if (cooldown > 0)
                    GLog.i("Your cloak needs " + cooldown + " more rounds to re-energize.");
                else if (charge <= 1)
                    GLog.i("Your cloak hasn't recharged enough to be usable yet.");
                else {
                    stealthed = true;
                    hero.spend_new(GameTime.TICK, false);
                    hero.busy();
                    Sample.INSTANCE.play(Assets.SND_MELD);
                    activeBuff = activeBuff();
                    activeBuff.attachTo(hero, null);
                    if (hero.sprite.parent != null) {
                        hero.sprite.parent.add(new AlphaTweener(hero.sprite, 0.4f, 0.4f));
                    } else {
                        hero.sprite.alpha(0.4f);
                    }
                    hero.sprite.operate(hero.pos());
                    GLog.i("Your cloak blends you into the shadows.");
                }
            } else {
                stealthed = false;
                activeBuff.detach();
                activeBuff = null;
                hero.sprite.operate(hero.pos());
                GLog.i("You return from underneath your cloak.");
            }

            return true;
        } else {
            return super.execute(hero, action);
        }
    }

    @Override
    public void onEquip(Char owner, boolean cursed) {
        super.onEquip(owner, cursed);

        if (stealthed) {
            activeBuff = activeBuff();
            activeBuff.attachTo(owner, null);
        }
    }

    @Override
    public void onUnequip(Char owner) {
        stealthed = false;
    }


    @Override
    protected ArtifactBuff passiveBuff() {
        return new cloakRecharge();
    }

    @Override
    protected ArtifactBuff activeBuff() {
        return new cloakStealth();
    }

    @Override
    public Item upgrade(Item source, int n) {
        chargeCap += n;
        return super.upgrade(source, n);
    }

    @Override
    public String desc() {
        String desc = "This light silken cloak shimmers in and out of your vision as it sways in the air. When worn, " +
                "it can be used to hide your presence for a short time.\n\n";

        if (level() < 5)
            desc += "The cloak's magic has faded and it is not very powerful, perhaps it will regain strength through use.";
        else if (level() < 10)
            desc += "The cloak's power has begun to return.";
        else if (level() < 15)
            desc += "The cloak has almost returned to full strength.";
        else
            desc += "The cloak is at full potential and will work for extended durations.";


        if (isEquipped(Dungeon.hero))
            desc += "\n\nThe cloak rests around your shoulders.";


        return desc;
    }

    private static final String STEALTHED = "stealthed";
    private static final String COOLDOWN = "cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STEALTHED, stealthed);
        bundle.put(COOLDOWN, cooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        stealthed = bundle.getBoolean(STEALTHED);
        cooldown = bundle.getInt(COOLDOWN);
    }

    public class cloakRecharge extends ArtifactBuff {
        @Override
        public boolean act() {
            if (charge < chargeCap) {
                LockedFloor lock = target.buff(LockedFloor.class);
                if (!stealthed && (lock == null || lock.regenOn()))
                    partialCharge += (1f / (60 - (chargeCap - charge) * 2));

                if (partialCharge >= 1) {
                    charge++;
                    partialCharge -= 1;
                    if (charge == chargeCap) {
                        partialCharge = 0;
                    }

                }
            } else
                partialCharge = 0;

            if (cooldown > 0)
                cooldown--;

            updateQuickslot();

            spend_new(GameTime.TICK, false);

            return true;
        }

    }

    public class cloakStealth extends ArtifactBuff {
        int turnsToCost = 0;

        @Override
        public int icon() {
            return BuffIndicator.INVISIBLE;
        }

        @Override
        public boolean attachTo(Char target, Char source) {
            if (super.attachTo(target, source)) {
                target.invisible++;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean act() {
            if (turnsToCost == 0) charge--;
            if (charge <= 0) {
                detach();
                GLog.w("Your cloak has run out of energy.");
                ((Hero) target).interrupt();
            }

            if (turnsToCost == 0) exp += 10 + target.level;

            if (exp >= (level() + 1) * 50 && level() < levelCap) {
                upgrade(null, 1);
                exp -= level() * 50;
                GLog.p("Your cloak grows stronger!");
            }

            if (turnsToCost == 0) turnsToCost = 2;
            else turnsToCost--;
            updateQuickslot();

            spend_new(GameTime.TICK, false);

            return true;
        }

        public void dispel() {
            charge--;

            exp += 10 + ((Hero) target).level;

            if (exp >= (level() + 1) * 50 && level() < levelCap) {
                upgrade(null, 1);
                exp -= level() * 50;
                GLog.p("Your cloak grows stronger!");
            }

            updateQuickslot();
            detach();
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.INVISIBLE);
            else if (target.invisible == 0) target.sprite.remove(CharSprite.State.INVISIBLE);
        }

        @Override
        public String toString() {
            return "Cloaked";
        }

        @Override
        public String desc() {
            return "Your cloak of shadows is granting you invisibility while you are shrouded by it.\n" +
                    "\n" +
                    "While you are invisible enemies are unable to attack or follow you. " +
                    "Most physical attacks and magical effects (such as scrolls and wands) will immediately cancel invisibility.\n" +
                    "\n" +
                    "You will remain cloaked until it is cancelled or your cloak runs out of charge.";
        }

        @Override
        public void detach() {
            if (target.invisible > 0)
                target.invisible--;
            stealthed = false;
            cooldown = 10 - (level() / 3);

            updateQuickslot();
            super.detach();
        }
    }
}
