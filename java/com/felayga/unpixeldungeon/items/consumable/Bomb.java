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

package com.felayga.unpixeldungeon.items.consumable;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.particles.BlastMagicParticle;
import com.felayga.unpixeldungeon.effects.particles.BlastParticle;
import com.felayga.unpixeldungeon.effects.particles.SmokeParticle;
import com.felayga.unpixeldungeon.items.Heap;
import com.felayga.unpixeldungeon.items.IFlammable;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.mechanics.Material;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Bomb extends Item implements IFlammable {

    public Bomb()
	{
		name = "bomb";
		image = ItemSpriteSheet.BOMB;
        material = Material.Mineral;

        hasLevels(false);

		defaultAction = AC_LIGHTTHROW;
		usesTargeting = true;

		stackable = true;
        price = 50;
	}

    @Override
    public boolean burn(Char cause) {
        IBag parent = parent();
        parent.remove(this, 1);
        explode(cause, parent.pos());
        return true;
    }

	public Fuse fuse;

	//FIXME using a static variable for this is kinda gross, should be a better way
	private static boolean lightingFuse = false;

	private static final String AC_LIGHTTHROW = "Light & Throw";

	@Override
    protected boolean checkSimilarity(Item item) {
		return item instanceof Bomb && this.fuse == ((Bomb) item).fuse;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add ( AC_LIGHTTHROW );
		return actions;
	}

	@Override
	public boolean execute(Hero hero, String action) {
		if (action.equals( AC_LIGHTTHROW )){
			lightingFuse = true;
			action = Constant.Action.THROW;
		} else {
			lightingFuse = false;
		}

		return super.execute(hero, action);
	}

	@Override
	protected void onThrow( Char thrower, int cell ) {
        if (!Level.chasm[cell] && lightingFuse) {
            Actor.addDelayed(fuse = new Fuse(thrower).ignite(this), 2);
        }
        if (Actor.findChar(cell) != null && !(Actor.findChar(cell) == Dungeon.hero)) {
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int i : Level.NEIGHBOURS8)
                if (Level.passable[cell + i])
                    candidates.add(cell + i);
            int newCell = candidates.isEmpty() ? cell : Random.element(candidates);
            Dungeon.level.drop(this, newCell).sprite.drop(cell);
        } else {
            super.onThrow(thrower, cell);
        }
    }

	@Override
	public boolean doPickUp(Hero hero) {
		if (fuse != null) {
			GLog.w("You quickly snuff the bomb's fuse.");
			fuse = null;
		}
		return super.doPickUp(hero);
	}

    public static void explode(Char cause, Item causeItem, int cell, boolean destroyItems, MagicType damageType, int minDamage, int maxDamage) {
        Sample.INSTANCE.play( Assets.SND_BLAST );

        if (Dungeon.visible[cell]) {
            if (damageType == MagicType.Magic) {
                CellEmitter.center(cell).burst(BlastMagicParticle.FACTORY, 30);
            } else {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
            }
        }

        boolean terrainAffected = false;
        for (int n : Level.NEIGHBOURS9) {
            int c = cell + n;
            if (c >= 0 && c < Level.LENGTH) {
                if (Dungeon.visible[c]) {
                    CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 4 );
                }

                if (Level.burnable[c]) {
                    Dungeon.level.set(c, Terrain.CHARCOAL, true);
                    GameScene.updateMap(c);
                    terrainAffected = true;
                }

                if (destroyItems) {
                    //destroys items / triggers bombs caught in the blast.
                    Heap heap = Dungeon.level.heaps.get(c);
                    if (heap != null) {
                        heap.explode(cause);
                    }
                }

                Char ch = Actor.findChar( c );
                if (ch != null) {
                    //those not at the center of the blast take damage less consistently.
                    int minCorrected = minDamage;
                    if (c != cell) {
                        minCorrected /= 4;
                    }

                    int dmg = Random.NormalIntRange( minCorrected, maxDamage );
                    if (dmg > 0) {
                        ch.damage( dmg, damageType, cause, causeItem );
                    }

                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        Dungeon.fail("Killed by an explosion");
                    }
                }
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }
    }

	public void explode(Char cause, int cell) {
        //We're blowing up, so no need for a fuse anymore.
        this.fuse = null;

        explode(cause, null, cell, true, MagicType.Mundane, 3, 18);
    }
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public Item random() {
		switch(Random.Int( 2 )){
			case 0:
			default:
				return this;
			case 1:
				return new DoubleBomb();
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return fuse != null ? new ItemSprite.Glowing( 0xFF0000, 0.6f) : null;
	}
	
	@Override
	public String info() {
		return
			"A fairly hefty black powder bomb. An explosion from this would certainly do damage to anything nearby." +
				(fuse != null ? "\n\nThe bomb's fuse is burning away, keep your distance or put it out!" :
					"\n\nIt looks like the fuse will take a couple rounds to burn down once it is lit.");
	}

	private static final String FUSE = "fuse";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FUSE, fuse );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains( FUSE ))
			Actor.add( fuse = ((Fuse)bundle.get(FUSE)).ignite(this) );
	}


	public static class Fuse extends Actor{
        private Char cause;

        public Fuse(Char cause)
		{
			actPriority = 3; //as if it were a buff

            this.cause = cause;
        }

		private Bomb bomb;

		public Fuse ignite(Bomb bomb){
			this.bomb = bomb;
			return this;
		}

		@Override
		protected boolean act() {
			//something caused our bomb to explode early, or be defused. Do nothing.
			if (bomb.fuse != this){
				Actor.remove( this );
				return true;
			}

			//look for our bomb, remove it from its heap, and blow it up.
			for (Heap heap : Dungeon.level.heaps.values()) {
				if (heap.contains(bomb)) {
					heap.remove(bomb);

					bomb.explode(cause, heap.pos());

					Actor.remove(this);
					return true;
				}
			}

			//can't find our bomb, something must have removed it, do nothing.
			bomb.fuse = null;
			Actor.remove( this );
			return true;
		}
	}


	public static class DoubleBomb extends Bomb{

		{
			name = "two bombs";
			image = ItemSpriteSheet.DBL_BOMB;
			stackable = false;
		}

		@Override
		public String info() {
			return
				"A stack of two hefty black powder bombs, looks like you get one free!";
		}

		@Override
		public boolean doPickUp(Hero hero) {
			Bomb bomb = new Bomb();
			bomb.quantity(2);
			if (bomb.doPickUp(hero)) {
				//isaaaaac....
				hero.sprite.showStatus(CharSprite.NEUTRAL, "1+1 free!");
				return true;
			}
			return false;
		}
	}
}
