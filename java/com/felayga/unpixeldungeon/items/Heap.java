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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.Splash;
import com.felayga.unpixeldungeon.effects.particles.ElmoParticle;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.bags.ItemIterator;
import com.felayga.unpixeldungeon.items.food.Blandfruit;
import com.felayga.unpixeldungeon.items.potions.Potion;
import com.felayga.unpixeldungeon.items.potions.PotionOfExperience;
import com.felayga.unpixeldungeon.items.scrolls.Scroll;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.plants.Plant.Seed;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.Icons;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class Heap implements Bundlable, IBag {
    //region IBag

    public Item self() {
        return null;
    }
    public String action() { return null; }
    public String getDisplayName() {
        return "ground";
    }
    public Icons tabIcon() {
        return Icons.FLOORHEAP;
    }
    public Char owner() { return null; }

    public void onWeightChanged(int change) {

    }

    public boolean tryMergeExistingStack(Item test) {
        return collect(test);
    }

    public boolean collect(Item item) {
        boolean retval = false;

        if (item.stackable) {
            for (Item subitem : items_derp) {
                if (item.isStackableWith(subitem)) {
                    subitem.quantity(subitem.quantity() + item.quantity());
                    subitem.updateQuickslot();

                    updateImage();
                    retval = true;
                }
            }
        }

        if (!retval) {
            retval = items_derp.add(item);
            item.parent(this);
        }

        if (retval) {
            updateImage();
        }

        return retval;
    }

    public boolean contains(Item item) {
        for (Item i : items_derp) {
            if (i == item) {
                return true;
            } else if (i instanceof IBag && ((IBag) i).contains(item)) {
                return true;
            }
        }
        return false;
    }

    public Item remove(Item item) {
        if (items_derp.contains(item)) {
            item.parent(null);
            items_derp.remove(item);

            updateImage();
            return item;
        }

        return null;
    }

    public Item remove(Item item, int quantity) {
        if (item.quantity() > quantity) {
            return Item.ghettoSplitStack(item, quantity, owner());
        } else if (item.quantity() == quantity) {
            return remove(item);
        }

        return null;
    }

    public boolean locked() { return false; }

    public Item randomItem() {
        return Random.element(items_derp);
    }

    public Iterator<Item> iterator() {
        return iterator(true);
    }

    public Iterator<Item> iterator(boolean allowNested) {
        return new ItemIterator(this, items_derp, allowNested);
    }

    //endregion

	private static final String TXT_MIMIC = "This is a mimic!";

	private static final int SEEDS_TO_POTION = 3;
	
	public enum Type {
		HEAP,
		FOR_SALE,
		TOMB,
		SKELETON,
		REMAINS
	}
	public Type type = Type.HEAP;
	
	public int pos = 0;
    public int pos() {
        return pos;
    }
	
	public ItemSprite sprite;
	public boolean seen = false;
	
	private LinkedList<Item> items_derp = new LinkedList<Item>();
	
	public int image() {
		switch (type) {
		case HEAP:
		case FOR_SALE:
			return size() > 0 ? items_derp.peek().image() : 0;
		case TOMB:
			return ItemSpriteSheet.TOMB;
		case SKELETON:
			return ItemSpriteSheet.BONES;
		case REMAINS:
			return ItemSpriteSheet.REMAINS;
		default:
			return 0;
		}
	}
	
	public ItemSprite.Glowing glowing() {
		return (type == Type.HEAP || type == Type.FOR_SALE) && items_derp.size() > 0 ? items_derp.peek().glowing() : null;
	}
	
	public void open( Hero hero ) {
        switch (type) {
            case TOMB:
                //todo: wraith spawn maybe?
                //Wraith.spawnAround( hero.pos );
                break;
            case SKELETON:
            case REMAINS:
                CellEmitter.center(pos).start(Speck.factory(Speck.RATTLE), 0.1f, 3);
                for (Item item : items_derp) {
                    if (item.bucStatus == BUCStatus.Cursed) {
                        //todo: wraith spawn from opening remains maybe?
                        if (false/*Wraith.spawnAt( pos ) == null*/) {
                            hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                            hero.damage(hero.HP / 2, MagicType.Magic, null);
                        }
                        Sample.INSTANCE.play(Assets.SND_CURSED);
                        item.bucStatusKnown = true;
                        break;
                    }
                }
                break;
            default:
        }

        type = Type.HEAP;
        sprite.link();
        sprite.drop();
    }
	
	public int size() {
        if (items_derp != null) {
            return items_derp.size();
        }

        return 0;
	}
	
	public Item pickUp() {
		Item item = remove(items_derp.get(0));
		updateImage();
		
		return item;
	}

	public void updateImage() {
		if (size() <= 0) {
			destroy();
		}
		else if (sprite != null) {
			sprite.view(image(), glowing());
		}
	}
	
	public Item peek() {
		return items_derp.peek();
	}

	public void replace( Item a, Item b ) {
        if (contains(a)) {
            remove(a);
            collect(b);
		}
	}
	
	public void burn() {
		if (type != Type.HEAP) {
			return;
		}
		
		boolean burnt = false;
		boolean evaporated = false;

        Iterator<Item> iterator = iterator(false);
        while (iterator.hasNext()) {
            Item item = iterator.next();

			if (item instanceof Scroll) {
				iterator.remove();
				burnt = true;
			} else if (item instanceof Dewdrop) {
                iterator.remove();
				evaporated = true;
                /*
			} else if (item instanceof MysteryMeat) {
				replace( item, ChargrilledMeat.cook( (MysteryMeat)item ) );
				burnt = true;
				*/
			} else if (item instanceof Bomb) {
                iterator.remove();
				((Bomb) item).explode( pos );
				//stop processing the burning, it will be replaced by the explosion.
				return;
			}
		}
		
		if (burnt || evaporated) {
			
			if (Dungeon.visible[pos]) {
				if (burnt) {
					burnFX( pos );
				} else {
					evaporateFX( pos );
				}
			}
			
			if (size() <= 0) {
				destroy();
			} else if (sprite != null) {
				sprite.view( image(), glowing() );
			}
			
		}
	}

	//Note: should not be called to initiate an explosion, but rather by an explosion that is happening.
	public void explode() {
		//breaks open most standard containers, mimics die.
		if (type == Type.SKELETON) {
			type = Type.HEAP;
			sprite.link();
			sprite.drop();
			return;
		}

		if (type != Type.HEAP) {
			return;
		} else {
            Iterator<Item> iterator = iterator(false);

			while (iterator.hasNext()) {
                Item item = iterator.next();

				if (item instanceof Potion) {
					iterator.remove();
					((Potion) item).shatter(pos);
				} else if (item instanceof Bomb) {
					iterator.remove();
					((Bomb) item).explode(pos);
					//stop processing current explosion, it will be replaced by the new one.
					return;
				//unique and upgraded items can endure the blast
				} else if (!(item.level > 0 || item.unique)) {
                    iterator.remove();
                }
			}

			if (items_derp.isEmpty()) {
                destroy();
            }
		}
	}
	
	public void freeze() {
		if (type != Type.HEAP) {
			return;
		}
		
		boolean frozen = false;

        Iterator<Item> iterator = iterator(false);

		while (iterator.hasNext()) {
            Item item = iterator.next();

            /*
			if (item instanceof MysteryMeat) {
				replace( item, FrozenCarpaccio.cook( (MysteryMeat)item ) );
				frozen = true;
			} else
			*/

            if (item instanceof Potion) {
				iterator.remove();
				((Potion) item).shatter(pos);
				frozen = true;
			} else if (item instanceof Bomb){
				((Bomb) item).fuse = null;
				frozen = true;
			}
		}
		
		if (frozen) {
			if (size() <= 0) {
				destroy();
			} else if (sprite != null) {
				sprite.view( image(), glowing() );
			}
		}
	}
	
	public Item transmute() {
		CellEmitter.get( pos ).burst( Speck.factory( Speck.BUBBLE ), 3 );
		Splash.at(pos, 0xFFFFFF, 3);
		
		float chances[] = new float[items_derp.size()];
		int count = 0;


		if (items_derp.size() == 2 && items_derp.get(0) instanceof Seed && items_derp.get(1) instanceof Blandfruit ) {

			Sample.INSTANCE.play( Assets.SND_PUFF );
			CellEmitter.center( pos ).burst( Speck.factory( Speck.EVOKE ), 3 );

			Blandfruit result = new Blandfruit();
			result.cook((Seed)items_derp.get(0));

			destroy();

			return result;

		}
		
		int index = 0;

        Iterator<Item> iterator = iterator(false);

		while (iterator.hasNext()) {
            Item item = iterator.next();

			if (item instanceof Seed) {
				count += item.quantity();
				chances[index++] = item.quantity();
			}  else{
				count = 0;
				break;
			}
		}

		//alchemists toolkit gives a chance to cook a potion in two or even one seeds
		AlchemistsToolkit.alchemy alchemy = Dungeon.hero.buff(AlchemistsToolkit.alchemy.class);
		int bonus = alchemy != null ? alchemy.level() : -1;

		if (bonus != -1 ? alchemy.tryCook(count) : count >= SEEDS_TO_POTION) {

			CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.SND_PUFF );

			Item potion;

			if (Random.Int( count + bonus ) == 0) {

				CellEmitter.center( pos ).burst( Speck.factory( Speck.EVOKE ), 3 );

				destroy();

				Statistics.potionsCooked++;
				Badges.validatePotionsCooked();

				potion = Generator.random( Generator.Category.POTION );

			} else {

				Seed proto = (Seed)items_derp.get( Random.chances( chances ) );
				Class<? extends Item> itemClass = proto.alchemyClass;

				destroy();

				Statistics.potionsCooked++;
				Badges.validatePotionsCooked();

				if (itemClass == null) {
					potion =  Generator.random( Generator.Category.POTION );
				} else {
					try {
						potion =  itemClass.newInstance();
					} catch (Exception e) {
						return null;
					}
				}
			}

			//not a buff per-se, meant to cancel out higher potion accuracy when ppl are farming for potions of exp.
			if (bonus > 0)
				if (Random.Int(1000/bonus) == 0)
					return new PotionOfExperience();

			/*
			while (potion instanceof PotionOfHealing && Random.Int(10) < Dungeon.limitedDrops.cookingHP.count)
				potion = Generator.random( Generator.Category.POTION );

			if (potion instanceof PotionOfHealing)
				Dungeon.limitedDrops.cookingHP.count++;
			*/

			return potion;

		} else {
			return null;
		}
	}

    public long decay() {
        return 0;
    }

    public boolean decay(long currentTime, boolean updateTime, boolean fixTime) {
        boolean updated = false;
        Iterator<Item> iterator = iterator(false);

        while (iterator.hasNext()) {
            Item item = iterator.next();

            if (item instanceof IDecayable) {
                IDecayable decayable = (IDecayable) item;
                if (decayable.decay(currentTime, updateTime, fixTime)) {
                    iterator.remove();
                    updated = true;
                }
            }
        }

        return updated;
    }

	public static void burnFX( int pos ) {
		CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
		Sample.INSTANCE.play(Assets.SND_BURNING);
	}
	
	public static void evaporateFX( int pos ) {
		CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 5 );
	}
	
	public void destroy() {
		Dungeon.level.heaps.remove( this.pos );
		if (sprite != null) {
			sprite.kill();
		}
        if (items_derp != null) {
            items_derp.clear();
        }
        items_derp = null;
	}

	private static final String POS		= "pos";
	private static final String SEEN	= "seen";
	private static final String TYPE	= "type";
	private static final String ITEMS	= "items";
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos = bundle.getInt(POS);
		seen = bundle.getBoolean(SEEN);
		type = Type.valueOf(bundle.getString(TYPE));

        items_derp = new LinkedList<Item>();

        Collection<Item> restored = (Collection<Item>) ((Collection<?>) bundle.getCollection( ITEMS ));
        restored.removeAll(Collections.singleton(null));

        for (Item item : restored) {
            collect(item);
        }
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( POS, pos );
		bundle.put( SEEN, seen );
		bundle.put( TYPE, type.toString() );
		bundle.put( ITEMS, items_derp );
	}
	
}
