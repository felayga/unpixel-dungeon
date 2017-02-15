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
package com.felayga.unpixeldungeon.windows.hero;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.Statistics;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.sprites.hero.HeroSprite;
import com.felayga.unpixeldungeon.ui.BuffIndicator;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;
import com.felayga.unpixeldungeon.utils.Utils;
import com.felayga.unpixeldungeon.windows.IconTitle;
import com.felayga.unpixeldungeon.windows.WndCatalogus;
import com.felayga.unpixeldungeon.windows.WndInfoBuff;
import com.felayga.unpixeldungeon.windows.WndTabbed;
import com.felayga.unpixeldungeon.windows.quest.WndJournal;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Button;

import java.util.Locale;

public class WndHero extends WndTabbed {

    private static final String TXT_STATS = "Stats";
    private static final String TXT_BUFFS = "Buffs";

    private static final String TXT_EXP = "Experience";

    private static final String TXT_STRCON = "Vitality";
    private static final String TXT_DEXCHA = "Cunning";
    private static final String TXT_INTWIS = "Sagacity";

    private static final String TXT_HEALTH = "Health";
    private static final String TXT_GOLD = "Gold Collected";
    private static final String TXT_DEPTH = "Maximum Depth";

    private static final int WIDTH = 100;
    private static final int TAB_WIDTH = 40;

    private StatsTab stats;
    private BuffsTab buffs;

    private SmartTexture icons;
    private TextureFilm film;

    public WndHero() {

        super();

        icons = TextureCache.get(Assets.BUFFS_LARGE);
        film = new TextureFilm(icons, 16, 16);

        stats = new StatsTab();
        add(stats);

        buffs = new BuffsTab();
        add(buffs);

        add(new LabeledTab(TXT_STATS) {
            protected void select(boolean value) {
                super.select(value);
                stats.visible = stats.active = selected;
            }

            ;
        });
        add(new LabeledTab(TXT_BUFFS) {
            protected void select(boolean value) {
                super.select(value);
                buffs.visible = buffs.active = selected;
            }

            ;
        });

        resize(WIDTH, (int) Math.max(stats.height(), buffs.height()));

        layoutTabs();

        select(0);
    }

    private class StatsTab extends Group {

        private static final String TXT_TITLE = "Level %d %s";
        private static final String TXT_CATALOGUS = "Catalogus";
        private static final String TXT_JOURNAL = "Journal";

        private static final int GAP = 5;

        private float pos;

        public StatsTab() {
            super(-1);

            Hero hero = Dungeon.hero;

            IconTitle title = new IconTitle();
            title.icon(HeroSprite.avatar(0, 0));
            title.label(Utils.format(TXT_TITLE, hero.level, hero.className()).toUpperCase(Locale.ENGLISH), 9);
            title.color(Window.SHPX_COLOR);
            title.setRect(0, 0, WIDTH, 0);
            add(title);

            RedButton btnCatalogus = new RedButton(TXT_CATALOGUS) {
                @Override
                protected void onClick() {
                    hide();
                    GameScene.show(new WndCatalogus());
                }
            };
            btnCatalogus.setRect(0, title.height(), btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2);
            add(btnCatalogus);

            RedButton btnJournal = new RedButton(TXT_JOURNAL) {
                @Override
                protected void onClick() {
                    hide();
                    GameScene.show(new WndJournal());
                }
            };
            btnJournal.setRect(
                    btnCatalogus.right() + 1, btnCatalogus.top(),
                    btnJournal.reqWidth() + 2, btnJournal.reqHeight() + 2);
            add(btnJournal);

            pos = btnCatalogus.bottom() + GAP;

            statSlot(TXT_STRCON, hero.STRCON());
            statSlot(TXT_DEXCHA, hero.DEXCHA());
            statSlot(TXT_INTWIS, hero.INTWIS());
            statSlot(TXT_HEALTH, hero.HP + "/" + hero.HT);
            statSlot(TXT_EXP, hero.exp + "/" + hero.maxExp());

            pos += GAP;

            statSlot(TXT_GOLD, Statistics.goldCollected);
            statSlot(TXT_DEPTH, "nope");

            pos += GAP;
        }

        private void statSlot(String label, String value) {

            BitmapText txt = PixelScene.createText(label, 8);
            txt.y = pos;
            add(txt);

            txt = PixelScene.createText(value, 8);
            txt.measure();
            txt.x = 65;
            txt.y = pos;
            add(txt);

            pos += GAP + txt.baseLine();
        }

        private void statSlot(String label, int value) {
            statSlot(label, Integer.toString(value));
        }

        public float height() {
            return pos;
        }
    }

    private class BuffsTab extends Group {

        private static final int GAP = 2;

        private float pos;

        public BuffsTab() {
            super(-1);

            for (Buff buff : Dungeon.hero.buffs()) {
                if (buff.icon() != BuffIndicator.NONE) {
                    BuffSlot slot = new BuffSlot(buff);
                    slot.setRect(0, pos, WIDTH, slot.icon.height());
                    add(slot);
                    pos += GAP + slot.height();
                }
            }
        }

        public float height() {
            return pos;
        }

        private class BuffSlot extends Button {

            private Buff buff;

            Image icon;
            BitmapText txt;

            public BuffSlot(Buff buff) {
                super();
                this.buff = buff;
                int index = buff.icon();

                icon = new Image(icons);
                icon.frame(film.get(index));
                icon.y = this.y;
                add(icon);

                txt = PixelScene.createText(buff.toString(), 8);
                txt.x = icon.width + GAP;
                txt.y = icon.y + (int) (icon.height - txt.baseLine()) / 2;
                add(txt);

            }

            @Override
            protected void layout() {
                super.layout();
                icon.y = this.y;
                txt.x = icon.width + GAP;
                txt.y = icon.y + (int) (icon.height - txt.baseLine()) / 2;
            }

            @Override
            protected void onClick() {
                GameScene.show(new WndInfoBuff(buff));
            }
        }
    }
}
