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

package com.felayga.unpixeldungeon.items.wands;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.ResultDescriptions;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.ConfusionGas;
import com.felayga.unpixeldungeon.actors.blobs.Fire;
import com.felayga.unpixeldungeon.actors.blobs.ParalyticGas;
import com.felayga.unpixeldungeon.actors.blobs.Regrowth;
import com.felayga.unpixeldungeon.actors.blobs.ToxicGas;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.negative.Burning;
import com.felayga.unpixeldungeon.actors.buffs.negative.Frost;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.mobs.npcs.Sheep;
import com.felayga.unpixeldungeon.effects.Beam;
import com.felayga.unpixeldungeon.effects.CellEmitter;
import com.felayga.unpixeldungeon.effects.Flare;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.SpellSprite;
import com.felayga.unpixeldungeon.effects.particles.PurpleParticle;
import com.felayga.unpixeldungeon.effects.particles.ShadowParticle;
import com.felayga.unpixeldungeon.items.Bomb;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.felayga.unpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.levels.traps.LightningTrap;
import com.felayga.unpixeldungeon.levels.traps.SummoningTrap;
import com.felayga.unpixeldungeon.mechanics.BUCStatus;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.mechanics.MagicType;
import com.felayga.unpixeldungeon.plants.Plant;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.ui.HealthIndicator;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.io.IOException;

/**
 * Created by HELLO on 3/3/2017.
 */
public class WandOfChaos extends Wand {
    public WandOfChaos() {
        super(8);

        name = "Wand of Chaos";

        collisionProperties = Ballistica.Mode.MagicRay;
        price = 500;
    }

    @Override
    public int randomCharges() {
        return Random.IntRange(4, 8);
    }

    private int rarityIndex;
    private int effectIndex;

    @Override
    protected void initiateZap(Hero hero) {
        randomizeEffect();

        super.initiateZap(hero);
    }

    private boolean randomizeEffect() {
        effectIndex = Random.Int(4);

        switch(Random.chances(new float[]{COMMON_CHANCE, UNCOMMON_CHANCE, RARE_CHANCE, VERY_RARE_CHANCE})) {
            default:
                rarityIndex = 0;

                switch(effectIndex) {
                    default:
                        directionalZap = true;
                        break;
                    case 1:
                        directionalZap = true;
                        break;
                    case 2:
                        directionalZap = false;
                        break;
                    case 3:
                        directionalZap = false;
                        break;
                }
                break;
            case 1:
                rarityIndex = 1;

                switch(effectIndex) {
                    default:
                        directionalZap = true;
                        break;
                    case 1:
                        directionalZap = true;
                        break;
                    case 2:
                        directionalZap = true;
                        break;
                    case 3:
                        directionalZap = false;
                        break;
                }
                break;
            case 2:
                rarityIndex = 2;

                switch(effectIndex) {
                    default:
                        directionalZap = true;
                        break;
                    case 1:
                        directionalZap = false;
                        break;
                    case 2:
                        directionalZap = false;
                        break;
                    case 3:
                        directionalZap = false;
                        break;
                }
                break;
            case 3:
                rarityIndex = 3;

                switch(effectIndex) {
                    default:
                        directionalZap = false;
                        break;
                    case 1:
                        directionalZap = true;
                        break;
                    case 2:
                        directionalZap = false;
                        break;
                    case 3:
                        directionalZap = false;
                        break;
                }
                break;
        }

        return directionalZap;
    }

    @Override
    protected void onZap(Ballistica beam) {
        if (beam != null) {
            onZap(beam.collisionPos, rarityIndex, effectIndex);
        } else {
            onZap(curUser.pos(), rarityIndex, effectIndex);
        }
    }

    private void onZap(int pos, int rarityIndex, int effectIndex) {
        switch (rarityIndex) {
            default:
                commonEffect(pos, effectIndex);
                break;
            case 1:
                uncommonEffect(pos, effectIndex);
                break;
            case 2:
                rareEffect(pos, effectIndex);
                break;
            case 3:
                veryRareEffect(pos, effectIndex);
                break;
        }
    }

    /*
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//less likely Grim proc
		if (Random.Int(3) == 0)
			new Death().proc( staff, attacker, defender, damage);
	}
	*/

