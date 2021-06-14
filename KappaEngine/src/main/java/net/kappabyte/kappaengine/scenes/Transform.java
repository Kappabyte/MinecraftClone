package net.kappabyte.kappaengine.scenes;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import net.kappabyte.kappaengine.scenes.components.Component;

public class Transform extends Component {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    private Matrix4f modelViewMatrix = new Matrix4f();

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f addPosition(Vector3f positionDelta) {
        position.add(positionDelta);
        return position;
    }

    public Vector3f addPositionLocal(Vector3f positionDelta) {
        Matrix3f rotationMatrix = new Matrix3f();
        rotationMatrix.identity();
        rotationMatrix.rotateXYZ((float) -Math.toRadians(rotation.x), (float) -Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));
        positionDelta.mul(rotationMatrix);
        position.add(positionDelta);
        return position;
    }

    public Vector3f addRotation(Vector3f rotationDelta) {
        rotation.add(rotationDelta.x % 360, rotationDelta.y % 360, rotationDelta.z % 360);
        return rotation;
    }

    public Vector3f addScale(Vector3f scaleDelta) {
        scale.add(scaleDelta);
        return scale;
    }

    public Vector3f getPositionLocal() {
        return position;
    }

    public Vector3f getPosition() {
        GameObject parent = getGameObject().getParentGameObject();

        if(parent == null) {
            return getPositionLocal();
        }

        return new Vector3f(parent.getTransform().getPosition()).add(position);
    }

    public Vector3f getRotationLocal() {
        return rotation;
    }

    public Vector3f getRotation() {
        GameObject parent = getGameObject().getParentGameObject();

        if(parent == null) {
            return getRotationLocal();
        }

        return new Vector3f(parent.getTransform().getRotation()).add(rotation);
    }

    public Vector3f getScaleLocal() {
        return scale;
    }

    public Vector3f getScale() {
        GameObject parent = getGameObject().getParentGameObject();

        if(parent == null) {
            return getScaleLocal();
        }

        return new Vector3f(parent.getTransform().getScale()).mul(scale);
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation.x % 360f, rotation.y % 360f, rotation.z % 360f);
    }

    public void setScale(Vector3f scale) {
        this.scale.set(position);
    }

    public Matrix4f getModelViewMatrix(Matrix4f viewMatrix) {
        Vector3f rotation = getRotation();
        modelViewMatrix.identity().translate(getPosition()).rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z)).scale(getScale());

        return new Matrix4f(viewMatrix).mul(modelViewMatrix);
    }

    public Matrix4f getModelViewMatrixNoRotation(Matrix4f viewMatrix) {
        modelViewMatrix.identity().translate(getPosition()).scale(getScale());

        return new Matrix4f(viewMatrix).mul(modelViewMatrix);
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
}
