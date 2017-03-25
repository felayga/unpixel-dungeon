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

package com.felayga.unpixeldungeon.sprites.mobs;

import com.felayga.unpixeldungeon.DungeonTilemap;
import com.felayga.unpixeldungeon.actors.mobs.Mob;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.sprites.MirrorSprite;
import com.felayga.unpixeldungeon.sprites.SheepSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bat.BatSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bat.GiantBatSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bat.VampireBatSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bug.CentipedeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bug.FireAntSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bug.GiantAntSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bug.GridBugSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bug.SoldierAntSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bug.SpiderBrownSprite;
import com.felayga.unpixeldungeon.sprites.mobs.bug.SpiderGraySprite;
import com.felayga.unpixeldungeon.sprites.mobs.foocubus.FireNymphSprite;
import com.felayga.unpixeldungeon.sprites.mobs.foocubus.ForestNymphSprite;
import com.felayga.unpixeldungeon.sprites.mobs.foocubus.IncubusSprite;
import com.felayga.unpixeldungeon.sprites.mobs.foocubus.SuccubusSprite;
import com.felayga.unpixeldungeon.sprites.mobs.foocubus.WaterNymphSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.BrownMoldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.GlassPiercerSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.GreenMoldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.IronPiercerSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.LichenSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.PurpleMoldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.RedMoldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.RockPiercerSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.ShriekerSprite;
import com.felayga.unpixeldungeon.sprites.mobs.golem.PaperGolemSprite;
import com.felayga.unpixeldungeon.sprites.mobs.golem.StrawGolemSprite;
import com.felayga.unpixeldungeon.sprites.mobs.goo.AcidBlobSprite;
import com.felayga.unpixeldungeon.sprites.mobs.goo.BlackGooSprite;
import com.felayga.unpixeldungeon.sprites.mobs.goo.GrayOozeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.dwarf.DwarfLordSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.dwarf.DwarfMasterSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.dwarf.DwarfSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.dwarf.DwarfZombieSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.dwarf.HobbitSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.elf.ElfGreySprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.elf.ElfMummySprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.elf.ElfSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.elf.ElfZombieSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.gnome.GnomeKingSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.gnome.GnomeLordSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.gnome.GnomeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.gnome.GnomeWizardSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.gnome.GnomeZombieSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.gnome.HomunculusSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.BugbearSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.GoblinSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.HillOrcSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.HobgoblinSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.ManbearpigSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.MordorOrcSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.OrcShamanSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.OrcSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.OrcZombieSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.orc.UrukhaiOrcSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.shopkeeper.HumanSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.shopkeeper.Nihilist2Sprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.shopkeeper.NihilistSprite;
import com.felayga.unpixeldungeon.sprites.mobs.jackal.CoyoteSprite;
import com.felayga.unpixeldungeon.sprites.mobs.jackal.FoxSprite;
import com.felayga.unpixeldungeon.sprites.mobs.jackal.JackalSprite;
import com.felayga.unpixeldungeon.sprites.mobs.kobold.KoboldLargeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.kobold.KoboldLordSprite;
import com.felayga.unpixeldungeon.sprites.mobs.kobold.KoboldMummySprite;
import com.felayga.unpixeldungeon.sprites.mobs.kobold.KoboldShamanSprite;
import com.felayga.unpixeldungeon.sprites.mobs.kobold.KoboldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.kobold.KoboldZombieSprite;
import com.felayga.unpixeldungeon.sprites.mobs.monkey.ApeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.newt.CrocodileBabySprite;
import com.felayga.unpixeldungeon.sprites.mobs.newt.GeckoSprite;
import com.felayga.unpixeldungeon.sprites.mobs.newt.IguanaSprite;
import com.felayga.unpixeldungeon.sprites.mobs.newt.NewtSprite;
import com.felayga.unpixeldungeon.sprites.mobs.rat.FerretSprite;
import com.felayga.unpixeldungeon.sprites.mobs.rat.GiantRatSprite;
import com.felayga.unpixeldungeon.sprites.mobs.rat.RabidRatSprite;
import com.felayga.unpixeldungeon.sprites.mobs.rat.RatSprite;
import com.felayga.unpixeldungeon.sprites.mobs.rat.RockMoleSprite;
import com.felayga.unpixeldungeon.sprites.mobs.rothe.RotheSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.CobraSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.GarterSnakeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.NagaHatchlingBlackSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.NagaHatchlingBlueSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.NagaHatchlingGreenSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.NagaHatchlingRedSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.NagaHatchlingYellowSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.PitViperSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.PythonSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.SnakeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.snake.WaterMoccasinSprite;
import com.felayga.unpixeldungeon.sprites.mobs.unused.SpinachSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.VioletFungusSprite;
import com.felayga.unpixeldungeon.sprites.mobs.fungus.YellowMoldSprite;
import com.felayga.unpixeldungeon.sprites.mobs.humanoid.shopkeeper.ShopkeeperSprite;
import com.felayga.unpixeldungeon.sprites.mobs.rat.RatKingSprite;
import com.felayga.unpixeldungeon.sprites.mobs.werejackal.WerejackalSprite;
import com.felayga.unpixeldungeon.sprites.mobs.werejackal.WereratSprite;
import com.felayga.unpixeldungeon.sprites.mobs.wraith.FloatingEyeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.wraith.GasSporeSprite;
import com.felayga.unpixeldungeon.sprites.mobs.wraith.ManesSprite;
import com.felayga.unpixeldungeon.sprites.mobs.wraith.WraithSprite;
import com.felayga.unpixeldungeon.sprites.npcs.BlacksmithSprite;
import com.felayga.unpixeldungeon.sprites.npcs.BoulderSprite;
import com.felayga.unpixeldungeon.sprites.npcs.ImpSprite;
import com.felayga.unpixeldungeon.sprites.npcs.WandmakerSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.HashMap;

