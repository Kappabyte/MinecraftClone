package net.kappabyte.kappaengine.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.joml.Vector2f;
import org.joml.Vector3f;

import net.kappabyte.kappaengine.util.Log;

public final class ModelLoader {
    public static Mesh loadOBJModel(String fileName) {
        Scanner reader = new Scanner(ModelLoader.class.getResourceAsStream("/" + fileName));
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        while(reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] tokens = line.split("\\s+");
            switch(tokens[0]) {
                case "v":
                    Vector3f vertex = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                    vertices.add(vertex);
                    break;
                case "vt":
                    Vector2f uv = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                    textures.add(uv);
                    break;
                case "vn":
                    Vector3f normal = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
                    normals.add(normal);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                    break;
                default:
                    break;
            }
        }

        reader.close();

        return generateMesh(vertices, textures, normals, faces);
    }

    private static class IndexGroup {

        public int indexPos = -1;
        public int indexUV = -1;
        public int indexNormal = -1;

        public IndexGroup() {

        }
    }

    private static class Face {
        private IndexGroup[] indexGroups;

        public Face(String v1, String v2, String v3) {
            indexGroups = new IndexGroup[3];

            indexGroups[0] = parseLine(v1);
            indexGroups[1] = parseLine(v2);
            indexGroups[2] = parseLine(v3);
        }

        private IndexGroup parseLine(String line) {
            IndexGroup group = new IndexGroup();
            String[] tokens = line.split("/");
            int length = tokens.length;
            group.indexPos = Integer.parseInt(tokens[0]) - 1;
            if(length > 1) {
                String textCoord = tokens[1];
                group.indexUV = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
                if(length > 2) {
                    group.indexNormal = Integer.parseInt(tokens[2]) - 1;
                }
            }

            return group;
        }

        public IndexGroup[] getFaceVertexIndices() {
            return indexGroups;
        }
    }

    private static Mesh generateMesh(List<Vector3f> positions, List<Vector2f> textureCoords, List<Vector3f> normals, List<Face> faces) {
        List<Integer> indices = new ArrayList<>();

        float[] positionsArr = new float[positions.size() * 3];
        float[] textureCoordsArr = new float[textureCoords.size() * 2];
        float[] normalsArr = new float[normals.size() * 3];

        for(int i = 0; i < positions.size(); i++) {
            Vector3f pos = positions.get(i);
            positionsArr[i * 3] = pos.x;
            positionsArr[i * 3 + 1] = pos.y;
            positionsArr[i * 3 + 2] = pos.z;
        }

        for(Face face : faces) {
            IndexGroup[] faceVertexIndicies = face.getFaceVertexIndices();
            for(IndexGroup index : faceVertexIndicies) {
                processFaceVertex(index, textureCoords, normals, indices, textureCoordsArr, normalsArr);
            }
        }
        int[] indicesArr = new int[indices.size()];
        indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        Mesh mesh = new Mesh(positionsArr, indicesArr, normalsArr, textureCoordsArr);
        return mesh;
    }

    private static void processFaceVertex(IndexGroup index, List<Vector2f> textureCoords, List<Vector3f> normals, List<Integer> indices, float[] textureCoordsArr, float[] normalsArr) {
        int positionIndex = index.indexPos;
        Log.debug("" + positionIndex);
        indices.add(positionIndex);

        if (index.indexUV >= 0) {
            Vector2f textCoord = textureCoords.get(index.indexUV);
            textureCoordsArr[positionIndex * 2] = textCoord.x;
            textureCoordsArr[positionIndex * 2 + 1] = 1 - textCoord.y;
        }
        if (index.indexNormal >= 0) {
            // Reorder vectornormals
            Vector3f vecNorm = normals.get(index.indexNormal);
            normalsArr[positionIndex * 3] = vecNorm.x;
            normalsArr[positionIndex * 3 + 1] = vecNorm.y;
            normalsArr[positionIndex * 3 + 2] = vecNorm.z;
        }
    }
}
