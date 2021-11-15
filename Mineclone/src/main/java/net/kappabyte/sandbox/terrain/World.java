package net.kappabyte.sandbox.terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import lib.fastnoise.FastNoiseLite;
import lib.fastnoise.FastNoiseLite.NoiseType;
import net.kappabyte.kappaengine.physics.Collider;
import net.kappabyte.kappaengine.scenes.GameObject;
import net.kappabyte.kappaengine.scenes.components.Component;
import net.kappabyte.kappaengine.util.Log;

public class World extends Component {

    public static World current;

    int seed = new Random().nextInt(Integer.MAX_VALUE);

    FastNoiseLite noise;
    HashMap<Vector2i, Chunk> chunks = new HashMap<>();

    public World() {
        current = this;
        noise = new FastNoiseLite(seed);
        noise.SetNoiseType(NoiseType.Perlin);
    }

    public Chunk getChunkAt(int x, int y, boolean generate) {
        Vector2i chunkCoordinate = new Vector2i(x, y);
        if(!chunks.containsKey(chunkCoordinate)) {
            if(generate) {
                Chunk chunk = new Chunk();
                chunks.put(chunkCoordinate, chunk);

                int blockCount = 0;

                //Terrain Generation
                for(int x1 = 0; x1 < 16; x1++) {
                    for(int z1 = 0; z1 < 16; z1++) {
                        float height = (noise.GetNoise(x * 16 + x1, y * 16 + z1) + 1.0f) / 2.0f;
                        for(int y1 = 0; y1 < height * 40; y1++) {
                            //Log.info("Setting Block At: " + x1 + ", " + y1 + ", " + z1);
                            blockCount++;
                            BlockMaterial material;
                            if(y1 < height * 40 && y1 > height * 40 - 4) {
                                material = BlockMaterial.DIRT;
                            } else {
                                material = BlockMaterial.STONE;
                            }
                            chunk.setBlockAt(x1, y1, z1, new Block(new Vector3i(x1, y1, z1), material));
                        }
                        chunk.setBlockAt(x1, (int) (height * 40), z1, new Block(new Vector3i(x1, (int) (height * 40), z1), BlockMaterial.GRASS_BLOCK));
                    }
                }

                //Trees
                long chunkSeed = (x + x * y) * seed;
                Random chunkRandom = new Random(chunkSeed);
                int numberOfTrees = chunkRandom.nextInt(10) + 3;
                for(int i = 0; i < numberOfTrees; i++) {
                    int tx = chunkRandom.nextInt(16);
                    int tz = chunkRandom.nextInt(16);
                    int ty = chunk.getMaxYAtBlock(tx, tz);

                    BlockMaterial log = BlockMaterial.OAK_LOG;
                    BlockMaterial leaves = BlockMaterial.OAK_LEAVES;
                    if(chunkRandom.nextBoolean()) {
                        log = BlockMaterial.BIRCH_LOG;
                        leaves = BlockMaterial.BIRCH_LEAVES;
                    }

                    setBlockAt(x * 16 + tx, ty + 1, y * 16 + tz, log);
                    setBlockAt(x * 16 + tx, ty + 2, y * 16 + tz, log);
                    setBlockAt(x * 16 + tx, ty + 3, y * 16 + tz, log);
                    for(int x1 = -2; x1 < 3; x1++) {
                        for(int z1 = -2; z1 < 3; z1++) {
                            setBlockAt(x * 16 + tx + x1, ty + 4, y * 16 + tz + z1, leaves);
                            setBlockAt(x * 16 + tx + x1, ty + 5, y * 16 + tz + z1, leaves);
                            if(x1 > -2 && x1 < 3 && z1 > -2 && z1 < 3) {
                                setBlockAt(x * 16 + tx + x1, ty + 6, y * 16 + tz + z1, leaves);
                            }
                        }
                    }
                    setBlockAt(x * 16 + tx, ty + 4, y * 16 + tz, log);
                    setBlockAt(x * 16 + tx, ty + 5, y * 16 + tz, log);
                    setBlockAt(x * 16 + tx, ty + 6, y * 16 + tz, log);
                    setBlockAt(x * 16 + tx, ty + 7, y * 16 + tz - 1, leaves);
                    setBlockAt(x * 16 + tx - 1, ty + 7, y * 16 + tz, leaves);
                    setBlockAt(x * 16 + tx, ty + 7, y * 16 + tz + 1, leaves);
                    setBlockAt(x * 16 + tx + 1, ty + 7, y * 16 + tz, leaves);
                    setBlockAt(x * 16 + tx, ty + 7, y * 16 + tz, leaves);
                }

                Log.info("Done generating chunk. Total Blocks: " + blockCount);

                buildChunkAt(x, y, true);
                return chunk;
            } else {
                return null;
            }
        }

        return chunks.get(chunkCoordinate);
    }

    public void spawnChunk(int x, int y) {
        Chunk chunk = getChunkAt(x, y, true);
        GameObject chunkObject = new GameObject("Chunk @ " + x + ", " + y);
        chunkObject.addComponent(chunk);

        getGameObject().addChild(chunkObject);
        chunkObject.getTransform().setPosition(new Vector3f(x * 16, 0, y * 16));
    }

    public void setBlockAt(int x, int y, int z, BlockMaterial material) {
        int chunkX = x >> 4;
        int chunkY = z >> 4;

        Chunk chunk = getChunkAt(chunkX, chunkY, false);
        if(chunk != null) {
            try {
                if(material == null) {
                    chunk.setBlockAt(x % 16, y, z % 16, null);
                } else {
                    chunk.setBlockAt(x % 16, y, z % 16, new Block(new Vector3i(x % 16, y, z % 16), material));
                }
            } catch(IndexOutOfBoundsException e) {
                Log.info("Failed to set block at: " + x + ", " + y + ", "  + z);
            }
            buildChunkAt(chunkX, chunkY, true);
        }
    }

    public void buildChunkAt(int x, int y, boolean updateAdjacent) {
        Chunk north = chunks.get(new Vector2i(x, y + 1));
        Chunk east = chunks.get(new Vector2i(x + 1, y));
        Chunk south = chunks.get(new Vector2i(x, y - 1));
        Chunk west = chunks.get(new Vector2i(x - 1, y));

        if(updateAdjacent) {
            if(north != null) buildChunkAt(x, y + 1, false);
            if(east != null) buildChunkAt(x + 1, y, false);
            if(south != null) buildChunkAt(x, y - 1, false);
            if(west != null) buildChunkAt(x - 1, y, false);
        }

        Chunk center = chunks.get(new Vector2i(x, y));
        if(center != null) center.buildChunk(north, east, south, west);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate() {
        //getScene().printSceneHeirarchy(0, getScene().getChildren());
    }

    @Override
    public void onDestroy() {
    }

	public List<Collider> getCollidersAtCoords(Vector3f position) {
		int chunkX = (int) Math.floor(position.x / 16f);
        int chunkY = (int) Math.floor(position.z / 16f);

        Chunk chunk = getChunkAt(chunkX, chunkY, false);
        if(chunk != null) {
            return chunk.getColldiers(position, chunkX, chunkY);
        }

        return new ArrayList<Collider>();
	}
}
