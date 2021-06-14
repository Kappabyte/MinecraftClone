package net.kappabyte.kappaengine.scenes.components;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import net.kappabyte.kappaengine.util.Log;
import net.kappabyte.kappaengine.window.Window;

public class Camera extends Component {

    private final float FOV = (float) Math.toRadians(60.0f);
    private final float Z_NEAR = 0.01f;
    private final float Z_FAR = 1000.f;

    private Matrix4f viewMatrix = new Matrix4f();

    private Matrix4f projectionMatrix, orthographicMatrix;

    private Window window;

    public Camera(Window window) {
        super();
        this.window = window;
        orthographicMatrix = new Matrix4f();
        updateProjectionMatrix();
    }

    public void updateProjectionMatrix() {
        float aspectRatio = ((float) window.getWidth()) / ((float) window.getHeight());
        Log.info("Window Ratio: " + aspectRatio);
        Log.info("Window Width: " + window.getWidth());
        Log.info("Window Height: " + window.getHeight());
        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onDestroy() {
    }

    public Matrix4f getViewMatrix() {
        viewMatrix.identity();

        viewMatrix.rotateXYZ((float) Math.toRadians(getTransform().getRotation().x), (float) Math.toRadians(getTransform().getRotation().y), (float) Math.toRadians(getTransform().getRotation().z));

        viewMatrix.translate(new Vector3f(getTransform().getPosition()).mul(-1));

        return viewMatrix;
    }

    public Matrix4f getOrthographicMatrix(Vector2f min, Vector2f max) {
        orthographicMatrix.identity();
        orthographicMatrix.setOrtho2D(min.x, max.x, max.y, min.y);
        return orthographicMatrix;
    }

}
