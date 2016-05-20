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
package com.felayga.unpixeldungeon.scenes;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.items.Generator;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.windows.WndError;
import com.felayga.unpixeldungeon.windows.WndStory;
import com.felayga.unpixeldungeon.windows.hero.WndInitHero;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InterlevelScene extends PixelScene {

	private static final float TIME_TO_FADE = 0.3f;
	
	private static final String TXT_DESCENDING	= "Descending...";
	private static final String TXT_ASCENDING	= "Ascending...";
	private static final String TXT_LOADING		= "Loading...";
	private static final String TXT_RESURRECTING= "Resurrecting...";
	private static final String TXT_RETURNING	= "Returning...";
	private static final String TXT_FALLING		= "Falling...";
	private static final String TXT_RESETTING   = "Resetting...";
	private static final String TXT_TELEPORTING	= "Teleporting...";
	
	private static final String ERR_FILE_NOT_FOUND	= "Save file not found. If this error persists after restarting, " +
														"it may mean this save game is corrupted. Sorry about that.";
	private static final String ERR_IO			    = "Cannot read save file. If this error persists after restarting, " +
														"it may mean this save game is corrupted. Sorry about that.";
	
	public static enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, TELEPORT
	};
	public static Mode mode;
	
	public static int returnDepth;
	public static int returnPos;

	public static int teleportDepth;
	public static int teleportPos;
	
	public static boolean noStory = false;

	public static boolean fallIntoPit;
	
	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	};
	private Phase phase;
	private float timeLeft;
	
	private BitmapText message;
	
	private Thread thread;
	private Exception error = null;
	
	@Override
	public void create() {
		super.create();

		String text = "";
		switch (mode) {
			case DESCEND:
				text = TXT_DESCENDING;
				break;
			case ASCEND:
				text = TXT_ASCENDING;
				break;
			case CONTINUE:
				text = TXT_LOADING;
				break;
			case RESURRECT:
				text = TXT_RESURRECTING;
				break;
			case RETURN:
				text = TXT_RETURNING;
				break;
			case FALL:
				text = TXT_FALLING;
				break;
			case RESET:
				text = TXT_RESETTING;
				break;
			case TELEPORT:
				text = TXT_TELEPORTING;
				break;
		}
		
		message = PixelScene.createText( text, 9 );
		message.measure();
		message.x = (Camera.main.width - message.width()) / 2;
		message.y = (Camera.main.height - message.height()) / 2;
		add( message );
		
		phase = Phase.FADE_IN;
		timeLeft = TIME_TO_FADE;

		thread = new Thread() {
			@Override
			public void run() {
				
				try {
					
					Generator.reset();

					switch (mode) {
						case DESCEND:
							descend();
							break;
						case ASCEND:
							ascend();
							break;
						case CONTINUE:
							restore();
							break;
						case RESURRECT:
							resurrect();
							break;
						case RETURN:
							returnTo();
							break;
						case FALL:
							fall();
							break;
						case RESET:
							reset();
							break;
						case TELEPORT:
							teleport();
							break;
					}
					
					if ((Dungeon.depth % 5) == 0) {
						Sample.INSTANCE.load( Assets.SND_BOSS );
					}
					
				} catch (Exception e) {
					
					error = e;

				}

				if (phase == Phase.STATIC && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE;
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = timeLeft / TIME_TO_FADE;
		
		switch (phase) {
		
		case FADE_IN:
			message.alpha( 1 - p );
			if ((timeLeft -= Game.elapsed) <= 0) {
				if (!thread.isAlive() && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE;
				} else {
					phase = Phase.STATIC;
				}
			}
			break;
			
		case FADE_OUT:
			message.alpha( p );

			if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
				Music.INSTANCE.volume( p * (ShatteredPixelDungeon.musicVol()/10f));
			}
			if ((timeLeft -= Game.elapsed) <= 0) {
				Game.switchScene( GameScene.class );
			}
			break;
			
		case STATIC:
			if (error != null) {
				String errorMsg;
				if (error instanceof FileNotFoundException) errorMsg = ERR_FILE_NOT_FOUND;
				else if (error instanceof IOException) errorMsg = ERR_IO;

				else throw new RuntimeException("fatal error occured while moving between floors", error);

				add( new WndError( errorMsg ) {
					public void onBackPressed() {
						super.onBackPressed();
						Game.switchScene( StartScene.class );
					};
				} );
				error = null;
			}
			break;
		}
	}

	private void descend() throws IOException {
		GameTime.fix();
		if (Dungeon.hero == null) {
			Dungeon.init();
			if (noStory) {
				Dungeon.chapters.add( WndStory.ID_SEWERS );
				noStory = false;
			}
		} else {
			Dungeon.saveLevel();
		}

		Level level;
		if (Statistics.floorsVisited[Dungeon.depth + 1]) {
			Dungeon.depth++;
			level = Dungeon.loadLevel(WndInitHero.savedGameIndex);
		} else {
			level = Dungeon.newLevel();
		}
		Dungeon.switchLevel( level, level.entrance );
	}
	
	private void fall() throws IOException {
		GameTime.fix();
		Dungeon.saveLevel();

		Level level;
		if (Statistics.floorsVisited[Dungeon.depth + 1]) {
			Dungeon.depth++;
			level = Dungeon.loadLevel(WndInitHero.savedGameIndex);
		} else {
			level = Dungeon.newLevel();
		}
		Dungeon.switchLevel( level, fallIntoPit ? level.pitCell() : level.randomRespawnCell() );
	}
	
	private void ascend() throws IOException {
		GameTime.fix();
		
		Dungeon.saveLevel();
		Dungeon.depth--;

		Level level = null;
		if (Statistics.floorsVisited[Dungeon.depth]) {
			level = Dungeon.loadLevel( WndInitHero.savedGameIndex );
		}
		else {
			Dungeon.depth--;
			level = Dungeon.newLevel();
		}

		Dungeon.switchLevel( level, level.exit );
	}
	
	private void returnTo() throws IOException {
		GameTime.fix();
		
		Dungeon.saveLevel();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel( WndInitHero.savedGameIndex );
		Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(returnPos) : returnPos);
	}
	
	private void restore() throws IOException {
		GameTime.fix();

		Dungeon.loadGame(WndInitHero.savedGameIndex);
		if (Dungeon.depth == -1) {
			Dungeon.depth = 1;
			for (int n = Dungeon.HIGHESTLEVEL - 1; n >= 1; n--)
			{
				if (Statistics.floorsVisited[n])
				{
					Dungeon.depth = n;
					break;
				}
			}
			Dungeon.switchLevel( Dungeon.loadLevel( WndInitHero.savedGameIndex ), Constant.POS_EXIT);
		} else {
			Level level = Dungeon.loadLevel( WndInitHero.savedGameIndex );
			Dungeon.switchLevel( level, Level.resizingNeeded ? level.adjustPos( Dungeon.hero.pos ) : Dungeon.hero.pos );
		}
	}
	
	private void resurrect() throws IOException {
		GameTime.fix();
		
		if (Dungeon.level.locked) {
			Dungeon.hero.resurrect( Dungeon.depth );
			Dungeon.depth--;
			Level level = Dungeon.newLevel();
			Dungeon.switchLevel( level, level.entrance );
		} else {
			Dungeon.hero.resurrect( -1 );
			Dungeon.resetLevel();
		}
	}

	private void reset() throws IOException {
		GameTime.fix();

		Dungeon.depth--;
		Level level = Dungeon.newLevel();
		Dungeon.switchLevel(level, level.entrance);
	}

	private void teleport() throws IOException {
		GameTime.fix();

		Level level = null;

		if (Statistics.floorsVisited[teleportDepth]) {
			Dungeon.depth = teleportDepth;
			level = Dungeon.loadLevel( WndInitHero.savedGameIndex );
		}
		else {
			Dungeon.depth = teleportDepth-1;
			level = Dungeon.newLevel();
		}

		Dungeon.switchLevel( level, teleportPos );
	}
	
	@Override
	protected void onBackPressed() {
		//Do nothing
	}
}
