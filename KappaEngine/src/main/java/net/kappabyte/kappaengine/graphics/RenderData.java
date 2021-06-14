package net.kappabyte.kappaengine.graphics;

import net.kappabyte.kappaengine.graphics.materials.Material;
import net.kappabyte.kappaengine.scenes.GameObject;
import net.kappabyte.kappaengine.scenes.Transform;
import net.kappabyte.kappaengine.scenes.components.Camera;

public class RenderData {
    GameObject gameObject;
    Transform transform;
    Mesh mesh;
    Material material;

    public RenderData(Transform transform, Mesh mesh, Material material, GameObject gameObject) {
        this.transform = transform;
        this.mesh = mesh;

        this.material = material;

        this.gameObject = gameObject;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public ShaderProgram getShaderProgram() {
        return material.getShaderProgram();
    }

    public Material getMaterial() {
        return material;
    }

    public Camera getCamera() {
        return gameObject.getScene().getActiveCamera();
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public Transform getTransform() {
        return gameObject.getTransform();
    }
}
