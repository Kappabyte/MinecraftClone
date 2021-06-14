package net.kappabyte.kappaengine.physics;

import org.joml.Vector3f;

public class Force {
    public Vector3f force;
    public float time;

    public Force(Vector3f force, float time) {
        this.force = force;
        this.time = time;
    }

    public Force(Vector3f force) {
        this.force = force;
        this.time = 0f;
    }
}
