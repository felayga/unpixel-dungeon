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
package com.felayga.unpixeldungeon.ui;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.effects.Speck;
import com.felayga.unpixeldungeon.effects.particles.BloodParticle;
import com.felayga.unpixeldungeon.levels.branches.DungeonBranch;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.ui.tag.DangerIndicator;
import com.felayga.unpixeldungeon.windows.WndGame;
import com.felayga.unpixeldungeon.windows.hero.WndHero;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

public class StatusPane extends Component {

    private NinePatch shield;
    private Image avatar;
    private Emitter blood;

    private int lastTier = 0;

    private Image hp;
    private Image mp;
    private Image exp;

    private BossHealthBar bossHP;

    private int lastLvl = -1;

    private BitmapText level;
    private BitmapText depth;

    private DangerIndicator danger;
    private BuffIndicator buffs;
    private Compass compass;

    private MenuButton btnMenu;

    @Override
    protected void createChildren() {

        shield = new NinePatch(Assets.STATUS, 80, 0, 30 + 18, 0);
        add(shield);

        add(new TouchArea(0, 1, 31, 31) {
            @Override
            protected void onClick(Touch touch) {
                Image sprite = Dungeon.hero.sprite;
                if (!sprite.isVisible()) {
                    Camera.main.focusOn(sprite);
                }
                GameScene.show(new WndHero());
            }

            ;
        });

        btnMenu = new MenuButton();
        add(btnMenu);

        avatar = HeroSprite.avatar(0, lastTier);
        add(avatar);

        blood = new Emitter();
        blood.pos(avatar);
        blood.pour(BloodParticle.FACTORY, 0.3f);
        blood.autoKill = false;
        blood.on = false;
        add(blood);

        compass = new Compass(Dungeon.level.exit);
        add(compass);

        hp = new Image(Assets.HP_BAR);
        add(hp);

        mp = new Image(Assets.MP_BAR);
        add(mp);

        exp = new Image(Assets.XP_BAR);
        add(exp);

        bossHP = new BossHealthBar();
        add(bossHP);

        level = new BitmapText(PixelScene.pixelFont);
        level.hardlight(0xFFEBA4);
        add(level);

        depth = new BitmapText(DungeonBranch.getDepthText(Dungeon.depth()), PixelScene.pixelFont);
        depth.hardlight(0xCACFC2);
        depth.measure();
        add(depth);

        /*
		Dungeon.hero.belongings.countIronKeys();
		keys = new BitmapText( PixelScene.pixelFont);
		keys.hardlight( 0xCACFC2 );
		add( keys );
		*/

        danger = new DangerIndicator();
        add(danger);

        buffs = new BuffIndicator(Dungeon.hero);
        add(buffs);
    }

    @Override
    protected void layout() {

        height = 32;

        shield.size(width, shield.height);

        avatar.x = shield.x + 15 - avatar.width / 2;
        avatar.y = shield.y + 16 - avatar.height / 2;

        compass.x = avatar.x + avatar.width / 2 - compass.origin.x;
        compass.y = avatar.y + avatar.height / 2 - compass.origin.y;

        hp.x = 30;
        hp.y = 3;

        mp.x = 30;
        mp.y = 8;

        bossHP.setPos(6 + (width - bossHP.width()) / 2, 20);

        depth.x = width - 24 - depth.width() - 18;
        depth.y = 6;

        danger.setPos(width - danger.width(), 20);

        buffs.setPos(30, 11);

        btnMenu.setPos(width - btnMenu.width(), 1);
    }

    @Override
    public void update() {
        super.update();

        float health = (float) Dungeon.hero.HP / Dungeon.hero.HT;
        float mana = (float) Dungeon.hero.MP / Dungeon.hero.MT;

        if (health == 0) {
            avatar.tint(0x000000, 0.6f);
            blood.on = false;
        } else if (health < 0.25f) {
            avatar.tint(0xcc0000, 0.4f);
            blood.on = true;
        } else {
            avatar.resetColor();
            blood.on = false;
        }

        hp.scale(health, hp.scale().y);
        mp.scale(mana, mp.scale().y);
        exp.scale((width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp(), exp.scale().y);

        if (Dungeon.hero.level != lastLvl) {

            if (lastLvl != -1) {
                Emitter emitter = (Emitter) recycle(Emitter.class);
                emitter.revive();
                emitter.pos(-1, 27, 27);
                emitter.burst(Speck.factory(Speck.STAR), 12);
            }

            lastLvl = Dungeon.hero.level;
            level.text(Integer.toString(lastLvl));
            level.measure();
            level.x = 27.0f - level.width() / 2;
            level.y = 27.5f - level.baseLine() / 2;
        }

		/*
		int tier = Dungeon.hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy( HeroSprite.avatar( Dungeon.hero.heroClass, tier ) );
		}
		*/
    }

    private static class MenuButton extends Button {

        private Image image;

        public MenuButton() {
            super();

            width = image.width + 4;
            height = image.height + 4;
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            image = new Image(Assets.STATUS, 114, 3, 12, 11);
            add(image);
        }

        @Override
        protected void layout() {
            super.layout();

            image.x = x + 2;
            image.y = y + 2;
        }

        @Override
        protected void onTouchDown() {
            image.brightness(1.5f);
            Sample.INSTANCE.play(Assets.SND_CLICK);
        }

        @Override
        protected void onTouchUp() {
            image.resetColor();
        }

        @Override
        protected void onClick() {
            GameScene.show(new WndGame());
        }
    }
}
