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

package com.felayga.unpixeldungeon.items.books.spellbook;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.negative.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.negative.Vertigo;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.items.Bomb;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.books.Book;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRage;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.items.spells.Spell;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.AttributeType;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

/**
 * Created by HELLO on 3/6/2017.
 */

public abstract class SpellBook extends Book {
    protected String spellName;
    protected Class<? extends Spell> learnedSpell;
    protected int spellLevel;

    public SpellBook(String spellName, Class<? extends Spell> learnedSpell, int spellLevel, long readTime) {
        super(readTime);
        name = "Tome of " + spellName;

        this.spellName = spellName;
        this.learnedSpell = learnedSpell;
        this.spellLevel = spellLevel;
    }

    protected void doRead(Char user) {
        int chance = (user.INTWIS() + 4 + user.level / 2 + 2 * spellLevel) * 16;

        if (user instanceof Hero) {
            Hero hero = (Hero) user;

            if (hero.heroClass == HeroClass.MAGE) {
                String difficulty = null;
                if (chance < 128) {
                    difficulty = "very difficult";
                } else if (chance < 256) {
                    difficulty = "difficult";
                }

                if (difficulty != null) {
                    //todo: this spellbook is [difficulty] to comprehend (for wizards).  continue?
                }
            }
        }

        if (Random.PassFail(chance)) {
            learnSpell(user, learnedSpell);
            setKnown();
        } else {
            Buff.prolong(user, null, Paralysis.class, readTime);

            switch (Random.Int(spellLevel)) {
                case 1:
                    ScrollOfRage.enrage(user, user.pos(), Level.LENGTH);
                    if (user == Dungeon.hero) {
                        GLog.n("You feel threatened.");
                    }
                    break;
                case 2:
                    Buff.prolong(user, null, Blindness.class, Random.IntRange(250, 350) * GameTime.TICK);
                    break;
                case 3:
                    Item gold = user.belongings.getItem(Gold.class, false);
                    if (gold != null && gold.quantity() > 0) {
                        int loss = Random.IntRange(100, 250);

                        if (gold.quantity() > loss) {
                            user.belongings.remove(gold, loss);
                            GLog.n("Your purse feels lighter.");
                        } else {
                            user.belongings.remove(gold);
                            GLog.n("You notice you have no gold!");
                        }
                    } else {
                        if (user == Dungeon.hero) {
                            GLog.w("You feel a strange sensation.");
                        }
                    }
                    break;
                case 4:
                    Buff.prolong(user, null, Vertigo.class, Random.IntRange(15, 25) * GameTime.TICK);
                    if (user == Dungeon.hero) {
                        GLog.n("These runes were just too much to comprehend.");
                    }
                    break;
                case 5:
                    EquippableItem gloves = user.belongings.gloves();

                    if (user == Dungeon.hero) {
                        GLog.n("The book was coated with contact poison!");
                    }

                    if (gloves != null) {
                        GLog.w("Good thing you were wearing gloves.");
                        gloves.corrode();
                    } else {
                        user.damage(Random.IntRange(1, 10), MagicType.Poison, null, null);

                        if ((user.resistanceMagical & MagicType.Poison.value) != 0) {
                            user.damageAttribute(AttributeType.STRCON, Random.IntRange(1, 2));
                        } else {
                            user.damageAttribute(AttributeType.STRCON, Random.IntRange(3, 6));
                        }
                    }
                    break;
                case 6:
                    GLog.n("As you read the book, it radiates explosive energy in your face!");
                    Bomb.explode(null, null, user.pos(), false, MagicType.Magic, 7, 25);
                    parent().remove(this);
                    return;
                default:
                    GLog.w("You feel a wrenching sensation.");
                    if (ScrollOfTeleportation.canTeleport(user)) {
                        ScrollOfTeleportation.doTeleport(user, Constant.Position.RANDOM);
                    }
                    break;
            }

            if (Random.Int(3)==0) {
                GLog.w("The spellbook crumbles into dust!");
                parent().remove(this);
                return;
            }
        }
    }

    private void learnSpell(Char user, Class<? extends Spell> learnedSpell) {
        Spell test = user.belongings.getItem(learnedSpell, false);

        if (test != null) {
            test.resetDecay();
        } else {
            Spell spell = null;
            try {
                spell = learnedSpell.newInstance();
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e) {
            }

            if (spell != null) {
                spell.level(spellLevel);
                user.belongings.collect(spell);
            }
        }
    }

    @Override
    public String desc() {
        return "This is a spell tome.  Its pages contain the knowledge required to teach yourself how to cast the " + spellName + " spell.";
    }
}