    @Override
    protected void fxEffect(int start, int end, Callback callback) {
        MagicMissile.rainbow(curUser.sprite.parent, start, end, callback);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0x220022);
        particle.am = 0.6f;
        particle.setLifespan(0.6f);
        particle.acc.set(40, -40);
        particle.setSize(0f, 3f);
        particle.shuffleXY(2f);
    }

    @Override
    public void explode(Char user) {
        super.explode(user);

        int maxDamage = curCharges() * 16;

        for (Integer offset : Level.NEIGHBOURS8) {
            int pos = user.pos() + offset;
            Char target = Dungeon.level.findMob(pos);

            if (target == null) {
                while (!randomizeEffect()) ;
                onZap(pos, rarityIndex, effectIndex);
                continue;
            }

            explode(target, maxDamage);
        }

        explode(user, maxDamage);
    }

    public void explode(Char target, int maxDamage) {
        int damage = Random.IntRange(1, maxDamage);

        target.damage(damage, MagicType.Magic, curUser, null);

        while (!randomizeEffect()) ;
        onZap(target.pos(), rarityIndex, effectIndex);
    }

    @Override
    public String desc() {
        return
                "This wand is made from a solid smooth chunk of obsidian, with a deep purple light running up its side, " +
                        "ending at the tip. It glows with destructive energy, waiting to shoot forward.\n\n" +
                        "This wand shoots a beam that pierces any obstacle, and will go farther the more it is upgraded.\n\n" +
                        "This wand deals bonus damage the more enemies and terrain it penetrates.";
    }


    private static float COMMON_CHANCE = 0.6f;
    private static float UNCOMMON_CHANCE = 0.3f;
    private static float RARE_CHANCE = 0.09f;
    private static float VERY_RARE_CHANCE = 0.01f;

    private static void commonEffect(int targetPos, int effectIndex) {
        Char user = curUser;
        switch (effectIndex) {
            //anti-entropy
            case 0: // RANGED
                Char target = Actor.findChar(targetPos);
                switch (Random.Int(2)) {
                    case 0:
                        if (target != null)
                            Buff.affect(target, user, Burning.class).reignite(target);
                        Buff.affect(user, user, Frost.class, Frost.duration(user) * Random.Long(GameTime.TICK * 3, GameTime.TICK * 5) / GameTime.TICK);
                        break;
                    default:
                        Buff.affect(user, user, Burning.class).reignite(user);
                        if (target != null)
                            Buff.affect(target, user, Frost.class, Frost.duration(target) * Random.Long(GameTime.TICK * 3, GameTime.TICK * 5) / GameTime.TICK);
                        break;
                }
                break;

            //spawns some regrowth
            case 1: // RANGED
                int c = Dungeon.level.map[targetPos];
                if (c == Terrain.EMPTY ||
                        c == Terrain.EMBERS ||
                        c == Terrain.EMPTY_DECO ||
                        c == Terrain.GRASS ||
                        c == Terrain.HIGH_GRASS) {
                    GameScene.add(Blob.seed(user, targetPos, 30, Regrowth.class));
                }
                break;

            //random teleportation
            case 2: // SELF
                switch (Random.Int(2)) {
                    case 0:
                        if (ScrollOfTeleportation.canTeleport(user)) {
                            ScrollOfTeleportation.doTeleport(user, Constant.Position.RANDOM);
                        }
                        break;
                    case 1:
                        Char ch = Actor.findChar(targetPos);
                        if (ch != null) {
                            int count = 10;
                            int pos;
                            do {
                                pos = Dungeon.level.randomRespawnCell();
                                if (count-- <= 0) {
                                    break;
                                }
                            } while (pos == -1);
                            if (pos == -1 || !ScrollOfTeleportation.canTeleport(user)) {
                                //empty, canTeleport produces failure message
                            } else {
                                ch.pos(pos);
                                ch.sprite.place(ch.pos());
                                ch.sprite.visible = Dungeon.visible[pos];
                            }
                        }
                        break;
                }
                break;

            //random gas at location
            default: // SELF
                switch (Random.Int(3)) {
                    case 0:
                        GameScene.add(Blob.seed(user, targetPos, 800, ConfusionGas.class));
                        break;
                    case 1:
                        GameScene.add(Blob.seed(user, targetPos, 500, ToxicGas.class));
                        break;
                    default:
                        GameScene.add(Blob.seed(user, targetPos, 200, ParalyticGas.class));
                        break;
                }
                break;
        }

    }

    private static void uncommonEffect(int targetPos, int effectIndex) {
        Char user = curUser;
        Item wand = curItem;
        switch (effectIndex) {
            //Random plant
            case 0: // RANGED
                int pos = targetPos;
                for (Integer offset : Level.NEIGHBOURS4) {
                    int subPos = pos + offset;

                    if (Level.passable[subPos] && Dungeon.level.traps.get(subPos) == null) {
                        Dungeon.level.plant(user, (Plant.Seed) Generator.random(Generator.Category.SEED), subPos);
                    }
                }
                break;

            //Bomb explosion
            case 1: // RANGED
                new Bomb().explode(user, targetPos);
                break;

            //Health transfer
            case 2: // RANGED
                final Char target = Actor.findChar(targetPos);
                if (target != null) {
                    int damage = user.level * 2;
                    switch (Random.Int(2)) {
                        case 0:
                            user.HP = Math.min(user.HT, user.HP + damage);
                            user.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
                            target.damage(damage, MagicType.Magic, curUser, null);
                            target.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                            break;
                        default:
                            user.damage(damage, MagicType.Magic, curUser, null);
                            user.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                            target.HP = Math.min(target.HT, target.HP + damage);
                            target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
                            Sample.INSTANCE.play(Assets.SND_CURSED);
                            if (!user.isAlive()) {
                                Dungeon.fail(Utils.format(ResultDescriptions.ITEM, wand.getDisplayName()));
                                GLog.n("You were killed by your own " + wand.getDisplayName());
                            }
                            break;
                    }
                } else {
                    GLog.i("nothing happens");
                }
                break;

            //shock and recharge
            case 3: // SELF
                new LightningTrap().set(user, user.pos()).activate();
                Buff.prolong(user, user, ScrollOfRecharging.Recharging.class, GameTime.TICK * 20);
                ScrollOfRecharging.charge(user);
                SpellSprite.show(user, SpellSprite.CHARGE);
                break;
        }

    }

    private static void rareEffect(int targetPos, int effectIndex) {
        Char user = curUser;

        switch (effectIndex) {
            //sheep transformation
            case 0: // RANGED
                Char ch = Actor.findChar(targetPos);
                //TODO: this is lazy, should think of a better way to ID bosses, or have this effect be more sophisticated.
                if (ch != null && ch != user) {
                    Sheep sheep = new Sheep();
                    sheep.lifespan = GameTime.TICK * 10;
                    sheep.pos(ch.pos());
                    ch.destroy(user);
                    ch.sprite.killAndErase();
                    Dungeon.level.mobs.remove(ch);
                    HealthIndicator.instance.target(null);
                    GameScene.add(sheep);
                    CellEmitter.get(sheep.pos()).burst(Speck.factory(Speck.WOOL), 4);
                } else {
                    GLog.i("nothing happens");
                }
                break;

            //curses!
            case 1: // SELF
                user.belongings.bucChange(true, BUCStatus.Cursed, true, true, true, false);

                //EquippableItem.equipCursed(user);
                GLog.n("Your worn equipment becomes cursed!");
                break;

            //inter-level teleportation
            case 2: // SELF
                if (ScrollOfTeleportation.canTeleport(user)) {
                    ScrollOfTeleportation.doLevelPort(user);
                }
                break;

            //summon monsters
            case 3: // SELF
                new SummoningTrap().set(user, user.pos()).activate();
                break;
        }
    }

    private static void veryRareEffect(int targetPos, int effectIndex) {
        Char user = curUser;
        Item wand = curItem;

        switch (effectIndex) {
            //great forest fire!
            case 0: // SELF
                for (int i = 0; i < Level.LENGTH; i++) {
                    int c = Dungeon.level.map[i];
                    if (c == Terrain.EMPTY ||
                            c == Terrain.EMBERS ||
                            c == Terrain.EMPTY_DECO ||
                            c == Terrain.GRASS ||
                            c == Terrain.HIGH_GRASS) {
                        GameScene.add(Blob.seed(user, i, 15, Regrowth.class));
                    }
                }
                do {
                    GameScene.add(Blob.seed(user, Dungeon.level.randomDestination(), 10, Fire.class));
                } while (Random.Int(5) != 0);
                new Flare(8, 32).color(0xFFFF66, true).show(user.sprite, 2f);
                Sample.INSTANCE.play(Assets.SND_TELEPORT);
                GLog.p("grass explodes around you!");
                GLog.w("you smell burning...");
                break;

            //superpowered mimic
            case 1: // RANGED
                /*
                Mimic mimic = Mimic.spawnAt(bolt.collisionPos, new ArrayList<Item>());
                mimic.adjustStats(Dungeon.depth + 10);
                mimic.HP = mimic.HT;
                Item reward;
                do {
                    reward = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
                            Generator.Category.RING, Generator.Category.WAND));
                } while (reward.level < 2 && !(reward instanceof MissileWeapon));
                Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1, 0.5f);
                mimic.items.clear();
                mimic.items.add(reward);
                */
                break;

            //crashes the game, yes, really.
            case 2: // SELF
                try {
                    Dungeon.saveAll();
                    GameScene.show(
                            new WndOptions("CURSED WAND ERROR", "this application will now self-destruct", "abort", "retry", "fail") {
                                @Override
                                public void hide() {
                                    throw new RuntimeException("critical wand exception");
                                }
                            }
                    );
                } catch (IOException e) {
                    //oookay maybe don't kill the game if the save failed.
                    GLog.i("nothing happens");
                }
                break;

            //random transmogrification
            case 3: // SELF
                user.belongings.remove(wand, 1);
                Item result;
                do {
                    result = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
                            Generator.Category.RING, Generator.Category.ARTIFACT));
                } while (result.level() < 0 && !(result instanceof MissileWeapon));
                if (result.isUpgradable()) result.upgrade(null, 1);
                result.bucStatus(BUCStatus.Cursed, true);
                GLog.w("your wand transmogrifies into a different item!");
                Dungeon.level.drop(result, user.pos()).sprite.drop();
                break;
        }
    }
}
