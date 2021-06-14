package net.kappabyte.kappaengine.graphics.materials;

import org.lwjgl.opengl.GL30;

import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.Texture;

public abstract class TexturedMaterial extends Material {

    private Texture texture;

    public TexturedMaterial(Texture texture) {
        this.texture = texture;

        createVBO("texCoord"); //Creates vbo for textureCoords

        getShaderProgram().createUniform("texture_sampler");
    }

	@Override
	public void setRenderData(RenderData data) {
        getShaderProgram().setUniform("texture_sampler", 0);
        fillVBODataFloat(data.getMesh().getUVs(), "texCoord", 2);
	}

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
	public void render(RenderData data) {
        // Activate first texture unit
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        // Bind the texture
        //Log.debug("GL Texture ID: " + texture.getGlTextureID());
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture.getGlTextureID());
	}

}
