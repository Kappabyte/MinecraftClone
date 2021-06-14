package net.kappabyte.kappaengine.graphics.materials;

import net.kappabyte.kappaengine.graphics.FragmentShader;
import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.ShaderProgram;
import net.kappabyte.kappaengine.graphics.VertexShader;

public class RainbowMaterial extends Material {

    static ShaderProgram program;

    static {
        program = new ShaderProgram(new VertexShader(RainbowMaterial.class.getResourceAsStream("/assets/shaders/rainbow.vert")), new FragmentShader(RainbowMaterial.class.getResourceAsStream("/assets/shaders/rainbow.frag")));
        program.createUniform("colour");
        program.createUniform("projectionMatrix");
        program.createUniform("modelViewMatrix");
    }

    public RainbowMaterial() {
    }

    float hue = 0f; //hue

    @Override
    public void setRenderData(RenderData data) {
        hue += 0.01f;
        if (hue > 1f) {
            hue = 0f;
        }
        float saturation = 1.0f; //saturation
        float brightness = 0.5f; //brightness

        float[] myRGBColor = hslColor(hue, saturation, brightness);
        float[] colours = new float[]{myRGBColor[0], myRGBColor[1], myRGBColor[2]};

        program.setUniform("colour", colours[0], colours[1], colours[2]);
    }

    @Override
    public ShaderProgram getShaderProgram() {
        return program;
    }

    private static float[] hslColor(float h, float s, float l) {
        float q, p, r, g, b;

        if (s == 0) {
            r = g = b = l; // achromatic
        } else {
            q = l < 0.5 ? (l * (1 + s)) : (l + s - l * s);
            p = 2 * l - q;
            r = hue2rgb(p, q, h + 1.0f / 3);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1.0f / 3);
        }
        return new float[]{r, g, b};
    }

    private static float hue2rgb(float p, float q, float h) {
        if (h < 0) {
            h += 1;
        }

        if (h > 1) {
            h -= 1;
        }

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }

        return p;
    }

    @Override
    public void render(RenderData data) {
    }

}