public class MobSprite extends CharSprite {
    public enum Hallucination {
        None(null, null, false),

        Bat("bat", BatSprite.class),
        GiantBat("giant bat", GiantBatSprite.class),
        VampireBat("vampire bat", VampireBatSprite.class),

        Centipede("centipede", CentipedeSprite.class),
        FireAnt("fire ant", FireAntSprite.class),
        GiantAnt("giant ant", GiantAntSprite.class),
        GridBug("grid bug", GridBugSprite.class),
        SoldierAnt("soldier ant", SoldierAntSprite.class),
        SpiderBrown("cave spider", SpiderBrownSprite.class),
        SpiderGray("giant spider", SpiderGraySprite.class),

        FireNymph("fire nymph", FireNymphSprite.class),
        ForestNymph("forest nymph", ForestNymphSprite.class),
        Incubus("incubus", IncubusSprite.class),
        Succubus("succubus", SuccubusSprite.class),
        WaterNymph("water nymph", WaterNymphSprite.class),

        BrownMold("brown mold", BrownMoldSprite.class),
        GlassPiercer("glass piercer", GlassPiercerSprite.class),
        GreenMold("green mold", GreenMoldSprite.class),
        IronPiercer("iron piercer", IronPiercerSprite.class),
        Lichen("lichen", LichenSprite.class),
        PurpleMold("purple mold", PurpleMoldSprite.class),
        RedMold("red mold", RedMoldSprite.class),
        RockPiercer("rock piercer", RockPiercerSprite.class),
        Shrieker("shrieker", ShriekerSprite.class),
        VioletFungus("violet fungus", VioletFungusSprite.class),
        YellowMold("yellow mold", YellowMoldSprite.class),

        PaperGolem("paper golem", PaperGolemSprite.class),
        StrawGolem("straw golem", StrawGolemSprite.class),

        AcidBlob("acid blob", AcidBlobSprite.class),
        BlackGoo("black goo", BlackGooSprite.class),
        GrayOoze("gray ooze", GrayOozeSprite.class),


        DwarfLord("dwarf lord", DwarfLordSprite.class),
        DwarfMaster("dwarf master", DwarfMasterSprite.class),
        Dwarf("dwarf", DwarfSprite.class),
        DwarfZombie("dwarf zombie", DwarfZombieSprite.class),
        Hobbit("hobbit", HobbitSprite.class),

        ElfGrey("grey elf", ElfGreySprite.class),
        ElfMummy("elf mummy", ElfMummySprite.class),
        Elf("elf", ElfSprite.class),
        ElfZombie("elf zombie", ElfZombieSprite.class),

        GnomeKing("gnome king", GnomeKingSprite.class),
        GnomeLord("gnome lord", GnomeLordSprite.class),
        Gnome("gnome", GnomeSprite.class),
        GnomeWizard("gnome wizard", GnomeWizardSprite.class),
        GnomeZombie("gnome zombie", GnomeZombieSprite.class),
        Homunculus("homunculus", HomunculusSprite.class),

        Bugbear("bugbear", BugbearSprite.class),
        Goblin("goblin", GoblinSprite.class),
        HillOrc("hill orc", HillOrcSprite.class),
        Hobgoblin("hobgoblin", HobgoblinSprite.class),
        Manbearpig("MANBEARPIG", ManbearpigSprite.class),
        MordorOrc("Mordor orc", MordorOrcSprite.class),
        OrcShaman("orc shaman", OrcShamanSprite.class),
        Orc("orc", OrcSprite.class),
        OrcZombie("orc zombie", OrcZombieSprite.class),
        UrukhaiOrc("Uruk-hai", UrukhaiOrcSprite.class),

        Human("human", HumanSprite.class),
        Nihilist2("nihilist", Nihilist2Sprite.class),
        Nihilist("nihilist", NihilistSprite.class),
        Shopkeeper("shopkeeper", ShopkeeperSprite.class, false),


        Coyote("coyote", CoyoteSprite.class),
        Fox("fox", FoxSprite.class),
        Jackal("jackal", JackalSprite.class),

