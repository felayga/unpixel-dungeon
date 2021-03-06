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
package com.felayga.unpixeldungeon.levels;

import com.felayga.unpixeldungeon.levels.painters.AltarPainter;
import com.felayga.unpixeldungeon.levels.painters.ArmoryPainter;
import com.felayga.unpixeldungeon.levels.painters.BlacksmithPainter;
import com.felayga.unpixeldungeon.levels.painters.BossExitPainter;
import com.felayga.unpixeldungeon.levels.painters.CryptPainter;
import com.felayga.unpixeldungeon.levels.painters.EntranceAlternatePainter;
import com.felayga.unpixeldungeon.levels.painters.EntrancePainter;
import com.felayga.unpixeldungeon.levels.painters.ExitAlternatePainter;
import com.felayga.unpixeldungeon.levels.painters.ExitPainter;
import com.felayga.unpixeldungeon.levels.painters.GardenPainter;
import com.felayga.unpixeldungeon.levels.painters.LaboratoryPainter;
import com.felayga.unpixeldungeon.levels.painters.MagicWellPainter;
import com.felayga.unpixeldungeon.levels.painters.Painter;
import com.felayga.unpixeldungeon.levels.painters.PassagePainter;
import com.felayga.unpixeldungeon.levels.painters.PitPainter;
import com.felayga.unpixeldungeon.levels.painters.PlainPainter;
import com.felayga.unpixeldungeon.levels.painters.RatKingPainter;
import com.felayga.unpixeldungeon.levels.painters.RitualSitePainter;
import com.felayga.unpixeldungeon.levels.painters.ShopPainter;
import com.felayga.unpixeldungeon.levels.painters.StandardPainter;
import com.felayga.unpixeldungeon.levels.painters.StoragePainter;
import com.felayga.unpixeldungeon.levels.painters.TrapsPainter;
import com.felayga.unpixeldungeon.levels.painters.TreasuryPainter;
import com.felayga.unpixeldungeon.levels.painters.TunnelPainter;
import com.felayga.unpixeldungeon.levels.painters.VaultPainter;
import com.felayga.unpixeldungeon.levels.painters.WeakFloorPainter;
import com.felayga.unpixeldungeon.unPixelDungeon;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Room extends Rect implements Graph.Node, Bundlable {
	
	public HashSet<Room> neigbours = new HashSet<>();
	public HashMap<Room, Door> connected = new HashMap<>();
	
	public int distance;
	public int price = 1;
	
	public enum Type {
        NULL(null),
        STANDARD(StandardPainter.class),
        ENTRANCE(EntrancePainter.class),
        EXIT(ExitPainter.class),
        ENTRANCE_ALTERNATE(EntranceAlternatePainter.class),
        EXIT_ALTERNATE(ExitAlternatePainter.class),
        BOSS_EXIT(BossExitPainter.class),
        TUNNEL(TunnelPainter.class),
        PASSAGE(PassagePainter.class),
        SHOP(ShopPainter.class),
        BLACKSMITH(BlacksmithPainter.class),
        TREASURY(TreasuryPainter.class),
        ARMORY(ArmoryPainter.class),
        //LIBRARY		    (LibraryPainter.class),
        LABORATORY(LaboratoryPainter.class),
        VAULT(VaultPainter.class),
        TRAPS(TrapsPainter.class),
        STORAGE(StoragePainter.class),
        MAGIC_WELL(MagicWellPainter.class),
        GARDEN(GardenPainter.class),
        CRYPT(CryptPainter.class),
        //STATUE		    (StatuePainter.class),
        //POOL		        (PoolPainter.class),
        RAT_KING	        (RatKingPainter.class),
        WEAK_FLOOR(WeakFloorPainter.class),
        PIT(PitPainter.class),

        //prison quests
        //MASS_GRAVE        (MassGravePainter.class),
        //ROT_GARDEN        (RotGardenPainter.class),
        RITUAL_SITE(RitualSitePainter.class),
        ALTAR(AltarPainter.class),
        PLAIN(PlainPainter.class);

        private Method paint;
        private Method canUse;

        Type(Class<? extends Painter> painter) {
            if (painter != null) {
                GLog.d("init " + painter.toString());
            } else {
                GLog.d("init null");
            }

            this.paint = null;
            this.canUse = null;

            if (painter != null) {
                Method[] methods = painter.getMethods();

                for (int n = 0; n < methods.length; n++) {
                    Method method = methods[n];
                    String name = method.getName();
                    Class<?>[] types;

                    switch (name) {
                        case "paint":
                            types = method.getParameterTypes();
                            if (types.length == 2 && types[0] == Level.class && types[1] == Room.class) {
                                this.paint = method;
                            }
                            break;
                        case "canUse":
                            types = method.getParameterTypes();
                            if (types.length == 1 && types[0] == Room.class) {
                                this.canUse = method;
                            }
                            break;
                    }
                }
            }
        }

        public void paint(Level level, Room room) {
            try {
                paint.invoke(null, level, room);
            } catch (Exception e) {
                unPixelDungeon.reportException(e);
            }
        }

        public boolean canUse(Room room) {
            if (canUse != null) {
                try {
                    Object retval = canUse.invoke(null, room);
                    return (boolean)retval;
                } catch (Exception e) {
                    GLog.d(e);
                }
            }

            return room.connected.size() > 0;
        }
    }
	
	public static final ArrayList<Type> SPECIALS = new ArrayList<>( Arrays.asList(
		/*Type.WEAK_FLOOR, Type.MAGIC_WELL, Type.CRYPT, Type.GARDEN, Type.ARMORY,
		Type.TREASURY, */Type.TRAPS/*, Type.STORAGE, Type.LABORATORY, Type.VAULT*/
            //Type.POOL, Type.LIBRARY, Type.STATUE
	) );
	
	public Type type = Type.NULL;

	public Room()
	{
		super();
	}
	
	public int random() {
		return random( 0 );
	}
	
	public int random( int m ) {
		int x = Random.Int( left + 1 + m, right - m );
		int y = Random.Int( top + 1 + m, bottom - m );
		return x + y * Level.WIDTH;
	}
	
	public void addNeigbour( Room other ) {
		
		Rect i = intersect( other );
		if ((i.width() == 0 && i.height() >= 3) ||
			(i.height() == 0 && i.width() >= 3)) {
			neigbours.add( other );
			other.neigbours.add( this );
		}
		
	}
	
	public void connect( Room room ) {
		if (!connected.containsKey( room )) {
			connected.put( room, null );
			room.connected.put( this, null );
		}
	}
	
	public Door entrance() {
		return connected.values().iterator().next();
	}
	
	public boolean inside( int p ) {
		int x = p % Level.WIDTH;
		int y = p / Level.WIDTH;
		return x > left && y > top && x < right && y < bottom;
	}
	
	public Point center() {
		return new Point(
			(left + right) / 2 + (((right - left) & 1) == 1 ? Random.Int( 2 ) : 0),
			(top + bottom) / 2 + (((bottom - top) & 1) == 1 ? Random.Int( 2 ) : 0) );
	}

	public HashSet<Integer> getCells(){
		HashSet<Point> points = getPoints();
		HashSet<Integer> cells = new HashSet<>();
		for( Point point : points)
			cells.add(point.x + point.y*Level.WIDTH);
		return cells;
	}
	
	// **** Graph.Node interface ****

	@Override
	public int distance() {
		return distance;
	}

	@Override
	public void distance( int value ) {
		distance = value;
	}
	
	@Override
	public int price() {
		return price;
	}

	@Override
	public void price( int value ) {
		price = value;
	}

	@Override
	public Collection<Room> edges() {
		return neigbours;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( "left", left );
		bundle.put( "top", top );
		bundle.put( "right", right );
		bundle.put( "bottom", bottom );
		bundle.put( "type", type.toString() );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		left = bundle.getInt( "left" );
		top = bundle.getInt( "top" );
		right = bundle.getInt( "right" );
		bottom = bundle.getInt( "bottom" );
		type = Type.valueOf( bundle.getString( "type" ) );
	}
	
	public static void shuffleTypes() {
		int size = SPECIALS.size();
		for (int i=0; i < size - 1; i++) {
			int j = Random.Int( i, size );
			if (j != i) {
				Type t = SPECIALS.get( i );
				SPECIALS.set( i, SPECIALS.get( j ) );
				SPECIALS.set( j, t );
			}
		}
	}
	
	public static void useType( Type type ) {
		if (SPECIALS.remove( type )) {
			SPECIALS.add( type );
		}
	}
	
	private static final String ROOMS	= "rooms";
	
	public static void restoreRoomsFromBundle( Bundle bundle ) {
		if (bundle.contains( ROOMS )) {
			SPECIALS.clear();
			for (String type : bundle.getStringArray( ROOMS )) {
				SPECIALS.add( Type.valueOf( type ));
			}
		} else {
			shuffleTypes();
		}
	}
	
	public static void storeRoomsInBundle( Bundle bundle ) {
		String[] array = new String[SPECIALS.size()];
		for (int i=0; i < array.length; i++) {
			array[i] = SPECIALS.get( i ).toString();
		}
		bundle.put( ROOMS, array );
	}
	
	public static class Door extends Point {
		
		public enum Type {
			EMPTY, TUNNEL, REGULAR, REGULAR_UNBROKEN, UNLOCKED, BARRICADE, LOCKED, NICHE, JAIL, SECRET
		}
		public Type type = Type.EMPTY;
		
		public Door( int x, int y ) {
			super( x, y );
		}
		
		public void set( Type type ) {
			if (type.compareTo( this.type ) > 0) {
				this.type = type;
			}
		}
	}
}