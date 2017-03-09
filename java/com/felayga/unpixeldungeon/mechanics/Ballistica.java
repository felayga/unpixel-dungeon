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
package com.felayga.unpixeldungeon.mechanics;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.hero.Encumbrance;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Level;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class Ballistica {
    public enum TravelCause {
        Kicked,
        Thrown,
        Launched
    }

    public static int maxDistance(Char thrower, Item what, TravelCause cause) {
        int strcon = thrower.getAttributeModifier(AttributeType.STRCON);
        int itemWeight = what.weight();

        EquippableItem item;
        int enchantment = 0;
        switch (cause) {
            case Kicked:
                item = thrower.belongings.boots();

                if (item != null) {
                    enchantment += item.level();
                }
                break;
            case Launched:
                item = thrower.belongings.ranged();

                enchantment += 2;
                if (item != null) {
                    enchantment += item.level();
                }
                break;
            default:
                break;
        }

        return strcon + 4 + enchantment - itemWeight / Encumbrance.UNIT / 40;
    }


    //note that the path is the FULL path of the projectile, including tiles after collision.
    //make sure to generate a subPath for the common case of going source to collision.
    public ArrayList<Integer> path = new ArrayList<>();
    public ArrayList<Integer> bounces = new ArrayList<>();
    public Integer sourcePos = null;
    public Integer collisionPos = null;
    public Integer dist = 0;
    public final int mode;

    //parameters to specify the colliding cell
    public enum Mode {
        NoCollision     (0x0001),
        StopTarget      (0x0002), //ballistica will stop at the target cell
        StopChars       (0x0004), //ballistica will stop on first char hit
        StopTerrain     (0x0008), //ballistica will stop on terrain(LOS blocking, impassable, etc.)
        BounceTerrain   (0x0010), //since this value has both "bounce" and "terrain" in its name, it's fair to assume that it will bounce off terrain
        StopSelf        (0x0020),
        Mask            (0x002F),

        IsDirected      (value(StopTarget, StopChars, StopTerrain, NoCollision)),

        Projectile      (value(StopTarget, StopChars, StopTerrain)),
        MagicBolt       (value(StopChars, StopTerrain)),
        SeekerBolt      (value(StopTarget, StopChars)),
        SplasherBolt    (value(StopTarget, StopTerrain)),
        MagicRay        (value(StopChars, StopTerrain, BounceTerrain));

        public final int value;

        Mode(int value) {
            this.value = value;
        }

        public static int value(Mode... modes) {
            int retval = 0;

            for (Mode mode : modes) {
                retval |= mode.value;
            }

            return retval;
        }
    }

    public Ballistica(int from, int to, int mode) {
        this.mode = mode;

        sourcePos = from;
        build(from, to);
        if (collisionPos != null) {
            dist = path.indexOf(collisionPos);
        } else {
            dist = path.size() - 1;
            if (dist >= 0) {
                collisionPos = path.get(dist);
            } else {
                collisionPos = -1;
            }
        }
    }

    private void build(int from, int to) {
        boolean stopTarget = (mode & Mode.StopTarget.value) != 0;
        boolean stopChars = (mode & Mode.StopChars.value) != 0;
        boolean stopTerrain = (mode & Mode.StopTerrain.value) != 0;

        int w = Level.WIDTH;

        int x0 = from % w;
        int x1 = to % w;
        int y0 = from / w;
        int y1 = to / w;

        int dx = x1 - x0;
        int dy = y1 - y0;

        int stepX = dx > 0 ? +1 : -1;
        int stepY = dy > 0 ? +1 : -1;

        dx = Math.abs(dx);
        dy = Math.abs(dy);

        int stepA;
        int stepB;
        int dA;
        int dB;

        if (dx > dy || (dx == dy && Random.Int(2) == 0)) {
            stepA = stepX;
            stepB = stepY * w;
            dA = dx;
            dB = dy;
        } else {
            stepA = stepY * w;
            stepB = stepX;
            dA = dy;
            dB = dx;
        }

        //GLog.d("Ballistica stepX=" + stepX + " stepY=" + stepY + " dA=" + dA + " dB=" + dB);

        int cell = from;

        int bouncesLeft = (mode & Mode.BounceTerrain.value) != 0 ? 2 : 0;
        boolean firstRun = true;

        int err = dA / 2;
        while (Level.insideMap(cell)) {
            boolean bounced = false;

            //if we're in a wall, collide with the previous cell along the path.
            if (stopTerrain && cell != sourcePos && !Level.passable[cell]) {
                if (bouncesLeft > 0) {
                    boolean testA = Level.passable[cell - stepA];
                    boolean testB = Level.passable[cell - stepB];

                    if (testA) {
                        stepA = -stepA;
                    } else if (testB) {
                        stepB = -stepB;
                        err = dA - err;
                    } else {
                        stepA = -stepA;
                        stepB = -stepB;
                        err = dA - err;
                    }

                    bounces.add(cell);
                    bounced = true;
                    bouncesLeft--;
                } else {
                    collide(cell);
                    break;
                }
            }

            path.add(cell);

            if (!bounced) {
                if ((stopTerrain && (!firstRun || cell != sourcePos) && Level.losBlocking[cell])
                        || ((!firstRun || cell != sourcePos) && stopChars && Actor.findChar(cell) != null)
                        || (cell == to && stopTarget)) {
                    collide(cell);
                    break;
                }
            }

            if (firstRun) {
                firstRun = false;
            }

            cell += stepA;

            err += dB;
            if (err >= dA) {
                err = err - dA;
                cell = cell + stepB;
            }
        }
    }

    //we only want to record the first position collision occurs at.
    private void collide(int cell) {
        if (collisionPos == null)
            collisionPos = cell;
    }

    //returns a segment of the path from start to end, inclusive.
    //if there is an error, returns an empty arraylist instead.
    public List<Integer> subPath(int start, int end) {
        try {
            end = Math.min(end, path.size() - 1);
            return path.subList(start, end + 1);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
