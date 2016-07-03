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
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.GamesInProgress;
import com.felayga.unpixeldungeon.ShatteredPixelDungeon;
import com.felayga.unpixeldungeon.actors.hero.HeroClass;
import com.felayga.unpixeldungeon.effects.BannerSprites;
import com.felayga.unpixeldungeon.effects.BannerSprites.Type;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.levels.branches.DungeonBranch;
import com.felayga.unpixeldungeon.ui.Archs;
import com.felayga.unpixeldungeon.ui.ExitButton;
import com.felayga.unpixeldungeon.ui.Icons;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.windows.WndChallenges;
import com.felayga.unpixeldungeon.windows.WndMessage;
import com.felayga.unpixeldungeon.windows.WndOptions;
import com.felayga.unpixeldungeon.windows.hero.WndInitHero;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.util.HashMap;

public class StartScene extends PixelScene {

	private static final float BUTTON_HEIGHT	= 24;
	private static final float GAP				= 2;

	private static final String TXT_LOAD	= "Load Game";
	private static final String TXT_NEW		= "New Game";

	private static final String TXT_ERASE		= "Erases Progress";
	private static final String TXT_DPTH_LVL	= "Depth: %d, Level: %d";

	private static final String TXT_REALLY	= "Do you really want to start new game?";
	private static final String TXT_WARNING	= "Your current game progress will be erased.";
	private static final String TXT_YES		= "Yes, start new game";
	private static final String TXT_NO		= "No, return to main menu";

	private static final String TXT_UNLOCK	= "To unlock this character class, slay the 3rd boss with any other class";

	private static final String TXT_WIN_THE_GAME =
			"To unlock \"Challenges\", win the game with any character class.";

	private static final float WIDTH_P    = 116;
	private static final float HEIGHT_P    = 220;

	private static final float WIDTH_L    = 224;
	private static final float HEIGHT_L    = 124;

	private static HashMap<Integer, ClassShield> shields = new HashMap<Integer, ClassShield>();

	private float buttonX;
	private float buttonY;

	private GameButton btnLoad;
	private GameButton btnNewGame;

	private static int curIndex;

	@Override
	public void create() {
		super.create();

		Badges.loadGlobal();

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		float width, height;
		if (ShatteredPixelDungeon.landscape()) {
			width = WIDTH_L;
			height = HEIGHT_L;
		} else {
			width = WIDTH_P;
			height = HEIGHT_P;
		}

		float left = (w - width) / 2;
		float top = (h - height) / 2;
		float bottom = h - top;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Image title = BannerSprites.get( Type.SELECT_YOUR_HERO );
		title.x = (w - title.width()) / 2;
		title.y = top;
		add( title );

		buttonX = left;
		buttonY = bottom - BUTTON_HEIGHT;

		btnNewGame = new GameButton( TXT_NEW, true ) {
			@Override
			protected void onClick() {
				GamesInProgress.Info info = GamesInProgress.check(curIndex);
				if (info != null && !info.dead) {
					StartScene.this.add( new WndOptions( TXT_REALLY, TXT_WARNING, TXT_YES, TXT_NO ) {
						@Override
						protected void onSelect( int index ) {
							if (index == 0) {
								startNewGame();
							}
						}
					} );
				} else {
					startNewGame();
				}
			}
		};
		add( btnNewGame );

		btnLoad = new GameButton( TXT_LOAD, true ) {
			@Override
			protected void onClick() {
				GamesInProgress.Info info = GamesInProgress.check(curIndex);

				if (info != null) {
					info.toWndHeroInit();
				}
				else {
					WndInitHero.setDefault();
				}

				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				Game.switchScene( InterlevelScene.class );
			}
		};
		add( btnLoad );

		float centralHeight = buttonY - title.y - title.height();

		HeroClass[] classes = {
				HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS, HeroClass.DEBUG
		};
		for (int n=0;n<GamesInProgress.MAXIMUM;n++) {
			ClassShield shield = new ClassShield( n );
			shields.put( n, shield );
			add( shield );
		}
		if (ShatteredPixelDungeon.landscape()) {
			float shieldW = width / 4;
			float shieldH = Math.min( centralHeight, shieldW );
			top = title.y + title.height + (centralHeight - shieldH) / 2;
			for (int n=0; n < GamesInProgress.MAXIMUM; n++) {
				ClassShield shield = shields.get( n );
				shield.setRect( left + n * shieldW, top, shieldW, shieldH );
			}

			/*
			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos(
					w/2 - challenge.width()/2,
					top + shieldH/2 - challenge.height()/2 );
			add( challenge );
			*/
		} else {
			float shieldW = width / 2;
			float shieldH = Math.min( centralHeight / 2, shieldW * 1.2f );
			top = title.y + title.height() + centralHeight / 2 - shieldH;
			for (int n=0; n < GamesInProgress.MAXIMUM; n++) {
				ClassShield shield = shields.get( n );
				shield.setRect(
						left + (n % 2) * shieldW,
						top + (n / 2) * shieldH,
						shieldW, shieldH );
			}

			/*
			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos(
					w/2 - challenge.width()/2,
					top + shieldH - challenge.height()/2 );
			add( challenge );
			*/
		}

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(Camera.main.width - btnExit.width(), 0);
		add(btnExit);

		curIndex = -1;
		updateClass(0);
		//todo: fart
		//updateClass( HeroClass.values()[ShatteredPixelDungeon.lastClass()] );

		fadeIn();

		Badges.loadingListener = new Callback() {
			@Override
			public void call() {
				if (Game.scene() == StartScene.this) {
					ShatteredPixelDungeon.switchNoFade( StartScene.class );
				}
			}
		};
	}

