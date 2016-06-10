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
package com.felayga.unpixeldungeon.actors.mobs;

import com.felayga.unpixeldungeon.Dungeon;
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
import com.felayga.unpixeldungeon.actors.mobs.lichen.RedMold;
import com.felayga.unpixeldungeon.actors.mobs.lichen.YellowMold;
import com.felayga.unpixeldungeon.actors.mobs.newt.Gecko;
import com.felayga.unpixeldungeon.actors.mobs.newt.Newt;
//import com.felayga.unpixeldungeon.actors.mobs.npcs.Ghost;
import com.felayga.unpixeldungeon.actors.mobs.orc.Goblin;
import com.felayga.unpixeldungeon.actors.mobs.rat.GiantRat;
import com.felayga.unpixeldungeon.actors.mobs.rat.SewerRat;
import com.felayga.unpixeldungeon.actors.mobs.wraith.GasSpore;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Bestiary {
    public static void spawn(int depth, int heroLevel, boolean rares, SpawnParams params) {
        if (!Dungeon.hero.isAlive()) {
            GLog.d("refused spawn, hero dead");
            return;
        }

        MobSpawn spawner = getMobSpawn(depth, heroLevel);
        Mob mob;

        try {
            Class<?> classType = spawner.classType;

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
            int pos = Constant.POS_NONE;
            ArrayList<Integer> positions = null;

            while (pos == Constant.POS_NONE && retries > 0 && positions == null) {
                pos = params.position();
                if (pos != Constant.POS_NONE) {
                    positions = placeMob(params.level(), spawner.quantity, pos);
                }
                retries--;
            }

            if (positions == null && retries <= 0) {
                GLog.d("failed spawning classtype="+classType.toString()+", no valid positions");
                return;
            }

            GLog.d("prepare to spawn classtype="+classType.toString()+" quantity="+spawner.quantity);

            for (int n=0;n<spawner.quantity;n++) {
                mob = (Mob) classType.newInstance();
                mob.pos = positions.get(n);

                params.initialize(mob);
            }

        } catch (Exception e) {
            GLog.d("mob spawning fucked up");
            GLog.d(e);
        }
    }

    private static ArrayList<Integer> placeMob(Level level, int quantity, int pos) {
        ArrayList<Integer> positionOffsets = new ArrayList<>();

        for (Integer ofs : Level.NEIGHBOURS9) {
            positionOffsets.add(ofs);
        }

        ArrayList<Integer> retval = new ArrayList<>();
        int heroPos = Dungeon.hero.pos;

        int repeatoffset = -1;

        while (true) {
            if (repeatoffset >= 0) {
                if (retval.size() <= 0) {
                    return null;
                }

                pos = retval.get(pos);
            }

            Collections.shuffle(positionOffsets);

            for (Integer ofs : positionOffsets) {
                int cell = pos + ofs;

                if (!Level.insideMap(cell)) {
                    continue;
                }

                GLog.d("mobs="+(level.mobs != null ? "not null":"null"));

                boolean passable = Level.passable[cell] && level.findMob(cell) == null;

                if (passable && Level.distance(heroPos, cell) >= 4) {
                    retval.add(cell);

                    if (retval.size() >= quantity) {
                        Collections.shuffle(retval);
                        return retval;
                    }
                }
            }

            repeatoffset++;
        }
    }

    public static interface SpawnParams {
        Level level();
        int position();
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

        public void done() {
            int size = piles.size();
            chances = new float[size];
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

    private static HashMap<Integer, MobSpawnGroup> spawnGroups = new HashMap<>();

    static {
        MobSpawnGroup group;

        group = new MobSpawnGroup();
        group.add(1.0f, GridBug.class, MobSpawnGroup.Size.Individual);
        spawnGroups.put(-1, group);

        group = new MobSpawnGroup();
        group.add(3.0f, GridBug.class, MobSpawnGroup.Size.Small);
        group.add(3.0f, Jackal.class, MobSpawnGroup.Size.Small);
        group.add(1.0f, KoboldZombie.class, MobSpawnGroup.Size.Individual);
        group.add(5.0f, Newt.class, MobSpawnGroup.Size.Individual);
        group.add(1.0f, SewerRat.class, MobSpawnGroup.Size.Small);
        group.add(1.0f, Fox.class, MobSpawnGroup.Size.Individual);
        group.add(4.0f, Lichen.class, MobSpawnGroup.Size.Individual);
        group.add(2.0f, Goblin.class, MobSpawnGroup.Size.Individual);
        group.add(1.0f, Kobold.class, MobSpawnGroup.Size.Individual);
        spawnGroups.put(0, group);

        group = new MobSpawnGroup();
        group.add(1.0f, Bat.class, MobSpawnGroup.Size.Small);
        group.add(1.0f, Coyote.class, MobSpawnGroup.Size.Small);
        group.add(5.0f, Gecko.class, MobSpawnGroup.Size.Individual);
        group.add(2.0f, GiantRat.class, MobSpawnGroup.Size.Small);
        group.add(1.0f, GnomeZombie.class, MobSpawnGroup.Size.Individual);
        group.add(2.0f, YellowMold.class, MobSpawnGroup.Size.Individual);
        group.add(2.0f, AcidBlob.class, MobSpawnGroup.Size.Individual);
        group.add(2.0f, BrownMold.class, MobSpawnGroup.Size.Individual);
        group.add(1.0f, GreenMold.class, MobSpawnGroup.Size.Individual);
        group.add(1.0f, RedMold.class, MobSpawnGroup.Size.Individual);
        group.add(1.0f, GasSpore.class, MobSpawnGroup.Size.Individual);
        group.add(2.0f, Hobbit.class, MobSpawnGroup.Size.Individual);
        group.add(1.0f, KoboldLarge.class, MobSpawnGroup.Size.Individual);
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

        if (spawnGroups.containsKey(test)) {
            group = spawnGroups.get(test);
        }
        else {
            group = spawnGroups.get(-1);
        }

        group.pickRandom();

        MobSpawn retval = new MobSpawn();

        retval.classType = group.classType;

        switch(group.size) {
            case Large:
                retval.quantity = Random.Int(2, 12);
                break;
            case Small:
                retval.quantity = Random.Int(2, 5);
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
