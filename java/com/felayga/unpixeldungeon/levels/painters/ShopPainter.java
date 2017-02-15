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
package com.felayga.unpixeldungeon.levels.painters;

import com.felayga.unpixeldungeon.actors.mobs.npcs.ImpShopkeeper;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.levels.LastShopLevel;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

//import com.felayga.unpixeldungeon.items.Honeypot;

public class ShopPainter extends Painter {
	//private static int pasWidth;
	//private static int pasHeight;

    public static boolean canUse(Room room) {
        return room.width() >= 2 && room.height() >= 2 && room.connected != null && room.connected.size() == 1;
    }

	private static ArrayList<Item> itemsToSpawn;
	
	public static void paint( Level level, Room room ) {
        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.EMPTY_SP);

        HashSet<Integer> itempositions = new HashSet<Integer>();
        int shopkeeperPos = Constant.Position.NONE;

        for (int y = room.top + 1; y <= room.bottom - 1; y++) {
            for (int x = room.left + 1; x <= room.right - 1; x++) {
                itempositions.add(x + y * Level.WIDTH);
            }
        }

        Room.Door entrance = room.entrance();
        int doorPos = entrance.x + entrance.y * Level.WIDTH;
        int doorwayPos = doorPos;

        if (entrance.y == room.top) {
            doorwayPos = doorPos + Level.WIDTH;
        } else if (entrance.y == room.bottom) {
            doorwayPos = doorPos - Level.WIDTH;
        } else if (entrance.x == room.left) {
            doorwayPos = doorPos + 1;
        } else if (entrance.x == room.right) {
            doorwayPos = doorPos - 1;
        }

        if (itempositions.contains(doorwayPos)) {
            itempositions.remove(doorwayPos);
        }

        List<Integer> fidgetPos = new ArrayList<>();

        for (int pos : Level.NEIGHBOURS8) {
            int subpos = pos + doorwayPos;

            if (itempositions.contains(subpos)) {
                itempositions.remove(subpos);
                shopkeeperPos = subpos;
                fidgetPos.add(subpos);
            }
        }

        List<Integer> wallPos = new ArrayList<>();

        for (int y=room.top;y<=room.bottom;y++) {
            wallPos.add(room.left + y * Level.WIDTH);
            wallPos.add(room.right + y * Level.WIDTH);
        }

        for (int x=room.left;x<=room.right;x++) {
            wallPos.add(x + room.top * Level.WIDTH);
            wallPos.add(x + room.bottom * Level.WIDTH);
        }

        //jesus christ java, be more bad.. how about naming the other method "removeAt"?
        wallPos.remove(Integer.valueOf(doorPos));

        itemSpawnCheck(itempositions.size());

        Shopkeeper shopkeeper = level instanceof LastShopLevel ? new ImpShopkeeper() : new Shopkeeper();
        shopkeeper.pos(shopkeeperPos);

        shopkeeper.setRoom(doorwayPos, doorPos, wallPos, fidgetPos);

        level.mobs.add(shopkeeper);
        Shopkeeper.Registry.add(shopkeeper);

        for (int pos : itempositions) {
            Item item = itemsToSpawn.remove(itemsToSpawn.size() - 1);
            item.shopkeeper(shopkeeper);
            level.drop(item, pos);
        }

        /*
		pasWidth = room.width() - 2;
		pasHeight = room.height() - 2;

		int per = pasWidth * 2 + pasHeight * 2;
		int pos = xy2p( room, room.entrance() ) + (per - itemsToSpawn.size()) / 2;
		for (Item item : itemsToSpawn) {
			
			Point xy = p2xy( room, (pos + per) % per );
			int cell = xy.x + xy.y * Level.WIDTH;
			
			if (level.heaps.get( cell ) != null) {
				do {
					cell = room.random();
				} while (level.heaps.get( cell ) != null);
			}
			
			level.drop( item, cell ).type = Heap.Type.FOR_SALE;
			
			pos++;
		}

        placeShopkeeper(level, room);
        */

        if (room.connected != null) {
            for (Room.Door door : room.connected.values()) {
                door.set(Room.Door.Type.REGULAR_UNBROKEN);
            }
        }

