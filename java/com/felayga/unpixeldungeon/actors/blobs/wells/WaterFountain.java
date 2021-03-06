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

package com.felayga.unpixeldungeon.actors.blobs.wells;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.Journal;
import com.felayga.unpixeldungeon.actors.buffs.Buff;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.effects.Identification;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.levels.Level;
import com.felayga.unpixeldungeon.levels.Terrain;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.felayga.unpixeldungeon.spellcasting.ObjectDetectionSpellcaster;
import com.felayga.unpixeldungeon.spellcasting.Spellcaster;
import com.felayga.unpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

/**
 * Created by HELLO on 6/14/2016.
 */
public class WaterFountain extends WellWater {

    private static final String TXT_PROCCED =
            "As you take a sip, you feel the knowledge pours into your mind. " +
                    "Now you know everything about your equipped items. Also you sense " +
                    "all items on the level and know all its secrets.";

    @Override
    protected boolean affectHero( Hero hero ) {

        Sample.INSTANCE.play( Assets.SND_DRINK );
        emitter.parent.add( new Identification( DungeonTilemap.tileCenterToWorld(pos) ) );

        hero.belongings.observe();

        for (int i=0; i < Level.LENGTH; i++) {

            int terr = Dungeon.level.map(i);
            if ((Terrain.flags[terr] & Terrain.FLAG_SECRET) != 0) {

                Dungeon.level.discover( i );

                if (Dungeon.visible[i]) {
                    GameScene.discoverTile(i, terr);
                }
            }
        }

        Spellcaster.cast(hero, hero.pos(), new ObjectDetectionSpellcaster(), Spellcaster.Origin.Silent);

        Dungeon.hero.interrupt();

        GLog.p(TXT_PROCCED);

        Journal.remove(Journal.Feature.WELL_FOUNTAIN);

        return true;
    }

    @Override
    protected Item affectItem( Item item ) {
        if (item.isIdentified()) {
            return null;
        } else {
            item.identify();
            Badges.validateItemLevelAquired(item);

            emitter.parent.add( new Identification( DungeonTilemap.tileCenterToWorld( pos ) ) );

            Journal.remove( Journal.Feature.WELL_FOUNTAIN );

            return item;
        }
    }

    @Override
    public String tileDesc() {
        return
                "Power of knowledge radiates from the water of this well. " +
                        "Take a sip from it to reveal all secrets of equipped items.";
    }
}
