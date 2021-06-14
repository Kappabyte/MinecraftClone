package net.kappabyte.sandbox.terrain;

import java.util.HashMap;

import net.kappabyte.sandbox.terrain.Block.BlockFace;

public enum BlockMaterial {
    GRASS_BLOCK("grass_block_side", "grass_block_side", "grass_block_side", "grass_block_side", "grass_block_top", "dirt"),
    TEST_BLOCK("north", "east", "south", "west", "top", "bottom"),
    DIRT("dirt"),
    OAK_LOG("oak_log", "oak_log", "oak_log", "oak_log", "oak_log_top", "oak_log_top"),
    OAK_LEAVES("oak_leaves"),
    BIRCH_LOG("birch_log", "birch_log", "birch_log", "birch_log", "birch_log_top", "birch_log_top"),
    BIRCH_LEAVES("birch_leaves"),
    PLANKS("planks"),
    STONE("stone");

    private HashMap<BlockFace,String> textureNames = new HashMap<>();

    private BlockMaterial(String textureName) {
        textureNames.put(BlockFace.NORTH, textureName);
        textureNames.put(BlockFace.EAST, textureName);
        textureNames.put(BlockFace.SOUTH, textureName);
        textureNames.put(BlockFace.WEST, textureName);
        textureNames.put(BlockFace.TOP, textureName);
        textureNames.put(BlockFace.BOTTOM, textureName);
    }

    /**
     *
     * @param textureNames NORTH, EAST, SOUTH, WEST, TOP, BOTTOM
     */
    private BlockMaterial(String... textureNames) {
        this.textureNames.put(BlockFace.NORTH, textureNames[0]);
        this.textureNames.put(BlockFace.EAST, textureNames[1]);
        this.textureNames.put(BlockFace.SOUTH, textureNames[2]);
        this.textureNames.put(BlockFace.WEST, textureNames[3]);
        this.textureNames.put(BlockFace.TOP, textureNames[4]);
        this.textureNames.put(BlockFace.BOTTOM, textureNames[5]);
    }

    public String getTexture(BlockFace face) {
        return textureNames.get(face);
    }
}
