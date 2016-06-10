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
package com.felayga.unpixeldungeon.ui;

import com.watabou.noosa.Image;
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;

public enum Icons {
	SKULL,
	BUSY,
	COMPASS,
	INFO,
	PREFS,
	WARNING,
	TARGET,
	MASTERY,
	WATA,
	SHPX,
	UNPX,
	WARRIOR,
	MAGE,
	ROGUE,
	HUNTRESS,
	CLOSE,
	DEPTH,
	DEPTH_LG,
	SLEEP,
	ALERT,
	BACKPACK,
	SEED_POUCH,
	SCROLL_HOLDER,
	POTION_BANDOLIER,
	WAND_HOLSTER,
	CHECKED,
	UNCHECKED,
	EXIT,
	NOTES,
	CHALLENGE_OFF,
	CHALLENGE_ON,
	RESUME,
	SPELLBOOK,
	TREASURECHEST,
    FLOORHEAP,
	BUTTON_LEFT,
	BUTTON_RIGHT;

	public Image get() {
		return get( this );
	}
	
	public static Image get( Icons type ) {
		Image icon = new Image(Assets.ICONS);
		switch (type) {
			case SKULL:
				icon.frame(icon.texture.uvRect(0, 0, 8, 8));
				break;
			case BUSY:
				icon.frame(icon.texture.uvRect(8, 0, 16, 8));
				break;
			case COMPASS:
				icon.frame(icon.texture.uvRect(0, 8, 7, 13));
				break;
			case INFO:
				icon.frame(icon.texture.uvRect(16, 0, 30, 14));
				break;
			case PREFS:
				icon.frame(icon.texture.uvRect(30, 0, 46, 16));
				break;
			case WARNING:
				icon.frame(icon.texture.uvRect(46, 0, 58, 12));
				break;
			case TARGET:
				icon.frame(icon.texture.uvRect(0, 13, 16, 29));
				break;
			case MASTERY:
				icon.frame(icon.texture.uvRect(16, 14, 30, 28));
				break;
			case WATA:
				icon.frame(icon.texture.uvRect(30, 16, 45, 26));
				break;
			case SHPX:
				icon.frame(icon.texture.uvRect(64, 44, 80, 60));
				break;
			case UNPX:
				icon.frame(icon.texture.uvRect(64, 60, 80, 76));
				break;
			case WARRIOR:
				icon.frame(icon.texture.uvRect(0, 29, 16, 45));
				break;
			case MAGE:
				icon.frame(icon.texture.uvRect(16, 29, 32, 45));
				break;
			case ROGUE:
				icon.frame(icon.texture.uvRect(32, 29, 48, 45));
				break;
			case HUNTRESS:
				icon.frame(icon.texture.uvRect(48, 29, 64, 45));
				break;
			case CLOSE:
				icon.frame(icon.texture.uvRect(0, 45, 13, 58));
				break;
			case DEPTH:
				icon.frame(icon.texture.uvRect(45, 12, 54, 20));
				break;
			case DEPTH_LG:
				icon.frame(icon.texture.uvRect(34, 46, 50, 62));
				break;
			case SLEEP:
				icon.frame(icon.texture.uvRect(13, 45, 22, 53));
				break;
			case ALERT:
				icon.frame(icon.texture.uvRect(22, 45, 30, 53));
				break;
			case BACKPACK:
				icon.frame(icon.texture.uvRect(58, 0, 68, 10));
				break;
			case SCROLL_HOLDER:
				icon.frame(icon.texture.uvRect(68, 0, 78, 10));
				break;
			case SEED_POUCH:
				icon.frame(icon.texture.uvRect(78, 0, 88, 10));
				break;
			case WAND_HOLSTER:
				icon.frame(icon.texture.uvRect(88, 0, 98, 10));
				break;
			case POTION_BANDOLIER:
				icon.frame(icon.texture.uvRect(98, 0, 108, 10));
				break;
			case CHECKED:
				icon.frame(icon.texture.uvRect(54, 12, 66, 24));
				break;
			case UNCHECKED:
				icon.frame(icon.texture.uvRect(66, 12, 78, 24));
				break;
			case EXIT:
				icon.frame(icon.texture.uvRect(108, 0, 124, 16));
				break;
			case NOTES:
				icon.frame(icon.texture.uvRect(79, 40, 94, 56));
				break;
			case CHALLENGE_OFF:
				icon.frame(icon.texture.uvRect(78, 16, 102, 40));
				break;
			case CHALLENGE_ON:
				icon.frame(icon.texture.uvRect(102, 16, 126, 40));
				break;
			case RESUME:
				icon.frame(icon.texture.uvRect(13, 53, 24, 64));
				break;
			case SPELLBOOK:
				icon.frame(icon.texture.uvRect(93, 43, 103, 53));
				break;
			case TREASURECHEST:
				icon.frame(icon.texture.uvRect(103, 43, 113, 53));
				break;
            case FLOORHEAP:
                icon.frame(icon.texture.uvRect(113, 43, 123, 53));
                break;
			case BUTTON_LEFT:
				icon.frame(icon.texture.uvRect(0, 64, 12, 76));
				break;
			case BUTTON_RIGHT:
				icon.frame(icon.texture.uvRect(12, 64, 24, 76));
				break;
		}
		return icon;
	}
	
	public static Image get( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return get( WARRIOR );
		case MAGE:
			return get( MAGE );
		case ROGUE:
			return get( ROGUE );
		case HUNTRESS:
			return get( HUNTRESS );
		default:
			return null;
		}
	}
}
