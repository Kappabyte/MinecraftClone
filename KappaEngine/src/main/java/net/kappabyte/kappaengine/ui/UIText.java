package net.kappabyte.kappaengine.ui;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import net.kappabyte.kappaengine.graphics.Font;
import net.kappabyte.kappaengine.graphics.Mesh;
import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.materials.UITextMaterial;

public class UIText extends UIRenderable {

    String text;
    Font font;

    Mesh mesh;

    public UIText(String text, Font font) {
        super(new UITextMaterial(font.getTexture()));
        this.text = text;
        this.font = font;

        generateMesh();
    }

    public void setText(String text) {
        this.text = text;
        generateMesh();
    }

    public void setFont(Font font) {
        this.font = font;
        generateMesh();
    }

    private void generateMesh() {
        ArrayList<Vector3f> vertices = new ArrayList<>();
        ArrayList<Vector2f> uvs = new ArrayList<>();
        float[] normals = new float[] {};
        ArrayList<Integer> indices = new ArrayList<>();

        float tileWidth = (float) font.getTexture().getWidth() / (float) font.getColumns();
        float tileHeight = (float) font.getTexture().getHeight() / (float) font.getRows();

        byte[] chars = text.getBytes(font.getCharset());

        for(int i = 0; i < chars.length; i++) {
            byte currentChar = chars[i];
            int col = currentChar % font.getColumns();
            int row = currentChar / font.getRows();

            // Top Left
            vertices.add(new Vector3f((float)i * tileWidth, 0.0f, 0.0f));
            uvs.add(new Vector2f((float) col / (float) font.getColumns()));
            uvs.add(new Vector2f((float) row / (float) font.getRows()));

            // Bottom Left
            vertices.add(new Vector3f((float)i * tileWidth, tileHeight, 0.0f));
            uvs.add(new Vector2f((float) col / (float) font.getColumns()));
            uvs.add(new Vector2f((float) (row + 1) / (float) font.getRows()));

            // Bottom Right
            vertices.add(new Vector3f((float)i * tileWidth + tileWidth, tileHeight, 0.0f));
            uvs.add(new Vector2f((float) (col + 1) / (float) font.getColumns()));
            uvs.add(new Vector2f((float) (row + 1) / (float) font.getRows()));

            // Top Right
            vertices.add(new Vector3f((float)i * tileWidth + tileWidth, 0.0f, 0.0f));
            uvs.add(new Vector2f((float) (col + 1) / (float) font.getColumns()));
            uvs.add(new Vector2f((float) row / (float) font.getRows()));

            indices.add(i * 4);
            indices.add(i * 4 + 1);
            indices.add(i * 4 + 2);
            indices.add(i * 4 + 3);
            indices.add(i * 4);
            indices.add(i * 4 + 2);
        }

        float[] vertexArray = new float[vertices.size() * 3];
        for(int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            vertexArray[i * 3] = vertex.x;
            vertexArray[i * 3 + 1] = vertex.y;
            vertexArray[i * 3 + 2] = vertex.z;
        }

        float[] uvArray = new float[uvs.size() * 2];
        for(int i = 0; i < vertices.size(); i++) {
            Vector2f uv = uvs.get(i);
            uvArray[i * 2] = uv.x;
            uvArray[i * 2 + 1] = uv.y;
        }

        int[] indiciesArray = indices.stream().mapToInt(i -> i).toArray();

        mesh = new Mesh(vertexArray, indiciesArray, normals, uvArray);
    }

    @Override
    protected RenderData supplyRenderData() {
        return new RenderData(getTransform(), mesh, material, getGameObject());
    }
}