	@Override
	public void destroy() {

		Badges.saveGlobal();
		Badges.loadingListener = null;

		super.destroy();

	}

	private void updateClass( int index ) {

		if (curIndex == index) {
			//todo: fart
			//add(new WndClass(index));
			return;
		}

		if (curIndex >= 0) {
			shields.get(curIndex).highlight(false);
		}

		curIndex = index;
		WndInitHero.savedGameIndex = curIndex;

		shields.get(curIndex).highlight(true);

		GamesInProgress.Info info = GamesInProgress.check(index);
		if (info != null && !info.dead) {
			btnLoad.visible = true;
			//todo: fart
			//btnLoad.secondary(Utils.format(TXT_DPTH_LVL, info.depth, info.level), info.challenges);
			btnLoad.secondary("", false);

			btnNewGame.visible = true;
			btnNewGame.secondary(TXT_ERASE, false);

			float w = (Camera.main.width - GAP) / 2 - buttonX;

			btnLoad.setRect(
					buttonX, buttonY, w, BUTTON_HEIGHT);
			btnNewGame.setRect(
					btnLoad.right() + GAP, buttonY, w, BUTTON_HEIGHT);

		} else {
			btnLoad.visible = false;

			btnNewGame.visible = true;
			btnNewGame.secondary(null, false);
			btnNewGame.setRect(buttonX, buttonY, Camera.main.width - buttonX * 2, BUTTON_HEIGHT);
		}
	}

	private void startNewGame() {
		Dungeon.hero = null;
		GamesInProgress.Info info = GamesInProgress.check(curIndex);

		if (info != null) {
			info.toWndHeroInit();
		}
		else {
			WndInitHero.setDefault();
		}

		ShatteredPixelDungeon.scene().add(new WndInitHero(new WndInitHero.Listener() {
			@Override
			public void onReady() {
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

				if (ShatteredPixelDungeon.intro()) {
					ShatteredPixelDungeon.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					Game.switchScene( InterlevelScene.class );
				}
			}
		}));
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade( TitleScene.class );
	}

	private static class GameButton extends RedButton {

		private static final int SECONDARY_COLOR_N    = 0xCACFC2;
		private static final int SECONDARY_COLOR_H    = 0xFFFF88;

		private BitmapText secondary;

		public GameButton( String primary, boolean clickSound ) {
			super( primary, clickSound );

			this.secondary.text( null );
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			secondary = createText( 6 );
			add( secondary );
		}

