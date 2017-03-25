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
package com.felayga.unpixeldungeon.actors.hero;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Boulder;
import com.felayga.unpixeldungeon.actors.mobs.npcs.NPC;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.tools.digging.DiggingTool;
import com.felayga.unpixeldungeon.items.tools.unlocking.UnlockingTool;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.IWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ammunition.AmmunitionWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ranged.RangedWeapon;
import com.felayga.unpixeldungeon.mechanics.Constant;


public class HeroAction {

    public int dst;

    public static class Move extends HeroAction {
        public Move(int dst) {
            this.dst = dst;
        }
    }

    public static class ReadSign extends HeroAction {
        public ReadSign(int dst) {
            this.dst = dst;
        }
    }

    public static class HandleHeap extends HeroAction {
        public HandleHeap(int dst) {
            this.dst = dst;
        }

        public static class OpenBag extends HandleHeap {
            public IBag bag;

            public OpenBag(int dst, IBag bag) {
                super(dst);

                this.bag = bag;
            }
        }

        public static class InteractItem extends HandleHeap {
            public Item item;

            public InteractItem(int dst, Item item) {
                super(dst);

                this.item = item;
            }
        }

        public static class PickUp extends InteractItem {
            public boolean forced;

            public PickUp(int dst, Item item) {
                this(dst, item, false);
            }

            public PickUp(int dst, Item item, boolean forced) {
                super(dst, item);

                this.forced = forced;
            }
        }
    }

    public static class OpenChest extends HeroAction {
        public OpenChest(int dst) {
            this.dst = dst;
        }
    }

    public static class Buy extends HeroAction {
        public Buy(int dst) {
            this.dst = dst;
        }
    }

    public static class Interact extends HeroAction {
        public NPC npc;

        public Interact(NPC npc) {
            this.npc = npc;
        }
    }

    public static class HandleDoor extends HeroAction {
        public HandleDoor(int door) {
            this.dst = door;
        }

        boolean successful = true;

        public static class OpenCloseDoor extends HandleDoor {
            public OpenCloseDoor(int door) {
                super(door);
            }
        }

        public static class KickDoor extends HandleDoor {
            public EquippableItem boots;

            public KickDoor(int door, EquippableItem boots) {
                super(door);

                this.boots = boots;
            }
        }

        public static class UnlockDoor extends HandleDoor {
            UnlockingTool tool;

            public UnlockDoor(int door, UnlockingTool tool) {
                super(door);
                this.tool = tool;
            }
        }
    }

    public static class MoveLevel extends HeroAction {
        public int direction;
        public boolean alternate;

        public MoveLevel(int pos, int direction, boolean alternate) {
            this.dst = pos;
            this.direction = direction;
            this.alternate = alternate;
        }
    }

    public static class InteractPosition extends HeroAction {
        public static class Cook extends InteractPosition {
            public Cook(int pot) {
                this.dst = pot;
            }
        }

        public static class Well extends InteractPosition {
            public String action;

            public Well(int pos) {
                this(pos, null);
            }

            public Well(int pos, String action) {
                this.dst = pos;
                this.action = action;
            }
        }
    }

    public static class Attack extends HeroAction {
        public Char target;
        public int targetPos;

        public IWeapon melee;
        public RangedWeapon launcher;
        public AmmunitionWeapon ammo;
        public MissileWeapon missile;

        public Attack(Char target) {
            this.target = target;
        }

        public Attack(int targetPos) {
            this.targetPos = targetPos;
        }

        public Attack(Char target, IWeapon melee) {
            this.target = target;
            this.melee = melee;
        }
    }

    public static class UseItem extends HeroAction {
        public Item target;
        public String action;

        public UseItem(Item target, String action) {
            this.target = target;
            this.action = action;
        }

        public static class UnlockBag extends UseItem {
            public UnlockingTool tool;
            public int location;

            public UnlockBag(UnlockingTool tool, Item target, int location) {
                super(target, null);

                this.tool = tool;
                this.location = location;
            }
        }

        public static class EatItem extends UseItem {
            public IBag targetOutsideInventory;
            public boolean startedEating;
            public boolean stoppedEating;
            public boolean forced;

            public EatItem(Item target, String action) {
                this(target, action, false);
            }

            public EatItem(Item target, String action, boolean forced) {
                super(target, action);

                this.forced = forced;

                targetOutsideInventory = null;
                startedEating = false;
                stoppedEating = false;
            }
        }

        public static class SlowAction extends UseItem {
            public long time;

            public SlowAction(Item target, String action, long time) {
                super(target, action);

                this.time = time;
            }
        }

        public static class Kick extends UseItem {
            public Char enemy;

            public Kick(Item item, int target) {
                super(item, Constant.Action.KICK);

                this.dst = target;
            }
        }
    }


    public static class Dig extends HeroAction {
        public DiggingTool tool;
        public int pos;
        public int effort;

        public Boulder boulder;
        public int direction;

        public Dig(DiggingTool tool, int pos, int effort) {
            this(tool, pos, effort, 0, null);
        }

        public Dig(DiggingTool tool, Boulder boulder, int effort) {
            this(tool, boulder.pos(), effort, 0, boulder);
        }

        public Dig(DiggingTool tool, int pos, int effort, int direction) {
            this(tool, pos, effort, direction, null);
        }

        public Dig(DiggingTool tool, int pos, int effort, int direction, Boulder boulder) {
            this.tool = tool;
            this.pos = pos;
            this.effort = effort;
            this.direction = direction;
            this.boulder = boulder;
        }
    }
}
