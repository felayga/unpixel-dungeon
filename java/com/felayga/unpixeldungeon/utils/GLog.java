/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */
package com.felayga.unpixeldungeon.utils;

import android.util.Log;

import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.watabou.utils.Signal;

public class GLog {

	public static final String TAG = "GAME";
	
	public static final String POSITIVE		= "++ ";
	public static final String NEGATIVE		= "-- ";
	public static final String WARNING		= "** ";
	public static final String HIGHLIGHT	= "@@ ";
	
	public static Signal<String> update = new Signal<>();
	
	public static void i( String text, Object... args ) {
		
		if (args.length > 0) {
			text = Utils.format( text, args );
		}
		
		Log.i( TAG, text );
		update.dispatch( text );
	}

	public static void d( String text, Object... args ) {
        i(NEGATIVE + text, args);
    }
    public static void d( Throwable throwable) { d(Log.getStackTraceString(throwable)); }
    public static void d( int pos, int relativePos ) {
        int x = pos % Level.WIDTH;
        int y = pos / Level.WIDTH;

        if (relativePos != Constant.Position.NONE) {
            int rx = relativePos % Level.WIDTH;
            int ry = relativePos / Level.WIDTH;

            d("pos=" + x + "," + y + " relative=" + (rx - x) + "," + (ry - y));
        }
        else {
            d("pos=" + x + "," + y);
        }
    }
	
	public static void p( String text, Object... args ) {
		i( POSITIVE + text, args );
	}
	
	public static void n( String text, Object... args ) {
		i( NEGATIVE + text, args );
	}
	
	public static void w( String text, Object... args ) {
		i( WARNING + text, args );
	}
	
	public static void h( String text, Object... args ) {
		i( HIGHLIGHT + text, args );
	}
}
