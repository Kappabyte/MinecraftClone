package net.kappabyte.kappaengine.physics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import net.kappabyte.kappaengine.graphics.Mesh;
import net.kappabyte.kappaengine.util.Log;

public class AABBCollider extends Collider {

    Vector3f min;
    Vector3f max;

    public AABBCollider(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;

        //debugMesh = generateDebugMesh();
    }

    @Override
    public Mesh generateDebugMesh() {
        float[] verts = new float[] { min.x, min.y, min.z, max.x, min.y, min.z, min.x, max.y, min.z, min.x, min.y,
            max.z, max.x, max.y, min.z, max.x, min.y, max.z, min.x, max.y, max.z, max.x, max.y, max.z };

        int[] indicies = new int[] { 0, 1, 5, 0, 5, 3, 0, 4, 1, 0, 2, 4, 3, 5, 7, 3, 7, 6, 2, 7, 4, 2, 6, 7, 1, 7, 5, 1,
                4, 7, 3, 2, 0, 3, 6, 2 };

        float[] uvs = new float[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };

        float[] normals = new float[] { 0, 0, 1, 1, 0, 0, 0, 0, -1, -1, 0, 0, 0, 1, 0, 0, -1, 0 };

        return new Mesh(verts, indicies, normals, uvs);
    }

    @Override
    public boolean isColliding() {
        for (Collider other : getAllColliders()) {
            if (other == this)
                continue;
            if(!(other instanceof AABBCollider)) continue;
            AABBCollider otherAABB = (AABBCollider) other;
            if ((getMinAbsolute().x <= otherAABB.getMaxAbsolute().x && getMaxAbsolute().x >= otherAABB.getMinAbsolute().x)
                && (getMinAbsolute().y <= otherAABB.getMaxAbsolute().y
                && getMaxAbsolute().y >= otherAABB.getMinAbsolute().y)
                && (getMinAbsolute().z <= otherAABB.getMaxAbsolute().z
                && getMaxAbsolute().z >= otherAABB.getMinAbsolute().z))
                return true;
        }
        return false;
    }

    @Override
    public List<Collider> getCollisions() {
        return getCollisions(new Vector3f(), getAllColliders());
    }

    @Override
    public List<Collider> getCollisions(List<Collider> colliders) {
        return getCollisions(new Vector3f(), colliders);
    }

    @Override
    public List<Collider> getCollisions(Vector3f offset, List<Collider> colliders) {
        List<Collider> collisions = new ArrayList<Collider>();
        for (Collider other : colliders) {
            if (other == this)
                continue;
            if(!(other instanceof AABBCollider)) continue;
            AABBCollider otherAABB = (AABBCollider) other;
            Vector3f thisMin = getMinAbsolute();
            Vector3f thisMax = getMaxAbsolute();
            Vector3f otherMin = otherAABB.getMinAbsolute();
            Vector3f otherMax = otherAABB.getMaxAbsolute();
            if ((thisMin.x + offset.x <= otherMax.x && thisMax.x + offset.x >= otherMin.x)
              && (thisMin.y + offset.y <= otherMax.y && thisMax.y + offset.y >= otherMin.y)
              && (thisMin.z + offset.z <= otherMax.z && thisMax.z + offset.z >= otherMin.z))
                    collisions.add(other);
        }
        return collisions;
    }

    public Vector3f getMinAbsolute() {
        return new Vector3f(min).add(getTransform().getPosition());
    }

    public Vector3f getMaxAbsolute() {
        return new Vector3f(max).add(getTransform().getPosition());
    }

    public Vector3f getMin() {
        return min;
    }

    public Vector3f getMax() {
        return max;
    }

    public Vector3f getSize() {
        return new Vector3f(max).sub(min);
    }

    @Override
    public String toString() {
        return "Min: " + min + "(" + getMinAbsolute() + "), Max: " + max + "(" + getMaxAbsolute() + ")";
    }
}
