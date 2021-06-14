package net.kappabyte.kappaengine.graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import net.kappabyte.kappaengine.util.Log;

public class TextureAtlas {

    public Texture texture;

    private HashMap<String, AtlasTexture> textures = new HashMap<>();

    int width, height;

    public TextureAtlas() {

    }

    public TextureAtlas setTextures(int textureWidth, int textureHeight, NamedAsset... textureAssets) {
        PNGDecoder decoder;

        int atlasSize = (int) Math.ceil(Math.sqrt(textureAssets.length));

        width = textureWidth * atlasSize;
        height = textureHeight * atlasSize;

        textures.clear();

        for(int i = 0; i < textureAssets.length; i++) {
            int atlasX = i % atlasSize;
            int atlasY = i / atlasSize;
            try {
                decoder = new PNGDecoder(TextureAtlas.class.getResourceAsStream("/" + textureAssets[i].assetURL));

                if(decoder.getWidth() != textureWidth || decoder.getHeight() != textureHeight) {
                    Log.error("Tried to add a texture to an atlas that does not conform to the atlas size!");
                    continue;
                }

                ByteBuffer textureData = ByteBuffer.allocateDirect(4 * textureWidth * textureHeight);
                decoder.decode(textureData, 4 * textureWidth, Format.RGBA);
                textureData.flip();
                textures.put(textureAssets[i].assetName, new AtlasTexture(textureData, new Vector2i(atlasX * textureWidth, atlasY * textureHeight), new Vector2i(atlasX * textureWidth + textureWidth, atlasY * textureHeight + textureHeight), textureAssets[i].assetName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        texture = new Texture(null, atlasSize * textureWidth, atlasSize * textureHeight);
        GL30.glBindTexture(GL30.GL_TEXTURE0, texture.getGlTextureID());
        GL30.glActiveTexture(texture.getGlTextureID());
        //Add subtextures
        for(AtlasTexture texture : textures.values()) {
            GL30.glTexSubImage2D(GL30.GL_TEXTURE_2D, 0, texture.start.x, texture.start.y, texture.end.x - texture.start.x, texture.end.y - texture.start.y, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, texture.data);
        }
        //GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
        return this;
    }

    public Texture getTexture() {
        return texture;
    }

    public AtlasTexture getAtlasTexture(String name) {
        return textures.get(name);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static class AtlasTexture {
        Vector2i start, end;
        String name;
        ByteBuffer data;

        public AtlasTexture(ByteBuffer data, Vector2i start, Vector2i end, String name) {
            this.data = data;
            this.start = start;
            this.end = end;
            this.name = name;
        }

        public Vector2f[] getUVs(int totalWidth, int totalHeight) {
            float x1 = (float)start.x / (float)totalWidth;
            float y1 = (float)start.y / (float)totalHeight;
            float x2 = (float)end.x / (float)totalWidth;
            float y2 = (float)end.y / (float)totalHeight;

            return new Vector2f[] {
                new Vector2f(x2, y2),
                new Vector2f(x1, y2),
                new Vector2f(x2, y1),
                new Vector2f(x1, y1),
            };
        }
    }
}
