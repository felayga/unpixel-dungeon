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

package com.felayga.unpixeldungeon.items.consumable.potions;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 2/4/2017.
 */
public class PotionOfGainAbility extends Potion {

    public PotionOfGainAbility()
    {
        name = "Potion of Gain Ability";
        initials = "GA";

        bones = true;
        isHelpful = true;

        price = 100;
    }

    @Override
    public void apply( Char hero ) {
        if (bucStatus() == BUCStatus.Cursed) {
            GLog.n("Ulch!  That potion tasted foul!");
        }
        else {
            setKnown();

            int good = 6;

            while (good > 0) {
                int test = Random.Int(6);
                switch (test / 2) {
                    case 0:
                        if (hero.tryIncreaseAttribute(AttributeType.STRCON, 1)) {
                            if (test % 2 == 0) {
                                GLog.p("You feel strong!");
                            } else {
                                GLog.p("You feel tough!");
                            }
                            good = -1;
                        } else {
                            good--;
                        }
                        break;
                    case 1:
                        if (hero.tryIncreaseAttribute(AttributeType.DEXCHA, 1)) {
                            if (test % 2 == 0) {
                                GLog.p("You feel agile!");
                            } else {
                                GLog.p("You feel charismatic!");
                            }
                            good = -1;
                        } else {
                            good--;
                        }
                        break;
                    default:
                        if (hero.tryIncreaseAttribute(AttributeType.INTWIS, 1)) {
                            if (test % 2 == 0) {
                                GLog.p("You feel smart!");
                            } else {
                                GLog.p("You feel wise!");
                            }
                            good = -1;
                        } else {
                            good--;
                        }
                        break;
                }
            }

            if (good == 0) {
                GLog.p("You seem to have reached the pinnacle of your abilities.");
            }
        }

		/*
		hero.STR++;
		hero.sprite.showStatus( CharSprite.POSITIVE, "+1 str" );
		GLog.p( "Newfound strength surges through your body." );
		*/

        /*
		GLog.p("Nope.");
		Badges.validateStrengthAttained();
		*/
    }

    @Override
    public String desc() {
        return
                "This powerful liquid will course through your muscles, " +
                        "permanently increasing your strength by one point.";
    }

}

