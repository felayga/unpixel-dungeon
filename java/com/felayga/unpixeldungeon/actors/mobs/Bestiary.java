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
package com.felayga.unpixeldungeon.actors.mobs;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.mobs.bat.Bat;
import com.felayga.unpixeldungeon.actors.mobs.bug.GridBug;
import com.felayga.unpixeldungeon.actors.mobs.dwarf.Hobbit;
import com.felayga.unpixeldungeon.actors.mobs.gnome.GnomeZombie;
import com.felayga.unpixeldungeon.actors.mobs.goo.AcidBlob;
import com.felayga.unpixeldungeon.actors.mobs.jackal.Coyote;
import com.felayga.unpixeldungeon.actors.mobs.jackal.Fox;
import com.felayga.unpixeldungeon.actors.mobs.jackal.Jackal;
import com.felayga.unpixeldungeon.actors.mobs.kobold.Kobold;
import com.felayga.unpixeldungeon.actors.mobs.kobold.KoboldLarge;
import com.felayga.unpixeldungeon.actors.mobs.kobold.KoboldZombie;
import com.felayga.unpixeldungeon.actors.mobs.lichen.BrownMold;
import com.felayga.unpixeldungeon.actors.mobs.lichen.GreenMold;
import com.felayga.unpixeldungeon.actors.mobs.lichen.Lichen;
import com.felayga.unpixeldungeon.actors.mobs.lichen.PurpleMold;
import com.felayga.unpixeldungeon.actors.mobs.lichen.RedMold;
import com.felayga.unpixeldungeon.actors.mobs.lichen.Shrieker;
import com.felayga.unpixeldungeon.actors.mobs.lichen.YellowMold;
import com.felayga.unpixeldungeon.actors.mobs.newt.Gecko;
import com.felayga.unpixeldungeon.actors.mobs.newt.Newt;
import com.felayga.unpixeldungeon.actors.mobs.orc.Goblin;
import com.felayga.unpixeldungeon.actors.mobs.rat.GiantRat;
import com.felayga.unpixeldungeon.actors.mobs.rat.SewerRat;
import com.felayga.unpixeldungeon.actors.mobs.wraith.GasSpore;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

//import com.felayga.unpixeldungeon.actors.mobs.npcs.Ghost;