        KoboldLarge("large kobold", KoboldLargeSprite.class),
        KoboldLord("kobold lord", KoboldLordSprite.class),
        KoboldMummy("kobold mummy", KoboldMummySprite.class),
        KoboldShaman("kobold shaman", KoboldShamanSprite.class),
        Kobold("kobold", KoboldSprite.class),
        KoboldZombie("kobold zombie", KoboldZombieSprite.class),

        Ape("ape", ApeSprite.class),

        CrocodileBaby("baby crocodile", CrocodileBabySprite.class),
        Gecko("gecko", GeckoSprite.class),
        Iguana("iguana", IguanaSprite.class),
        Newt("newt", NewtSprite.class),

        Ferret("ferret", FerretSprite.class),
        GiantRat("giant rat", GiantRatSprite.class),
        RabidRat("rabid rat", RabidRatSprite.class),
        RatKing("rat king", RatKingSprite.class, false),
        Rat("sewer rat", RatSprite.class),
        RockMole("rock mole", RockMoleSprite.class),

        Rothe("rothe", RotheSprite.class),

        Cobra("cobra", CobraSprite.class),
        GarterSnake("garter snake", GarterSnakeSprite.class),
        NagaHatchlingBlack("black naga hatchling", NagaHatchlingBlackSprite.class),
        NagaHatchlingBlue("blue naga hatchling", NagaHatchlingBlueSprite.class),
        NagaHatchlingGreen("green naga hatchling", NagaHatchlingGreenSprite.class),
        NagaHatchlingRed("red naga hatchling", NagaHatchlingRedSprite.class),
        NagaHatchlingYellow("yellow naga hatchling", NagaHatchlingYellowSprite.class),
        PitViper("pit viper", PitViperSprite.class),
        Python("python", PythonSprite.class),
        Snake("snake", SnakeSprite.class),
        WaterMoccasin("water moccasin", WaterMoccasinSprite.class),

        Werejackal("werejackal", WerejackalSprite.class),
        Wererat("wererat", WereratSprite.class),

        FloatingEye("floating eye", FloatingEyeSprite.class),
        GasSpore("gas spore", GasSporeSprite.class),
        Manes("manes", ManesSprite.class),
        Wraith("wraith", WraithSprite.class),

        TrollBlacksmith("troll blacksmith", BlacksmithSprite.class, false),
        Boulder("boulder", BoulderSprite.class, false),
        ImpShopkeeper("ambitious imp", ImpSprite.class, false),
        MirrorImage("mirror image", MirrorSprite.class, false),
        MagicSheep("sheep", SheepSprite.class, false),
        Spinach("spinach", SpinachSprite.class, false),
        OldWandmaker("old wandmaker", WandmakerSprite.class, false);


        public final String name;
        public final Class<? extends MobSprite> type;

        Hallucination(String name, Class<? extends MobSprite> type) {
            this(name, type, true);
        }

        Hallucination(String name, Class<? extends MobSprite> type, boolean hallucination) {
            this.name = name;
            this.type = type;
        }

        public static Hallucination getRandom() {
            Hallucination[] values = Hallucination.values();
            return values[Random.IntRange(1, values.length - 1)];
        }

        public MobSprite getSprite() {
            try {
                return type.newInstance();
            } catch (Exception e) {
                return null;
            }
        }

        private static HashMap<Class<? extends MobSprite>, Hallucination> fromType;

        public static Hallucination getHallucination(Class<? extends MobSprite> type) {
            if (fromType == null) {
                fromType = new HashMap<>();

                Hallucination[] values = Hallucination.values();
                for (int n = 0; n < values.length; n++) {
                    fromType.put(values[n].type, values[n]);
                }
            }

            return fromType.get(type);
        }

        public static String getName(Class<? extends MobSprite> type) {
            return getHallucination(type).name;
        }
    }

	private static final float FADE_TIME	= 3f;
	private static final float FALL_TIME	= 1f;
	
	@Override
	public void update() {
		sleeping = ch != null && ((Mob)ch).state == ((Mob)ch).SLEEPING;
		super.update();
	}
	
	@Override
	public void onComplete( Animation anim ) {
		
		super.onComplete( anim );
		
		if (anim == die) {
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					MobSprite.this.killAndErase();
					parent.erase( this );
				};
			} );
		}
	}
	
	public void fall() {
		origin.set( width / 2, height - DungeonTilemap.SIZE / 2 );
		angularSpeed = Random.Int( 2 ) == 0 ? -720 : 720;
		
		parent.add( new ScaleTweener( this, new PointF( 0, 0 ), FALL_TIME ) {
			@Override
			protected void onComplete() {
				MobSprite.this.killAndErase();
				parent.erase( this );
			};
			@Override
			protected void updateValues( float progress ) {
				super.updateValues( progress );
				am = 1 - progress;
			}
		} );
	}
}
