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
package com.felayga.unpixeldungeon.actors.mobs.npcs;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Journal;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.EquippableItem;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.quest.DarkGold;
import com.felayga.unpixeldungeon.items.quest.OldPickaxe;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.felayga.unpixeldungeon.levels.Room;
import com.felayga.unpixeldungeon.levels.Room.Type;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.npcs.BlacksmithSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.quest.WndBlacksmith;
import com.felayga.unpixeldungeon.windows.quest.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Collection;

public class Blacksmith extends NPC {

	private static final String TXT_GOLD_1 =
		"Hey human! Wanna be useful, eh? Take dis pickaxe and mine me some _dark gold ore_, _15 pieces_ should be enough. " +
		"What do you mean, how am I gonna pay? You greedy...\n" +
		"Ok, ok, I don't have money to pay, but I can do some smithin' for you. Consider yourself lucky, " +
		"I'm the only blacksmith around.";
	private static final String TXT_BLOOD_1 =
		"Hey human! Wanna be useful, eh? Take dis pickaxe and _kill a bat_ wit' it, I need its blood on the head. " +
		"What do you mean, how am I gonna pay? You greedy...\n" +
		"Ok, ok, I don't have money to pay, but I can do some smithin' for you. Consider yourself lucky, " +
		"I'm the only blacksmith around.";
	private static final String TXT2 =
		"Are you kiddin' me? Where is my pickaxe?!";
	private static final String TXT3 =
		"Dark gold ore. 15 pieces. Seriously, is it dat hard?";
	private static final String TXT4 =
		"I said I need bat blood on the pickaxe. Chop chop!";
	private static final String TXT_COMPLETED =
		"Oh, you have returned... Better late dan never.";
	private static final String TXT_GET_LOST =
		"I'm busy. Get lost!";
	
	private static final String TXT_LOOKS_BETTER	= "your %s certainly looks better now";

	public Blacksmith()
	{
		super(10);

		name = "troll blacksmith";
		spriteClass = BlacksmithSprite.class;
	}
	
	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo(pos, Dungeon.hero.pos);
		
		if (!Quest.given) {
			
			GameScene.show( new WndQuest( this,
				Quest.alternative ? TXT_BLOOD_1 : TXT_GOLD_1 ) {
				
				@Override
				public void onBackPressed() {
					super.onBackPressed();
					
					Quest.given = true;
					Quest.completed = false;
					
					OldPickaxe pick = new OldPickaxe();
					if (pick.doPickUp( Dungeon.hero )) {
						GLog.i( Hero.TXT_YOU_NOW_HAVE, pick.getDisplayName() );
					} else {
						Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
					}
				};
			} );
			
			Journal.add( Journal.Feature.TROLL );
			
		} else if (!Quest.completed) {
			if (Quest.alternative) {
				
				OldPickaxe pick = Dungeon.hero.belongings.getItem( OldPickaxe.class );
				if (pick == null) {
					tell( TXT2 );
				} else if (!pick.bloodStained) {
					tell( TXT4 );
				} else {
					if (pick.isEquipped( Dungeon.hero )) {
						Dungeon.hero.belongings.unequip(pick, false);
					}
					Dungeon.hero.belongings.remove(pick, 1);
					tell( TXT_COMPLETED );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			} else {
				
				OldPickaxe pick = Dungeon.hero.belongings.getItem( OldPickaxe.class );
				DarkGold gold = Dungeon.hero.belongings.getItem( DarkGold.class );
				if (pick == null) {
					tell( TXT2 );
				} else if (gold == null || gold.quantity() < 15) {
					tell( TXT3 );
				} else {
					if (pick.isEquipped( Dungeon.hero )) {
						Dungeon.hero.belongings.unequip(pick, false);
					}
					Dungeon.hero.belongings.remove(pick, 1);
					Dungeon.hero.belongings.remove(gold);
					tell( TXT_COMPLETED );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			}
		} else if (!Quest.reforged) {
			
			GameScene.show( new WndBlacksmith( this, Dungeon.hero ) );
			
		} else {
			
			tell( TXT_GET_LOST );
			
		}
	}
	
	private void tell( String text ) {
		GameScene.show( new WndQuest( this, text ) );
	}
	
	public static String verify( Item item1, Item item2 ) {
		
		if (item1 == item2) {
			return "Select 2 different items, not the same item twice!";
		}

		if (item1.getClass() != item2.getClass()) {
			return "Select 2 items of the same type!";
		}
		
		if (!item1.isIdentified() || !item2.isIdentified()) {
			return "I need to know what I'm working with, identify them first!";
		}
		
		if (item1.bucStatus() == BUCStatus.Cursed || item2.bucStatus() == BUCStatus.Cursed) {
			return "I don't work with cursed items!";
		}
		
		if (item1.level < 0 || item2.level < 0) {
			return "It's a junk, the quality is too poor!";
		}
		
		if (!item1.isUpgradable() || !item2.isUpgradable()) {
			return "I can't reforge these items!";
		}
		
		return null;
	}
	
	public static void upgrade( Item item1, Item item2 ) {
		
		Item first, second;
		if (item2.level > item1.level) {
			first = item2;
			second = item1;
		} else {
			first = item1;
			second = item2;
		}

		Sample.INSTANCE.play(Assets.SND_EVOKE);
		ScrollOfUpgrade.upgrade(Dungeon.hero);
		Item.evoke(Dungeon.hero);
		
		if (first.isEquipped( Dungeon.hero )) {
			Dungeon.hero.belongings.unequip((EquippableItem) first, true);
		}
		first.upgrade(null, 1);
		GLog.p(TXT_LOOKS_BETTER, first.getDisplayName());
		Dungeon.hero.spend_new(GameTime.TICK * 2, true);
		Badges.validateItemLevelAquired(first);
		
		if (second.isEquipped( Dungeon.hero )) {
			Dungeon.hero.belongings.unequip((EquippableItem) second, false);
		}
		Dungeon.hero.belongings.remove(second);

		Quest.reforged = true;
		
		Journal.remove( Journal.Feature.TROLL );
	}
	
	@Override
	public int damage( int dmg, MagicType type, Actor source) {
        return 0;
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public String description() {
		return
			"This troll blacksmith looks like all trolls look: he is tall and lean, and his skin resembles stone " +
			"in both color and texture. The troll blacksmith is tinkering with unproportionally small tools.";
	}

	public static class Quest {
		
		private static boolean spawned;
		
		private static boolean alternative;
		private static boolean given;
		private static boolean completed;
		private static boolean reforged;
		
		public static void reset() {
			spawned		= false;
			given		= false;
			completed	= false;
			reforged	= false;
		}
		
		private static final String NODE	= "blacksmith";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REFORGED	= "reforged";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REFORGED, reforged );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	=  node.getBoolean( ALTERNATIVE );
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reforged = node.getBoolean( REFORGED );
			} else {
				reset();
			}
		}
		
		public static boolean spawn( Collection<Room> rooms ) {
			if (!spawned && Dungeon.depthAdjusted > 11 && Random.Int( 15 - Dungeon.depthAdjusted ) == 0) {
				
				Room blacksmith = null;
				for (Room r : rooms) {
					if (r.type == Type.STANDARD && r.width() > 4 && r.height() > 4) {
						blacksmith = r;
						blacksmith.type = Type.BLACKSMITH;
						
						spawned = true;
						alternative = Random.Int( 2 ) == 0;
						
						given = false;
						
						break;
					}
				}
			}
			return spawned;
		}
	}
}