        itemsToSpawn = null;
    }
	
	private static void itemSpawnCheck(int count) {
        if (itemsToSpawn==null) {
            itemsToSpawn = new ArrayList<Item>();
        }

        while (itemsToSpawn.size() < count) {
            itemsToSpawn.add(new ScrollOfMagicMapping().random());
        }

        //todo: update shop item spawns
        /*
		switch (Dungeon._depth) {
		case 6:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Quarterstaff() : new Spear()).identify() );
			itemsToSpawn.add(Random.Int(2) == 0 ?
                    new IncendiaryDart().quantity(Random.NormalIntRange(2, 4)) :
                    new CurareDart().quantity(Random.NormalIntRange(1, 3)));
			itemsToSpawn.add( new LeatherArmor().identify() );
			break;
			
		case 11:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Sword() : new Mace()).identify() );
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new CurareDart().quantity(Random.NormalIntRange(2, 5)) :
					new Shuriken().quantity(Random.NormalIntRange(3, 6)));
			itemsToSpawn.add( new MailArmor().identify() );
			break;
			
		case 16:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Longsword() : new BattleAxe()).identify() );
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new Shuriken().quantity(Random.NormalIntRange(4, 7)) :
					new Javelin().quantity(Random.NormalIntRange(3, 6)));
			itemsToSpawn.add( new ScaleArmor().identify() );
			break;
			
		case 21:
			itemsToSpawn.add( Random.Int( 2 ) == 0 ? new Glaive().identify() : new WarHammer().identify() );
			itemsToSpawn.add( Random.Int(2) == 0 ?
					new Javelin().quantity(Random.NormalIntRange(4, 7)) :
					new Tomahawk().quantity(Random.NormalIntRange(4, 7)));
			itemsToSpawn.add( new HalfPlateArmor().identify() );
			itemsToSpawn.add( new Torch() );
			itemsToSpawn.add( new Torch() );
			break;
		}

		itemsToSpawn.add( new MerchantsBeacon());

        itemsToSpawn.add( new PotionOfHealing() );
		for (int i=0; i < 3; i++)
			itemsToSpawn.add( Generator.random( Generator.Category.POTION ) );

		itemsToSpawn.add( new ScrollOfIdentify() );
		itemsToSpawn.add( new ScrollOfRemoveCurse() );
		itemsToSpawn.add( new ScrollOfMagicMapping() );
		itemsToSpawn.add( Generator.random( Generator.Category.SCROLL ) );

		for (int i=0; i < 2; i++)
			itemsToSpawn.add( Random.Int(2) == 0 ?
					Generator.random( Generator.Category.POTION ) :
					Generator.random( Generator.Category.SCROLL ) );


		itemsToSpawn.add( new OverpricedRation() );
		itemsToSpawn.add( new OverpricedRation() );

		itemsToSpawn.add( new Bomb().random() );
		switch (Random.Int(5)){
			case 1:
				itemsToSpawn.add( new Bomb() );
				break;
			case 2:
				itemsToSpawn.add( new Bomb().random() );
				break;
			case 3:
			case 4:
				//itemsToSpawn.add( new Honeypot() );
				break;
		}


		if (Dungeon._depth == 6) {
			itemsToSpawn.add( new Ankh() );
			itemsToSpawn.add( new Weightstone() );
		} else {
			itemsToSpawn.add(Random.Int(2) == 0 ? new Ankh() : new Weightstone());
		}


		TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
		if (hourglass != null){
			int bags = 0;
			//creates the given float percent of the remaining bags to be dropped.
			//this way players who get the hourglass late can still max it, usually.
			switch (Dungeon._depth) {
				case 6:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.20f ); break;
				case 11:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f ); break;
				case 16:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.50f ); break;
				case 21:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f ); break;
			}

			for(int i = 1; i <= bags; i++){
				itemsToSpawn.add( new TimekeepersHourglass.sandBag());
				hourglass.sandBags ++;
			}
		}

		Item rare;
		switch (Random.Int(10)){
			case 0:
				rare = Generator.random( Generator.Category.WAND );
				if (rare.level > 0 ) rare.upgrade(null, -rare.level);
				break;
			case 1:
				rare = Generator.random(Generator.Category.RING);
				rare.level = 1;
				break;
			case 2:
				rare = Generator.random( Generator.Category.ARTIFACT ).identify();
				break;
			default:
				rare = new Stylus();
		}
		if (Random.Int(4) == 0)
		{
			rare.bucStatus(BUCStatus.Blessed, false);
		}
		else {
			rare.bucStatus(BUCStatus.Uncursed, false);
		}

		itemsToSpawn.add( rare );

		//this is a hard limit, level gen allows for at most an 8x5 room, can't fit more than 39 items + 1 shopkeeper.
		if (itemsToSpawn.size() > 39)
			throw new RuntimeException("Shop attempted to carry more than 39 items!");
        */

		Collections.shuffle(itemsToSpawn);
	}

    /*
	public static int spaceNeeded(){
		if (itemsToSpawn == null)
			generateItems();

		//plus one for the shopkeeper
		return itemsToSpawn.size() + 1;
	}
	
	private static void placeShopkeeper( Level level, Room room ) {
		
		int pos;
		do {
			pos = room.random();
		} while (level.heaps.get( pos ) != null);
		
		Mob shopkeeper = level instanceof LastShopLevel ? new ImpShopkeeper() : new Shopkeeper();
		shopkeeper.pos = pos;
		level.mobs.add( shopkeeper );
		
		if (level instanceof LastShopLevel) {
			for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
				int p = shopkeeper.pos + Level.NEIGHBOURS9[i];
				if (level.map[p] == Terrain.EMPTY_SP) {
					level.map[p] = Terrain.WATER;
				}
			}
		}
	}

	private static int xy2p( Room room, Point xy ) {
		if (xy.y == room.top) {
			
			return (xy.x - room.left - 1);
			
		} else if (xy.x == room.right) {
			
			return (xy.y - room.top - 1) + pasWidth;
			
		} else if (xy.y == room.bottom) {
			
			return (room.right - xy.x - 1) + pasWidth + pasHeight;
			
		} else {
			
			if (xy.y == room.top + 1) {
				return 0;
			} else {
				return (room.bottom - xy.y - 1) + pasWidth * 2 + pasHeight;
			}
			
		}
	}
	
	private static Point p2xy( Room room, int p ) {
		if (p < pasWidth) {
			
			return new Point( room.left + 1 + p, room.top + 1);
			
		} else if (p < pasWidth + pasHeight) {
			
			return new Point( room.right - 1, room.top + 1 + (p - pasWidth) );
			
		} else if (p < pasWidth * 2 + pasHeight) {
			
			return new Point( room.right - 1 - (p - (pasWidth + pasHeight)), room.bottom - 1 );
			
		} else {
			
			return new Point( room.left + 1, room.bottom - 1 - (p - (pasWidth * 2 + pasHeight)) );
			
		}
	}
	*/
}