		@Override
		protected void layout() {
			super.layout();

			if (secondary.text().length() > 0) {
				text.y = y + (height - text.height() - secondary.baseLine()) / 2;

				secondary.x = x + (width - secondary.width()) / 2;
				secondary.y = text.y + text.height();
			} else {
				text.y = y + (height - text.baseLine()) / 2;
			}
		}

		public void secondary( String text, boolean highlighted ) {
			secondary.text( text );
			secondary.measure();

			secondary.hardlight( highlighted ? SECONDARY_COLOR_H : SECONDARY_COLOR_N );
		}
	}

	private class ClassShield extends RedButton {

		private static final float MIN_BRIGHTNESS	= 0.6f;

		private static final int BASIC_NORMAL        = 0x444444;
		private static final int BASIC_HIGHLIGHTED    = 0xCACFC2;

		private static final int WIDTH	= 24;
		private static final int HEIGHT	= 32;
		private static final float SCALE	= 1.75f;

		private int index;

		private Emitter emitter;

		private int normal;
		private int highlighted;

		BitmapTextMultiline txtInfo;

		public ClassShield( int index ) {
			super("Game " + index, true);
			this.index = index;

			//resize( WIDTH, (int)(txtInfo.y + txtInfo.height()) );
			GamesInProgress.Info info = GamesInProgress.check(index);

			if (info != null) {
				txtInfo.text("Level " + info.level + "\n" +
								(info.gender == 0 ? "Male" : "Female") + " " + HeroClass.toHeroClass(info.heroClass).toString().toLowerCase() + "\n" +
								"Depth " + DungeonBranch.getDepthText(info.depth)
				);
			}
			else {
				txtInfo.text("<no saved game found>");
			}

			normal = BASIC_NORMAL;
			highlighted = BASIC_HIGHLIGHTED;

			updateBrightness();
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			emitter = new Emitter();
			add( emitter );

			txtInfo = PixelScene.createMultiline( "", 6 );
			txtInfo.maxWidth = WIDTH;
			txtInfo.x = this.left();
			txtInfo.y = this.bottom() + GAP;
			txtInfo.measure();
			add(txtInfo);
		}

		@Override
		protected void layout() {
			super.layout();
			text.y = y + GAP;

			emitter.pos(-1, x, y, width(), height());
			txtInfo.point(new PointF(x + GAP, y + GAP * 2 + text.height()));
			txtInfo.maxWidth = (int)(width - GAP * 2);
			txtInfo.measure();
		}

		@Override
		protected void onTouchDown() {

			emitter.revive();
			emitter.start(Speck.factory(Speck.LIGHT), 0.05f, 7);

			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 1.2f );
			updateClass(index);
		}

		private boolean isHighlighted = false;

		public void highlight( boolean value ) {
			isHighlighted = value;

			updateBrightness();
		}

		private void updateBrightness() {
			if (isHighlighted)
			{
				super.textColor(highlighted);
			}
			else {
				super.textColor(normal);
			}
		}
	}

	private class ChallengeButton extends Button {

		private Image image;

		public ChallengeButton() {
			super();

			width = image.width;
			height = image.height;

			image.am = Badges.isUnlocked( Badges.Badge.VICTORY ) ? 1.0f : 0.5f;
		}

		@Override
		protected void createChildren() {

			super.createChildren();

			image = Icons.get( ShatteredPixelDungeon.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF );
			add( image );
		}

		@Override
		protected void layout() {

			super.layout();

			image.x = x;
			image.y = y;
		}

		@Override
		protected void onClick() {
			if (Badges.isUnlocked( Badges.Badge.VICTORY )) {
				StartScene.this.add(new WndChallenges(ShatteredPixelDungeon.challenges(), true) {
					public void onBackPressed() {
						super.onBackPressed();
						image.copy( Icons.get( ShatteredPixelDungeon.challenges() > 0 ?
								Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF ) );
					};
				} );
			} else {
				StartScene.this.add( new WndMessage( TXT_WIN_THE_GAME ) );
			}
		}

		@Override
		protected void onTouchDown() {
			Sample.INSTANCE.play( Assets.SND_CLICK );
		}
	}
}
