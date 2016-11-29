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
package com.felayga.unpixeldungeon;

import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.windows.hero.WndInitHero;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.HashMap;

public class GamesInProgress {
	public static final int MAXIMUM = 4;

	private static HashMap<Integer, Info> state = new HashMap<Integer, Info>();
	
	public static Info check( int index ) {
		if (state.containsKey( index )) {
			return state.get( index );
		} else {
			
			Info info;
			try {
				Bundle bundle = Dungeon.gameBundle( Dungeon.gameFile( index ) );
				info = new Info();
				info.restoreFromBundle(bundle);
			} catch (Exception e) {
				info = null;
			}
			
			state.put( index, info );
			return info;
			
		}
	}

	public static void set( int index, HeroClass heroClass, int level, int depth ) {
		Info info = new Info();
		info.heroClass = HeroClass.toInt(heroClass);
		info.gender = WndInitHero.genderSelected;
		info.hair = WndInitHero.hairSelected;
		info.hairFace = WndInitHero.hairFaceSelected;
		info.hairColor = WndInitHero.hairColorSelected;
		info.level = level;
		info.depth = depth;
		info.dead = false;
		state.put( index, info );
	}
	
	public static void setUnknown( int index ) {
		Info test = state.get(index);
		if (test != null) {
			test.dead = true;
		}
	}
	
	public static void delete( int index ) {
		Info test = state.get(index);
		if (test != null) {
			test.dead = true;
		}
	}
	
	public static class Info implements Bundlable {
		public int heroClass;
		public int gender;
		public int hair;
		public int hairFace;
		public int hairColor;

		public int level;
		public int depth;
		public boolean dead;

		public void toWndHeroInit()
		{
			WndInitHero.heroClassSelected = heroClass;
			WndInitHero.genderSelected = gender;
			WndInitHero.hairSelected = hair;
			WndInitHero.hairFaceSelected = hairFace;
			WndInitHero.hairColorSelected = hairColor;
		}

		public void fromWndHeroInit() {
			heroClass = WndInitHero.heroClassSelected;
			gender = WndInitHero.genderSelected;
			hair = WndInitHero.hairSelected;
			hairFace = WndInitHero.hairFaceSelected;
			hairColor = WndInitHero.hairColorSelected;
		}

		private static final String CLASS		= "hero_class";
		private static final String GENDER		= "hero_gender";
		private static final String HAIR		= "hero_hair";
		private static final String HAIRFACE	= "hero_hairFace";
		private static final String HAIRCOLOR	= "hero_hairColor";
		private static final String DEAD		= "hero_dead";

		public void storeInBundle( Bundle bundle ) {
			bundle.put(CLASS, heroClass);
			bundle.put(GENDER, gender);
			bundle.put(HAIR, hair);
			bundle.put(HAIRFACE, hairFace);
			bundle.put(HAIRCOLOR, hairColor);

            bundle.put(DEAD, dead);
		}

		public void restoreFromBundle( Bundle bundle ) {
			heroClass = bundle.getInt( CLASS );
			gender = bundle.getInt( GENDER );
			hair = bundle.getInt( HAIR );
			hairFace = bundle.getInt( HAIRFACE );
			hairColor = bundle.getInt( HAIRCOLOR );

			Hero.preview(this, bundle.getBundle(Dungeon.HERO));
			depth = bundle.getInt( Dungeon.DEPTH );
			dead = bundle.getBoolean( DEAD );

			if (depth == -1) {
				depth = bundle.getInt( "maxDepth" );
			}
		}
	}
}
