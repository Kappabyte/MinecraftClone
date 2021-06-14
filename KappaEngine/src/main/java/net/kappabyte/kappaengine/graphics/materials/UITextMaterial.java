package net.kappabyte.kappaengine.graphics.materials;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import net.kappabyte.kappaengine.graphics.FragmentShader;
import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.ShaderProgram;
import net.kappabyte.kappaengine.graphics.Texture;
import net.kappabyte.kappaengine.graphics.VertexShader;
import net.kappabyte.kappaengine.window.Window;

public class UITextMaterial extends TexturedMaterial {

    static ShaderProgram program = new ShaderProgram(new VertexShader(UITextMaterial.class.getResourceAsStream("/assets/shaders/uitext.vert")), new FragmentShader(UITextMaterial.class.getResourceAsStream("/assets/shaders/uitext.frag")));
    static {
        program.createUniform("colour");
        program.createUniform("projModelMatrix");
    }

    public UITextMaterial(Texture texture) {
        super(texture);
    }

    @Override
    public ShaderProgram getShaderProgram() {
        return program;
    }

    @Override
    public void render(RenderData data) {
        super.render(data);
        Window window = data.getGameObject().getScene().getWindow();
        int width = window.getWidth();
        int height = window.getHeight();
        program.setUniform("projModelMatrix", data.getTransform().getModelViewMatrix(data.getCamera().getOrthographicMatrix(new Vector2f(0, 0), new Vector2f(width, height))));
        program.setUniform("colour", new Vector4f(1, 1, 1, 1));
    }

}
