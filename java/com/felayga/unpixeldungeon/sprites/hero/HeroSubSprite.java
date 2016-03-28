package com.felayga.unpixeldungeon.sprites.hero;

import android.graphics.RectF;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Quad;
import com.watabou.noosa.NoosaScript;

import java.nio.FloatBuffer;

/**
 * Created by HELLO on 3/24/2016.
 */
public class HeroSubSprite {
    private SmartTexture armorTexture;
    private float[] armorTextureVertices = new float[16];
    private FloatBuffer armorTextureVerticesBuffer = Quad.create();

    public HeroSubSprite(Object tx) {
        armorTexture = tx instanceof SmartTexture ? (SmartTexture)tx : TextureCache.get(tx);
    }

    public void refreshBuffer()
    {
        armorTextureVerticesBuffer.position(0);
        armorTextureVerticesBuffer.put(armorTextureVertices);
    }

    public void draw(NoosaScript script, float[] matrix, float rm, float gm, float bm, float am, float ra, float ga, float ba, float aa)
    {
        armorTexture.bind();
        script.uModel.valueM4(matrix);
        script.lighting(
                rm, gm, bm, am,
                ra, ga, ba, aa);

        script.drawQuad(armorTextureVerticesBuffer);
    }

    public void updateFrame(int armorIndex, RectF frame, boolean flipHorizontal, boolean flipVertical)
    {
        if (flipHorizontal) {
            armorTextureVertices[2]		= frame.right;
            armorTextureVertices[6]		= frame.left;
            armorTextureVertices[10]	= frame.left;
            armorTextureVertices[14]	= frame.right;
        } else {
            armorTextureVertices[2]		= frame.left;
            armorTextureVertices[6]		= frame.right;
            armorTextureVertices[10]	= frame.right;
            armorTextureVertices[14]	= frame.left;
        }

        float frameHeight = (float)HeroSprite.FRAME_HEIGHT / (float)armorTexture.height;
        float top = (float)armorIndex * frameHeight;
        float bottom = top + frameHeight;

        if (flipVertical) {
            armorTextureVertices[3]		= bottom;
            armorTextureVertices[7]		= bottom;
            armorTextureVertices[11]	= top;
            armorTextureVertices[15]	= top;
        } else {
            armorTextureVertices[3]		= top;
            armorTextureVertices[7]		= top;
            armorTextureVertices[11]	= bottom;
            armorTextureVertices[15]	= bottom;
        }
    }

    public void updateVertices(float width, float height)
    {
        armorTextureVertices[0] 	= 0;
        armorTextureVertices[1] 	= 0;

        armorTextureVertices[4] 	= width;
        armorTextureVertices[5] 	= 0;

        armorTextureVertices[8] 	= width;
        armorTextureVertices[9] 	= height;

        armorTextureVertices[12]	= 0;
        armorTextureVertices[13]	= height;
    }

}
