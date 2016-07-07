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
package com.felayga.unpixeldungeon.items.scrolls;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.buffs.positive.Invisibility;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.branches.DungeonBranch;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.scenes.InterlevelScene;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Random;

//import com.felayga.unpixeldungeon.items.artifacts.DriedRose;

public class ScrollOfTeleportation extends PositionScroll {
	public static final String TXT_TELEPORTED = "In a blink of an eye you were teleported to another location of the level.";
	public static final String TXT_NO_TELEPORT = "A mysterious force prevents you from teleporting.";
	public static final String TXT_MISSED_TELEPORT = "You feel disoriented for a moment.";

    public ScrollOfTeleportation()
	{
		name = "Scroll of Teleportation";
		initials = "TP";

        price = 40;
	}

    @Override
	protected void doRead() {
        super.doRead();
		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();

		curUser.spend_new(TIME_TO_READ, true);

		if (canTeleport(curUser)) {
			switch (bucStatus) {
				case Cursed:
					if (Random.Int(5) != 0) {
						doLevelPort(curUser);
					}
					else {
						GLog.w(TXT_MISSED_TELEPORT);
					}
					break;
				case Uncursed:
					doTeleport(curUser, Constant.Position.RANDOM);
					break;
				case Blessed:
                    doSelectCell(curUser);
					break;
			}
		}
	}

	public static boolean canTeleport(Char user) {
		if ((Level.flags & Level.FLAG_NOTELEPORTATION) != 0)
		{
			GLog.w(TXT_NO_TELEPORT);
			return false;
		}

		return true;
	}

    public static int doLevelPort(Char user) {
        if (user instanceof Hero) {
            int target = Dungeon.depth();

            DungeonBranch branch = DungeonBranch.currentBranch(Dungeon.depth());

            while (target == Dungeon.depth() || branch != DungeonBranch.currentBranch(target)) {
                target = Random.IntRange(branch.levelMin, Dungeon.depth() + 3);
            }

            GLog.d("cursed levelport to " + target);

            Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();

            /*
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                if (mob instanceof DriedRose.GhostHero) mob.destroy();
            */

            InterlevelScene.mode = InterlevelScene.Mode.TELEPORT;
            InterlevelScene.teleportDepth = target;
            InterlevelScene.teleportPos = Constant.Position.RANDOM;
            Game.switchScene(InterlevelScene.class);

            return target;
        } else {
            GLog.d("levelport for non-hero");
            //todo: implement non-hero levelport
            user.destroy();
            user.sprite.killAndErase();
            Dungeon.level.mobs.remove(user);
            return -1;
        }
    }

    @Override
    protected void onPositionSelected(Hero user, int position) {
        super.onPositionSelected(user, position);

        if (position == user.pos) {
            position = Constant.Position.RANDOM;
        }

        doTeleport(user, position);
    }

	public static void doTeleport( Char user, int pos ) {
		int count = 10;
		switch(pos) {
            case Constant.Position.ENTRANCE:
                pos = Dungeon.level.entrance;
                break;
            case Constant.Position.ENTRANCE_ALTERNATE:
                pos = Dungeon.level.entranceAlternate;
                break;
            case Constant.Position.EXIT:
                pos = Dungeon.level.exit;
                break;
            case Constant.Position.EXIT_ALTERNATE:
                pos = Dungeon.level.exitAlternate;
                break;
            case Constant.Position.RANDOM:
                do {
                    pos = Dungeon.level.randomRespawnCell();
                    if (count-- <= 0) {
                        break;
                    }
                } while (pos < 0);
                break;
            case Constant.Position.NONE:
                pos = -1;
                break;
        }

		if (pos >= 0) {
			appear(user, pos);
			Dungeon.level.press(pos, user);

			if (user instanceof Hero) {
				Dungeon.observe();
			}

			GLog.i(TXT_TELEPORTED);
		} else {
			GLog.w(TXT_MISSED_TELEPORT);
		}
	}

	public static void appear( Char ch, int pos ) {
		ch.sprite.interruptMotion();

		ch.move( pos );
		ch.sprite.place( pos );

		if (ch.invisible == 0) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}

		ch.sprite.emitter().start( Speck.factory(Speck.LIGHT), 0.2f, 3 );
		Sample.INSTANCE.play( Assets.SND_TELEPORT );
	}
	
	@Override
	public String desc() {
		return
			"The spell on this parchment instantly transports the reader " +
			"to a random location on the dungeon level. It can be used " +
			"to escape a dangerous situation, but the unlucky reader might " +
			"find himself in an even more dangerous place.";
	}

}
