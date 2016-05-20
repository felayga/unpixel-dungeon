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
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Gold;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.artifacts.Artifact_old;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Bones {

	private static final String BONES_FILE	= "bones.dat";
	
	private static final String LEVEL	= "level";
	private static final String ITEM	= "item";
	
	private static int depth = -1;
	private static Item item;
	
	public static void leave() {

		depth = Dungeon.depth;

		//heroes which have won the game, who die far above their farthest depth, or who are challenged drop no bones.
		if (Statistics.amuletObtained || /*(Statistics.deepestFloor - 5) >= depth ||*/ Dungeon.challenges > 0) {
			depth = -1;
			return;
		}

		item = pickItem(Dungeon.hero);

		Bundle bundle = new Bundle();
		bundle.put( LEVEL, depth );
		bundle.put( ITEM, item );

		try {
			OutputStream output = Game.instance.openFileOutput( BONES_FILE, Game.MODE_PRIVATE );
			Bundle.write( bundle, output );
			output.close();
		} catch (IOException e) {

		}
	}

	private static Item pickItem(Hero hero){
		Item item = null;
		if (Random.Int(2) == 0) {
			switch (Random.Int(13)) {
				case 0:
					item = hero.belongings.weapon;
					break;
				case 1:
					item = hero.belongings.offhand;
					break;
				case 2:
					item = hero.belongings.weapon2;
					break;
				case 3:
					item = hero.belongings.offhand2;
					break;
                case 4:
                    item = hero.belongings.armor;
                    break;
                case 5:
                    item = hero.belongings.gloves;
                    break;
                case 6:
                    item = hero.belongings.boots;
                    break;
                case 7:
                    item = hero.belongings.cloak;
                    break;
                case 8:
                    item = hero.belongings.ring1;
                    break;
                case 9:
                    item = hero.belongings.ring2;
                    break;
                case 10:
                    item = hero.belongings.amulet;
                    break;
                case 11:
                    item = hero.belongings.face;
                    break;
				default:
					item = Dungeon.quickslot.randomNonePlaceholder();
					break;
			}
			if (item != null && !item.bones)
				return pickItem(hero);
		} else {

			Iterator<Item> iterator = hero.belongings.iterator();
			Item curItem;
			ArrayList<Item> items = new ArrayList<Item>();
			while (iterator.hasNext()){
				curItem = iterator.next();
				if (curItem.bones)
					items.add(curItem);
			}

			if (Random.Int(3) < items.size()) {
				item = Random.element(items);
				if (item.stackable){
					if (item instanceof MissileWeapon){
						item.quantity(Random.NormalIntRange(1, item.quantity()));
					} else {
						item.quantity(Random.NormalIntRange(1, (item.quantity() + 1) / 2));
					}
				}
			}
		}
		if (item == null) {
			item = new Gold(Random.Int(50, 250));
			/*
			if (Dungeon.gold > 50) {
				item = new Gold( Random.NormalIntRange( 50, Dungeon.gold ) );
			} else {
				item = new Gold( 50 );
			}
			*/
		}
		return item;
	}

	public static Item get() {
		if (depth == -1) {

			try {
				InputStream input = Game.instance.openFileInput( BONES_FILE ) ;
				Bundle bundle = Bundle.read( input );
				input.close();

				depth = bundle.getInt( LEVEL );
				item = (Item)bundle.get( ITEM );

				return get();

			} catch (Exception e) {
				return null;
			}

		} else {
			//heroes who are challenged cannot find bones
			if (depth == Dungeon.depth && Dungeon.challenges == 0) {
				Game.instance.deleteFile( BONES_FILE );
				depth = 0;

				if (item instanceof Artifact_old){
					if (Generator.removeArtifact((Artifact_old)item)) {
						try {
							Artifact_old artifactOld = (Artifact_old)item.getClass().newInstance();
							artifactOld.bucStatus(BUCStatus.Cursed, false);
							//caps displayed artifactOld level
							artifactOld.transferUpgrade(Math.min(
									item.visiblyUpgraded(),
									1 + ((Dungeon.depth * 3) / 10)));

							return item;
						} catch (Exception e) {
							return new Gold(item.price());
						}
					} else {
						return new Gold(item.price());
					}
				}
				
				if (item.isUpgradable()) {
					item.bucStatus(BUCStatus.Cursed, true);
					if (item.isUpgradable()) {
						//gain 1 level every 3.333 floors down plus one additional level.
						int lvl = 1 + ((Dungeon.depth * 3) / 10);
						if (lvl < item.level) {
							item.upgrade( null, -(item.level - lvl) );
						}
						item.levelKnown = false;
					}
				}
				
				item.syncVisuals();
				
				return item;
			} else {
				return null;
			}
		}
	}
}
