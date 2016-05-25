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
package com.felayga.unpixeldungeon.actors.mobs.npcs.unused;
/*
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Journal;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.StenchGas;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Paralysis;
import com.felayga.unpixeldungeon.actors.buffs.Roots;
import com.felayga.unpixeldungeon.actors.mobs.Crab;
import com.felayga.unpixeldungeon.actors.mobs.Gnoll;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.actors.mobs.unused.MarsupialRat;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.KindOfWeapon;
import com.felayga.unpixeldungeon.items.armor.Armor;
import com.felayga.unpixeldungeon.items.food.MysteryMeat;
import com.felayga.unpixeldungeon.items.wands.Wand;
import com.felayga.unpixeldungeon.items.weapon.Weapon;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.AcidChance;
import com.felayga.unpixeldungeon.items.weapon.melee.mob.ComboChance;
import com.felayga.unpixeldungeon.items.weapon.missiles.CurareDart;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.SewerLevel;
import com.felayga.unpixeldungeon.levels.traps.LightningTrap;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.mobs.rat.RabidRatSprite;
import com.felayga.unpixeldungeon.sprites.npcs.GhostSprite;
import com.felayga.unpixeldungeon.sprites.mobs.unused.GnollTricksterSprite;
import com.felayga.unpixeldungeon.sprites.mobs.GreatCrabSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.quest.WndQuest;
import com.felayga.unpixeldungeon.windows.quest.WndSadGhost;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Ghost extends NPC {

	{
		Sample.INSTANCE.load( Assets.SND_GHOST );
	}

	public Ghost()
	{
		super(5);

		name = "sad ghost";
		spriteClass = GhostSprite.class;
		
		flying = true;
		isEthereal = true;
		
		state = WANDERING;
	}
	
	private static final String TXT_RAT1	=
			"Hello %s... Once I was like you - strong and confident... " +
			"But I was slain by a foul beast... I can't leave this place... Not until I have my revenge... " +
			"Slay the _fetid rat_, that has taken my life...\n\n" +
			"It stalks this floor... Spreading filth everywhere... " +
			"_Beware its cloud of stink and corrosive bite, the acid dissolves in water..._ ";

	private static final String TXT_RAT2	=
			"Please... Help me... Slay the abomination...\n\n" +
			"_Fight it near water... Avoid the stench..._";

	private static final String TXT_GNOLL1	=
			"Hello %s... Once I was like you - strong and confident... " +
			"But I was slain by a devious foe... I can't leave this place... Not until I have my revenge... " +
			"Slay the _gnoll trickster_, that has taken my life...\n\n" +
			"It is not like the other gnolls... It hides and uses thrown weapons... " +
			"_Beware its poisonous and incendiary darts, don't attack from a distance..._";

	private static final String TXT_GNOLL2	=
			"Please... Help me... Slay the trickster...\n\n" +
			"_Don't let it hit you... Get near to it..._";

	private static final String TXT_CRAB1	=
			"Hello %s... Once I was like you - strong and confident... " +
			"But I was slain by an ancient creature... I can't leave this place... Not until I have my revenge... " +
			"Slay the _great crab_, that has taken my life...\n\n" +
			"It is unnaturally old... With a massive single claw and a thick shell... " +
			"_Beware its claw, you must surprise the crab or it will block with it..._";

	private static final String TXT_CRAB2	=
			"Please... Help me... Slay the Crustacean...\n\n" +
			"_It will always block... When it sees you coming..._";
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public long speed() {
		return GameTime.TICK / 2;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		Sample.INSTANCE.play( Assets.SND_GHOST );
		
		if (Quest.given) {
			if (Quest.weapon != null) {
				if (Quest.processed) {
					GameScene.show(new WndSadGhost(this, Quest.type));
				} else {
					switch (Quest.type) {
						case 1:
						default:
							GameScene.show(new WndQuest(this, TXT_RAT2));
							break;
						case 2:
							GameScene.show(new WndQuest(this, TXT_GNOLL2));
							break;
						case 3:
							GameScene.show(new WndQuest(this, TXT_CRAB2));
							break;
					}

					int newPos = -1;
					for (int i = 0; i < 10; i++) {
						newPos = Dungeon.level.randomRespawnCell();
						if (newPos != -1) {
							break;
						}
					}
					if (newPos != -1) {

						CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
						pos = newPos;
						sprite.place(pos);
						sprite.visible = Dungeon.visible[pos];
					}
				}
			}
		} else {
			Mob questBoss;
			String txt_quest;

			//todo: fix quest if keeping

			switch (Quest.type){
				case 1: default:
					questBoss = new MarisupialRatFetid();
					txt_quest = Utils.format(TXT_RAT1, Dungeon.hero.givenName()); break;
				case 2:
					questBoss = new GnollTrickster();
					txt_quest = Utils.format(TXT_GNOLL1, Dungeon.hero.givenName()); break;
				case 3:
					questBoss = new GreatCrab();
					txt_quest = Utils.format(TXT_CRAB1, Dungeon.hero.givenName()); break;
			}

			questBoss.pos = Dungeon.level.randomRespawnCell();

			if (questBoss.pos != -1) {
				GameScene.add(questBoss);
				GameScene.show( new WndQuest( this, txt_quest ) );
				Quest.given = true;
				Journal.add( Journal.Feature.GHOST );
			}
		}
	}
	
	@Override
	public String description() {
		return
			"The ghost is barely visible. It looks like a shapeless " +
			"spot of faint light with a sorrowful face.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Paralysis.class );
		IMMUNITIES.add( Roots.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}



	public static class Quest {
		
		private static boolean spawned;

		private static int type;

		private static boolean given;
		private static boolean processed;
		
		private static int depth;
		
		public static Weapon weapon;
		public static Armor armor;
		
		public static void reset() {
			spawned = false;
			
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String TYPE        = "type";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed);
				
				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );

			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

				type = node.getInt(TYPE);
				given	= node.getBoolean( GIVEN );
				processed = node.getBoolean( PROCESSED );

				depth	= node.getInt( DEPTH );
				
				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armor)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 5 - Dungeon.depth ) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell();
				} while (ghost.pos == -1);
				level.mobs.add( ghost );
				
				spawned = true;
				//dungeon depth determines type of quest.
				//depth2=fetid rat, 3=gnoll trickster, 4=great crab
				type = Dungeon.depth-1;
				
				given = false;
				processed = false;
				depth = Dungeon.depth;

				do {
					weapon = Generator.randomWeapon(10);
				} while (weapon instanceof MissileWeapon);
				armor = Generator.randomArmor();

				for (int i = 1; i <= 3; i++) {
					Item another;
					do {
						another = Generator.randomWeapon(10+i);
					} while (another instanceof MissileWeapon);
					if (another.level >= weapon.level) {
						weapon = (Weapon) another;
					}
					another = Generator.randomArmor();
					if (another.level >= armor.level) {
						armor = (Armor) another;
					}
				}

				weapon.identify();
				armor.identify();
			}
		}
		
		public static void process() {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				GLog.n("sad ghost: Thank you... come find me...");
				for (Mob m : Dungeon.level.mobs){
					if (m instanceof Ghost)
						m.beckon(Dungeon.hero.pos);
				}
				Sample.INSTANCE.play( Assets.SND_GHOST );
				processed = true;
				Generator.Category.ARTIFACT.probs[10] = 1; //flags the dried rose as spawnable.
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;
			
			Journal.remove( Journal.Feature.GHOST );
		}

		public static boolean completed(){
			return spawned && processed;
		}
	}


	public static class MarisupialRatFetid extends MarsupialRat {
		public MarisupialRatFetid()
		{
			super();

			name = "fetid rat";
			spriteClass = RabidRatSprite.class;

			DEXCHA = 12;
			nutrition = 30;

			HP = HT = 20;
			defenseSkill = 5;

			EXP = 4;

			state = WANDERING;

			KindOfWeapon weapon = (KindOfWeapon)belongings.weapon;
			belongings.weapon = new AcidChance(attackDelay(weapon.delay_new), weapon.damageMin, weapon.damageMax);
		}

		@Override
		public int defenseProc( Char enemy, int damage ) {

			GameScene.add( Blob.seed( pos, 20, StenchGas.class ) );

			return super.defenseProc(enemy, damage);
		}

		@Override
		public void die( Object cause ) {
			super.die( cause );

			Quest.process();
		}

		@Override
		public String description() {
			return
				"Something is clearly wrong with this rat. Its greasy black fur and rotting skin are very " +
				"different from the healthy rats you've seen previously. It's pale green eyes " +
				"make it seem especially menacing.\n\n" +
				"The rat carries a cloud of horrible stench with it, it's overpoweringly strong up close.\n\n" +
				"Dark ooze dribbles from the rat's mouth, it eats through the floor but seems to dissolve in water.";
		}

		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( StenchGas.class );
		}

		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}



	public static class GnollTrickster extends Gnoll {
		ComboChance specialWeapon;

		{
			name = "gnoll trickster";
			spriteClass = GnollTricksterSprite.class;

			DEXCHA = 16;

			HP = HT = 20;
			defenseSkill = 5;

			EXP = 5;

			state = WANDERING;

			loot = Generator.random(CurareDart.class);
			lootChance = 1f;

			KindOfWeapon weapon = (KindOfWeapon)belongings.weapon;
			specialWeapon = new ComboChance(attackDelay(weapon.delay_new), weapon.damageMin, weapon.damageMax);
			belongings.weapon = specialWeapon;
		}

		@Override
		protected boolean canAttack( Char enemy ) {
			Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
			return !Level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
		}

		@Override
		protected boolean getCloser( int target ) {
			specialWeapon.combo = 0; //if he's moving, he isn't attacking, reset combo.
			if (state == HUNTING) {
				return enemySeen && getFurther( target );
			} else {
				return super.getCloser( target );
			}
		}

		@Override
		public void die( Object cause ) {
			super.die( cause );

			Quest.process();
		}

		@Override
		public String description() {
			return
					"A strange looking creature, even by gnoll standards. It hunches forward with a wicked grin, " +
					"almost cradling the satchel hanging over its shoulder. Its eyes are wide with a strange mix of " +
					"fear and excitement.\n\n" +
					"There is a large collection of poorly made darts in its satchel, they all seem to be " +
					"tipped with various harmful substances.";
		}

		private static final String COMBO = "combo";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle(bundle);
			bundle.put(COMBO, specialWeapon.combo);
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			specialWeapon.combo = bundle.getInt( COMBO );
		}

	}



	public static class GreatCrab extends Crab {
		{
			name = "great crab";
			spriteClass = GreatCrabSprite.class;

			HP = HT = 25;
			defenseSkill = 0; //see damage()
			movementSpeed = GameTime.TICK;

			EXP = 6;

			state = WANDERING;
		}

		private int moving = 0;

		@Override
		protected boolean getCloser( int target ) {
			//this is used so that the crab remains slower, but still detects the player at the expected rate.
			moving++;
			if (moving < 3) {
				return super.getCloser( target );
			} else {
				moving = 0;
				return true;
			}

		}

		@Override
		public void damage( int dmg, Object src ){
			//crab blocks all attacks originating from the hero or enemy characters or traps if it is alerted.
			//All direct damage from these sources is negated, no exceptions. blob/debuff effects go through as normal.
			if (enemySeen && (src instanceof Wand || src instanceof LightningTrap.Electricity || src instanceof Char)){
				GLog.n("The crab notices the attack and blocks with its massive claw.");
				sprite.showStatus( CharSprite.NEUTRAL, "blocked" );
			} else {
				super.damage( dmg, src );
			}
		}

		@Override
		public void die( Object cause ) {
			super.die( cause );

			Quest.process();

			Dungeon.level.drop( new MysteryMeat(), pos );
			Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
		}

		@Override
		public String description() {
			return
					"This crab is gigantic, even compared to other sewer crabs. " +
					"Its blue shell is covered in cracks and barnacles, showing great age. " +
					"It lumbers around slowly, barely keeping balance with its massive claw.\n\n" +
					"While the crab only has one claw, its size easily compensates. " +
					"The crab holds the claw infront of itself whenever it sees a threat, shielding " +
					"itself behind an impenetrable wall of carapace.";
		}
	}
}

*/