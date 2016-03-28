/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
 * Copyright (C) 2015 Randall Foudray
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
 */
package com.felayga.unpixeldungeon.actors.hero;

import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.mobs.npcs.NPC;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.tools.digging.DiggingTool;
import com.felayga.unpixeldungeon.items.tools.unlocking.UnlockingTool;


public class HeroAction {
	
	public int dst;
	
	public static class Move extends HeroAction {
		public Move( int dst ) {
			this.dst = dst;
		}
	}
	
	public static class PickUp extends HeroAction {
		public PickUp( int dst ) {
			this.dst = dst;
		}
	}

	public static class OpenBag extends HeroAction {
		public OpenBag(int dst) {
			this.dst = dst;
		}
	}

	public static class OpenChest extends HeroAction {
		public OpenChest( int dst ) {
			this.dst = dst;
		}
	}
	
	public static class Buy extends HeroAction {
		public Buy( int dst ) {
			this.dst = dst;
		}
	}
	
	public static class Interact extends HeroAction {
		public NPC npc;
		public Interact( NPC npc ) {
			this.npc = npc;
		}
	}
	
	public static class HandleDoor extends HeroAction {
		public HandleDoor( int door ) {
			this.dst = door;
		}
		boolean successful = true;

		public static class OpenCloseDoor extends HandleDoor {
			public OpenCloseDoor(int door) { super(door); }
		}

		public static class KickDoor extends HandleDoor {
			public KickDoor(int door) { super(door); }
		}

		public static class UnlockDoor extends HandleDoor {
			UnlockingTool tool;
			public UnlockDoor(int door, UnlockingTool tool) {
				super(door);
				this.tool = tool;
			}
		}
	}
	
	public static class Descend extends HeroAction {
		public Descend( int stairs ) {
			this.dst = stairs;
		}
	}
	
	public static class Ascend extends HeroAction {
		public Ascend( int stairs ) {
			this.dst = stairs;
		}
	}
	
	public static class Cook extends HeroAction {
		public Cook( int pot ) {
			this.dst = pot;
		}
	}
	
	public static class Attack extends HeroAction {
		public Char target;
		public KindOfWeapon weapon;
		public boolean weaponThrown;
		public Attack( KindOfWeapon weapon, boolean thrown, Char target )
		{
			this.target = target;
			this.weapon = weapon;
			this.weaponThrown = thrown;
		}
	}

	public static class UseItem extends HeroAction {
		public Item target;
		public String action;

		public UseItem(Item target, String action)
		{
			this.target = target;
			this.action = action;
		}
	}

	public static class UnlockBag extends HeroAction {
		public UnlockingTool tool;
		public Item target;
		public int location;

		public UnlockBag(UnlockingTool tool, Item target, int location) {
			this.tool = tool;
			this.target = target;
			this.location = location;
		}
	}

	public static class EatItem extends UseItem{
		public boolean targetOutsideInventory;
		public boolean startedEating;
		public boolean stoppedEating;
		public boolean forced;

		public EatItem(Item target, String action) {
			this(target, action, false);
		}

		public EatItem(Item target, String action, boolean forced) {
			super(target, action);

			this.forced = forced;

			targetOutsideInventory = false;
			startedEating = false;
			stoppedEating = false;
		}
	}

	public static class Dig extends HeroAction {
		public DiggingTool tool;
		public int pos;
		public int effort;

		public Dig(DiggingTool tool, int pos, int effort) {
			this.tool = tool;
			this.pos = pos;
			this.effort = effort;
		}
	}
}
