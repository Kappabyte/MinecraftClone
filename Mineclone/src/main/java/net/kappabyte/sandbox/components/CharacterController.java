package net.kappabyte.sandbox.components;

import org.joml.Matrix3f;
import org.joml.Vector3f;

import net.kappabyte.kappaengine.input.InputManager.Input;
import net.kappabyte.kappaengine.math.Time;
import net.kappabyte.kappaengine.physics.AABBCollider;
import net.kappabyte.kappaengine.physics.Collider;
import net.kappabyte.kappaengine.physics.Raycast;
import net.kappabyte.kappaengine.physics.Rigidbody;
import net.kappabyte.kappaengine.physics.Raycast.Ray;
import net.kappabyte.kappaengine.scenes.GameObject;
import net.kappabyte.kappaengine.scenes.components.Component;
import net.kappabyte.kappaengine.util.Log;
import net.kappabyte.sandbox.terrain.BlockMaterial;
import net.kappabyte.sandbox.terrain.World;

public class CharacterController extends Component {

    private float speed = 5;

    private float turnSpeed = 5000;

    GameObject cameraObject;

    Rigidbody rb;

    public CharacterController(GameObject cameraObject) {
        this.cameraObject = cameraObject;

    }

    @Override
    public void onStart() {
        rb = getGameObject().GetComponent(Rigidbody.class);
        rb.setColliderSupplier(() -> {
            return World.current.getCollidersAtCoords(getTransform().getPosition());
        });
    }

    float mouseButtonCooldown = 0;

    @Override
    public void onUpdate() {
        Vector3f displacement = new Vector3f();
        if(getWindow().getInputManager().held(Input.KEYBOARD_W)) displacement.add(new Vector3f(0, 0, -1).mul(speed).mul(Time.deltaTime()));
        if(getWindow().getInputManager().held(Input.KEYBOARD_S)) displacement.add(new Vector3f(0, 0, 1).mul(speed).mul(Time.deltaTime()));
        if(getWindow().getInputManager().held(Input.KEYBOARD_A)) displacement.add(new Vector3f(-1, 0, 0).mul(speed).mul(Time.deltaTime()));
        if(getWindow().getInputManager().held(Input.KEYBOARD_D)) displacement.add(new Vector3f(1, 0, 0).mul(speed).mul(Time.deltaTime()));
        if(getWindow().getInputManager().held(Input.KEYBOARD_SPACE) && rb.onGround) rb.applyForce(new Vector3f(0, 9.81f * 15, 0));

        if(getWindow().getInputManager().held(Input.MOUSE_LEFT_BUTTON) && mouseButtonCooldown <= 0) {
            Ray hit = Raycast.castRayFromScreenPoint(0, 0, 50, getScene(), rb);
            Log.info("Raycast: " + hit);
            if(hit != null) {
                AABBCollider collider = (AABBCollider) hit.getCollider();
                Vector3f block = collider.getMinAbsolute();
                World.current.setBlockAt((int) block.x, (int) block.y, (int) block.z, null);
            }

            mouseButtonCooldown = 0.25f;
        }

        if(getWindow().getInputManager().held(Input.MOUSE_RIGHT_BUTTON) && mouseButtonCooldown < 0) {
            Ray hit = Raycast.castRayFromScreenPoint(0, 0, 50, getScene(), rb);
            Log.info("Raycast: " + hit);
            if(hit != null) {
                AABBCollider collider = (AABBCollider) hit.getCollider();
                Vector3f block = collider.getMinAbsolute().add(hit.getNormal());
                World.current.setBlockAt((int) block.x, (int) block.y, (int) block.z, BlockMaterial.PLANKS);
            }

            mouseButtonCooldown = 0.25f;
        }
        mouseButtonCooldown -= Time.deltaTime();

        Matrix3f rotationMatrix = new Matrix3f();
        rotationMatrix.identity();
        rotationMatrix.rotateXYZ((float) -Math.toRadians(getTransform().getRotationLocal().x), (float) -Math.toRadians(getTransform().getRotationLocal().y), (float) Math.toRadians(getTransform().getRotationLocal().z));

        displacement.mul(rotationMatrix);

        // if(getWindow().getInputManager().held(Input.KEYBOARD_W)) rb.applyForce(new Vector3f(0, 0, -1).mul(speed));
        // if(getWindow().getInputManager().held(Input.KEYBOARD_S)) rb.applyForce(new Vector3f(0, 0, 1).mul(speed));
        // if(getWindow().getInputManager().held(Input.KEYBOARD_A)) rb.applyForce(new Vector3f(-1, 0, 0).mul(speed));
        // if(getWindow().getInputManager().held(Input.KEYBOARD_D)) rb.applyForce(new Vector3f(1, 0, 0).mul(speed));
        // if(getWindow().getInputManager().held(Input.KEYBOARD_SPACE)) rb.applyForce(new Vector3f(0, 1, 0).mul(speed));
        // if(getWindow().getInputManager().held(Input.KEYBOARD_LEFT_SHIFT)) rb.applyForce(new Vector3f(0, -1, 0).mul(speed));

        if(getWindow().getInputManager().getMouseDelta().length() > 0) {
            getTransform().addRotation(new Vector3f(0, 1, 0).mul((float) getWindow().getInputManager().getMouseDelta().x * Time.deltaTime() * turnSpeed));
            cameraObject.getTransform().addRotation(new Vector3f(1, 0, 0).mul((float) getWindow().getInputManager().getMouseDelta().y * Time.deltaTime() * turnSpeed));
        }


        //Clamp rotation
        float pitch = cameraObject.getTransform().getRotationLocal().x;
        if(pitch < -90 || pitch > 270) pitch = -90;
        if(pitch > 90) pitch = 90;

        cameraObject.getTransform().setRotation(new Vector3f(pitch, cameraObject.getTransform().getRotationLocal().y, cameraObject.getTransform().getRotationLocal().z));
        for(Collider collider : rb.getCollisions()) {
            if(!(collider instanceof AABBCollider)) continue;
            AABBCollider other = (AABBCollider) collider;
            rb.reactToCollision(other, displacement);
        }

        getTransform().addPosition(displacement);
    }

    @Override
    public void onDestroy() {
    }

}
