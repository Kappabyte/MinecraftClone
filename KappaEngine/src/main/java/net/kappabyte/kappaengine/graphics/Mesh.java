package net.kappabyte.kappaengine.graphics;

public class Mesh {
    private float[] vertices;
    private float[] normals;
    private float[] uvs;
    private int[] indices;

    public Mesh(float[] vertices, int[] indices, float[] normals, float[] uvs) {
        this.vertices = vertices;
        this.indices = indices;
        this.normals = normals;
        this.uvs = uvs;
    }

    public void setVerticesAndIndices(float[] verticies, int[] indicies) {
        this.vertices = verticies;
        this.indices = indicies;
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public int[] getIndicies() {
        return indices;
    }

    public float[] getUVs() {
        return uvs;
    }
}
