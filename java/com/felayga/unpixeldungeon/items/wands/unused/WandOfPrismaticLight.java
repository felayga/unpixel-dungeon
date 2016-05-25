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
package com.felayga.unpixeldungeon.items.wands.unused;
/*
import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Blindness;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.Cripple;
import com.felayga.unpixeldungeon.actors.buffs.Light;
import com.felayga.unpixeldungeon.actors.mobs.*;
//import com.felayga.unpixeldungeon.actors.mobs.npcs.Ghost;
//import com.felayga.unpixeldungeon.actors.mobs.unused.Scorpio;
import com.felayga.unpixeldungeon.effects.Beam;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.particles.RainbowParticle;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;

public class WandOfPrismaticLight extends Wand {

	{
		name = "Wand of Prismatic Light";
		image = ItemSpriteSheet.WAND_PRISMATIC_LIGHT;

		collisionProperties = Ballistica.MAGIC_BOLT;
	}

	//FIXME: this is sloppy
	private static HashSet<Class> evilMobs = new HashSet<Class>(Arrays.asList(
			//Any Location
			Mimic.class, Wraith.class,
			//Sewers
			Ghost.MarisupialRatFetid.class,
			Goo.class,
			//Prison
			Skeleton.class , Thief.class, Bandit.class,
			//Caves

			//City
			Warlock.class, Monk.class, Senior.class,
			King.class, King.Undead.class,
			//Halls
			Succubus.class, Eye.class, Scorpio.class, Acidic.class,
			Yog.class, Yog.RottingFist.class, Yog.BurningFist.class, Yog.Larva.class
	));

	@Override
	protected void onZap(Ballistica beam) {
		Char ch = Actor.findChar(beam.collisionPos);
		if (ch != null){
			processSoulMark(ch, chargesPerCast());
			affectTarget(ch);
		}
		affectMap(beam);

		if (curUser.viewDistance < 4)
			Buff.prolong( curUser, Light.class, GameTime.TICK * (10 + level*5));
	}

	private void affectTarget(Char ch){
		int dmg = Random.NormalIntRange(level, (int) (8+(level*(level/5f))));

		//three in (5+lvl) chance of failing
		if (Random.Int(5+level) >= 3) {
			Buff.prolong(ch, Blindness.class, GameTime.TICK * (2 + (level * 34 / 100)));
			ch.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 6 );
		}

		if (evilMobs.contains(ch.getClass())){
			ch.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10+level );
			Sample.INSTANCE.play(Assets.SND_BURNING);

			ch.damage((int)(dmg*1.5), this);
		} else {
			ch.sprite.centerEmitter(-1).burst( RainbowParticle.BURST, 10+level );

			ch.damage(dmg, this);
		}

	}

	private void affectMap(Ballistica beam){
		boolean noticed = false;
		for (int c: beam.subPath(0, beam.dist)){
			for (int n : Level.NEIGHBOURS9DIST2){
				int cell = c+n;
				if (!Level.insideMap(cell))
					continue;

				if (Level.discoverable[cell])
					Dungeon.level.mapped[cell] = true;

				int terr = Dungeon.level.map[cell];
				if ((Terrain.flags[terr] & Terrain.FLAG_SECRET) != 0) {

					Dungeon.level.discover( cell );

					GameScene.discoverTile( cell, terr );
					ScrollOfMagicMapping.discover(cell);

					noticed = true;
				}
			}

			CellEmitter.center(c).burst( RainbowParticle.BURST, Random.IntRange( 1, 2 ) );
		}
		if (noticed)
			Sample.INSTANCE.play( Assets.SND_SECRET );

		Dungeon.observe();
	}

	@Override
	protected void fx( Ballistica beam, Callback callback ) {
		curUser.sprite.parent.add(
				new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.tileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//cripples enemy
		Buff.prolong( defender, Cripple.class, GameTime.TICK * (1 + staff.level));
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( Random.Int( 0x1000000 ) );
		particle.am = 0.3f;
		particle.setLifespan(1f);
		particle.speed.polar(Random.Float(PointF.PI2), 2f);
		particle.setSize( 1f, 2.5f);
		particle.radiateXY(1f);
	}

	@Override
	public String desc() {
		return
			"This wand is made of a solid piece of translucent crystal, like a long chunk of smooth glass. " +
			"It becomes clear towards the tip, where you can see colorful lights dancing around inside it.\n\n" +
			"This wand shoots rays of light which damage and blind enemies and cut through the darkness of the dungeon, " +
			"revealing hidden areas and traps. Evildoers, demons, and the undead will burn in the bright light " +
			"of the wand, taking significant bonus damage.";
	}
}
*/