public class Bestiary {
    public static Mob spawn(int depth, int heroLevel) {
        try {
            MobSpawn spawner = getMobSpawn(depth, heroLevel);
            Class<?> classType = spawner.classType;
            return (Mob) classType.newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static void spawn(int depth, int heroLevel, boolean rares, final SpawnParams params) {
        if (!Dungeon.hero.isAlive()) {
            GLog.d("refused spawn, hero dead");
            return;
        }

        MobSpawn spawner = getMobSpawn(depth, heroLevel);
        Mob mob;

        try {
            Class<?> classType = params.type(spawner.classType);

            if (rares) {
                /*
                if (Random.Int( 30 ) == 0) {
                    if (cl == MarsupialRat.class) {
                        cl = MarsupialRatAlbino.class;
                    } else if (cl == Thief.class) {
                        cl = Bandit.class;
                    } else if (cl == Brute.class) {
                        cl = Shielded.class;
                    } else if (cl == Monk.class) {
                        cl = Senior.class;
                    } else if (cl == Scorpio.class) {
                        cl = Acidic.class;
                    }
                }
                */
            }

            int retries = 5;
            int pos;
            ArrayList<Integer> positions = null;

            int quantity = params.quantity(spawner.quantity);

            while (retries > 0 && positions == null) {
                pos = params.position();
                if (pos >= 0) {
                    final Level level = params.level();
                    positions = level.randomPositionsNear(pos, quantity, new Level.RandomPositionValidator() {
                        @Override
                        public boolean isValidPosition(int pos) {
                            return Level.passable[pos] && (!Level.avoid[pos]) && Actor.findChar(pos) == null && Dungeon.hero.pos() != pos;
                        }
                    });
                }
                retries--;
            }

            if (positions == null && retries <= 0) {
                GLog.d("failed spawning classtype=" + classType.toString() + ", no valid positions");
                return;
            }

            //GLog.d("prepare to spawn classtype=" + classType.toString() + " quantity=" + quantity + " positions=" + positions.size());

            for (int n = 0; n < quantity; n++) {
                mob = (Mob) classType.newInstance();
                mob.pos(positions.get(n));

                params.initialize(mob);
            }

        } catch (Exception e) {
            GLog.d("mob spawning fucked up");
            GLog.d(e);
        }
    }

    public interface SpawnParams {
        Level level();
        int position();

        Class<?> type(Class<?> type);
        int quantity(int quantity);

        void initialize(Mob mob);
    }

    private static class MobSpawnGroup {
        public enum Size {
            Individual,
            Small,
            Large
        }

        private float[] chances;
        private Class<?>[] classes;
        private Size[] sizes;

        public Class<?> classType;
        public Size size;

        private static class Pile {
            public float chance;
            public Class<?> classtype;
            public Size size;

            public Pile(float chance, Class<?> classtype, Size size) {
                this.chance = chance;
                this.classtype = classtype;
                this.size = size;
            }
        }

        private ArrayList<Pile> piles;

        public MobSpawnGroup() {
            piles = new ArrayList<Pile>();
        }

        public void add(float chance, Class<?> classtype, Size size) {
            piles.add(new Pile(chance, classtype, size));
        }

        @SuppressWarnings({"unchecked"})
        public void done() {
            int size = piles.size();
            chances = new float[size];
            //classes = new Class<? extends Mob>[size]; //mmmkay...
            classes = new Class<?>[size];
            sizes = new Size[size];

            for (int n=0;n<size;n++) {
                Pile pile = piles.get(n);

                chances[n] = pile.chance;
                classes[n] = pile.classtype;
                sizes[n] = pile.size;
            }

            piles = null;
        }

        public void pickRandom() {
            if (piles != null) {
                done();
            }

            int index = Random.chances(chances);

            classType = classes[index];
            size = sizes[index];
        }
    }

    private static SparseArray<MobSpawnGroup> spawnGroups = new SparseArray<>();

    static {
        MobSpawnGroup group;

        group = new MobSpawnGroup();
        group.add(1, GridBug.class, MobSpawnGroup.Size.Individual);
        spawnGroups.put(-1, group);

        group = new MobSpawnGroup();
        group.add(3, GridBug.class, MobSpawnGroup.Size.Small);
        group.add(3, Jackal.class, MobSpawnGroup.Size.Small);
        group.add(1, KoboldZombie.class, MobSpawnGroup.Size.Individual);
        group.add(5, Newt.class, MobSpawnGroup.Size.Individual);
        group.add(1, SewerRat.class, MobSpawnGroup.Size.Small);
        group.add(1, Fox.class, MobSpawnGroup.Size.Individual);
        group.add(4, Lichen.class, MobSpawnGroup.Size.Individual);
        group.add(2, Goblin.class, MobSpawnGroup.Size.Individual);
        group.add(1, Kobold.class, MobSpawnGroup.Size.Individual);
        spawnGroups.put(0, group);

        group = new MobSpawnGroup();
        group.add(1, Bat.class, MobSpawnGroup.Size.Small);
        group.add(1, Coyote.class, MobSpawnGroup.Size.Small);
        group.add(5, Gecko.class, MobSpawnGroup.Size.Individual);
        group.add(2, GiantRat.class, MobSpawnGroup.Size.Small);
        group.add(1, GnomeZombie.class, MobSpawnGroup.Size.Individual);
        group.add(2, YellowMold.class, MobSpawnGroup.Size.Individual);
        group.add(2, AcidBlob.class, MobSpawnGroup.Size.Individual);
        group.add(2, BrownMold.class, MobSpawnGroup.Size.Individual);
        group.add(1, GreenMold.class, MobSpawnGroup.Size.Individual);
        group.add(1, RedMold.class, MobSpawnGroup.Size.Individual);
        group.add(2, PurpleMold.class, MobSpawnGroup.Size.Individual);
        group.add(1, GasSpore.class, MobSpawnGroup.Size.Individual);
        group.add(2, Hobbit.class, MobSpawnGroup.Size.Individual);
        group.add(1, KoboldLarge.class, MobSpawnGroup.Size.Individual);
        group.add(1, Shrieker.class, MobSpawnGroup.Size.Individual);
        spawnGroups.put(1, group);
    }

    private static class MobSpawn {
        public Class<?> classType;
        public int quantity;
    }

    private static MobSpawn getMobSpawn(int depth, int heroLevel) {
        int min = depth / 7;
        int max = (depth + heroLevel) / 2;

        if (min > max) {
            int swap = min;
            min = max;
            max = swap;
        }

        int test = Math.max(Random.Int(min, max + 1) - 1, 0);

        MobSpawnGroup group;

        if ((group = spawnGroups.get(test)) == null) {
            group = spawnGroups.get(-1);
        }

        group.pickRandom();

        MobSpawn retval = new MobSpawn();

        retval.classType = group.classType;

        switch(group.size) {
            case Large:
                retval.quantity = Random.IntRange(2, 12);
                break;
            case Small:
                retval.quantity = Random.IntRange(2, 5);
                break;
            default:
                retval.quantity = 1;
                break;
        }

        if (heroLevel <= 4) {
            if (heroLevel <= 2) {
                retval.quantity /= 4;
            }
            else {
                retval.quantity /= 2;
            }

            if (retval.quantity < 1) {
                retval.quantity = 1;
            }
        }

        return retval;
    }

    public static boolean isUnique(Char mob) {
		/*
		return mob instanceof Goo || mob instanceof Tengu || mob instanceof DM300 || mob instanceof King
				|| mob instanceof Yog.BurningFist || mob instanceof Yog.RottingFist
			|| mob instanceof Ghost.MarisupialRatFetid || mob instanceof Ghost.GnollTrickster || mob instanceof Ghost.GreatCrab;
		*/

        return false;
    }
}
