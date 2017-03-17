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

package com.felayga.unpixeldungeon.ui.tag;

import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.hero.HeroAction;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.Constant;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.actions.CloseDoorSprite;
import com.felayga.unpixeldungeon.sprites.actions.DownStairsSprite;
import com.felayga.unpixeldungeon.sprites.actions.ReadSignSprite;
import com.felayga.unpixeldungeon.sprites.actions.UpStairsSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

import java.util.ArrayList;

/**
 * Created by HELLO on 2/7/2017.
 */
public class ActionIndicator extends Tag {

    private static final float ENABLED = 1.0f;
    private static final float DISABLED = 0.3f;

    private static float delay = 0.75f;

    private static ActionIndicator instance;

    private CharSprite sprite = null;

    private int lastTargetPos = Constant.Position.NONE;
    private int lastTargetTerrain;

    public ActionIndicator() {
        super(DangerIndicator.COLOR);

        instance = this;

        setSize(24, 24);
        visible(false);
        enable(false);
    }

    @Override
    protected void createChildren() {
        super.createChildren();
    }

    @Override
    protected void layout() {
        super.layout();

        if (sprite != null) {
            sprite.x = x + (width - sprite.width()) / 2;
            sprite.y = y + (height - sprite.height()) / 2;
        }
    }

    @Override
    public void update() {
        super.update();

        if (!bg.visible) {
            enable(false);
            if (delay > 0f) delay -= Game.elapsed;
            if (delay <= 0f) active = false;
        } else {
            delay = 0.75f;
            active = true;

            if (Dungeon.hero.isAlive()) {

                enable(Dungeon.hero.ready);

            } else {
                visible(false);
                enable(false);
            }
        }
    }

    private void checkActions() {
        lastTargetPos = Constant.Position.NONE;

        int heroPos = Dungeon.hero.pos();

        switch (Dungeon.level.map(heroPos)) {
            case Terrain.STAIRS_UP:
            case Terrain.STAIRS_UP_ALTERNATE:
            case Terrain.STAIRS_DOWN:
            case Terrain.STAIRS_DOWN_ALTERNATE:
            case Terrain.SIGN:
                lastTargetPos = heroPos;
                lastTargetTerrain = Dungeon.level.map(heroPos);
                break;
            default:
                for (Integer offset : Level.NEIGHBOURS4) {
                    int pos = heroPos + offset;

                    if (Dungeon.level.map(pos) == Terrain.OPEN_DOOR && Actor.findChar(pos) == null && Dungeon.level.heaps.get(pos) == null) {
                        lastTargetPos = pos;
                        lastTargetTerrain = Dungeon.level.map(pos);
                        break;
                    }
                }
                break;
        }

        if (lastTargetPos != Constant.Position.NONE) {
            updateImage();
            visible(true);
        } else {
            visible(false);
        }

        enable(bg.visible);
    }

    private HeroAction actionOverride;

    private void updateImage() {
        if (sprite != null) {
            sprite.killAndErase();
            sprite = null;
        }

        actionOverride = null;

        try {
            switch (lastTargetTerrain) {
                case Terrain.OPEN_DOOR:
                    sprite = new CloseDoorSprite();
                    actionOverride = new HeroAction.HandleDoor(lastTargetPos);
                    break;
                case Terrain.STAIRS_UP:
                    sprite = new UpStairsSprite();
                    actionOverride = new HeroAction.MoveLevel(lastTargetPos, -1, false);
                    break;
                case Terrain.STAIRS_UP_ALTERNATE:
                    sprite = new UpStairsSprite();
                    actionOverride = new HeroAction.MoveLevel(lastTargetPos, -1, true);
                    break;
                case Terrain.STAIRS_DOWN:
                    sprite = new DownStairsSprite();
                    actionOverride = new HeroAction.MoveLevel(lastTargetPos, 1, false);
                    break;
                case Terrain.STAIRS_DOWN_ALTERNATE:
                    sprite = new DownStairsSprite();
                    actionOverride = new HeroAction.MoveLevel(lastTargetPos, 1, true);
                    break;
                case Terrain.SIGN:
                    sprite = new ReadSignSprite();
                    actionOverride = new HeroAction.ReadSign(lastTargetPos);
                    break;
            }

            active = true;
            sprite.idle();
            sprite.paused = false;
            add(sprite);

            sprite.x = x + (width - sprite.width()) / 2 + 1;
            sprite.y = y + (height - sprite.height()) / 2;

        } catch (Exception e) {
            GLog.d("updateImage failure in ActionIndicator");
            GLog.d(e);
        }
    }

    private boolean enabled = true;

    private void enable(boolean value) {
        enabled = value;
        if (sprite != null) {
            sprite.alpha(value ? ENABLED : DISABLED);
        }
    }

    private void visible(boolean value) {
        bg.visible = value;
        if (sprite != null) {
            sprite.visible = value;
        }
    }

    @Override
    protected void onClick() {
        if (enabled) {
            if (Dungeon.hero.handle(lastTargetPos, actionOverride)) {
                Dungeon.hero.next();
            }
        }
    }

    public static void updateState() {
        instance.checkActions();
    }
}

