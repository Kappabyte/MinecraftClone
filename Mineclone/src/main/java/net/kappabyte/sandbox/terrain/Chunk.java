package net.kappabyte.sandbox.terrain;

import java.util.ArrayList;
import java.util.List;

import net.kappabyte.kappaengine.graphics.Mesh;
import net.kappabyte.kappaengine.graphics.NamedAsset;
import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.TextureAtlas;
import net.kappabyte.kappaengine.graphics.materials.UnitTexturedMaterial;
import net.kappabyte.kappaengine.physics.Collider;
import net.kappabyte.kappaengine.scenes.components.Renderable;
import net.kappabyte.sandbox.terrain.Block.BlockFace;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Chunk extends Renderable {

    Block[][][] blocks = new Block[16][256][16];

    Mesh chunkMesh;

    static TextureAtlas atlas = new TextureAtlas();
    static {
        atlas.setTextures(16, 16,
            new NamedAsset("grass_block_side", "assets/blocks/grass_block_side.png"),
            new NamedAsset("grass_block_top", "assets/blocks/grass_block_top.png"),
            new NamedAsset("north", "assets/blocks/north.png"),
            new NamedAsset("east", "assets/blocks/east.png"),
            new NamedAsset("south", "assets/blocks/south.png"),
            new NamedAsset("west", "assets/blocks/west.png"),
            new NamedAsset("top", "assets/blocks/top.png"),
            new NamedAsset("bottom", "assets/blocks/bottom.png"),
            new NamedAsset("dirt", "assets/blocks/dirt.png"),
            new NamedAsset("oak_log", "assets/blocks/oak_log.png"),
            new NamedAsset("oak_log_top", "assets/blocks/oak_log_top.png"),
            new NamedAsset("oak_leaves", "assets/blocks/oak_leaves.png"),
            new NamedAsset("birch_log", "assets/blocks/birch_log.png"),
            new NamedAsset("birch_log_top", "assets/blocks/birch_log_top.png"),
            new NamedAsset("birch_leaves", "assets/blocks/birch_leaves.png"),
            new NamedAsset("planks", "assets/blocks/planks.png"),
            new NamedAsset("stone", "assets/blocks/stone.png"));
    }

    public Chunk() {
        super(new UnitTexturedMaterial(atlas.getTexture()), false);
        buildChunk(null, null, null, null);
    }

    @Override
    protected RenderData supplyRenderData() {
        return new RenderData(getTransform(), chunkMesh, material, getGameObject());
    }

    public Block getBlockAt(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public List<Collider> getColldiers(Vector3f location, int chunkX, int chunkY) {
        int x = (int) (location.x % 16f);
        int y = (int) (location.y);
        int z = (int) (location.z % 16f);

        if(x < 0) x += 16;
        if(z < 0) z += 16;

        List<Collider> colliders = new ArrayList<Collider>();
        //Adding colliders around the player
        if(x >= 0 && x < blocks.length && y >= 0 && y < blocks[x].length && z >= 0 && z < blocks[x][y].length && blocks[x][y][z] != null) colliders.add(blocks[x][y][z].collider);
        if(x > 0 && x < blocks.length && y >= 0 && y < blocks[x].length && z >= 0 && z < blocks[x][y].length && blocks[x - 1][y][z] != null) colliders.add(blocks[x - 1][y][z].collider);
        if(x >= 0 && x < blocks.length - 1 && y >= 0 && y < blocks[x].length && z >= 0 && z < blocks[x][y].length && blocks[x + 1][y][z] != null) colliders.add(blocks[x + 1][y][z].collider);
        if(x >= 0 && x < blocks.length && y > 0 && y < blocks[x].length && z >= 0 && z < blocks[x][y].length && blocks[x][y - 1][z] != null) colliders.add(blocks[x][y - 1][z].collider);
        if(x >= 0 && x < blocks.length && y >= 0 && y < blocks[x].length - 1 && z >= 0 && z < blocks[x][y].length && blocks[x][y + 1][z] != null) colliders.add(blocks[x][y + 1][z].collider);
        if(x >= 0 && x < blocks.length && y >= 0 && y < blocks[x].length - 2 && z >= 0 && z < blocks[x][y].length && blocks[x][y + 2][z] != null) colliders.add(blocks[x][y + 2][z].collider);
        if(x >= 0 && x < blocks.length && y >= 0 && y < blocks[x].length && z > 0 && z < blocks[x][y].length && blocks[x][y][z - 1] != null) colliders.add(blocks[x][y][z - 1].collider);
        if(x >= 0 && x < blocks.length && y >= 0 && y < blocks[x].length && z >= 0 && z < blocks[x][y].length - 1 && blocks[x][y][z + 1] != null) colliders.add(blocks[x][y][z + 1].collider);

        return colliders;
    }

    public void setBlockAt(int x, int y, int z, Block block) {
        if(getGameObject() != null && blocks[x][y][z] != null) {
            getGameObject().removeComponent(blocks[x][y][z].collider);
        }
        blocks[x][y][z] = block;
        if(getGameObject() != null && block != null) {
            getGameObject().addComponent(block.collider);
        }
    }

    public int getMaxYAtBlock(int x, int z) {
        int maxY = 0;
        for(int y = 0; y < blocks[x].length; y++) {
            if(blocks[x][y][z] != null) {
                maxY = y;
            }
        }

        return maxY;
    }

    public void buildChunk(Chunk north, Chunk east, Chunk south, Chunk west) {
        ArrayList<Vector3f> verticies = new ArrayList<>();
        ArrayList<Integer> indicies = new ArrayList<>();
        ArrayList<Vector2f> uvs = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();

        for(int x = 0; x < blocks.length; x++) {
            for(int y = 0; y < blocks[x].length; y++) {
                for(int z = 0; z < blocks[x][y].length; z++) {
                    if(blocks[x][y][z] == null) continue;

                    if((x > 0 && blocks[x-1][y][z] == null) || (x == 0 && (west == null || west.blocks[15][y][z] == null))) {
                        addBlockFace(blocks[x][y][z], x,y,z, BlockFace.WEST, verticies, indicies, uvs, normals);
                    }
                    if((z < blocks[x][y].length - 1 && blocks[x][y][z + 1] == null) || (z == blocks[x][y].length - 1 && (north == null || north.blocks[x][y][0] == null))) {
                        addBlockFace(blocks[x][y][z], x,y,z, BlockFace.NORTH, verticies, indicies, uvs, normals);
                    }
                    if((x < blocks.length - 1 && blocks[x + 1][y][z] == null) || (x == blocks.length - 1 && (east == null || east.blocks[0][y][z] == null))) {
                        addBlockFace(blocks[x][y][z], x,y,z, BlockFace.EAST, verticies, indicies, uvs, normals);
                    }
                    if((z > 0 && blocks[x][y][z - 1] == null) || (z == 0 && (south == null || south.blocks[x][y][15] == null))) {
                        addBlockFace(blocks[x][y][z], x,y,z, BlockFace.SOUTH, verticies, indicies, uvs, normals);
                    }
                    if((y < blocks[x].length - 1 && blocks[x][y + 1][z] == null) || y == blocks[x].length - 1) {
                        addBlockFace(blocks[x][y][z], x,y,z, BlockFace.TOP, verticies, indicies, uvs, normals);
                    }
                    if((y > 0 && blocks[x][y - 1][z] == null) || y == 0) {
                        addBlockFace(blocks[x][y][z], x,y,z, BlockFace.BOTTOM, verticies, indicies, uvs, normals);
                    }
                }
            }
        }

        float[] vertexArray = new float[verticies.size() * 3];
        for(int i = 0; i < verticies.size(); i++) {
            Vector3f vertex = verticies.get(i);
            vertexArray[i * 3] = vertex.x;
            vertexArray[i * 3 + 1] = vertex.y;
            vertexArray[i * 3 + 2] = vertex.z;
        }

        int[] indexArray = (int[]) indicies.stream().mapToInt(i -> i).toArray();

        float[] uvArray = new float[uvs.size() * 2];
        for(int i = 0; i < uvs.size(); i++) {
            Vector2f uv = uvs.get(i);
            uvArray[i * 2] = uv.x;
            uvArray[i * 2 + 1] = uv.y;
        }

        float[] normalsArray = new float[normals.size() * 3];
        for(int i = 0; i < normals.size(); i++) {
            Vector3f normal = normals.get(i);
            normalsArray[i * 3] = normal.x;
            normalsArray[i * 3 + 1] = normal.y;
            normalsArray[i * 3 + 2] = normal.z;
        }

        chunkMesh = new Mesh(vertexArray, indexArray, normalsArray, uvArray);
    }

    private void addBlockFace(Block block, int x, int y, int z, BlockFace face, ArrayList<Vector3f> verticies, ArrayList<Integer> indicies, ArrayList<Vector2f> uvs, ArrayList<Vector3f> normals) {
        Vector3f[] vertexLocations = new Vector3f[4];

        Vector3f normal = new Vector3f();

        switch(face) {
            case WEST:
                vertexLocations[1] = new Vector3f(x, y, z);
                vertexLocations[0] = new Vector3f(x, y, z + 1);
                vertexLocations[3] = new Vector3f(x, y + 1, z);
                vertexLocations[2] = new Vector3f(x, y + 1, z + 1);
                normal = new Vector3f(-1, 0, 0);
                break;
            case NORTH:
                vertexLocations[0] = new Vector3f(x + 1, y, z + 1);
                vertexLocations[2] = new Vector3f(x + 1, y + 1, z + 1);
                vertexLocations[1] = new Vector3f(x, y, z + 1);
                vertexLocations[3] = new Vector3f(x, y + 1, z + 1);
                normal = new Vector3f(0, 0, 1);
                break;
            case EAST:
                vertexLocations[0] = new Vector3f(x + 1, y, z);
                vertexLocations[2] = new Vector3f(x + 1, y + 1, z);
                vertexLocations[1] = new Vector3f(x + 1, y, z + 1);
                vertexLocations[3] = new Vector3f(x + 1, y + 1, z + 1);
                normal = new Vector3f(1, 0, 0);
                break;
            case SOUTH:
                vertexLocations[0] = new Vector3f(x, y, z);
                vertexLocations[1] = new Vector3f(x + 1, y, z);
                vertexLocations[2] = new Vector3f(x, y + 1, z);
                vertexLocations[3] = new Vector3f(x + 1, y + 1, z);
                normal = new Vector3f(0, 0, -1);
                break;
            case TOP:
                vertexLocations[3] = new Vector3f(x + 1, y + 1, z);
                vertexLocations[1] = new Vector3f(x, y + 1, z);
                vertexLocations[2] = new Vector3f(x + 1, y + 1, z + 1);
                vertexLocations[0] = new Vector3f(x, y + 1, z + 1);
                normal = new Vector3f(0, 1, 0);
                break;
            case BOTTOM:
                vertexLocations[0] = new Vector3f(x + 1, y, z);
                vertexLocations[1] = new Vector3f(x, y, z);
                vertexLocations[2] = new Vector3f(x + 1, y, z + 1);
                vertexLocations[3] = new Vector3f(x, y, z + 1);
                normal = new Vector3f(0, -1, 0);
                break;
        }
        Vector2f[] atlasUVs = atlas.getAtlasTexture(block.material.getTexture(face)).getUVs(atlas.getWidth(), atlas.getHeight());

        int startIndex = verticies.size();

        for(int i = 0; i < vertexLocations.length; i++) {
            verticies.add(vertexLocations[i]);
            uvs.add(atlasUVs[i]);
            normals.add(normal);
        }

        indicies.add(startIndex);
        indicies.add(startIndex + 1);
        indicies.add(startIndex + 2);

        indicies.add(startIndex + 3);
        indicies.add(startIndex + 2);
        indicies.add(startIndex + 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        for(int x = 0; x < blocks.length; x++) {
            for(int y = 0; y < blocks[x].length; y++) {
                for(int z = 0; z < blocks[x][y].length; z++) {
                    if(blocks[x][y][z] != null) {
                        getGameObject().addComponent(blocks[x][y][z].collider);
                    }
                }
            }
        }
    }
}
