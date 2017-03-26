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
package com.felayga.unpixeldungeon.items;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.Splash;
import com.felayga.unpixeldungeon.effects.particles.ElmoParticle;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.effects.particles.ShaftParticle;
import com.felayga.unpixeldungeon.items.bags.IBag;
import com.felayga.unpixeldungeon.items.bags.ItemIterator;
import com.felayga.unpixeldungeon.items.consumable.Bomb;
import com.felayga.unpixeldungeon.items.consumable.potions.IAlchemyComponent;
import com.felayga.unpixeldungeon.items.consumable.potions.Potion;
import com.felayga.unpixeldungeon.items.consumable.potions.PotionOfBrewing;
import com.felayga.unpixeldungeon.items.consumable.wands.Wand;
import com.felayga.unpixeldungeon.items.equippableitem.weapon.ammunition.simple.Rock;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.IDecayable;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.sprites.ItemSprite;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.ui.Icons;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Heap implements Bundlable, IBag {
    //region IBag

    public Item self() {
        return null;
    }

    public String action() {
        return null;
    }

    public String getDisplayName() {
        return "on the ground nearby";
    }

    public Icons tabIcon() {
        return Icons.FLOORHEAP;
    }

    public Char owner() {
        return null;
    }

    public IBag parent() {
        return null;
    }

    public void onWeightChanged(int change) {

    }

    public boolean tryMergeExistingStack(Item test) {
        return collect(test);
    }

    public boolean collect(Item item) {
        boolean retval = false;

        if (Dungeon.level != null && Dungeon.level.map(pos) == Terrain.ALTAR) {
            if (!item.bucStatusKnown()) {
                item.bucStatus(true);

                switch (item.bucStatus()) {
                    case Blessed:
                        CellEmitter.get(pos).start(ShaftParticle.FACTORY_GREEN, 0.2f, 3);
                        break;
                    case Cursed:
                        CellEmitter.get(pos).start(ShaftParticle.FACTORY_RED, 0.2f, 3);
                        break;
                    default:
                        CellEmitter.get(pos).start(ShaftParticle.FACTORY, 0.2f, 3);
                        break;
                }
            }
        }

        if (item.stackable) {
            for (Item subitem : items) {
                if (item.isStackableWith(subitem)) {
                    subitem.quantity(subitem.quantity() + item.quantity());
                    subitem.updateQuickslot();

                    retval = true;
                }
            }
        }

        if (!retval) {
            retval = items.add(item);
            item.parent(this);
        }

        if (retval) {
            updateImage();
        }

        return retval;
    }

    public void rockBottom() {
        //having rocks appear over gems in mining operations would be tedious and lame

        Rock rock = null;

        for (Item item : items) {
            if (item instanceof Rock) {
                rock = (Rock) item;
                break;
            }
        }

        if (rock != null) {
            items.remove(rock);
            items.add(0, rock);
            updateImage();
        }
    }

    public void bury() {
        for (Item item : items) {
            itemsBuried.add(item);
        }

        items.clear();

        updateImage();
    }

    public void unbury(boolean verbose) {
        for (Item item : itemsBuried) {
            collect(item);
        }

        itemsBuried.clear();

        if (type == Type.GRAVE) {
            switch(Random.Int(5)) {
                case 0:
                case 1:
                    if (verbose) {
                        GLog.w("You unearth a corpse.");
                    }
                    //todo: random corpse
                    break;
                case 2:
                    if (verbose) {
                        if (GameScene.isHallucinating()) {
                            GLog.w("Dude!  The living dead!");
                        } else {
                            GLog.w("The grave's owner is very upset!");
                        }
                    }
                    //todo: spawn zombie
                    break;
                case 3:
                    if (verbose) {
                        if (GameScene.isHallucinating()) {
                            GLog.w("Are you my mummy?");
                        } else {
                            GLog.w("You've disturbed a tomb!");
                        }
                    }
                    //todo: spawn mummy
                    break;
                default:
                    if (verbose) {
                        GLog.w("The grave seems unused.  Strange...");
                    }
                    //nothing
                    break;
            }

            type = Type.HEAP;
        }

        updateImage();
    }

    public boolean contains(Item item) {
        for (Item i : items) {
            if (i == item) {
                return true;
            } else if (i instanceof IBag && ((IBag) i).contains(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Class<?> type, boolean allowNested) {
        for (Item i : items) {
            if (type.isAssignableFrom(i.getClass())) {
                return true;
            } else if (allowNested && i instanceof IBag && ((IBag) i).contains(type, allowNested)) {
                return true;
            }
        }
        return false;
    }

    public Item remove(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            if (item.parent() == this) {
                item.parent(null);
            }

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

    public void onNestedItemRemoved(Item item) {
        //heap don't care
    }

    public boolean locked() {
        return false;
    }

    public Item randomItem() {
        return Random.element(items);
    }

    public Iterator<Item> iterator() {
        return iterator(true);
    }

    public Iterator<Item> iterator(boolean allowNested) {
        return new ItemIterator(this, items, allowNested);
    }

    public Iterator<Item> iteratorBuried(boolean allowNested) {
        return new ItemIterator(this, itemsBuried, allowNested);
    }

    public void contentsScatter() {
        List<Integer> newPos = new ArrayList<>();

        for (int offset : Level.NEIGHBOURS8) {
            int subPos = pos + offset;

            if (Level.passable[subPos]) {
                newPos.add(subPos);
            }
        }

        while (items != null && items.size() > 0) {
            Item item = remove(items.getLast());
            Dungeon.level.drop(item, newPos.get(Random.Int(newPos.size())));
        }
    }

    public void contentsImpact(boolean verbose) {
        Iterator<Item> iterator = iterator(false);

        List<Item> pendingremoval = new ArrayList<>();

        int shatter = 0;
        int crack = 0;

        while (iterator.hasNext()) {
            Item item = iterator.next();

            if (item instanceof IBag) {
                IBag bag = (IBag) item;
                bag.contentsImpact(verbose);
            } else if (item instanceof Potion) {
                int count = item.quantity();

                while (count > 0) {
                    if (Random.Int(3) != 0) {
                        pendingremoval.add(item);
                        shatter++;
                    }
                    count--;
                }
            } else if (item instanceof Wand) {
                if (Random.Int(3) != 0) {
                    pendingremoval.add(item);
                    crack++;
                }
            }
        }

        if (pendingremoval.size() > 0) {
            if (verbose && Dungeon.audible[pos]) {
                if (shatter > 0 && crack > 0) {
                    Sample.INSTANCE.play(Assets.SND_SHATTER);
                    Sample.INSTANCE.play(Assets.SND_DOOR_SMASH);
                } else if (shatter > 0) {
                    Sample.INSTANCE.play(Assets.SND_SHATTER);
                    //todo: broken potion effects in heap
                } else if (crack > 0) {
                    Sample.INSTANCE.play(Assets.SND_DOOR_SMASH);
                }
            }

            for (Item item : pendingremoval) {
                this.remove(item, 1);
            }
        }
    }

    //endregion

    private static final String TXT_MIMIC = "This is a mimic!";

    private static final int SEEDS_TO_POTION = 3;

    public enum Type {
        HEAP,
        GRAVE
    }

    public Type type = Type.HEAP;

    private int pos = 0;

    public void pos(int newPos) {
        if (newPos < 0) {
            GLog.d("heap pos="+newPos+"?");
            GLog.d(""+1/0);
        }
        this.pos = newPos;
    }

    public int pos() {
        return pos;
    }

    public ItemSprite sprite;
    public boolean seen = false;

    private LinkedList<Item> items = new LinkedList<>();
    private LinkedList<Item> itemsBuried = new LinkedList<>();

    /*
	public int image() {
		switch (type) {
		case HEAP:
		case FOR_SALE:
			return size() > 0 ? items_derp.peekLast().image() : 0;
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
	
	public ItemSpriteBase.Glowing glowing() {
		return (type == Type.HEAP || type == Type.FOR_SALE) && items_derp.size() > 0 ? items_derp.peekLast().glowing() : null;
	}
	*/

    public void open(Hero hero) {
        /*
        switch (type) {
            case TOMB:
                //todo: wraith spawn maybe?
                //Wraith.spawnAround( hero.pos );
                break;
            case SKELETON:
            case REMAINS:
                CellEmitter.center(pos).start(Speck.factory(Speck.RATTLE), 0.1f, 3);
                for (Item item : items) {
                    if (item.bucStatus() == BUCStatus.Cursed) {
                        //todo: wraith spawn from opening remains maybe?
                        if (false) { //Wraith.spawnAt( pos ) == null) {
                            hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                            hero.damage(hero.HP / 2, MagicType.Magic, null, null);
                        }
                        Sample.INSTANCE.play(Assets.SND_CURSED);
                        item.bucStatus(true);
                        break;
                    }
                }
                break;
            default:
        }
        */

        type = Type.HEAP;
        sprite.link();
        sprite.drop();
    }

    public boolean isEmpty() {
        return items.size() <= 0 && itemsBuried.size() <= 0;
    }

    public int size() {
        return items.size();
    }

    public Item pickUp() {
        Item item = remove(items.get(0));
        updateImage();

        return item;
    }

    public void updateImage() {
        if (isEmpty()) {
            destroy();
        } else if (sprite != null) {
            if (size() > 0) {
                sprite.view(peek());
            } else {
                sprite.view(ItemSpriteSheet.ITEM_EMPTY, null);
            }
        }
    }

    public Item peek() {
        return items.peekLast();
    }

    public void replace(Item a, Item b) {
        if (contains(a)) {
            remove(a);
            collect(b);
        }
    }

    public void contentsBurn(Char cause) {
        if (type != Type.HEAP) {
            return;
        }

        boolean burnt = false;
        boolean evaporated = false;

        List<IFlammable> flammables = new ArrayList<>();

        Iterator<Item> iterator = iterator(false);
        while (iterator.hasNext()) {
            Item item = iterator.next();

            if (item instanceof IFlammable) {
                for (int n = 0; n < item.quantity(); n++) {
                    if (Random.Int(Constant.Chance.ITEM_DESTROYED) == 0) {
                        IFlammable flammable = (IFlammable) item;
                        flammables.add(flammable);
                    }
                }
            }
        }

        if (flammables.size() > 0) {
            burnt = true;

            for (IFlammable flammable : flammables) {
                if (flammable.burn(cause)) {
                    return;
                }
            }
        }

        if (burnt || evaporated) {
            if (burnt) {
                burnFX(pos, Dungeon.visible[pos], Dungeon.audible[pos]);
            } else {
                evaporateFX(pos, Dungeon.visible[pos], Dungeon.audible[pos]);
            }

            updateImage();
        }
    }

    //Note: should not be called to initiate an explosion, but rather by an explosion that is happening.
    public void explode(Char cause) {
        //breaks open most standard containers, mimics die.
        /*
        if (type == Type.SKELETON) {
            type = Type.HEAP;
            sprite.link();
            sprite.drop();
            return;
        }

        if (type != Type.HEAP) {
            return;
        } else {
        */

        Iterator<Item> iterator = iterator(false);

        while (iterator.hasNext()) {
            Item item = iterator.next();

            if (item instanceof Potion) {
                iterator.remove();
                ((Potion) item).shatter(null, pos);
            } else if (item instanceof Bomb) {
                iterator.remove();
                ((Bomb) item).explode(cause, pos);
                //stop processing current explosion, it will be replaced by the new one.
                return;
                //unique and upgraded items can endure the blast
            } else if (!(item.level() > 0 || item.unique)) {
                iterator.remove();
            }
        }

        updateImage();
    }

    public void freeze(Char source) {
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
                ((Potion) item).shatter(source, pos);
                frozen = true;
            } else if (item instanceof Bomb) {
                ((Bomb) item).fuse = null;
                frozen = true;
            }
        }

        if (frozen) {
            updateImage();
        }
    }

    public Item transmute() {
        Item retval = null;

        CellEmitter.get(pos).burst(Speck.factory(Speck.BUBBLE), 3);
        Splash.at(pos, 0xFFFFFF, 3);

        float chances[] = new float[items.size()];
        int count = 0;

        if (items.size() >= 2) {
            List<IAlchemyComponent> components = new ArrayList<>();

            Iterator<Item> iterator = iterator(false);
            while (iterator.hasNext()) {
                Item item = iterator.next();

                if (item instanceof IAlchemyComponent) {
                    components.add((IAlchemyComponent) item);
                }
            }

            if (components.size() >= 2) {
                Collections.shuffle(components);

                retval = PotionOfBrewing.Handler.handle(this, components.get(0), components.get(1), false);
                CellEmitter.center(pos).burst(Speck.factory(Speck.EVOKE), 3);

                if (!Random.PassFail(230 + Dungeon.hero.luck() * 2)) {
                    Dungeon.level.set(pos, Terrain.ALCHEMY_EMPTY, true);
                    Dungeon.level.removeVisuals(pos);
                    GLog.w("This brewing station is used up.");
                }
            }
        }

        return retval;

        /*
		if (items_derp.size() == 2 && items_derp.get(0) instanceof Seed && items_derp.get(1) instanceof Blandfruit ) {

			Sample.INSTANCE.play( Assets.SND_PUFF );
			CellEmitter.center( pos ).burst( Speck.factory( Speck.EVOKE ), 3 );

			Blandfruit result = new Blandfruit();
			result.cook((Seed)items_derp.get(0));

			destroy();

			return result;
		}
		*/

        /*
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

            GLog.d("potion brew");
            potion = new PotionOfWater();
*/
            /*
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
			*/
/*
			//not a buff per-se, meant to cancel out higher potion accuracy when ppl are farming for potions of exp.
			if (bonus > 0)
				if (Random.Int(1000/bonus) == 0)
					return new PotionOfGainLevel();
*/
			/*
			while (potion instanceof PotionOfHealing && Random.Int(10) < Dungeon.limitedDrops.cookingHP.count)
				potion = Generator.random( Generator.Category.POTION );

			if (potion instanceof PotionOfHealing)
				Dungeon.limitedDrops.cookingHP.count++;
			*/
/*
			return potion;

		} else {
			return null;
		}
*/
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

    public static void burnFX(int pos, boolean visible, boolean audible) {
        if (visible) {
            CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
        }
        if (audible) {
            Sample.INSTANCE.play(Assets.SND_BURNING);
        }
    }

    public static void evaporateFX(int pos, boolean visible, boolean audible) {
        if (visible) {
            CellEmitter.get(pos).burst(Speck.factory(Speck.STEAM), 5);
        }
    }

    public void destroy() {
        Dungeon.level.heaps.remove(this.pos);

        if (sprite != null) {
            sprite.kill();
            sprite = null;
        }

        items.clear();
    }

    private static final String POS = "pos";
    private static final String SEEN = "seen";
    private static final String TYPE = "type";
    private static final String ITEMS = "items";
    private static final String ITEMSBURIED = "itemsBuried";

    @SuppressWarnings("unchecked")
    @Override
    public void restoreFromBundle(Bundle bundle) {
        pos = bundle.getInt(POS);
        seen = bundle.getBoolean(SEEN);
        type = Type.valueOf(bundle.getString(TYPE));

        Collection<Item> items = (Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMS));
        items.removeAll(Collections.singleton(null));

        for (Item item : items) {
            collect(item);
        }

        Collection<Item> itemsBuried = (Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMSBURIED));
        itemsBuried.removeAll(Collections.singleton(null));

        for (Item item : itemsBuried) {
            this.itemsBuried.add(item);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(POS, pos);
        bundle.put(SEEN, seen);
        bundle.put(TYPE, type.toString());
        bundle.put(ITEMS, items);
        bundle.put(ITEMSBURIED, itemsBuried);
    }

}
