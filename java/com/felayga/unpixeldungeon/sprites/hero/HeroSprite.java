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
package com.felayga.unpixeldungeon.sprites.hero;

import android.graphics.RectF;

import com.felayga.unpixeldungeon.Assets;
import com.felayga.unpixeldungeon.Dungeon;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.hero.Hero;
import com.felayga.unpixeldungeon.items.equippableitem.EquippableItem;
import com.felayga.unpixeldungeon.items.equippableitem.armor.Armor;
import com.felayga.unpixeldungeon.sprites.CharSprite;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.hero.WndInitHero;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class HeroSprite extends CharSprite {
	private static final int FRAME_WIDTH	= 13;
	public static final int FRAME_HEIGHT	= 15;

	public static final int RUN_FRAMERATE	= 20;

	private static final int HEROINDEX_DEAD	= 2;

	private static TextureFilm tiers;

	private Animation fly;

	private HeroSubSprite armorTexture;
	private HeroSubSprite hairTexture;
	private HeroSubSprite hairFaceTexture;
    private HeroSubSprite cloakTexture;

	private int heroGender = HEROINDEX_DEAD;
	private ArmorIndex armorIndex = ArmorIndex.NakedMale;
    private ArmorIndex cloakIndex = ArmorIndex.CloakNone;
	private int hairIndex = 0;
	private int hairFaceIndex = 0;

	private float rHair;
	private float gHair;
	private float bHair;

	public HeroSprite(Hero hero) {
		this(hero, true);
	}

	public HeroSprite(Hero hero, boolean link) {
		super();

		if (link) {
			link(hero);
		}

		armorTexture = new HeroSubSprite(Assets.HERO_ARMOR);
		hairTexture = new HeroSubSprite(Assets.HERO_HAIR);
		hairFaceTexture = new HeroSubSprite(Assets.HERO_HAIRFACE);
        cloakTexture = new HeroSubSprite(Assets.HERO_CLOAK);
		texture(Assets.HERO_HEAD);

		updateGender();

		if (ch == null || ch.isAlive()) {
			idle();
		}
		else {
			die();
		}
	}

	////////////////////////////////////////////////////////////////////////////////

	@Override
	public void link(Char ch) {
        super.link(ch);

        if (ch == Dungeon.hero) {
            Hero hero = (Hero) ch;

            heroGender = WndInitHero.genderSelected;
            hairIndex = WndInitHero.hairSelected;
            hairFaceIndex = WndInitHero.hairFaceSelected;
            setHairColor(WndInitHero.hairColorSelected);

            EquippableItem test = hero.belongings.armor();
            if (test != null) {
                Armor armor = (Armor) test;
                armorIndex = armor.spriteTextureIndex;
            } else {
                armorIndex = ArmorIndex.fromGender(heroGender);
            }

            test = hero.belongings.cloak();
            if (test != null) {
                Armor cloak = (Armor) test;
                cloakIndex = cloak.spriteTextureIndex;
            } else {
                cloakIndex = ArmorIndex.CloakNone;
            }
        }
    }


	@Override
	public void draw() {
		updateMatrix();

		NoosaScript script = NoosaScript.get();

		texture.bind();

		script.camera(camera());

		script.uModel.valueM4(matrix);
		script.lighting(
				rm, gm, bm, am,
				ra, ga, ba, aa);

		if (dirty) {
			verticesBuffer.position(0);
			verticesBuffer.put(vertices);

			armorTexture.refreshBuffer();
            cloakTexture.refreshBuffer();
			hairTexture.refreshBuffer();
			hairFaceTexture.refreshBuffer();

			dirty = false;
		}
		script.drawQuad(verticesBuffer);

		if (heroGender != HEROINDEX_DEAD) {
			armorTexture.draw(script, matrix, rm, gm, bm, am, ra, ga, ba, aa);
            if (!cloakIndex.hooded) {
                hairTexture.draw(script, matrix, rHair, gHair, bHair, am, ra, ga, ba, aa);
            }
            cloakTexture.draw(script, matrix, rm, gm, bm, am, ra, ga, ba, aa);
			hairFaceTexture.draw(script, matrix, rHair, gHair, bHair, am, ra, ga, ba, aa);
		}
	}

	@Override
	protected void updateFrame() {
		super.updateFrame();

		armorTexture.updateFrame(armorIndex.value, frame, flipHorizontal, flipVertical);
        cloakTexture.updateFrame(cloakIndex.value, frame, flipHorizontal, flipVertical);
		hairTexture.updateFrame(hairIndex, frame, flipHorizontal, flipVertical);
		hairFaceTexture.updateFrame(hairFaceIndex, frame, flipHorizontal, flipVertical);
	}

	@Override
	protected void updateVertices() {
		super.updateVertices();

		armorTexture.updateVertices(width, height);
        cloakTexture.updateVertices(width, height);
		hairTexture.updateVertices(width, height);
		hairFaceTexture.updateVertices(width, height);
	}

	/////////////////////////////////////////////////////////////////////////////

	public void setAppearance(int heroGender, ArmorIndex armorIndex, ArmorIndex cloakIndex, int hairIndex, int hairFaceIndex, int hairColorIndex) {
		setGender(heroGender);
		setArmor(armorIndex);
        setCloak(cloakIndex);
		setHair(hairIndex);
		setHairFace(hairFaceIndex);
		setHairColor(hairColorIndex);
	}

	public void setGender(int heroGender) {
		if (this.heroGender == heroGender) {
			return;
		}

		this.heroGender = heroGender;

		updateGender();
	}

    public enum ArmorIndex {
        Naked(-1, true, false),
        NakedMale(0, true, false),
        NakedFemale(1, true, false),
        Cloth(2, false, false),
        Leather(3, false, false),
        Chainmail(4, false, false),
        Scale(5, false, false),
        HalfPlate(6, false, false),
        FullPlate(7, false, false),
        StuddedLeather(8, false, false),
        Hide(9, false, false),
        Banded(10, false, false),
        ElvishChainmail(11, false, false),
        OrcishChainmail(12, false, false),
        Ringmail(13, false, false),

        CloakNone(0, false, false),
        CloakLeather(1, false, true),
        CloakDwarvish(2, false, true),
        CloakOrcish(3, false, false),
        CloakElvish(4, false, true),
        CloakApron(5, false, false),
        CloakOilskin(6, false, true),
        CloakMummyWrapping(7, false, true),
        CloakWizardRobe(8, false, false),
        CloakPlain(9, false, true),
        CloakOpera(10, false, false),
        CloakFancy(11, false, true),
        CloakTattered(12, false, false);


        public final int value;
        public final boolean naked;
        public final boolean hooded;

        ArmorIndex(int value, boolean naked, boolean hooded) {
            this.value = value;
            this.naked = naked;
            this.hooded = hooded;
        }

        public static ArmorIndex fromGender(int genderIndex) {
            switch(genderIndex) {
                case 1:
                    return NakedFemale;
                default:
                    return NakedMale;
            }
        }
    }

	public void setArmor(ArmorIndex armorIndex) {
        GLog.d("setArmor("+armorIndex+")");
		if (armorIndex.naked) {
            armorIndex = ArmorIndex.fromGender(heroGender);
		}
		if (this.armorIndex == armorIndex) {
			return;
		}

		this.armorIndex = armorIndex;

		updateSubSprites();
	}

    public void setCloak(ArmorIndex cloakIndex) {
        GLog.d("setCloak("+cloakIndex+")");
        if (this.cloakIndex == cloakIndex) {
            return;
        }

        this.cloakIndex = cloakIndex;


        updateSubSprites();
    }

	public void setHair(int hairIndex) {
		if (this.hairIndex == hairIndex) {
			return;
		}

		this.hairIndex = hairIndex;

		updateSubSprites();
	}

	public void setHairFace(int hairFaceIndex) {
		if (this.hairFaceIndex == hairFaceIndex) {
			return;
		}

		this.hairFaceIndex = hairFaceIndex;

		updateSubSprites();
	}

	public void setHairColor(int index) {
		switch(index) {
			case 0:
				rHair = 250.0f / 255.0f;
				gHair = 83.0f / 255.0f;
				bHair = 0.0f / 255.0f;
				break;
			case 1:
				rHair = 250.0f / 255.0f;
				gHair = 229.0f / 255.0f;
				bHair = 0.0f / 255.0f;
				break;
			case 2:
				rHair = 174.0f / 255.0f;
				gHair = 113.0f / 255.0f;
				bHair = 0.0f / 255.0f;
				break;
			case 3:
				rHair = 62.0f / 255.0f;
				gHair = 62.0f / 255.0f;
				bHair = 62.0f / 255.0f;
				break;
			default:
				rHair = 255.0f / 255.0f;
				gHair = 255.0f / 255.0f;
				bHair = 255.0f / 255.0f;
		}
	}

	private void updateGender() {
		TextureFilm film = new TextureFilm( tiers(), heroGender, FRAME_WIDTH, FRAME_HEIGHT );

        // 0 1 8 9 10 11 12

        if (heroGender == 1) {
            idle = new Animation(1, true);
            idle.frames(film, 14, 14, 14, 15, 14, 14, 15, 15);

            attack = new Animation(15, false);
            attack.frames(film, 16, 17, 18); // 8, 9, 10, 0

            operate = new Animation(8, false);
            operate.frames(film, 19, 20, 19, 20);
        } else {
            idle = new Animation(1, true);
            idle.frames(film, 0, 0, 0, 1, 0, 0, 1, 1);

            attack = new Animation(15, false);
            attack.frames(film, 8, 9, 10); // 8, 9, 10, 0

            operate = new Animation(8, false);
            operate.frames(film, 11, 12, 11, 12);
        }

        run = new Animation( RUN_FRAMERATE, true );
		run.frames( film, 2, 3, 4, 5, 6, 7 );

		die = new Animation( 20, false );
		die.frames( film, 0, 1, 2, 3, 4, 3 );

		zap = attack.clone();

		fly = new Animation( 1, true );
		fly.frames(film, 13);
	}

	private void updateSubSprites() {
		updateFrame();
		updateVertices();
	}

	@Override
	public void place( int p ) {
		super.place(p);
		Camera.main.target = this;
	}

	@Override
	public void move( int from, int to ) {
		super.move(from, to);
		if (ch.flying()) {
			play( fly );
		}
		Camera.main.target = this;
	}

	@Override
	public void jump( int from, int to, Callback callback ) {
		super.jump(from, to, callback);
		play(fly);
	}

	@Override
	public void update() {
		sleeping = ch != null && ch.isAlive() && ((Hero)ch).resting;

		super.update();
	}

	@Override
	public void die() {
		if (heroGender != 2) {
			heroGender = 2;
			updateGender();
		}
		super.die();
	}

	public boolean sprint( boolean on ) {
		run.delay = on ? 0.667f / RUN_FRAMERATE : 1f / RUN_FRAMERATE;
		return on;
	}

	public static TextureFilm tiers() {
		if (tiers == null) {
			SmartTexture texture = TextureCache.get( Assets.HERO_HEAD );
			tiers = new TextureFilm( texture, texture.width, FRAME_HEIGHT );
		}

		return tiers;
	}

	public static Image avatar( int heroIndex, int armorIndex ) {
		RectF patch = tiers().get( heroIndex );
		Image avatar = new Image( Assets.HERO_HEAD );
		RectF frame = avatar.texture.uvRect( 1, 0, FRAME_WIDTH, FRAME_HEIGHT );
		frame.offset( patch.left, patch.top );
		avatar.frame( frame );

		return avatar;
	}
}
