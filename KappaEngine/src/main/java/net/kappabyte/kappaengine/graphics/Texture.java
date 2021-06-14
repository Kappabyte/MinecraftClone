package net.kappabyte.kappaengine.graphics;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import net.kappabyte.kappaengine.util.Log;

public class Texture {
    String assetName;
    int texture;

    int width, height;

    public Texture(String asset) {
        assetName = asset;
        PNGDecoder decoder = null;

        try {
			decoder = new PNGDecoder(Texture.class.getResourceAsStream("/" + asset));

            ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
            buf.flip();

            width = decoder.getWidth();
            height = decoder.getHeight();

            texture = GL30.glGenTextures();
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
            GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);

            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);

            GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buf);

            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
            Log.debug("Loaded Texture: " + asset);
		} catch (IOException e) {
			Log.error("Failed to load texture: " + asset);
            return;
		}
    }

    public Texture(ByteBuffer buf, int width, int height) {
        this.width = width;
        this.height = height;

        texture = GL30.glGenTextures();
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
        GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);

        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);

        Log.debug("Loading Raw Texture");
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width, height, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buf);

        GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
        Log.debug("Loaded Raw Texture");
    }

    public String getAssetName() {
        return assetName;
    }

    public int getGlTextureID() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
