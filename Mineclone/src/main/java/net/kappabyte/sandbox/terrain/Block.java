package net.kappabyte.sandbox.terrain;

import org.joml.Vector3f;
import org.joml.Vector3i;

import net.kappabyte.kappaengine.physics.AABBCollider;

public class Block {

    Vector3i location;
    AABBCollider collider;
    BlockMaterial material;

    public Block(Vector3i location, BlockMaterial material) {
        this.location = location;
        this.material = material;

        collider = new AABBCollider(new Vector3f(location), new Vector3f(location).add(1, 1, 1));
    }

    public static enum BlockFace {
        NORTH, EAST, SOUTH, WEST, TOP, BOTTOM
    }
}
